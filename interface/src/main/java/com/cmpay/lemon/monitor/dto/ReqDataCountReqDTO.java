package com.cmpay.lemon.monitor.dto;

public class ReqDataCountReqDTO {

    String reqImplMon;
    // 部门
    String devpLeadDept;
    //产品经理
    String productMng;

    String dayNumber;

    private String firstLevelOrganization;

    public String getReqImplMon() {
        return reqImplMon;
    }

    public void setReqImplMon(String reqImplMon) {
        this.reqImplMon = reqImplMon;
    }

    public String getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(String dayNumber) {
        this.dayNumber = dayNumber;
    }

    public String getDevpLeadDept() {
        return devpLeadDept;
    }

    public void setDevpLeadDept(String devpLeadDept) {
        this.devpLeadDept = devpLeadDept;
    }

    public String getProductMng() {
        return productMng;
    }

    public void setProductMng(String productMng) {
        this.productMng = productMng;
    }

    public String getFirstLevelOrganization() {
        return firstLevelOrganization;
    }

    public void setFirstLevelOrganization(String firstLevelOrganization) {
        this.firstLevelOrganization = firstLevelOrganization;
    }

    @Override
    public String toString() {
        return "ReqDataCountReqDTO{" +
                "reqImplMon='" + reqImplMon + '\'' +
                ", devpLeadDept='" + devpLeadDept + '\'' +
                ", productMng='" + productMng + '\'' +
                ", dayNumber='" + dayNumber + '\'' +
                ", firstLevelOrganization='" + firstLevelOrganization + '\'' +
                '}';
    }
}
