/*
 * @ClassName SmokeTestFailedCountDO
 * @Description 
 * @version 1.0
 * @Date 2020-06-17 14:29:04
 */
package com.cmpay.lemon.monitor.entity;

import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;

@DataObject
public class SmokeTestFailedCountDO extends BaseDO {
    /**
     * @Fields id 
     */
    private Integer id;
    /**
     * @Fields reqNo 
     */
    private String reqNo;
    /**
     * @Fields jiraKey 
     */
    private String jiraKey;
    /**
     * @Fields count 
     */
    private Integer count;

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
}