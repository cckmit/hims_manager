package com.cmpay.lemon.monitor.bo;
/**
 * @author ty
 */
public class AutomatedProductionCallbackReqBO {
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

    @Override
    public String toString() {
        return "AutomatedProductionCallbackReqBO{" +
                "proNumber='" + proNumber + '\'' +
                ", status='" + status + '\'' +
                ", remark='" + remark + '\'' +
                ", env='" + env + '\'' +
                '}';
    }
}
