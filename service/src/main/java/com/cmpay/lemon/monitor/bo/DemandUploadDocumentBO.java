package com.cmpay.lemon.monitor.bo;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.sql.Date;

public class DemandUploadDocumentBO {
    @Excel(name = "需求编号")
    private String reqNo;
    @Excel(name = "需求名称")
    private String reqNm;
    @Excel(name = "项目启动日期")
    private String projectStartTm;
    @Excel(name = "需求文档上传日期")
    private String actPrdUploadTm;
    @Excel(name = "技术方案上传日期")
    private String actWorkloadUploadTm;
    @Excel(name = "sit测试报告上传时间")
    private String actSitUploadTm;
    @Excel(name = "测试案例上传时间")
    private String actTestCasesUploadTm;
    @Excel(name = "uat测试报告上传时间")
    private String actUatUploadTm;
    @Excel(name = "预投产验证报告上传时间")
    private String actPreUploadTm;
    @Excel(name = "投产验证报告上传时间")
    private String actProductionUploadTm;
    @Excel(name = "主导部门")
    private String devpLeadDept;
    @Excel(name = "产品经理")
    private String productMng;
    @Excel(name = "后台开发人员")
    private String devpEng;
    @Excel(name = "前端开发人员")
    private String frontEng;
    @Excel(name = "测试人员")
    private String testEng;
    @Excel(name = "需求当前阶段")
    private String preCurPeriod;

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

    public String getProjectStartTm() {
        return projectStartTm;
    }

    public void setProjectStartTm(String projectStartTm) {
        this.projectStartTm = projectStartTm;
    }

    public String getActPrdUploadTm() {
        return actPrdUploadTm;
    }

    public void setActPrdUploadTm(String actPrdUploadTm) {
        this.actPrdUploadTm = actPrdUploadTm;
    }

    public String getActWorkloadUploadTm() {
        return actWorkloadUploadTm;
    }

    public void setActWorkloadUploadTm(String actWorkloadUploadTm) {
        this.actWorkloadUploadTm = actWorkloadUploadTm;
    }

    public String getActSitUploadTm() {
        return actSitUploadTm;
    }

    public void setActSitUploadTm(String actSitUploadTm) {
        this.actSitUploadTm = actSitUploadTm;
    }

    public String getActTestCasesUploadTm() {
        return actTestCasesUploadTm;
    }

    public void setActTestCasesUploadTm(String actTestCasesUploadTm) {
        this.actTestCasesUploadTm = actTestCasesUploadTm;
    }

    public String getActUatUploadTm() {
        return actUatUploadTm;
    }

    public void setActUatUploadTm(String actUatUploadTm) {
        this.actUatUploadTm = actUatUploadTm;
    }

    public String getActPreUploadTm() {
        return actPreUploadTm;
    }

    public void setActPreUploadTm(String actPreUploadTm) {
        this.actPreUploadTm = actPreUploadTm;
    }

    public String getActProductionUploadTm() {
        return actProductionUploadTm;
    }

    public void setActProductionUploadTm(String actProductionUploadTm) {
        this.actProductionUploadTm = actProductionUploadTm;
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

    public String getDevpEng() {
        return devpEng;
    }

    public void setDevpEng(String devpEng) {
        this.devpEng = devpEng;
    }

    public String getFrontEng() {
        return frontEng;
    }

    public void setFrontEng(String frontEng) {
        this.frontEng = frontEng;
    }

    public String getTestEng() {
        return testEng;
    }

    public void setTestEng(String testEng) {
        this.testEng = testEng;
    }

    public String getPreCurPeriod() {
        return preCurPeriod;
    }

    public void setPreCurPeriod(String preCurPeriod) {
        this.preCurPeriod = preCurPeriod;
    }

    @Override
    public String toString() {
        return "DemandUploadDocumentBO{" +
                "reqNo='" + reqNo + '\'' +
                ", reqNm='" + reqNm + '\'' +
                ", projectStartTm='" + projectStartTm + '\'' +
                ", actPrdUploadTm='" + actPrdUploadTm + '\'' +
                ", actWorkloadUploadTm='" + actWorkloadUploadTm + '\'' +
                ", actSitUploadTm='" + actSitUploadTm + '\'' +
                ", actTestCasesUploadTm='" + actTestCasesUploadTm + '\'' +
                ", actUatUploadTm='" + actUatUploadTm + '\'' +
                ", actPreUploadTm='" + actPreUploadTm + '\'' +
                ", actProductionUploadTm='" + actProductionUploadTm + '\'' +
                ", devpLeadDept='" + devpLeadDept + '\'' +
                ", productMng='" + productMng + '\'' +
                ", devpEng='" + devpEng + '\'' +
                ", frontEng='" + frontEng + '\'' +
                ", testEng='" + testEng + '\'' +
                ", preCurPeriod='" + preCurPeriod + '\'' +
                '}';
    }
}
