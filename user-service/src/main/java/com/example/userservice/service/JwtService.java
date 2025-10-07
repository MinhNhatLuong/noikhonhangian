package com.example.userservice.service;

import com.example.userservice.entity.User;

public interface JwtService {
    String generateAccessToken(User user);
    String generateRefreshToken(User user);
}
