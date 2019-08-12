package com.cmpay.lemon.monitor.bo;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * @author: zhou_xiong
 */
public class TopStatBO {
    /**
     * 时间段
     */
    private String[] logPeriods;
    /**
     * 中台名称
     */
    private String centerName;
    /**
     * 交易码
     */
    private String txCode;
    /**
     * 交易量
     */
    private Long txCount;
    /**
     * 平均时长
     */
    private Long avgDuration;
    /**
     * 成功率
     */
    private BigDecimal sucRate;
    /**
     * 系统错误率
     */
    private BigDecimal sysErrRate;

    public TopStatBO() {
    }

    public TopStatBO(String[] logPeriods, String centerName) {
        this.logPeriods = logPeriods;
        this.centerName = centerName;
    }

    public String[] getLogPeriods() {
        return logPeriods;
    }

    public void setLogPeriods(String[] logPeriods) {
        this.logPeriods = logPeriods;
    }

    public String getCenterName() {
        return centerName;
    }

    public void setCenterName(String centerName) {
        this.centerName = centerName;
    }

    public String getTxCode() {
        return txCode;
    }

    public void setTxCode(String txCode) {
        this.txCode = txCode;
    }

    public Long getTxCount() {
        return txCount;
    }

    public void setTxCount(Long txCount) {
        this.txCount = txCount;
    }

    public Long getAvgDuration() {
        return avgDuration;
    }

    public void setAvgDuration(Long avgDuration) {
        this.avgDuration = avgDuration;
    }

    public BigDecimal getSucRate() {
        return sucRate;
    }

    public void setSucRate(BigDecimal sucRate) {
        this.sucRate = sucRate;
    }

    public BigDecimal getSysErrRate() {
        return sysErrRate;
    }

    public void setSysErrRate(BigDecimal sysErrRate) {
        this.sysErrRate = sysErrRate;
    }

    @Override
    public String toString() {
        return "TopStatBO{" +
                "logPeriods=" + Arrays.toString(logPeriods) +
                ", centerName='" + centerName + '\'' +
                ", txCode='" + txCode + '\'' +
                ", txCount='" + txCount + '\'' +
                ", avgDuration=" + avgDuration +
                ", sucRate=" + sucRate +
                ", sysErrRate=" + sysErrRate +
                '}';
    }
}
