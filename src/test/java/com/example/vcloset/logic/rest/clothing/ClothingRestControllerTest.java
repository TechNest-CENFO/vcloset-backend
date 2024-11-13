package com.example.vcloset.logic.rest.clothing;

import com.example.vcloset.logic.entity.clothing.Clothing;
import com.example.vcloset.logic.entity.clothing.ClothingRepository;
import com.example.vcloset.logic.entity.http.GlobalResponseHandler;
import com.example.vcloset.logic.entity.http.Meta;
import com.example.vcloset.logic.entity.user.User;
import com.example.vcloset.logic.entity.user.UserRepository;
import com.example.vcloset.logic.entity.clothing.clothingType.ClothingType;
import com.example.vcloset.logic.entity.clothing.clothingType.ClothingTypeRepository;
import com.example.vcloset.rest.clothing.ClothingRestController;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ClothingRestControllerTest {
    @InjectMocks
    private ClothingRestController clothingRestController;

    @Mock
    private ClothingRepository clothingRepository;

    @Mock
    private UserRepository userRepository;

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
    void testAddClothing_Null() {
        // Arrange: Crear un nuevo usuario y tipo de ropa
        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("testuser@example.com");

        ClothingType clothingType = new ClothingType();
        clothingType.setId(1L);


        Clothing clothing = new Clothing();
        clothing.setClothingType(clothingType);
        clothing.setName("T-Shirt");
//Metodo que utilizamos
        when(request.getMethod()).thenReturn("GET");
        //Endpoint al que se llama con el id
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost/api/user/{userId}"));

        // Simular que el repositorio encuentra el usuario y el tipo de ropa
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(clothingTypeRepository.findById(1L)).thenReturn(Optional.of(clothingType));
        when(clothingRepository.save(any(Clothing.class))).thenReturn(clothing);


        // Act: Llamar al método addClothing
        ResponseEntity<?> response = clothingRestController.addClothing(1L, clothing, request);

        // Assert: Verificar que el resultado es el esperado
        assertNull(response);


        // Verificar interacciones con los mocks
        verify(userRepository, times(1)).findById(1L);
        verify(clothingTypeRepository, times(1)).findById(1L);
        verify(clothingRepository, times(1)).save(any(Clothing.class));
        verify(globalResponseHandler, times(1)).handleResponse(
                anyString(), any(), eq(HttpStatus.CREATED), eq(request)
        );
    }

    @Test
    void testGetClothing_Error() {
        // Arrange: Simular la respuesta de ClothingRepository
        Clothing clothing1 = new Clothing();
        clothing1.setId(1L);
        clothing1.setName("Shirt");


        Clothing clothing2 = new Clothing();
        clothing2.setId(2L);
        clothing2.setName("Jeans");

        //Metodo que utilizamos
        when(request.getMethod()).thenReturn("GET");
        //Endpoint al que se llama con el id
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/clothing"));


        when(clothingRepository.findAll()).thenReturn(List.of(clothing1, clothing2));

        when(globalResponseHandler.handleResponse(
                anyString(), any(), eq(HttpStatus.INTERNAL_SERVER_ERROR), eq(request))
        );

        // Act: Llamar al método getClothing
        ResponseEntity<?> response = clothingRestController.getClothing(request);

        // Assert: Verificar que el resultado es el esperado
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

    }


    @Test
    void testGetAllByUser_Error() {
        // Arrange: Crear un usuario y ropas
        User user = new User();
        user.setId(1L);
        user.setName("Test User");

        Clothing clothing1 = new Clothing();
        clothing1.setId(1L);
        clothing1.setName("Shirt");


        Clothing clothing2 = new Clothing();
        clothing2.setId(2L);
        clothing2.setName("Jeans");
        //Metodo que utilizamos
        when(request.getMethod()).thenReturn("GET");
        //Endpoint al que se llama con el id
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/clothing/user/{userId}/clothing"));

        Pageable pageable = PageRequest.of(0, 10);
        Page<Clothing> clothingPage = mock(Page.class);
        when(clothingPage.getContent()).thenReturn(List.of(clothing1, clothing2));
        when(clothingPage.getTotalPages()).thenReturn(1);
        when(clothingPage.getTotalElements()).thenReturn(2L);
        when(clothingPage.getNumber()).thenReturn(0);
        when(clothingPage.getSize()).thenReturn(10);


        // Simular la respuesta del GlobalResponseHandler
        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(1);
        meta.setTotalElements(2L);
        meta.setPageNumber(1);
        meta.setPageSize(10);

        // Act: Llamar al método getAllByUser
        ResponseEntity<?> response = clothingRestController.getAllByUser(1L, 1, 10, request);

        // Assert: Verificar que el resultado es el esperado
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }
}
