package com.demo.admin.controller;

import com.demo.admin.dto.CreateUserWithRole;
import com.demo.admin.service.AdminServiceImpl;
import com.demo.admin.dto.RegistrationAdminDto;
import com.demo.entity.UserEntity;
import com.demo.refresh.service.RefreshTokenService;
import com.demo.security.JWTService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminServiceImpl adminService;


    @PostMapping("/register")
    public ResponseEntity<?> registerAdmin(
            @Valid @RequestBody RegistrationAdminDto dto) {

        UserEntity user = adminService.registerAdmin(dto);

        return ResponseEntity.ok(
                Map.of(
                        "message", "User registered successfully!",
                        "email", user.getEmail()
                )
        );
    }

    @PostMapping("/create-role-user")
    public ResponseEntity<?> createNewRoles(
            @Valid @RequestBody CreateUserWithRole dto) {

        UserEntity user = adminService.createNewRole(dto);

        return ResponseEntity.ok(
                Map.of(
                        "message", "User registered successfully!",
                        "email", user.getEmail()
                )
        );
    }
}
