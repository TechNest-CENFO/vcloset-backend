DELIMITER //
CREATE PROCEDURE GetClothingByUserAndType(
    IN userIdParam BIGINT,
    IN subTypeTypeParam VARCHAR(50)
)
BEGIN
SELECT c.*
FROM clothing c
         JOIN clothing_type ct ON c.clothing_type_id = ct.id
WHERE c.user_id = userIdParam
  AND (ct.type = subTypeTypeParam OR subTypeTypeParam IS NULL);
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE GetClothingTypeSP(
    IN userIdParam BIGINT
)
BEGIN
SELECT  c.*, ct.type, ct.sub_type, c2.name nameCategory
FROM clothing c
         JOIN clothing_type ct ON c.clothing_type_id = ct.id
         JOIN category_clothing cat ON c.id = cat.clothing_id
         JOIN category c2 on cat.category_id = c2.id
WHERE c.user_id = userIdParam order by ct.type;
END //
DELIMITER ;

CREATE PROCEDURE GetClothingDataSP(
    IN userIdParam BIGINT,
    IN clothingTypeIdParam BIGINT
)
BEGIN
SELECT c.*, ct.type, ct.sub_type
FROM vcloset.clothing c
         JOIN vcloset.clothing_type ct ON c.clothing_type_id = ct.id
WHERE c.user_id = userIdParam
  AND (ct.id = clothingTypeIdParam OR clothingTypeIdParam IS NULL)
ORDER BY ct.type;
END //
DELIMITER ;
