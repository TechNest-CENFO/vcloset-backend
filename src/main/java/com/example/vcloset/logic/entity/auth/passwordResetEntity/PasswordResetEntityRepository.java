package com.example.vcloset.logic.entity.auth.passwordResetEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetEntityRepository extends JpaRepository<PasswordResetEntity, Long> {
    PasswordResetEntity findByToken(String token);
}
