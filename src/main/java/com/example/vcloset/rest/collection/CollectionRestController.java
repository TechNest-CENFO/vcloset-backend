package com.example.vcloset.rest.collection;

import com.example.vcloset.logic.entity.category.Category;
import com.example.vcloset.logic.entity.clothing.Clothing;
import com.example.vcloset.logic.entity.collection.Collection;
import com.example.vcloset.logic.entity.collection.CollectionRepository;
import com.example.vcloset.logic.entity.http.GlobalResponseHandler;
import com.example.vcloset.logic.entity.http.Meta;
import com.example.vcloset.logic.entity.collection.Collection;
import com.example.vcloset.logic.entity.outfit.Outfit;
import com.example.vcloset.logic.entity.outfit.OutfitRepository;
import com.example.vcloset.logic.entity.user.User;
import com.example.vcloset.logic.entity.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequestMapping("/collection")
@RestController
public class CollectionRestController {

    @Autowired
    private GlobalResponseHandler globalResponseHandler;

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private OutfitRepository outfitRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Collection> collectionPage = collectionRepository.findAll(pageable);
        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(collectionPage.getTotalPages());
        meta.setTotalElements(collectionPage.getTotalElements());
        meta.setPageNumber(collectionPage.getNumber() + 1);
        meta.setPageSize(collectionPage.getSize());

        return new GlobalResponseHandler().handleResponse("Order retrieved successfully",
                collectionPage.getContent(), HttpStatus.OK, meta);
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
            Page<Collection> collectionPage = collectionRepository.findByUserIdAndIsDeletedFalse(userId, pageable);
            Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
            meta.setTotalPages(collectionPage.getTotalPages());
            meta.setTotalElements(collectionPage.getTotalElements());
            meta.setPageNumber(collectionPage.getNumber() + 1);
            meta.setPageSize(collectionPage.getSize());

            return new GlobalResponseHandler().handleResponse("Collection retrieved successfully",
                    collectionPage.getContent(), HttpStatus.OK, meta);
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
            Page<Collection> collectionPage = collectionRepository.findByUserIdAndIsDeletedFalse(userId, pageable);
            Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
            meta.setTotalPages(collectionPage.getTotalPages());
            meta.setTotalElements(collectionPage.getTotalElements());
            meta.setPageNumber(collectionPage.getNumber() + 1);
            meta.setPageSize(collectionPage.getSize());

            return new GlobalResponseHandler().handleResponse("Favorites retrieved successfully",
                    collectionPage.getContent(), HttpStatus.OK, meta);
        } else {
            return new GlobalResponseHandler().handleResponse("User id " + userId + " not found",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @PutMapping("/collection/{collectionId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteCollection(@PathVariable Long collectionId, HttpServletRequest request) {
        Optional<Collection> foundCollection = collectionRepository.findById(collectionId);
        if (foundCollection.isPresent()) {
            Collection collection = foundCollection.get();
            collection.setDeleted(true);
            collectionRepository.save(collection);
            return new GlobalResponseHandler().handleResponse("Collection deleted successfully",
                    collection, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("Collection id " + collectionId + " not found",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<?> addClothing(@PathVariable Long userId, @RequestBody Collection collection, HttpServletRequest request) {

        Set<Outfit> outfitsToAdd = new HashSet<>();
        for (Outfit outfit : collection.getOutfits()) {
            try {
                Outfit foundOutfit = outfitRepository.findById(Long.valueOf(outfit.getId()))
                        .orElseThrow(() -> new IllegalArgumentException("Clothing not found: " + outfit.getId()));
                outfitsToAdd.add(foundOutfit);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isPresent() && !collection.getOutfits().isEmpty()) {
            Collection collectionToAdd = new Collection();
            collectionToAdd.setUser(foundUser.get());

            collectionToAdd.setName(collection.getName());

            for (Outfit outfit : outfitsToAdd) {
                outfit.getCollections().add(collectionToAdd);
            }
            collectionToAdd.setOutfits(outfitsToAdd);

            Collection savedCollection = collectionRepository.save(collectionToAdd);
            return globalResponseHandler.handleResponse("Outfit created successfully",
                    savedCollection, HttpStatus.CREATED, request);
        } else {
            return globalResponseHandler.handleResponse("User id " + userId + " not found",
                    HttpStatus.NOT_FOUND, request);
        }
    }
}
