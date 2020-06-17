/*
 * @ClassName ProductionDefectsDO
 * @Description 
 * @version 1.0
 * @Date 2020-06-16 15:15:52
 */
package com.cmpay.lemon.monitor.bo;

import com.cmpay.lemon.framework.annotation.DataObject;

@DataObject
public class ProductionDefectsBO{
    /**
     * @Fields id 主键
     */
    private Integer id;
    /**
     * @Fields serialnumber 流水号
     */
    private String serialnumber;
    /**
     * @Fields documentnumber 文号
     */
    private String documentnumber;
    /**
     * @Fields processstatus 流程状态
     */
    private String processstatus;
    /**
     * @Fields processstartdate 流程开始日期
     */
    private String processstartdate;
    /**
     * @Fields currentsession 当前环节
     */
    private String currentsession;
    /**
     * @Fields currentexecutor 当前执行人
     */
    private String currentexecutor;
    /**
     * @Fields problemraiser 问题录入(问题提出人)
     */
    private String problemraiser;
    /**
     * @Fields questiontitle 问题录入(问题标题)
     */
    private String questiontitle;
    /**
     * @Fields questionnumber 问题录入(问题编号)
     */
    private String questionnumber;
    /**
     * @Fields questiondetails 问题录入(问题详细)
     */
    private String questiondetails;
    /**
     * @Fields solution 问题分析(问题原因及解决方案)
     */
    private String solution;
    /**
     * @Fields problemattributiondept 问题分析(问题归属部门)
     */
    private String problemattributiondept;
    /**
     * @Fields personincharge 问题分析(问题负责人)
     */
    private String personincharge;
    /**
     * @Fields questiontype 问题分析(问题类型)
     */
    private String questiontype;
    /**
     * @Fields identifytheproblem 回复审核(问题定位)
     */
    private String identifytheproblem;
    /**
     * @Fields developmentleader 程序修改(开发负责人)
     */
    private String developmentleader;
    /**
     * @Fields updatetype 程序修改(更新类型)
     */
    private String updatetype;
    /**
     * 页数
     */
    private int pageNum;
    /**
     * 页面大小
     */
    private int pageSize;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSerialnumber() {
        return serialnumber;
    }

    public void setSerialnumber(String serialnumber) {
        this.serialnumber = serialnumber;
    }

    public String getDocumentnumber() {
        return documentnumber;
    }

    public void setDocumentnumber(String documentnumber) {
        this.documentnumber = documentnumber;
    }

    public String getProcessstatus() {
        return processstatus;
    }

    public void setProcessstatus(String processstatus) {
        this.processstatus = processstatus;
    }

    public String getProcessstartdate() {
        return processstartdate;
    }

    public void setProcessstartdate(String processstartdate) {
        this.processstartdate = processstartdate;
    }

    public String getCurrentsession() {
        return currentsession;
    }

    public void setCurrentsession(String currentsession) {
        this.currentsession = currentsession;
    }

    public String getCurrentexecutor() {
        return currentexecutor;
    }

    public void setCurrentexecutor(String currentexecutor) {
        this.currentexecutor = currentexecutor;
    }

    public String getProblemraiser() {
        return problemraiser;
    }

    public void setProblemraiser(String problemraiser) {
        this.problemraiser = problemraiser;
    }

    public String getQuestiontitle() {
        return questiontitle;
    }

    public void setQuestiontitle(String questiontitle) {
        this.questiontitle = questiontitle;
    }

    public String getQuestionnumber() {
        return questionnumber;
    }

    public void setQuestionnumber(String questionnumber) {
        this.questionnumber = questionnumber;
    }

    public String getQuestiondetails() {
        return questiondetails;
    }

    public void setQuestiondetails(String questiondetails) {
        this.questiondetails = questiondetails;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getProblemattributiondept() {
        return problemattributiondept;
    }

    public void setProblemattributiondept(String problemattributiondept) {
        this.problemattributiondept = problemattributiondept;
    }

    public String getPersonincharge() {
        return personincharge;
    }

    public void setPersonincharge(String personincharge) {
        this.personincharge = personincharge;
    }

    public String getQuestiontype() {
        return questiontype;
    }

    public void setQuestiontype(String questiontype) {
        this.questiontype = questiontype;
    }

    public String getIdentifytheproblem() {
        return identifytheproblem;
    }

    public void setIdentifytheproblem(String identifytheproblem) {
        this.identifytheproblem = identifytheproblem;
    }

    public String getDevelopmentleader() {
        return developmentleader;
    }

    public void setDevelopmentleader(String developmentleader) {
        this.developmentleader = developmentleader;
    }

    public String getUpdatetype() {
        return updatetype;
    }

    public void setUpdatetype(String updatetype) {
        this.updatetype = updatetype;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}