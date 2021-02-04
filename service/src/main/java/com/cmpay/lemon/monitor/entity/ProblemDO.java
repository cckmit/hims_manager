/*
 * @ClassName ProblemDO
 * @Description
 * @version 1.0
 * @Date 2021-02-02 16:03:37
 */
package com.cmpay.lemon.monitor.entity;

import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;
import java.time.LocalDateTime;

@DataObject
public class ProblemDO extends BaseDO {
    /**
     * @Fields problemSerialNumber id
     */
    private Long problemSerialNumber;
    /**
     * @Fields proNumber 投产编号
     */
    private String proNumber;
    /**
     * @Fields problemDetail 问题描述
     */
    private String problemDetail;
    /**
     * @Fields problemTime 提出时间
     */
    private LocalDateTime problemTime;
    /**
     * @Fields issuekey jira编号
     */
    private String issuekey;
    /**
     * @Fields displayname 问题提出人
     */
    private String displayname;
    /**
     * @Fields devpLeadDept 归属部门
     */
    private String devpLeadDept;
    /**
     * @Fields isJira 是否创建jira任务
     */
    private String isJira;
    /**
     * @Fields problemType 投产问题分类
     */
    private String problemType;
    /**
     * @Fields updateTime 修改时间
     */
    private LocalDateTime updateTime;
    /**
     * @Fields updateUser 修改人
     */
    private String updateUser;

    private String reqStartMon;

    public String getReqStartMon() {
        return reqStartMon;
    }

    public void setReqStartMon(String reqStartMon) {
        this.reqStartMon = reqStartMon;
    }
    public Long getProblemSerialNumber() {
        return problemSerialNumber;
    }

    public void setProblemSerialNumber(Long problemSerialNumber) {
        this.problemSerialNumber = problemSerialNumber;
    }

    public String getProNumber() {
        return proNumber;
    }

    public void setProNumber(String proNumber) {
        this.proNumber = proNumber;
    }

    public String getProblemDetail() {
        return problemDetail;
    }

    public void setProblemDetail(String problemDetail) {
        this.problemDetail = problemDetail;
    }

    public LocalDateTime getProblemTime() {
        return problemTime;
    }

    public void setProblemTime(LocalDateTime problemTime) {
        this.problemTime = problemTime;
    }

    public String getIssuekey() {
        return issuekey;
    }

    public void setIssuekey(String issuekey) {
        this.issuekey = issuekey;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getDevpLeadDept() {
        return devpLeadDept;
    }

    public void setDevpLeadDept(String devpLeadDept) {
        this.devpLeadDept = devpLeadDept;
    }

    public String getIsJira() {
        return isJira;
    }

    public void setIsJira(String isJira) {
        this.isJira = isJira;
    }

    public String getProblemType() {
        return problemType;
    }

    public void setProblemType(String problemType) {
        this.problemType = problemType;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
}
