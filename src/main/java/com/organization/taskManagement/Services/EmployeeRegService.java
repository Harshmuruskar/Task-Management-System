package com.organization.taskManagement.Services;

import com.organization.taskManagement.DTO.EmployeeRegistrationRequestDTO;
import com.organization.taskManagement.DTO.EmployeeRegistrationResponseDTO;
import com.organization.taskManagement.DTO.LoginRequestDTO;
import com.organization.taskManagement.Mappers.EmployeeMapper;
import com.organization.taskManagement.Model.EmployeeRegModel;
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


    public EmployeeRegistrationResponseDTO registerEmployee(EmployeeRegistrationRequestDTO request) {

        if (employeeRegRepo.existsByEmployeeId(request.getEmployeeId())) {
            throw new RuntimeException("Employee ID already exists");
        }

        if (employeeRegRepo.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        EmployeeRegModel employeeRegModel = EmployeeMapper.toEntity(request);
        employeeRegModel.setPassword(passwordEncoder.encode(employeeRegModel.getPassword()));
        EmployeeRegModel result = employeeRegRepo.save(employeeRegModel);

        return EmployeeMapper.toResponse(result);
    }

    public EmployeeRegistrationResponseDTO login(LoginRequestDTO request) {
        EmployeeRegModel employee = employeeRegRepo.findByEmployeeId(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (employee.getRole() != request.getRole()) {
            throw new RuntimeException("Invalid role");
        }

        if (!passwordEncoder.matches(request.getPassword(), employee.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return EmployeeMapper.toResponse(employee);
    }

    public void deleteEmployee (Long id){
        EmployeeRegModel employee = employeeRegRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        employeeRegRepo.delete(employee);
    }

    public Page<EmployeeRegistrationResponseDTO> getAllEmployees(Pageable pageable) {
        return employeeRegRepo.findAll(pageable)
                .map(EmployeeMapper::toResponse);
    }


}
