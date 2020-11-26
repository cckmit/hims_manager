/*
 * @ClassName QuantitativeDataDO
 * @Description
 * @version 1.0
 * @Date 2020-11-11 14:28:53
 */
package com.cmpay.lemon.monitor.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;

@DataObject
public class QuantitativeDataDO extends BaseDO {
    /**
     * @Fields quantitativeId id
     */
    private Long quantitativeId;
    /**
     * @Fields firstlevelOrganization 一级主导团队
     */
    @Excel(name = "一级主导团队")
    private String firstlevelOrganization;
    /**
     * @Fields reqImplMon 月份
     */
    @Excel(name = "月份")
    private String reqImplMon;
    /**
     * @Fields functionPointsAssessWorkload 功能点评估工作量
     */
    @Excel(name = "功能点评估工作量")
    private String functionPointsAssessWorkload;
    /**
     * @Fields costCoefficientsSum 部门成本系数总和
     */
    @Excel(name = "部门成本系数总和")
    private String costCoefficientsSum;
    /**
     * @Fields inputOutputRatio 投入产出比
     */
    @Excel(name = "投入产出比")
    private String inputOutputRatio;
    /**
     * @Fields targetCompletionRate 目标完成率
     */
    @Excel(name = "目标完成率")
    private String targetCompletionRate;
    /**
     * @Fields productReleaseRate 产品发布率
     */
    @Excel(name = "产品发布率")
    private String productReleaseRate;
    /**
     * @Fields documentsOutputNumber 文档未及时输出数
     */
    @Excel(name = "文档未及时输出数")
    private Integer documentsOutputNumber;
    /**
     * @Fields projectsNotCompletedNumber 未及时完成项目启动数
     */
    @Excel(name = "未及时完成项目启动数")
    private Integer projectsNotCompletedNumber;
    /**
     * @Fields projectsDocumentsNotCompletedNumber 未及时提供项目文档数
     */
    @Excel(name = "未及时提供项目文档数")
    private Integer projectsDocumentsNotCompletedNumber;
    /**
     * @Fields workNotCompletedNumber 未及时完成工作量延期天数
     */
    @Excel(name = "未及时完成工作量延期天数")
    private Integer workNotCompletedNumber;
    /**
     * @Fields unstandardizedFeedbackWorksNumber 未规范反馈工作量条数
     */
    @Excel(name = "未规范反馈工作量条数")
    private Integer unstandardizedFeedbackWorksNumber;
    /**
     * @Fields notTimelyInputProductionNumber 未及时录入投产数
     */
    @Excel(name = "未及时录入投产数")
    private Integer notTimelyInputProductionNumber;
    /**
     * @Fields defectRate 缺陷率
     */
    @Excel(name = "缺陷率")
    private String defectRate;
    /**
     * @Fields dataChangeProblemsNumber 数据变更问题数
     */
    @Excel(name = "数据变更问题数")
    private Integer dataChangeProblemsNumber;
    /**
     * @Fields productionProblemsNumber 生产问题个数
     */
    @Excel(name = "生产问题个数")
    private Integer productionProblemsNumber;
    /**
     * @Fields smokeTestFailed 冒烟测试不通过数
     */
    @Excel(name = "冒烟测试不通过数")
    private Integer smokeTestFailed;
    /**
     * @Fields versionUpdateIssues 版本更新问题数
     */
    @Excel(name = "版本更新问题数")
    private Integer versionUpdateIssues;
    /**
     * @Fields baseAwardedMarks 基地加分
     */
    @Excel(name = "基地加分")
    private Integer baseAwardedMarks;
    /**
     * @Fields praiseAwardedMarks 通报表扬
     */
    @Excel(name = "通报表扬")
    private Integer praiseAwardedMarks;
    /**
     * @Fields qualityAwardedMarks 质量加分
     */
    @Excel(name = "质量加分")
    private Integer qualityAwardedMarks;
    /**
     * @Fields innovateAwardedMarks 创新加分
     */
    @Excel(name = "创新加分")
    private Integer innovateAwardedMarks;
    /**
     * @Fields baseDeductMarks 基地扣分
     */
    @Excel(name = "基地扣分")
    private Integer baseDeductMarks;
    /**
     * @Fields criticismDeductMarks 通报批评
     */
    @Excel(name = "通报批评")
    private Integer criticismDeductMarks;
    /**
     * @Fields fundLoss 资金损失
     */
    @Excel(name = "资金损失")
    private Integer fundLoss;
    /**
     * @Fields workingAttitude 工作态度及制度遵守
     */
    @Excel(name = "工作态度及制度遵守")
    private Integer workingAttitude;
    /**
     * @Fields remark 备注
     */
    @Excel(name = "备注")
    private String remark;
    /**
     * @Fields developWork 开发工作量调整
     */
    private Double developWork;
    /**
     * @Fields supportWork 支撑工作量调整
     */
    private Double supportWork;
    /**
     * @Fields developWorkSum 开发工作量汇总
     */
    private Double developWorkSum;
    /**
     * @Fields supportWorkSum 支撑工作量汇总
     */
    private Double supportWorkSum;
    /**
     * @Fields easyWorkSum 简易工作量汇总
     */
    private Double easyWorkSum;

    public Long getQuantitativeId() {
        return quantitativeId;
    }

    public void setQuantitativeId(Long quantitativeId) {
        this.quantitativeId = quantitativeId;
    }

    public String getFirstlevelOrganization() {
        return firstlevelOrganization;
    }

    public void setFirstlevelOrganization(String firstlevelOrganization) {
        this.firstlevelOrganization = firstlevelOrganization;
    }

    public String getReqImplMon() {
        return reqImplMon;
    }

    public void setReqImplMon(String reqImplMon) {
        this.reqImplMon = reqImplMon;
    }

    public String getFunctionPointsAssessWorkload() {
        return functionPointsAssessWorkload;
    }

    public void setFunctionPointsAssessWorkload(String functionPointsAssessWorkload) {
        this.functionPointsAssessWorkload = functionPointsAssessWorkload;
    }

    public String getCostCoefficientsSum() {
        return costCoefficientsSum;
    }

    public void setCostCoefficientsSum(String costCoefficientsSum) {
        this.costCoefficientsSum = costCoefficientsSum;
    }

    public String getInputOutputRatio() {
        return inputOutputRatio;
    }

    public void setInputOutputRatio(String inputOutputRatio) {
        this.inputOutputRatio = inputOutputRatio;
    }

    public String getTargetCompletionRate() {
        return targetCompletionRate;
    }

    public void setTargetCompletionRate(String targetCompletionRate) {
        this.targetCompletionRate = targetCompletionRate;
    }

    public String getProductReleaseRate() {
        return productReleaseRate;
    }

    public void setProductReleaseRate(String productReleaseRate) {
        this.productReleaseRate = productReleaseRate;
    }

    public Integer getDocumentsOutputNumber() {
        return documentsOutputNumber;
    }

    public void setDocumentsOutputNumber(Integer documentsOutputNumber) {
        this.documentsOutputNumber = documentsOutputNumber;
    }

    public Integer getProjectsNotCompletedNumber() {
        return projectsNotCompletedNumber;
    }

    public void setProjectsNotCompletedNumber(Integer projectsNotCompletedNumber) {
        this.projectsNotCompletedNumber = projectsNotCompletedNumber;
    }

    public Integer getProjectsDocumentsNotCompletedNumber() {
        return projectsDocumentsNotCompletedNumber;
    }

    public void setProjectsDocumentsNotCompletedNumber(Integer projectsDocumentsNotCompletedNumber) {
        this.projectsDocumentsNotCompletedNumber = projectsDocumentsNotCompletedNumber;
    }

    public Integer getWorkNotCompletedNumber() {
        return workNotCompletedNumber;
    }

    public void setWorkNotCompletedNumber(Integer workNotCompletedNumber) {
        this.workNotCompletedNumber = workNotCompletedNumber;
    }

    public Integer getUnstandardizedFeedbackWorksNumber() {
        return unstandardizedFeedbackWorksNumber;
    }

    public void setUnstandardizedFeedbackWorksNumber(Integer unstandardizedFeedbackWorksNumber) {
        this.unstandardizedFeedbackWorksNumber = unstandardizedFeedbackWorksNumber;
    }

    public Integer getNotTimelyInputProductionNumber() {
        return notTimelyInputProductionNumber;
    }

    public void setNotTimelyInputProductionNumber(Integer notTimelyInputProductionNumber) {
        this.notTimelyInputProductionNumber = notTimelyInputProductionNumber;
    }

    public String getDefectRate() {
        return defectRate;
    }

    public void setDefectRate(String defectRate) {
        this.defectRate = defectRate;
    }

    public Integer getDataChangeProblemsNumber() {
        return dataChangeProblemsNumber;
    }

    public void setDataChangeProblemsNumber(Integer dataChangeProblemsNumber) {
        this.dataChangeProblemsNumber = dataChangeProblemsNumber;
    }

    public Integer getProductionProblemsNumber() {
        return productionProblemsNumber;
    }

    public void setProductionProblemsNumber(Integer productionProblemsNumber) {
        this.productionProblemsNumber = productionProblemsNumber;
    }

    public Integer getSmokeTestFailed() {
        return smokeTestFailed;
    }

    public void setSmokeTestFailed(Integer smokeTestFailed) {
        this.smokeTestFailed = smokeTestFailed;
    }

    public Integer getVersionUpdateIssues() {
        return versionUpdateIssues;
    }

    public void setVersionUpdateIssues(Integer versionUpdateIssues) {
        this.versionUpdateIssues = versionUpdateIssues;
    }

    public Integer getBaseAwardedMarks() {
        return baseAwardedMarks;
    }

    public void setBaseAwardedMarks(Integer baseAwardedMarks) {
        this.baseAwardedMarks = baseAwardedMarks;
    }

    public Integer getPraiseAwardedMarks() {
        return praiseAwardedMarks;
    }

    public void setPraiseAwardedMarks(Integer praiseAwardedMarks) {
        this.praiseAwardedMarks = praiseAwardedMarks;
    }

    public Integer getQualityAwardedMarks() {
        return qualityAwardedMarks;
    }

    public void setQualityAwardedMarks(Integer qualityAwardedMarks) {
        this.qualityAwardedMarks = qualityAwardedMarks;
    }

    public Integer getInnovateAwardedMarks() {
        return innovateAwardedMarks;
    }

    public void setInnovateAwardedMarks(Integer innovateAwardedMarks) {
        this.innovateAwardedMarks = innovateAwardedMarks;
    }

    public Integer getBaseDeductMarks() {
        return baseDeductMarks;
    }

    public void setBaseDeductMarks(Integer baseDeductMarks) {
        this.baseDeductMarks = baseDeductMarks;
    }

    public Integer getCriticismDeductMarks() {
        return criticismDeductMarks;
    }

    public void setCriticismDeductMarks(Integer criticismDeductMarks) {
        this.criticismDeductMarks = criticismDeductMarks;
    }

    public Integer getFundLoss() {
        return fundLoss;
    }

    public void setFundLoss(Integer fundLoss) {
        this.fundLoss = fundLoss;
    }

    public Integer getWorkingAttitude() {
        return workingAttitude;
    }

    public void setWorkingAttitude(Integer workingAttitude) {
        this.workingAttitude = workingAttitude;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Double getDevelopWork() {
        return developWork;
    }

    public void setDevelopWork(Double developWork) {
        this.developWork = developWork;
    }

    public Double getSupportWork() {
        return supportWork;
    }

    public void setSupportWork(Double supportWork) {
        this.supportWork = supportWork;
    }

    public Double getDevelopWorkSum() {
        return developWorkSum;
    }

    public void setDevelopWorkSum(Double developWorkSum) {
        this.developWorkSum = developWorkSum;
    }

    public Double getSupportWorkSum() {
        return supportWorkSum;
    }

    public void setSupportWorkSum(Double supportWorkSum) {
        this.supportWorkSum = supportWorkSum;
    }

    public Double getEasyWorkSum() {
        return easyWorkSum;
    }

    public void setEasyWorkSum(Double easyWorkSum) {
        this.easyWorkSum = easyWorkSum;
    }

    @Override
    public String toString() {
        return "QuantitativeDataDO{" +
                "quantitativeId=" + quantitativeId +
                ", firstlevelOrganization='" + firstlevelOrganization + '\'' +
                ", reqImplMon='" + reqImplMon + '\'' +
                ", functionPointsAssessWorkload='" + functionPointsAssessWorkload + '\'' +
                ", costCoefficientsSum='" + costCoefficientsSum + '\'' +
                ", inputOutputRatio='" + inputOutputRatio + '\'' +
                ", targetCompletionRate='" + targetCompletionRate + '\'' +
                ", productReleaseRate='" + productReleaseRate + '\'' +
                ", documentsOutputNumber=" + documentsOutputNumber +
                ", projectsNotCompletedNumber=" + projectsNotCompletedNumber +
                ", projectsDocumentsNotCompletedNumber=" + projectsDocumentsNotCompletedNumber +
                ", workNotCompletedNumber=" + workNotCompletedNumber +
                ", unstandardizedFeedbackWorksNumber=" + unstandardizedFeedbackWorksNumber +
                ", notTimelyInputProductionNumber=" + notTimelyInputProductionNumber +
                ", defectRate='" + defectRate + '\'' +
                ", dataChangeProblemsNumber=" + dataChangeProblemsNumber +
                ", productionProblemsNumber=" + productionProblemsNumber +
                ", smokeTestFailed=" + smokeTestFailed +
                ", versionUpdateIssues=" + versionUpdateIssues +
                ", baseAwardedMarks=" + baseAwardedMarks +
                ", praiseAwardedMarks=" + praiseAwardedMarks +
                ", qualityAwardedMarks=" + qualityAwardedMarks +
                ", innovateAwardedMarks=" + innovateAwardedMarks +
                ", baseDeductMarks=" + baseDeductMarks +
                ", criticismDeductMarks=" + criticismDeductMarks +
                ", fundLoss=" + fundLoss +
                ", workingAttitude=" + workingAttitude +
                ", remark='" + remark + '\'' +
                ", developWork=" + developWork +
                ", supportWork=" + supportWork +
                ", developWorkSum=" + developWorkSum +
                ", supportWorkSum=" + supportWorkSum +
                ", easyWorkSum=" + easyWorkSum +
                '}';
    }
}
