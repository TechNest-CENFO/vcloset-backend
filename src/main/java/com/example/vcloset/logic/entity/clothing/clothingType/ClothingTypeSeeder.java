package com.example.vcloset.logic.entity.clothing.clothingType;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
public class ClothingTypeSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final ClothingTypeRepository clothingTypeRepository;


    public ClothingTypeSeeder(ClothingTypeRepository clothingTypeRepository) {
        this.clothingTypeRepository = clothingTypeRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.loadClothingTypes();
    }

    private void loadClothingTypes() {
        ClothingTypeEnum[] clothingTypeNames = new ClothingTypeEnum[] { ClothingTypeEnum.TOP, ClothingTypeEnum.BOTTOM, ClothingTypeEnum.OUTWEAR, ClothingTypeEnum.FOOTWEAR, ClothingTypeEnum.FULLBODY, ClothingTypeEnum.HEAD, ClothingTypeEnum.FORMAL_ACCESSORY, ClothingTypeEnum.ACCESSORY };

        Arrays.stream(clothingTypeNames).forEach((clothingTypeName) -> {
            Optional<ClothingType> optionalClothingType = clothingTypeRepository.findByName(clothingTypeName);

            optionalClothingType.ifPresentOrElse(System.out::println, () -> {
                ClothingType clothingTypeToCreate = new ClothingType();

                clothingTypeToCreate.setName(clothingTypeName);

                clothingTypeRepository.save(clothingTypeToCreate);
            });
        });
    }
}
