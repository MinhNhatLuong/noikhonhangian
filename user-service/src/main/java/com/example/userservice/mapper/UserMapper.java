package com.example.userservice.mapper;

import com.example.userservice.dto.request.UserCreationRequest;
import com.example.userservice.dto.response.LoginResponse;
import com.example.userservice.dto.response.UserCreationResponse;
import com.example.userservice.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUserEntity(UserCreationRequest request);
    UserCreationResponse toUserCreationResponse(User user);
//    public LoginResponse toLoginResponse(User user);
}
