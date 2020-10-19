package com.cmpay.lemon.monitor.dto;

public class UpdateReqStsDTO {

    String reqStsRemarks;
    String reqInnerSeq;
    String reqSts;
    String reqNm;
    String reqNo;
    String stateCauseClassification;

    public String getStateCauseClassification() {
        return stateCauseClassification;
    }

    public void setStateCauseClassification(String stateCauseClassification) {
        this.stateCauseClassification = stateCauseClassification;
    }

    public String getReqStsRemarks() {
        return reqStsRemarks;
    }

    public void setReqStsRemarks(String reqStsRemarks) {
        this.reqStsRemarks = reqStsRemarks;
    }

    public String getReqInnerSeq() {
        return reqInnerSeq;
    }

    public void setReqInnerSeq(String reqInnerSeq) {
        this.reqInnerSeq = reqInnerSeq;
    }

    public String getReqSts() {
        return reqSts;
    }

    public void setReqSts(String reqSts) {
        this.reqSts = reqSts;
    }

    public String getReqNm() {
        return reqNm;
    }

    public void setReqNm(String reqNm) {
        this.reqNm = reqNm;
    }

    public String getReqNo() {
        return reqNo;
    }

    public void setReqNo(String reqNo) {
        this.reqNo = reqNo;
    }

    @Override
    public String toString() {
        return "UpdateReqStsDTO{" +
                "reqStsRemarks='" + reqStsRemarks + '\'' +
                ", reqInnerSeq='" + reqInnerSeq + '\'' +
                ", reqSts='" + reqSts + '\'' +
                ", reqNm='" + reqNm + '\'' +
                ", reqNo='" + reqNo + '\'' +
                ", stateCauseClassification='" + stateCauseClassification + '\'' +
                '}';
    }
}
