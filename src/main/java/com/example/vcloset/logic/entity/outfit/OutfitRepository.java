package com.example.vcloset.logic.entity.outfit;

import com.example.vcloset.logic.entity.clothing.Clothing;
import com.example.vcloset.logic.entity.clothing.clothingType.ClothingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface OutfitRepository extends JpaRepository<Outfit, Long> {




    @Query(value = "CALL GetClothingTypeSP(:userIdParam)", nativeQuery = true)
    List<Map<String, Object>> GetClothingTypeSP(
            @Param("userIdParam") Long userId);

    //Iterable<ClothingType> findClothingTypeByUserID(Long id);
}
