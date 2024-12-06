package com.example.vcloset.logic.entity.user;

import com.example.vcloset.logic.entity.reports.DTO.UsersPerMonthDTO;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>  {
    @Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE %?1%")
    List<User> findUsersWithCharacterInName(String character);

    @Query("SELECT u FROM User u WHERE u.name = ?1")
    Optional<User> findByName(String name);

    Optional<User> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.password = :password WHERE u.email = :email")
    void updatePassword(@Param("email") String email, @Param("password") String password);

    @Transactional
    // Usamos @Query para llamar al stored procedure directamente
    @Query(value = "CALL GetUsersCreatedLast12Months()", nativeQuery = true)
    List<Object[]> getUsersCreatedLast12Months();


    // Contar todos los usuarios
    @Transactional
    @Query("SELECT COUNT(u) FROM User u")
    int countAllUsers();

    // Contar los usuarios inactivos (is_user_active = 0)
    @Transactional
    @Query("SELECT COUNT(u) FROM User u WHERE u.isUserActive = false")
    int countInactiveUsers();
}
