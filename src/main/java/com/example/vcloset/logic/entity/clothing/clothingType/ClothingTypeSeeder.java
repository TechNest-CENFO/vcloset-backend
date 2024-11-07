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
        ClothingTypeEnum[] clothingTypeNames = new ClothingTypeEnum[] {
                ClothingTypeEnum.T_SHIRT_SHORT_SLEEVE,
                ClothingTypeEnum.T_SHIRT_LONG_SLEEVE,
                ClothingTypeEnum.T_SHIRT_SLEEVELESS,

                ClothingTypeEnum.SHIRT_CASUAL,
                ClothingTypeEnum.SHIRT_FORMAL,
                ClothingTypeEnum.SHIRT_BUTTON_DOWN,
                ClothingTypeEnum.SHIRT_POLO,

                ClothingTypeEnum.BLOUSE_CASUAL,
                ClothingTypeEnum.BLOUSE_ELEGANT,
                ClothingTypeEnum.BLOUSE_SHORT_SLEEVE,
                ClothingTypeEnum.BLOUSE_LONG_SLEEVE,

                ClothingTypeEnum.SWEATSHIRT_WITH_HOOD,
                ClothingTypeEnum.SWEATSHIRT_WITHOUT_HOOD,
                ClothingTypeEnum.SWEATSHIRT_ZIP_UP,

                ClothingTypeEnum.TOP_CROP_TOP,
                ClothingTypeEnum.TOP_BRALETTE,
                ClothingTypeEnum.TOP_TANK_TOP,

                ClothingTypeEnum.UPPER_OTHERS_OTHER,

                ////////////////////
                //PANTS
                ClothingTypeEnum.PANTS_JEAN,
                ClothingTypeEnum.PANTS_DRESS,
                ClothingTypeEnum.PANTS_SWEETPANTS,
                ClothingTypeEnum.PANTS_CARGO,

                ClothingTypeEnum.SKIRT_PENCIL,
                ClothingTypeEnum.SKIRT_LONG,
                ClothingTypeEnum.SKIRT_MIDI,
                ClothingTypeEnum.SKIRT_MINI,
                ClothingTypeEnum.SKIRT_PLEATED,

                ClothingTypeEnum.LEGGINGS_ATHLETIC,
                ClothingTypeEnum.LEGGINGS_CASUAL,
                ClothingTypeEnum.LEGGINGS_FAUX_LEATHER,

                ClothingTypeEnum.SHORTS_CASUAL,
                ClothingTypeEnum.SHORTS_ATHLETIC,
                ClothingTypeEnum.SHORTS_DENIM,
                ClothingTypeEnum.SHORTS_BERMUDA,

                ClothingTypeEnum.SWIMWEAR_ATHLETIC_SWIM_SHORTS,
                ClothingTypeEnum.SWIMWEAR_CASUAL_SWIM_SHORTS,

                ClothingTypeEnum.OTHERS_CULOTTES,
                ClothingTypeEnum.OTHERS_HIGH_WAISTED_PANTS,
                ClothingTypeEnum.OTHERS_FLARED_PANTS,
                ClothingTypeEnum.OTHERS_LINEN_PANTS,
                ClothingTypeEnum.PANTS_OTHERS_OTHER,

                /////////////////
                //OUTWEAR
                ClothingTypeEnum.COAT_LONG,
                ClothingTypeEnum.COAT_TRENCH,
                ClothingTypeEnum.COAT_WOOL,
                ClothingTypeEnum.COAT_PUFFER,

                ClothingTypeEnum.JACKET_LEATHER,
                ClothingTypeEnum.JACKET_DENIM,
                ClothingTypeEnum.JACKET_BOMBER,
                ClothingTypeEnum.JACKET_KNIT,

                ClothingTypeEnum.WINDBREAKER_WINDBREAKER,

                ClothingTypeEnum.PARKA_LINED,
                ClothingTypeEnum.PARKA_WATERPROOF,

                ClothingTypeEnum.HOODIE_COTTON,
                ClothingTypeEnum.HOODIE_STREET_STYLE,

                ClothingTypeEnum.OTHERS_PONCHO,
                ClothingTypeEnum.OTHERS_CARDIGAN,
                ClothingTypeEnum.OTHERS_RAINCOAT,
                ClothingTypeEnum.OUTWEAR_OTHERS_OTHER,

                ////////////////
                //FOOTWEAR
                ClothingTypeEnum.SNEAKERS_ATHLETIC,
                ClothingTypeEnum.SNEAKERS_CASUAL,
                ClothingTypeEnum.SNEAKERS_HIGH_TOP,

                ClothingTypeEnum.BOOTS_ANKLE,
                ClothingTypeEnum.BOOTS_KNEE_HIGH,
                ClothingTypeEnum.BOOTS_COMBAT,
                ClothingTypeEnum.BOOTS_CHELSEA,

                ClothingTypeEnum.DRESS_SHOES_OXFORDS,
                ClothingTypeEnum.DRESS_SHOES_LOAFERS,
                ClothingTypeEnum.DRESS_SHOES_DERBY,
                ClothingTypeEnum.DRESS_SHOES_MONK_STRAP_SHOES,

                ClothingTypeEnum.SANDALS_FLIP_FLOPS,
                ClothingTypeEnum.SANDALS_SLIDE,
                ClothingTypeEnum.SANDALS_STRAPPY,
                ClothingTypeEnum.SANDALS_GLADIATOR,

                ClothingTypeEnum.HEELS_STILETTO,
                ClothingTypeEnum.HEELS_BLOCK,
                ClothingTypeEnum.HEELS_WEDGE,
                ClothingTypeEnum.HEELS_KITTEN,

                ClothingTypeEnum.FLATS_LOAFERS,
                ClothingTypeEnum.FLATS_SLIP_ON_FLATS,

                ClothingTypeEnum.SLIPPERS_HOUSE_SLIPPERS,
                ClothingTypeEnum.SLIPPERS_MOCCASINS,

                ClothingTypeEnum.OTHERS_RAIN_BOOTS,
                ClothingTypeEnum.OTHERS_HIKING_BOOTS,
                ClothingTypeEnum.FOOTWEAR_OTHERS_OTHER,

                /////////////
                //FULL BODY
                ClothingTypeEnum.DRESS_COCKTAIL_DRESS,
                ClothingTypeEnum.DRESS_CASUAL_DRESS,
                ClothingTypeEnum.DRESS_EVENING_GOWN,
                ClothingTypeEnum.DRESS_MAXI_DRESS,
                ClothingTypeEnum.DRESS_MIDI_DRESS,
                ClothingTypeEnum.DRESS_MINI_DRESS,
                ClothingTypeEnum.DRESS_WRAP_DRESS,
                ClothingTypeEnum.DRESS_SHIRT_DRESS,

                ClothingTypeEnum.OVERALL_DENIM_OVERALL,
                ClothingTypeEnum.OVERALL_SHORT_OVERALL,
                ClothingTypeEnum.OVERALL_UTILITY_OVERALL,

                ClothingTypeEnum.FULL_BODY_OTHERS_OTHER,

                ///////////
                //ACCESSORY
                ClothingTypeEnum.HEAD_WEAR_HAT,
                ClothingTypeEnum.HEAD_WEAR_CAP,
                ClothingTypeEnum.HEAD_WEAR_BEANIE,
                ClothingTypeEnum.HEAD_WEAR_BERET,
                ClothingTypeEnum.HEAD_WEAR_SUN_HAT,

                ClothingTypeEnum.SCARVES_WINTER_SCARF,
                ClothingTypeEnum.SCARVES_CASUAL_SCARF,

                ClothingTypeEnum.ACCESSORY_OTHERS_OTHER
        };

        Arrays.stream(clothingTypeNames).forEach((clothingTypeName) -> {
            Optional<ClothingType> optionalClothingType = clothingTypeRepository.findByName(clothingTypeName);

            optionalClothingType.ifPresentOrElse(System.out::println, () -> {
                ClothingType clothingTypeToCreate = new ClothingType();

                clothingTypeToCreate.setName(clothingTypeName);

                clothingTypeToCreate.setSubType(clothingTypeName.getSubType());

                clothingTypeToCreate.setType(clothingTypeName.getSubType().getType());

                clothingTypeRepository.save(clothingTypeToCreate);
            });
        });
    }
}