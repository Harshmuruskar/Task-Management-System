package com.organization.taskManagement.Mappers;

import com.organization.taskManagement.DTO.CommentRequest;
import com.organization.taskManagement.DTO.CommentResponse;
import com.organization.taskManagement.Model.EmployeeRegModel;
import com.organization.taskManagement.Model.Comment;
import com.organization.taskManagement.Model.Task;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class CommentMapper {

    public static Comment toEntity(CommentRequest request, Task task, EmployeeRegModel employee) {

        if (request == null) return null;

        return Comment.builder()
                .message(request.getText())
                .employeeId(employee)
                .task(task)
                .build();
    }

    public static CommentResponse toResponse(Comment comment) {

        if (comment == null) return null;

        CommentResponse response = new CommentResponse();
        response.setId(comment.getId() != null ? String.valueOf(comment.getId()) : null);
        response.setUserId(comment.getEmployeeId() != null ? comment.getEmployeeId().getEmployeeId() : null);
        response.setUserName(comment.getEmployeeId() != null ? comment.getEmployeeId().getName() : null);
        response.setText(comment.getMessage());
        response.setTimestamp(
                comment.getCreatedAt() == null
                        ? null
                        : comment.getCreatedAt()
                                .atZone(ZoneId.systemDefault())
                                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        );

        return response;
    }
}