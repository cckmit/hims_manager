package com.cmpay.lemon.monitor.bo.jira;

public class JiraTaskBodyBO {
    /**
     * /ira任务编号  jiraKey
     */
    private String jiraKey;
    /**
     * 内部需求编号
     */
    private String reqInnerSeq;
    /*
     *经办人
     */
    String assignee;
    /*
     *计划开始时间
     */
    String planStartTime;
    /*
     *计划完成时间
     */
    String planEndTime;

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

    public String  getEditTestMainTaskBody(){

        return "{\n" +
                "\"fields\": {\n" +
                "\t \"assignee\": {\n" +
                "\t \"name\":\""+this.getAssignee()+"\"\n" +
                "\t },\n" +
                "    \"customfield_10186\":\""+  this.getPlanStartTime()+"\",\n" +
                "    \"customfield_10187\":\""+  this.getPlanEndTime()+"\"\n" +
                "  }\n" +
                "}";
    }


}
