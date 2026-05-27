package com.organization.taskManagement.Services;

import com.organization.taskManagement.DTO.TaskRequest;
import com.organization.taskManagement.DTO.TaskPatchRequest;
import com.organization.taskManagement.DTO.TaskResponse;
import com.organization.taskManagement.DTO.TaskUpdateResponse;
import com.organization.taskManagement.Enums.TaskStatus;
import com.organization.taskManagement.Mappers.TaskMapper;
import com.organization.taskManagement.Model.EmployeeRegModel;
import com.organization.taskManagement.Model.Task;
import com.organization.taskManagement.Repos.EmployeeRegRepo;
import com.organization.taskManagement.Repos.TaskRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

// Service for managing task operations
@Service
@RequiredArgsConstructor
public class TaskService {

	private final TaskRepo taskRepo;

	private final EmployeeRegRepo employeeRegRepo;

	public TaskResponse createTask(TaskRequest taskRequest) {

		EmployeeRegModel employeeRegModel = employeeRegRepo.findByEmployeeId(taskRequest.getAssignedToId())
				.orElseThrow(() -> new RuntimeException("Employee not found with ID: " + taskRequest.getAssignedToId()));

		Task task = Task.builder()
				.title(taskRequest.getTitle())
				.description(taskRequest.getDescription())
				.teamId(taskRequest.getTeamId())
				.assignedTo(employeeRegModel)
				.dueDate(taskRequest.getDueDate())
				.updatedAt(Instant.now())
				.status(TaskStatus.NEW)
				.build();

		Task savedTask = taskRepo.save(task);

		return TaskMapper.toResponse(savedTask);
	}

	public TaskUpdateResponse updateTask(Long id, TaskPatchRequest patchRequest) {
		Task task = taskRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("Task not found with ID: " + id));

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
			EmployeeRegModel employeeRegModel = employeeRegRepo.findByEmployeeId(patchRequest.getAssignedToId())
					.orElseThrow(() -> new RuntimeException("Employee not found with ID: " + patchRequest.getAssignedToId()));
			task.setAssignedTo(employeeRegModel);
		}

		Task savedTask = taskRepo.save(task);
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
