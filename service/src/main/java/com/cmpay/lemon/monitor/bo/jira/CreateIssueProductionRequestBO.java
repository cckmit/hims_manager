package com.cmpay.lemon.monitor.bo.jira;

/**
 * 投产问题
 */
public class CreateIssueProductionRequestBO extends CreateIssueRequestBO{
    /**
     * 项目
     */
    private int project;
    /**
     * 问题类型
     */
    private int issuetype;
    /**
     * 创建时间
     */
    private String customfield_10404;
    /**
     * 问题提出人
     */
    private String customfield_10207;
    /**
     * 投产编号
     */
    private String customfield_10400;
    /**
     * 问题描述
     */
    private String  customfield_10403;
    /**
     * 经办人
     */
    private String  assignee;
    /**
     * 概要
     */
    private String  summary;

    public int getProject() {
        return project;
    }

    public void setProject(int project) {
        this.project = project;
    }

    public int getIssuetype() {
        return issuetype;
    }

    public void setIssuetype(int issuetype) {
        this.issuetype = issuetype;
    }

    public String getCustomfield_10404() {
        return customfield_10404;
    }

    public void setCustomfield_10404(String customfield_10404) {
        this.customfield_10404 = customfield_10404;
    }

    public String getCustomfield_10207() {
        return customfield_10207;
    }

    public void setCustomfield_10207(String customfield_10207) {
        this.customfield_10207 = customfield_10207;
    }

    public String getCustomfield_10400() {
        return customfield_10400;
    }

    public void setCustomfield_10400(String customfield_10400) {
        this.customfield_10400 = customfield_10400;
    }

    public String getCustomfield_10403() {
        return customfield_10403;
    }

    public void setCustomfield_10403(String customfield_10403) {
        this.customfield_10403 = customfield_10403;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public String toString() {
        return "{\n" +
                "    \"fields\":{\n" +
                "        \"summary\":\""+this.getSummary()+"\",\n" +
                "        \"issuetype\":{\n" +
                "            \"id\":\""+this.getIssuetype()+"\"\n" +
                "        },\n" +
                "        \"project\":{\n" +
                "            \"id\":\""+this.getProject()+"\"\n" +
                "        },\n" +
                "        \"customfield_10207\":{\n" +
                "            \"name\":\""+this.getCustomfield_10207()+"\"\n" +
                "        },\n" +
                "        \"customfield_10403\":\""+this.getCustomfield_10403().replaceAll("\r|\n", "")+"\",\n" +
                "        \"customfield_10400\":\""+this.getCustomfield_10400()+"\",\n" +
                "         \"assignee\": {\n" +
                "         \t\"name\":\""+this.getAssignee()+"\"\n" +
                "         }\n" +
                "\t}\n" +
                "}";
    }
}
