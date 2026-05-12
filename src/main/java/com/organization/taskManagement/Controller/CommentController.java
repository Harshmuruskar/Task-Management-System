package com.organization.taskManagement.Controller;

import com.organization.taskManagement.DTO.ApiResponseDTO;
import com.organization.taskManagement.DTO.CommentCreateResponseDTO;
import com.organization.taskManagement.DTO.CommentRequestDTO;
import com.organization.taskManagement.Services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;

	@PostMapping("/{id}/comments")
	public ResponseEntity<ApiResponseDTO<CommentCreateResponseDTO>> addComment(
			@PathVariable Long id,
			@RequestBody CommentRequestDTO request
	) {
		try {
			CommentCreateResponseDTO response = commentService.addComment(id, request);
			return ResponseEntity.ok(ApiResponseDTO.success("Comment added successfully", response));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(ApiResponseDTO.failure(e.getMessage()));
		}
	}
}
