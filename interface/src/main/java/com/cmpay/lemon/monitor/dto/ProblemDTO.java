/*
 * @ClassName ProblemDO
 * @Description
 * @version 1.0
 * @Date 2020-09-25 15:17:26
 */
package com.cmpay.lemon.monitor.dto;



import com.cmpay.framework.data.response.PageableRspDTO;

import java.time.LocalDateTime;


public class ProblemDTO  extends PageableRspDTO {
    /**
     *  problemSerialNumber id
     */
    private Long problemSerialNumber;
    /**
     *  proNumber 投产编号
     */
    private String proNumber;
    /**
     *  problemDetail 问题详情
     */
    private String problemDetail;
    /**
     *  problemTime 录入时间
     */
    private LocalDateTime problemTime;
    /**
     *  issuekey jira编号
     */
    private String issuekey;
    /**
     *  displayname 问题提出人
     */
    private String displayname;
    /**
     *  devpLeadDept 归属部门
     */
    private String devpLeadDept;
    /**
     * 页数
     */
    private int pageNum;
    /**
     * 页面大小
     */
    private int pageSize;
    /**
     * 类型
     */
    private String  proType;
    /**
     * 投产日期
     */
    private String  proDate;
    /**
     * 需求名称
     */
    private String  proNeed;
    /**
     * problemType 投产问题分类
     */
    private String problemType;
    /**
     *  updateTime 修改时间
     */
    private LocalDateTime updateTime;
    /**
     *  updateUser 修改人
     */
    private String updateUser;
    private String isJira;
    private String reqStartMon;

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

    public String getProType() {
        return proType;
    }

    public void setProType(String proType) {
        this.proType = proType;
    }

    public String getProDate() {
        return proDate;
    }

    public void setProDate(String proDate) {
        this.proDate = proDate;
    }

    public String getProNeed() {
        return proNeed;
    }

    public void setProNeed(String proNeed) {
        this.proNeed = proNeed;
    }

    public String getIsJira() {
        return isJira;
    }

    public void setIsJira(String isJira) {
        this.isJira = isJira;
    }

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

    @Override
    public int getPageNum() {
        return pageNum;
    }

    @Override
    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "ProblemDTO{" +
                "problemSerialNumber=" + problemSerialNumber +
                ", proNumber='" + proNumber + '\'' +
                ", problemDetail='" + problemDetail + '\'' +
                ", problemTime=" + problemTime +
                ", issuekey='" + issuekey + '\'' +
                ", displayname='" + displayname + '\'' +
                ", devpLeadDept='" + devpLeadDept + '\'' +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", proType='" + proType + '\'' +
                ", proDate='" + proDate + '\'' +
                ", proNeed='" + proNeed + '\'' +
                ", problemType='" + problemType + '\'' +
                ", updateTime=" + updateTime +
                ", updateUser='" + updateUser + '\'' +
                ", isJira='" + isJira + '\'' +
                ", reqStartMon='" + reqStartMon + '\'' +
                '}';
    }
}
