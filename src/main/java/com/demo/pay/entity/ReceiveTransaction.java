package com.demo.pay.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReceiveTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = DistributeTransaction.class, fetch = FetchType.EAGER)
    @NonNull
    private DistributeTransaction distributeTransaction;

    @NonNull
    private Integer seq;

    @NonNull
    private Long amount;

    private Long userId;

    private LocalDateTime datetime;

    public void receive(long userId) {
        this.userId = userId;
        datetime = LocalDateTime.now();
    }

    public boolean isReceived() {
        return Objects.nonNull(userId);
    }

    public boolean isNotReceived() {
        return !isReceived();
    }
}
