package com.cmpay.lemon.monitor.utils;

import com.cmpay.lemon.common.utils.StringUtils;

import java.math.BigDecimal;

public class AmountUtil {
    public static final BigDecimal MIN_AMOUNT = BigDecimal.ZERO;

    private AmountUtil() {
    }

    public static BigDecimal add(BigDecimal d1, BigDecimal d2) {
        return d1.add(d2).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal sub(BigDecimal d1, BigDecimal d2) {
        return d1.subtract(d2).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal mul(BigDecimal d1, BigDecimal d2) {
        return d1.multiply(d2).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal div(BigDecimal d1, BigDecimal d2) {
        if (d2.compareTo(MIN_AMOUNT) == 0) {
            return MIN_AMOUNT;
        }
        return d1.divide(d2, 2, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal mulToInt(BigDecimal d1, BigDecimal d2) {
        return d1.multiply(d2).setScale(0, BigDecimal.ROUND_HALF_UP);
    }

    public static String toString(BigDecimal intd) {
        return intd.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    public static BigDecimal fromString(String dstr) {
        if (StringUtils.isBlank(dstr)) {
            return MIN_AMOUNT;
        }
        return new BigDecimal(dstr).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
