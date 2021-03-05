package com.demo.pay.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class ResponseBody {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMddHHmmss")
    private final LocalDateTime datetime;

    private final long amount;

    private final long pickupAmount;

    private final List<ResponseBodyPair> pickups;
}
