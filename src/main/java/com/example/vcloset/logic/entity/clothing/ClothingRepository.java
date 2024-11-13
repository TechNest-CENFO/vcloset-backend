package com.example.vcloset.logic.entity.clothing;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClothingRepository extends JpaRepository<Clothing, Long> {

    Page<Clothing> getOrderByUserId(Long id, Pageable pageable);
}