package com.example.userservice.controller;

import com.example.userservice.dto.request.LoginRequest;
import com.example.userservice.dto.response.LoginResponse;
import com.example.userservice.dto.response.RestResponse;
import com.example.userservice.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/auth/login")
    public RestResponse<LoginResponse> login(@RequestBody LoginRequest request){

        return RestResponse.<LoginResponse>builder()
                .result(authenticationService.login(request))
                .message("Login successful")
                .statusCode(200)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
