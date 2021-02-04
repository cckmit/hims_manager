/*
 * @ClassName VerificationResultsFeedbackDO
 * @Description 
 * @version 1.0
 * @Date 2021-02-02 16:57:23
 */
package com.cmpay.lemon.monitor.entity;

import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;
import java.time.LocalDateTime;

@DataObject
public class VerificationResultsFeedbackDO extends BaseDO {
    /**
     * @Fields id id
     */
    private Long id;
    /**
     * @Fields proNumber 投产编号
     */
    private String proNumber;
    /**
     * @Fields devpLeadDept 归属部门
     */
    private String devpLeadDept;
    /**
     * @Fields resultsDetail 投产结果描述
     */
    private String resultsDetail;
    /**
     * @Fields isVerification 是否验证通过
     */
    private String isVerification;
    /**
     * @Fields functionCaseDetail 功能案例验证结果描述
     */
    private String functionCaseDetail;
    /**
     * @Fields functionCaseFilename 功能案例验证结果文件名
     */
    private String functionCaseFilename;
    /**
     * @Fields functionCaseTime 功能案例验证结果上传时间
     */
    private LocalDateTime functionCaseTime;
    /**
     * @Fields technicalCaseDetail 技术案例验证结果描述
     */
    private String technicalCaseDetail;
    /**
     * @Fields technicalCaseFilename 技术案例验证结果文件名
     */
    private String technicalCaseFilename;
    /**
     * @Fields technicalCaseTime 技术案例验证结果上传时间
     */
    private LocalDateTime technicalCaseTime;
    /**
     * @Fields updateUser 修改人
     */
    private String updateUser;
    /**
     * @Fields updateTime 修改时间
     */
    private LocalDateTime updateTime;
    /**
     * @Fields otherFeedback 其它
     */
    private String otherFeedback;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProNumber() {
        return proNumber;
    }

    public void setProNumber(String proNumber) {
        this.proNumber = proNumber;
    }

    public String getDevpLeadDept() {
        return devpLeadDept;
    }

    public void setDevpLeadDept(String devpLeadDept) {
        this.devpLeadDept = devpLeadDept;
    }

    public String getResultsDetail() {
        return resultsDetail;
    }

    public void setResultsDetail(String resultsDetail) {
        this.resultsDetail = resultsDetail;
    }

    public String getIsVerification() {
        return isVerification;
    }

    public void setIsVerification(String isVerification) {
        this.isVerification = isVerification;
    }

    public String getFunctionCaseDetail() {
        return functionCaseDetail;
    }

    public void setFunctionCaseDetail(String functionCaseDetail) {
        this.functionCaseDetail = functionCaseDetail;
    }

    public String getFunctionCaseFilename() {
        return functionCaseFilename;
    }

    public void setFunctionCaseFilename(String functionCaseFilename) {
        this.functionCaseFilename = functionCaseFilename;
    }

    public LocalDateTime getFunctionCaseTime() {
        return functionCaseTime;
    }

    public void setFunctionCaseTime(LocalDateTime functionCaseTime) {
        this.functionCaseTime = functionCaseTime;
    }

    public String getTechnicalCaseDetail() {
        return technicalCaseDetail;
    }

    public void setTechnicalCaseDetail(String technicalCaseDetail) {
        this.technicalCaseDetail = technicalCaseDetail;
    }

    public String getTechnicalCaseFilename() {
        return technicalCaseFilename;
    }

    public void setTechnicalCaseFilename(String technicalCaseFilename) {
        this.technicalCaseFilename = technicalCaseFilename;
    }

    public LocalDateTime getTechnicalCaseTime() {
        return technicalCaseTime;
    }

    public void setTechnicalCaseTime(LocalDateTime technicalCaseTime) {
        this.technicalCaseTime = technicalCaseTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getOtherFeedback() {
        return otherFeedback;
    }

    public void setOtherFeedback(String otherFeedback) {
        this.otherFeedback = otherFeedback;
    }
}