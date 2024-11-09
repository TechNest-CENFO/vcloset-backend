package com.example.vcloset.rest.clothing.clothingType;

import com.example.vcloset.logic.entity.clothing.clothingType.ClothingType;
import com.example.vcloset.logic.entity.clothing.clothingType.ClothingTypeRepository;
import com.example.vcloset.logic.entity.http.GlobalResponseHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/clothingType")
@RestController
public class ClothingTypeRestController {

    @Autowired
    private ClothingTypeRepository clothingTypeRepository;

    @Autowired
    private GlobalResponseHandler globalResponseHandler;

    @Autowired
    private HttpServletRequest request;

    @GetMapping
    public ResponseEntity<?> getAllClothingTypes(HttpServletRequest request) {
        try {
            Iterable<ClothingType> clothingTypeList = clothingTypeRepository.findAll();
            return new GlobalResponseHandler().handleResponse(
                    "Clothing types retrieved successfully",
                    clothingTypeList,
                    HttpStatus.OK,
                    request
            );
        } catch (Exception e) {
            return new GlobalResponseHandler().handleResponse(
                    "Error retrieving clothing types",
                    e.getMessage(),
                    HttpStatus.BAD_REQUEST,
                    request
            );
        }
    }


}
