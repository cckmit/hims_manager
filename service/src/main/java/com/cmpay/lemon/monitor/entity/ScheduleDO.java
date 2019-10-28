package com.cmpay.lemon.monitor.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cmpay.lemon.framework.annotation.DataObject;
import java.io.Serializable;
import java.sql.Date;
/**
 * 投产状态变更记录
 */
@DataObject
public class ScheduleDO extends AbstractDO {
    @Excel(name = "序号")
    private int seqId;
    @Excel(name = "投产编号")
    private String proNumber;
    @Excel(name = "操作人")
    private String proOperator;
    private String operationType;
    @Excel(name = "操作时间")
    private Date scheduleTime;
    @Excel(name = "操作前投产状态")
    private String preOperation;
    @Excel(name = "操作后投产状态")
    private String afterOperation;
    @Excel(name = "操作类型变更原因")
    private String operationReason;
    private String proType;
    private String isOperationProduction;
    private String operRequestContent;
    private String proposeDate;
    private String isRefSql;
    private String sysOperType;
    private String operStatus;
    private String applicationSector;
    private String operApplicant;
    private String identifier;
    private String developmentLeader;
    private String svntabName ;
    private String analysis;
    private String operApplicationReason;
    private String notProductionImpact;
    private String urgentReasonPhrase;

    public ScheduleDO(int seqId, String proNumber, String proOperator, String operationType, Date scheduleTime, String preOperation, String afterOperation, String operationReason, String proType, String isOperationProduction, String operRequestContent, String proposeDate, String isRefSql, String sysOperType, String operStatus, String applicationSector, String operApplicant, String identifier, String developmentLeader, String svntabName, String analysis, String operApplicationReason, String notProductionImpact, String urgentReasonPhrase) {
        this.seqId = seqId;
        this.proNumber = proNumber;
        this.proOperator = proOperator;
        this.operationType = operationType;
        this.scheduleTime = scheduleTime;
        this.preOperation = preOperation;
        this.afterOperation = afterOperation;
        this.operationReason = operationReason;
        this.proType = proType;
        this.isOperationProduction = isOperationProduction;
        this.operRequestContent = operRequestContent;
        this.proposeDate = proposeDate;
        this.isRefSql = isRefSql;
        this.sysOperType = sysOperType;
        this.operStatus = operStatus;
        this.applicationSector = applicationSector;
        this.operApplicant = operApplicant;
        this.identifier = identifier;
        this.developmentLeader = developmentLeader;
        this.svntabName = svntabName;
        this.analysis = analysis;
        this.operApplicationReason = operApplicationReason;
        this.notProductionImpact = notProductionImpact;
        this.urgentReasonPhrase = urgentReasonPhrase;
    }

    public ScheduleDO() {
    }
    public ScheduleDO(String proOperator) {
        this.proOperator = proOperator;
    }


    public ScheduleDO( String proNumber, String proOperator,
                         String operationType,String preOperation,
                         String afterOperation, String operationReason) {
        this.proNumber = proNumber;
        this.proOperator = proOperator;
        this.operationType = operationType;
        this.preOperation = preOperation;
        this.afterOperation = afterOperation;
        this.operationReason = operationReason;
    }



    public ScheduleDO(int seqId, String proNumber, String proOperator,
                        String operationType, Date scheduleTime, String preOperation,
                        String afterOperation, String operationReason) {
        this.seqId = seqId;
        this.proNumber = proNumber;
        this.proOperator = proOperator;
        this.operationType = operationType;
        this.scheduleTime = scheduleTime;
        this.preOperation = preOperation;
        this.afterOperation = afterOperation;
        this.operationReason = operationReason;

    }



    public ScheduleDO(String proNumber, String proOperator,
                        String operationType, Date scheduleTime, String preOperation,
                        String afterOperation, String operationReason) {
        this.proNumber = proNumber;
        this.proOperator = proOperator;
        this.operationType = operationType;
        this.scheduleTime = scheduleTime;
        this.preOperation = preOperation;
        this.afterOperation = afterOperation;
        this.operationReason = operationReason;
    }

    @Override
    public Serializable getId() {
        return null;
    }

    public int getSeqId() {
        return seqId;
    }

    public void setSeqId(int seqId) {
        this.seqId = seqId;
    }

    public String getProNumber() {
        return proNumber;
    }

    public void setProNumber(String proNumber) {
        this.proNumber = proNumber;
    }

    public String getProOperator() {
        return proOperator;
    }

    public void setProOperator(String proOperator) {
        this.proOperator = proOperator;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public Date getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(Date scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public String getPreOperation() {
        return preOperation;
    }

    public void setPreOperation(String preOperation) {
        this.preOperation = preOperation;
    }

    public String getAfterOperation() {
        return afterOperation;
    }

    public void setAfterOperation(String afterOperation) {
        this.afterOperation = afterOperation;
    }

    public String getOperationReason() {
        return operationReason;
    }

    public void setOperationReason(String operationReason) {
        this.operationReason = operationReason;
    }

    public String getProType() {
        return proType;
    }

    public void setProType(String proType) {
        this.proType = proType;
    }

    public String getIsOperationProduction() {
        return isOperationProduction;
    }

    public void setIsOperationProduction(String isOperationProduction) {
        this.isOperationProduction = isOperationProduction;
    }

    public String getOperRequestContent() {
        return operRequestContent;
    }

    public void setOperRequestContent(String operRequestContent) {
        this.operRequestContent = operRequestContent;
    }

    public String getProposeDate() {
        return proposeDate;
    }

    public void setProposeDate(String proposeDate) {
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

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getDevelopmentLeader() {
        return developmentLeader;
    }

    public void setDevelopmentLeader(String developmentLeader) {
        this.developmentLeader = developmentLeader;
    }

    public String getSvntabName() {
        return svntabName;
    }

    public void setSvntabName(String svntabName) {
        this.svntabName = svntabName;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public String getOperApplicationReason() {
        return operApplicationReason;
    }

    public void setOperApplicationReason(String operApplicationReason) {
        this.operApplicationReason = operApplicationReason;
    }

    public String getNotProductionImpact() {
        return notProductionImpact;
    }

    public void setNotProductionImpact(String notProductionImpact) {
        this.notProductionImpact = notProductionImpact;
    }

    public String getUrgentReasonPhrase() {
        return urgentReasonPhrase;
    }

    public void setUrgentReasonPhrase(String urgentReasonPhrase) {
        this.urgentReasonPhrase = urgentReasonPhrase;
    }

    @Override
    public String toString() {
        return "ScheduleDO{" +
                "seqId=" + seqId +
                ", proNumber='" + proNumber + '\'' +
                ", proOperator='" + proOperator + '\'' +
                ", operationType='" + operationType + '\'' +
                ", scheduleTime=" + scheduleTime +
                ", preOperation='" + preOperation + '\'' +
                ", afterOperation='" + afterOperation + '\'' +
                ", operationReason='" + operationReason + '\'' +
                ", proType='" + proType + '\'' +
                ", isOperationProduction='" + isOperationProduction + '\'' +
                ", operRequestContent='" + operRequestContent + '\'' +
                ", proposeDate='" + proposeDate + '\'' +
                ", isRefSql='" + isRefSql + '\'' +
                ", sysOperType='" + sysOperType + '\'' +
                ", operStatus='" + operStatus + '\'' +
                ", applicationSector='" + applicationSector + '\'' +
                ", operApplicant='" + operApplicant + '\'' +
                ", identifier='" + identifier + '\'' +
                ", developmentLeader='" + developmentLeader + '\'' +
                ", svntabName='" + svntabName + '\'' +
                ", analysis='" + analysis + '\'' +
                ", operApplicationReason='" + operApplicationReason + '\'' +
                ", notProductionImpact='" + notProductionImpact + '\'' +
                ", urgentReasonPhrase='" + urgentReasonPhrase + '\'' +
                '}';
    }
}