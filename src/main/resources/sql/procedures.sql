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
create
definer = vcloset@localhost procedure GetClothingTypeSP(IN userIdParam bigint)
BEGIN
SELECT  c.*, ct.type, ct.sub_type
FROM vcloset.clothing c
         JOIN vcloset.clothing_type ct ON c.clothing_type_id = ct.id
WHERE c.user_id = userIdParam order ct.type;
END; //
DELIMITER ;


DELIMITER //
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

