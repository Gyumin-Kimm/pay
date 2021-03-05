package com.demo.pay.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Data
public class DistributeTransaction {
    @Id
    @NonNull
    private String token;

    @NonNull
    private String roomId;

    @NonNull
    private long userId;

    @NonNull
    private long amount;

    @NonNull
    private long count;

    @NonNull
    private LocalDateTime createdDateTime;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.EAGER)
    private List<ReceiveTransaction> ReceiveTransactions;

    public List<ReceiveTransaction> getReceiveTransactions(){
        if(Objects.isNull(ReceiveTransactions)){
            ReceiveTransactions = new ArrayList<>();
        }
        return ReceiveTransactions;
    }

    public boolean isExpired(int minutes){
        return createdDateTime.isBefore(LocalDateTime.now().minusMinutes(minutes));
    }
}
