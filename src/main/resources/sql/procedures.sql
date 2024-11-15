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