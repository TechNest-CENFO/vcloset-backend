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
                ClothingTypeEnum.CAMISETA_MANGA_CORTA,
                ClothingTypeEnum.CAMISETA_MANGA_LARGA,
                ClothingTypeEnum.CAMISETA_SIN_MANGAS,

                ClothingTypeEnum.CAMISA_CASUAL,
                ClothingTypeEnum.CAMISA_FORMAL,
                ClothingTypeEnum.CAMISA_CON_BOTONES_ABAJO,
                ClothingTypeEnum.CAMISA_POLO,

                ClothingTypeEnum.BLUSA_CASUAL,
                ClothingTypeEnum.BLUSA_ELEGANTE,
                ClothingTypeEnum.BLUSA_DE_MANGA_CORTA,
                ClothingTypeEnum.BLUSA_MANGA_LARGA,

                ClothingTypeEnum.SUDADERA_CON_CAPUCHA,
                ClothingTypeEnum.SUDADERA_SIN_CAPUCHA,
                ClothingTypeEnum.SUDADERA_CON_CREMALLERA,

                ClothingTypeEnum.TOP_CROP,
                ClothingTypeEnum.TOP_BRALETTE,
                ClothingTypeEnum.TOP_TANK,

                ClothingTypeEnum.OTROS_TOPS,

                ////////////////////
                //PANTS
                ClothingTypeEnum.PANTALONES_DE_MEZCLILLA,
                ClothingTypeEnum.PANTALONES_DE_TRAJE,
                ClothingTypeEnum.PANTALONES_SWEETPANTS,
                ClothingTypeEnum.PANTALONES_CARGO,

                ClothingTypeEnum.FALDA,
                ClothingTypeEnum.FALDA_LARGA,
                ClothingTypeEnum.FALDA_MEDIA,
                ClothingTypeEnum.FALDA_MINI,
                ClothingTypeEnum.FALDA_PLISADA,

                ClothingTypeEnum.LEGGINGS_ATLETICOS,
                ClothingTypeEnum.LEGGINGS_CASUAL,
                ClothingTypeEnum.LEGGINGS_DE_CUERO_SINTÉTICO,

                ClothingTypeEnum.PANTALONES_CORTOS_CASUALES,
                ClothingTypeEnum.PANTALONES_CORTOS_ATLETICOS,
                ClothingTypeEnum.PANTALONES_CORTOS_DE_MEZCLILLA,
                ClothingTypeEnum.SHORTS_BERMUDA,

                ClothingTypeEnum.BAÑADOR_ATLETICO_PANTALONES_CORTOS,
                ClothingTypeEnum.BAÑADOR_CASUAL_PANTALONES_CORTOS,

                ClothingTypeEnum.OTROS,
                ClothingTypeEnum.OTROS_PANTALONES_DE_CINTURA_ALTA,
                ClothingTypeEnum.OTROS_PANTALONES_ACAMPANADOS,
                ClothingTypeEnum.OTROS_PANTALONES_DE_LINO,
                ClothingTypeEnum.PANTALONES_OTROS_OTROS,

                /////////////////
                //OUTWEAR
                ClothingTypeEnum.ABRIGO_LARGO,
                ClothingTypeEnum.ABRIGO_TRENCH,
                ClothingTypeEnum.ABRIGO_DE_LANA,
                ClothingTypeEnum.ABRIGO_PUFFER,

                ClothingTypeEnum.CHAQUETA_DE_CUERO,
                ClothingTypeEnum.CHAQUETA_DE_MEZCLILLA,
                ClothingTypeEnum.CHAQUETA_DE_PUNTO,
                ClothingTypeEnum.CHAQUETA_DE_PUNTO,

                ClothingTypeEnum.ROMPEVIENTO_ROMPEVIENTO,

                ClothingTypeEnum.PARKA_FORRADA,
                ClothingTypeEnum.PARKA_IMPERMEABLE,

                ClothingTypeEnum.SUDADERA_DE_ALGODÓN,
                ClothingTypeEnum.SUDADERA_ESTILO_CALLE,

                ClothingTypeEnum.OTROS_PONCHO,
                ClothingTypeEnum.OTROS_CÁRDIGAN,
                ClothingTypeEnum.OTROS_IMPERMEABLE,
                ClothingTypeEnum.ABRIGOS_OTROS,

                ////////////////
                //FOOTWEAR
                ClothingTypeEnum.ZAPATILLAS_ATLETICAS,
                ClothingTypeEnum.ZAPATILLAS_CASUALES,
                ClothingTypeEnum.ZAPATILLAS_DE_ALTA_CANA,

                ClothingTypeEnum.BOTAS_DE_TOBILLO,
                ClothingTypeEnum.BOTAS_HASTA_LA_RODILLA,
                ClothingTypeEnum.BOTAS_COMBAT,
                ClothingTypeEnum.BOTAS_CHELSEA,

                ClothingTypeEnum.ZAPATOS_DE_TRAJE_OXFORD,
                ClothingTypeEnum.ZAPATOS_DE_TRAJE_LOAFERS,
                ClothingTypeEnum.ZAPATOS_DE_TRAJE_DERBY,
                ClothingTypeEnum.ZAPATOS_DE_TRAJE_MONK_STRAP,

                ClothingTypeEnum.SANDALIAS_DE_PLAYA,
                ClothingTypeEnum.SANDALIAS_SLIDE,
                ClothingTypeEnum.SANDALIAS_CON_TIRAS,
                ClothingTypeEnum.SANDALIAS_GLADIADOR,

                ClothingTypeEnum.TACONES_STILETTO,
                ClothingTypeEnum.TACONES_BLOQUE,
                ClothingTypeEnum.TACONES_DE_CUNA,
                ClothingTypeEnum.TACONES_KITTEN,

                ClothingTypeEnum.MOCASINES,
                ClothingTypeEnum.ZAPATOS_PLANOS_SLIP_ON,

                ClothingTypeEnum.ZAPATOS_DE_CASA,
                ClothingTypeEnum.MOCASINES,

                ClothingTypeEnum.OTROS_BOTAS_DE_LLUVIA,
                ClothingTypeEnum.OTROS_BOTAS_DE_MONTAÑA,
                ClothingTypeEnum.CALZADO_OTRO_OTRO,

                /////////////
                //FULL BODY
                ClothingTypeEnum.VESTIDO_DE_COCTEL,
                ClothingTypeEnum.VESTIDO_CASUAL,
                ClothingTypeEnum.VESTIDO_DE_NOCHE,
                ClothingTypeEnum.VESTIDO_MAXI,
                ClothingTypeEnum.VESTIDO_MIDI,
                ClothingTypeEnum.VESTIDO_MINI,
                ClothingTypeEnum.VESTIDO_CRUZADO,
                ClothingTypeEnum.VESTIDO_CAMISA,

                ClothingTypeEnum.OVEROL_DE_MEZCLILLA,
                ClothingTypeEnum.OVEROL_CORTO,
                ClothingTypeEnum.OVEROL_DE_UTILIDAD,

                ClothingTypeEnum.CUERPO_COMPLETO_OTRO_OTRO,

                ///////////
                //ACCESSORY
                ClothingTypeEnum.SOMBRERO,
                ClothingTypeEnum.GORRA,
                ClothingTypeEnum.GORRO,
                ClothingTypeEnum.BOINA,
                ClothingTypeEnum.SOMBRERO_DE_SOL,

                ClothingTypeEnum.BUFANDA_DE_INVIERNO,
                ClothingTypeEnum.BUFANDA_CASUAL,

                ClothingTypeEnum.ACCESORIOS_OTROS_OTROS
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