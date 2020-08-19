package com.cmpay.lemon.monitor.dto;

/**
 * @author  ty
 */
public class TestProgressDetailDTO {

    /**
     *  reqPrdLine 产品线
     */
    private String reqPrdLine;
    /**
     *   完成需求个数
     */
    private int completionNumber;
    /**
     *   进行中个数
     */
    private int underwayNumber;
    /**
     *   测试执行数
     */
    private int caseExecutionNumber;
    /**
     *   plog
     */
    private int plogNumber;

    public String getReqPrdLine() {
        return reqPrdLine;
    }

    public void setReqPrdLine(String reqPrdLine) {
        this.reqPrdLine = reqPrdLine;
    }

    public int getCompletionNumber() {
        return completionNumber;
    }

    public void setCompletionNumber(int completionNumber) {
        this.completionNumber = completionNumber;
    }

    public int getUnderwayNumber() {
        return underwayNumber;
    }

    public void setUnderwayNumber(int underwayNumber) {
        this.underwayNumber = underwayNumber;
    }

    public int getCaseExecutionNumber() {
        return caseExecutionNumber;
    }

    public void setCaseExecutionNumber(int caseExecutionNumber) {
        this.caseExecutionNumber = caseExecutionNumber;
    }

    public int getPlogNumber() {
        return plogNumber;
    }

    public void setPlogNumber(int plogNumber) {
        this.plogNumber = plogNumber;
    }

    @Override
    public String toString() {
        return "TestProgressDetailDTO{" +
                "reqPrdLine='" + reqPrdLine + '\'' +
                ", completionNumber='" + completionNumber + '\'' +
                ", underwayNumber='" + underwayNumber + '\'' +
                ", caseExecutionNumber='" + caseExecutionNumber + '\'' +
                ", plogNumber='" + plogNumber + '\'' +
                '}';
    }
}
