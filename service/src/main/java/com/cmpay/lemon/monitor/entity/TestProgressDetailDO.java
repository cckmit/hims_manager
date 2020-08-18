/*
 * @ClassName TestProgressDetailDO
 * @Description 
 * @version 1.0
 * @Date 2020-08-18 10:50:32
 */
package com.cmpay.lemon.monitor.entity;

import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;

@DataObject
public class TestProgressDetailDO extends BaseDO {
    /**
     * @Fields id 主键
     */
    private Long id;
    /**
     * @Fields epickey epic编号
     */
    private String epickey;
    /**
     * @Fields testCaseNumber 测试案例总数
     */
    private String testCaseNumber;
    /**
     * @Fields caseExecutionNumber 测试案例执行数
     */
    private String caseExecutionNumber;
    /**
     * @Fields caseCompletedNumber 测试案例完成数
     */
    private String caseCompletedNumber;
    /**
     * @Fields defectsNumber 缺陷数
     */
    private String defectsNumber;
    /**
     * @Fields testProgress 测试进度
     */
    private String testProgress;
    /**
     * @Fields testDate 登记日期
     */
    private String testDate;
    /**
     * @Fields reqPrdLine 产品线
     */
    private String reqPrdLine;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEpickey() {
        return epickey;
    }

    public void setEpickey(String epickey) {
        this.epickey = epickey;
    }

    public String getTestCaseNumber() {
        return testCaseNumber;
    }

    public void setTestCaseNumber(String testCaseNumber) {
        this.testCaseNumber = testCaseNumber;
    }

    public String getCaseExecutionNumber() {
        return caseExecutionNumber;
    }

    public void setCaseExecutionNumber(String caseExecutionNumber) {
        this.caseExecutionNumber = caseExecutionNumber;
    }

    public String getCaseCompletedNumber() {
        return caseCompletedNumber;
    }

    public void setCaseCompletedNumber(String caseCompletedNumber) {
        this.caseCompletedNumber = caseCompletedNumber;
    }

    public String getDefectsNumber() {
        return defectsNumber;
    }

    public void setDefectsNumber(String defectsNumber) {
        this.defectsNumber = defectsNumber;
    }

    public String getTestProgress() {
        return testProgress;
    }

    public void setTestProgress(String testProgress) {
        this.testProgress = testProgress;
    }

    public String getTestDate() {
        return testDate;
    }

    public void setTestDate(String testDate) {
        this.testDate = testDate;
    }

    public String getReqPrdLine() {
        return reqPrdLine;
    }

    public void setReqPrdLine(String reqPrdLine) {
        this.reqPrdLine = reqPrdLine;
    }
}