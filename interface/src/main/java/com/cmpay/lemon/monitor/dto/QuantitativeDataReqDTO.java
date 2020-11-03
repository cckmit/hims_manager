/*
 * @ClassName QuantitativeDataDO
 * @Description
 * @version 1.0
 * @Date 2020-11-02 16:52:35
 */
package com.cmpay.lemon.monitor.dto;


import com.cmpay.framework.data.request.GenericDTO;
import com.cmpay.framework.data.response.PageableRspDTO;

public class QuantitativeDataReqDTO extends PageableRspDTO {
    /**
     *  quantitativeId id
     */
    private Long quantitativeId;
    /**
     *  firstlevelOrganization 一级主导团队
     */
    private String firstlevelOrganization;
    /**
     *  reqImplMon 月份
     */
    private String reqImplMon;
    /**
     *  functionPointsAssessWorkload 功能点评估工作量
     */
    private String functionPointsAssessWorkload;
    /**
     *  costCoefficientsSum 部门成本系数总和
     */
    private String costCoefficientsSum;
    /**
     *  inputOutputRatio 投入产出比
     */
    private String inputOutputRatio;
    /**
     *  targetCompletionRate 目标完成率
     */
    private String targetCompletionRate;
    /**
     *  productReleaseRate 产品发布率
     */
    private String productReleaseRate;
    /**
     *  documentsOutputNumber 文档未及时输出数
     */
    private Integer documentsOutputNumber;
    /**
     *  projectsNotCompletedNumber 未及时完成项目启动数
     */
    private Integer projectsNotCompletedNumber;
    /**
     *  workNotCompletedNumber 未及时完成工作量延期天数
     */
    private Integer workNotCompletedNumber;
    /**
     *  unstandardizedFeedbackWorksNumber 未规范反馈工作量条数
     */
    private Integer unstandardizedFeedbackWorksNumber;
    /**
     *  notTimelyInputProductionNumber 未及时录入投产数
     */
    private Integer notTimelyInputProductionNumber;
    /**
     *  defectRate 缺陷率
     */
    private String defectRate;
    /**
     *  dataChangeProblemsNumber 数据变更问题数
     */
    private Integer dataChangeProblemsNumber;
    /**
     *  productionProblemsNumber 生产问题个数
     */
    private Integer productionProblemsNumber;
    /**
     *  smokeTestFailed 冒烟测试不通过数
     */
    private Integer smokeTestFailed;
    /**
     *  versionUpdateIssues 版本更新问题数
     */
    private Integer versionUpdateIssues;
    /**
     *  baseAwardedMarks 基地加分
     */
    private Integer baseAwardedMarks;
    /**
     *  praiseAwardedMarks 通报表扬
     */
    private Integer praiseAwardedMarks;
    /**
     *  qualityAwardedMarks 质量加分
     */
    private Integer qualityAwardedMarks;
    /**
     *  innovateAwardedMarks 创新加分
     */
    private Integer innovateAwardedMarks;
    /**
     *  baseDeductMarks 基地扣分
     */
    private Integer baseDeductMarks;
    /**
     *  criticismDeductMarks 通报批评
     */
    private Integer criticismDeductMarks;
    /**
     *  fundLoss 资金损失
     */
    private Integer fundLoss;
    /**
     *  workingAttitude 工作态度及制度遵守
     */
    private Integer workingAttitude;
    /**
     *  remark 备注
     */
    private String remark;

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

    @Override
    public String toString() {
        return "QuantitativeDataBO{" +
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
                '}';
    }
}
