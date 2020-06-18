/*
 * @ClassName SmokeTestRegistrationDO
 * @Description 
 * @version 1.0
 * @Date 2020-06-18 14:35:53
 */
package com.cmpay.lemon.monitor.entity;

import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;

@DataObject
public class SmokeTestRegistrationDO extends BaseDO {
    /**
     * @Fields id 主键
     */
    private Integer id;
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
}