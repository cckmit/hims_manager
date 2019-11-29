/*
 * @ClassName DemandStateHistoryDO
 * @Description 
 * @version 1.0
 * @Date 2019-11-14 09:52:51
 */
package com.cmpay.lemon.monitor.bo;

import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;

import java.time.LocalDateTime;

@DataObject
public class DemandStateHistoryBO extends BaseDO {
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
     * @Fields oldReqSts 修改前需求状态
     */
    private String oldReqSts;
    /**
     * @Fields reqSts 需求状态
     */
    private String reqSts;
    /**
     * @Fields remarks 需求状态修改备注
     */
    private String remarks;
    /**
     * @Fields identification 标识（同一需求的统一标识）
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

    public String getOldReqSts() {
        return oldReqSts;
    }

    public void setOldReqSts(String oldReqSts) {
        this.oldReqSts = oldReqSts;
    }
}