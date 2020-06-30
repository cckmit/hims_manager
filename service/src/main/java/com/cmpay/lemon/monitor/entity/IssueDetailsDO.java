/*
 * @ClassName IssueDetailsDO
 * @Description 
 * @version 1.0
 * @Date 2020-06-30 16:04:29
 */
package com.cmpay.lemon.monitor.entity;

import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;

@DataObject
public class IssueDetailsDO extends BaseDO {
    /**
     * @Fields jireKey 问题jira编号
     */
    private String jireKey;
    /**
     * @Fields reqNo 问题归属需求编号
     */
    private String reqNo;
    /**
     * @Fields epicKey 对应epic编号
     */
    private String epicKey;
    /**
     * @Fields issueType 问题类型
     */
    private String issueType;
    /**
     * @Fields issueStatus 问题状态
     */
    private String issueStatus;
    /**
     * @Fields issueRegistrant 问题登记人
     */
    private String issueRegistrant;
    /**
     * @Fields issueDepartment 问题归属部门
     */
    private String issueDepartment;
    /**
     * @Fields registrationDate 登记日期
     */
    private String registrationDate;
    /**
     * @Fields issueDetails 问题详情
     */
    private String issueDetails;
    /**
     * @Fields assignee 问题处理人
     */
    private String assignee;

    public String getJireKey() {
        return jireKey;
    }

    public void setJireKey(String jireKey) {
        this.jireKey = jireKey;
    }

    public String getReqNo() {
        return reqNo;
    }

    public void setReqNo(String reqNo) {
        this.reqNo = reqNo;
    }

    public String getEpicKey() {
        return epicKey;
    }

    public void setEpicKey(String epicKey) {
        this.epicKey = epicKey;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getIssueStatus() {
        return issueStatus;
    }

    public void setIssueStatus(String issueStatus) {
        this.issueStatus = issueStatus;
    }

    public String getIssueRegistrant() {
        return issueRegistrant;
    }

    public void setIssueRegistrant(String issueRegistrant) {
        this.issueRegistrant = issueRegistrant;
    }

    public String getIssueDepartment() {
        return issueDepartment;
    }

    public void setIssueDepartment(String issueDepartment) {
        this.issueDepartment = issueDepartment;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getIssueDetails() {
        return issueDetails;
    }

    public void setIssueDetails(String issueDetails) {
        this.issueDetails = issueDetails;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }
}