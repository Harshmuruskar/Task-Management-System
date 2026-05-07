package com.organization.taskManagement.Repos;

import com.organization.taskManagement.Model.EmployeeRegModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRegRepo extends JpaRepository<EmployeeRegModel, Long> {

    boolean existsByEmail(String email);

    boolean existsByEmployeeId(String employeeId);

    Optional<EmployeeRegModel> findByEmail(String email);

    Optional<EmployeeRegModel> findByEmployeeId(String employeeId);

}
