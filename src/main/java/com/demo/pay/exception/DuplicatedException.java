package com.demo.pay.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DuplicatedException extends RuntimeException{
    private static final String MESSAGE = "이미 완료된 Transaction 입니다.";
    public DuplicatedException(){
        super(MESSAGE);
        log.error(MESSAGE);
    }
}
