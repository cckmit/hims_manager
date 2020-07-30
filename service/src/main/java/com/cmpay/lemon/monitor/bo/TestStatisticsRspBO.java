package com.cmpay.lemon.monitor.bo;

public class TestStatisticsRspBO {
    String period;
    String demandName;
    int caseWritingNumber;
    int caseExecutionNumber;
    int caseCompletedNumber;

    int defectsNumber;

    String workingHours;

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getDemandName() {
        return demandName;
    }

    public void setDemandName(String demandName) {
        this.demandName = demandName;
    }

    public int getCaseWritingNumber() {
        return caseWritingNumber;
    }

    public void setCaseWritingNumber(int caseWritingNumber) {
        this.caseWritingNumber = caseWritingNumber;
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

    public String getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }
}
