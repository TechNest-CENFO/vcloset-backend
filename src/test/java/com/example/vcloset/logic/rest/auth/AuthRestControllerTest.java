package com.example.vcloset.logic.rest.auth;

import com.example.vcloset.logic.entity.auth.AuthenticationService;
import com.example.vcloset.logic.entity.auth.JwtService;
import com.example.vcloset.logic.entity.rol.RoleEnum;
import com.example.vcloset.logic.entity.rol.RoleRepository;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;


    private User user;


    @BeforeEach
    public void setUp() {
       MockitoAnnotations.openMocks(this);
        user = new User();
        user.setEmail("super.admin@gmail.com");
        user.setPassword("superadmin123");

    }

    @Test
    public void testRegisterUser_RoleNotFound() {
        // Arrange
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());  // No hay usuario con este email
        when(roleRepository.findByName(RoleEnum.USER)).thenReturn(Optional.empty());  // No se encuentra el role
        when(passwordEncoder.encode(user.getPassword())).thenReturn("password"); // Mockea la codificación de la contraseña
        // Act
        ResponseEntity<?> response = authRestController.registerUser(user);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Role not found", response.getBody());
    }


}
