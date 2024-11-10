package com.example.vcloset.rest.clothing;

import com.example.vcloset.logic.entity.clothing.Clothing;
import com.example.vcloset.logic.entity.clothing.ClothingRepository;
import com.example.vcloset.logic.entity.http.GlobalResponseHandler;
import com.example.vcloset.logic.entity.http.Meta;
import com.example.vcloset.logic.entity.user.User;
import com.example.vcloset.logic.entity.user.UserRepository;
import com.example.vcloset.logic.entity.clothing.clothingType.ClothingType;
import com.example.vcloset.logic.entity.clothing.clothingType.ClothingTypeRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/user/{userId}/clothing")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllByUser (@PathVariable Long userId,
                                           @RequestParam(defaultValue = "1") int page,
                                           @RequestParam(defaultValue = "10") int size,
                                           HttpServletRequest request) {
        Optional<User> foundUser = userRepository.findById(userId);
        if(foundUser.isPresent()) {


            Pageable pageable = PageRequest.of(page-1, size);
            Page<Clothing> ordersPage = clothingRepository.getOrderByUserId(userId, pageable);
            Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
            meta.setTotalPages(ordersPage.getTotalPages());
            meta.setTotalElements(ordersPage.getTotalElements());
            meta.setPageNumber(ordersPage.getNumber() + 1);
            meta.setPageSize(ordersPage.getSize());

            return new GlobalResponseHandler().handleResponse("Order retrieved successfully",
                    ordersPage.getContent(), HttpStatus.OK, meta);
        } else {
            return new GlobalResponseHandler().handleResponse("User id " + userId + " not found"  ,
                    HttpStatus.NOT_FOUND, request);
        }
    }
}
