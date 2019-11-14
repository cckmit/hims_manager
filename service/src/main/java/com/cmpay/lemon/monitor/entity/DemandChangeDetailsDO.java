/*
 * @ClassName DemandChangeDetailsDO
 * @Description 
 * @version 1.0
 * @Date 2019-11-13 10:55:00
 */
package com.cmpay.lemon.monitor.entity;

import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;

import java.time.LocalDateTime;

@DataObject
public class DemandChangeDetailsDO extends BaseDO {
    /**
     * @Fields id id
     */
    private Integer id;
    /**
     * @Fields reqInnerSeq 需求内部号
     */
    private String reqInnerSeq;
    /**
     * @Fields reqNo 需求编号
     */
    private String reqNo;
    /**
     * @Fields reqNm 需求名称
     */
    private String reqNm;
    /**
     * @Fields reqImplMon 需求实施月份
     */
    private String reqImplMon;
    /**
     * @Fields parentReqNo 父需求编号（由那条需求变更而得）
     */
    private String parentReqNo;
    /**
     * @Fields identification 标识（需求变更之后的数据库表生成的新需求使用同一标识）
     */
    private String identification;
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

    public String getReqNo() {
        return reqNo;
    }

    public void setReqNo(String reqNo) {
        this.reqNo = reqNo;
    }

    public String getReqNm() {
        return reqNm;
    }

    public void setReqNm(String reqNm) {
        this.reqNm = reqNm;
    }

    public String getReqImplMon() {
        return reqImplMon;
    }

    public void setReqImplMon(String reqImplMon) {
        this.reqImplMon = reqImplMon;
    }

    public String getParentReqNo() {
        return parentReqNo;
    }

    public void setParentReqNo(String parentReqNo) {
        this.parentReqNo = parentReqNo;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
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