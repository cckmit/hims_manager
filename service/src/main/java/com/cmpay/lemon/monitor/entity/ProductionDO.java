/*
 * @ClassName CenterDO
 * @Description
 * @version 1.0
 * @Date 2019-07-25 11:01:18
 */
package com.cmpay.lemon.monitor.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;

import java.sql.Timestamp;
import java.sql.Date;

@DataObject
public class ProductionDO extends BaseDO {
    @Excel(name = "投产编号")
    private String proNumber;
    @Excel(name = "需求名称及内容简述")
    private String proNeed;
    @Excel(name = "投产类型")
    private String proType;
    private Date proDateStart;
    private Date proDateEnd;
    @Excel(name = "计划投产日期")
    private Date proDate;
    @Excel(name = "申请部门")
    private String applicationDept;
    @Excel(name = "投产申请人")
    private String proApplicant;
    @Excel(name = "申请人联系方式")
    private String applicantTel;
    @Excel(name = "产品所属模块")
    private String proModule;
    @Excel(name = "业务需求提出人")
    private String businessPrincipal;
    @Excel(name = "基地负责人")
    private String basePrincipal;
    @Excel(name = "产品经理")
    private String proManager;
    @Excel(name = "需求状态")
    private String proStatus;
    @Excel(name = "是否更新数据库数据")
    private String isUpDatabase;
    @Excel(name = "是否更新数据库（表）结构（包含DDL语句）")
    private String isUpStructure;
    @Excel(name = "投产后是否需要运维监控")
    private String proOperation;
    @Excel(name = "是否涉及证书")
    private String isRefCerificate;
    @Excel(name = "是否预投产验证")
    private String isAdvanceProduction;
    @Excel(name = "不能预投产验证原因")
    private String notAdvanceReason;
    @Excel(name = "预投产验证结果")
    private String proAdvanceResult;
    @Excel(name = "验证人")
    private String identifier;
    @Excel(name = "验证人联系方式")
    private String identifierTel;
    @Excel(name = "验证复核人")
    private String proChecker;
    @Excel(name = "验证复核人联系方式")
    private String checkerTel;
    @Excel(name = "生产验证方式")
    private String validation;
    @Excel(name = "开发负责人")
    private String developmentLeader;
    @Excel(name = "审批人")
    private String approver;
    @Excel(name = "版本更新操作人")
    private String updateOperator;
    @Excel(name = "备注")
    private String remark;
    @Excel(name = "不能走正常投产原因")
    private String unusualReasonPhrase;
    @Excel(name = "当天不投产的影响")
    private String notProductionImpact;
    private String urgentReasonPhrase;
    private String productionDeploymentResult;
    private String isOperationProduction;
    private String mailLeader;//开发负责人邮箱
    private String svntabName;//SVN表名称
    /*
     * 紧急更新
     */
    private String completionUpdate ;
    private String earlyImplementation;
    private String influenceUse ;
    private String influenceUseReason;
    private String influenceUseInf;
    private String operatingTime;
    /*
     * 审核邮件
     */
    private String mailRecipient;
    private String mailCopyPerson;
    /*
     * 部门关系
     */
    private String deptName;
    private String deptManagerName;
    private String developmentDept;
    /**
     * 投产包
     */
    private String proPkgStatus;
    private Timestamp proPkgTime;
    private String proPkgName;
    /**
     * 是否有回退方案
     */
    private String isFallback;
    private String proDate2;
    /**
     * 是否是考核问题
     */
    @Excel(name = "是否是考核问题")
    private String isAccessQuestion;
    /**
     * 考核问题备注
     */
    @Excel(name = "考核问题备注")
    private String accessRemark;

    public String getIsAccessQuestion() {
        return isAccessQuestion;
    }

    public void setIsAccessQuestion(String isAccessQuestion) {
        this.isAccessQuestion = isAccessQuestion;
    }

    public String getAccessRemark() {
        return accessRemark;
    }

    public void setAccessRemark(String accessRemark) {
        this.accessRemark = accessRemark;
    }

    public String getProDate2() {
        return proDate2;
    }

    public void setProDate2(String proDate2) {
        this.proDate2 = proDate2;
    }

    public ProductionDO() {
    }

    public ProductionDO(String proNumber, String proStatus) {
        this.proNumber = proNumber;
        this.proStatus = proStatus;
    }

    public String getProNumber() {
        return proNumber;
    }

    public void setProNumber(String proNumber) {
        this.proNumber = proNumber;
    }

    public String getProNeed() {
        return proNeed;
    }

    public void setProNeed(String proNeed) {
        this.proNeed = proNeed;
    }

    public String getProType() {
        return proType;
    }

    public void setProType(String proType) {
        this.proType = proType;
    }

    public Date getProDateStart() {
        return proDateStart;
    }

    public void setProDateStart(Date proDateStart) {
        this.proDateStart = proDateStart;
    }

    public Date getProDateEnd() {
        return proDateEnd;
    }

    public void setProDateEnd(Date proDateEnd) {
        this.proDateEnd = proDateEnd;
    }

    public Date getProDate() {
        return proDate;
    }

    public void setProDate(Date proDate) {
        this.proDate = proDate;
    }

    public String getApplicationDept() {
        return applicationDept;
    }

    public void setApplicationDept(String applicationDept) {
        this.applicationDept = applicationDept;
    }

    public String getProApplicant() {
        return proApplicant;
    }

    public void setProApplicant(String proApplicant) {
        this.proApplicant = proApplicant;
    }

    public String getApplicantTel() {
        return applicantTel;
    }

    public void setApplicantTel(String applicantTel) {
        this.applicantTel = applicantTel;
    }

    public String getProModule() {
        return proModule;
    }

    public void setProModule(String proModule) {
        this.proModule = proModule;
    }

    public String getBusinessPrincipal() {
        return businessPrincipal;
    }

    public void setBusinessPrincipal(String businessPrincipal) {
        this.businessPrincipal = businessPrincipal;
    }

    public String getBasePrincipal() {
        return basePrincipal;
    }

    public void setBasePrincipal(String basePrincipal) {
        this.basePrincipal = basePrincipal;
    }

    public String getProManager() {
        return proManager;
    }

    public void setProManager(String proManager) {
        this.proManager = proManager;
    }

    public String getProStatus() {
        return proStatus;
    }

    public void setProStatus(String proStatus) {
        this.proStatus = proStatus;
    }

    public String getIsUpDatabase() {
        return isUpDatabase;
    }

    public void setIsUpDatabase(String isUpDatabase) {
        this.isUpDatabase = isUpDatabase;
    }

    public String getIsUpStructure() {
        return isUpStructure;
    }

    public void setIsUpStructure(String isUpStructure) {
        this.isUpStructure = isUpStructure;
    }

    public String getProOperation() {
        return proOperation;
    }

    public void setProOperation(String proOperation) {
        this.proOperation = proOperation;
    }

    public String getIsRefCerificate() {
        return isRefCerificate;
    }

    public void setIsRefCerificate(String isRefCerificate) {
        this.isRefCerificate = isRefCerificate;
    }

    public String getIsAdvanceProduction() {
        return isAdvanceProduction;
    }

    public void setIsAdvanceProduction(String isAdvanceProduction) {
        this.isAdvanceProduction = isAdvanceProduction;
    }

    public String getNotAdvanceReason() {
        return notAdvanceReason;
    }

    public void setNotAdvanceReason(String notAdvanceReason) {
        this.notAdvanceReason = notAdvanceReason;
    }

    public String getProAdvanceResult() {
        return proAdvanceResult;
    }

    public void setProAdvanceResult(String proAdvanceResult) {
        this.proAdvanceResult = proAdvanceResult;
    }

    public String getNotProductionImpact() {
        return notProductionImpact;
    }

    public void setNotProductionImpact(String notProductionImpact) {
        this.notProductionImpact = notProductionImpact;
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

    public String getValidation() {
        return validation;
    }

    public void setValidation(String validation) {
        this.validation = validation;
    }

    public String getDevelopmentLeader() {
        return developmentLeader;
    }

    public void setDevelopmentLeader(String developmentLeader) {
        this.developmentLeader = developmentLeader;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getUpdateOperator() {
        return updateOperator;
    }

    public void setUpdateOperator(String updateOperator) {
        this.updateOperator = updateOperator;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUnusualReasonPhrase() {
        return unusualReasonPhrase;
    }

    public void setUnusualReasonPhrase(String unusualReasonPhrase) {
        this.unusualReasonPhrase = unusualReasonPhrase;
    }

    public String getUrgentReasonPhrase() {
        return urgentReasonPhrase;
    }

    public void setUrgentReasonPhrase(String urgentReasonPhrase) {
        this.urgentReasonPhrase = urgentReasonPhrase;
    }

    public String getProductionDeploymentResult() {
        return productionDeploymentResult;
    }

    public void setProductionDeploymentResult(String productionDeploymentResult) {
        this.productionDeploymentResult = productionDeploymentResult;
    }

    public String getIsOperationProduction() {
        return isOperationProduction;
    }

    public void setIsOperationProduction(String isOperationProduction) {
        this.isOperationProduction = isOperationProduction;
    }

    public String getMailLeader() {
        return mailLeader;
    }

    public void setMailLeader(String mailLeader) {
        this.mailLeader = mailLeader;
    }

    public String getSvntabName() {
        return svntabName;
    }

    public void setSvntabName(String svntabName) {
        this.svntabName = svntabName;
    }

    public String getCompletionUpdate() {
        return completionUpdate;
    }

    public void setCompletionUpdate(String completionUpdate) {
        this.completionUpdate = completionUpdate;
    }

    public String getEarlyImplementation() {
        return earlyImplementation;
    }

    public void setEarlyImplementation(String earlyImplementation) {
        this.earlyImplementation = earlyImplementation;
    }

    public String getInfluenceUse() {
        return influenceUse;
    }

    public void setInfluenceUse(String influenceUse) {
        this.influenceUse = influenceUse;
    }

    public String getInfluenceUseReason() {
        return influenceUseReason;
    }

    public void setInfluenceUseReason(String influenceUseReason) {
        this.influenceUseReason = influenceUseReason;
    }

    public String getInfluenceUseInf() {
        return influenceUseInf;
    }

    public void setInfluenceUseInf(String influenceUseInf) {
        this.influenceUseInf = influenceUseInf;
    }

    public String getOperatingTime() {
        return operatingTime;
    }

    public void setOperatingTime(String operatingTime) {
        this.operatingTime = operatingTime;
    }

    public String getMailRecipient() {
        return mailRecipient;
    }

    public void setMailRecipient(String mailRecipient) {
        this.mailRecipient = mailRecipient;
    }

    public String getMailCopyPerson() {
        return mailCopyPerson;
    }

    public void setMailCopyPerson(String mailCopyPerson) {
        this.mailCopyPerson = mailCopyPerson;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptManagerName() {
        return deptManagerName;
    }

    public void setDeptManagerName(String deptManagerName) {
        this.deptManagerName = deptManagerName;
    }

    public String getDevelopmentDept() {
        return developmentDept;
    }

    public void setDevelopmentDept(String developmentDept) {
        this.developmentDept = developmentDept;
    }

    public String getProPkgStatus() {
        return proPkgStatus;
    }

    public void setProPkgStatus(String proPkgStatus) {
        this.proPkgStatus = proPkgStatus;
    }

    public Timestamp getProPkgTime() {
        return proPkgTime;
    }

    public void setProPkgTime(Timestamp proPkgTime) {
        this.proPkgTime = proPkgTime;
    }

    public String getProPkgName() {
        return proPkgName;
    }

    public void setProPkgName(String proPkgName) {
        this.proPkgName = proPkgName;
    }

    public String getIsFallback() {
        return isFallback;
    }

    public void setIsFallback(String isFallback) {
        this.isFallback = isFallback;
    }

    @Override
    public String toString() {
        return "ProductionDO{" +
                "proNumber='" + proNumber + '\'' +
                ", proNeed='" + proNeed + '\'' +
                ", proType='" + proType + '\'' +
                ", proDateStart=" + proDateStart +
                ", proDateEnd=" + proDateEnd +
                ", proDate=" + proDate +
                ", applicationDept='" + applicationDept + '\'' +
                ", proApplicant='" + proApplicant + '\'' +
                ", applicantTel='" + applicantTel + '\'' +
                ", proModule='" + proModule + '\'' +
                ", businessPrincipal='" + businessPrincipal + '\'' +
                ", basePrincipal='" + basePrincipal + '\'' +
                ", proManager='" + proManager + '\'' +
                ", proStatus='" + proStatus + '\'' +
                ", isUpDatabase='" + isUpDatabase + '\'' +
                ", isUpStructure='" + isUpStructure + '\'' +
                ", proOperation='" + proOperation + '\'' +
                ", isRefCerificate='" + isRefCerificate + '\'' +
                ", isAdvanceProduction='" + isAdvanceProduction + '\'' +
                ", notAdvanceReason='" + notAdvanceReason + '\'' +
                ", proAdvanceResult='" + proAdvanceResult + '\'' +
                ", identifier='" + identifier + '\'' +
                ", identifierTel='" + identifierTel + '\'' +
                ", proChecker='" + proChecker + '\'' +
                ", checkerTel='" + checkerTel + '\'' +
                ", validation='" + validation + '\'' +
                ", developmentLeader='" + developmentLeader + '\'' +
                ", approver='" + approver + '\'' +
                ", updateOperator='" + updateOperator + '\'' +
                ", remark='" + remark + '\'' +
                ", unusualReasonPhrase='" + unusualReasonPhrase + '\'' +
                ", notProductionImpact='" + notProductionImpact + '\'' +
                ", urgentReasonPhrase='" + urgentReasonPhrase + '\'' +
                ", productionDeploymentResult='" + productionDeploymentResult + '\'' +
                ", isOperationProduction='" + isOperationProduction + '\'' +
                ", mailLeader='" + mailLeader + '\'' +
                ", svntabName='" + svntabName + '\'' +
                ", completionUpdate='" + completionUpdate + '\'' +
                ", earlyImplementation='" + earlyImplementation + '\'' +
                ", influenceUse='" + influenceUse + '\'' +
                ", influenceUseReason='" + influenceUseReason + '\'' +
                ", influenceUseInf='" + influenceUseInf + '\'' +
                ", operatingTime='" + operatingTime + '\'' +
                ", mailRecipient='" + mailRecipient + '\'' +
                ", mailCopyPerson='" + mailCopyPerson + '\'' +
                ", deptName='" + deptName + '\'' +
                ", deptManagerName='" + deptManagerName + '\'' +
                ", developmentDept='" + developmentDept + '\'' +
                ", proPkgStatus='" + proPkgStatus + '\'' +
                ", proPkgTime=" + proPkgTime +
                ", proPkgName='" + proPkgName + '\'' +
                ", isFallback='" + isFallback + '\'' +
                ", proDate2='" + proDate2 + '\'' +
                ", isAccessQuestion='" + isAccessQuestion + '\'' +
                ", accessRemark='" + accessRemark + '\'' +
                '}';
    }
}
