package com.cmpay.lemon.monitor.bo;

import java.util.Arrays;

/**
 * @author zhou_xiong
 */
public class TxnPlteLogBO extends PageQueryBO {
    /**
     * 请求ID
     */
    private String logRequestId;

    private String[] logPeriods;

    public TxnPlteLogBO() {
    }

    public TxnPlteLogBO(String logRequestId) {
        this.logRequestId = logRequestId;
    }

    public TxnPlteLogBO(String[] logPeriods) {
        this.logPeriods = logPeriods;
    }

    public String getLogRequestId() {
        return logRequestId;
    }

    public void setLogRequestId(String logRequestId) {
        this.logRequestId = logRequestId;
    }

    public String[] getLogPeriods() {
        return logPeriods;
    }

    public void setLogPeriods(String[] logPeriods) {
        this.logPeriods = logPeriods;
    }

    @Override
    public String toString() {
        return "TxnPlteLogBO{" +
                "logRequestId='" + logRequestId + '\'' +
                ", logPeriods=" + Arrays.toString(logPeriods) +
                '}';
    }
}
