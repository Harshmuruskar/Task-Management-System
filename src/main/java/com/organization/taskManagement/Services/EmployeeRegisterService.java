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
public class EmployeeRegisterService {

    private final EmployeeRegisterRepository employeeRegisterRepository;
    private final PasswordEncoder passwordEncoder;


    public EmployeeRegistrationResponse registerEmployee(EmployeeRegistrationRequest request) {

        if (employeeRegisterRepository.existsByEmployeeId(request.getEmployeeId())) {
            throw new RuntimeException("Employee ID already exists");
        }

        if (employeeRegisterRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        EmployeeRegisterModel employeeRegisterModel = EmployeeMapper.toEntity(request);
        employeeRegisterModel.setPassword(passwordEncoder.encode(employeeRegisterModel.getPassword()));
        EmployeeRegisterModel result = employeeRegisterRepository.save(employeeRegisterModel);

        return EmployeeMapper.toResponse(result);
    }

    public EmployeeRegistrationResponse login(LoginRequest request) {
        EmployeeRegisterModel employee = employeeRegisterRepository.findByEmployeeId(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));



        if (!passwordEncoder.matches(request.getPassword(), employee.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return EmployeeMapper.toResponse(employee);
    }

    public void deleteEmployee (Long id){
        EmployeeRegisterModel employee = employeeRegisterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        employeeRegisterRepository.delete(employee);
    }

    public Page<EmployeeRegistrationResponse> getAllEmployees(Pageable pageable) {
        return employeeRegisterRepository.findAll(pageable)
                .map(EmployeeMapper::toResponse);
    }


}
