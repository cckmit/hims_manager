package com.cmpay.lemon.monitor.bo.jira;

import com.cmpay.lemon.common.utils.JudgeUtils;

public class JiraTaskBodyBO {
    /**
     * jira任务编号  jiraKey
     */
    private String jiraKey;

    /**
     * 创建者  creator
     */
    String creator;

    /**
     * 关联创建者  epicCreator
     */
    String epicCreator;
    /**
     * jira任务编号  parentTaskKey
     */
    private String parentTaskKey;
    /**
     * jira任务编号  EpicKey
     */
    private String epicKey;
    /**
     * jira任务归属部门  department
     */
    private String department;

    /**
     * jira任务类型  jiraType
     */
    private String jiraType;
    /**
     * jira任务名  issueName
     */
    private String  issueName;
    /**
     * jira任务及关联子任务总用时  aggregatetimespent
     */
    private String aggregatetimespent;
    /**
     * jira任务用时  timespent
     */
    private String timespent;
    /**
     * 内部需求编号
     */
    private String reqInnerSeq;
    /*
     *经办人
     */
    String assignee;

    /*
     *工作日志流水
     */
    String worklogs;

    /*
     *工作日志流水
     */
    String subtasks;

    /*
     *计划开始时间
     */
    String planStartTime;
    /*
     *计划完成时间
     */
    String planEndTime;

    public String getEpicCreator() {
        return epicCreator;
    }

    public void setEpicCreator(String epicCreator) {
        this.epicCreator = epicCreator;
    }

    public String getParentTaskKey() {
        return parentTaskKey;
    }

    public void setParentTaskKey(String parentTaskKey) {
        this.parentTaskKey = parentTaskKey;
    }

    public String getEpicKey() {
        return epicKey;
    }

    public void setEpicKey(String epicKey) {
        this.epicKey = epicKey;
    }

    public String getAggregatetimespent() {
        return aggregatetimespent;
    }

    public void setAggregatetimespent(String aggregatetimespent) {
        this.aggregatetimespent = aggregatetimespent;
    }

    public String getJiraKey() {
        return jiraKey;
    }

    public void setJiraKey(String jiraKey) {
        this.jiraKey = jiraKey;
    }

    public String getReqInnerSeq() {
        return reqInnerSeq;
    }

    public void setReqInnerSeq(String reqInnerSeq) {
        this.reqInnerSeq = reqInnerSeq;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getPlanStartTime() {
        return planStartTime;
    }

    public void setPlanStartTime(String planStartTime) {
        this.planStartTime = planStartTime;
    }

    public String getPlanEndTime() {
        return planEndTime;
    }

    public void setPlanEndTime(String planEndTime) {
        this.planEndTime = planEndTime;
    }


    public String getJiraType() {
        return jiraType;
    }

    public void setJiraType(String jiraType) {
        this.jiraType = jiraType;
    }

    public String getTimespent() {
        return timespent;
    }

    public void setTimespent(String timespent) {
        this.timespent = timespent;
    }

    public String getWorklogs() {
        return worklogs;
    }

    public void setWorklogs(String worklogs) {
        this.worklogs = worklogs;
    }

    public String getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(String subtasks) {
        this.subtasks = subtasks;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getIssueName() {
        return issueName;
    }

    public void setIssueName(String issueName) {
        this.issueName = issueName;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String  getEditTestMainTaskBody(){

        return
                "{\n" +
                "\"fields\": {\n" +
                "\t \"assignee\": {\n" +
                "\t \"name\":\""+this.getAssignee()+"\"\n" +
                "\t }"+
                        this.getCustomfield_10252()+
                        this.getCustomfield_10253()+
                "  }\n" +
                "}";
    }

    public String  getCustomfield_10252(){
        if(JudgeUtils.isNotBlank(this.getPlanStartTime())) {
            return ",\n"  +
                    "\"customfield_10252\":\"" + this.getPlanStartTime() + "\"";
        }
        return "";
    }
    public String  getCustomfield_10253(){
        if(JudgeUtils.isNotBlank(this.getPlanStartTime())) {
            return  ",\n" +
                    "\"customfield_10253\":\""+  this.getPlanEndTime()+"\"\n";
        }
        return "";
    }

    @Override
    public String toString() {
        return "JiraTaskBodyBO{" +
                "jiraKey='" + jiraKey + '\'' +
                ", parentTaskKey='" + parentTaskKey + '\'' +
                ", epicKey='" + epicKey + '\'' +
                ", department='" + department + '\'' +
                ", jiraType='" + jiraType + '\'' +
                ", issueName='" + issueName + '\'' +
                ", aggregatetimespent='" + aggregatetimespent + '\'' +
                ", timespent='" + timespent + '\'' +
                ", reqInnerSeq='" + reqInnerSeq + '\'' +
                ", assignee='" + assignee + '\'' +
                ", worklogs='" + worklogs + '\'' +
                ", subtasks='" + subtasks + '\'' +
                ", planStartTime='" + planStartTime + '\'' +
                ", planEndTime='" + planEndTime + '\'' +
                '}';
    }
}
