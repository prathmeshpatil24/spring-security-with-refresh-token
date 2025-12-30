package com.demo.auth.service;

import com.demo.auth.dto.LoginRequest;
import com.demo.auth.dto.LoginResponse;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {



    LoginResponse login(LoginRequest loginRequest,
                        HttpServletResponse httpServletResponse);
}
