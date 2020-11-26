/*
 * @ClassName GitlabDataDO
 * @Description
 * @version 1.0
 * @Date 2020-11-20 15:21:41
 */
package com.cmpay.lemon.monitor.entity;

import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;

@DataObject
public class GitlabDataDO extends BaseDO {
    /**
     * @Fields gitlabId id
     */
    private String gitlabId;
    /**
     * @Fields committerName 提交人
     */
    private String committerName;
    /**
     * @Fields committedDate 提交日期
     */
    private String committedDate;
    /**
     * @Fields committerEmail 提交人邮箱
     */
    private String committerEmail;
    /**
     * @Fields title 提交标题
     */
    private String title;
    /**
     * @Fields message 提交备注
     */
    private String message;
    /**
     * @Fields statsTotal 代码变更总行数
     */
    private Integer statsTotal;
    /**
     * @Fields statsAdditions 代码新增行数
     */
    private Integer statsAdditions;
    /**
     * @Fields statsDeletions 代码删除行数
     */
    private Integer statsDeletions;
    /**
     * @Fields branchName 提交分支名
     */
    private String branchName;
    /**
     * @Fields httpUrlToRepo 归属项目路径
     */
    private String httpUrlToRepo;
    /**
     * @Fields projectId 归属项目id
     */
    private String projectId;
    /**
     * @Fields nameWithNamespace 归属项目名
     */
    private String nameWithNamespace;
    /**
     * @Fields description 归属项目描述
     */
    private String description;
    /**
     * @Fields devpLeadDept 归属团队
     */
    private String devpLeadDept;
    /**
     * @Fields displayName 显示中文名
     */
    private String displayName;

    public String getGitlabId() {
        return gitlabId;
    }

    public void setGitlabId(String gitlabId) {
        this.gitlabId = gitlabId;
    }

    public String getCommitterName() {
        return committerName;
    }

    public void setCommitterName(String committerName) {
        this.committerName = committerName;
    }

    public String getCommittedDate() {
        return committedDate;
    }

    public void setCommittedDate(String committedDate) {
        this.committedDate = committedDate;
    }

    public String getCommitterEmail() {
        return committerEmail;
    }

    public void setCommitterEmail(String committerEmail) {
        this.committerEmail = committerEmail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatsTotal() {
        return statsTotal;
    }

    public void setStatsTotal(Integer statsTotal) {
        this.statsTotal = statsTotal;
    }

    public Integer getStatsAdditions() {
        return statsAdditions;
    }

    public void setStatsAdditions(Integer statsAdditions) {
        this.statsAdditions = statsAdditions;
    }

    public Integer getStatsDeletions() {
        return statsDeletions;
    }

    public void setStatsDeletions(Integer statsDeletions) {
        this.statsDeletions = statsDeletions;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getHttpUrlToRepo() {
        return httpUrlToRepo;
    }

    public void setHttpUrlToRepo(String httpUrlToRepo) {
        this.httpUrlToRepo = httpUrlToRepo;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getNameWithNamespace() {
        return nameWithNamespace;
    }

    public void setNameWithNamespace(String nameWithNamespace) {
        this.nameWithNamespace = nameWithNamespace;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDevpLeadDept() {
        return devpLeadDept;
    }

    public void setDevpLeadDept(String devpLeadDept) {
        this.devpLeadDept = devpLeadDept;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return "GitlabDataDO{" +
                "gitlabId='" + gitlabId + '\'' +
                ", committerName='" + committerName + '\'' +
                ", committedDate='" + committedDate + '\'' +
                ", committerEmail='" + committerEmail + '\'' +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", statsTotal=" + statsTotal +
                ", statsAdditions=" + statsAdditions +
                ", statsDeletions=" + statsDeletions +
                ", branchName='" + branchName + '\'' +
                ", httpUrlToRepo='" + httpUrlToRepo + '\'' +
                ", projectId='" + projectId + '\'' +
                ", nameWithNamespace='" + nameWithNamespace + '\'' +
                ", description='" + description + '\'' +
                ", devpLeadDept='" + devpLeadDept + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
