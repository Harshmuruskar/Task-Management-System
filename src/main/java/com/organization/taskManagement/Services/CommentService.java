package com.organization.taskManagement.Services;

import com.organization.taskManagement.DTO.CommentCreateResponseDTO;
import com.organization.taskManagement.DTO.CommentRequestDTO;
import com.organization.taskManagement.DTO.CommentResponseDTO;
import com.organization.taskManagement.Mappers.CommentMapper;
import com.organization.taskManagement.Model.Comment;
import com.organization.taskManagement.Model.EmployeeRegModel;
import com.organization.taskManagement.Model.Task;
import com.organization.taskManagement.Repository.CommentRepository;
import com.organization.taskManagement.Repository.EmployeeRegisterRepository;
import com.organization.taskManagement.Repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;
	private final TaskRepository taskRepo;
	private final EmployeeRegisterRepository employeeRegRepo;

	public CommentCreateResponseDTO addComment(Long taskId, CommentRequestDTO request) {
		Task task = taskRepo.findById(taskId)
				.orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));

		EmployeeRegModel employee = employeeRegRepo.findByEmployeeId(request.getUserId())
				.orElseThrow(() -> new RuntimeException("Employee not found with ID: " + request.getUserId()));

		Comment comment = CommentMapper.toEntity(request, task, employee);
		Comment savedComment = commentRepository.save(comment);
		CommentResponseDTO commentResponse = CommentMapper.toResponse(savedComment);

		return new CommentCreateResponseDTO(commentResponse);
	}
}
