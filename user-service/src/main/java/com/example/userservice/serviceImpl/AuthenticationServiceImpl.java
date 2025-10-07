package com.example.userservice.serviceImpl;

import com.example.userservice.dto.request.IntrospectRequest;
import com.example.userservice.dto.request.LoginRequest;
import com.example.userservice.dto.response.IntrospectResponse;
import com.example.userservice.dto.response.LoginResponse;
import com.example.userservice.entity.User;
import com.example.userservice.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtServiceImpl jwtService;

    @Override
    public LoginResponse login(LoginRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        User user = (User) authenticate.getPrincipal();
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        //if success
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest request) {
        var token = request.getToken();
        boolean isValid = true;

        try {
            jwtService.verifyToken(token, false);
        } catch (JOSEException | ParseException e) {
            isValid = false;
        }

        return IntrospectResponse.builder().valid(isValid).build();
    }

}
