package com.organization.taskManagement.Controller;


import com.organization.taskManagement.DTO.ApiResponseDTO;
import com.organization.taskManagement.DTO.EmployeeRegistrationRequestDTO;
import com.organization.taskManagement.DTO.EmployeeRegistrationResponseDTO;
import com.organization.taskManagement.DTO.LoginRequestDTO;
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
@RequestMapping
@RequiredArgsConstructor
public class EmployeeRegisterController {

    private final EmployeeRegService employeeRegService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDTO<EmployeeRegistrationResponseDTO>> register(@Valid @RequestBody EmployeeRegistrationRequestDTO request) {
        try {
            EmployeeRegistrationResponseDTO response = employeeRegService.registerEmployee(request);
            return ResponseEntity.ok(ApiResponseDTO.success("Employee registered successfully", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponseDTO.failure(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<EmployeeRegistrationResponseDTO>> login(@Valid @RequestBody LoginRequestDTO request) {
        try {
            EmployeeRegistrationResponseDTO response = employeeRegService.login(request);
            return ResponseEntity.ok(ApiResponseDTO.success("Login successful", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponseDTO.failure(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<Page<EmployeeRegistrationResponseDTO>> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sortBy);

        return ResponseEntity.ok(employeeRegService.getAllEmployees(pageable));
    }

    @DeleteMapping("/{id}")
   public ResponseEntity<ApiResponseDTO<?>> deleteEmployee(@PathVariable Long id){
        employeeRegService.deleteEmployee(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Employee deleted successfully", null));
   }
}
