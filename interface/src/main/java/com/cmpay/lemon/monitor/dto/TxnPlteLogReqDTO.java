package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.request.GenericDTO;

import javax.validation.constraints.NotNull;

/**
 * @author zhou_xiong
 */
public class TxnPlteLogReqDTO extends GenericDTO {
    /**
     * 请求ID
     */
    @NotNull(message = "请求ID不能为空")
    private String logRequestId;

    public String getLogRequestId() {
        return logRequestId;
    }

    public void setLogRequestId(String logRequestId) {
        this.logRequestId = logRequestId;
    }

    @Override
    public String toString() {
        return "TxnPlteLogReqDTO{" +
                ", logRequestId='" + logRequestId + '\'' +
                '}';
    }
}
