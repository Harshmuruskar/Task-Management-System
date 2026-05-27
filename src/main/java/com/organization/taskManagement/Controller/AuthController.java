package com.organization.taskManagement.Controller;

import com.organization.taskManagement.DTO.*;
import com.organization.taskManagement.Model.EmployeeRegModel;
import com.organization.taskManagement.Model.RefreshToken;
import com.organization.taskManagement.Repos.EmployeeRegRepo;
import com.organization.taskManagement.Repos.RefreshTokenRepository;
import com.organization.taskManagement.Services.EmployeeRegService;
import com.organization.taskManagement.security.JwtService;
import com.organization.taskManagement.security.RefreshTokenService;
import com.organization.taskManagement.security.UserInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {


    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserInfoService userInfoService;
    private final EmployeeRegRepo employeeRegRepo;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;

    @PostMapping("/register")
    public String register(@Valid @RequestBody EmployeeRegistrationRequest request)  {
        try {
            System.out.println(request);
            return userInfoService.addEmployee(request);
        }
       catch (UsernameNotFoundException e) {
            return e.getMessage();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try{
            if(request.getEmployeeId() == null || request.getPassword()== null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body( errorResponse( "Employee ID and password are required"));

            }
            Optional<EmployeeRegModel> employeeRegModels = employeeRegRepo.findByEmployeeId(request.getEmployeeId());
            if(employeeRegModels.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse("User not found with employee ID: " + request.getEmployeeId()));
            }
            EmployeeRegModel employee = employeeRegModels.get();
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmployeeId(), request.getPassword())
            );
            if(authentication.isAuthenticated()){
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                String jwtToken = jwtService.generateToken(request.getEmployeeId(),userDetails);
                System.out.println("checking ...... role : "+employee.getRole().toString());
                RefreshToken refreshToken = refreshTokenService.createRefreshToken(employee.getEmployeeId(),employee.getRole().toString());
                RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
                refreshTokenRequest.setAccessToken(jwtToken);
                refreshTokenRequest.setRefreshToken(refreshToken.getToken());
                return ResponseEntity.status(HttpStatus.OK).body(refreshTokenRequest);

            }else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse("Invalid credentials"));
            }

        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse(e.getMessage()));
        }


   }

    private Map<String, Object> errorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", true);
        response.put("message", message);
        response.put("timestamp", LocalDateTime.now());
        return response;
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        try {
            Optional<RefreshToken> tokenOpt = refreshTokenRepository.findByToken(request.getRefreshToken());
            if (tokenOpt.isEmpty()) {

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(errorResponse("Invalid refresh token"));
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
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(errorResponse("Refresh token expired"));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("An error occurred during token refresh"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody RefreshTokenRequest request) {
        try {
            if (request == null || request.getRefreshToken() == null || request.getRefreshToken().isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(errorResponse("Refresh token is required"));
            }

            Optional<RefreshToken> tokenOpt = refreshTokenRepository.findByToken(request.getRefreshToken());

            if (tokenOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(errorResponse("Invalid or expired refresh token"));
            }

            RefreshToken refreshToken = tokenOpt.get();

            // Verify that the authenticated user matches the token owner
            String authenticatedUser = getAuthenticatedEmployeeId();
            if (authenticatedUser != null && !authenticatedUser.equals(refreshToken.getEmployeeId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(errorResponse("Cannot logout another user's session"));
            }

            refreshTokenRepository.delete(refreshToken);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Logout successful");
            response.put("timestamp", LocalDateTime.now());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse("An error occurred during logout"));
        }
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
