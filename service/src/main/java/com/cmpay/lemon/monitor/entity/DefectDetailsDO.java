/*
 * @ClassName DefectDetailsDO
 * @Description
 * @version 1.0
 * @Date 2020-10-21 11:20:35
 */
package com.cmpay.lemon.monitor.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;

@DataObject
public class DefectDetailsDO extends BaseDO {
    /**
     * @Fields jireKey 缺陷jira编号
     */
    @Excel(name = "JIRA编号")
    private String jireKey;
    /**
     * @Fields reqNo 缺陷归属需求编号
     */
    @Excel(name = "需求编号")
    private String reqNo;
    /**
     * @Fields defectName 缺陷名称
     */
    @Excel(name = "缺陷名称")
    private String defectName;
    /**
     * @Fields epicKey 对应epic编号
     */
    @Excel(name = "Epic编号")
    private String epicKey;
    /**
     * @Fields defectType 缺陷类型
     */
    @Excel(name = "缺陷类型")
    private String defectType;
    /**
     * @Fields defectStatus 缺陷状态
     */
    @Excel(name = "缺陷状态")
    private String defectStatus;
    /**
     * @Fields defectRegistrant 缺陷登记人
     */
    @Excel(name = "缺陷登记人")
    private String defectRegistrant;

    /**
     * @Fields registrationDate 登记日期
     */
    @Excel(name = "登记日期")
    private String registrationDate;
    /**
     * @Fields firstlevelorganization 一级主导团队
     */
    @Excel(name = "一级主导团队")
    private String firstlevelorganization;
    /**
     * @Fields assignee 问题处理经办人
     */
    @Excel(name = "问题处理经办人")
    private String assignee;
    /**
     * @Fields problemHandlerDepartment 经办人归属部门
     */
    @Excel(name = "经办人归属部门")
    private String problemHandlerDepartment;
    /**
     * @Fields problemHandler 问题处理人
     */
    @Excel(name = "问题处理人")
    private String problemHandler;
    /**
     * @Fields defectsDepartment 缺陷归属部门
     */
    @Excel(name = "问题归属部门")
    private String defectsDepartment;
    /**
     * @Fields defectDetails 缺陷详情
     */
    @Excel(name = "缺陷详情")
    private String defectDetails;

    /**
     * @Fields testNumber 缺陷重测次数
     */
    @Excel(name = "缺陷重测次数")
    private Integer testNumber;
    /**
     * @Fields securityLevel 缺陷严重级别（致命 严重 一般 轻微）
     */
    @Excel(name = "缺陷严重级别")
    private String securityLevel;
    /**
     * @Fields productLine 缺陷归属产品线
     */
    @Excel(name = "缺陷归属产品线")
    private String productLine;
    /**
     * @Fields solution 解决结果
     */
    @Excel(name = "解决结果")
    private String solution;



    private String startTime;

    private String endTime;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

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

    public String getDefectName() {
        return defectName;
    }

    public void setDefectName(String defectName) {
        this.defectName = defectName;
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

    public Integer getTestNumber() {
        return testNumber;
    }

    public void setTestNumber(Integer testNumber) {
        this.testNumber = testNumber;
    }

    public String getSecurityLevel() {
        return securityLevel;
    }

    public void setSecurityLevel(String securityLevel) {
        this.securityLevel = securityLevel;
    }

    public String getProductLine() {
        return productLine;
    }

    public void setProductLine(String productLine) {
        this.productLine = productLine;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getFirstlevelorganization() {
        return firstlevelorganization;
    }

    public void setFirstlevelorganization(String firstlevelorganization) {
        this.firstlevelorganization = firstlevelorganization;
    }

    public String getProblemHandler() {
        return problemHandler;
    }

    public void setProblemHandler(String problemHandler) {
        this.problemHandler = problemHandler;
    }

    public String getProblemHandlerDepartment() {
        return problemHandlerDepartment;
    }

    public void setProblemHandlerDepartment(String problemHandlerDepartment) {
        this.problemHandlerDepartment = problemHandlerDepartment;
    }

    @Override
    public String toString() {
        return "DefectDetailsDO{" +
                "jireKey='" + jireKey + '\'' +
                ", reqNo='" + reqNo + '\'' +
                ", defectName='" + defectName + '\'' +
                ", epicKey='" + epicKey + '\'' +
                ", defectType='" + defectType + '\'' +
                ", defectStatus='" + defectStatus + '\'' +
                ", defectRegistrant='" + defectRegistrant + '\'' +
                ", defectsDepartment='" + defectsDepartment + '\'' +
                ", registrationDate='" + registrationDate + '\'' +
                ", defectDetails='" + defectDetails + '\'' +
                ", assignee='" + assignee + '\'' +
                ", testNumber=" + testNumber +
                ", securityLevel='" + securityLevel + '\'' +
                ", productLine='" + productLine + '\'' +
                ", solution='" + solution + '\'' +
                ", firstlevelorganization='" + firstlevelorganization + '\'' +
                ", problemHandler='" + problemHandler + '\'' +
                ", problemHandlerDepartment='" + problemHandlerDepartment + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
