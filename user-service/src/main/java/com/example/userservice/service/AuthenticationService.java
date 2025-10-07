package com.example.userservice.service;

import com.example.userservice.dto.request.IntrospectRequest;
import com.example.userservice.dto.request.LoginRequest;
import com.example.userservice.dto.response.IntrospectResponse;
import com.example.userservice.dto.response.LoginResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {
    LoginResponse login(LoginRequest request);
    IntrospectResponse introspect(IntrospectRequest request);
}
