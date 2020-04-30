package com.cmpay.lemon.monitor.bo.jira;

public class JiraWorklogBO {

    /**
     * @Fields issueid 日志key
     */
    private String jiraWorklogKey;
    /**
     * @Fields issueid 问题ID
     */
    private String issueid;
    /**
     * @Fields issuekey 问题key
     */
    private String issuekey;
    /**
     * @Fields timespnet 用时
     */
    private String timespnet;
    /**
     * @Fields name 操作人name
     */
    private String name;
    /**
     * @Fields displayname 操作人中文名
     */
    private String displayname;
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


    public String getIssueid() {
        return issueid;
    }

    public void setIssueid(String issueid) {
        this.issueid = issueid;
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

    public String getJiraWorklogKey() {
        return jiraWorklogKey;
    }

    public void setJiraWorklogKey(String jiraWorklogKey) {
        this.jiraWorklogKey = jiraWorklogKey;
    }
}
