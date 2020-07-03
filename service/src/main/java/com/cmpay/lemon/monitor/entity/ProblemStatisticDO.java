/*
 * @ClassName ProblemStatisticDO
 * @Description
 * @version 1.0
 * @Date 2020-07-02 17:05:01
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
    private int requirementsReviewNumber;
    /**
     * @Fields codeReviewNumber 代码评审问题数
     */
    private int codeReviewNumber;
    /**
     * @Fields testReviewNumber 测试案例评审问题数
     */
    private int testReviewNumber;
    /**
     * @Fields productionReviewNumber 投产方案评审问题数
     */
    private int productionReviewNumber;
    /**
     * @Fields technicalReviewNumber 技术方案评审问题数
     */
    private int technicalReviewNumber;
    /**
     * @Fields otherReviewsNumber 其他评审问题数
     */
    private int otherReviewsNumber;
    /**
     * @Fields externalDefectsNumber 外围平台缺陷数
     */
    private int externalDefectsNumber;
    /**
     * @Fields versionDefectsNumber 版本更新缺陷数
     */
    private int versionDefectsNumber;
    /**
     * @Fields parameterDefectsNumber 参数配置缺陷数
     */
    private int parameterDefectsNumber;
    /**
     * @Fields functionDefectsNumber 功能设计缺陷数
     */
    private int functionDefectsNumber;
    /**
     * @Fields processDefectsNumber 流程优化缺陷数
     */
    private int processDefectsNumber;
    /**
     * @Fields promptDefectsNumber 提示语优化缺陷数
     */
    private int promptDefectsNumber;
    /**
     * @Fields pageDefectsNumber 页面设计缺陷数
     */
    private int pageDefectsNumber;
    /**
     * @Fields backgroundDefectsNumber 后台应用缺陷数
     */
    private int backgroundDefectsNumber;
    /**
     * @Fields modifyDefectsNumber 修改引入问题缺陷数
     */
    private int modifyDefectsNumber;
    /**
     * @Fields designDefectsNumber 技术设计缺陷数
     */
    private int designDefectsNumber;
    /**
     * @Fields invalidDefectsNumber 无效问题数
     */
    private int invalidDefectsNumber;
    /**
     * @Fields frontDefectsNumber 前端缺陷数
     */
    private int frontDefectsNumber;

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

    public Integer getRequirementsReviewNumber() {
        return requirementsReviewNumber;
    }

    public void setRequirementsReviewNumber(Integer requirementsReviewNumber) {
        this.requirementsReviewNumber = requirementsReviewNumber;
    }

    public Integer getCodeReviewNumber() {
        return codeReviewNumber;
    }

    public void setCodeReviewNumber(Integer codeReviewNumber) {
        this.codeReviewNumber = codeReviewNumber;
    }

    public Integer getTestReviewNumber() {
        return testReviewNumber;
    }

    public void setTestReviewNumber(Integer testReviewNumber) {
        this.testReviewNumber = testReviewNumber;
    }

    public Integer getProductionReviewNumber() {
        return productionReviewNumber;
    }

    public void setProductionReviewNumber(Integer productionReviewNumber) {
        this.productionReviewNumber = productionReviewNumber;
    }

    public Integer getTechnicalReviewNumber() {
        return technicalReviewNumber;
    }

    public void setTechnicalReviewNumber(Integer technicalReviewNumber) {
        this.technicalReviewNumber = technicalReviewNumber;
    }

    public Integer getOtherReviewsNumber() {
        return otherReviewsNumber;
    }

    public void setOtherReviewsNumber(Integer otherReviewsNumber) {
        this.otherReviewsNumber = otherReviewsNumber;
    }

    public Integer getExternalDefectsNumber() {
        return externalDefectsNumber;
    }

    public void setExternalDefectsNumber(Integer externalDefectsNumber) {
        this.externalDefectsNumber = externalDefectsNumber;
    }

    public Integer getVersionDefectsNumber() {
        return versionDefectsNumber;
    }

    public void setVersionDefectsNumber(Integer versionDefectsNumber) {
        this.versionDefectsNumber = versionDefectsNumber;
    }

    public Integer getParameterDefectsNumber() {
        return parameterDefectsNumber;
    }

    public void setParameterDefectsNumber(Integer parameterDefectsNumber) {
        this.parameterDefectsNumber = parameterDefectsNumber;
    }

    public Integer getFunctionDefectsNumber() {
        return functionDefectsNumber;
    }

    public void setFunctionDefectsNumber(Integer functionDefectsNumber) {
        this.functionDefectsNumber = functionDefectsNumber;
    }

    public Integer getProcessDefectsNumber() {
        return processDefectsNumber;
    }

    public void setProcessDefectsNumber(Integer processDefectsNumber) {
        this.processDefectsNumber = processDefectsNumber;
    }

    public Integer getPromptDefectsNumber() {
        return promptDefectsNumber;
    }

    public void setPromptDefectsNumber(Integer promptDefectsNumber) {
        this.promptDefectsNumber = promptDefectsNumber;
    }

    public Integer getPageDefectsNumber() {
        return pageDefectsNumber;
    }

    public void setPageDefectsNumber(Integer pageDefectsNumber) {
        this.pageDefectsNumber = pageDefectsNumber;
    }

    public Integer getBackgroundDefectsNumber() {
        return backgroundDefectsNumber;
    }

    public void setBackgroundDefectsNumber(Integer backgroundDefectsNumber) {
        this.backgroundDefectsNumber = backgroundDefectsNumber;
    }

    public Integer getModifyDefectsNumber() {
        return modifyDefectsNumber;
    }

    public void setModifyDefectsNumber(Integer modifyDefectsNumber) {
        this.modifyDefectsNumber = modifyDefectsNumber;
    }

    public Integer getDesignDefectsNumber() {
        return designDefectsNumber;
    }

    public void setDesignDefectsNumber(Integer designDefectsNumber) {
        this.designDefectsNumber = designDefectsNumber;
    }

    public Integer getInvalidDefectsNumber() {
        return invalidDefectsNumber;
    }

    public void setInvalidDefectsNumber(Integer invalidDefectsNumber) {
        this.invalidDefectsNumber = invalidDefectsNumber;
    }

    public Integer getFrontDefectsNumber() {
        return frontDefectsNumber;
    }

    public void setFrontDefectsNumber(Integer frontDefectsNumber) {
        this.frontDefectsNumber = frontDefectsNumber;
    }
}