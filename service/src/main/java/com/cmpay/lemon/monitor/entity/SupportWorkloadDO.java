/*
 * @ClassName SupportWorkloadDO
 * @Description
 * @version 1.0
 * @Date 2020-08-27 15:02:17
 */
package com.cmpay.lemon.monitor.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;
/**
 * @author ty
 */
@DataObject
public class SupportWorkloadDO extends BaseDO {

    @Excel(name = "实施月份")
    private String reqImplMon;
    @Excel(name = "一级主导团队")
    private String firstLevelOrganization;
    /**
     * @Fields productmanagementdepartment 产品管理部门
     */
    @Excel(name = "产品管理部门")
    private String productmanagementdepartment;
    /**
     * @Fields costdepartment 成本管理部门
     */
    @Excel(name = "成本管理部门")
    private String costdepartment;
    /**
     * @Fields documentnumber 文号
     */
    @Excel(name = "文号")
    private String documentnumber;
    /**
     * @Fields processstartdate 流程开始日期
     */
    @Excel(name = "流程开始日期")
    private String processstartdate;
    /**
     * @Fields productowner 产品负责人
     */
    @Excel(name = "产品负责人")
    private String productowner;
    /**
     * @Fields supportingmanufacturerproducts 支撑厂家产品
     */
    @Excel(name = "支撑厂家产品")
    private String supportingmanufacturerproducts;
    /**
     * @Fields supportthetopic 支撑主题
     */
    @Excel(name = "支撑主题")
    private String supportthetopic;
    /**
     * @Fields contentdescription 内容描述
     */
    @Excel(name = "内容描述")
    private String contentdescription;
    /**
     * @Fields functionpointsdetail 功能点明细
     */
    @Excel(name = "功能点明细")
    private String functionpointsdetail;
    /**
     * @Fields proposaltime 需求提出时间
     */
    @Excel(name = "需求提出时间")
    private String proposaltime;
    /**
     * @Fields completiontime 需求完成时间
     */
    @Excel(name = "需求完成时间")
    private String completiontime;
    /**
     * @Fields supportmanager 支撑负责人
     */
    @Excel(name = "支撑负责人")
    private String supportmanager;
    /**
     * @Fields supportworkload 支撑工作量
     */
    @Excel(name = "支撑工作量")
    private String supportworkload;
    /**
     * @Fields finalworkload 最终工作量
     */
    @Excel(name = "最终工作量")
    private String finalworkload;
    /**
     * @Fields supportingmanufacturers 支撑厂家
     */
    @Excel(name = "支撑厂家")
    private String supportingmanufacturers;
    /**
     * @Fields secondlevelorganization 二级主导团队
     */
    private String secondlevelorganization;
    /**
     * @Fields remark 备注
     */
    @Excel(name = "备注")
    private String remark;
    private String startTime;
    private String endTime;


    public String getFirstLevelOrganization() {
        return firstLevelOrganization;
    }

    public void setFirstLevelOrganization(String firstLevelOrganization) {
        this.firstLevelOrganization = firstLevelOrganization;
    }

    public String getReqImplMon() {
        return reqImplMon;
    }

    public void setReqImplMon(String reqImplMon) {
        this.reqImplMon = reqImplMon;
    }

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
    public String getDocumentnumber() {
        return documentnumber;
    }

    public void setDocumentnumber(String documentnumber) {
        this.documentnumber = documentnumber;
    }

    public String getProcessstartdate() {
        return processstartdate;
    }

    public void setProcessstartdate(String processstartdate) {
        this.processstartdate = processstartdate;
    }

    public String getProductmanagementdepartment() {
        return productmanagementdepartment;
    }

    public void setProductmanagementdepartment(String productmanagementdepartment) {
        this.productmanagementdepartment = productmanagementdepartment;
    }

    public String getProductowner() {
        return productowner;
    }

    public void setProductowner(String productowner) {
        this.productowner = productowner;
    }

    public String getSupportingmanufacturerproducts() {
        return supportingmanufacturerproducts;
    }

    public void setSupportingmanufacturerproducts(String supportingmanufacturerproducts) {
        this.supportingmanufacturerproducts = supportingmanufacturerproducts;
    }

    public String getSupportthetopic() {
        return supportthetopic;
    }

    public void setSupportthetopic(String supportthetopic) {
        this.supportthetopic = supportthetopic;
    }

    public String getContentdescription() {
        return contentdescription;
    }

    public void setContentdescription(String contentdescription) {
        this.contentdescription = contentdescription;
    }

    public String getFunctionpointsdetail() {
        return functionpointsdetail;
    }

    public void setFunctionpointsdetail(String functionpointsdetail) {
        this.functionpointsdetail = functionpointsdetail;
    }

    public String getProposaltime() {
        return proposaltime;
    }

    public void setProposaltime(String proposaltime) {
        this.proposaltime = proposaltime;
    }

    public String getCompletiontime() {
        return completiontime;
    }

    public void setCompletiontime(String completiontime) {
        this.completiontime = completiontime;
    }

    public String getSupportmanager() {
        return supportmanager;
    }

    public void setSupportmanager(String supportmanager) {
        this.supportmanager = supportmanager;
    }

    public String getSupportworkload() {
        return supportworkload;
    }

    public void setSupportworkload(String supportworkload) {
        this.supportworkload = supportworkload;
    }

    public String getFinalworkload() {
        return finalworkload;
    }

    public void setFinalworkload(String finalworkload) {
        this.finalworkload = finalworkload;
    }

    public String getSupportingmanufacturers() {
        return supportingmanufacturers;
    }

    public void setSupportingmanufacturers(String supportingmanufacturers) {
        this.supportingmanufacturers = supportingmanufacturers;
    }

    public String getCostdepartment() {
        return costdepartment;
    }

    public void setCostdepartment(String costdepartment) {
        this.costdepartment = costdepartment;
    }

    public String getSecondlevelorganization() {
        return secondlevelorganization;
    }

    public void setSecondlevelorganization(String secondlevelorganization) {
        this.secondlevelorganization = secondlevelorganization;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "SupportWorkloadDO{" +
                "firstLevelOrganization='" + firstLevelOrganization + '\'' +
                ", costdepartment='" + costdepartment + '\'' +
                ", reqImplMon='" + reqImplMon + '\'' +
                ", documentnumber='" + documentnumber + '\'' +
                ", processstartdate='" + processstartdate + '\'' +
                ", productmanagementdepartment='" + productmanagementdepartment + '\'' +
                ", productowner='" + productowner + '\'' +
                ", supportingmanufacturerproducts='" + supportingmanufacturerproducts + '\'' +
                ", supportthetopic='" + supportthetopic + '\'' +
                ", contentdescription='" + contentdescription + '\'' +
                ", functionpointsdetail='" + functionpointsdetail + '\'' +
                ", proposaltime='" + proposaltime + '\'' +
                ", completiontime='" + completiontime + '\'' +
                ", supportmanager='" + supportmanager + '\'' +
                ", supportworkload='" + supportworkload + '\'' +
                ", finalworkload='" + finalworkload + '\'' +
                ", supportingmanufacturers='" + supportingmanufacturers + '\'' +
                ", secondlevelorganization='" + secondlevelorganization + '\'' +
                ", remark='" + remark + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
