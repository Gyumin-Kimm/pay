package com.demo.pay.common;

import org.apache.commons.lang3.RandomUtils;

public class AmountDistributer {

    public static long[] divide(long amount, int count) {
        long[] array = new long[count];
        long max = RandomUtils.nextLong(amount / count, amount / count * 2);
        for (int i = 0; i < count - 1; i++) {
            if (amount > 0) {
                array[i] = RandomUtils.nextLong(1, Math.min(max, amount));
                amount -= array[i];
            }
            else{
                array[i] = 0;
            }
        }
        array[count - 1] = amount;
        return array;
    }
}
