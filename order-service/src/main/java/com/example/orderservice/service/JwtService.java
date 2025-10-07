package com.example.orderservice.service;

import com.example.userservice.entity.User;

public interface JwtService {
    String generateAccessToken(User user);
    String generateRefreshToken(User user);
}
