package com.organization.taskManagement.security;

import com.organization.taskManagement.Model.EmployeeRegisterModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;


public class UserInfoDetails implements UserDetails {

    // Internal reference to our Employee entity
    private EmployeeRegisterModel employee;

    public UserInfoDetails(EmployeeRegisterModel employee) {
        this.employee = employee;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(employee != null && employee.getRole() != null) {
            // We use the employee ID as the authority for identification
            return Collections.singleton(new SimpleGrantedAuthority(employee.getEmployeeId()));
        }
        else return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return employee.getPassword();
    }

    @Override
    public String getUsername() {
        return employee.getEmployeeId();
    }

    // Standard UserDetails flags - here we default to 'true' (active)
    
    @Override
    public boolean isAccountNonExpired() {
        return true; 
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
