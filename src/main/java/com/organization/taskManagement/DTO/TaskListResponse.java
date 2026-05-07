package com.organization.taskManagement.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TaskListResponse {
    private List<TaskResponse> tasks;
}
