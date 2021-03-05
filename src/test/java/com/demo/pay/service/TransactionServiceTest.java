package com.demo.pay.service;

import com.demo.pay.entity.DistributeTransaction;
import com.demo.pay.repository.DistributeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransactionServiceTest {
    @Autowired
    private DistributeRepository distributeRepository;

    @Test
    void distribute() {
        DistributeTransaction distributeTransaction = new DistributeTransaction("AAA", "ROOM1", 111L, 1000L, 3, LocalDateTime.now());
        distributeRepository.save(distributeTransaction);
        Assertions.assertThat(distributeTransaction.getToken()).isEqualTo("AAA");
    }

    @Test
    void receive() {
    }

    @Test
    void  findTransaction(){
        DistributeTransaction distributeTransaction = distributeRepository.findByToken("AAA");
    }
}