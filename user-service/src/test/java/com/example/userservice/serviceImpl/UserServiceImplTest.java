package com.example.userservice.serviceImpl;

import com.example.userservice.dto.request.UserCreationRequest;
import com.example.userservice.dto.response.RestResponse;
import com.example.userservice.dto.response.UserCreationResponse;
import com.example.userservice.entity.Role;
import com.example.userservice.entity.User;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.repository.RoleRepository;
import com.example.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private UserCreationRequest request;
    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        request = new UserCreationRequest();
        request.setUsername("john");
        request.setPassword("123456");

        user = new User();
        user.setUsername("john");

        role = new Role();
        role.setRoleName("Admin");
    }

    @Test
    void createUser_shouldSaveUser_whenUserDoesNotExist() {
        // given
        when(userRepository.existsByUsername(request.getUsername())).thenReturn(false);
        when(userMapper.toUserEntity(request)).thenReturn(user);
        when(roleRepository.findByRoleName("Admin")).thenReturn(role);
        when(userMapper.toUserCreationResponse(any(User.class))).thenReturn(new UserCreationResponse());

        // when
        RestResponse<UserCreationResponse> response = userService.createUser(request);

        // then
        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.getMessage()).isEqualTo("User created successfully!");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_shouldThrowException_whenUserAlreadyExists() {
        when(userRepository.existsByUsername("john")).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("already exists");

        verify(userRepository, never()).save(any());
    }
}
