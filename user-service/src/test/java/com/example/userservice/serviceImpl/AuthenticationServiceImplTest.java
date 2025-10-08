package com.example.userservice.serviceImpl;

import com.example.userservice.dto.request.LoginRequest;
import com.example.userservice.dto.response.LoginResponse;
import com.example.userservice.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtServiceImpl jwtService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    private LoginRequest request;
    private User user;

    @BeforeEach
    void setUp() {
        request = new LoginRequest();
        request.setUsername("john");
        request.setPassword("123456");

        user = new User();
        user.setUsername("john");
    }

    @Test
    void login_shouldReturnTokens_whenCredentialsAreValid() {
        // given
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(jwtService.generateAccessToken(user)).thenReturn("access-token");
        when(jwtService.generateRefreshToken(user)).thenReturn("refresh-token");

        // when
        LoginResponse response = authenticationService.login(request);

        // then
        assertThat(response.getAccessToken()).isEqualTo("access-token");
        assertThat(response.getRefreshToken()).isEqualTo("refresh-token");
        verify(authenticationManager).authenticate(any());
    }

    @Test
    void login_shouldThrowException_whenCredentialsAreInvalid() {
        // given
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Invalid username or password"));

        // when + then
        assertThatThrownBy(() -> authenticationService.login(request))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessageContaining("Invalid username or password");
    }
}
