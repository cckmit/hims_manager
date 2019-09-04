/*
 * @ClassName CenterDO
 * @Description 
 * @version 1.0
 * @Date 2019-07-25 11:01:18
 */
package com.cmpay.lemon.monitor.entity;

import com.cmpay.lemon.framework.annotation.DataObject;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@DataObject
public class ProductionDO extends AbstractDO {
    private String pro_number;
    private String pro_need;
    private String pro_type;
    private Date pro_date_start;
    private Date pro_date_end;
    private Date pro_date;
    private String application_dept;
    private String pro_applicant;
    private String applicant_tel;
    private String pro_module;
    private String business_principal;
    private String base_principal;
    private String pro_manager;
    private String pro_status;
    private String is_up_database;
    private String is_up_structure;
    private String pro_operation;
    private String is_ref_cerificate;
    private String is_advance_production;
    private String not_advance_reason;
    private String pro_advance_result;
    private String not_production_impact;
    private String identifier;
    private String identifier_tel;
    private String pro_checker;
    private String checker_tel;
    private String validation;
    private String development_leader;
    private String approver;
    private String update_operator;
    private String remark;
    private String unusual_reason_phrase;
    private String urgent_reason_phrase;
    private String production_deployment_result;
    private String is_operation_production;
    private String mail_leader;//开发负责人邮箱
    private String svntab_name;//SVN表名称


    /*
     * 紧急更新
     */
    private String completion_update ;
    private String early_implementation;
    private String influence_use ;
    private String influence_use_reason;
    private String influence_use_inf;
    private String operating_time;

    /*
     * 审核邮件
     */
    private String mail_recipient;
    private String mail_copy_person;

    /*
     * 部门关系
     */

    private String dept_name;
    private String dept_manager_name;
    private String development_dept;

    /**
     * 投产包
     */
    private String pro_pkg_status;
    private Timestamp pro_pkg_time;
    private String pro_pkg_name;

    /**
     * 是否有回退方案
     */
    private String is_fallback;


    public ProductionDO() {

    }
    public ProductionDO(String proNumber) {
        pro_number = proNumber;
    }

    public ProductionDO(String proNumber, String proStatus) {
        pro_number = proNumber;
        pro_status = proStatus;
    }

    public ProductionDO(Date pro_date_start, Date pro_date_end) {
        this.pro_date_start = pro_date_start;
        this.pro_date_end = pro_date_end;
    }





    public ProductionDO(String proNumber, String proNeed, String proType,
                          Date proDate, String applicationDept, String proApplicant,
                          String applicantTel, String proModule, String businessPrincipal,
                          String basePrincipal, String proManager, String proStatus,
                          String isUpDatabase, String isUpStructure, String proOperation,
                          String isRefCerificate, String isAdvanceProduction,
                          String notAdvanceReason, String proAdvanceResult,
                          String notProductionImpact, String identifier,
                          String identifierTel, String proChecker, String checkerTel,
                          String validation, String developmentLeader, String approver,
                          String updateOperator, String remark, String unusualReasonPhrase,
                          String urgentReasonPhrase, String productionDeploymentResult,
                          String isOperationProduction, String completionUpdate,
                          String earlyImplementation, String influenceUse,
                          String influenceUseReason, String influenceUseInf,
                          String operatingTime, String mailRecipient, String mailCopyPerson,
                          String proPkgStatus,Timestamp proPkgTime, String proPkgName,String mailLeader,String svntabName) {
        super();
        pro_number = proNumber;
        pro_need = proNeed;
        pro_type = proType;
        pro_date = proDate;
        application_dept = applicationDept;
        pro_applicant = proApplicant;
        applicant_tel = applicantTel;
        pro_module = proModule;
        business_principal = businessPrincipal;
        base_principal = basePrincipal;
        pro_manager = proManager;
        pro_status = proStatus;
        is_up_database = isUpDatabase;
        is_up_structure = isUpStructure;
        pro_operation = proOperation;
        is_ref_cerificate = isRefCerificate;
        is_advance_production = isAdvanceProduction;
        not_advance_reason = notAdvanceReason;
        pro_advance_result = proAdvanceResult;
        not_production_impact = notProductionImpact;
        this.identifier = identifier;
        identifier_tel = identifierTel;
        pro_checker = proChecker;
        checker_tel = checkerTel;
        this.validation = validation;
        development_leader = developmentLeader;
        this.approver = approver;
        update_operator = updateOperator;
        this.remark = remark;
        unusual_reason_phrase = unusualReasonPhrase;
        urgent_reason_phrase = urgentReasonPhrase;
        production_deployment_result = productionDeploymentResult;
        is_operation_production = isOperationProduction;
        completion_update = completionUpdate;
        early_implementation = earlyImplementation;
        influence_use = influenceUse;
        influence_use_reason = influenceUseReason;
        influence_use_inf = influenceUseInf;
        operating_time = operatingTime;
        mail_recipient = mailRecipient;
        mail_copy_person = mailCopyPerson;
        pro_pkg_status = proPkgStatus;
        pro_pkg_time = proPkgTime;
        pro_pkg_name = proPkgName;
        mail_leader = mailLeader;
        svntab_name = svntabName;
    }
    public ProductionDO(String proNumber, String proNeed, String proType,
                          Date proDate, String applicationDept, String proApplicant,
                          String applicantTel, String proModule, String businessPrincipal,
                          String basePrincipal, String proManager, String proStatus,
                          String isUpDatabase, String isUpStructure, String proOperation,
                          String isRefCerificate, String isAdvanceProduction,
                          String notAdvanceReason, String proAdvanceResult,
                          String notProductionImpact, String identifier,
                          String identifierTel, String proChecker, String checkerTel,
                          String validation, String developmentLeader, String approver,
                          String updateOperator, String remark, String unusualReasonPhrase,
                          String urgentReasonPhrase, String productionDeploymentResult,
                          String isOperationProduction, String completionUpdate,
                          String earlyImplementation, String influenceUse,
                          String influenceUseReason, String influenceUseInf,
                          String operatingTime, String proPkgStatus, Timestamp proPkgTime, String proPkgName,String mailLeader,String svntabName) {
        super();
        pro_number = proNumber;
        pro_need = proNeed;
        pro_type = proType;
        pro_date = proDate;
        application_dept = applicationDept;
        pro_applicant = proApplicant;
        applicant_tel = applicantTel;
        pro_module = proModule;
        business_principal = businessPrincipal;
        base_principal = basePrincipal;
        pro_manager = proManager;
        pro_status = proStatus;
        is_up_database = isUpDatabase;
        is_up_structure = isUpStructure;
        pro_operation = proOperation;
        is_ref_cerificate = isRefCerificate;
        is_advance_production = isAdvanceProduction;
        not_advance_reason = notAdvanceReason;
        pro_advance_result = proAdvanceResult;
        not_production_impact = notProductionImpact;
        this.identifier = identifier;
        identifier_tel = identifierTel;
        pro_checker = proChecker;
        checker_tel = checkerTel;
        this.validation = validation;
        development_leader = developmentLeader;
        this.approver = approver;
        update_operator = updateOperator;
        this.remark = remark;
        unusual_reason_phrase = unusualReasonPhrase;
        urgent_reason_phrase = urgentReasonPhrase;
        production_deployment_result = productionDeploymentResult;
        is_operation_production = isOperationProduction;
        completion_update = completionUpdate;
        early_implementation = earlyImplementation;
        influence_use = influenceUse;
        influence_use_reason = influenceUseReason;
        influence_use_inf = influenceUseInf;
        operating_time = operatingTime;
        pro_pkg_status = proPkgStatus;
        pro_pkg_time = proPkgTime;
        pro_pkg_name = proPkgName;
        mail_leader = mailLeader;
        svntab_name = svntabName;
    }



    public String getDept_name() {
        return dept_name;
    }
    public void setDept_name(String deptName) {
        dept_name = deptName;
    }
    public String getDept_manager_name() {
        return dept_manager_name;
    }
    public void setDept_manager_name(String deptManagerName) {
        dept_manager_name = deptManagerName;
    }
    public String getMail_recipient() {
        return mail_recipient;
    }
    public void setMail_recipient(String mailRecipient) {
        mail_recipient = mailRecipient;
    }
    public String getMail_copy_person() {
        return mail_copy_person;
    }
    public void setMail_copy_person(String mailCopyPerson) {
        mail_copy_person = mailCopyPerson;
    }
    public String getIs_operation_production() {
        return is_operation_production;
    }
    public void setIs_operation_production(String isOperationProduction) {
        is_operation_production = isOperationProduction;
    }
    public String getProduction_deployment_result() {
        return production_deployment_result;
    }



    public void setProduction_deployment_result(String productionDeploymentResult) {
        production_deployment_result = productionDeploymentResult;
    }



    public String getNot_production_impact() {
        return not_production_impact;
    }



    public void setNot_production_impact(String notProductionImpact) {
        not_production_impact = notProductionImpact;
    }





    public String getPro_number() {
        return pro_number;
    }




    public void setPro_number(String proNumber) {
        pro_number = proNumber;
    }




    public String getDevelopment_dept() {
        return development_dept;
    }
    public void setDevelopment_dept(String developmentDept) {
        development_dept = developmentDept;
    }
    public String getPro_need() {
        return pro_need;
    }




    public void setPro_need(String proNeed) {
        pro_need = proNeed;
    }




    public Date getPro_date_start() {
        return pro_date_start;
    }
    public void setPro_date_start(Date proDateStart) {
        pro_date_start = proDateStart;
    }
    public Date getPro_date_end() {
        return pro_date_end;
    }
    public void setPro_date_end(Date proDateEnd) {
        pro_date_end = proDateEnd;
    }
    public String getPro_type() {
        return pro_type;
    }




    public void setPro_type(String proType) {
        pro_type = proType;
    }




    public Date getPro_date() {
        return pro_date;
    }




    public void setPro_date(Date proDate) {
        pro_date = proDate;
    }




    public String getApplication_dept() {
        return application_dept;
    }




    public void setApplication_dept(String applicationDept) {
        application_dept = applicationDept;
    }




    public String getPro_applicant() {
        return pro_applicant;
    }




    public void setPro_applicant(String proApplicant) {
        pro_applicant = proApplicant;
    }




    public String getApplicant_tel() {
        return applicant_tel;
    }




    public void setApplicant_tel(String applicantTel) {
        applicant_tel = applicantTel;
    }




    public String getPro_module() {
        return pro_module;
    }




    public void setPro_module(String proModule) {
        pro_module = proModule;
    }




    public String getBusiness_principal() {
        return business_principal;
    }




    public void setBusiness_principal(String businessPrincipal) {
        business_principal = businessPrincipal;
    }




    public String getBase_principal() {
        return base_principal;
    }




    public void setBase_principal(String basePrincipal) {
        base_principal = basePrincipal;
    }




    public String getPro_manager() {
        return pro_manager;
    }




    public void setPro_manager(String proManager) {
        pro_manager = proManager;
    }




    public String getPro_status() {
        return pro_status;
    }




    public void setPro_status(String proStatus) {
        pro_status = proStatus;
    }




    public String getIs_up_database() {
        return is_up_database;
    }




    public void setIs_up_database(String isUpDatabase) {
        is_up_database = isUpDatabase;
    }




    public String getIs_up_structure() {
        return is_up_structure;
    }




    public void setIs_up_structure(String isUpStructure) {
        is_up_structure = isUpStructure;
    }




    public String getPro_operation() {
        return pro_operation;
    }




    public void setPro_operation(String proOperation) {
        pro_operation = proOperation;
    }




    public String getIs_ref_cerificate() {
        return is_ref_cerificate;
    }




    public void setIs_ref_cerificate(String isRefCerificate) {
        is_ref_cerificate = isRefCerificate;
    }




    public String getIs_advance_production() {
        return is_advance_production;
    }




    public void setIs_advance_production(String isAdvanceProduction) {
        is_advance_production = isAdvanceProduction;
    }




    public String getNot_advance_reason() {
        return not_advance_reason;
    }




    public void setNot_advance_reason(String notAdvanceReason) {
        not_advance_reason = notAdvanceReason;
    }




    public String getPro_advance_result() {
        return pro_advance_result;
    }




    public void setPro_advance_result(String proAdvanceResult) {
        pro_advance_result = proAdvanceResult;
    }




    public String getIdentifier() {
        return identifier;
    }




    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }




    public String getIdentifier_tel() {
        return identifier_tel;
    }




    public void setIdentifier_tel(String identifierTel) {
        identifier_tel = identifierTel;
    }




    public String getPro_checker() {
        return pro_checker;
    }




    public void setPro_checker(String proChecker) {
        pro_checker = proChecker;
    }




    public String getChecker_tel() {
        return checker_tel;
    }




    public void setChecker_tel(String checkerTel) {
        checker_tel = checkerTel;
    }




    public String getValidation() {
        return validation;
    }




    public void setValidation(String validation) {
        this.validation = validation;
    }




    public String getDevelopment_leader() {
        return development_leader;
    }




    public void setDevelopment_leader(String developmentLeader) {
        development_leader = developmentLeader;
    }




    public String getApprover() {
        return approver;
    }




    public void setApprover(String approver) {
        this.approver = approver;
    }




    public String getUpdate_operator() {
        return update_operator;
    }




    public void setUpdate_operator(String updateOperator) {
        update_operator = updateOperator;
    }




    public String getRemark() {
        return remark;
    }




    public void setRemark(String remark) {
        this.remark = remark;
    }




    public String getUnusual_reason_phrase() {
        return unusual_reason_phrase;
    }




    public void setUnusual_reason_phrase(String unusualReasonPhrase) {
        unusual_reason_phrase = unusualReasonPhrase;
    }




    public String getUrgent_reason_phrase() {
        return urgent_reason_phrase;
    }




    public void setUrgent_reason_phrase(String urgentReasonPhrase) {
        urgent_reason_phrase = urgentReasonPhrase;
    }




    public String getCompletion_update() {
        return completion_update;
    }




    public void setCompletion_update(String completionUpdate) {
        completion_update = completionUpdate;
    }




    public String getEarly_implementation() {
        return early_implementation;
    }




    public void setEarly_implementation(String earlyImplementation) {
        early_implementation = earlyImplementation;
    }




    public String getInfluence_use() {
        return influence_use;
    }




    public void setInfluence_use(String influenceUse) {
        influence_use = influenceUse;
    }




    public String getInfluence_use_reason() {
        return influence_use_reason;
    }




    public void setInfluence_use_reason(String influenceUseReason) {
        influence_use_reason = influenceUseReason;
    }




    public String getInfluence_use_inf() {
        return influence_use_inf;
    }




    public void setInfluence_use_inf(String influenceUseInf) {
        influence_use_inf = influenceUseInf;
    }




    public String getOperating_time() {
        return operating_time;
    }


    public void setOperating_time(String operatingTime) {
        operating_time = operatingTime;
    }


    public String getPro_pkg_status() {
        return pro_pkg_status;
    }

    public void setPro_pkg_status(String proPkgStatus) {
        pro_pkg_status = proPkgStatus;
    }

    public Timestamp getPro_pkg_time() {
        return pro_pkg_time;
    }
    public void setPro_pkg_time(Timestamp proPkgTime) {
        pro_pkg_time = proPkgTime;
    }

    public String getPro_pkg_name() {
        return pro_pkg_name;
    }
    public void setPro_pkg_name(String proPkgName) {
        pro_pkg_name = proPkgName;
    }
    public String getMail_leader() {
        return mail_leader;
    }
    public void setMail_leader(String mailLeader) {
        this.mail_leader = mailLeader;
    }

    public String getSvntab_name() {
        return svntab_name;
    }
    public void setSvntab_name(String svntabName) {
        this.svntab_name = svntabName;
    }
    @Override
    public Serializable getId() {
        return null;
    }

    public String getIs_fallback() {
        return is_fallback;
    }

    public void setIs_fallback(String is_fallback) {
        this.is_fallback = is_fallback;
    }
}