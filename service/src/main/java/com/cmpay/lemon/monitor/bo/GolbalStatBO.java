package com.cmpay.lemon.monitor.bo;

import java.util.Arrays;

/**
 * @author: zhou_xiong
 */
public class GolbalStatBO {
    private String[] logPeriods;

    public String[] getLogPeriods() {
        return logPeriods;
    }

    public GolbalStatBO(String[] logPeriods) {
        this.logPeriods = logPeriods;
    }

    public void setLogPeriods(String[] logPeriods) {
        this.logPeriods = logPeriods;
    }

    @Override
    public String toString() {
        return "GolbalStatBO{" +
                "logPeriods=" + Arrays.toString(logPeriods) +
                '}';
    }
}
