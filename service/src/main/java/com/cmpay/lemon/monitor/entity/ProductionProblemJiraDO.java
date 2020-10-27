/*
 * @ClassName ProductionProblemJiraDO
 * @Description
 * @version 1.0
 * @Date 2020-10-26 16:31:27
 */
package com.cmpay.lemon.monitor.entity;

import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;

import java.time.LocalDateTime;

@DataObject
public class ProductionProblemJiraDO extends BaseDO {
    /**
     * @Fields problemSerialNumber 投产问题id
     */
    private String problemSerialNumber;
    /**
     * @Fields proNumber 投产编号
     */
    private String proNumber;
    /**
     * @Fields proNeed 投产名称
     */
    private String proNeed;
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

    public String getProblemSerialNumber() {
        return problemSerialNumber;
    }

    public void setProblemSerialNumber(String problemSerialNumber) {
        this.problemSerialNumber = problemSerialNumber;
    }

    public String getProNumber() {
        return proNumber;
    }

    public void setProNumber(String proNumber) {
        this.proNumber = proNumber;
    }

    public String getProNeed() {
        return proNeed;
    }

    public void setProNeed(String proNeed) {
        this.proNeed = proNeed;
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
}
