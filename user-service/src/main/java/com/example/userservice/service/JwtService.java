package com.example.userservice.service;

import com.example.userservice.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface JwtService {
    String generateAccessToken(User user);
    String generateRefreshToken(User user);
}
