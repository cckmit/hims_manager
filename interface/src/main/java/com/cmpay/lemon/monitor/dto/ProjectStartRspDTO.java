/*
 * @ClassName LogType
 * @Description
 * @version 1.0
 * @Date 2019-01-16 16:31:48
 */
package com.cmpay.lemon.monitor.dto;


import com.cmpay.framework.data.response.GenericRspDTO;

/**
 * 项目启动dto
 */
public class ProjectStartRspDTO extends GenericRspDTO {
    private String reqInnerSeq;
    private String reqNo;
    private String reqNm;
    private String sendTo;
    private String copyTo;
    private String preCurPeriod;
    private String uploadPeriod;
    private DictionaryRspDTO dictionaryRspDTO;

    public DictionaryRspDTO getDictionaryRspDTO() {
        return dictionaryRspDTO;
    }

    public void setDictionaryRspDTO(DictionaryRspDTO dictionaryRspDTO) {
        this.dictionaryRspDTO = dictionaryRspDTO;
    }

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

    public String getPreCurPeriod() {
        return preCurPeriod;
    }

    public void setPreCurPeriod(String preCurPeriod) {
        this.preCurPeriod = preCurPeriod;
    }

    public String getUploadPeriod() {
        return uploadPeriod;
    }

    public void setUploadPeriod(String uploadPeriod) {
        this.uploadPeriod = uploadPeriod;
    }

    public ProjectStartRspDTO(String reqInnerSeq, String reqNo, String reqNm, String sendTo, String copyTo) {
        this.reqInnerSeq = reqInnerSeq;
        this.reqNo = reqNo;
        this.reqNm = reqNm;
        this.sendTo = sendTo;
        this.copyTo = copyTo;
    }

    public ProjectStartRspDTO(String reqInnerSeq, String reqNo, String reqNm, String sendTo, String copyTo, String preCurPeriod, String uploadPeriod) {
        this.reqInnerSeq = reqInnerSeq;
        this.reqNo = reqNo;
        this.reqNm = reqNm;
        this.sendTo = sendTo;
        this.copyTo = copyTo;
        this.preCurPeriod = preCurPeriod;
        this.uploadPeriod = uploadPeriod;
    }

    public ProjectStartRspDTO(String reqInnerSeq, String reqNo, String reqNm, String sendTo, String copyTo, String preCurPeriod, String uploadPeriod, DictionaryRspDTO dictionaryRspDTO) {
        this.reqInnerSeq = reqInnerSeq;
        this.reqNo = reqNo;
        this.reqNm = reqNm;
        this.sendTo = sendTo;
        this.copyTo = copyTo;
        this.preCurPeriod = preCurPeriod;
        this.uploadPeriod = uploadPeriod;
        this.dictionaryRspDTO = dictionaryRspDTO;
    }

    @Override
    public String toString() {
        return "ProjectStartRspDTO{" +
                "reqInnerSeq='" + reqInnerSeq + '\'' +
                ", reqNo='" + reqNo + '\'' +
                ", reqNm='" + reqNm + '\'' +
                ", sendTo='" + sendTo + '\'' +
                ", copyTo='" + copyTo + '\'' +
                ", preCurPeriod='" + preCurPeriod + '\'' +
                ", uploadPeriod='" + uploadPeriod + '\'' +
                ", dictionaryRspDTO=" + dictionaryRspDTO +
                '}';
    }

    public ProjectStartRspDTO() {
    }
}
