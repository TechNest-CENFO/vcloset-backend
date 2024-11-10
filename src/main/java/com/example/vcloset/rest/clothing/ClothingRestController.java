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
import java.util.Optional;

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

    @PostMapping("/user/{userId}")
    public ResponseEntity<?> addClothing(@PathVariable Long userId, @RequestBody Clothing clothing, HttpServletRequest request) {
        Optional<User> foundUser = userRepository.findById(userId);
        if(foundUser.isPresent()) {
            Optional<ClothingType> foundClothingType = clothingTypeRepository.findById(clothing.getClothingType().getId());
            if(foundClothingType.isPresent()) {
                clothing.setUser(foundUser.get());
                Clothing savedClothing = clothingRepository.save(clothing);
                return globalResponseHandler.handleResponse("Clothing created successfully",
                        savedClothing, HttpStatus.CREATED, request);
            } else {
                return globalResponseHandler.handleResponse("Clothing type id " + clothing.getClothingType().getId() + " not found",
                        HttpStatus.NOT_FOUND, request);
            }
        } else {
            return globalResponseHandler.handleResponse("User id " + userId + " not found",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @GetMapping
    public ResponseEntity<?> getClothing(HttpServletRequest request) {
        try {
            return new GlobalResponseHandler().handleResponse(
                    "Clothing retrieved successfully",
                    clothingRepository.findAll(),
                    HttpStatus.OK,
                    request
            );
        } catch (Exception e) {
            return new GlobalResponseHandler().handleResponse(
                    "Error retrieving clothing",
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    request
            );
        }
    }

}
