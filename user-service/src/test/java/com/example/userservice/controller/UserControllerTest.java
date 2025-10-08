package com.example.userservice.controller;

import com.example.userservice.dto.request.UserCreationRequest;
import com.example.userservice.dto.response.RestResponse;
import com.example.userservice.dto.response.UserCreationResponse;
import com.example.userservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createUser_shouldReturnCreatedUser() throws Exception {
        UserCreationRequest request = new UserCreationRequest();
        request.setUsername("john");
        request.setPassword("123456");

        UserCreationResponse responseData = new UserCreationResponse();
        RestResponse<UserCreationResponse> response =
                new RestResponse<>(200, "User created successfully", responseData);

        Mockito.when(userService.createUser(any())).thenReturn(response);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User created successfully"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAllUsers_shouldReturnUserList() throws Exception {
        RestResponse<List<UserCreationResponse>> response =
                new RestResponse<>(200, "Get all users successfully", List.of(new UserCreationResponse()));

        Mockito.when(userService.getAllUsers()).thenReturn(response);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Get all users successfully"));
    }
}
