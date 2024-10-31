package com.example.vcloset.logic.entity.season;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SeasonRepository extends JpaRepository<Season, Long> {
    Optional<Season> findByName(SeasonEnum seasonName);
}
