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
public class QuantitativeScoreDO extends BaseDO {
    /**
     * @Fields firstlevelOrganization 一级主导团队
     */
    @Excel(name = "一级主导团队")
    private String firstlevelOrganization;
    /**
     * @Fields 部门投入产出比得分
     */
    @Excel(name = "部门投入产出比得分")
    private Double inputOutputRatioMarks;
    /**
     * @Fields functionPointsAssessWorkload 需求目标完成率
     */
    @Excel(name = "需求目标完成率")
    private Double functionPointsAssessWorkload;
    /**
     * @Fields costCoefficientsSum 流程规范得分
     */
    @Excel(name = "流程规范得分")
    private Double costCoefficientsSum;
    /**
     * @Fields productReleaseRate 版本、生产问题得分
     */
    @Excel(name = "版本、生产问题得分")
    private Double productReleaseRate;

    /**
     * @Fields defectRateMarks 缺陷率(漏出率)得分
     */
    @Excel(name = "缺陷率(漏出率)得分")
    private Double defectRateMarks;
    /**
     * @Fields productionProblemsNumber 版本、生产问题得分
     */
    @Excel(name = "版本、生产问题得分")
    private Double versionProductionProblemsMarks;
    /**
     * @Fields baseAwardedMarks 基地加分
     */
    @Excel(name = "基地加分")
    private Double baseAwardedMarks;
    /**
     * @Fields praiseAwardedMarks 通报表扬
     */
    @Excel(name = "通报表扬")
    private Double praiseAwardedMarks;
    /**
     * @Fields qualityAwardedMarks 质量加分
     */
    @Excel(name = "质量加分")
    private Double qualityAwardedMarks;
    /**
     * @Fields innovateAwardedMarks 创新加分
     */
    @Excel(name = "创新加分")
    private Double innovateAwardedMarks;
    /**
     * @Fields baseDeductMarks 基地扣分
     */
    @Excel(name = "基地扣分")
    private Double baseDeductMarks;
    /**
     * @Fields criticismDeductMarks 通报批评
     */
    @Excel(name = "通报批评")
    private Double criticismDeductMarks;
    /**
     * @Fields fundLoss 资金损失
     */
    @Excel(name = "资金损失")
    private Double fundLoss;
    /**
     * @Fields workingAttitude 工作态度及制度遵守
     */
    @Excel(name = "工作态度及制度遵守")
    private Double workingAttitude;
    /**
     * @Fields departmentMarks 部门得分
     */
    @Excel(name = "部门得分")
    private Double departmentMarks;
    /**
     * @Fields departmentAverageMarks 部门人均得分
     */
    @Excel(name = "部门人均得分")
    private Double departmentAverageMarks;
    /**
     * @Fields departmentManagerMarks 部门经理得分
     */
    @Excel(name = "部门经理得分")
    private Double departmentManagerMarks;

    public String getFirstlevelOrganization() {
        return firstlevelOrganization;
    }

    public void setFirstlevelOrganization(String firstlevelOrganization) {
        this.firstlevelOrganization = firstlevelOrganization;
    }

    public Double getInputOutputRatioMarks() {
        return inputOutputRatioMarks;
    }

    public void setInputOutputRatioMarks(Double inputOutputRatioMarks) {
        this.inputOutputRatioMarks = inputOutputRatioMarks;
    }

    public Double getFunctionPointsAssessWorkload() {
        return functionPointsAssessWorkload;
    }

    public void setFunctionPointsAssessWorkload(Double functionPointsAssessWorkload) {
        this.functionPointsAssessWorkload = functionPointsAssessWorkload;
    }

    public Double getCostCoefficientsSum() {
        return costCoefficientsSum;
    }

    public void setCostCoefficientsSum(Double costCoefficientsSum) {
        this.costCoefficientsSum = costCoefficientsSum;
    }

    public Double getProductReleaseRate() {
        return productReleaseRate;
    }

    public void setProductReleaseRate(Double productReleaseRate) {
        this.productReleaseRate = productReleaseRate;
    }

    public Double getDefectRateMarks() {
        return defectRateMarks;
    }

    public void setDefectRateMarks(Double defectRateMarks) {
        this.defectRateMarks = defectRateMarks;
    }

    public Double getVersionProductionProblemsMarks() {
        return versionProductionProblemsMarks;
    }

    public void setVersionProductionProblemsMarks(Double versionProductionProblemsMarks) {
        this.versionProductionProblemsMarks = versionProductionProblemsMarks;
    }

    public Double getBaseAwardedMarks() {
        return baseAwardedMarks;
    }

    public void setBaseAwardedMarks(Double baseAwardedMarks) {
        this.baseAwardedMarks = baseAwardedMarks;
    }

    public Double getPraiseAwardedMarks() {
        return praiseAwardedMarks;
    }

    public void setPraiseAwardedMarks(Double praiseAwardedMarks) {
        this.praiseAwardedMarks = praiseAwardedMarks;
    }

    public Double getQualityAwardedMarks() {
        return qualityAwardedMarks;
    }

    public void setQualityAwardedMarks(Double qualityAwardedMarks) {
        this.qualityAwardedMarks = qualityAwardedMarks;
    }

    public Double getInnovateAwardedMarks() {
        return innovateAwardedMarks;
    }

    public void setInnovateAwardedMarks(Double innovateAwardedMarks) {
        this.innovateAwardedMarks = innovateAwardedMarks;
    }

    public Double getBaseDeductMarks() {
        return baseDeductMarks;
    }

    public void setBaseDeductMarks(Double baseDeductMarks) {
        this.baseDeductMarks = baseDeductMarks;
    }

    public Double getCriticismDeductMarks() {
        return criticismDeductMarks;
    }

    public void setCriticismDeductMarks(Double criticismDeductMarks) {
        this.criticismDeductMarks = criticismDeductMarks;
    }

    public Double getFundLoss() {
        return fundLoss;
    }

    public void setFundLoss(Double fundLoss) {
        this.fundLoss = fundLoss;
    }

    public Double getWorkingAttitude() {
        return workingAttitude;
    }

    public void setWorkingAttitude(Double workingAttitude) {
        this.workingAttitude = workingAttitude;
    }

    public Double getDepartmentMarks() {
        return departmentMarks;
    }

    public void setDepartmentMarks(Double departmentMarks) {
        this.departmentMarks = departmentMarks;
    }

    public Double getDepartmentAverageMarks() {
        return departmentAverageMarks;
    }

    public void setDepartmentAverageMarks(Double departmentAverageMarks) {
        this.departmentAverageMarks = departmentAverageMarks;
    }

    public Double getDepartmentManagerMarks() {
        return departmentManagerMarks;
    }

    public void setDepartmentManagerMarks(Double departmentManagerMarks) {
        this.departmentManagerMarks = departmentManagerMarks;
    }

    @Override
    public String toString() {
        return "QuantitativeScoreDO{" +
                "firstlevelOrganization='" + firstlevelOrganization + '\'' +
                ", inputOutputRatioMarks=" + inputOutputRatioMarks +
                ", functionPointsAssessWorkload=" + functionPointsAssessWorkload +
                ", costCoefficientsSum=" + costCoefficientsSum +
                ", productReleaseRate=" + productReleaseRate +
                ", defectRateMarks=" + defectRateMarks +
                ", versionProductionProblemsMarks=" + versionProductionProblemsMarks +
                ", baseAwardedMarks=" + baseAwardedMarks +
                ", praiseAwardedMarks=" + praiseAwardedMarks +
                ", qualityAwardedMarks=" + qualityAwardedMarks +
                ", innovateAwardedMarks=" + innovateAwardedMarks +
                ", baseDeductMarks=" + baseDeductMarks +
                ", criticismDeductMarks=" + criticismDeductMarks +
                ", fundLoss=" + fundLoss +
                ", workingAttitude=" + workingAttitude +
                ", departmentMarks=" + departmentMarks +
                ", departmentAverageMarks=" + departmentAverageMarks +
                ", departmentManagerMarks=" + departmentManagerMarks +
                '}';
    }
}
