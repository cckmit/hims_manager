package com.cmpay.lemon.monitor.bo;

import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * 基地工作量BO
 * @author ty
 */
public class BaseWorkloadBO {

    @Excel(name = "需求内部编号")
    private String reqInnerSeq;
    @Excel(name = "需求编号")
    private String reqNo;
    @Excel(name = "需求名称")
    private String reqNm;
    @Excel(name = "产品名称")
    private String reqPrdLine;
    @Excel(name = "二级主导团队")
    private String devpLeadDept;
    @Excel(name = "开发负责人")
    private String devpResMng;
    @Excel(name = "总工作量")
    private int totalWorkload;
    @Excel(name = "已录入工作量(不包含本月)")
    private int inputWorkload;
    @Excel(name = "本月录入工作量")
    private int monInputWorkload;
    @Excel(name = "剩余工作量")
    private int remainWorkload;
    @Excel(name = "需求当前阶段")
    private String preCurPeriod;
    @Excel(name = "一级主导团队")
    private String firstLevelOrganization;

    public String getFirstLevelOrganization() {
        return firstLevelOrganization;
    }

    public void setFirstLevelOrganization(String firstLevelOrganization) {
        this.firstLevelOrganization = firstLevelOrganization;
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

    public String getReqPrdLine() {
        return reqPrdLine;
    }

    public void setReqPrdLine(String reqPrdLine) {
        this.reqPrdLine = reqPrdLine;
    }

    public String getDevpLeadDept() {
        return devpLeadDept;
    }

    public void setDevpLeadDept(String devpLeadDept) {
        this.devpLeadDept = devpLeadDept;
    }

    public String getDevpResMng() {
        return devpResMng;
    }

    public void setDevpResMng(String devpResMng) {
        this.devpResMng = devpResMng;
    }

    public int getTotalWorkload() {
        return totalWorkload;
    }

    public void setTotalWorkload(int totalWorkload) {
        this.totalWorkload = totalWorkload;
    }

    public int getInputWorkload() {
        return inputWorkload;
    }

    public void setInputWorkload(int inputWorkload) {
        this.inputWorkload = inputWorkload;
    }

    public int getMonInputWorkload() {
        return monInputWorkload;
    }

    public void setMonInputWorkload(int monInputWorkload) {
        this.monInputWorkload = monInputWorkload;
    }

    public int getRemainWorkload() {
        return remainWorkload;
    }

    public void setRemainWorkload(int remainWorkload) {
        this.remainWorkload = remainWorkload;
    }

    public String getPreCurPeriod() {
        return preCurPeriod;
    }

    public void setPreCurPeriod(String preCurPeriod) {
        this.preCurPeriod = preCurPeriod;
    }

    @Override
    public String toString() {
        return "BaseWorkloadBO{" +
                "reqInnerSeq='" + reqInnerSeq + '\'' +
                ", reqNo='" + reqNo + '\'' +
                ", reqNm='" + reqNm + '\'' +
                ", reqPrdLine='" + reqPrdLine + '\'' +
                ", devpLeadDept='" + devpLeadDept + '\'' +
                ", devpResMng='" + devpResMng + '\'' +
                ", totalWorkload=" + totalWorkload +
                ", inputWorkload=" + inputWorkload +
                ", monInputWorkload=" + monInputWorkload +
                ", remainWorkload=" + remainWorkload +
                ", preCurPeriod='" + preCurPeriod + '\'' +
                ", firstLevelOrganization='" + firstLevelOrganization + '\'' +
                '}';
    }
}
