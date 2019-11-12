package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.request.GenericDTO;
import com.cmpay.framework.data.response.PageableRspDTO;

import java.sql.Date;

/**
 * @author: zhou_xiong
 */

public class ScheduleReqDTO extends PageableRspDTO {
    private int seqId;
    private String proNumber;
    private String proOperator;
    private String operationType;
    private Date scheduleTime;
    private String preOperation;
    private String afterOperation;
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
    // 主导部门
    private String applicationDept;

    public String getApplicationDept() {
        return applicationDept;
    }

    public void setApplicationDept(String applicationDept) {
        this.applicationDept = applicationDept;
    }
    private String proNeed;

    public String getProNeed() {
        return proNeed;
    }

    public void setProNeed(String proNeed) {
        this.proNeed = proNeed;
    }
    /**
     * 页数
     */
    private int pageNum;
    /**
     * 页面大小
     */
    private int pageSize;

    public ScheduleReqDTO() {
    }

    @Override
    public String toString() {
        return "ScheduleBO{" +
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
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
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
