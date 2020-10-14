/*
 * @ClassName SmokeTestRegistrationDO
 * @Description
 * @version 1.0
 * @Date 2020-07-08 11:33:25
 */
package com.cmpay.lemon.monitor.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;

@DataObject
public class SmokeTestRegistrationDO extends BaseDO {
    /**
     * @Fields id 主键
     */
    private Integer id;
    /**
     * @Fields jiraKey
     */
    @Excel(name = "jiraKey")
    private String jiraKey;
    /**
     * @Fields department
     */
    @Excel(name = "归属部门")
    private String department;
    /**
     * @Fields reqInnerSeq
     */
    @Excel(name = "内部需求编号")
    private String reqInnerSeq;
    /**
     * @Fields reqNo
     */
    @Excel(name = "需求编号")
    private String reqNo;
    /**
     * @Fields reqNm
     */
    @Excel(name = "需求名称")
    private String reqNm;
    /**
     * @Fields testers
     */
    @Excel(name = "测试人员")
    private String testers;
    /**
     * @Fields testDate
     */
    @Excel(name = "测试日期")
    private String testDate;
    /**
     * @Fields testdescription
     */
    @Excel(name = "冒烟不通过描述")
    private String testdescription;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getTestDate() {
        return testDate;
    }

    public void setTestDate(String testDate) {
        this.testDate = testDate;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
