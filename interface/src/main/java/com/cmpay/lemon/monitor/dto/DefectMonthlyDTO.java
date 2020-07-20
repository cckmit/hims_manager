package com.cmpay.lemon.monitor.dto;


/**
 * @author wlr
 */
public class DefectMonthlyDTO {


    private String reqNo;

    private String reqNm;

    private String reqPrdLine;

    private String defectRate;

    private int totalWorkload;

    private int defectsNumber;

    private int fatalDefectsNumber;

    private int seriousDefectsNumber;

    private int ordinaryDefectsNumber;

    private int minorDefectsNumber;

    private int executionCaseNumber;

    private int successCaseNumber;

    private String testPassRate;

    private String reqMnger;

    private String reqMngerDept;

    private String devpResMng;

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



    public int getTotalWorkload() {
        return totalWorkload;
    }

    public void setTotalWorkload(int totalWorkload) {
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
