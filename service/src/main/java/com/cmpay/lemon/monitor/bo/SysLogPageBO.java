package com.cmpay.lemon.monitor.bo;

/**
 * Created on 2018/12/25
 *
 * @author zhou_xiong
 */
public class SysLogPageBO {
    /**
     * userNo 用户ID
     */
    private String userNo;
    /**
     * operation 用户操作
     */
    private String operation;
    /**
     * requestUri 请求URI
     */
    private String requestUri;
    /**
     * 开始时间.
     */
    private Long startTime;
    /**
     * 结束时间.
     */
    private Long endTime;
    /**
     * 页码
     */
    private Integer pageNum;
    /**
     * 每页记录数
     */
    private Integer pageSize;

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

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "SysLogPageBO{" +
                "userNo='" + userNo + '\'' +
                ", operation='" + operation + '\'' +
                ", requestUri='" + requestUri + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }
}
