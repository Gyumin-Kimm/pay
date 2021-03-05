package com.demo.pay.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ResponseBodyPair {
    private final long userId;

    private final long amount;
}
