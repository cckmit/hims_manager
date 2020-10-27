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
     * 创建时间  createTime
     */
    String createTime;

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
     * jira任务状态  status
     */
    private String status;

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
    private String assignee;

    /*
     *工作日志流水
     */
    private String worklogs;

    /*
     *工作日志流水
     */
    private String subtasks;

    /*
     *计划开始时间
     */
    private String planStartTime;
    /*
     *计划完成时间
     */
    private String planEndTime;
    /*
     *问题处理人
     */
    private String problemHandlers;
    /*
     *重测次数
     */
    private int retestTimes;
    /*
     *问题定位
     */
    private String problemType;
    /*
     *缺陷归属部门
     */
    private String defectsDepartment;
    /*
     *缺陷名称
     */
    private String defectName;
    /*
     *缺陷详情
     */
    private String defectDetails;

    /*
     *评审问题类型
     */
    private String reviewQuestionType;
    /*
     *安全级别
     */
    private String securityLevel;
    /*
     *测试案例总数
     */
    private String testCaseNumber;
    /**
     * 解决结果
     */
    private String solution;
    /**
     * @Fields problemHandler 问题处理经办人
     */
    private String problemHandler;
    /**
     * @Fields problemHandlerDepartment 经办人归属部门
     */
    private String problemHandlerDepartment;

    // 投产问题提出人
    private String productionIssueRegistrant;

    //投产问题编号
    private String proNumber;

    public String getProductionIssueRegistrant() {
        return productionIssueRegistrant;
    }

    public void setProductionIssueRegistrant(String productionIssueRegistrant) {
        this.productionIssueRegistrant = productionIssueRegistrant;
    }

    public String getProNumber() {
        return proNumber;
    }

    public void setProNumber(String proNumber) {
        this.proNumber = proNumber;
    }

    public String getTestCaseNumber() {
        return testCaseNumber;
    }

    public void setTestCaseNumber(String testCaseNumber) {
        this.testCaseNumber = testCaseNumber;
    }

    public String getSecurityLevel() {
        return securityLevel;
    }

    public void setSecurityLevel(String securityLevel) {
        this.securityLevel = securityLevel;
    }

    public String getReviewQuestionType() {
        return reviewQuestionType;
    }

    public void setReviewQuestionType(String reviewQuestionType) {
        this.reviewQuestionType = reviewQuestionType;
    }

    public String getDefectDetails() {
        return defectDetails;
    }

    public void setDefectDetails(String defectDetails) {
        this.defectDetails = defectDetails;
    }

    public String getDefectName() {
        return defectName;
    }

    public void setDefectName(String defectName) {
        this.defectName = defectName;
    }

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

    public String getProblemHandler() {
        return problemHandler;
    }

    public void setProblemHandler(String problemHandler) {
        this.problemHandler = problemHandler;
    }

    public int getRetestTimes() {
        return retestTimes;
    }

    public void setRetestTimes(int retestTimes) {
        this.retestTimes = retestTimes;
    }

    public String getProblemType() {
        return problemType;
    }

    public void setProblemType(String problemType) {
        this.problemType = problemType;
    }

    public String getDefectsDepartment() {
        return defectsDepartment;
    }

    public void setDefectsDepartment(String defectsDepartment) {
        this.defectsDepartment = defectsDepartment;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProblemHandlers() {
        return problemHandlers;
    }

    public void setProblemHandlers(String problemHandlers) {
        this.problemHandlers = problemHandlers;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getProblemHandlerDepartment() {
        return problemHandlerDepartment;
    }

    public void setProblemHandlerDepartment(String problemHandlerDepartment) {
        this.problemHandlerDepartment = problemHandlerDepartment;
    }

    @Override
    public String toString() {
        return "JiraTaskBodyBO{" +
                "jiraKey='" + jiraKey + '\'' +
                ", creator='" + creator + '\'' +
                ", createTime='" + createTime + '\'' +
                ", epicCreator='" + epicCreator + '\'' +
                ", parentTaskKey='" + parentTaskKey + '\'' +
                ", epicKey='" + epicKey + '\'' +
                ", department='" + department + '\'' +
                ", jiraType='" + jiraType + '\'' +
                ", status='" + status + '\'' +
                ", issueName='" + issueName + '\'' +
                ", aggregatetimespent='" + aggregatetimespent + '\'' +
                ", timespent='" + timespent + '\'' +
                ", reqInnerSeq='" + reqInnerSeq + '\'' +
                ", assignee='" + assignee + '\'' +
                ", worklogs='" + worklogs + '\'' +
                ", subtasks='" + subtasks + '\'' +
                ", planStartTime='" + planStartTime + '\'' +
                ", planEndTime='" + planEndTime + '\'' +
                ", problemHandlers='" + problemHandlers + '\'' +
                ", retestTimes=" + retestTimes +
                ", problemType='" + problemType + '\'' +
                ", defectsDepartment='" + defectsDepartment + '\'' +
                ", defectName='" + defectName + '\'' +
                ", defectDetails='" + defectDetails + '\'' +
                ", reviewQuestionType='" + reviewQuestionType + '\'' +
                ", securityLevel='" + securityLevel + '\'' +
                ", testCaseNumber='" + testCaseNumber + '\'' +
                ", solution='" + solution + '\'' +
                ", problemHandler='" + problemHandler + '\'' +
                ", problemHandlerDepartment='" + problemHandlerDepartment + '\'' +
                ", productionIssueRegistrant='" + productionIssueRegistrant + '\'' +
                ", proNumber='" + proNumber + '\'' +
                '}';
    }
}
