package com.example.vcloset.rest.clothing;

import com.example.vcloset.logic.entity.clothing.Clothing;
import com.example.vcloset.logic.entity.clothing.ClothingRepository;
import com.example.vcloset.logic.entity.clothing.clothingType.ClothingTypeEnum;
import com.example.vcloset.logic.entity.http.GlobalResponseHandler;
import com.example.vcloset.logic.entity.http.Meta;
import com.example.vcloset.logic.entity.user.User;
import com.example.vcloset.logic.entity.user.UserRepository;
import com.example.vcloset.logic.entity.clothing.clothingType.ClothingType;
import com.example.vcloset.logic.entity.clothing.clothingType.ClothingTypeRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        if (foundUser.isPresent()) {
            Optional<ClothingType> foundClothingType = clothingTypeRepository.findById(clothing.getClothingType().getId());
            if (foundClothingType.isPresent()) {
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

    @GetMapping("/user/{userId}/clothing")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllByUser(@PathVariable Long userId,
                                          @RequestParam(defaultValue = "1") int page,
                                          @RequestParam(defaultValue = "10") int size,
                                          HttpServletRequest request) {
        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isPresent()) {
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<Clothing> clothingPage = clothingRepository.findByIsClothingItemActiveTrueAndUserId(userId, pageable);
            Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
            meta.setTotalPages(clothingPage.getTotalPages());
            meta.setTotalElements(clothingPage.getTotalElements());
            meta.setPageNumber(clothingPage.getNumber() + 1);
            meta.setPageSize(clothingPage.getSize());
            return new GlobalResponseHandler().handleResponse("Clothing Items retrieved successfully",
                    clothingPage.getContent(), HttpStatus.OK, meta);
        } else {
            return new GlobalResponseHandler().handleResponse("User id " + userId + " not found",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @GetMapping("/user/{userId}/clothing/isFavorite")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllFavoritesByUser(@PathVariable Long userId,
                                                   @RequestParam(defaultValue = "1") int page,
                                                   @RequestParam(defaultValue = "10") int size,
                                                   HttpServletRequest request) {
        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isPresent()) {


            Pageable pageable = PageRequest.of(page - 1, size);
            Page<Clothing> clothingPage = clothingRepository.findByUserIdAndIsFavoriteTrueAndIsClothingItemActiveTrue(userId, pageable);
            Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
            meta.setTotalPages(clothingPage.getTotalPages());
            meta.setTotalElements(clothingPage.getTotalElements());
            meta.setPageNumber(clothingPage.getNumber() + 1);
            meta.setPageSize(clothingPage.getSize());

            return new GlobalResponseHandler().handleResponse("Order retrieved successfully",
                    clothingPage.getContent(), HttpStatus.OK, meta);
        } else {
            return new GlobalResponseHandler().handleResponse("User id " + userId + " not found",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @Transactional
    @GetMapping("/user/{userId}/clothing/{type}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllByType(@PathVariable Long userId,
                                          @PathVariable String type,
                                          @RequestParam(defaultValue = "1") int page,
                                          @RequestParam(defaultValue = "10") int size,
                                          HttpServletRequest request) {
        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isPresent()) {
            Pageable pageable = PageRequest.of(page - 1, size);

            List<Clothing> allClothingItems = clothingRepository.getClothingByUserAndType(userId, type);

            Page<Clothing> clothingPage = new PageImpl<>(allClothingItems, pageable, allClothingItems.size());

            Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
            meta.setTotalPages(clothingPage.getTotalPages());
            meta.setTotalElements(clothingPage.getTotalElements());
            meta.setPageNumber(clothingPage.getNumber() + 1);
            meta.setPageSize(clothingPage.getSize());
            return new GlobalResponseHandler().handleResponse("Item retrieved successfully",
                    clothingPage.getContent(), HttpStatus.OK, meta);
        } else {
            return new GlobalResponseHandler().handleResponse("User id " + userId + " not found",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @PatchMapping("/delete/{clothing_id}")
    @PreAuthorize("isAuthenticated()")
    public Optional<Clothing> deleteClothingItem(@PathVariable  Long clothing_id ,@RequestBody Clothing clothing, HttpServletRequest request) {

        return clothingRepository.findById(clothing_id)
                .map(existingClothingItem -> {
                    existingClothingItem.setClothingItemActive(false);
                    return clothingRepository.save(existingClothingItem);
                });
    }

    @PutMapping("/edit/user/{userId}/item/{clothingId}")
    public ResponseEntity<?> editClothingItem(@PathVariable Long clothingId,@PathVariable Long userId, @RequestBody Clothing clothing, HttpServletRequest request) {
        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isPresent()) {
            Optional<ClothingType> foundClothingType = clothingTypeRepository.findById(clothing.getClothingType().getId());
            if (foundClothingType.isPresent()) {
                clothing.setUser(foundUser.get());
                Clothing savedClothing = clothingRepository.save(clothing);
                return globalResponseHandler.handleResponse("Clothing updated successfully",
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

    @PatchMapping("/setIsFavorite/user/{userId}/item/{clothingId}")
    public ResponseEntity<?> setIsFavorite(@PathVariable Long clothingId,@PathVariable Long userId, @RequestBody Clothing clothing, HttpServletRequest request) {
        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isPresent()) {
            Optional<Clothing> foundClothing = clothingRepository.findById(clothing.getId());
            if (foundClothing.isPresent()) {
                clothing.setIsFavorite(!foundClothing.get().getIsFavorite());
                clothing.setUser(foundUser.get());
                Clothing savedClothing = clothingRepository.save(clothing);
                return globalResponseHandler.handleResponse("Clothing updated successfully",
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
}
