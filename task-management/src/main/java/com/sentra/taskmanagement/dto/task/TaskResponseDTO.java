package com.sentra.taskmanagement.dto.task;

import com.sentra.taskmanagement.enums.TaskPriority;
import com.sentra.taskmanagement.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponseDTO {

    private Long id;

    private String title;

    private String description;

    private TaskStatus status;

    private TaskPriority priority;

    private LocalDate dueDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Assigned user details (nullable)
    private Long assignedUserId;

    private String assignedUserName;

    private String assignedUserEmail;
}
