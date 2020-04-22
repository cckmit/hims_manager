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
public class CreateIssueTestMainTaskRequestBO extends CreateIssueRequestBO {
    /**
     * 问题名
     */
    private String summary;
    /**
    * 问题类型
    */
    private int issueType;
    /**
     * 关联EpicKey
     */
    private String EpicKey;
    /**
     * 内部需求编号
     */
    private String reqInnerSeq;
    /**
     * 项目
     */
    private int project;
    /**
     * 问题描述
     */
    private String description;
    /**
     * 开发主导部门
     */
    private String  devpLeadDept;
    /**
     * 经办人
     */
    private String  Manager;

    public String getEpicKey() {
        return EpicKey;
    }

    public void setEpicKey(String epicKey) {
        EpicKey = epicKey;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
    public String getSummary() {
        return summary;
    }

    public int getIssueType() {
        return issueType;
    }

    public int getProject() {
        return project;
    }

    public void setProject(int project) {
        this.project = project;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }

    public void setIssueType(int issueType) {
        this.issueType = issueType;
    }

    public String getDevpLeadDept() {
        return devpLeadDept;
    }

    public void setDevpLeadDept(String devpLeadDept) {
        this.devpLeadDept = devpLeadDept;
    }

    public String getManager() {
        return Manager;
    }

    public void setManager(String manager) {
        Manager = manager;
    }

    public String getReqInnerSeq() {
        return reqInnerSeq;
    }

    public void setReqInnerSeq(String reqInnerSeq) {
        this.reqInnerSeq = reqInnerSeq;
    }

    @Override
    public String toString() {
        return "{\n" +
                "    \"fields\":{\n" +
                "        \"summary\":\""+this.getSummary()+"\",\n" +
                "        \"issuetype\":{\n" +
                "            \"id\":\""+this.getIssueType()+"\"\n" +
                "        },\n" +
                "        \"project\":{\n" +
                "            \"id\":\""+this.getProject()+"\"\n" +
                "        },\n" +
                "         \""+this.JIRA_EPICLINK+"\": \""+this.getEpicKey()+"\",\n" +
                "        \n" +
                "        \"description\":\""+this.getDescription().replaceAll("\r|\n", "")+"\",\n" +
                "        \n" +
                "         \"assignee\": {\n" +
                "         \t\"name\":\""+this.getManager()+"\"\n" +
                "         }\n" +
                "\t}\n" +
                "}";
    }
}