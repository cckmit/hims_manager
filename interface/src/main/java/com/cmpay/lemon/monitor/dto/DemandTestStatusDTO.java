package com.cmpay.lemon.monitor.dto;


public class DemandTestStatusDTO {
    //需求编号
    private String reqNo;
    // 需求名称
    private String reqNm;
    // 测试案例总数
    String testCaseNumber;
    // 测试案例执行数
    private int caseExecutionNumber;
    // 测试案例完成数
    private int caseCompletedNumber;
    // 缺陷数
    private int defectsNumber;
    // 缺陷发现率
    private String defectDiscoveryRate;
    //缺陷率
    private String defectRate;
    // 测试进度
    private String testProgress;

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

    public String getTestCaseNumber() {
        return testCaseNumber;
    }

    public void setTestCaseNumber(String testCaseNumber) {
        this.testCaseNumber = testCaseNumber;
    }

    public int getCaseExecutionNumber() {
        return caseExecutionNumber;
    }

    public void setCaseExecutionNumber(int caseExecutionNumber) {
        this.caseExecutionNumber = caseExecutionNumber;
    }

    public int getCaseCompletedNumber() {
        return caseCompletedNumber;
    }

    public void setCaseCompletedNumber(int caseCompletedNumber) {
        this.caseCompletedNumber = caseCompletedNumber;
    }

    public int getDefectsNumber() {
        return defectsNumber;
    }

    public void setDefectsNumber(int defectsNumber) {
        this.defectsNumber = defectsNumber;
    }

    public String getDefectDiscoveryRate() {
        return defectDiscoveryRate;
    }

    public void setDefectDiscoveryRate(String defectDiscoveryRate) {
        this.defectDiscoveryRate = defectDiscoveryRate;
    }

    public String getDefectRate() {
        return defectRate;
    }

    public void setDefectRate(String defectRate) {
        this.defectRate = defectRate;
    }

    public String getTestProgress() {
        return testProgress;
    }

    public void setTestProgress(String testProgress) {
        this.testProgress = testProgress;
    }
}
