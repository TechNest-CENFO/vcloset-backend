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

    private CategoryRepository categoryRepository;
  
    private List<Clothing> outfit = new ArrayList<>();
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

        outfit = new ArrayList<>();
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
                Object subTypeObject = categories.get(SUB_TYPE);

                selectRandomFromList(categories.get(INFERIOR));
                selectRandomFromList(categories.get(CALZADO));
                selectRandomFromList(categories.get(ABRIGO));
            } else {
                // Si es 'false', se selecciona de 'CUERPO_COMPLETO'
                selectRandomFromList(categories.get(CUERPO_COMPLETO));
                if(!isDress) {
                    selectRandomFromList(categories.get(SUPERIOR));
                }
                selectRandomFromList(categories.get(CALZADO));
                selectRandomFromList(categories.get(ACCESORIO));
            }
        } else {
            // Si 'SUPERIOR' no tiene elementos selecciona de cuerpo completo
            if(!categories.get(SUPERIOR).isEmpty()){
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


            }else if (!categories.get(CUERPO_COMPLETO).isEmpty()){
                selectRandomFromList(categories.get(CUERPO_COMPLETO));
                if(!isDress) {
                    selectRandomFromList(categories.get(SUPERIOR));
                }
                selectRandomFromList(categories.get(CALZADO));
                selectRandomFromList(categories.get(ACCESORIO));
            }
        }
    }


    //Método para seleccionar aleatoriamente un elemento de la lista
    private void selectRandomFromList(List<Map<String, String>> list) {
        Map<String, String> selectedItem = new HashMap<>();
        int index = 0;
        if (!list.isEmpty()) {
            if (list.size() > 1) {
                Random random = new Random();
                index = random.nextInt(list.size());
                selectedItem = list.get(index);
            }else{
                selectedItem = list.getFirst();

            }
            isDress = selectedItem.get(SUB_TYPE).contains(VESTIDOS);
            addClothing(selectedItem);


        }

    }

    private void addClothing(Map<String, String> selectedItem) {

        Clothing item = new Clothing();
        item.setId(Long.valueOf(selectedItem.get(ID)));
        item.setImageUrl(selectedItem.get(IMAGE_URL));
        outfit.add(item);
    }

    private Map<String, String> addRow(Map<String, Object> row) {
        Map<String,String> result = new HashMap<>();
        result.put(ID, String.valueOf(row.get(ID)));
        result.put(SUB_TYPE, (String) row.get(SUB_TYPE));
        result.put(COLOR,(String) row.get(COLOR));
        result.put(IMAGE_URL, (String) row.get(IMAGE_URL));
        return result;
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

    private Map<String, Long> calculateTrends(List<Map<String, Object>> temporal, String trendKey) {
        return temporal.stream()
                .filter(row -> row != null && row.get(trendKey) != null) // Valida clave y valor
                .collect(Collectors.groupingBy(
                        row -> (String) row.get(trendKey),
                        Collectors.counting()
                ));
    }

    private String getMostFrequent(Map<String, Long> trendMap) {
        if (trendMap == null || trendMap.isEmpty()) {
            return null; // Si el mapa está vacío, retorna nulo
        }
        return trendMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    @Transactional
    @GetMapping("/{userId}/generate")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> generateOutfit(@PathVariable Long userId, HttpServletRequest request) {
        try {
            // Obtiene prendas desde el Store Procedure
            List<Map<String, Object>> temporal = outfitRepository.GetClothingDataSP(userId, null);

            // Identificar tendencias
            Map<String, Long> colorTrends = calculateTrends(temporal, COLOR);
            Map<String, Long> categoryTrends = calculateTrends(temporal, TYPE);

            String trendingColor = getMostFrequent(colorTrends);
            String trendingCategory = getMostFrequent(categoryTrends);

            // Generar outfit
            getTypeList(temporal, trendingColor, trendingCategory);

            return new GlobalResponseHandler().handleResponse(
                    "Outfit generado con éxito",
                    outfit,
                    HttpStatus.OK,
                    request
            );
        } catch (Exception e) {
            e.printStackTrace(); // Usa un logger en producción
            return new GlobalResponseHandler().handleResponse(
                    "Error al tratar de generar el outfit",
                    "Detalles del error: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST,
                    request
            );
        }
    }

    private void getTypeList(List<Map<String, Object>> temporal, String trendingColor, String trendingCategory) {
        outfit = new ArrayList<>();
        Map<String, List<Map<String, String>>> categories = new HashMap<>();

        // Agrupa por categorías
        initializeCategories(categories, temporal);

        // Lógica de generación
        for (String category : categories.keySet()) {
            List<Map<String, String>> items = categories.get(category);

            if (items != null && !items.isEmpty()) {
                // Filtra por tendencias de color y categoría
                List<Map<String, String>> trendingItems = items.stream()
                        .filter(item -> {
                            boolean colorMatch = trendingColor != null && trendingColor.equals(item.get(COLOR));
                            boolean categoryMatch = trendingCategory != null && trendingCategory.equals(category);
                            return colorMatch || categoryMatch;
                        })
                        .collect(Collectors.toList());

                // Selección con preferencia por tendencias
                if (!trendingItems.isEmpty()) {
                    safeSelectFromList(trendingItems); // Usa el nuevo método
                } else {
                    safeSelectFromList(items); // Usa el nuevo método
                }
            }
        }
    }

    private void initializeCategories(Map<String, List<Map<String, String>>> categories, List<Map<String, Object>> temporal) {
        categories.put(SUPERIOR, new ArrayList<>());
        categories.put(INFERIOR, new ArrayList<>());
        categories.put(ABRIGO, new ArrayList<>());
        categories.put(CALZADO, new ArrayList<>());
        categories.put(CUERPO_COMPLETO, new ArrayList<>());
        categories.put(ACCESORIO, new ArrayList<>());

        for (Map<String, Object> row : temporal) {
            if (row != null) {
                String type = (String) row.get(TYPE);
                if (type != null && categories.containsKey(type)) {
                    Map<String, String> processedRow = addRow(row);
                    if (processedRow != null) {
                        categories.get(type).add(processedRow);
                    }
                }
            }
        }
    }

    private void safeSelectFromList(List<Map<String, String>> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("La lista está vacía. No se puede seleccionar un elemento.");
        }
        selectRandomFromList(list); // Llama al método existente con una lista válida
    }


}
