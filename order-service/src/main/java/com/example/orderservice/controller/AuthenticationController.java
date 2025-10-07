package com.example.orderservice.controller;

import com.example.userservice.dto.request.LoginRequest;
import com.example.userservice.dto.response.LoginResponse;
import com.example.userservice.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/auth/login")
    public LoginResponse login(@RequestBody LoginRequest request){
        return authenticationService.login(request);
    }
}
