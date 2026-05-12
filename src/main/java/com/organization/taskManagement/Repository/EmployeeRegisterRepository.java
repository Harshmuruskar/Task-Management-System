package com.organization.taskManagement.Repository;

import com.organization.taskManagement.Model.EmployeeRegModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRegisterRepository extends JpaRepository<EmployeeRegModel, Long> {

    boolean existsByEmail(String email);

    boolean existsByEmployeeId(String employeeId);

    Optional<EmployeeRegModel> findByEmail(String email);

    Optional<EmployeeRegModel> findByEmployeeId(String employeeId);

}
