package com.organization.taskManagement.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = "EmployeeId is required")
    private String employeeId;

    @NotBlank(message = "Password is required")
    private String password;


}
