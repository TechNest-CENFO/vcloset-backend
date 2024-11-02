package com.example.vcloset.logic.rest;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;


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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;



import java.util.ArrayList;
import java.util.List;
@ExtendWith(MockitoExtension.class)
public class UserRestControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private UserRestController userRestController;

    private Page<User> userPage;

    @BeforeEach
    public void setUp() {
        List<User> userList = new ArrayList<>();
        userList.add(new User());
        userList.add(new User());

        userPage = new PageImpl<>(userList);
    }

    @Test
    public void testGetAll() {
        // Arrange
        int page = 1;
        int size = 10;
        Pageable pageable = PageRequest.of(page-1, size);
        when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost/api/users"));

        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(userPage.getTotalPages());
        meta.setTotalElements(userPage.getTotalElements());
        meta.setPageNumber(userPage.getNumber()+1);
        meta.setPageSize(userPage.getSize());

        // Act
        ResponseEntity<?> response = userRestController.getAll(page, size, request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }
}