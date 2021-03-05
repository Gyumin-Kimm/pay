package com.demo.pay.service;

import com.demo.pay.common.AmountDistributer;
import com.demo.pay.common.TokenGenerator;
import com.demo.pay.common.constants.FixedValue;
import com.demo.pay.entity.DistributeTransaction;
import com.demo.pay.entity.ReceiveTransaction;
import com.demo.pay.exception.DuplicatedException;
import com.demo.pay.exception.ExpiredException;
import com.demo.pay.exception.NotAllowedException;
import com.demo.pay.exception.NotFoundException;
import com.demo.pay.repository.DistributeRepository;
import com.demo.pay.repository.ReceiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TransactionService {
    private final TokenGenerator tokenGenerator;
    private final DistributeRepository distributeRepository;
    private final ReceiveRepository receiveRepository;

    @Transactional
    public DistributeTransaction distribute(String roomId, long userId, long amount, int count) {
        //token은 3자리 문자열로 구성되며 예측이 불가능해야 합니다
        String token = tokenGenerator.makeToken();

        DistributeTransaction distributeTransaction = new DistributeTransaction(token, roomId, userId, amount, count, LocalDateTime.now());
        distributeRepository.save(distributeTransaction);

        //뿌릴 금액을 인원수에 맞게 분배하여 저장합니다. (분배 로직은 자유롭게 구현해 주세요.)
        long[] divided = AmountDistributer.divide(amount, count);
        for (int i = 0; i < count; i++) {
            ReceiveTransaction receiveTransaction = new ReceiveTransaction(distributeTransaction, i + 1, divided[i]);
            receiveRepository.save(receiveTransaction);
        }

        return distributeTransaction;
    }

    @Transactional
    public long receive(String roomId, long userId, String token) {
        DistributeTransaction distributeTransaction = distributeRepository.findById(token).orElseThrow(NotFoundException::new);

        //뿌리기 당 한 사용자는 한번만 받을 수 있습니다.
        if (distributeTransaction.getReceiveTransactions()
                .stream()
                .filter(ReceiveTransaction::isReceived)
                .anyMatch(it -> it.getUserId() == userId)) {
            throw new DuplicatedException();
        }

        //뿌리기가 전부 처리 된 경우
        if (distributeTransaction.getReceiveTransactions()
                .stream()
                .noneMatch(ReceiveTransaction::isNotReceived)
        ) {
            throw new ExpiredException();
        }

        //자신이 뿌리기한 건은 자신이 받을 수 없습니다.
        if (distributeTransaction.getUserId() == userId) {
            throw new NotAllowedException();
        }

        //뿌린기가 호출된 대화방과 동일한 대화방에 속한 사용자만이 받을 수 있습니다.
        if (!distributeTransaction.getRoomId().equals(roomId)) {
            throw new NotAllowedException();
        }

        //뿌린 건은 10분간만 유효합니다. 뿌린지 10분이 지난 요청에 대해서는 받기 실패 응답이 내려가야 합니다.
        if (distributeTransaction.isExpired(FixedValue.TIME_OUT_MINUTE)) {
            throw new ExpiredException();
        }

        //token에 해당하는 뿌리기 건 중 아직 누구에게도 할당되지 않은 분배건 하나를
        //API를 호출한 사용자에게 할당하고, 그 금액을 응답값으로 내려줍니다.
        List<ReceiveTransaction> enableReceiveTransactions = distributeTransaction.getReceiveTransactions()
                .stream()
                .filter(ReceiveTransaction::isNotReceived)
                .collect(Collectors.toList());

        ReceiveTransaction receiveTransaction = enableReceiveTransactions.get(0);

        return receiveTransaction.getAmount();
    }

    @Transactional
    public DistributeTransaction findTransaction(long userId, String token) {
        LocalDateTime createdDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX).minusDays(FixedValue.LIMIT_DAYS);

        DistributeTransaction distributeTransaction = distributeRepository.findByTokenAndCreatedDateTimeGreaterThan(token, createdDateTime);
        if (Objects.isNull(distributeTransaction)) {
            throw new NotFoundException();
        }
        if (distributeTransaction.getUserId() != userId) {
            throw new NotAllowedException();
        }

        return distributeTransaction;
    }
}
