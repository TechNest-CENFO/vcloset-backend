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
SELECT  c.color, c.image_url, ct.type
FROM vcloset.clothing c
         JOIN vcloset.clothing_type ct ON c.clothing_type_id = ct.id
WHERE c.user_id = userIdParam order by ct.type;
END //
DELIMITER ;

