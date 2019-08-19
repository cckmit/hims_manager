package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.response.PageableRspDTO;

/**
 * Created on 2018/12/25
 *
 * @author zhou_xiong
 */
public class SysLogPageReqDTO extends PageableRspDTO {
    /**
     * 用户ID
     */
    private String userNo;
    /**
     * 用户操作
     */
    private String operation;
    /**
     * requestUri 请求URI
     */
    private String requestUri;
    /**
     * 开始时间.
     */
    private String startTime;
    /**
     * 结束时间.
     */
    private String endTime;

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "SysLogPageReqDTO{" +
                "userNo='" + userNo + '\'' +
                ", operation='" + operation + '\'' +
                ", requestUri='" + requestUri + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
