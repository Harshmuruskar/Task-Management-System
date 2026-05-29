package com.organization.taskManagement.Controller;


import com.organization.taskManagement.DTO.Response.ApiResponse;
import com.organization.taskManagement.DTO.Response.EmployeeRegistrationResponse;
import com.organization.taskManagement.Services.EmployeeRegService;
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
public class EmployeeRegisterController {

    private final EmployeeRegService employeeRegService;


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
