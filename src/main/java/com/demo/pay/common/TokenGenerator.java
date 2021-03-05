package com.demo.pay.common;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import static com.demo.pay.common.constants.FixedValue.TOKEN_LENGTH;

@Component
public class TokenGenerator {
    public String makeToken() {
        return RandomStringUtils.randomAlphabetic(TOKEN_LENGTH);
    }
}
