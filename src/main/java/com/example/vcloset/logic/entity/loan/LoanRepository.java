package com.example.vcloset.logic.entity.loan;

import com.example.vcloset.logic.entity.clothing.Clothing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    @Query("SELECT c FROM Clothing c WHERE c.isClothingItemActive = true and c.isPublic = true and c.user.id != :userId")
    Page<Clothing> findByPublicClothingItem(Long userId, Pageable pageable);

    @Query("SELECT c FROM Loan l join Clothing c on l.clothing.id = c.id WHERE l.isItemRequested = true")
    Page<Clothing> findMyRequests(Long userId, Pageable pageable);

    @Query("SELECT c FROM Clothing c WHERE c.isClothingItemActive = true and c.isPublic = true and c.user.id != :userId")
    Page<Clothing> findMyLends(Long userId, Pageable pageable);

    @Query("SELECT c FROM Clothing c WHERE c.isClothingItemActive = true and c.isPublic = true and c.user.id != :userId")
    Page<Clothing> findMyLoans(Long userId, Pageable pageable);
}
