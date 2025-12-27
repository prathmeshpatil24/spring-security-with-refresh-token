package com.demo.controller;

import com.demo.audit.AuditAwareConfig;
import com.demo.entity.UserEntity;
import com.demo.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private AuditAwareConfig auditAwareConfig;
    
    @Autowired
    private ProfileService profileService;

    @GetMapping()
    public ResponseEntity<?> profileDetails(){
        
        Integer userId = auditAwareConfig.getCurrentAuditor()
                .orElseThrow(() -> new RuntimeException(
                        "User is unauthenticated, please login with proper credentials"
                ));
        UserEntity userEntity = profileService.profileInfo(userId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of(
                        "status", HttpStatus.OK.value(),
                        "data", userEntity
                ));
    }

}
