/*
 * @ClassName ProUnhandledIssuesDO
 * @Description 
 * @version 1.0
 * @Date 2020-09-14 15:49:03
 */
package com.cmpay.lemon.monitor.entity;

import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;

@DataObject
public class ProUnhandledIssuesDO extends BaseDO {
    /**
     * @Fields reqNo 需求编号
     */
    private String reqNo;
    /**
     * @Fields jirakey epic任务jira编号
     */
    private String jirakey;
    /**
     * @Fields department 主导部门
     */
    private String department;
    /**
     * @Fields problemNumber 评审问题未解决数
     */
    private Integer problemNumber;
    /**
     * @Fields defectsNumber 缺陷问题未解决数
     */
    private Integer defectsNumber;
    /**
     * @Fields productionDate 投产日期
     */
    private String productionDate;
    /**
     * @Fields calculateFlag 处理标识（Y已处理，N未处理）
     */
    private String calculateFlag;
    /**
     * @Fields problemNumberSum 评审问题未解决总数
     */
    private Integer problemNumberSum;
    /**
     * @Fields defectsNumberSum 缺陷问题未解决总数
     */
    private Integer defectsNumberSum;

    public String getReqNo() {
        return reqNo;
    }

    public void setReqNo(String reqNo) {
        this.reqNo = reqNo;
    }

    public String getJirakey() {
        return jirakey;
    }

    public void setJirakey(String jirakey) {
        this.jirakey = jirakey;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getProblemNumber() {
        return problemNumber;
    }

    public void setProblemNumber(Integer problemNumber) {
        this.problemNumber = problemNumber;
    }

    public Integer getDefectsNumber() {
        return defectsNumber;
    }

    public void setDefectsNumber(Integer defectsNumber) {
        this.defectsNumber = defectsNumber;
    }

    public String getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(String productionDate) {
        this.productionDate = productionDate;
    }

    public String getCalculateFlag() {
        return calculateFlag;
    }

    public void setCalculateFlag(String calculateFlag) {
        this.calculateFlag = calculateFlag;
    }

    public Integer getProblemNumberSum() {
        return problemNumberSum;
    }

    public void setProblemNumberSum(Integer problemNumberSum) {
        this.problemNumberSum = problemNumberSum;
    }

    public Integer getDefectsNumberSum() {
        return defectsNumberSum;
    }

    public void setDefectsNumberSum(Integer defectsNumberSum) {
        this.defectsNumberSum = defectsNumberSum;
    }
}