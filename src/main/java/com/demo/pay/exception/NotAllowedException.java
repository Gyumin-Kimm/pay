package com.demo.pay.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotAllowedException extends RuntimeException{
    private static final String MESSAGE= "허가되지 않은 Transaction 입니다";
    public NotAllowedException(){
        super(MESSAGE);
        log.error(MESSAGE);
    }
}
