package com.cmpay.lemon.monitor.dto;

public class UpdateReqStsDTO {

    String reqStsRemarks;
    String reqInnerSeq;
    String reqSts;

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
}
