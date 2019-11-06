package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.request.GenericDTO;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * @author: zhou_xiong
 */
public class OperationApplicationDTO extends GenericDTO {
    private String operNumber;
    private String operRequestContent;
    private Date proposeDate;
    private String isRefSql;
    private String sysOperType;
    private String operStatus;
    private String applicationSector;
    private String operApplicant;
    private String applicantTel;
    private String operType;
    private String identifier;
    private String identifierTel;
    private String validationType;
    private String validationInstruction;
    private String operApplicationReason;
    private String analysis;
    private String completionUpdate;
    private String remark;
    private String developmentLeader;
    /*
     * 审核邮件
     */
    private String mailRecipient;
    private String mailCopyPerson;
    private String mailLeader;//开发负责人邮箱
    private String svntabName;//SVN表名称

    /**
     * 是否有回退方案
     */
    private String isBackWay;

    private int stateId;

    private Date poDateStart;
    private Date poDateEnd;

    /**
     * 页数
     */
    private int pageNum;
    /**
     * 页面大小
     */
    private int pageSize;

    public String getOperNumber() {
        return operNumber;
    }

    public void setOperNumber(String operNumber) {
        this.operNumber = operNumber;
    }

    public String getOperRequestContent() {
        return operRequestContent;
    }

    public void setOperRequestContent(String operRequestContent) {
        this.operRequestContent = operRequestContent;
    }

    public Date getProposeDate() {
        return proposeDate;
    }

    public void setProposeDate(Date proposeDate) {
        this.proposeDate = proposeDate;
    }

    public String getIsRefSql() {
        return isRefSql;
    }

    public void setIsRefSql(String isRefSql) {
        this.isRefSql = isRefSql;
    }

    public String getSysOperType() {
        return sysOperType;
    }

    public void setSysOperType(String sysOperType) {
        this.sysOperType = sysOperType;
    }

    public String getOperStatus() {
        return operStatus;
    }

    public void setOperStatus(String operStatus) {
        this.operStatus = operStatus;
    }

    public String getApplicationSector() {
        return applicationSector;
    }

    public void setApplicationSector(String applicationSector) {
        this.applicationSector = applicationSector;
    }

    public String getOperApplicant() {
        return operApplicant;
    }

    public void setOperApplicant(String operApplicant) {
        this.operApplicant = operApplicant;
    }

    public String getApplicantTel() {
        return applicantTel;
    }

    public void setApplicantTel(String applicantTel) {
        this.applicantTel = applicantTel;
    }

    public String getOperType() {
        return operType;
    }

    public void setOperType(String operType) {
        this.operType = operType;
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

    public String getValidationType() {
        return validationType;
    }

    public void setValidationType(String validationType) {
        this.validationType = validationType;
    }

    public String getValidationInstruction() {
        return validationInstruction;
    }

    public void setValidationInstruction(String validationInstruction) {
        this.validationInstruction = validationInstruction;
    }

    public String getOperApplicationReason() {
        return operApplicationReason;
    }

    public void setOperApplicationReason(String operApplicationReason) {
        this.operApplicationReason = operApplicationReason;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public String getCompletionUpdate() {
        return completionUpdate;
    }

    public void setCompletionUpdate(String completionUpdate) {
        this.completionUpdate = completionUpdate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDevelopmentLeader() {
        return developmentLeader;
    }

    public void setDevelopmentLeader(String developmentLeader) {
        this.developmentLeader = developmentLeader;
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

    public String getIsBackWay() {
        return isBackWay;
    }

    public void setIsBackWay(String isBackWay) {
        this.isBackWay = isBackWay;
    }

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    public Date getPoDateStart() {
        return poDateStart;
    }

    public void setPoDateStart(Date poDateStart) {
        this.poDateStart = poDateStart;
    }

    public Date getPoDateEnd() {
        return poDateEnd;
    }

    public void setPoDateEnd(Date poDateEnd) {
        this.poDateEnd = poDateEnd;
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
        return "OperationApplicationDTO{" +
                "operNumber='" + operNumber + '\'' +
                ", operRequestContent='" + operRequestContent + '\'' +
                ", proposeDate=" + proposeDate +
                ", isRefSql='" + isRefSql + '\'' +
                ", sysOperType='" + sysOperType + '\'' +
                ", operStatus='" + operStatus + '\'' +
                ", applicationSector='" + applicationSector + '\'' +
                ", operApplicant='" + operApplicant + '\'' +
                ", applicantTel='" + applicantTel + '\'' +
                ", operType='" + operType + '\'' +
                ", identifier='" + identifier + '\'' +
                ", identifierTel='" + identifierTel + '\'' +
                ", validationType='" + validationType + '\'' +
                ", validationInstruction='" + validationInstruction + '\'' +
                ", operApplicationReason='" + operApplicationReason + '\'' +
                ", analysis='" + analysis + '\'' +
                ", completionUpdate='" + completionUpdate + '\'' +
                ", remark='" + remark + '\'' +
                ", developmentLeader='" + developmentLeader + '\'' +
                ", mailRecipient='" + mailRecipient + '\'' +
                ", mailCopyPerson='" + mailCopyPerson + '\'' +
                ", mailLeader='" + mailLeader + '\'' +
                ", svntabName='" + svntabName + '\'' +
                ", isBackWay='" + isBackWay + '\'' +
                ", stateId=" + stateId +
                ", poDateStart=" + poDateStart +
                ", poDateEnd=" + poDateEnd +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }
}
