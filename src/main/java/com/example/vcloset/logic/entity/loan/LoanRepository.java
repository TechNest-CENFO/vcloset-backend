package com.example.vcloset.logic.entity.loan;

import com.example.vcloset.logic.entity.clothing.Clothing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    @Query("SELECT c " +
            "FROM Clothing c " +
            "LEFT JOIN Loan l " +
            "on l.clothing.id = c.id " +
            "WHERE c.isClothingItemActive = true " +
            "and c.isPublic = true " +
            "AND (l.isItemBorrowed = false OR l IS NULL) " +
            "and c.user.id != :userId " +
            "order by c.id")
    Page<Clothing> findByPublicClothingItem(Long userId, Pageable pageable);

    @Query("SELECT l " +
            "FROM Loan l " +
            "WHERE l.isItemRequested = true " +
            "and l.lenderUser.id = :userId " +
            "and l.requestStatus = 'PENDING' ")
    Page<Loan> findMyReceivedRequests(Long userId, Pageable pageable);

    @Query("SELECT l " +
            "FROM Loan l " +
            "WHERE l.isItemRequested = true " +
            "and l.loanerUser.id = :userId")
    Page<Loan> findMySentRequests(Long userId, Pageable pageable);

    @Query("SELECT l " +
            "FROM Clothing c " +
            "JOIN Loan l " +
            "on c.id = l.clothing.id " +
            "WHERE c.isClothingItemActive = true " +
            "and c.isPublic = true " +
            "and l.lenderUser.id = :userId")
    Page<Loan> findMyLends(Long userId, Pageable pageable);

    @Query("SELECT l " +
            "FROM Clothing c " +
            "JOIN Loan l " +
            "on c.id = l.clothing.id " +
            "WHERE c.isClothingItemActive = true " +
            "and c.isPublic = true " +
            "and l.loanerUser.id = :userId")
    Page<Loan> findMyLoans(Long userId, Pageable pageable);

//    @Query("SELECT c FROM Loan c")
//    Page<Loan> findMyLoans(Pageable pageable);
}
