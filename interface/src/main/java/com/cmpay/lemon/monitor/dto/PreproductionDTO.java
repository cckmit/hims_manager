package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.request.GenericDTO;

import java.sql.Date;

/**
 * @author: zhou_xiong
 */
public class PreproductionDTO extends GenericDTO {
    //@Excel(name = "投产编号")
    private String preNumber;
    //@Excel(name = "需求名称及内容简述")
    private String preNeed;
    //@Excel(name = "投产类型")
    private String preType;
    private java.sql.Date preDateStart;
    private java.sql.Date preDateEnd;
    // @Excel(name = "计划投产日期")
    private java.sql.Date preDate;
    //@Excel(name = "申请部门")
    private String applicationDept;
    //@Excel(name = "预投产申请人")
    private String preApplicant;
    //@Excel(name = "申请人联系方式")
    private String applicantTel;
    //@Excel(name = "产品经理")
    private String preManager;
    //@Excel(name = "开发负责人")
    private String developmentLeader;
    //@Excel(name = "开发负责人邮箱")
    private String mailLeader;
    //@Excel(name = "验证人")
    private String identifier;
    //@Excel(name = "验证人联系方式")
    private String identifierTel;
    //@Excel(name = "验证复核人")
    private String proChecker;
    //@Excel(name = "验证复核人联系方式")
    private String checkerTel;
    //@Excel(name = "预投产需求状态")
    private String preStatus;
    //@Excel(name = "备注")
    private String remark;
    //@Excel(name = "预投产包上传时间")
    private Date proPkgTime;
    //@Excel(name = "预投产包名")
    private String proPkgName;
    //@Excel(name = "预投产部署结果")
    private String productionDeploymentResult;
    /**
     * @Fields proAdvanceResult 预投产验证结果
     */
    //@Excel(name = "预投产验证结果")
    private String proAdvanceResult;
    /**
     * 页数
     */
    private int pageNum;
    /**
     * 页面大小
     */
    private int pageSize;

    public PreproductionDTO() {
    }

    @Override
    public String toString() {
        return "PreproductionDTO{" +
                "preNumber='" + preNumber + '\'' +
                ", preNeed='" + preNeed + '\'' +
                ", preType='" + preType + '\'' +
                ", preDateStart=" + preDateStart +
                ", preDateEnd=" + preDateEnd +
                ", preDate=" + preDate +
                ", applicationDept='" + applicationDept + '\'' +
                ", preApplicant='" + preApplicant + '\'' +
                ", applicantTel='" + applicantTel + '\'' +
                ", preManager='" + preManager + '\'' +
                ", developmentLeader='" + developmentLeader + '\'' +
                ", mailLeader='" + mailLeader + '\'' +
                ", identifier='" + identifier + '\'' +
                ", identifierTel='" + identifierTel + '\'' +
                ", proChecker='" + proChecker + '\'' +
                ", checkerTel='" + checkerTel + '\'' +
                ", preStatus='" + preStatus + '\'' +
                ", proAdvanceResult='" + proAdvanceResult + '\'' +
                ", remark='" + remark + '\'' +
                ", proPkgTime=" + proPkgTime +
                ", proPkgName='" + proPkgName + '\'' +
                ", productionDeploymentResult='" + productionDeploymentResult + '\'' +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }

    public String getPreNumber() {
        return preNumber;
    }

    public void setPreNumber(String preNumber) {
        this.preNumber = preNumber;
    }

    public String getPreNeed() {
        return preNeed;
    }

    public void setPreNeed(String preNeed) {
        this.preNeed = preNeed;
    }

    public String getPreType() {
        return preType;
    }

    public void setPreType(String preType) {
        this.preType = preType;
    }

    public Date getPreDateStart() {
        return preDateStart;
    }

    public void setPreDateStart(Date preDateStart) {
        this.preDateStart = preDateStart;
    }

    public Date getPreDateEnd() {
        return preDateEnd;
    }

    public void setPreDateEnd(Date preDateEnd) {
        this.preDateEnd = preDateEnd;
    }

    public Date getPreDate() {
        return preDate;
    }

    public void setPreDate(Date preDate) {
        this.preDate = preDate;
    }

    public String getApplicationDept() {
        return applicationDept;
    }

    public void setApplicationDept(String applicationDept) {
        this.applicationDept = applicationDept;
    }

    public String getPreApplicant() {
        return preApplicant;
    }

    public void setPreApplicant(String preApplicant) {
        this.preApplicant = preApplicant;
    }

    public String getApplicantTel() {
        return applicantTel;
    }

    public void setApplicantTel(String applicantTel) {
        this.applicantTel = applicantTel;
    }

    public String getPreManager() {
        return preManager;
    }

    public void setPreManager(String preManager) {
        this.preManager = preManager;
    }

    public String getDevelopmentLeader() {
        return developmentLeader;
    }

    public void setDevelopmentLeader(String developmentLeader) {
        this.developmentLeader = developmentLeader;
    }

    public String getMailLeader() {
        return mailLeader;
    }

    public void setMailLeader(String mailLeader) {
        this.mailLeader = mailLeader;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifierTel() {
        return identifierTel;
    }

    public void setIdentifierTel(String identifierTel) {
        this.identifierTel = identifierTel;
    }

    public String getProChecker() {
        return proChecker;
    }

    public void setProChecker(String proChecker) {
        this.proChecker = proChecker;
    }

    public String getCheckerTel() {
        return checkerTel;
    }

    public void setCheckerTel(String checkerTel) {
        this.checkerTel = checkerTel;
    }

    public String getPreStatus() {
        return preStatus;
    }

    public void setPreStatus(String preStatus) {
        this.preStatus = preStatus;
    }

    public String getProAdvanceResult() {
        return proAdvanceResult;
    }

    public void setProAdvanceResult(String proAdvanceResult) {
        this.proAdvanceResult = proAdvanceResult;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getProPkgTime() {
        return proPkgTime;
    }

    public void setProPkgTime(Date proPkgTime) {
        this.proPkgTime = proPkgTime;
    }

    public String getProPkgName() {
        return proPkgName;
    }

    public void setProPkgName(String proPkgName) {
        this.proPkgName = proPkgName;
    }

    public String getProductionDeploymentResult() {
        return productionDeploymentResult;
    }

    public void setProductionDeploymentResult(String productionDeploymentResult) {
        this.productionDeploymentResult = productionDeploymentResult;
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
}
