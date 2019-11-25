package com.cmpay.lemon.monitor.entity;

import java.io.Serializable;

/**
 * 错误码使用环境
 *
 * @author: **
 */
public class ErrorRecordBeanDO extends AbstractDO {

    private static final long serialVersionUID = 1L;

    public Serializable getId(){
        return serialVersionUID;
    }

    /**
     * 错误码编号
     */
    private String errorCode;

    /**
     * 错误码使用环境
     */
    private String envirCode;

    /**
     * 时间戳
     */
    private String timeStmp;


    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getEnvirCode() {
        return envirCode;
    }

    public void setEnvirCode(String envirCode) {
        this.envirCode = envirCode;
    }

    public String getTimeStmp() {
        return timeStmp;
    }

    public void setTimeStmp(String timeStmp) {
        this.timeStmp = timeStmp;
    }
}
