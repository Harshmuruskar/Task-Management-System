package com.organization.taskManagement.DTO;

import lombok.Data;

@Data
public class CommentResponseDTO {
    private String id;
    private String userId;
    private String userName;
    private String text;
    private String timestamp;
}
