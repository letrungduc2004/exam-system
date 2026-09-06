package com.example.exam_system.features.account.repository;

import com.example.exam_system.features.account.entity.InvalidateToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidateTokenRepository extends JpaRepository<InvalidateToken, String> {
}
