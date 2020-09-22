/*
 * @ClassName SmokeTestFailedCountDO
 * @Description
 * @version 1.0
 * @Date 2020-09-16 16:08:58
 */
package com.cmpay.lemon.monitor.dto;


public class SmokeTestFailedCountDTO {
    /**
     *  id
     */
    private Integer id;
    /**
     *  reqNo
     */
    private String reqNo;
    /**
     *  jiraKey
     */
    private String jiraKey;
    /**
     *  count
     */
    private Integer count;
    /**
     *  department
     */
    private String department;
    /**
     *  testDate
     */
    private String testDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReqNo() {
        return reqNo;
    }

    public void setReqNo(String reqNo) {
        this.reqNo = reqNo;
    }

    public String getJiraKey() {
        return jiraKey;
    }

    public void setJiraKey(String jiraKey) {
        this.jiraKey = jiraKey;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTestDate() {
        return testDate;
    }

    public void setTestDate(String testDate) {
        this.testDate = testDate;
    }

    @Override
    public String toString() {
        return "SmokeTestFailedCountBO{" +
                "id=" + id +
                ", reqNo='" + reqNo + '\'' +
                ", jiraKey='" + jiraKey + '\'' +
                ", count=" + count +
                ", department='" + department + '\'' +
                ", testDate='" + testDate + '\'' +
                '}';
    }
}
