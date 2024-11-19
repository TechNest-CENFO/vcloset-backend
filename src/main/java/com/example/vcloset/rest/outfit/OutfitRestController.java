package com.example.vcloset.rest.outfit;

import com.example.vcloset.logic.entity.category.Category;
import com.example.vcloset.logic.entity.category.CategoryEnum;
import com.example.vcloset.logic.entity.clothing.Clothing;
import com.example.vcloset.logic.entity.clothing.ClothingRepository;
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


import java.util.*;

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
    public static final String PICTURE = "picture";
    public static final String COLOR = "color";
    public static final String IMAGE = "image";
    public static final String IMAGE_URL = "image_url";
    @Autowired
    private OutfitRepository outfitRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GlobalResponseHandler globalResponseHandler;

    @Autowired
    private ClothingRepository clothingRepository;

    private Map<String, String> outfit = new HashMap<>();
    private int numberPicture = 0;

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

            List<Map<String, Object>> temporal = outfitRepository.GetClothingTypeSP(userId);
            getTypeList(temporal);
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

    private void getTypeList(List<Map<String, Object>> temporal) {
        numberPicture = 0;
        outfit = new HashMap<>();
        // Crear un mapa para agrupar las listas por tipo
        Map<String, List<Map<String, String>>> categories = new HashMap<>();
        categories.put(SUPERIOR, new ArrayList<>());
        categories.put(INFERIOR, new ArrayList<>());
        categories.put(ABRIGO, new ArrayList<>());
        categories.put(CALZADO, new ArrayList<>());
        categories.put(CUERPO_COMPLETO, new ArrayList<>());
        categories.put(ACCESORIO, new ArrayList<>());

        // Recorrer la lista del Store procedure para desglosarlo en las categorías
        for (Map<String, Object> row : temporal) {
            String type = (String) row.get(TYPE);
            if (categories.containsKey(type)) {
                categories.get(type).add(addRow(row));
            }
        }

        Random random = new Random();
        if (!categories.get(SUPERIOR).isEmpty() && !categories.get(CUERPO_COMPLETO).isEmpty()) {
            boolean randomBool = random.nextBoolean();

            // Si es 'true', se selecciona de 'SUPERIOR'
            if (randomBool) {
                // Selección de SUPERIOR, INFERIOR, CALZADOS, ABRIGOS
                selectRandomFromList(categories.get(SUPERIOR));
                selectRandomFromList(categories.get(INFERIOR));
                selectRandomFromList(categories.get(CALZADO));
                selectRandomFromList(categories.get(ABRIGO));
            } else {
                // Si es 'false', se selecciona de 'CUERPO_COMPLETO'
                selectRandomFromList(categories.get(CUERPO_COMPLETO));
                selectRandomFromList(categories.get(SUPERIOR));
                selectRandomFromList(categories.get(CALZADO));
                selectRandomFromList(categories.get(ACCESORIO));
            }
        } else {
            // Si 'SUPERIOR' no tiene elementos selecciona de cuerpo completo
            if (!categories.get(SUPERIOR).isEmpty()) {
                selectRandomFromList(categories.get(SUPERIOR));
                // Lista de categorías a verificar
                List<String> categoriesToCheck = List.of(
                        INFERIOR, CALZADO, ABRIGO, ACCESORIO
                );
                // Iterar sobre las categorías adicionales
                for (String category : categoriesToCheck) {
                    // Verificar si la categoría no está vacía
                    if (!categories.get(category).isEmpty()) {
                        selectRandomFromList(categories.get(category));
                    }
                }


            } else if (!categories.get(CUERPO_COMPLETO).isEmpty()) {
                selectRandomFromList(categories.get(CUERPO_COMPLETO));
                selectRandomFromList(categories.get(SUPERIOR));
                selectRandomFromList(categories.get(CALZADO));
                selectRandomFromList(categories.get(ACCESORIO));
            }
        }
    }


    //Método para seleccionar aleatoriamente un elemento de la lista
    private void selectRandomFromList(List<Map<String, String>> list) {
        Map<String, String> selectedItem = new HashMap<>();
        if (!list.isEmpty()) {
            numberPicture++;
            if (list.size() > 1) {
                Random random = new Random();
                int index = random.nextInt(list.size());
                selectedItem = list.get(index);
            } else {
                selectedItem = list.getFirst();

            }
            outfit.put(PICTURE + String.valueOf(numberPicture), selectedItem.get(IMAGE));

        }
    }

    private Map<String, String> addRow(Map<String, Object> row) {
        Map<String, String> result = new HashMap<>();
        result.put(COLOR, (String) row.get(COLOR));
        result.put(IMAGE, (String) row.get(IMAGE_URL));
        return result;
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<?> addManualOutfit(@PathVariable Long userId, @RequestBody Outfit outfit, HttpServletRequest request) {

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
        if (foundUser.isPresent()) {
            Outfit outfitToAdd = new Outfit();
            outfitToAdd.setUser(foundUser.get());

            if (outfit.getCategory() == null) {
                Category category = new Category();
                category.setName(CategoryEnum.CASUAL);
                category.setId(1L);
                outfitToAdd.setCategory(category);
            }

            outfitToAdd.setName(outfit.getName());
            outfitToAdd.setImageUrl("test");
            outfitToAdd.setFavorite(false);
            outfitToAdd.setPublic(true);

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
}
