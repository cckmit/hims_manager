/*
 * @ClassName OnlineDefectDO
 * @Description
 * @version 1.0
 * @Date 2020-10-21 17:33:48
 */
package com.cmpay.lemon.monitor.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;

@DataObject
public class OnlineDefectDO extends BaseDO {
    /**
     * @Fields id 主键
     */
    private Long id;
    /**
     * @Fields reqImplMon 实施月份
     */
    @Excel(name = "实施月份")
    private String reqImplMon;
    /**
     * @Fields firstlevelorganization 一级主导部门
     */
    @Excel(name = "一级主导部门")
    private String firstlevelorganization;
    /**
     * @Fields isAssess 是否考核问题
     */
    @Excel(name = "是否考核问题")
    private String isAssess;
    /**
     * @Fields notAssessCause 非考核问题原因
     */
    @Excel(name = "非考核问题原因")
    private String notAssessCause;
    /**
     * @Fields documentNumber 文号
     */
    @Excel(name = "文号")
    private String documentNumber;
    /**
     * @Fields processStatus 流程状态
     */
    @Excel(name = "流程状态")
    private String processStatus;
    /**
     * @Fields processStartDate 流程开始日期
     */
    @Excel(name = "流程开始日期")
    private String processStartDate;
    /**
     * @Fields defectProposer 缺陷提出人
     */
    @Excel(name = "缺陷提出人")
    private String defectProposer;
    /**
     * @Fields defectTheme 缺陷主题
     */
    @Excel(name = "缺陷主题")
    private String defectTheme;
    /**
     * @Fields defrctDescription 缺陷描述
     */
    @Excel(name = "缺陷描述")
    private String defrctDescription;
    /**
     * @Fields developmentLeader 开发负责人
     */
    @Excel(name = "开发负责人")
    private String developmentLeader;
    /**
     * @Fields productLeader 产品负责人
     */
    @Excel(name = "产品负责人")
    private String productLeader;
    /**
     * @Fields manufacturers 所属厂家
     */
    @Excel(name = "所属厂家")
    private String manufacturers;
    /**
     * @Fields manufacturersProduct 所属厂家产品
     */
    @Excel(name = "所属厂家产品")
    private String manufacturersProduct;
    /**
     * @Fields questionCause 问题产生原因
     */
    @Excel(name = "问题产生原因")
    private String questionCause;
    /**
     * @Fields questionType 问题类型
     */
    @Excel(name = "问题类型")
    private String questionType;
    /**
     * @Fields solution 问题解决方案
     */
    @Excel(name = "问题解决方案")
    private String solution;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReqImplMon() {
        return reqImplMon;
    }

    public void setReqImplMon(String reqImplMon) {
        this.reqImplMon = reqImplMon;
    }

    public String getFirstlevelorganization() {
        return firstlevelorganization;
    }

    public void setFirstlevelorganization(String firstlevelorganization) {
        this.firstlevelorganization = firstlevelorganization;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    public String getProcessStartDate() {
        return processStartDate;
    }

    public void setProcessStartDate(String processStartDate) {
        this.processStartDate = processStartDate;
    }

    public String getDefectProposer() {
        return defectProposer;
    }

    public void setDefectProposer(String defectProposer) {
        this.defectProposer = defectProposer;
    }

    public String getDefectTheme() {
        return defectTheme;
    }

    public void setDefectTheme(String defectTheme) {
        this.defectTheme = defectTheme;
    }

    public String getDefrctDescription() {
        return defrctDescription;
    }

    public void setDefrctDescription(String defrctDescription) {
        this.defrctDescription = defrctDescription;
    }

    public String getDevelopmentLeader() {
        return developmentLeader;
    }

    public void setDevelopmentLeader(String developmentLeader) {
        this.developmentLeader = developmentLeader;
    }

    public String getProductLeader() {
        return productLeader;
    }

    public void setProductLeader(String productLeader) {
        this.productLeader = productLeader;
    }

    public String getManufacturers() {
        return manufacturers;
    }

    public void setManufacturers(String manufacturers) {
        this.manufacturers = manufacturers;
    }

    public String getManufacturersProduct() {
        return manufacturersProduct;
    }

    public void setManufacturersProduct(String manufacturersProduct) {
        this.manufacturersProduct = manufacturersProduct;
    }

    public String getQuestionCause() {
        return questionCause;
    }

    public void setQuestionCause(String questionCause) {
        this.questionCause = questionCause;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getIsAssess() {
        return isAssess;
    }

    public void setIsAssess(String isAssess) {
        this.isAssess = isAssess;
    }

    public String getNotAssessCause() {
        return notAssessCause;
    }

    public void setNotAssessCause(String notAssessCause) {
        this.notAssessCause = notAssessCause;
    }
}
