/*
 * @ClassName ProblemStatisticDO
 * @Description 
 * @version 1.0
 * @Date 2020-06-30 16:04:29
 */
package com.cmpay.lemon.monitor.entity;

import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;

@DataObject
public class ProblemStatisticDO extends BaseDO {
    /**
     * @Fields epicKey epic编号
     */
    private String epicKey;
    /**
     * @Fields reqNo 需求编号
     */
    private String reqNo;
    /**
     * @Fields requirementsReviewNumber 需求评审问题数
     */
    private String requirementsReviewNumber;
    /**
     * @Fields codeReviewNumber 代码评审问题数
     */
    private String codeReviewNumber;
    /**
     * @Fields testReviewNumber 测试案例评审问题数
     */
    private String testReviewNumber;
    /**
     * @Fields productionReviewNumber 投产方案评审问题数
     */
    private String productionReviewNumber;
    /**
     * @Fields technicalReviewNumber 技术方案评审问题数
     */
    private String technicalReviewNumber;
    /**
     * @Fields otherReviewsNumber 其他评审问题数
     */
    private String otherReviewsNumber;
    /**
     * @Fields externalDefectsNumber 外围平台缺陷数
     */
    private String externalDefectsNumber;
    /**
     * @Fields versionDefectsNumber 版本更新缺陷数
     */
    private String versionDefectsNumber;
    /**
     * @Fields parameterDefectsNumber 参数配置缺陷数
     */
    private String parameterDefectsNumber;
    /**
     * @Fields functionDefectsNumber 功能设计缺陷数
     */
    private String functionDefectsNumber;
    /**
     * @Fields processDefectsNumber 流程优化缺陷数
     */
    private String processDefectsNumber;
    /**
     * @Fields promptDefectsNumber 提示语优化缺陷数
     */
    private String promptDefectsNumber;
    /**
     * @Fields pageDefectsNumber 页面设计缺陷数
     */
    private String pageDefectsNumber;
    /**
     * @Fields backgroundDefectsNumber 后台应用缺陷数
     */
    private String backgroundDefectsNumber;
    /**
     * @Fields modifyDefectsNumber 修改引入问题缺陷数
     */
    private String modifyDefectsNumber;
    /**
     * @Fields designDefectsNumber 技术设计缺陷数
     */
    private String designDefectsNumber;
    /**
     * @Fields invalidDefectsNumber 无效问题数
     */
    private String invalidDefectsNumber;

    public String getEpicKey() {
        return epicKey;
    }

    public void setEpicKey(String epicKey) {
        this.epicKey = epicKey;
    }

    public String getReqNo() {
        return reqNo;
    }

    public void setReqNo(String reqNo) {
        this.reqNo = reqNo;
    }

    public String getRequirementsReviewNumber() {
        return requirementsReviewNumber;
    }

    public void setRequirementsReviewNumber(String requirementsReviewNumber) {
        this.requirementsReviewNumber = requirementsReviewNumber;
    }

    public String getCodeReviewNumber() {
        return codeReviewNumber;
    }

    public void setCodeReviewNumber(String codeReviewNumber) {
        this.codeReviewNumber = codeReviewNumber;
    }

    public String getTestReviewNumber() {
        return testReviewNumber;
    }

    public void setTestReviewNumber(String testReviewNumber) {
        this.testReviewNumber = testReviewNumber;
    }

    public String getProductionReviewNumber() {
        return productionReviewNumber;
    }

    public void setProductionReviewNumber(String productionReviewNumber) {
        this.productionReviewNumber = productionReviewNumber;
    }

    public String getTechnicalReviewNumber() {
        return technicalReviewNumber;
    }

    public void setTechnicalReviewNumber(String technicalReviewNumber) {
        this.technicalReviewNumber = technicalReviewNumber;
    }

    public String getOtherReviewsNumber() {
        return otherReviewsNumber;
    }

    public void setOtherReviewsNumber(String otherReviewsNumber) {
        this.otherReviewsNumber = otherReviewsNumber;
    }

    public String getExternalDefectsNumber() {
        return externalDefectsNumber;
    }

    public void setExternalDefectsNumber(String externalDefectsNumber) {
        this.externalDefectsNumber = externalDefectsNumber;
    }

    public String getVersionDefectsNumber() {
        return versionDefectsNumber;
    }

    public void setVersionDefectsNumber(String versionDefectsNumber) {
        this.versionDefectsNumber = versionDefectsNumber;
    }

    public String getParameterDefectsNumber() {
        return parameterDefectsNumber;
    }

    public void setParameterDefectsNumber(String parameterDefectsNumber) {
        this.parameterDefectsNumber = parameterDefectsNumber;
    }

    public String getFunctionDefectsNumber() {
        return functionDefectsNumber;
    }

    public void setFunctionDefectsNumber(String functionDefectsNumber) {
        this.functionDefectsNumber = functionDefectsNumber;
    }

    public String getProcessDefectsNumber() {
        return processDefectsNumber;
    }

    public void setProcessDefectsNumber(String processDefectsNumber) {
        this.processDefectsNumber = processDefectsNumber;
    }

    public String getPromptDefectsNumber() {
        return promptDefectsNumber;
    }

    public void setPromptDefectsNumber(String promptDefectsNumber) {
        this.promptDefectsNumber = promptDefectsNumber;
    }

    public String getPageDefectsNumber() {
        return pageDefectsNumber;
    }

    public void setPageDefectsNumber(String pageDefectsNumber) {
        this.pageDefectsNumber = pageDefectsNumber;
    }

    public String getBackgroundDefectsNumber() {
        return backgroundDefectsNumber;
    }

    public void setBackgroundDefectsNumber(String backgroundDefectsNumber) {
        this.backgroundDefectsNumber = backgroundDefectsNumber;
    }

    public String getModifyDefectsNumber() {
        return modifyDefectsNumber;
    }

    public void setModifyDefectsNumber(String modifyDefectsNumber) {
        this.modifyDefectsNumber = modifyDefectsNumber;
    }

    public String getDesignDefectsNumber() {
        return designDefectsNumber;
    }

    public void setDesignDefectsNumber(String designDefectsNumber) {
        this.designDefectsNumber = designDefectsNumber;
    }

    public String getInvalidDefectsNumber() {
        return invalidDefectsNumber;
    }

    public void setInvalidDefectsNumber(String invalidDefectsNumber) {
        this.invalidDefectsNumber = invalidDefectsNumber;
    }
}