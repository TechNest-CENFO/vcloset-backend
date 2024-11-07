package com.example.vcloset.rest.clothing;

import com.example.vcloset.logic.entity.clothing.Clothing;
import com.example.vcloset.logic.entity.clothing.ClothingRepository;
import com.example.vcloset.logic.entity.http.GlobalResponseHandler;
import com.example.vcloset.logic.entity.user.User;
import com.example.vcloset.logic.entity.user.UserRepository;
import com.example.vcloset.logic.entity.clothing.clothingType.ClothingType;
import com.example.vcloset.logic.entity.clothing.clothingType.ClothingTypeRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/clothing")
@RestController
public class ClothingRestController {

    @Autowired
    private ClothingRepository clothingRepository;

    @Autowired
    private UserRepository userRepository; // Repositorio para User

    @Autowired
    private ClothingTypeRepository clothingTypeRepository; // Repositorio para ClothingType

    @Autowired
    private GlobalResponseHandler globalResponseHandler;

    @PostMapping
    public ResponseEntity<?> addClothing(@RequestBody Clothing clothing, HttpServletRequest request) {
        try {
            // Busca el usuario completo con todos sus datos
            User user = userRepository.findById(clothing.getUser().getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Asigna el usuario completo a la entidad Clothing
            clothing.setUser(user);

            Clothing savedClothing = clothingRepository.save(clothing);
            return new GlobalResponseHandler().handleResponse(
                    "Clothing created successfully",
                    savedClothing,
                    HttpStatus.CREATED,
                    request
            );
        } catch (Exception e) {
            return new GlobalResponseHandler().handleResponse(
                    "Error creating clothing",
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    request
            );
        }
    }
}
