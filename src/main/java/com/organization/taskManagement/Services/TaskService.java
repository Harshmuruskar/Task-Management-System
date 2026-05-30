package com.organization.taskManagement.Services;

import com.organization.taskManagement.DTO.Request.TaskPatchRequest;
import com.organization.taskManagement.DTO.Request.TaskRequest;
import com.organization.taskManagement.DTO.Response.TaskResponse;
import com.organization.taskManagement.DTO.Response.TaskUpdateResponse;
import com.organization.taskManagement.Enums.TaskStatus;
import com.organization.taskManagement.Mappers.TaskMapper;
import com.organization.taskManagement.Model.EmployeeRegisterModel;
import com.organization.taskManagement.Model.TaskModel;
import com.organization.taskManagement.Repository.EmployeeRegisterRepository;
import com.organization.taskManagement.Repository.TaskRepository;
import com.organization.taskManagement.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

// Service for managing task operations
@Service
@RequiredArgsConstructor
public class TaskService {

	private final TaskRepository taskRepo;

	private final EmployeeRegisterRepository employeeRegRepo;

	public TaskResponse createTask(TaskRequest taskRequest) {
        EmployeeRegisterModel employeeRegModel = null;

		if(taskRequest.getAssignedToId() != null){
             employeeRegModel = employeeRegRepo.findByEmployeeId(taskRequest.getAssignedToId())
				.orElseThrow(() -> new ResourceNotFoundException("Employee", "ID", taskRequest.getAssignedToId()));
        }

		TaskModel task = TaskMapper.toEntity(taskRequest, employeeRegModel);
		TaskModel savedTask = taskRepo.save(task);

		return TaskMapper.toResponse(savedTask);
	}

	public TaskUpdateResponse updateTask(Long id, TaskPatchRequest patchRequest) {
		TaskModel task = taskRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Task", id));

		if (patchRequest.getTitle() != null) {
			task.setTitle(patchRequest.getTitle());
		}
		if (patchRequest.getDescription() != null) {
			task.setDescription(patchRequest.getDescription());
		}
		if (patchRequest.getDueDate() != null) {
			task.setDueDate(patchRequest.getDueDate());
		}
		if (patchRequest.getStatus() != null) {
			task.setStatus(patchRequest.getStatus());
		}
		if (patchRequest.getAssignedToId() != null) {
			EmployeeRegisterModel employeeRegModel = employeeRegRepo.findByEmployeeId(patchRequest.getAssignedToId())
					.orElseThrow(() -> new ResourceNotFoundException("Employee", "ID", patchRequest.getAssignedToId()));
			task.setAssignedTo(employeeRegModel);
		}

		TaskModel savedTask = taskRepo.save(task);
		return new TaskUpdateResponse(TaskMapper.toResponse(savedTask));
	}

	public List<TaskResponse> getTasks(String team, String status) {
		String parsedTeam = (team == null || team.isBlank()) ? null : team;
		TaskStatus parsedStatus = TaskStatus.fromValue(status);

		return taskRepo.findAll()
				.stream()
				.filter(task -> parsedTeam == null || (task.getTeamId() != null && task.getTeamId().equalsIgnoreCase(parsedTeam)))
				.filter(task -> parsedStatus == null || task.getStatus() == parsedStatus)
				.map(TaskMapper::toResponse)
                .collect(Collectors.toList());
	}

	/*public TaskResponse getTaskById(Long id) {
        Task task = taskRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + id));
        return TaskMapper.toResponse(task);
    }*/
}
