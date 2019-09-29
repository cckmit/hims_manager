/*
 * @ClassName DemandJiraDO
 * @Description 
 * @version 1.0
 * @Date 2019-09-29 10:20:16
 */
package com.cmpay.lemon.monitor.entity;

import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;

import java.time.LocalDateTime;

@DataObject
public class DemandJiraDO extends BaseDO {
    /**
     * @Fields reqInnerSeq 需求内部号
     */
    private String reqInnerSeq;
    /**
     * @Fields reqNm 需求名称
     */
    private String reqNm;
    /**
     * @Fields jiraId jiraId
     */
    private String jiraId;
    /**
     * @Fields jiraKey jiraKey
     */
    private String jiraKey;
    /**
     * @Fields createState 创建状态
     */
    private String createState;
    /**
     * @Fields failCause 失败原因
     */
    private String failCause;
    /**
     * @Fields creatTime 创建时间
     */
    private LocalDateTime creatTime;

    public String getReqInnerSeq() {
        return reqInnerSeq;
    }

    public void setReqInnerSeq(String reqInnerSeq) {
        this.reqInnerSeq = reqInnerSeq;
    }

    public String getReqNm() {
        return reqNm;
    }

    public void setReqNm(String reqNm) {
        this.reqNm = reqNm;
    }

    public String getJiraId() {
        return jiraId;
    }

    public void setJiraId(String jiraId) {
        this.jiraId = jiraId;
    }

    public String getJiraKey() {
        return jiraKey;
    }

    public void setJiraKey(String jiraKey) {
        this.jiraKey = jiraKey;
    }

    public String getCreateState() {
        return createState;
    }

    public void setCreateState(String createState) {
        this.createState = createState;
    }

    public String getFailCause() {
        return failCause;
    }

    public void setFailCause(String failCause) {
        this.failCause = failCause;
    }

    public LocalDateTime getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(LocalDateTime creatTime) {
        this.creatTime = creatTime;
    }
}