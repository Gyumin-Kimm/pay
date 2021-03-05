package com.demo.pay.repository;

import com.demo.pay.entity.DistributeTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface DistributeRepository extends JpaRepository<DistributeTransaction, String> {
    DistributeTransaction findByToken(String token);
    DistributeTransaction findByTokenAndCreatedDateTimeGreaterThan(String token, LocalDateTime createdAt);
}
