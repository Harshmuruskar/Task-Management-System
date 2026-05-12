package com.organization.taskManagement.DTO;

import com.organization.taskManagement.Enums.TaskStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskResponseDTO {
    private String id;
    private String title;
    private TaskStatus status;
    private String assignedTo;
    private LocalDate dueDate;
}
