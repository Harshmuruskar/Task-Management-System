package com.organization.taskManagement.Controller;

import com.organization.taskManagement.DTO.Request.EmployeeRegistrationRequest;
import com.organization.taskManagement.DTO.Request.LoginRequest;
import com.organization.taskManagement.DTO.Request.RefreshTokenRequest;
import com.organization.taskManagement.DTO.Response.ApiResponse;
import com.organization.taskManagement.DTO.Response.AuthResponse;
import com.organization.taskManagement.DTO.Response.EmployeeRegistrationResponse;
import com.organization.taskManagement.Model.RefreshToken;
import com.organization.taskManagement.Repository.RefreshTokenRepository;
import com.organization.taskManagement.Services.AuthService;
import com.organization.taskManagement.security.JwtService;
import com.organization.taskManagement.security.RefreshTokenService;
import com.organization.taskManagement.security.UserInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {


    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserInfoService userInfoService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<EmployeeRegistrationResponse>> register(@Valid @RequestBody EmployeeRegistrationRequest request)  {
        EmployeeRegistrationResponse response =  authService.registerEmployee(request);
        return ResponseEntity.ok(ApiResponse.success("Employee Registered successfully", response));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<RefreshTokenRequest> refreshToken(@RequestBody RefreshTokenRequest request) {
        Optional<RefreshToken> tokenOpt = refreshTokenRepository.findByToken(request.getRefreshToken());
        if (tokenOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        RefreshToken token = refreshTokenService.verifyExpiration(tokenOpt.get());

        // Load user details
        UserDetails userDetails = userInfoService.loadUserByUsername(token.getEmployeeId());

        // Generate new access token
        String newAccessToken = jwtService.generateToken(token.getEmployeeId(), userDetails);

        RefreshTokenRequest response = new RefreshTokenRequest();
        response.setAccessToken(newAccessToken);
        response.setRefreshToken(token.getToken()); // Reuse the refresh token

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody RefreshTokenRequest request) {
        if (request == null || request.getRefreshToken() == null || request.getRefreshToken().isEmpty()) {
            throw new IllegalArgumentException("Refresh token is required");
        }

        Optional<RefreshToken> tokenOpt = refreshTokenRepository.findByToken(request.getRefreshToken());

        if (tokenOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid or expired refresh token");
        }

        RefreshToken refreshToken = tokenOpt.get();

        // Verify that the authenticated user matches the token owner
        String authenticatedUser = getAuthenticatedEmployeeId();
        if (authenticatedUser != null && !authenticatedUser.equals(refreshToken.getEmployeeId())) {
            throw new RuntimeException("Cannot logout another user's session");
        }

        refreshTokenRepository.delete(refreshToken);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Logout successful");
        response.put("timestamp", LocalDateTime.now());

        return ResponseEntity.ok(response);
    }

    private String getAuthenticatedEmployeeId() {
        Authentication authentication = org.springframework.security.core.context.SecurityContextHolder
                .getContext()
                .getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            return authentication.getName();
        }
        return null;
    }
}
