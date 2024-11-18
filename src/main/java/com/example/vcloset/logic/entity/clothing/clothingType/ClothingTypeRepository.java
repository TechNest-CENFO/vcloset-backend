
package com.example.vcloset.logic.entity.clothing.clothingType;

import com.example.vcloset.logic.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ClothingTypeRepository extends JpaRepository<ClothingType, Long> {
    Optional<ClothingType> findByName(ClothingTypeEnum clothingTypeName);



}
