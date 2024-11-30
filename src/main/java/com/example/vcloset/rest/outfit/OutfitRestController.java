package com.example.vcloset.rest.outfit;

import com.example.vcloset.logic.entity.category.Category;
import com.example.vcloset.logic.entity.category.CategoryEnum;
import com.example.vcloset.logic.entity.category.CategoryRepository;
import com.example.vcloset.logic.entity.clothing.Clothing;
import com.example.vcloset.logic.entity.clothing.ClothingRepository;
import com.example.vcloset.logic.entity.clothing.clothingType.ClothingTypeSeeder;
import com.example.vcloset.logic.entity.http.GlobalResponseHandler;
import com.example.vcloset.logic.entity.http.Meta;
import com.example.vcloset.logic.entity.outfit.Outfit;
import com.example.vcloset.logic.entity.outfit.OutfitRepository;
import com.example.vcloset.logic.entity.user.User;
import com.example.vcloset.logic.entity.user.UserRepository;
import com.example.vcloset.logic.service.category.CategoryService;
import com.example.vcloset.logic.service.outfit.OutfitService;
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


import java.util.*;
import java.util.stream.Collectors;

@RequestMapping("/outfit")
@RestController
public class OutfitRestController {
    public static final String SUPERIOR = "SUPERIOR";
    public static final String INFERIOR = "INFERIOR";
    public static final String ABRIGO = "ABRIGO";
    public static final String CUERPO_COMPLETO = "CUERPO_COMPLETO";
    public static final String ACCESORIO = "ACCESORIO";
    public static final String TYPE = "type";
    public static final String CALZADO = "CALZADO";
    public static final String COLOR = "color";
    public static final String IMAGE_URL = "image_url";
    public static final String ID = "id";
    public static final String SUB_TYPE = "sub_type";
    public static final String VESTIDOS = "VESTIDOS";
    @Autowired
    private OutfitRepository outfitRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GlobalResponseHandler globalResponseHandler;

    @Autowired
    private ClothingRepository clothingRepository;


    @Autowired
    private CategoryService categoryService;

    @Autowired
    private OutfitService outfitService;
    private List<Clothing> outfit = new ArrayList<>();
    private List<List<Clothing>> weeklyOutfits = new ArrayList<>();


    private boolean isDress = false;
    @Autowired
    private ClothingTypeSeeder clothingTypeSeeder;


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
            Page<Outfit> outfitPage = outfitRepository.findByUserIdAndIsDeletedFalse(userId, pageable);
            Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
            meta.setTotalPages(outfitPage.getTotalPages());
            meta.setTotalElements(outfitPage.getTotalElements());
            meta.setPageNumber(outfitPage.getNumber() + 1);
            meta.setPageSize(outfitPage.getSize());

            return new GlobalResponseHandler().handleResponse("Outfit retrieved successfully",
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
            Page<Outfit> outfitPage = outfitRepository.findByUserIdAndIsFavoriteTrueAndIsDeletedFalse(userId, pageable);
            Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
            meta.setTotalPages(outfitPage.getTotalPages());
            meta.setTotalElements(outfitPage.getTotalElements());
            meta.setPageNumber(outfitPage.getNumber() + 1);
            meta.setPageSize(outfitPage.getSize());

            return new GlobalResponseHandler().handleResponse("Favorites retrieved successfully",
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

            Page<Outfit> outfitPage = new PageImpl<>(allOutfitItems, pageable, allOutfitItems.size());

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
    @GetMapping("/{userId}/random")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllOutfits(@PathVariable Long userId,
                                           HttpServletRequest request) {
        try {

            List<Map<String,Object>> temporal = outfitRepository.GetClothingTypeSP(userId);
            outfit = outfitService.getTypeList(temporal, "random");
            return new GlobalResponseHandler().handleResponse(
                    "Outfit generado con éxito",
                    outfit,
                    HttpStatus.OK,
                    request
            );
        } catch (Exception e) {
            return new GlobalResponseHandler().handleResponse(
                    "Error al tratar de generar el outfit",
                    e.getMessage(),
                    HttpStatus.BAD_REQUEST,
                    request
            );
        }
    }


    @Transactional
    @GetMapping("/{userId}/weekly/{temp}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getWeeklyOutfits(@PathVariable Long userId,
                                              @PathVariable float temp,
                                           HttpServletRequest request) {
        try {

            List<Map<String,Object>> temporal = outfitRepository.GetClothingTypeSP(userId);
            weeklyOutfits = outfitService.getWeeklyOutfits(temporal, "weekly", 24);
            return new GlobalResponseHandler().handleResponse(
                    "Outfit semanal generado con éxito",
                    weeklyOutfits,
                    HttpStatus.OK,
                    request
            );
        } catch (Exception e) {
            return new GlobalResponseHandler().handleResponse(
                    "Error al tratar de generar el outfit semanal",
                    e.getMessage(),
                    HttpStatus.BAD_REQUEST,
                    request
            );
        }
    }


    @PutMapping("/{outfitId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateOutfit(@PathVariable Long outfitId, @RequestBody Outfit outfit, HttpServletRequest request) {
        Optional<Outfit> foundOutfit = outfitRepository.findById(outfitId);

        if (foundOutfit.isPresent()) {
            Outfit existingOutfit = foundOutfit.get();

            existingOutfit.setName(outfit.getName());

            if (outfit.getCategory() != null && outfit.getCategory().getName() != null) {
                try {
                    Category category = categoryService.findByName(outfit.getCategory().getName())
                            .orElseThrow(() -> new IllegalArgumentException("Category not found: " + outfit.getCategory().getName()));
                    existingOutfit.setCategory(category);
                } catch (Exception e) {
                    return globalResponseHandler.handleResponse(
                            "Category error: " + e.getMessage(),
                            HttpStatus.BAD_REQUEST,
                            request
                    );
                }
            }

            Set<Clothing> clothingToAdd = new HashSet<>();
            for (Clothing clothing : outfit.getClothing()) {
                try {
                    Clothing foundClothing = clothingRepository.findById(clothing.getId())
                            .orElseThrow(() -> new IllegalArgumentException("Clothing not found: " + clothing.getId()));
                    clothingToAdd.add(foundClothing);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            if (!clothingToAdd.isEmpty()) {
                for (Clothing clothing : existingOutfit.getClothing()) {
                    clothing.getOutfits().remove(existingOutfit);
                }
                for (Clothing clothing : clothingToAdd) {
                    clothing.getOutfits().add(existingOutfit);
                }
                existingOutfit.setClothing(clothingToAdd);
            }


            existingOutfit.setFavorite(outfit.getFavorite() != null ? outfit.getFavorite() : existingOutfit.getFavorite());
            existingOutfit.setIsPublic(outfit.getIsPublic() != null ? outfit.getIsPublic() : existingOutfit.getIsPublic());
            existingOutfit.setImageUrl(outfit.getImageUrl() != null ? outfit.getImageUrl() : existingOutfit.getImageUrl());

            Outfit updatedOutfit = outfitRepository.save(existingOutfit);

            return globalResponseHandler.handleResponse(
                    "Outfit updated successfully",
                    updatedOutfit,
                    HttpStatus.OK,
                    request
            );
        } else {
            return globalResponseHandler.handleResponse(
                    "Outfit id " + outfitId + " not found",
                    HttpStatus.NOT_FOUND,
                    request
            );
        }
    }



    @PutMapping("/{outfitId}/delete")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteOutfit(@PathVariable Long outfitId, HttpServletRequest request) {
        Optional<Outfit> foundOutfit = outfitRepository.findById(outfitId);
        if (foundOutfit.isPresent()) {
            Outfit outfit = foundOutfit.get();
            outfit.setDeleted(true);
            outfitRepository.save(outfit);
            return new GlobalResponseHandler().handleResponse("Order deleted successfully",
                    outfit, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Order id " + outfitId + " not found",
                    HttpStatus.NOT_FOUND, request);
        }
    }



    @PostMapping("/user/{userId}")
    public ResponseEntity<?> addManualOutfit(@PathVariable Long userId, @RequestBody Outfit outfit, HttpServletRequest request) {

        System.out.println(outfit);

        Set<Clothing> clothingToAdd = new HashSet<>();
        for (Clothing clothing : outfit.getClothing()) {
            try {
                Clothing foundClothing = clothingRepository.findById(clothing.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Clothing not found: " + clothing.getId()));
                clothingToAdd.add(foundClothing);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isPresent() && outfit.getClothing().size()>=2) {
            Outfit outfitToAdd = new Outfit();
            outfitToAdd.setUser(foundUser.get());
            Category category;
            try {
                category = categoryService.findByName(outfit.getCategory().getName())
                        .orElseThrow(() -> new IllegalArgumentException("Category not found: "));
                outfitToAdd.setCategory(category);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            outfitToAdd.setName(outfit.getName());
            outfitToAdd.setImageUrl("test");
            outfitToAdd.setFavorite(false);
            outfitToAdd.setIsPublic(true);

            for (Clothing clothing : clothingToAdd) {
                clothing.getOutfits().add(outfitToAdd);
            }
            outfitToAdd.setClothing(clothingToAdd);

            Outfit savedOutfit = outfitRepository.save(outfitToAdd);
            return globalResponseHandler.handleResponse("Outfit created successfully",
                    savedOutfit, HttpStatus.CREATED, request);
        } else {
            return globalResponseHandler.handleResponse("User id " + userId + " not found",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @PatchMapping("/setIsFavorite/user/{userId}/item/{outfitId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> setIsFavorite(@PathVariable Long outfitId,@PathVariable Long userId, @RequestBody Outfit outfit, HttpServletRequest request) {
        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isPresent()) {
            Optional<Outfit> foundOutfit = outfitRepository.findById(outfitId);
            System.out.println(foundOutfit);
            if (foundOutfit.isPresent()) {
                outfit.setFavorite(!foundOutfit.get().getFavorite());
                outfit.setUser(foundUser.get());
                Outfit savedOutfit = outfitRepository.save(outfit);
                return globalResponseHandler.handleResponse("Outfit updated successfully",
                        savedOutfit, HttpStatus.CREATED, request);
            } else {
                return globalResponseHandler.handleResponse("Outfit with id:  " + outfit.getId() + " not found",
                        HttpStatus.NOT_FOUND, request);
            }
        } else {
            return globalResponseHandler.handleResponse("User id " + userId + " not found",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @PatchMapping("/setIsPublic/user/{userId}/item/{outfitId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> setIsPublic(@PathVariable Long outfitId,@PathVariable Long userId, @RequestBody Outfit outfit, HttpServletRequest request) {
        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isPresent()) {
            Optional<Outfit> foundOutfit = outfitRepository.findById(outfitId);
            System.out.println(foundOutfit);
            if (foundOutfit.isPresent()) {
                outfit.setIsPublic(!foundOutfit.get().getIsPublic());
                outfit.setUser(foundUser.get());
                Outfit savedOutfit = outfitRepository.save(outfit);
                return globalResponseHandler.handleResponse("Outfit updated successfully",
                        savedOutfit, HttpStatus.CREATED, request);
            } else {
                return globalResponseHandler.handleResponse("Outfit with id:  " + outfit.getId() + " not found",
                        HttpStatus.NOT_FOUND, request);
            }
        } else {
            return globalResponseHandler.handleResponse("User id " + userId + " not found",
                    HttpStatus.NOT_FOUND, request);
        }
    }


}
