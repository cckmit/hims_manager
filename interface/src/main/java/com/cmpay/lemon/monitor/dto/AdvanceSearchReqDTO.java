package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.request.GenericDTO;

import javax.validation.constraints.NotNull;
import java.util.Arrays;

/**
 * @author zhou_xiong
 */
public class AdvanceSearchReqDTO extends GenericDTO {
    /**
     * 时间段
     */
    @NotNull(message = "时间段不能为空")
    private String[] logPeriods;
    /**
     * 日志类型
     */
    private String logType;
    /**
     * 日志应用
     */
    private String logApp;
    /**
     * 页码
     */
    private Integer pageNum;
    /**
     * 每页记录数
     */
    private Integer pageSize;
    /**
     * 日志来源
     */
    private String source;
    /**
     * 其他关键字
     */
    private String keywords;
    /**
     * 请求ID
     */
    private String logRequestId;
    /**
     * 交易码
     */
    private String logTxCode;
    /**
     * 返回码
     */
    private String logTxReturn;
    /**
     * 执行时长 从
     */
    private String from;
    /**
     * 执行时长 到
     */
    private String to;

    public String[] getLogPeriods() {
        return logPeriods;
    }

    public void setLogPeriods(String[] logPeriods) {
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
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

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "AdvanceSearchReqDTO{" +
                "logPeriods=" + Arrays.toString(logPeriods) +
                ", logType='" + logType + '\'' +
                ", logApp='" + logApp + '\'' +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", source='" + source + '\'' +
                ", keywords='" + keywords + '\'' +
                ", logRequestId='" + logRequestId + '\'' +
                ", logTxCode='" + logTxCode + '\'' +
                ", logTxReturn='" + logTxReturn + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                '}';
    }
}
