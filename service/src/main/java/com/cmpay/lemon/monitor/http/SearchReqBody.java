package com.cmpay.lemon.monitor.http;

import java.util.Map;

/**
 * @author: zhou_xiong
 */
public class SearchReqBody {
    private String logPeriods;
    private String logType;
    private String logApp;
    private Integer durationFrom;
    private Integer durationTo;
    private String source;
    private String hostName;
    private String logRequestId;
    private String logTxCode;
    private String logTxReturn;
    private String logTxIpAddr;
    private Map<String, Object> keywords;
    private Integer pageNum;
    private Integer pageSize;

    public String getLogPeriods() {
        return logPeriods;
    }

    public void setLogPeriods(String logPeriods) {
        this.logPeriods = logPeriods;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getLogApp() {
        return logApp;
    }

    public void setLogApp(String logApp) {
        this.logApp = logApp;
    }

    public Integer getDurationFrom() {
        return durationFrom;
    }

    public void setDurationFrom(Integer durationFrom) {
        this.durationFrom = durationFrom;
    }

    public Integer getDurationTo() {
        return durationTo;
    }

    public void setDurationTo(Integer durationTo) {
        this.durationTo = durationTo;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getLogRequestId() {
        return logRequestId;
    }

    public void setLogRequestId(String logRequestId) {
        this.logRequestId = logRequestId;
    }

    public String getLogTxCode() {
        return logTxCode;
    }

    public void setLogTxCode(String logTxCode) {
        this.logTxCode = logTxCode;
    }

    public String getLogTxReturn() {
        return logTxReturn;
    }

    public void setLogTxReturn(String logTxReturn) {
        this.logTxReturn = logTxReturn;
    }

    public String getLogTxIpAddr() {
        return logTxIpAddr;
    }

    public void setLogTxIpAddr(String logTxIpAddr) {
        this.logTxIpAddr = logTxIpAddr;
    }

    public Map<String, Object> getKeywords() {
        return keywords;
    }

    public void setKeywords(Map<String, Object> keywords) {
        this.keywords = keywords;
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
        return "SearchReqBody{" +
                "logPeriods='" + logPeriods + '\'' +
                ", logType='" + logType + '\'' +
                ", logApp='" + logApp + '\'' +
                ", durationFrom=" + durationFrom +
                ", durationTo=" + durationTo +
                ", source='" + source + '\'' +
                ", hostName='" + hostName + '\'' +
                ", logRequestId='" + logRequestId + '\'' +
                ", logTxCode='" + logTxCode + '\'' +
                ", logTxReturn='" + logTxReturn + '\'' +
                ", logTxIpAddr='" + logTxIpAddr + '\'' +
                ", keywords=" + keywords +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }
}
