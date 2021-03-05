package com.demo.pay.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExpiredException extends RuntimeException{
    private static final String MESSAGE = "만료된 Transaction 입니다.";
    public ExpiredException(){
        super(MESSAGE);
        log.error(MESSAGE);
    }
}
