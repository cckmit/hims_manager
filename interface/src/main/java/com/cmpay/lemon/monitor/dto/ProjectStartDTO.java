/*
 * @ClassName LogType
 * @Description
 * @version 1.0
 * @Date 2019-01-16 16:31:48
 */
package com.cmpay.lemon.monitor.dto;


import com.cmpay.framework.data.request.GenericDTO;

/**
 * 项目启动dto
 */
public class ProjectStartDTO extends GenericDTO {
    private String reqInnerSeq;
    private String reqNo;
    private String reqNm;
    private String sendTo;
    private String copyTo;

    public String getReqInnerSeq() {
        return reqInnerSeq;
    }

    public void setReqInnerSeq(String reqInnerSeq) {
        this.reqInnerSeq = reqInnerSeq;
    }

    public String getReqNo() {
        return reqNo;
    }

    public void setReqNo(String reqNo) {
        this.reqNo = reqNo;
    }

    public String getReqNm() {
        return reqNm;
    }

    public void setReqNm(String reqNm) {
        this.reqNm = reqNm;
    }

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }

    public String getCopyTo() {
        return copyTo;
    }

    public void setCopyTo(String copyTo) {
        this.copyTo = copyTo;
    }

    public ProjectStartDTO(String reqInnerSeq, String reqNo, String reqNm, String sendTo, String copyTo) {
        this.reqInnerSeq = reqInnerSeq;
        this.reqNo = reqNo;
        this.reqNm = reqNm;
        this.sendTo = sendTo;
        this.copyTo = copyTo;
    }

    public ProjectStartDTO() {
    }

    @Override
    public String toString() {
        return "ProjectStartDTO{" +
                "reqInnerSeq='" + reqInnerSeq + '\'' +
                ", reqNo='" + reqNo + '\'' +
                ", reqNm='" + reqNm + '\'' +
                ", sendTo='" + sendTo + '\'' +
                ", copyTo='" + copyTo + '\'' +
                '}';
    }
}
