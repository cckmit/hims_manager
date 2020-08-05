/*
 * @ClassName WorkingHoursDO
 * @Description
 * @version 1.0
 * @Date 2020-07-23 11:32:44
 */
package com.cmpay.lemon.monitor.entity;

import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;

import java.util.List;

@DataObject
public class WorkingHoursDO extends BaseDO {
    /**
     * @Fields jiraworklogkey 关联jira日志主键ID
     */
    private String jiraworklogkey;

    public String getSelectTime() {
        return selectTime;
    }

    public void setSelectTime(String selectTime) {
        this.selectTime = selectTime;
    }

    /**
     * @Fields issuekey 子任务key
     */
    private String issuekey;
    /**
     * @Fields timespnet 用时
     */
    private String timespnet;
    /**
     * @Fields subtaskname 子任务名称
     */
    private String subtaskname;
    /**
     * @Fields assignmentDepartment 任务归属部门
     */
    private String assignmentDepartment;
    /**
     * @Fields name 操作人name
     */
    private String name;
    /**
     * @Fields displayname 操作人中文名
     */
    private String displayname;
    /**
     * @Fields devpLeadDept 员工归属部门
     */
    private String devpLeadDept;
    /**
     * @Fields comment 工作备注
     */
    private String comment;
    /**
     * @Fields createdtime 录入时间
     */
    private String createdtime;
    /**
     * @Fields startedtime 日志开始时间
     */
    private String startedtime;
    /**
     * @Fields updatedtime 修改日志时间
     */
    private String updatedtime;
    // 总公时
    private String sumTime;
    private String selectTime;
    // 总部门人数
    private String sumDept;
    /**
     * @Fields epickey 关联epickey
     */
    private String epickey;
    /**
     * @Fields epiccreator epic创建者
     */
    private String epiccreator;
    /**
     * @Fields registerflag 是否已经登记标识
     */
    private String registerflag;
    /**
     * @Fields roletype 角色类型
     */
    private String roletype;
    /**
     * @Fields caseWritingNumber 案例编写数
     */
    private int caseWritingNumber;
    /**
     * @Fields caseExecutionNumber 案例执行数
     */
    private int caseExecutionNumber;
    /**
     * @Fields caseCompletedNumber 案例完成数
     */
    private int caseCompletedNumber;

    /**
     * @Fields 开始时间
     */
    private String startTime;
    /**
     * @Fields 结束时间
     */
    private String endTime;
    /*
每天天工作量
 */
    private List<String> listDay;

    public int getCaseWritingNumber() {
        return caseWritingNumber;
    }

    public void setCaseWritingNumber(int caseWritingNumber) {
        this.caseWritingNumber = caseWritingNumber;
    }

    public int getCaseExecutionNumber() {
        return caseExecutionNumber;
    }

    public String getJiraworklogkey() {
        return jiraworklogkey;
    }

    public void setJiraworklogkey(String jiraworklogkey) {
        this.jiraworklogkey = jiraworklogkey;
    }

    public String getIssuekey() {
        return issuekey;
    }

    public void setIssuekey(String issuekey) {
        this.issuekey = issuekey;
    }

    public String getTimespnet() {
        return timespnet;
    }

    public void setTimespnet(String timespnet) {
        this.timespnet = timespnet;
    }

    public String getSubtaskname() {
        return subtaskname;
    }

    public void setSubtaskname(String subtaskname) {
        this.subtaskname = subtaskname;
    }

    public String getAssignmentDepartment() {
        return assignmentDepartment;
    }

    public void setAssignmentDepartment(String assignmentDepartment) {
        this.assignmentDepartment = assignmentDepartment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getDevpLeadDept() {
        return devpLeadDept;
    }

    public void setDevpLeadDept(String devpLeadDept) {
        this.devpLeadDept = devpLeadDept;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(String createdtime) {
        this.createdtime = createdtime;
    }

    public String getStartedtime() {
        return startedtime;
    }

    public void setStartedtime(String startedtime) {
        this.startedtime = startedtime;
    }

    public String getUpdatedtime() {
        return updatedtime;
    }

    public void setUpdatedtime(String updatedtime) {
        this.updatedtime = updatedtime;
    }

    public String getSumTime() {
        return sumTime;
    }

    public void setSumTime(String sumTime) {
        this.sumTime = sumTime;
    }

    public String getSumDept() {
        return sumDept;
    }

    public void setSumDept(String sumDept) {
        this.sumDept = sumDept;
    }

    public String getEpickey() {
        return epickey;
    }

    public void setEpickey(String epickey) {
        this.epickey = epickey;
    }

    public String getEpiccreator() {
        return epiccreator;
    }

    public void setEpiccreator(String epiccreator) {
        this.epiccreator = epiccreator;
    }

    public String getRegisterflag() {
        return registerflag;
    }

    public void setRegisterflag(String registerflag) {
        this.registerflag = registerflag;
    }

    public String getRoletype() {
        return roletype;
    }

    public void setRoletype(String roletype) {
        this.roletype = roletype;
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

    public String getWorkHoursToString() {
        return "{'value': '" + getWorkHours(timespnet) + "', 'name': '" + roletype + "'}";
    }

    public Double getWorkHours(String value) {
        Long time = Long.parseLong(value);
        return (double) (Math.round(time * 100 / 28800) / 100.0);
    }

    public void setCaseExecutionNumber(int caseExecutionNumber) {
        this.caseExecutionNumber = caseExecutionNumber;
    }

    public int getCaseCompletedNumber() {
        return caseCompletedNumber;
    }

    public void setCaseCompletedNumber(int caseCompletedNumber) {
        this.caseCompletedNumber = caseCompletedNumber;

    }

    public List<String> getListDay() {
        return listDay;
    }

    public void setListDay(List<String> listDay) {
        this.listDay = listDay;
    }

    @Override
    public String toString() {
        return "WorkingHoursDO{" +
                "jiraworklogkey='" + jiraworklogkey + '\'' +
                ", issuekey='" + issuekey + '\'' +
                ", timespnet='" + timespnet + '\'' +
                ", subtaskname='" + subtaskname + '\'' +
                ", assignmentDepartment='" + assignmentDepartment + '\'' +
                ", name='" + name + '\'' +
                ", displayname='" + displayname + '\'' +
                ", devpLeadDept='" + devpLeadDept + '\'' +
                ", comment='" + comment + '\'' +
                ", createdtime='" + createdtime + '\'' +
                ", startedtime='" + startedtime + '\'' +
                ", updatedtime='" + updatedtime + '\'' +
                ", sumTime='" + sumTime + '\'' +
                ", selectTime='" + selectTime + '\'' +
                ", sumDept='" + sumDept + '\'' +
                ", epickey='" + epickey + '\'' +
                ", epiccreator='" + epiccreator + '\'' +
                ", registerflag='" + registerflag + '\'' +
                ", roletype='" + roletype + '\'' +
                ", caseWritingNumber=" + caseWritingNumber +
                ", caseExecutionNumber=" + caseExecutionNumber +
                ", caseCompletedNumber=" + caseCompletedNumber +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", listDay='" + listDay + '\'' +
                '}';
    }
}
