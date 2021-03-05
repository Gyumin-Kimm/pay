package com.demo.pay.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotFoundException extends RuntimeException{
    private static final String MESSAGE = "Transaction이 존재하지 않습니다.";
    public NotFoundException(){
        super(MESSAGE);
        log.error(MESSAGE);
    }
}
