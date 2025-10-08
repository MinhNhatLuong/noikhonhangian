package com.example.userservice.controller;

import com.example.userservice.dto.request.UserCreationRequest;
import com.example.userservice.dto.response.RestResponse;
import com.example.userservice.dto.response.UserCreationResponse;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public RestResponse<UserCreationResponse> createUser(@Valid @RequestBody UserCreationRequest request){
        return userService.createUser(request);
    }

    @GetMapping
//    @PreAuthorize("hasRole('Admin')")
    public RestResponse<List<UserCreationResponse>> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public RestResponse<UserCreationResponse> getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }
}
