/*
 * @ClassName ProductionFollowDO
 * @Description
 * @version 1.0
 * @Date 2021-02-02 16:03:37
 */
package com.cmpay.lemon.monitor.bo;

import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;

import java.time.LocalDateTime;


public class ProductionFollowBO  {
    /**
     * @Fields followId 主键
     */
    private Long followId;
    /**
     * @Fields proNumber 投产编号
     */
    private String proNumber;
    /**
     * @Fields devpLeadDept 归属部门
     */
    private String devpLeadDept;
    /**
     * @Fields displayname 创建提出人
     */
    private String displayname;
    /**
     * @Fields issuekey jira编号
     */
    private String issuekey;
    /**
     * @Fields issueStatus jira状态
     */
    private String issueStatus;
    /**
     * @Fields followDetail 跟进项描述
     */
    private String followDetail;
    /**
     * @Fields followUser 跟进人
     */
    private String followUser;
    /**
     * @Fields followTime 创建时间
     */
    private LocalDateTime followTime;
    /**
     * @Fields updateTime 修改时间
     */
    private LocalDateTime updateTime;
    /**
     * @Fields updateUser 修改人
     */
    private String updateUser;
    /**
     * 页数
     */
    private int pageNum;
    /**
     * 页面大小
     */
    private int pageSize;
    private String reqStartMon;
    public String getReqStartMon() {
        return reqStartMon;
    }

    public void setReqStartMon(String reqStartMon) {
        this.reqStartMon = reqStartMon;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Long getFollowId() {
        return followId;
    }

    public void setFollowId(Long followId) {
        this.followId = followId;
    }

    public String getProNumber() {
        return proNumber;
    }

    public void setProNumber(String proNumber) {
        this.proNumber = proNumber;
    }

    public String getDevpLeadDept() {
        return devpLeadDept;
    }

    public void setDevpLeadDept(String devpLeadDept) {
        this.devpLeadDept = devpLeadDept;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getIssuekey() {
        return issuekey;
    }

    public void setIssuekey(String issuekey) {
        this.issuekey = issuekey;
    }

    public String getIssueStatus() {
        return issueStatus;
    }

    public void setIssueStatus(String issueStatus) {
        this.issueStatus = issueStatus;
    }

    public String getFollowDetail() {
        return followDetail;
    }

    public void setFollowDetail(String followDetail) {
        this.followDetail = followDetail;
    }

    public String getFollowUser() {
        return followUser;
    }

    public void setFollowUser(String followUser) {
        this.followUser = followUser;
    }

    public LocalDateTime getFollowTime() {
        return followTime;
    }

    public void setFollowTime(LocalDateTime followTime) {
        this.followTime = followTime;
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

    @Override
    public String toString() {
        return "ProductionFollowBO{" +
                "followId=" + followId +
                ", proNumber='" + proNumber + '\'' +
                ", devpLeadDept='" + devpLeadDept + '\'' +
                ", displayname='" + displayname + '\'' +
                ", issuekey='" + issuekey + '\'' +
                ", issueStatus='" + issueStatus + '\'' +
                ", followDetail='" + followDetail + '\'' +
                ", followUser='" + followUser + '\'' +
                ", followTime=" + followTime +
                ", updateTime=" + updateTime +
                ", updateUser='" + updateUser + '\'' +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }
}
