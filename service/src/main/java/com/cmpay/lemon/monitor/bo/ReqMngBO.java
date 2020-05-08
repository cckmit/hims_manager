package com.cmpay.lemon.monitor.bo;



public class ReqMngBO extends BaseConditionBO{

    //需求名称、需求编号、开发主导部门、开发配合部门、需求阶段，需求类型、需求启动月份、需求实施月份、产品负责人、项目经理、产品经理、是否建立svn目录、需求异常状态、月底备注、需求状态
    private String reqNm;
    private String reqNo;
    private String devpLeadDept;
    private String devpCoorDept;
    private String preCurPeriod;
    private String reqType;
    private String reqStartMon;
    private String reqImplMon;
    private String reqPrdLine;
    private String reqMnger;
    private String projectMng;
    private String productMng;
    private String isSvnBuild;
    private String reqAbnorType;
    private String endMonRemark;
    private String reqSts;
    private String testEng;
    private String devpEng;

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

    public String getDevpLeadDept() {
        return devpLeadDept;
    }

    public void setDevpLeadDept(String devpLeadDept) {
        this.devpLeadDept = devpLeadDept;
    }

    public String getDevpCoorDept() {
        return devpCoorDept;
    }

    public void setDevpCoorDept(String devpCoorDept) {
        this.devpCoorDept = devpCoorDept;
    }

    public String getPreCurPeriod() {
        return preCurPeriod;
    }

    public void setPreCurPeriod(String preCurPeriod) {
        this.preCurPeriod = preCurPeriod;
    }

    public String getReqType() {
        return reqType;
    }

    public void setReqType(String reqType) {
        this.reqType = reqType;
    }

    public String getReqStartMon() {
        return reqStartMon;
    }

    public void setReqStartMon(String reqStartMon) {
        this.reqStartMon = reqStartMon;
    }

    public String getReqImplMon() {
        return reqImplMon;
    }

    public void setReqImplMon(String reqImplMon) {
        this.reqImplMon = reqImplMon;
    }

    public String getReqPrdLine() {
        return reqPrdLine;
    }

    public void setReqPrdLine(String reqPrdLine) {
        this.reqPrdLine = reqPrdLine;
    }

    public String getReqMnger() {
        return reqMnger;
    }

    public void setReqMnger(String reqMnger) {
        this.reqMnger = reqMnger;
    }

    public String getProjectMng() {
        return projectMng;
    }

    public void setProjectMng(String projectMng) {
        this.projectMng = projectMng;
    }

    public String getProductMng() {
        return productMng;
    }

    public void setProductMng(String productMng) {
        this.productMng = productMng;
    }

    public String getIsSvnBuild() {
        return isSvnBuild;
    }

    public void setIsSvnBuild(String isSvnBuild) {
        this.isSvnBuild = isSvnBuild;
    }

    public String getReqAbnorType() {
        return reqAbnorType;
    }

    public void setReqAbnorType(String reqAbnorType) {
        this.reqAbnorType = reqAbnorType;
    }

    public String getEndMonRemark() {
        return endMonRemark;
    }

    public void setEndMonRemark(String endMonRemark) {
        this.endMonRemark = endMonRemark;
    }

    public String getReqSts() {
        return reqSts;
    }

    public void setReqSts(String reqSts) {
        this.reqSts = reqSts;
    }

    public String getTestEng() {
        return testEng;
    }

    public void setTestEng(String testEng) {
        this.testEng = testEng;
    }

    public String getDevpEng() {
        return devpEng;
    }

    public void setDevpEng(String devpEng) {
        this.devpEng = devpEng;
    }
}
