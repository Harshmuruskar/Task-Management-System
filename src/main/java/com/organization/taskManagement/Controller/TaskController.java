package com.organization.taskManagement.Controller;

import com.organization.taskManagement.DTO.ApiResponseDTO;
import com.organization.taskManagement.DTO.TaskListResponseDTO;
import com.organization.taskManagement.DTO.TaskPatchRequestDTO;
import com.organization.taskManagement.DTO.TaskRequestDTO;
import com.organization.taskManagement.DTO.TaskResponseDTO;
import com.organization.taskManagement.DTO.TaskUpdateResponseDTO;
import com.organization.taskManagement.Services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class TaskController {


    private final TaskService taskService;

    @PostMapping("/task")
    public ResponseEntity<ApiResponseDTO<TaskResponseDTO>> createTask(@RequestBody TaskRequestDTO taskRequest) {
        try {
            TaskResponseDTO taskResponse = taskService.createTask(taskRequest);
            return ResponseEntity.ok(ApiResponseDTO.success("Task created successfully", taskResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponseDTO.failure(e.getMessage()));
        }
    }

    @GetMapping("/tasks")
    public ResponseEntity<ApiResponseDTO<TaskListResponseDTO>> getTasks(
            @RequestParam(required = false) String team,
            @RequestParam(required = false) String status
    ) {
        try {
            TaskListResponseDTO response = new TaskListResponseDTO(taskService.getTasks(team, status));
            return ResponseEntity.ok(ApiResponseDTO.success("", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponseDTO.failure(e.getMessage()));
        }
    }

    @PatchMapping("/tasks/{id}")
    public ResponseEntity<ApiResponseDTO<TaskUpdateResponseDTO>> updateTask(
            @PathVariable Long id,
            @RequestBody TaskPatchRequestDTO request
    ) {
        try {
            TaskUpdateResponseDTO response = taskService.updateTask(id, request);
            return ResponseEntity.ok(ApiResponseDTO.success("Task updated successfully", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponseDTO.failure(e.getMessage()));
        }
    }
}
