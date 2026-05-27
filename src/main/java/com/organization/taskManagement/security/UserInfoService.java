package com.organization.taskManagement.security;

import com.organization.taskManagement.DTO.EmployeeRegistrationRequest;
import com.organization.taskManagement.Mappers.EmployeeMapper;
import com.organization.taskManagement.Model.EmployeeRegModel;
import com.organization.taskManagement.Repos.EmployeeRegRepo;
import lombok.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserInfoService implements UserDetailsService {

    private final EmployeeRegRepo employeeRegRepo;
    private final PasswordEncoder passwordEncoder;

    public UserInfoService(EmployeeRegRepo employeeRegRepo, PasswordEncoder passwordEncoder) {
        this.employeeRegRepo = employeeRegRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(@NonNull String employeeId) throws UsernameNotFoundException {
        Optional<EmployeeRegModel> employeeOpt = employeeRegRepo.findByEmployeeId(employeeId);
        if(employeeOpt.isEmpty()) {
            throw new UsernameNotFoundException("User not found with employee ID: " + employeeId);
        }

        EmployeeRegModel employee = employeeOpt.get();
        return new UserInfoDetails(employee);
        }


        public String addEmployee(EmployeeRegistrationRequest employee) {
            if(!employeeRegRepo.findByEmployeeId(employee.getEmployeeId()).isEmpty()) {
                return "Email already exists";
            }
            employee.setPassword(passwordEncoder.encode(employee.getPassword()));
            employeeRegRepo.save(EmployeeMapper.toEntity(employee));
            return "Employee added successfully";
        }
}

