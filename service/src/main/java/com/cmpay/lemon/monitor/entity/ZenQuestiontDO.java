/*
 * @ClassName ZenQuestiontDO
 * @Description
 * @version 1.0
 * @Date 2020-09-07 10:14:44
 */
package com.cmpay.lemon.monitor.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;

@DataObject
public class ZenQuestiontDO extends BaseDO {
    /**
     * @Fields bugnumber BUG编号
     */
    @Excel(name = "BUG编号")
    private String bugnumber;
    /**
     * @Fields belongproducts 所属产品
     */
    @Excel(name = "所属产品")
    private String belongproducts;
    /**
     * @Fields belongmodule 所属模块
     */
    @Excel(name = "所属模块")
    private String belongmodule;
    /**
     * @Fields belongproject 所属项目
     */
    @Excel(name = "所属项目")
    private String belongproject;
    /**
     * @Fields relateddemand 相关需求
     */
    @Excel(name = "相关需求")
    private String relateddemand;
    /**
     * @Fields relatedtask 相关任务
     */
    @Excel(name = "相关任务")
    private String relatedtask;
    /**
     * @Fields bugtitle Bug标题
     */
    @Excel(name = "Bug标题")
    private String bugtitle;
    /**
     * @Fields keyword 关键词
     */
    @Excel(name = "关键词")
    private String keyword;
    /**
     * @Fields severity 严重程度
     */
    @Excel(name = "严重程度")
    private String severity;
    /**
     * @Fields priority 优先级
     */
    @Excel(name = "优先级")
    private String priority;
    /**
     * @Fields bugtype Bug类型
     */
    @Excel(name = "Bug类型")
    private String bugtype;
    /**
     * @Fields operatingsystem 操作系统
     */
    @Excel(name = "操作系统")
    private String operatingsystem;
    /**
     * @Fields browser 浏览器
     */
    @Excel(name = "浏览器")
    private String browser;
    /**
     * @Fields repeatsteps 重现步骤
     */
    @Excel(name = "重现步骤")
    private String repeatsteps;
    /**
     * @Fields bugstatus Bug状态
     */
    @Excel(name = "Bug状态")
    private String bugstatus;
    /**
     * @Fields expirationdate 截止日期
     */
    @Excel(name = "截止日期")
    private String expirationdate;
    /**
     * @Fields activatenumber 激活次数
     */
    @Excel(name = "激活次数")
    private String activatenumber;
    /**
     * @Fields whetherconfirm 是否确认
     */
    @Excel(name = "是否确认")
    private String whetherconfirm;
    /**
     * @Fields carboncopy 抄送给
     */
    @Excel(name = "抄送给")
    private String carboncopy;
    /**
     * @Fields creator 由谁创建
     */
    @Excel(name = "由谁创建")
    private String creator;
    /**
     * @Fields createddate 创建日期
     */
    @Excel(name = "创建日期")
    private String createddate;
    /**
     * @Fields affectsversion 影响版本
     */
    @Excel(name = "影响版本")
    private String affectsversion;
    /**
     * @Fields assigned 指派给
     */
    @Excel(name = "指派给")
    private String assigned;
    /**
     * @Fields assigneddate 指派日期
     */
    @Excel(name = "指派日期")
    private String assigneddate;
    /**
     * @Fields solver 解决者
     */
    @Excel(name = "解决者")
    private String solver;
    /**
     * @Fields solution 解决方案
     */
    @Excel(name = "解决方案")
    private String solution;
    /**
     * @Fields solveversion 解决版本
     */
    @Excel(name = "解决版本")
    private String solveversion;
    /**
     * @Fields solvedate 解决日期
     */
    @Excel(name = "解决日期")
    private String solvedate;
    /**
     * @Fields shutperson 由谁关闭
     */
    @Excel(name = "由谁关闭")
    private String shutperson;
    /**
     * @Fields shutdate 关闭日期
     */
    @Excel(name = "关闭日期")
    private String shutdate;
    /**
     * @Fields repetitionid 重复ID
     */
    @Excel(name = "重复ID")
    private String repetitionid;
    /**
     * @Fields relatedbug 相关Bug
     */
    @Excel(name = "相关Bug")
    private String relatedbug;
    /**
     * @Fields relatedcase 相关用例
     */
    @Excel(name = "相关用例")
    private String relatedcase;
    /**
     * @Fields lastreviser 最后修改者
     */
    @Excel(name = "最后修改者")
    private String lastreviser;
    /**
     * @Fields changeddate 修改日期
     */
    @Excel(name = "修改日期")
    private String changeddate;
    /**
     * @Fields accessory 附件
     */
    @Excel(name = "附件")
    private String accessory;
    /**
     * @Fields secondlevelorganization 二级主导部门
     */
    @Excel(name = "归属二级团队")
    private String secondlevelorganization;
    private String startTime;
    private String endTime;
    private String reqImplMon;
    @Excel(name = "归属一级团队")
    private String firstLevelOrganization;
    private boolean isTest;

    public boolean isTest() {
        return isTest;
    }

    public void setTest(boolean test) {
        isTest = test;
    }

    public String getBugnumber() {
        return bugnumber;
    }

    public void setBugnumber(String bugnumber) {
        this.bugnumber = bugnumber;
    }

    public String getBelongproducts() {
        return belongproducts;
    }

    public void setBelongproducts(String belongproducts) {
        this.belongproducts = belongproducts;
    }

    public String getBelongmodule() {
        return belongmodule;
    }

    public void setBelongmodule(String belongmodule) {
        this.belongmodule = belongmodule;
    }

    public String getBelongproject() {
        return belongproject;
    }

    public void setBelongproject(String belongproject) {
        this.belongproject = belongproject;
    }

    public String getRelateddemand() {
        return relateddemand;
    }

    public void setRelateddemand(String relateddemand) {
        this.relateddemand = relateddemand;
    }

    public String getRelatedtask() {
        return relatedtask;
    }

    public void setRelatedtask(String relatedtask) {
        this.relatedtask = relatedtask;
    }

    public String getBugtitle() {
        return bugtitle;
    }

    public void setBugtitle(String bugtitle) {
        this.bugtitle = bugtitle;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getBugtype() {
        return bugtype;
    }

    public void setBugtype(String bugtype) {
        this.bugtype = bugtype;
    }

    public String getOperatingsystem() {
        return operatingsystem;
    }

    public void setOperatingsystem(String operatingsystem) {
        this.operatingsystem = operatingsystem;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getRepeatsteps() {
        return repeatsteps;
    }

    public void setRepeatsteps(String repeatsteps) {
        this.repeatsteps = repeatsteps;
    }

    public String getBugstatus() {
        return bugstatus;
    }

    public void setBugstatus(String bugstatus) {
        this.bugstatus = bugstatus;
    }

    public String getExpirationdate() {
        return expirationdate;
    }

    public void setExpirationdate(String expirationdate) {
        this.expirationdate = expirationdate;
    }

    public String getActivatenumber() {
        return activatenumber;
    }

    public void setActivatenumber(String activatenumber) {
        this.activatenumber = activatenumber;
    }

    public String getWhetherconfirm() {
        return whetherconfirm;
    }

    public void setWhetherconfirm(String whetherconfirm) {
        this.whetherconfirm = whetherconfirm;
    }

    public String getCarboncopy() {
        return carboncopy;
    }

    public void setCarboncopy(String carboncopy) {
        this.carboncopy = carboncopy;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreateddate() {
        return createddate;
    }

    public void setCreateddate(String createddate) {
        this.createddate = createddate;
    }

    public String getAffectsversion() {
        return affectsversion;
    }

    public void setAffectsversion(String affectsversion) {
        this.affectsversion = affectsversion;
    }

    public String getAssigned() {
        return assigned;
    }

    public void setAssigned(String assigned) {
        this.assigned = assigned;
    }

    public String getAssigneddate() {
        return assigneddate;
    }

    public void setAssigneddate(String assigneddate) {
        this.assigneddate = assigneddate;
    }

    public String getSolver() {
        return solver;
    }

    public void setSolver(String solver) {
        this.solver = solver;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getSolveversion() {
        return solveversion;
    }

    public void setSolveversion(String solveversion) {
        this.solveversion = solveversion;
    }

    public String getSolvedate() {
        return solvedate;
    }

    public void setSolvedate(String solvedate) {
        this.solvedate = solvedate;
    }

    public String getShutperson() {
        return shutperson;
    }

    public void setShutperson(String shutperson) {
        this.shutperson = shutperson;
    }

    public String getShutdate() {
        return shutdate;
    }

    public void setShutdate(String shutdate) {
        this.shutdate = shutdate;
    }

    public String getRepetitionid() {
        return repetitionid;
    }

    public void setRepetitionid(String repetitionid) {
        this.repetitionid = repetitionid;
    }

    public String getRelatedbug() {
        return relatedbug;
    }

    public void setRelatedbug(String relatedbug) {
        this.relatedbug = relatedbug;
    }

    public String getRelatedcase() {
        return relatedcase;
    }

    public void setRelatedcase(String relatedcase) {
        this.relatedcase = relatedcase;
    }

    public String getLastreviser() {
        return lastreviser;
    }

    public void setLastreviser(String lastreviser) {
        this.lastreviser = lastreviser;
    }

    public String getChangeddate() {
        return changeddate;
    }

    public void setChangeddate(String changeddate) {
        this.changeddate = changeddate;
    }

    public String getAccessory() {
        return accessory;
    }

    public void setAccessory(String accessory) {
        this.accessory = accessory;
    }

    public String getSecondlevelorganization() {
        return secondlevelorganization;
    }

    public void setSecondlevelorganization(String secondlevelorganization) {
        this.secondlevelorganization = secondlevelorganization;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getReqImplMon() {
        return reqImplMon;
    }

    public void setReqImplMon(String reqImplMon) {
        this.reqImplMon = reqImplMon;
    }

    public String getFirstLevelOrganization() {
        return firstLevelOrganization;
    }

    public void setFirstLevelOrganization(String firstLevelOrganization) {
        this.firstLevelOrganization = firstLevelOrganization;
    }

    @Override
    public String toString() {
        return "ZenQuestiontDO{" +
                "bugnumber='" + bugnumber + '\'' +
                ", belongproducts='" + belongproducts + '\'' +
                ", belongmodule='" + belongmodule + '\'' +
                ", belongproject='" + belongproject + '\'' +
                ", relateddemand='" + relateddemand + '\'' +
                ", relatedtask='" + relatedtask + '\'' +
                ", bugtitle='" + bugtitle + '\'' +
                ", keyword='" + keyword + '\'' +
                ", severity='" + severity + '\'' +
                ", priority='" + priority + '\'' +
                ", bugtype='" + bugtype + '\'' +
                ", operatingsystem='" + operatingsystem + '\'' +
                ", browser='" + browser + '\'' +
                ", repeatsteps='" + repeatsteps + '\'' +
                ", bugstatus='" + bugstatus + '\'' +
                ", expirationdate='" + expirationdate + '\'' +
                ", activatenumber='" + activatenumber + '\'' +
                ", whetherconfirm='" + whetherconfirm + '\'' +
                ", carboncopy='" + carboncopy + '\'' +
                ", creator='" + creator + '\'' +
                ", createddate='" + createddate + '\'' +
                ", affectsversion='" + affectsversion + '\'' +
                ", assigned='" + assigned + '\'' +
                ", assigneddate='" + assigneddate + '\'' +
                ", solver='" + solver + '\'' +
                ", solution='" + solution + '\'' +
                ", solveversion='" + solveversion + '\'' +
                ", solvedate='" + solvedate + '\'' +
                ", shutperson='" + shutperson + '\'' +
                ", shutdate='" + shutdate + '\'' +
                ", repetitionid='" + repetitionid + '\'' +
                ", relatedbug='" + relatedbug + '\'' +
                ", relatedcase='" + relatedcase + '\'' +
                ", lastreviser='" + lastreviser + '\'' +
                ", changeddate='" + changeddate + '\'' +
                ", accessory='" + accessory + '\'' +
                ", secondlevelorganization='" + secondlevelorganization + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", reqImplMon='" + reqImplMon + '\'' +
                ", firstLevelOrganization='" + firstLevelOrganization + '\'' +
                ", isTest=" + isTest +
                '}';
    }
}
