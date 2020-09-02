/*
 * @ClassName DemandEaseDevelopmentDO
 * @Description
 * @version 1.0
 * @Date 2020-08-31 14:33:51
 */
package com.cmpay.lemon.monitor.dto;


import com.cmpay.framework.data.request.GenericDTO;

public class DemandEaseDevelopmentDTO extends GenericDTO {
    /**
     *  documentnumber 文号
     */
    private String documentnumber;
    /**
     *  processstartdate 流程开始日期
     */
    private String processstartdate;
    /**
     *  developmentowner 开发负责人
     */
    private String developmentowner;
    /**
     *  supportingmanufacturers 支撑厂家
     */
    private String supportingmanufacturers;
    /**
     *  supportingmanufacturerproducts 支撑厂家产品
     */
    private String supportingmanufacturerproducts;
    /**
     *  cuttype 裁剪类型
     */
    private String cuttype;
    /**
     *  demandtheme 需求主题
     */
    private String demandtheme;
    /**
     *  requirementdescription 需求描述
     */
    private String requirementdescription;
    /**
     *  commissioningdate 投产日期
     */
    private String commissioningdate;
    /**
     *  acceptancedate 验收日期
     */
    private String acceptancedate;
    /**
     *  acceptor 验收人
     */
    private String acceptor;
    /**
     *  developmentworkloadassess 开发工作量评估
     */
    private String developmentworkloadassess;
    /**
     *  developmentworkload 开发工作量
     */
    private String developmentworkload;
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
    private String startTime;
    private String endTime;
    private String reqImplMon;
    private String firstLevelOrganization;
    /**
     * 页数
     */
    private int pageNum;
    /**
     * 页面大小
     */
    private int pageSize;

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

    public String getReqImplMon() {
        return reqImplMon;
    }

    public void setReqImplMon(String reqImplMon) {
        this.reqImplMon = reqImplMon;
    }

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

    public String getDevelopmentowner() {
        return developmentowner;
    }

    public void setDevelopmentowner(String developmentowner) {
        this.developmentowner = developmentowner;
    }

    public String getSupportingmanufacturers() {
        return supportingmanufacturers;
    }

    public void setSupportingmanufacturers(String supportingmanufacturers) {
        this.supportingmanufacturers = supportingmanufacturers;
    }

    public String getSupportingmanufacturerproducts() {
        return supportingmanufacturerproducts;
    }

    public void setSupportingmanufacturerproducts(String supportingmanufacturerproducts) {
        this.supportingmanufacturerproducts = supportingmanufacturerproducts;
    }

    public String getCuttype() {
        return cuttype;
    }

    public void setCuttype(String cuttype) {
        this.cuttype = cuttype;
    }

    public String getDemandtheme() {
        return demandtheme;
    }

    public void setDemandtheme(String demandtheme) {
        this.demandtheme = demandtheme;
    }

    public String getRequirementdescription() {
        return requirementdescription;
    }

    public void setRequirementdescription(String requirementdescription) {
        this.requirementdescription = requirementdescription;
    }

    public String getCommissioningdate() {
        return commissioningdate;
    }

    public void setCommissioningdate(String commissioningdate) {
        this.commissioningdate = commissioningdate;
    }

    public String getAcceptancedate() {
        return acceptancedate;
    }

    public void setAcceptancedate(String acceptancedate) {
        this.acceptancedate = acceptancedate;
    }

    public String getAcceptor() {
        return acceptor;
    }

    public void setAcceptor(String acceptor) {
        this.acceptor = acceptor;
    }

    public String getDevelopmentworkloadassess() {
        return developmentworkloadassess;
    }

    public void setDevelopmentworkloadassess(String developmentworkloadassess) {
        this.developmentworkloadassess = developmentworkloadassess;
    }

    public String getDevelopmentworkload() {
        return developmentworkload;
    }

    public void setDevelopmentworkload(String developmentworkload) {
        this.developmentworkload = developmentworkload;
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
        return "DemandEaseDevelopmentBO{" +
                "documentnumber='" + documentnumber + '\'' +
                ", processstartdate='" + processstartdate + '\'' +
                ", developmentowner='" + developmentowner + '\'' +
                ", supportingmanufacturers='" + supportingmanufacturers + '\'' +
                ", supportingmanufacturerproducts='" + supportingmanufacturerproducts + '\'' +
                ", cuttype='" + cuttype + '\'' +
                ", demandtheme='" + demandtheme + '\'' +
                ", requirementdescription='" + requirementdescription + '\'' +
                ", commissioningdate='" + commissioningdate + '\'' +
                ", acceptancedate='" + acceptancedate + '\'' +
                ", acceptor='" + acceptor + '\'' +
                ", developmentworkloadassess='" + developmentworkloadassess + '\'' +
                ", developmentworkload='" + developmentworkload + '\'' +
                ", costdepartment='" + costdepartment + '\'' +
                ", secondlevelorganization='" + secondlevelorganization + '\'' +
                ", remark='" + remark + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", reqImplMon='" + reqImplMon + '\'' +
                ", firstLevelOrganization='" + firstLevelOrganization + '\'' +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }
}
