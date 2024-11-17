package com.example.vcloset.logic.entity.outfit;

import com.example.vcloset.logic.entity.clothing.Clothing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OutfitRepository extends JpaRepository<Outfit, Long> {

    Page<Outfit> findByUserId(Long userId, Pageable pageable);


    Page<Outfit> findByUserIdAndIsFavoriteTrue(Long userId, Pageable pageable);

    @Procedure(name = "GetOutfitByUserAndCategory")
    List<Outfit> getOutfitByUserAndCategory(
            @Param("userIdParam") Long userId,
            @Param("categoryParam") String category);
    /*
    Page<Clothing> findByUserId(Long userId, Pageable pageable);




    * */
}
