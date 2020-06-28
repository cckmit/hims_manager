/*
 * @ClassName JiraBasicInfoDO
 * @Description 
 * @version 1.0
 * @Date 2020-06-28 09:56:29
 */
package com.cmpay.lemon.monitor.entity;

import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;

@DataObject
public class JiraBasicInfoDO extends BaseDO {
    /**
     * @Fields jirakey jirakey
     */
    private String jirakey;
    /**
     * @Fields jiratype jira类型
     */
    private String jiratype;
    /**
     * @Fields epickey 关联epic
     */
    private String epickey;
    /**
     * @Fields parenttaskkey 关联父任务
     */
    private String parenttaskkey;
    /**
     * @Fields description 任务描述
     */
    private String description;
    /**
     * @Fields department 部门
     */
    private String department;
    /**
     * @Fields assignee 经办人
     */
    private String assignee;
    /**
     * @Fields timespent 花费时间
     */
    private String timespent;
    /**
     * @Fields aggregatetimespent 关联子任务总花费时间
     */
    private String aggregatetimespent;
    /**
     * @Fields planstarttime 计划开始时间
     */
    private String planstarttime;
    /**
     * @Fields planendtime 计划完成时间
     */
    private String planendtime;

    public String getJirakey() {
        return jirakey;
    }

    public void setJirakey(String jirakey) {
        this.jirakey = jirakey;
    }

    public String getJiratype() {
        return jiratype;
    }

    public void setJiratype(String jiratype) {
        this.jiratype = jiratype;
    }

    public String getEpickey() {
        return epickey;
    }

    public void setEpickey(String epickey) {
        this.epickey = epickey;
    }

    public String getParenttaskkey() {
        return parenttaskkey;
    }

    public void setParenttaskkey(String parenttaskkey) {
        this.parenttaskkey = parenttaskkey;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getTimespent() {
        return timespent;
    }

    public void setTimespent(String timespent) {
        this.timespent = timespent;
    }

    public String getAggregatetimespent() {
        return aggregatetimespent;
    }

    public void setAggregatetimespent(String aggregatetimespent) {
        this.aggregatetimespent = aggregatetimespent;
    }

    public String getPlanstarttime() {
        return planstarttime;
    }

    public void setPlanstarttime(String planstarttime) {
        this.planstarttime = planstarttime;
    }

    public String getPlanendtime() {
        return planendtime;
    }

    public void setPlanendtime(String planendtime) {
        this.planendtime = planendtime;
    }
}