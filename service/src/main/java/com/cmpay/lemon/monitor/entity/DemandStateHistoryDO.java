/*
 * @ClassName DemandStateHistoryDO
 * @Description 
 * @version 1.0
 * @Date 2019-09-29 14:11:41
 */
package com.cmpay.lemon.monitor.entity;

import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;

import java.time.LocalDateTime;

@DataObject
public class DemandStateHistoryDO extends BaseDO {
    /**
     * @Fields id id
     */
    private Integer id;
    /**
     * @Fields reqInnerSeq 需求内部号
     */
    private String reqInnerSeq;
    /**
     * @Fields reqNm 需求名称
     */
    private String reqNm;
    /**
     * @Fields reqSts 需求状态
     */
    private String reqSts;
    /**
     * @Fields remarks 需求状态修改备注
     */
    private String remarks;
    /**
     * @Fields creatUser 操作人
     */
    private String creatUser;
    /**
     * @Fields creatTime 操作时间
     */
    private LocalDateTime creatTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public String getReqSts() {
        return reqSts;
    }

    public void setReqSts(String reqSts) {
        this.reqSts = reqSts;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCreatUser() {
        return creatUser;
    }

    public void setCreatUser(String creatUser) {
        this.creatUser = creatUser;
    }

    public LocalDateTime getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(LocalDateTime creatTime) {
        this.creatTime = creatTime;
    }
}