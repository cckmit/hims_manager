/*
 * @ClassName OnlineDefectDO
 * @Description
 * @version 1.0
 * @Date 2020-10-19 14:11:34
 */
package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.request.GenericDTO;
import com.cmpay.framework.data.response.PageableRspDTO;


public class OnlineDefectReqDTO extends PageableRspDTO {
    /**
     *  id 主键
     */
    private Long id;
    /**
     *  reqImplMon 实施月份
     */
    private String reqImplMon;
    /**
     *  firstlevelorganization 一级主导部门
     */
    private String firstlevelorganization;
    /**
     *  documentNumber 文号
     */
    private String documentNumber;
    /**
     *  processStatus 流程状态
     */
    private String processStatus;
    /**
     *  processStartDate 流程开始日期
     */
    private String processStartDate;
    /**
     *  defectProposer 缺陷提出人
     */
    private String defectProposer;
    /**
     *  defectTheme 缺陷主题
     */
    private String defectTheme;
    /**
     *  defrctDescription 缺陷描述
     */
    private String defrctDescription;
    /**
     *  developmentLeader 开发负责人
     */
    private String developmentLeader;
    /**
     *  productLeader 产品负责人
     */
    private String productLeader;
    /**
     *  manufacturers 所属厂家
     */
    private String manufacturers;
    /**
     *  manufacturersProduct 所属厂家产品
     */
    private String manufacturersProduct;
    /**
     *  questionCause 问题产生原因
     */
    private String questionCause;
    /**
     *  questionType 问题类型
     */
    private String questionType;
    /**
     *  solution 问题解决方案
     */
    private String solution;
    /**
     * 页数
     */
    private int pageNum;
    /**
     * 页面大小
     */
    private int pageSize;
    /**
     *  isAssess 是否考核问题
     */
    private String isAssess;
    /**
     *  notAssessCause 非考核问题原因
     */
    private String notAssessCause;

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

    @Override
    public int getPageNum() {
        return pageNum;
    }

    @Override
    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "OnlineDefectReqDTO{" +
                "id=" + id +
                ", reqImplMon='" + reqImplMon + '\'' +
                ", firstlevelorganization='" + firstlevelorganization + '\'' +
                ", documentNumber='" + documentNumber + '\'' +
                ", processStatus='" + processStatus + '\'' +
                ", processStartDate='" + processStartDate + '\'' +
                ", defectProposer='" + defectProposer + '\'' +
                ", defectTheme='" + defectTheme + '\'' +
                ", defrctDescription='" + defrctDescription + '\'' +
                ", developmentLeader='" + developmentLeader + '\'' +
                ", productLeader='" + productLeader + '\'' +
                ", manufacturers='" + manufacturers + '\'' +
                ", manufacturersProduct='" + manufacturersProduct + '\'' +
                ", questionCause='" + questionCause + '\'' +
                ", questionType='" + questionType + '\'' +
                ", solution='" + solution + '\'' +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", isAssess='" + isAssess + '\'' +
                ", notAssessCause='" + notAssessCause + '\'' +
                '}';
    }
}
