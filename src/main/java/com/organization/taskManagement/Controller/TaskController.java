package com.organization.taskManagement.Controller;

import com.organization.taskManagement.DTO.Request.TaskPatchRequest;
import com.organization.taskManagement.DTO.Request.TaskRequest;
import com.organization.taskManagement.DTO.Response.ApiResponse;
import com.organization.taskManagement.DTO.Response.TaskListResponse;
import com.organization.taskManagement.DTO.Response.TaskResponse;
import com.organization.taskManagement.DTO.Response.TaskUpdateResponse;
import com.organization.taskManagement.Services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TaskController {


    private final TaskService taskService;

    @PostMapping("/task")
    public ResponseEntity<ApiResponse<TaskResponse>> createTask(@RequestBody TaskRequest taskRequest) {
        TaskResponse taskResponse = taskService.createTask(taskRequest);
        return ResponseEntity.ok(ApiResponse.success("Task created successfully", taskResponse));
    }

    @GetMapping("/tasks")
    public ResponseEntity<ApiResponse<TaskListResponse>> getTasks(
            @RequestParam(required = false) String team,
            @RequestParam(required = false) String status
    ) {
        TaskListResponse response = new TaskListResponse(taskService.getTasks(team, status));
        return ResponseEntity.ok(ApiResponse.success("", response));
    }

    @PatchMapping("/tasks/{id}")
    public ResponseEntity<ApiResponse<TaskUpdateResponse>> updateTask(
            @PathVariable Long id,
            @RequestBody TaskPatchRequest request
    ) {
        TaskUpdateResponse response = taskService.updateTask(id, request);
        return ResponseEntity.ok(ApiResponse.success("Task updated successfully", response));

    }

}
