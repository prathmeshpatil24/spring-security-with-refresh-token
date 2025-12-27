package com.demo.service;

import com.demo.dto.LoginRequest;
import com.demo.dto.LoginResponse;
import com.demo.dto.RegistrationDto;
import com.demo.entity.UserEntity;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public interface AuthService {

    //admin registration only
    UserEntity registerAdmin(RegistrationDto dto);

    LoginResponse login(LoginRequest loginRequest,
                        HttpServletResponse httpServletResponse);
}
