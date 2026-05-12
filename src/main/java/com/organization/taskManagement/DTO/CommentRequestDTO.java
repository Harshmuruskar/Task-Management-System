package com.organization.taskManagement.DTO;

import lombok.Data;

@Data
public class CommentRequestDTO {
    private String text;
    private String userId;
    private String userName;
}
