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

DELIMITER //

CREATE PROCEDURE GetUsersCreatedLast12Months()
BEGIN
    SELECT
        YEAR(created_at) AS year,
        CASE
            WHEN MONTH(created_at) = 1 THEN 'Enero'
            WHEN MONTH(created_at) = 2 THEN 'Febrero'
            WHEN MONTH(created_at) = 3 THEN 'Marzo'
            WHEN MONTH(created_at) = 4 THEN 'Abril'
            WHEN MONTH(created_at) = 5 THEN 'Mayo'
            WHEN MONTH(created_at) = 6 THEN 'Junio'
            WHEN MONTH(created_at) = 7 THEN 'Julio'
            WHEN MONTH(created_at) = 8 THEN 'Agosto'
            WHEN MONTH(created_at) = 9 THEN 'Septiembre'
            WHEN MONTH(created_at) = 10 THEN 'Octubre'
            WHEN MONTH(created_at) = 11 THEN 'Noviembre'
            WHEN MONTH(created_at) = 12 THEN 'Diciembre'
            END AS month,  -- Nombre del mes en español
        COUNT(*) AS users_created
    FROM
        USER
    WHERE
        created_at >= CURDATE() - INTERVAL 12 MONTH  -- Filtrar usuarios de los últimos 12 meses
    GROUP BY
        YEAR(created_at), MONTH(created_at)  -- Agrupar por año y mes
    ORDER BY
        YEAR(created_at) DESC, MONTH(created_at) DESC;  -- Ordenar por año y mes en orden descendente
END//

DELIMITER ;

