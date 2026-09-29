package com.sentra.taskmanagement.controller;

import com.sentra.taskmanagement.dto.task.TaskRequestDTO;
import com.sentra.taskmanagement.entity.Task;
import com.sentra.taskmanagement.enums.TaskStatus;
import com.sentra.taskmanagement.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @PostMapping
    public Task createTask(@Valid @RequestBody TaskRequestDTO dto) {
        return service.createTask(dto);
    }
    
    @GetMapping
    public Page<Task> getAllTasks(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return service.getAllTasks(status, priority, userId, page, size);
    }

    
    @GetMapping("/{id}")
    public Task getTask(@PathVariable Long id) {
        return service.getTask(id);
    }


    @PatchMapping("/{id}/status")
    public ResponseEntity<Task> updateTaskStatus(
            @PathVariable Long id,
            @RequestParam TaskStatus status) {

        Task task = service.updateStatus(id, status);
        return ResponseEntity.ok(task);
    }


    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        service.deleteTask(id);
    }
    @PutMapping("/{id}")
    public org.springframework.http.ResponseEntity<Task> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequestDTO dto) {

        var result = service.upsertTask(id, dto);

        if (result.created()) {
            java.net.URI location = java.net.URI.create("/api/tasks/" + result.task().getId());
            return org.springframework.http.ResponseEntity.created(location).body(result.task());
        } else {
            return org.springframework.http.ResponseEntity.ok(result.task());
        }
    }

}

