package com.demo.pay.repository;

import com.demo.pay.entity.ReceiveTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiveRepository extends JpaRepository<ReceiveTransaction, Long> {
}
