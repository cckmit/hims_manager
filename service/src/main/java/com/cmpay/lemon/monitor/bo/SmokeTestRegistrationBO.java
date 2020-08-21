package com.cmpay.lemon.monitor.bo;

/*
 * @ClassName DemandNameChangeDO
 * @Description
 * @version 1.0
 * @Date 2020-06-03 10:21:06
 */


public class SmokeTestRegistrationBO {
    /**
     * @Fields reqNm
     */
    private String reqNm;
    /**
     * @Fields reqNo
     */
    private String reqNo;
    /**
     * @Fields reqInnerSeq
     */
    private String reqInnerSeq;
    /**
     * @Fields jiraKey
     */
    private String jiraKey;
    /**
     * @Fields testers
     */
    private String testers;
    /**
     * @Fields testdescription
     */
    private String testdescription;
    /**
     * @Fields testDate
     */
    private String testDate;
    /**
     * @Fields department
     */
    private String department;
    /**
     * 页数
     */
    private int pageNum;
    /**
     * 页面大小
     */
    private int pageSize;

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getReqNm() {
        return reqNm;
    }

    public void setReqNm(String reqNm) {
        this.reqNm = reqNm;
    }

    public String getReqNo() {
        return reqNo;
    }

    public void setReqNo(String reqNo) {
        this.reqNo = reqNo;
    }

    public String getReqInnerSeq() {
        return reqInnerSeq;
    }

    public void setReqInnerSeq(String reqInnerSeq) {
        this.reqInnerSeq = reqInnerSeq;
    }

    public String getJiraKey() {
        return jiraKey;
    }

    public void setJiraKey(String jiraKey) {
        this.jiraKey = jiraKey;
    }

    public String getTesters() {
        return testers;
    }

    public void setTesters(String testers) {
        this.testers = testers;
    }

    public String getTestdescription() {
        return testdescription;
    }

    public void setTestdescription(String testdescription) {
        this.testdescription = testdescription;
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

    public String getTestDate() {
        return testDate;
    }

    public void setTestDate(String testDate) {
        this.testDate = testDate;
    }

    @Override
    public String toString() {
        return "SmokeTestRegistrationBO{" +
                "reqNm='" + reqNm + '\'' +
                ", reqNo='" + reqNo + '\'' +
                ", reqInnerSeq='" + reqInnerSeq + '\'' +
                ", jiraKey='" + jiraKey + '\'' +
                ", testers='" + testers + '\'' +
                ", testdescription='" + testdescription + '\'' +
                ", testDate='" + testDate + '\'' +
                ", department='" + department + '\'' +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }
}
