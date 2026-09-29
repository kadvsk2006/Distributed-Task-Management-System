package com.sentra.taskmanagement.dto.task;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequestDTO {

    @NotNull
    @Size(min = 3, max = 100)
    private String title;

    @Size(max = 500)
    private String description;

    @NotNull
    private String status;     // TODO, IN_PROGRESS, DONE

    @NotNull
    private String priority;   // LOW, MEDIUM, HIGH

    private LocalDate dueDate;

    private Long assignedToUserId;
}
