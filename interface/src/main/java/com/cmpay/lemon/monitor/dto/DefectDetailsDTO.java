/*
 * @ClassName DefectDetailsDO
 * @Description
 * @version 1.0
 * @Date 2020-06-30 16:04:29
 */
package com.cmpay.lemon.monitor.dto;

/**
 * @author wlr
 */
public class DefectDetailsDTO {
    /**
     * @Fields jireKey 缺陷jira编号
     */
    private String jireKey;
    /**
     * @Fields reqNo 缺陷归属需求编号
     */
    private String reqNo;
    /**
     * @Fields epicKey 对应epic编号
     */
    private String epicKey;
    /**
     * @Fields defectType 缺陷类型
     */
    private String defectType;
    /**
     * @Fields defectStatus 缺陷状态
     */
    private String defectStatus;
    /**
     * @Fields defectRegistrant 缺陷登记人
     */
    private String defectRegistrant;
    /**
     * @Fields defectsDepartment 缺陷归属部门
     */
    private String defectsDepartment;
    /**
     * @Fields registrationDate 登记日期
     */
    private String registrationDate;
    /**
     * @Fields defectDetails 缺陷详情
     */
    private String defectDetails;
    /**
     * @Fields assignee 问题处理人
     */
    private String assignee;
    /**
     * @Fields testNumber 缺陷重测次数
     */
    private String testNumber;

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

    public String getDefectType() {
        return defectType;
    }

    public void setDefectType(String defectType) {
        this.defectType = defectType;
    }

    public String getDefectStatus() {
        return defectStatus;
    }

    public void setDefectStatus(String defectStatus) {
        this.defectStatus = defectStatus;
    }

    public String getDefectRegistrant() {
        return defectRegistrant;
    }

    public void setDefectRegistrant(String defectRegistrant) {
        this.defectRegistrant = defectRegistrant;
    }

    public String getDefectsDepartment() {
        return defectsDepartment;
    }

    public void setDefectsDepartment(String defectsDepartment) {
        this.defectsDepartment = defectsDepartment;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getDefectDetails() {
        return defectDetails;
    }

    public void setDefectDetails(String defectDetails) {
        this.defectDetails = defectDetails;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getTestNumber() {
        return testNumber;
    }

    public void setTestNumber(String testNumber) {
        this.testNumber = testNumber;
    }

    @Override
    public String toString() {
        return "DefectDetailsDO{" +
                "jireKey='" + jireKey + '\'' +
                ", reqNo='" + reqNo + '\'' +
                ", epicKey='" + epicKey + '\'' +
                ", defectType='" + defectType + '\'' +
                ", defectStatus='" + defectStatus + '\'' +
                ", defectRegistrant='" + defectRegistrant + '\'' +
                ", defectsDepartment='" + defectsDepartment + '\'' +
                ", registrationDate='" + registrationDate + '\'' +
                ", defectDetails='" + defectDetails + '\'' +
                ", assignee='" + assignee + '\'' +
                ", testNumber='" + testNumber + '\'' +
                '}';
    }
}
