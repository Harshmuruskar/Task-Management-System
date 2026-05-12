package com.organization.taskManagement.Mappers;

import com.organization.taskManagement.DTO.EmployeeRegistrationRequestDTO;
import com.organization.taskManagement.DTO.EmployeeRegistrationResponseDTO;
import com.organization.taskManagement.Model.EmployeeRegModel;

public class EmployeeMapper {

    public static EmployeeRegModel toEntity(EmployeeRegistrationRequestDTO request){

        if(request == null) return null;

        return EmployeeRegModel.builder()
                .name(request.getName())
                .email(request.getEmail())
                .employeeId(request.getEmployeeId())
                .password(request.getPassword())
                .role(request.getRole())
                .designation(request.getDesignation())
                .build();
    }

    public static EmployeeRegistrationResponseDTO toResponse(EmployeeRegModel employee){

        if(employee == null) return  null;

        return EmployeeRegistrationResponseDTO.builder()
                .id(employee.getId())
                .name(employee.getName())
                .email(employee.getEmail())
                .employeeId(employee.getEmployeeId())
                .role(employee.getRole())
                .designation(employee.getDesignation())
                .createdAt(employee.getCreatedAt())
                .updatedAt(employee.getUpdatedAt())
                .build();
    }

}
