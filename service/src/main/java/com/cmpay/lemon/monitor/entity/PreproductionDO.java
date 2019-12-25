/*
 * @ClassName PreproductionDO
 * @Description 
 * @version 1.0
 * @Date 2019-12-25 11:24:02
 */
package com.cmpay.lemon.monitor.entity;

import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;
import java.time.LocalDate;

@DataObject
public class PreproductionDO extends BaseDO {
    /**
     * @Fields preNumber 预投产编号
     */
    private String preNumber;
    /**
     * @Fields preNeed 预投产名称
     */
    private String preNeed;
    /**
     * @Fields preType 预投产类型
     */
    private String preType;
    /**
     * @Fields preDate 预计预投产时间
     */
    private LocalDate preDate;
    /**
     * @Fields applicationDept 申请部门
     */
    private String applicationDept;
    /**
     * @Fields preApplicant 预投产申请人
     */
    private String preApplicant;
    /**
     * @Fields applicantTel 预投产申请人联系方式
     */
    private String applicantTel;
    /**
     * @Fields preManager 产品经理
     */
    private String preManager;
    /**
     * @Fields preStatus 预投产状态
     */
    private String preStatus;
    /**
     * @Fields productionDeploymentResult 预投产部署结果
     */
    private String productionDeploymentResult;
    /**
     * @Fields proAdvanceResult 预投产验证结果
     */
    private String proAdvanceResult;
    /**
     * @Fields proPkgTime 投产包上传时间
     */
    private LocalDate proPkgTime;
    /**
     * @Fields proPkgName 投产包名称
     */
    private String proPkgName;
    /**
     * @Fields developmentLeader 开发负责人
     */
    private String developmentLeader;
    /**
     * @Fields identifier 验证人
     */
    private String identifier;
    /**
     * @Fields identifierTel 验证人联系方式
     */
    private String identifierTel;
    /**
     * @Fields proChecker 验证复核人
     */
    private String proChecker;
    /**
     * @Fields checkerTel 验证复核人联系方式
     */
    private String checkerTel;
    /**
     * @Fields remark 备注
     */
    private String remark;
    /**
     * @Fields mailLeader 开发负责人邮箱
     */
    private String mailLeader;

    public String getPreNumber() {
        return preNumber;
    }

    public void setPreNumber(String preNumber) {
        this.preNumber = preNumber;
    }

    public String getPreNeed() {
        return preNeed;
    }

    public void setPreNeed(String preNeed) {
        this.preNeed = preNeed;
    }

    public String getPreType() {
        return preType;
    }

    public void setPreType(String preType) {
        this.preType = preType;
    }

    public LocalDate getPreDate() {
        return preDate;
    }

    public void setPreDate(LocalDate preDate) {
        this.preDate = preDate;
    }

    public String getApplicationDept() {
        return applicationDept;
    }

    public void setApplicationDept(String applicationDept) {
        this.applicationDept = applicationDept;
    }

    public String getPreApplicant() {
        return preApplicant;
    }

    public void setPreApplicant(String preApplicant) {
        this.preApplicant = preApplicant;
    }

    public String getApplicantTel() {
        return applicantTel;
    }

    public void setApplicantTel(String applicantTel) {
        this.applicantTel = applicantTel;
    }

    public String getPreManager() {
        return preManager;
    }

    public void setPreManager(String preManager) {
        this.preManager = preManager;
    }

    public String getPreStatus() {
        return preStatus;
    }

    public void setPreStatus(String preStatus) {
        this.preStatus = preStatus;
    }

    public String getProductionDeploymentResult() {
        return productionDeploymentResult;
    }

    public void setProductionDeploymentResult(String productionDeploymentResult) {
        this.productionDeploymentResult = productionDeploymentResult;
    }

    public String getProAdvanceResult() {
        return proAdvanceResult;
    }

    public void setProAdvanceResult(String proAdvanceResult) {
        this.proAdvanceResult = proAdvanceResult;
    }

    public LocalDate getProPkgTime() {
        return proPkgTime;
    }

    public void setProPkgTime(LocalDate proPkgTime) {
        this.proPkgTime = proPkgTime;
    }

    public String getProPkgName() {
        return proPkgName;
    }

    public void setProPkgName(String proPkgName) {
        this.proPkgName = proPkgName;
    }

    public String getDevelopmentLeader() {
        return developmentLeader;
    }

    public void setDevelopmentLeader(String developmentLeader) {
        this.developmentLeader = developmentLeader;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifierTel() {
        return identifierTel;
    }

    public void setIdentifierTel(String identifierTel) {
        this.identifierTel = identifierTel;
    }

    public String getProChecker() {
        return proChecker;
    }

    public void setProChecker(String proChecker) {
        this.proChecker = proChecker;
    }

    public String getCheckerTel() {
        return checkerTel;
    }

    public void setCheckerTel(String checkerTel) {
        this.checkerTel = checkerTel;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMailLeader() {
        return mailLeader;
    }

    public void setMailLeader(String mailLeader) {
        this.mailLeader = mailLeader;
    }
}