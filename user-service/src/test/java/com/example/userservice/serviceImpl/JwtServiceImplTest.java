package com.example.userservice.serviceImpl;

import com.example.userservice.entity.Role;
import com.example.userservice.entity.User;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.text.ParseException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceImplTest {

    private JwtServiceImpl jwtService;
    private User testUser;

    @BeforeEach
    void setUp() {
        jwtService = new JwtServiceImpl();

        ReflectionTestUtils.setField(jwtService, "secretKey",
                "1234567890123456789012345678901234567890123456789012345678901234");
        ReflectionTestUtils.setField(jwtService, "REFRESHABLE_DURATION", 86400L);

        Role role = new Role();
        role.setRoleName("guest");
        role.setId(1);

        testUser = new User();
        testUser.setId(1);
        testUser.setUsername("testUser");
        testUser.setRole(role);
    }

    @Test
    void testGenerateAccessToken_ShouldReturnValidJWT() throws Exception {
        String token = jwtService.generateAccessToken(testUser);
        assertNotNull(token);

        SignedJWT signedJWT = SignedJWT.parse(token);
        assertEquals("testUser", signedJWT.getJWTClaimsSet().getSubject());
        assertEquals("guest", signedJWT.getJWTClaimsSet().getStringClaim("role"));
        assertEquals(1L, signedJWT.getJWTClaimsSet().getLongClaim("id"));
    }

    @Test
    void testGenerateRefreshToken_ShouldReturnValidJWT() throws Exception {
        String token = jwtService.generateRefreshToken(testUser);
        assertNotNull(token);

        SignedJWT signedJWT = SignedJWT.parse(token);
        assertEquals("testUser", signedJWT.getJWTClaimsSet().getSubject());
        assertTrue(signedJWT.getJWTClaimsSet().getExpirationTime().after(new Date()));
    }

    @Test
    void testVerifyToken_ValidToken_ShouldReturnTrue() throws Exception {
        String token = jwtService.generateAccessToken(testUser);
        boolean result = jwtService.verifyToken(token);
        assertTrue(result);
    }

    @Test
    void testVerifyToken_InvalidToken_ShouldThrowException() throws Exception {
        String token = jwtService.generateAccessToken(testUser);
        // wrong 1 char
        String invalidToken = token.substring(0, token.length() - 1) + "x";
        assertThrows(ParseException.class, () -> jwtService.verifyToken(invalidToken));
    }

    @Test
    void testVerifyTokenWithRefresh_ShouldReturnSignedJWT() throws Exception {
        String refreshToken = jwtService.generateRefreshToken(testUser);
        SignedJWT jwt = jwtService.verifyToken(refreshToken, true);
        assertNotNull(jwt);
        assertEquals("testUser", jwt.getJWTClaimsSet().getSubject());
    }

    @Test
    @Disabled("Skipping expiration test - requires time manipulation")
    void testVerifyTokenWithRefresh_Expired_ShouldThrowException() throws Exception {
        // Test này cần cơ chế mock time để test chính xác
        // Tạm thời disable
    }
}