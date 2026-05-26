package com.organization.taskManagement.Controller;

import com.organization.taskManagement.DTO.Request.EmployeeRegistrationRequestDTO;
import com.organization.taskManagement.DTO.Request.LoginRequestDTO;
import com.organization.taskManagement.DTO.Response.ApiResponseDTO;
import com.organization.taskManagement.DTO.Response.EmployeeRegistrationResponseDTO;
import com.organization.taskManagement.DTO.Response.LoginResponseDTO;
import com.organization.taskManagement.Services.AuthorizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173/")
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDTO<EmployeeRegistrationResponseDTO>> register(@Valid @RequestBody EmployeeRegistrationRequestDTO request) {
        EmployeeRegistrationResponseDTO response = authorizationService.registerEmployee(request);
        return ResponseEntity.ok(ApiResponseDTO.success("Employee registered successfully", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<LoginResponseDTO>> login(@Valid @RequestBody LoginRequestDTO request) {
        LoginResponseDTO response = authorizationService.login(request);
        return ResponseEntity.ok(ApiResponseDTO.success("Login successful", response));
    }

}
