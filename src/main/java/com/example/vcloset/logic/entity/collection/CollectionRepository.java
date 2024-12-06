package com.example.vcloset.logic.entity.collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollectionRepository extends JpaRepository<Collection, Long> {
    List<Collection> findByUserId(Long userId);

    Page<Collection> findByUserIdAndIsDeletedFalse(Long userId, Pageable pageable);

}
