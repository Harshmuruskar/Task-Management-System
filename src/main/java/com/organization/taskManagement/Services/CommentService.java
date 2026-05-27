package com.organization.taskManagement.Services;

import com.organization.taskManagement.DTO.CommentCreateResponse;
import com.organization.taskManagement.DTO.CommentRequest;
import com.organization.taskManagement.DTO.CommentResponse;
import com.organization.taskManagement.Mappers.CommentMapper;
import com.organization.taskManagement.Model.Comment;
import com.organization.taskManagement.Model.EmployeeRegModel;
import com.organization.taskManagement.Model.Task;
import com.organization.taskManagement.Repos.CommentRepository;
import com.organization.taskManagement.Repos.EmployeeRegRepo;
import com.organization.taskManagement.Repos.TaskRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;
	private final TaskRepo taskRepo;
	private final EmployeeRegRepo employeeRegRepo;

	public CommentCreateResponse addComment(Long taskId, CommentRequest request) {
		Task task = taskRepo.findById(taskId)
				.orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));

		EmployeeRegModel employee = employeeRegRepo.findByEmployeeId(request.getUserId())
				.orElseThrow(() -> new RuntimeException("Employee not found with ID: " + request.getUserId()));

		Comment comment = CommentMapper.toEntity(request, task, employee);
		Comment savedComment = commentRepository.save(comment);
		CommentResponse commentResponse = CommentMapper.toResponse(savedComment);

		return new CommentCreateResponse(commentResponse);
	}
}
