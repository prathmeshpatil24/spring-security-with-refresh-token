package com.demo.controller;


import com.demo.dto.LoginRequest;
import com.demo.dto.LoginResponse;
import com.demo.dto.RegistrationDto;

import com.demo.entity.RefreshTokenEntity;
import com.demo.entity.RoleEntity;
import com.demo.entity.UserEntity;
import com.demo.security.JWTService;
import com.demo.service.AuthServiceImpl;
import com.demo.service.RefreshTokenService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthServiceImpl authService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JWTService jwtService;

    @PostMapping("/admin/register")
    public ResponseEntity<?> registerAdmin(
            @Valid @RequestBody RegistrationDto dto) {

        UserEntity user = authService.registerAdmin(dto);

        return ResponseEntity.ok(
                Map.of(
                        "message", "User registered successfully!",
                        "email", user.getEmail()
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request,
                                   HttpServletResponse httpServletResponse) {

        LoginResponse loginResponse = authService.login(request,httpServletResponse);

        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of(
                        "status", HttpStatus.OK.value(),
                        "message", "Login Successfully",
                        "data", loginResponse
                ));
    }


    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(
            @CookieValue("refreshToken") String refreshToken) {

        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Refresh token missing");
        }

        RefreshTokenEntity rt =
                refreshTokenService.verifyRefreshToken(refreshToken);

        UserEntity user = rt.getUser();

        String newAccessToken =
                jwtService.generateAccessToken(user);

        LoginResponse response = new LoginResponse();
        response.setEmail(user.getEmail());
        response.setToken(newAccessToken);
        response.setRoles(
                user.getRoles().stream()
                        .map(RoleEntity::getRoleType)
                        .toList()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @CookieValue("refreshToken") String refreshToken,
            HttpServletResponse response) {

        refreshTokenService.revokeToken(refreshToken);

        ResponseCookie deleteCookie = ResponseCookie
                .from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());

        return ResponseEntity.ok("Logged out successfully");
    }
}
