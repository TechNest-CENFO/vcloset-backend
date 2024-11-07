package com.example.vcloset.rest.clothing;

import com.example.vcloset.logic.entity.clothing.Clothing;
import com.example.vcloset.logic.entity.clothing.ClothingRepository;
import com.example.vcloset.logic.entity.http.GlobalResponseHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/clothing")
@RestController
public class ClothingRestController {

    @Autowired
    private ClothingRepository clothingRepository;

    @Autowired
    private GlobalResponseHandler globalResponseHandler;

    @Autowired
    private HttpServletRequest request;


    @GetMapping
    public ResponseEntity<?> getAllClothing(HttpServletRequest request) {
        try {
            Iterable<Clothing> clothingList = clothingRepository.findAll();
            return new GlobalResponseHandler().handleResponse(
                    "Clothing retrieved successfully",
                    clothingList,
                    HttpStatus.OK,
                    request
            );
        } catch (Exception e) {
            return new GlobalResponseHandler().handleResponse(
                    "Error retrieving clothing",
                    e.getMessage(),
                    HttpStatus.BAD_REQUEST,
                    request
            );
        }
    }

    @PostMapping
    public ResponseEntity<?> addClothing(@RequestBody Clothing clothing, HttpServletRequest request) {
        Clothing savedClothing = clothingRepository.save(clothing);
        return new GlobalResponseHandler().handleResponse(
                "Clothing created successfully",
                savedClothing,
                HttpStatus.CREATED,
                request
        );
    }
}

/*
*     @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<?> addCategoria(@RequestBody Categoria categoria, HttpServletRequest request) {
        Categoria savedCategoria = categoriaRepository.save(categoria);
        return new GlobalResponseHandler().handleResponse("Categoria created successfully",
                savedCategoria, HttpStatus.CREATED, request);
    }
* */
