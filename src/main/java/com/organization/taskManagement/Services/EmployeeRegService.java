package com.organization.taskManagement.Services;

import com.organization.taskManagement.DTO.Request.EmployeeRegistrationRequest;
import com.organization.taskManagement.DTO.Request.LoginRequest;
import com.organization.taskManagement.DTO.Response.EmployeeRegistrationResponse;
import com.organization.taskManagement.Mappers.EmployeeMapper;
import com.organization.taskManagement.Model.EmployeeRegisterModel;
import com.organization.taskManagement.Repository.EmployeeRegisterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeRegService {

    private final EmployeeRegisterRepository employeeRegRepo;
    private final PasswordEncoder passwordEncoder;


    public EmployeeRegistrationResponse registerEmployee(EmployeeRegistrationRequest request) {

        if (employeeRegRepo.existsByEmployeeId(request.getEmployeeId())) {
            throw new RuntimeException("Employee ID already exists");
        }

        if (employeeRegRepo.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        EmployeeRegisterModel employeeRegModel = EmployeeMapper.toEntity(request);
        employeeRegModel.setPassword(passwordEncoder.encode(employeeRegModel.getPassword()));
        EmployeeRegisterModel result = employeeRegRepo.save(employeeRegModel);

        return EmployeeMapper.toResponse(result);
    }

    public EmployeeRegistrationResponse login(LoginRequest request) {
        EmployeeRegisterModel employee = employeeRegRepo.findByEmployeeId(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));



        if (!passwordEncoder.matches(request.getPassword(), employee.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return EmployeeMapper.toResponse(employee);
    }

    public void deleteEmployee (Long id){
        EmployeeRegisterModel employee = employeeRegRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        employeeRegRepo.delete(employee);
    }

    public Page<EmployeeRegistrationResponse> getAllEmployees(Pageable pageable) {
        return employeeRegRepo.findAll(pageable)
                .map(EmployeeMapper::toResponse);
    }


}
