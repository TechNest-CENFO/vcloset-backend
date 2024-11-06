package com.example.vcloset.logic.rest.auth;

import com.example.vcloset.logic.entity.auth.AuthenticationService;
import com.example.vcloset.logic.entity.auth.JwtService;
import com.example.vcloset.logic.entity.user.LoginResponse;
import com.example.vcloset.logic.entity.user.User;
import com.example.vcloset.logic.entity.user.UserRepository;
import com.example.vcloset.rest.auth.AuthRestController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class AuthRestControllerTest {
    @InjectMocks
    private AuthRestController authRestController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private JwtService jwtService;

    private User user;
    private LoginResponse loginResponse;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setEmail("super.admin@gmail.com");
        user.setPassword("superadmin123");

        loginResponse = new LoginResponse();
        loginResponse.setToken("dummy-jwt-token");
        loginResponse.setExpiresIn(3600);
    }

    @Test
    public void testAuthenticate_Success() {
        // Arrange
        when(authenticationService.authenticate(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("dummy-jwt-token");
        when(jwtService.getExpirationTime()).thenReturn(3600L);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // Act
        ResponseEntity<LoginResponse> response = authRestController.authenticate(user);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response);
        assertEquals(user, response.getBody().getAuthUser());
    }


}
