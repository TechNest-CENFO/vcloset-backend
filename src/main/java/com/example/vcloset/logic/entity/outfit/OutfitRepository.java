package com.example.vcloset.logic.entity.outfit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface OutfitRepository extends JpaRepository<Outfit, Long> {

    Page<Outfit> findByUserId(Long userId, Pageable pageable);

    Page<Outfit> findByUserIdAndIsFavoriteTrue(Long userId, Pageable pageable);

    Page<Outfit> findByUserIdAndIsDeletedFalse(Long userId, Pageable pageable);

    Page<Outfit> findByUserIdAndIsFavoriteTrueAndIsDeletedFalse(Long userId, Pageable pageable);

    @Procedure(name = "GetOutfitByUserAndCategory")
    List<Outfit> getOutfitByUserAndCategory(
            @Param("userIdParam") Long userId,
            @Param("categoryParam") String category);

    @Query(value = "CALL GetClothingTypeSP(:userIdParam)", nativeQuery = true)
    List<Map<String, Object>> GetClothingTypeSP(
            @Param("userIdParam") Long userId);
}
