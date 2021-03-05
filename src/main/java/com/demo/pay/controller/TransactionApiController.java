package com.demo.pay.controller;

import com.demo.pay.entity.*;
import com.demo.pay.entity.ResponseBody;
import com.demo.pay.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1")
public class TransactionApiController {
    private final TransactionService transactionService;

    @PostMapping("/distribute")
    ResponseEntity<DistributeTransaction> distribute(
            @RequestHeader(Header.ROOM_ID) String roomId,
            @RequestHeader(Header.USER_ID) long userId,
            @RequestBody RequesedtBody requesedtBody,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        log.debug("roomid={}, userid={}, amount={}, count={}", roomId, userId, requesedtBody.getAmount(), requesedtBody.getCount());

        DistributeTransaction distributeTransaction = transactionService.distribute(roomId, userId, requesedtBody.getAmount(), requesedtBody.getCount());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponentsBuilder.path("/{token}").buildAndExpand(distributeTransaction.getToken()).toUri());

        return new ResponseEntity<>(distributeTransaction, httpHeaders, HttpStatus.CREATED);
    }

    @PutMapping("/{token:[a-zA-Z]{3}}")
    long receive(
            @RequestHeader(Header.ROOM_ID) String roomId,
            @RequestHeader(Header.USER_ID) long userId,
            @PathVariable("token") String token
    ) {
        log.debug("roomid={}, userid={}, token={}", roomId, userId, token);

        long amount = transactionService.receive(roomId, userId, token);

        return amount;
    }

    @GetMapping(value = "/{token:[a-zA-Z]{3}}")
    ResponseBody findTransaction(
            @RequestHeader(Header.ROOM_ID) String roomId,
            @RequestHeader(Header.USER_ID) long userId,
            @PathVariable("token") String token
    ) {
        log.debug("roomId={}, userId={}, token={}", roomId, userId, token);

        DistributeTransaction distributeTransaction = transactionService.findTransaction(userId, token);

        ResponseBody responseBody = new ResponseBody(
                distributeTransaction.getCreatedDateTime(),
                distributeTransaction.getAmount(),
                distributeTransaction.getReceiveTransactions()
                        .stream()
                        .filter(ReceiveTransaction::isReceived)
                        .mapToLong(ReceiveTransaction::getAmount)
                        .sum(),
                distributeTransaction.getReceiveTransactions()
                        .stream()
                        .filter(ReceiveTransaction::isReceived)
                        .map(it -> new ResponseBodyPair(it.getUserId(), it.getAmount()))
                        .collect(Collectors.toList())
        );

        return responseBody;
    }
}
