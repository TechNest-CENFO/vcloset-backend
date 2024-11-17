package com.example.vcloset.rest.outfit;

import com.example.vcloset.logic.entity.clothing.Clothing;
import com.example.vcloset.logic.entity.http.GlobalResponseHandler;
import com.example.vcloset.logic.entity.http.Meta;
import com.example.vcloset.logic.entity.outfit.Outfit;
import com.example.vcloset.logic.entity.outfit.OutfitRepository;
import com.example.vcloset.logic.entity.user.User;
import com.example.vcloset.logic.entity.user.UserRepository;
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

@RequestMapping("/outfit")
@RestController
public class OutfitRestController {

    @Autowired
    private OutfitRepository outfitRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GlobalResponseHandler globalResponseHandler;

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Outfit> outfitPage = outfitRepository.findAll(pageable);
        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(outfitPage.getTotalPages());
        meta.setTotalElements(outfitPage.getTotalElements());
        meta.setPageNumber(outfitPage.getNumber() + 1);
        meta.setPageSize(outfitPage.getSize());

        return new GlobalResponseHandler().handleResponse("Order retrieved successfully",
                outfitPage.getContent(), HttpStatus.OK, meta);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllByUser(@PathVariable Long userId,
                                          @RequestParam(defaultValue = "1") int page,
                                          @RequestParam(defaultValue = "10") int size,
                                          HttpServletRequest request) {
        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isPresent()) {
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<Outfit> outfitPage = outfitRepository.findByUserId(userId, pageable);
            Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
            meta.setTotalPages(outfitPage.getTotalPages());
            meta.setTotalElements(outfitPage.getTotalElements());
            meta.setPageNumber(outfitPage.getNumber() + 1);
            meta.setPageSize(outfitPage.getSize());

            return new GlobalResponseHandler().handleResponse("Order retrieved successfully",
                    outfitPage.getContent(), HttpStatus.OK, meta);
        } else {
            return new GlobalResponseHandler().handleResponse("User id " + userId + " not found",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @GetMapping("/user/{userId}/favorite")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllFavoritesByUser(@PathVariable Long userId,
                                                   @RequestParam(defaultValue = "1") int page,
                                                   @RequestParam(defaultValue = "10") int size,
                                                   HttpServletRequest request) {
        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isPresent()) {
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<Outfit> outfitPage = outfitRepository.findByUserIdAndIsFavoriteTrue(userId, pageable);
            Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
            meta.setTotalPages(outfitPage.getTotalPages());
            meta.setTotalElements(outfitPage.getTotalElements());
            meta.setPageNumber(outfitPage.getNumber() + 1);
            meta.setPageSize(outfitPage.getSize());

            return new GlobalResponseHandler().handleResponse("Order retrieved successfully",
                    outfitPage.getContent(), HttpStatus.OK, meta);
        } else {
            return new GlobalResponseHandler().handleResponse("User id " + userId + " not found",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @Transactional
    @GetMapping("/user/{userId}/category/{category}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllByType(@PathVariable Long userId,
                                          @PathVariable String type,
                                          @RequestParam(defaultValue = "1") int page,
                                          @RequestParam(defaultValue = "10") int size,
                                          HttpServletRequest request) {
        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isPresent()) {
            Pageable pageable = PageRequest.of(page - 1, size);

            List<Outfit> allOutfitItems = outfitRepository.getOutfitByUserAndCategory(userId, type);

            Page<Outfit> clothingPage = new PageImpl<>(allOutfitItems, pageable, allOutfitItems.size());

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
}
