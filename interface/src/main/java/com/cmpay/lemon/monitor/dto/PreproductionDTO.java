package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.request.GenericDTO;


import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author: zhou_xiong
 */
public class PreproductionDTO extends GenericDTO {
    private Date preDateStart;
    private Date preDateEnd;
    /**
     * @Fields preNumber 预投产编号
     */
    private String preNumber;
    /**
     * @Fields preNeed 预投产名称
     */
    private String preNeed;
    /**
     * @Fields preDate 预计预投产时间
     */
    private Date preDate;
    /**
     * @Fields applicationDept 申请部门
     */
    private String applicationDept;
    /**
     * @Fields preApplicant 预投产申请人
     */
    private String preApplicant;
    /**
     * @Fields applicantTel 预投产申请人联系方式
     */
    private String applicantTel;
    /**
     * @Fields preManager 产品经理
     */
    private String preManager;
    /**
     * @Fields preStatus 预投产状态
     */
    private String preStatus;
    /**
     * @Fields productionDeploymentResult 预投产部署结果
     */
    private String productionDeploymentResult;
    /**
     * @Fields proAdvanceResult 预投产验证结果
     */
    private String proAdvanceResult;
    /**
     * @Fields proPkgTime 版本操作投产包上传时间
     */
    private LocalDateTime proPkgTime;
    /**
     * @Fields proPkgName 版本操作投产包名称
     */
    private String proPkgName;
    /**
     * @Fields developmentLeader 开发负责人
     */
    private String developmentLeader;
    /**
     * @Fields mailLeader 开发负责人邮箱
     */
    private String mailLeader;
    /**
     * @Fields identifier 验证人
     */
    private String identifier;
    /**
     * @Fields identifierTel 验证人联系方式
     */
    private String identifierTel;
    /**
     * @Fields proChecker 验证复核人
     */
    private String proChecker;
    /**
     * @Fields checkerTel 验证复核人联系方式
     */
    private String checkerTel;
    /**
     * @Fields remark 备注
     */
    private String remark;
    /**
     * @Fields ddlPkgName ddl操作预投产包
     */
    private String ddlPkgName;
    /**
     * @Fields ddlPkgTime ddl操作预投产包上传时间
     */
    private LocalDateTime ddlPkgTime;
    /**
     * @Fields isDbaOperation 是否有DBA操作
     */
    private String isDbaOperation;
    /**
     * @Fields isDbaOperationComplete DBA操作是否完成
     */
    private String isDbaOperationComplete;
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
                "preDateStart=" + preDateStart +
                ", preDateEnd=" + preDateEnd +
                ", preNumber='" + preNumber + '\'' +
                ", preNeed='" + preNeed + '\'' +
                ", preDate=" + preDate +
                ", applicationDept='" + applicationDept + '\'' +
                ", preApplicant='" + preApplicant + '\'' +
                ", applicantTel='" + applicantTel + '\'' +
                ", preManager='" + preManager + '\'' +
                ", preStatus='" + preStatus + '\'' +
                ", productionDeploymentResult='" + productionDeploymentResult + '\'' +
                ", proAdvanceResult='" + proAdvanceResult + '\'' +
                ", proPkgTime=" + proPkgTime +
                ", proPkgName='" + proPkgName + '\'' +
                ", developmentLeader='" + developmentLeader + '\'' +
                ", mailLeader='" + mailLeader + '\'' +
                ", identifier='" + identifier + '\'' +
                ", identifierTel='" + identifierTel + '\'' +
                ", proChecker='" + proChecker + '\'' +
                ", checkerTel='" + checkerTel + '\'' +
                ", remark='" + remark + '\'' +
                ", ddlPkgName='" + ddlPkgName + '\'' +
                ", ddlPkgTime=" + ddlPkgTime +
                ", isDbaOperation='" + isDbaOperation + '\'' +
                ", isDbaOperationComplete='" + isDbaOperationComplete + '\'' +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
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

    public String getPreStatus() {
        return preStatus;
    }

    public void setPreStatus(String preStatus) {
        this.preStatus = preStatus;
    }

    public String getProductionDeploymentResult() {
        return productionDeploymentResult;
    }

    public void setProductionDeploymentResult(String productionDeploymentResult) {
        this.productionDeploymentResult = productionDeploymentResult;
    }

    public String getProAdvanceResult() {
        return proAdvanceResult;
    }

    public void setProAdvanceResult(String proAdvanceResult) {
        this.proAdvanceResult = proAdvanceResult;
    }

    public LocalDateTime getProPkgTime() {
        return proPkgTime;
    }

    public void setProPkgTime(LocalDateTime proPkgTime) {
        this.proPkgTime = proPkgTime;
    }

    public String getProPkgName() {
        return proPkgName;
    }

    public void setProPkgName(String proPkgName) {
        this.proPkgName = proPkgName;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDdlPkgName() {
        return ddlPkgName;
    }

    public void setDdlPkgName(String ddlPkgName) {
        this.ddlPkgName = ddlPkgName;
    }

    public LocalDateTime getDdlPkgTime() {
        return ddlPkgTime;
    }

    public void setDdlPkgTime(LocalDateTime ddlPkgTime) {
        this.ddlPkgTime = ddlPkgTime;
    }

    public String getIsDbaOperation() {
        return isDbaOperation;
    }

    public void setIsDbaOperation(String isDbaOperation) {
        this.isDbaOperation = isDbaOperation;
    }

    public String getIsDbaOperationComplete() {
        return isDbaOperationComplete;
    }

    public void setIsDbaOperationComplete(String isDbaOperationComplete) {
        this.isDbaOperationComplete = isDbaOperationComplete;
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
