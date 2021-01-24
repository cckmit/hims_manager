package com.cmpay.lemon.monitor.bo;

import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * @author wlr
 */
public class DefectMonthlyBO {

    @Excel(name = "需求编号")
    private String reqNo;
    @Excel(name = "需求名称")
    private String reqNm;
    @Excel(name = "产品线")
    private String reqPrdLine;
    @Excel(name = "研发代码缺陷密度")
    private String defectRate;
    @Excel(name = "需求工作量")
    private double totalWorkload;
    @Excel(name = "问题总数")
    private int defectsNumber;
    @Excel(name = "致命问题数")
    private int fatalDefectsNumber;
    @Excel(name = "严重问题数")
    private int seriousDefectsNumber;
    @Excel(name = "一般问题数")
    private int ordinaryDefectsNumber;
    @Excel(name = "轻微问题数")
    private int minorDefectsNumber;
    @Excel(name = "执行案例数")
    private int executionCaseNumber;
    @Excel(name = "测试成功案例数")
    private int successCaseNumber;
    @Excel(name = "测试通过率")
    private String testPassRate;
    @Excel(name = "产品负责人")
    private String reqMnger;
    @Excel(name = "产品负责部门")
    private String reqMngerDept;
    @Excel(name = "开发负责人")
    private String devpResMng;
    @Excel(name = "开发部门")
    private String devpLeadDept;

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

    public double getTotalWorkload() {
        return totalWorkload;
    }

    public void setTotalWorkload(double totalWorkload) {
        this.totalWorkload = totalWorkload;
    }

    public int getDefectsNumber() {
        return defectsNumber;
    }

    public void setDefectsNumber(int defectsNumber) {
        this.defectsNumber = defectsNumber;
    }

    public int getFatalDefectsNumber() {
        return fatalDefectsNumber;
    }

    public void setFatalDefectsNumber(int fatalDefectsNumber) {
        this.fatalDefectsNumber = fatalDefectsNumber;
    }

    public int getSeriousDefectsNumber() {
        return seriousDefectsNumber;
    }

    public void setSeriousDefectsNumber(int seriousDefectsNumber) {
        this.seriousDefectsNumber = seriousDefectsNumber;
    }

    public int getOrdinaryDefectsNumber() {
        return ordinaryDefectsNumber;
    }

    public void setOrdinaryDefectsNumber(int ordinaryDefectsNumber) {
        this.ordinaryDefectsNumber = ordinaryDefectsNumber;
    }

    public int getMinorDefectsNumber() {
        return minorDefectsNumber;
    }

    public void setMinorDefectsNumber(int minorDefectsNumber) {
        this.minorDefectsNumber = minorDefectsNumber;
    }

    public int getExecutionCaseNumber() {
        return executionCaseNumber;
    }

    public void setExecutionCaseNumber(int executionCaseNumber) {
        this.executionCaseNumber = executionCaseNumber;
    }

    public int getSuccessCaseNumber() {
        return successCaseNumber;
    }

    public void setSuccessCaseNumber(int successCaseNumber) {
        this.successCaseNumber = successCaseNumber;
    }


    public String getDefectRate() {
        return defectRate;
    }

    public void setDefectRate(String defectRate) {
        this.defectRate = defectRate;
    }

    public String getTestPassRate() {
        return testPassRate;
    }

    public void setTestPassRate(String testPassRate) {
        this.testPassRate = testPassRate;
    }

    public String getReqMnger() {
        return reqMnger;
    }

    public void setReqMnger(String reqMnger) {
        this.reqMnger = reqMnger;
    }

    public String getReqMngerDept() {
        return reqMngerDept;
    }

    public void setReqMngerDept(String reqMngerDept) {
        this.reqMngerDept = reqMngerDept;
    }

    public String getDevpResMng() {
        return devpResMng;
    }

    public void setDevpResMng(String devpResMng) {
        this.devpResMng = devpResMng;
    }

    public String getDevpLeadDept() {
        return devpLeadDept;
    }

    public void setDevpLeadDept(String devpLeadDept) {
        this.devpLeadDept = devpLeadDept;
    }
}
