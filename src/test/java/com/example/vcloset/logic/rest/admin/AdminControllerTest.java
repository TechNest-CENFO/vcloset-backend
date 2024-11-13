package com.example.vcloset.logic.rest.admin;

import com.example.vcloset.logic.entity.rol.Role;
import com.example.vcloset.logic.entity.rol.RoleEnum;
import com.example.vcloset.logic.entity.rol.RoleRepository;
import com.example.vcloset.logic.entity.user.User;
import com.example.vcloset.logic.entity.user.UserRepository;
import com.example.vcloset.rest.admin.AdminController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
public class AdminControllerTest {
    @InjectMocks
    private AdminController adminController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Inicializa los mocks
    }

    @Test
    void testCreateAdministrator_Success() {
        // Crear un rol de administrador
        Role adminRole = new Role();
        adminRole.setName(RoleEnum.ADMIN);

        // Arrange: Crear un nuevo usuario
        User newAdminUser = new User();
        newAdminUser.setName("Admin User");
        newAdminUser.setEmail("admin@example.com");
        newAdminUser.setPassword("password");
        newAdminUser.setRole(adminRole);



        // Simular que el RoleRepository encuentra el rol "ADMIN"
        when(roleRepository.findByName(RoleEnum.ADMIN)).thenReturn(Optional.of(adminRole));

        // Simular que el PasswordEncoder encripta correctamente la contraseña
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        // Simular el comportamiento de UserRepository para guardar el usuario
        when(userRepository.save(any(User.class))).thenReturn(newAdminUser);

        // Act: Llamar al método createAdministrator
        User createdUser = adminController.createAdministrator(newAdminUser);

        // Assert: Verificar que el resultado es correcto
        assertNotNull(createdUser);
        assertEquals("Admin User", createdUser.getName());
        assertEquals("admin@example.com", createdUser.getEmail());
        assertEquals("password", createdUser.getPassword());
        assertEquals(adminRole, createdUser.getRole());

        // Verificar que se haya llamado al método save del userRepository
        verify(userRepository, times(1)).save(any(User.class));
        verify(roleRepository, times(1)).findByName(RoleEnum.ADMIN);
        verify(passwordEncoder, times(1)).encode("password");
    }
}
