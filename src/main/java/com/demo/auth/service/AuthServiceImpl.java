package com.demo.auth.service;

import com.demo.auth.dto.LoginRequest;
import com.demo.auth.dto.LoginResponse;
import com.demo.refresh.entity.RefreshTokenEntity;
import com.demo.entity.RoleEntity;
import com.demo.entity.UserEntity;
import com.demo.enums.RoleType;
import com.demo.repository.RoleRepository;
import com.demo.repository.UserDataRepo;
import com.demo.security.CustomUserDetails;
import com.demo.security.JWTService;
import com.demo.refresh.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;


@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserDataRepo userDataRepo;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;



    @Override
    public LoginResponse login(LoginRequest loginRequest,
                               HttpServletResponse response) {

        // 1. Authenticate user (Spring Security handles validation)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUserName(),
                        loginRequest.getPassword()
                )
        );

        // 2. Get authenticated principal
        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        // 3. Fetch full user entity (needed for JWT claims)
        UserEntity userEntity = userDataRepo.findByEmail(userDetails.getUsername())
                .orElseThrow(() ->
                        new RuntimeException("User not found with email: " + userDetails.getUsername())
                );

        // 4. Generate JWT token and refresh token
        String accessToken = jwtService.generateAccessToken(userEntity);
        RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(userEntity);

        ResponseCookie cookie = ResponseCookie
                .from("refreshToken", refreshToken.getToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
//                .maxAge(Duration.ofDays(7))
                .maxAge(Duration.ofMinutes(10))
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        // 5. Extract roles
        List<RoleType> roleTypeList = userEntity.getRoles()
                .stream()
                .map(RoleEntity::getRoleType)
                .toList();

        // 6. Build response
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setEmail(userEntity.getEmail());
        loginResponse.setToken(accessToken);
        loginResponse.setRoles(roleTypeList);

        return loginResponse;
    }
}
