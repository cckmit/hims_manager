package com.cmpay.lemon.monitor.dto;

public class AutomatedProductionCallbackReqDTO {
    String proNumber;
    String status;
    String remark;
    String env;
    public String getProNumber() {
        return proNumber;
    }

    public void setProNumber(String proNumber) {
        this.proNumber = proNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getTest(){
        return "{\n" +
                "\t\"proNumber\":\"autotouchantest_yutouchan\",\n" +
                "\t\"status\":\"1\",\n" +
                "\t\"env\":\"1\",\n" +
                "\t\"remark\":\"其他\"\n" +
                "}";
    }
}
