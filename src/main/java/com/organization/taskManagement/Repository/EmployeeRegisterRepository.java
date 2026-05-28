package com.organization.taskManagement.Repository;

import com.organization.taskManagement.Model.EmployeeRegisterModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;
@Repository
public interface EmployeeRegisterRepository extends JpaRepository<EmployeeRegisterModel, Long> {

    boolean existsByEmail(String email);

    boolean existsByEmployeeId(String employeeId);

    Optional<EmployeeRegisterModel> findByEmail(String email);

    Optional<EmployeeRegisterModel> findByEmployeeId(String employeeId);

}
