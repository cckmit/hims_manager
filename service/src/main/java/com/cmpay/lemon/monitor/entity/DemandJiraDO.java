/*
 * @ClassName DemandJiraDO
 * @Description 
 * @version 1.0
 * @Date 2019-10-11 17:11:20
 */
package com.cmpay.lemon.monitor.entity;

import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;

import java.time.LocalDateTime;

@DataObject
public class DemandJiraDO extends BaseDO {
    /**
     * @Fields reqInnerSeq 需求内部号
     */
    private String reqInnerSeq;
    /**
     * @Fields reqNm 需求名称
     */
    private String reqNm;
    /**
     * @Fields jiraId jiraId
     */
    private String jiraId;
    /**
     * @Fields jiraKey jiraKey
     */
    private String jiraKey;
    /**
     * @Fields issueType 任务类型
     */
    private String issueType;
    /**
     * @Fields assignmentDepartment 任务归属部门
     */
    private String assignmentDepartment;
    /**
     * @Fields createState 创建状态
     */
    private String createState;
    /**
     * @Fields remarks 备注
     */
    private String remarks;
    /**
     * @Fields creatTime 创建时间
     */
    private LocalDateTime creatTime;
    /**
     * @Fields relevanceEpic 关联Epic
     */
    private String relevanceEpic;

    public String getReqInnerSeq() {
        return reqInnerSeq;
    }

    public void setReqInnerSeq(String reqInnerSeq) {
        this.reqInnerSeq = reqInnerSeq;
    }

    public String getReqNm() {
        return reqNm;
    }

    public void setReqNm(String reqNm) {
        this.reqNm = reqNm;
    }

    public String getJiraId() {
        return jiraId;
    }

    public void setJiraId(String jiraId) {
        this.jiraId = jiraId;
    }

    public String getJiraKey() {
        return jiraKey;
    }

    public void setJiraKey(String jiraKey) {
        this.jiraKey = jiraKey;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getAssignmentDepartment() {
        return assignmentDepartment;
    }

    public void setAssignmentDepartment(String assignmentDepartment) {
        this.assignmentDepartment = assignmentDepartment;
    }

    public String getCreateState() {
        return createState;
    }

    public void setCreateState(String createState) {
        this.createState = createState;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public LocalDateTime getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(LocalDateTime creatTime) {
        this.creatTime = creatTime;
    }

    public String getRelevanceEpic() {
        return relevanceEpic;
    }

    public void setRelevanceEpic(String relevanceEpic) {
        this.relevanceEpic = relevanceEpic;
    }
}