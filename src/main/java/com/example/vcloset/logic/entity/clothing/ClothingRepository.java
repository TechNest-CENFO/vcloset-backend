package com.example.vcloset.logic.entity.clothing;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClothingRepository extends JpaRepository<Clothing, Long> {

    Page<Clothing> findByUserId(Long userId, Pageable pageable);

    List<Clothing> findByUserId(Long userId);

    Page<Clothing> findByUserIdAndIsFavoriteTrue(Long userId, Pageable pageable);

    @Procedure(name = "GetClothingByUserAndType")
    List<Clothing> getClothingByUserAndType(
            @Param("userIdParam") Long userId,
            @Param("typeParam") String type);
}