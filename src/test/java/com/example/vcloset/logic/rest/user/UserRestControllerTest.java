package com.example.vcloset.logic.rest.user;

import static javax.management.Query.eq;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


import com.example.vcloset.logic.entity.http.GlobalResponseHandler;
import com.example.vcloset.logic.entity.http.Meta;
import com.example.vcloset.logic.entity.user.User;
import com.example.vcloset.logic.entity.user.UserRepository;
import com.example.vcloset.rest.user.UserRestController;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;



import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserRestControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private UserRestController userRestController;

    @Mock
    private GlobalResponseHandler globalResponseHandler;

    private Page<User> userPage;
    private User user;
    private Long userId = 1L;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        List<User> userList = new ArrayList<>();
        userList.add(new User());
        userList.add(new User());
        user = new User();
        user.setEmail("regular.user@gmail.com");
        user.setPassword("regularuser123");
        userId = 1L;
        userPage = new PageImpl<>(userList);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAll() {
        // Arrange
        //Se configuran los Mocks para simular el comportamiento del UserRepository y request

        int page = 1;
        int size = 10;
        Pageable pageable = PageRequest.of(page-1, size);
        //buscamos la clase Pageable que nos devuelve una Page
        when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);
        //Metodo que llamamos
        when(request.getMethod()).thenReturn("GET");
        //Endpoint al que se llama con el id
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost/api/users"));

        //Se configura el meta
        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(userPage.getTotalPages());
        meta.setTotalElements(userPage.getTotalElements());
        meta.setPageNumber(userPage.getNumber()+1);
        meta.setPageSize(userPage.getSize());

        // Act
        //Se llama al metodo con los parametros necesarios
        ResponseEntity<?> response = userRestController.getAll(page, size, request);

        // Assert
        //Se comprueba que la respuesta tenga el código de respuesta correcto
        //En este caso no siempre nos va adevolver aunque sea un array vacio por lo tanto siempre será un OK
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    public void testUpdateUser_UserNotFound() throws Exception {
        // Arrange
        //Se configuran los Mocks para simular el comportamiento del UserRepository y request
        //Se establece un userId que no existe en la BD
        Long userId = 5000L;
        User user = new User();
        user.setId(userId);
        user.setPassword("password");

        //Llamados al optional para asegurarno de que el usuario no existe
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        //Metodo que utilizamos
        when(request.getMethod()).thenReturn("PUT");
        //Endpoint al que se llama con el id
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost/api/users/" + userId));

        // Act
        //Se llama al metodo con los parametros necesarios
        ResponseEntity<?> response = userRestController.updateUser(userId, user, request);

        // Assert
        //Se comprueba que la respuesta tenga el código de respuesta correcto
        //En este caso no se encuentra el usuario por lo tanto el estado NOT_FOUND
        //es un estado correcto en nuestro testing
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody() instanceof GlobalResponseHandler);
        verify(userRepository, never()).save(any(User.class));
    }


    @Test
    public void testDeleteUser_UserNotFound() throws Exception {
        // Arrange
        //Se configuran los Mocks para simular el comportamiento del UserRepository y request
        //Se establece un userId que no existe en la BD
        Long userId = 5000L;

        //Llamados al optional para asegurarno de que el usuario no existe
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        //Metodo que utilizamos
        when(request.getMethod()).thenReturn("DELETE");
        //Endpoint al que se llama con el id
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost/api/users/" + userId));

        // Act
        //Se llama al metodo con los parametros necesarios
        ResponseEntity<?> response = userRestController.deleteUser(userId, request);

        // Assert
        //Se comprueba que la respuesta tenga el código de respuesta correcto
        //En este caso no se encuentra el usuario por lo tanto el estado NOT_FOUND
        //es un estado correcto en nuestro testing
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody() instanceof GlobalResponseHandler);
        verify(userRepository, never()).deleteById(anyLong()); // Verifica que no se llame a deleteById
    }

}