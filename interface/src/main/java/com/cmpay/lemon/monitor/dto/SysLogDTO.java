
package com.cmpay.lemon.monitor.dto;

import java.util.Date;

/**
 * Created on 2018/12/25
 *
 * @author zhou_xiong
 */
public class SysLogDTO {
    /**
     * @Fields id 
     */
    private String id;
    /**
     * @Fields userNo 用户ID
     */
    private String userNo;
    /**
     * @Fields operation 用户操作
     */
    private String operation;
    /**
     * @Fields requestUri 请求URI
     */
    private String requestUri;
    /**
     * @Fields time 执行时长(毫秒)
     */
    private Long time;
    /**
     * @Fields ip 操作者IP
     */
    private String ip;
    /**
     * @Fields createDate 操作时间
     */
    private Date createDate;
    /**
     * @Fields userAgent 浏览器信息
     */
    private String userAgent;
    /**
     * @Fields method 提交方式
     */
    private String method;
    /**
     * @Fields params 请求参数
     */
    private String params;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "SysLogDTO{" +
                "id='" + id + '\'' +
                ", userNo='" + userNo + '\'' +
                ", operation='" + operation + '\'' +
                ", requestUri='" + requestUri + '\'' +
                ", time=" + time +
                ", ip='" + ip + '\'' +
                ", createDate=" + createDate +
                ", userAgent='" + userAgent + '\'' +
                ", method='" + method + '\'' +
                ", params='" + params + '\'' +
                '}';
    }
}