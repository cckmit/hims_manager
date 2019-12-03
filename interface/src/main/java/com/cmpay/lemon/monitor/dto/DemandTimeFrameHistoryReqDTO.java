/*
 * @ClassName DemandTimeFrameHistoryDO
 * @Description 
 * @version 1.0
 * @Date 2019-12-02 17:57:24
 */
package com.cmpay.lemon.monitor.dto;

import com.cmpay.lemon.framework.annotation.DataObject;

import java.time.LocalDateTime;

@DataObject
public class DemandTimeFrameHistoryReqDTO {
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
     * @Fields prdFinshTm 修改后需求预计定稿时间
     */
    private String prdFinshTm;
    /**
     * @Fields expPrdReleaseTm 修改后需求预计投产时间
     */
    private String expPrdReleaseTm;
    /**
     * @Fields uatUpdateTm 修改后需求预计uat更新时间
     */
    private String uatUpdateTm;
    /**
     * @Fields testFinshTm 修改后需求预计测试完成时间
     */
    private String testFinshTm;
    /**
     * @Fields preTm 修改后需求预计定稿时间
     */
    private String preTm;
    /**
     * @Fields oldPrdFinshTm 修改前需求预计定稿时间
     */
    private String oldPrdFinshTm;
    /**
     * @Fields oldExpPrdReleaseTm 修改前需求预计投产时间
     */
    private String oldExpPrdReleaseTm;
    /**
     * @Fields oldUatUpdateTm 修改前需求预计uat更新时间
     */
    private String oldUatUpdateTm;
    /**
     * @Fields oldTestFinshTm 修改前需求预计测试完成时间
     */
    private String oldTestFinshTm;
    /**
     * @Fields oldPreTm 修改前需求预计定稿时间
     */
    private String oldPreTm;
    /**
     * @Fields remarks 需求时间节点修改备注
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
    /**
     * 页数
     */
    private int pageNum;
    /**
     * 页面大小
     */
    private int pageSize;

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

    public String getPrdFinshTm() {
        return prdFinshTm;
    }

    public void setPrdFinshTm(String prdFinshTm) {
        this.prdFinshTm = prdFinshTm;
    }

    public String getExpPrdReleaseTm() {
        return expPrdReleaseTm;
    }

    public void setExpPrdReleaseTm(String expPrdReleaseTm) {
        this.expPrdReleaseTm = expPrdReleaseTm;
    }

    public String getUatUpdateTm() {
        return uatUpdateTm;
    }

    public void setUatUpdateTm(String uatUpdateTm) {
        this.uatUpdateTm = uatUpdateTm;
    }

    public String getTestFinshTm() {
        return testFinshTm;
    }

    public void setTestFinshTm(String testFinshTm) {
        this.testFinshTm = testFinshTm;
    }

    public String getPreTm() {
        return preTm;
    }

    public void setPreTm(String preTm) {
        this.preTm = preTm;
    }

    public String getOldPrdFinshTm() {
        return oldPrdFinshTm;
    }

    public void setOldPrdFinshTm(String oldPrdFinshTm) {
        this.oldPrdFinshTm = oldPrdFinshTm;
    }

    public String getOldExpPrdReleaseTm() {
        return oldExpPrdReleaseTm;
    }

    public void setOldExpPrdReleaseTm(String oldExpPrdReleaseTm) {
        this.oldExpPrdReleaseTm = oldExpPrdReleaseTm;
    }

    public String getOldUatUpdateTm() {
        return oldUatUpdateTm;
    }

    public void setOldUatUpdateTm(String oldUatUpdateTm) {
        this.oldUatUpdateTm = oldUatUpdateTm;
    }

    public String getOldTestFinshTm() {
        return oldTestFinshTm;
    }

    public void setOldTestFinshTm(String oldTestFinshTm) {
        this.oldTestFinshTm = oldTestFinshTm;
    }

    public String getOldPreTm() {
        return oldPreTm;
    }

    public void setOldPreTm(String oldPreTm) {
        this.oldPreTm = oldPreTm;
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
}