package com.example.orderservice.serviceImpl;

import com.example.userservice.dto.request.UserCreationRequest;
import com.example.userservice.dto.response.RestResponse;
import com.example.userservice.dto.response.UserCreationResponse;
import com.example.userservice.entity.User;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.repository.RoleRepository;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    @Override
    public RestResponse<UserCreationResponse> createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("User with username " + request.getUsername() + " already exists");
        }

        User user = userMapper.toUserEntity(request);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        user.setRole(roleRepository.findByRoleName("Admin"));
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
        return new RestResponse<UserCreationResponse>(200, "User created successfully!", userMapper.toUserCreationResponse(user));
    }

    @Override
    public RestResponse<List<UserCreationResponse>> getAllUsers() {
        List<UserCreationResponse> userList = userRepository.findAll()
                .stream()
                .map(userMapper::toUserCreationResponse)
                .toList();
        return new RestResponse<List<UserCreationResponse>>(200, "Get all users successfully!", userList);
    }

    @Override
    public RestResponse<UserCreationResponse> getUserById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User with id " + id + " not found"));
        return new RestResponse<UserCreationResponse>(200, "User found!", userMapper.toUserCreationResponse(user));
    }
}
