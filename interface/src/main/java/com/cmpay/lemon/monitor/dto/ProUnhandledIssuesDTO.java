/*
 * @ClassName ProUnhandledIssuesDO
 * @Description
 * @version 1.0
 * @Date 2020-07-03 17:03:49
 */
package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.request.GenericDTO;

public class ProUnhandledIssuesDTO extends GenericDTO {
    /**
     *  reqNo 需求编号
     */
    private String reqNo;
    /**
     *  jirakey epic任务jira编号
     */
    private String jirakey;
    /**
     *  department 主导部门
     */
    private String department;
    /**
     * problemNumber 评审问题未解决数
     */
    private Integer problemNumber;
    /**
     *  defectsNumber 缺陷问题未解决数
     */
    private Integer defectsNumber;
    /**
     *  productionDate 投产日期
     */
    private String productionDate;
    /**
     * calculateFlag 处理标识（Y已处理，N未处理）
     */
    private String calculateFlag;
    /**
     *  problemNumberSum 评审问题未解决总数
     */
    private Integer problemNumberSum;
    /**
     *  defectsNumberSum 缺陷问题未解决总数
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

    @Override
    public String toString() {
        return "ProUnhandledIssuesDTO{" +
                "reqNo='" + reqNo + '\'' +
                ", jirakey='" + jirakey + '\'' +
                ", department='" + department + '\'' +
                ", problemNumber=" + problemNumber +
                ", defectsNumber=" + defectsNumber +
                ", productionDate='" + productionDate + '\'' +
                ", calculateFlag='" + calculateFlag + '\'' +
                ", problemNumberSum=" + problemNumberSum +
                ", defectsNumberSum=" + defectsNumberSum +
                '}';
    }
}
