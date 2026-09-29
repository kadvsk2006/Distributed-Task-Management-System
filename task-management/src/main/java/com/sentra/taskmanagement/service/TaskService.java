package com.sentra.taskmanagement.service;

import com.sentra.taskmanagement.dto.task.TaskRequestDTO;
import com.sentra.taskmanagement.entity.Task;
import com.sentra.taskmanagement.entity.User;
import com.sentra.taskmanagement.enums.TaskPriority;
import com.sentra.taskmanagement.enums.TaskStatus;
import com.sentra.taskmanagement.exception.ResourceNotFoundException;
import com.sentra.taskmanagement.repository.TaskRepository;
import com.sentra.taskmanagement.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@Service
public class TaskService {

    private final TaskRepository taskRepo;
    private final UserRepository userRepo;

    public TaskService(TaskRepository taskRepo, UserRepository userRepo) {
        this.taskRepo = taskRepo;
        this.userRepo = userRepo;
    }

    public Page<Task> getAllTasks(TaskStatus status,
            String priority,
            Long userId,
            int page,
            int size) {

        Pageable pageable = PageRequest.of(page, size);

        Specification<Task> spec = Specification.where(null);

        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }

        // convert priority string to enum before comparing
        if (priority != null) {
            TaskPriority pEnum;
            try {
                pEnum = TaskPriority.valueOf(priority.trim().toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("Invalid priority value. Allowed: LOW, MEDIUM, HIGH");
            }
            spec = spec.and((root, query, cb) -> cb.equal(root.get("priority"), pEnum));
        }

        if (userId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("assignedTo").get("id"), userId));
        }

        return taskRepo.findAll(spec, pageable);
    }

    public Task createTask(TaskRequestDTO dto) {

        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());

        // normalize and validate priority
        if (dto.getPriority() == null) {
            throw new IllegalArgumentException("Priority is required and must be one of: LOW, MEDIUM, HIGH");
        }
        try {
            task.setPriority(TaskPriority.valueOf(dto.getPriority().trim().toUpperCase()));
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid priority value. Allowed: LOW, MEDIUM, HIGH");
        }

        task.setDueDate(dto.getDueDate());

        // normalize and validate status
        if (dto.getStatus() == null) {
            throw new IllegalArgumentException("Status is required and must be one of: TODO, IN_PROGRESS, DONE");
        }
        try {
            task.setStatus(TaskStatus.valueOf(dto.getStatus().trim().toUpperCase()));
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid status value. Allowed: TODO, IN_PROGRESS, DONE");
        }

        if (dto.getAssignedToUserId() != null) {
            User user = userRepo.findById(dto.getAssignedToUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            task.setAssignedTo(user);
        }

        return taskRepo.save(task);
    }

    public Task getTask(Long id) {
        return taskRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    }

    public boolean existsById(Long id) {
        return taskRepo.existsById(id);
    }


    public Task updateStatus(Long id, TaskStatus status) {

        Task task = taskRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        task.setStatus(status);
        return taskRepo.save(task);
    }


    public void deleteTask(Long id) {
        taskRepo.delete(getTask(id));
    }


    public record UpsertResult(Task task, boolean created) {
    }

    public UpsertResult upsertTask(Long id, TaskRequestDTO dto) {
        return taskRepo.findById(id).map(task -> {
            // update fields
            task.setTitle(dto.getTitle());
            task.setDescription(dto.getDescription());

            if (dto.getStatus() != null) {
                try {
                    task.setStatus(TaskStatus.valueOf(dto.getStatus().trim().toUpperCase()));
                } catch (IllegalArgumentException ex) {
                    throw new IllegalArgumentException("Invalid status value. Allowed: TODO, IN_PROGRESS, DONE");
                }
            }

            if (dto.getPriority() != null) {
                try {
                    task.setPriority(TaskPriority.valueOf(dto.getPriority().trim().toUpperCase()));
                } catch (IllegalArgumentException ex) {
                    throw new IllegalArgumentException("Invalid priority value. Allowed: LOW, MEDIUM, HIGH");
                }
            }

            task.setDueDate(dto.getDueDate());

            if (dto.getAssignedToUserId() != null) {
                User user = userRepo.findById(dto.getAssignedToUserId())
                        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
                task.setAssignedTo(user);
            } else {
                task.setAssignedTo(null);
            }

            Task saved = taskRepo.save(task);
            return new UpsertResult(saved, false);
        }).orElseGet(() -> {
            // create new
            Task newTask = new Task();
            newTask.setTitle(dto.getTitle());
            newTask.setDescription(dto.getDescription());

            // priority (required)
            if (dto.getPriority() == null) {
                throw new IllegalArgumentException("Priority is required and must be one of: LOW, MEDIUM, HIGH");
            }
            try {
                newTask.setPriority(TaskPriority.valueOf(dto.getPriority().trim().toUpperCase()));
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("Invalid priority value. Allowed: LOW, MEDIUM, HIGH");
            }

            newTask.setDueDate(dto.getDueDate());

            // status (required)
            if (dto.getStatus() == null) {
                throw new IllegalArgumentException("Status is required and must be one of: TODO, IN_PROGRESS, DONE");
            }
            try {
                newTask.setStatus(TaskStatus.valueOf(dto.getStatus().trim().toUpperCase()));
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("Invalid status value. Allowed: TODO, IN_PROGRESS, DONE");
            }

            if (dto.getAssignedToUserId() != null) {
                User user = userRepo.findById(dto.getAssignedToUserId())
                        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
                newTask.setAssignedTo(user);
            }

            Task saved = taskRepo.save(newTask);
            return new UpsertResult(saved, true);
        });
    }

    public Task updateTask(Long id, TaskRequestDTO dto) {
        // Delegate to atomic upsert to avoid duplicate/incorrect repository references
        UpsertResult result = upsertTask(id, dto);
        return result.task();
    }

}
