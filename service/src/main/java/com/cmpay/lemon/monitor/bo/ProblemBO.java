/*
 * @ClassName ProblemDO
 * @Description
 * @version 1.0
 * @Date 2020-09-25 15:17:26
 */
package com.cmpay.lemon.monitor.bo;



import java.time.LocalDateTime;


public class ProblemBO  {
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

    @Override
    public String toString() {
        return "ProblemBO{" +
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
                '}';
    }
}
