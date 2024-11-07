
package com.example.vcloset.logic.entity.clothing.clothingType;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClothingTypeRepository extends JpaRepository<ClothingType, Long> {
    Optional<ClothingType> findByName(ClothingTypeEnum clothingTypeName);
}
