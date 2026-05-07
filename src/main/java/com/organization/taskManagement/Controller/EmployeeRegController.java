package com.organization.taskManagement.Controller;


import com.organization.taskManagement.DTO.ApiResponse;
import com.organization.taskManagement.DTO.EmployeeRegistrationRequest;
import com.organization.taskManagement.DTO.EmployeeRegistrationResponse;
import com.organization.taskManagement.DTO.LoginRequest;
import com.organization.taskManagement.Services.EmployeeRegService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class EmployeeRegController {

    private final EmployeeRegService employeeRegService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<EmployeeRegistrationResponse>> register(@Valid @RequestBody EmployeeRegistrationRequest request) {
        try {
            EmployeeRegistrationResponse response = employeeRegService.registerEmployee(request);
            return ResponseEntity.ok(ApiResponse.success("Employee registered successfully", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.failure(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<EmployeeRegistrationResponse>> login(@Valid @RequestBody LoginRequest request) {
        try {
            EmployeeRegistrationResponse response = employeeRegService.login(request);
            return ResponseEntity.ok(ApiResponse.success("Login successful", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.failure(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<Page<EmployeeRegistrationResponse>> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sortBy);

        return ResponseEntity.ok(employeeRegService.getAllEmployees(pageable));
    }

    @DeleteMapping("/{id}")
   public ResponseEntity<ApiResponse<?>> deleteEmployee(@PathVariable Long id){
        employeeRegService.deleteEmployee(id);
        return ResponseEntity.ok(ApiResponse.success("Employee deleted successfully", null));
   }


}
