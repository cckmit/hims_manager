/*
 * @ClassName ZenQuestiontDO
 * @Description
 * @version 1.0
 * @Date 2020-09-04 11:30:37
 */
package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.request.GenericDTO;

public class ZenQuestiontReqDTO extends GenericDTO {
    /**
     *  bugnumber BUG编号
     */
    private String bugnumber;
    /**
     *  belongproducts 所属产品
     */
    private String belongproducts;
    /**
     *  belongmodule 所属模块
     */
    private String belongmodule;
    /**
     *  belongproject 所属项目
     */
    private String belongproject;
    /**
     *  relateddemand 相关需求
     */
    private String relateddemand;
    /**
     *  relatedtask 相关任务
     */
    private String relatedtask;
    /**
     *  bugtitle Bug标题
     */
    private String bugtitle;
    /**
     *  keyword 关键词
     */
    private String keyword;
    /**
     *  severity 严重程度
     */
    private String severity;
    /**
     *  priority 优先级
     */
    private String priority;
    /**
     *  bugtype Bug类型
     */
    private String bugtype;
    /**
     *  operatingsystem 操作系统
     */
    private String operatingsystem;
    /**
     *  browser 浏览器
     */
    private String browser;
    /**
     *  repeatsteps 重现步骤
     */
    private String repeatsteps;
    /**
     *  bugstatus Bug状态
     */
    private String bugstatus;
    /**
     *  expirationdate 截止日期
     */
    private String expirationdate;
    /**
     *  activatenumber 激活次数
     */
    private String activatenumber;
    /**
     * whetherconfirm 是否确认
     */
    private String whetherconfirm;
    /**
     *  carboncopy 抄送给
     */
    private String carboncopy;
    /**
     *  creator 由谁创建
     */
    private String creator;
    /**
     *  createddate 创建日期
     */
    private String createddate;
    /**
     *  affectsversion 影响版本
     */
    private String affectsversion;
    /**
     *  assigned 指派给
     */
    private String assigned;
    /**
     *  assigneddate 指派日期
     */
    private String assigneddate;
    /**
     *  solver 解决者
     */
    private String solver;
    /**
     *  solution 解决方案
     */
    private String solution;
    /**
     *  solveversion 解决版本
     */
    private String solveversion;
    /**
     *  solvedate 解决日期
     */
    private String solvedate;
    /**
     *  shutperson 由谁关闭
     */
    private String shutperson;
    /**
     *  shutdate 关闭日期
     */
    private String shutdate;
    /**
     *  repetitionid 重复ID
     */
    private String repetitionid;
    /**
     *  relatedbug 相关Bug
     */
    private String relatedbug;
    /**
     *  relatedcase 相关用例
     */
    private String relatedcase;
    /**
     *  lastreviser 最后修改者
     */
    private String lastreviser;
    /**
     *  changeddate 修改日期
     */
    private String changeddate;
    /**
     *  accessory 附件
     */
    private String accessory;
    /**
     *  secondlevelorganization 二级主导团队
     */
    private String secondlevelorganization;
    private String startTime;
    private String endTime;
    private String reqImplMon;
    private String firstLevelOrganization;
    /**
     * 页数
     */
    private int pageNum;
    /**
     * 页面大小
     */
    private int pageSize;


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
        return "ZenQuestiontReqDTO{" +
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
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }
}
