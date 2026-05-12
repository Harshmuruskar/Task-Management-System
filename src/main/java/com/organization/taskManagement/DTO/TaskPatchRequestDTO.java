package com.organization.taskManagement.DTO;

import com.organization.taskManagement.Enums.TaskStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskPatchRequestDTO {
    private String title;
    private String description;
    private String assignedToId;
    private LocalDate dueDate;
    private TaskStatus status;
}

