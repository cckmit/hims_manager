/*
 * @ClassName SupportWorkloadDO
 * @Description
 * @version 1.0
 * @Date 2020-08-27 15:02:17
 */
package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.request.GenericDTO;

/**
 * @author ty
 */
public class SupportWorkloadDTO extends GenericDTO {
    /**
     *  documentnumber 文号
     */
    private String documentnumber;
    /**
     *  processstartdate 流程开始日期
     */
    private String processstartdate;
    /**
     *  productmanagementdepartment 产品管理部门
     */
    private String productmanagementdepartment;
    /**
     *  productowner 产品负责人
     */
    private String productowner;
    /**
     *  supportingmanufacturerproducts 支撑厂家产品
     */
    private String supportingmanufacturerproducts;
    /**
     *  supportthetopic 支撑主题
     */
    private String supportthetopic;
    /**
     *  contentdescription 内容描述
     */
    private String contentdescription;
    /**
     *  functionpointsdetail 功能点明细
     */
    private String functionpointsdetail;
    /**
     *  proposaltime 需求提出时间
     */
    private String proposaltime;
    /**
     * @Fields completiontime 需求完成时间
     */
    private String completiontime;
    /**
     *  supportmanager 支撑负责人
     */
    private String supportmanager;
    /**
     *  supportworkload 支撑工作量
     */
    private String supportworkload;
    /**
     *  finalworkload 最终工作量
     */
    private String finalworkload;
    /**
     *  supportingmanufacturers 支撑厂家
     */
    private String supportingmanufacturers;
    /**
     *  costdepartment 成本管理部门
     */
    private String costdepartment;
    /**
     *  secondlevelorganization 二级主导团队
     */
    private String secondlevelorganization;
    /**
     *  remark 备注
     */
    private String remark;
    /**
     * 页数
     */
    private int pageNum;
    /**
     * 页面大小
     */
    private int pageSize;
    private String firstLevelOrganization;

    public String getFirstLevelOrganization() {
        return firstLevelOrganization;
    }

    public void setFirstLevelOrganization(String firstLevelOrganization) {
        this.firstLevelOrganization = firstLevelOrganization;
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

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "SupportWorkloadBO{" +
                "documentnumber='" + documentnumber + '\'' +
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
                ", costdepartment='" + costdepartment + '\'' +
                ", secondlevelorganization='" + secondlevelorganization + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
