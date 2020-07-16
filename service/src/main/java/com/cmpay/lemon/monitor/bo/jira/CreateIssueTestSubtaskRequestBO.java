package com.cmpay.lemon.monitor.bo.jira;

/**
 * Copyright 2019 bejson.com
 */

/**
 * Auto-generated: 2019-09-02 9:5:10
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class CreateIssueTestSubtaskRequestBO extends CreateIssueRequestBO {

    /**
     * 项目
     */
    private int project;
    /**
     * 问题名
     */
    private String summary;
    /**
    * 问题类型
    */
    private int issueType;
    /**
     * 父任务key
     */
    private String parentKey;

    /**
     * 问题描述
     */
    private String description;

    /**
     * 经办人
     */
    private String  assignee;

    /**
     * 开始时间
     */
    private String  startTime;

    /**
     * 结束时间
     */
    private String  endTime;


    public int getProject() {
        return project;
    }

    public void setProject(int project) {
        this.project = project;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getIssueType() {
        return issueType;
    }

    public void setIssueType(int issueType) {
        this.issueType = issueType;
    }

    public String getParentKey() {
        return parentKey;
    }

    public void setParentKey(String parentKey) {
        this.parentKey = parentKey;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
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

    @Override
    public String toString() {
        return "{\n" +
                "    \"fields\":{\n" +
                "        \"parent\":{\n" +
                "            \"key\":\""+this.getParentKey()+"\"\n" +
                "        },\n" +
                "        \"summary\":\""+this.getSummary()+"\",\n" +
                "        \"issuetype\":{\n" +
                "            \"id\":\""+this.getIssueType()+"\"\n" +
                "        },\n" +
                "        \"project\":{\n" +
                "            \"id\":\""+this.getProject()+"\"\n" +
                "        },\n" +
                "        \"description\":\""+this.getDescription().replaceAll("\r|\n", "")+"\",\n" +
                "        \n" +
                "         \"assignee\": {\n" +
                "         \t\"name\":\""+this.getAssignee()+"\"\n" +
                "         },\n" +
                "          \"customfield_10252\":\""+this.getStartTime()+"\",\n" +
                "          \"customfield_10253\":\""+this.getEndTime()+"\"\n" +
                "\t}\n" +
                "}";
    }
}