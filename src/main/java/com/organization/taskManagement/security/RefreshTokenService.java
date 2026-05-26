package com.organization.taskManagement.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.organization.taskManagement.Model.RefreshTokenModel;
import com.organization.taskManagement.Repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    
    private final RefreshTokenRepository refreshTokenRepository;


    public RefreshTokenModel createRefreshToken(String employeeId, String employeeRole) {
        // Cleanup old tokens
        refreshTokenRepository.deleteByEmployeeId(employeeId);
        
        // Build new token object
        RefreshTokenModel refreshToken = RefreshTokenModel.builder()
                .token(UUID.randomUUID().toString())
                .employeeId(employeeId)
                .userType(employeeRole)
                .expiryDate(Instant.now().plus(7, ChronoUnit.DAYS)) // Valid for 7 days
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshTokenModel verifyExpiration(RefreshTokenModel token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token was expired. Please make a new login request");
        }
        return token;
    }
}
