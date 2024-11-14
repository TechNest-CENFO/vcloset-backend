package com.example.vcloset.logic.rest.clothing.clothingType;

import com.example.vcloset.logic.entity.clothing.clothingType.ClothingTypeRepository;
import com.example.vcloset.logic.entity.http.GlobalResponseHandler;
import com.example.vcloset.rest.clothing.clothingType.ClothingTypeRestController;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ClothingTypeRestControllerTest {
    @InjectMocks
    private ClothingTypeRestController clothingTypeRestController;

    @Mock
    private ClothingTypeRepository clothingTypeRepository;

    @Mock
    private GlobalResponseHandler globalResponseHandler;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa los mocks
    }

    @Test
    void testGetAllClothingTypes_Failure() {
        //Metodo que utilizamos
        when(request.getMethod()).thenReturn("GET");
        //Endpoint al que se llama con el id
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/clothingType"));
        // Arrange: Simula una excepción
        when(clothingTypeRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        when(globalResponseHandler.handleResponse(
                anyString(),
                any(),
                eq(HttpStatus.BAD_REQUEST),
                eq(request))
        );

        // Act: Llamada al método
        ResponseEntity<?> response = clothingTypeRestController.getAllClothingTypes(request);

        // Assert: Verificar que la respuesta es la esperada
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }
}
