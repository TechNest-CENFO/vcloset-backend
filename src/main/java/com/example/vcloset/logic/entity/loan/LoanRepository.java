package com.example.vcloset.logic.entity.loan;

import com.example.vcloset.logic.entity.clothing.Clothing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    @Query("SELECT c FROM Clothing c WHERE c.isClothingItemActive = true and c.isPublic = true and c.user.id != :userId")
    Page<Clothing> findByPublicClothingItem(Long userId, Pageable pageable);

    @Query("SELECT l FROM Loan l WHERE l.isItemRequested = true and l.lenderUser.id = :userId")
    Page<Loan> findMyReceivedRequests(Long userId, Pageable pageable);

    @Query("SELECT l FROM Loan l WHERE l.isItemRequested = true and l.loanerUser.id = :userId")
    Page<Loan> findMySentRequests(Long userId, Pageable pageable);

    @Query("SELECT c FROM Clothing c WHERE c.isClothingItemActive = true and c.isPublic = true and c.user.id != :userId")
    Page<Clothing> findMyLends(Long userId, Pageable pageable);

    @Query("SELECT c FROM Clothing c WHERE c.isClothingItemActive = true and c.isPublic = true and c.user.id != :userId")
    Page<Clothing> findMyLoans(Long userId, Pageable pageable);

    @Query("SELECT c FROM Loan c")
    Page<Loan> findMyLoans(Pageable pageable);
}
