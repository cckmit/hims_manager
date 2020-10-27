/*
 * @ClassName ProductionIssueDetailsDO
 * @Description 
 * @version 1.0
 * @Date 2020-10-27 11:32:57
 */
package com.cmpay.lemon.monitor.entity;

import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;

@DataObject
public class ProductionIssueDetailsDO extends BaseDO {
    /**
     * @Fields jiraKey 投产问题jira编号
     */
    private String jiraKey;
    /**
     * @Fields proNumber 投产问题归属投产编号
     */
    private String proNumber;
    /**
     * @Fields productionIssueStatus 投产问题状态
     */
    private String productionIssueStatus;
    /**
     * @Fields productionIssueRegistrant 投产问题登记人
     */
    private String productionIssueRegistrant;
    /**
     * @Fields productionIssueDepartment 投产问题归属部门
     */
    private String productionIssueDepartment;
    /**
     * @Fields registrationDate 登记日期
     */
    private String registrationDate;
    /**
     * @Fields productionIssueDetails 问题详情
     */
    private String productionIssueDetails;
    /**
     * @Fields assignee 问题处理人
     */
    private String assignee;

    public String getJiraKey() {
        return jiraKey;
    }

    public void setJiraKey(String jiraKey) {
        this.jiraKey = jiraKey;
    }

    public String getProNumber() {
        return proNumber;
    }

    public void setProNumber(String proNumber) {
        this.proNumber = proNumber;
    }

    public String getProductionIssueStatus() {
        return productionIssueStatus;
    }

    public void setProductionIssueStatus(String productionIssueStatus) {
        this.productionIssueStatus = productionIssueStatus;
    }

    public String getProductionIssueRegistrant() {
        return productionIssueRegistrant;
    }

    public void setProductionIssueRegistrant(String productionIssueRegistrant) {
        this.productionIssueRegistrant = productionIssueRegistrant;
    }

    public String getProductionIssueDepartment() {
        return productionIssueDepartment;
    }

    public void setProductionIssueDepartment(String productionIssueDepartment) {
        this.productionIssueDepartment = productionIssueDepartment;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getProductionIssueDetails() {
        return productionIssueDetails;
    }

    public void setProductionIssueDetails(String productionIssueDetails) {
        this.productionIssueDetails = productionIssueDetails;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }
}