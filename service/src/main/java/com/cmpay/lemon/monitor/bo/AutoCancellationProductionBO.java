package com.cmpay.lemon.monitor.bo;

/**
 * 取消自动化投产包
 */
public class AutoCancellationProductionBO {
    //取消原因
    private String reason;
    //投产编号
    private String proNumber;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getProNumber() {
        return proNumber;
    }

    public void setProNumber(String proNumber) {
        this.proNumber = proNumber;
    }

    public String getJson(){
        return "{\n" +
                "\"proNumber\":"+"\""+this.getProNumber()+"\""+",\n" +
                "\"reason\": "+"\""+this.getReason()+"\""+"\n" +
                "}\n";
    }

    @Override
    public String toString() {
        return "AutoCancellationProductionBO{" +
                "reason='" + reason + '\'' +
                ", proNumber='" + proNumber + '\'' +
                '}';
    }
}
