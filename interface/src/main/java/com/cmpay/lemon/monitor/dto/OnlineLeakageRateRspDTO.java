package com.cmpay.lemon.monitor.dto;

public class OnlineLeakageRateRspDTO {

    String[] months;
    String[] leakageRate;

    public String[] getMonths() {
        return months;
    }

    public void setMonths(String[] months) {
        this.months = months;
    }

    public String[] getLeakageRate() {
        return leakageRate;
    }

    public void setLeakageRate(String[] leakageRate) {
        this.leakageRate = leakageRate;
    }
}
