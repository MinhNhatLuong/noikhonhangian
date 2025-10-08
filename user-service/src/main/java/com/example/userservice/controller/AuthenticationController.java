package com.example.userservice.controller;

import com.example.userservice.dto.request.IntrospectRequest;
import com.example.userservice.dto.request.LoginRequest;
import com.example.userservice.dto.response.IntrospectResponse;
import com.example.userservice.dto.response.LoginResponse;
import com.example.userservice.dto.response.RestResponse;
import com.example.userservice.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@Slf4j
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
    @PostMapping("/auth/introspect")
    public ResponseEntity<RestResponse<IntrospectResponse>> authenticate(@RequestBody IntrospectRequest request) {
        log.info("Incoming introspect request: {}", request);
        if (request.getToken() == null) {
            log.error("Token is null!");
        }
        var result = authenticationService.introspect(request);
        return ResponseEntity.ok(
                RestResponse.<IntrospectResponse>builder()
                        .statusCode(200)
                        .message("Introspect token successfully")
                        .result(result)
                        .build()
        );
    }
}
