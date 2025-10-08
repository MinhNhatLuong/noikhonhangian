package com.example.userservice.service;

import com.example.userservice.dto.request.UserCreationRequest;
import com.example.userservice.dto.response.RestResponse;
import com.example.userservice.dto.response.UserCreationResponse;
import com.example.userservice.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    RestResponse<UserCreationResponse> createUser(UserCreationRequest request);
    RestResponse<List<UserCreationResponse>> getAllUsers();
    RestResponse<UserCreationResponse> getUserById(Integer id);
    RestResponse<UserCreationResponse> getUserInfo();
}