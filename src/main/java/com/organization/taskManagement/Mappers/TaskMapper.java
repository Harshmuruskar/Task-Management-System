package com.organization.taskManagement.Mappers;

import com.organization.taskManagement.DTO.TaskRequest;
import com.organization.taskManagement.DTO.TaskResponse;
import com.organization.taskManagement.Enums.TaskStatus;
import com.organization.taskManagement.Model.EmployeeRegModel;
import com.organization.taskManagement.Model.Task;

// Mapper for converting Task DTOs to/from entities
public class TaskMapper {

    public static Task toEntity(TaskRequest request, EmployeeRegModel employee) {

        if (request == null) return null;

        return Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .teamId(request.getTeamId())
                .dueDate(request.getDueDate())
                .assignedTo(employee)
                .status(request.getStatus() != null ? request.getStatus() : TaskStatus.NEW)
                .build();
    }

    public static TaskResponse toResponse(Task task) {

        if (task == null) return null;

        TaskResponse response = new TaskResponse();
        response.setId(task.getId() != null ? String.valueOf(task.getId()) : null);
        response.setTitle(task.getTitle());
        response.setStatus(task.getStatus());
        response.setAssignedTo(task.getAssignedTo() != null ? task.getAssignedTo().getEmployeeId() : null);
        response.setDueDate(task.getDueDate());

        return response;
    }
}