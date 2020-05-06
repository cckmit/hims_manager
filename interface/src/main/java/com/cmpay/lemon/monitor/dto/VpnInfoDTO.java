package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.request.GenericDTO;

import java.sql.Date;
import java.time.LocalDateTime;

/**
 * @author: zhou_xiong
 */
public class VpnInfoDTO extends GenericDTO {
    /**
     * @Fields vpnInnerSeq 需求内部号
     */
    private String vpnInnerSeq;
    /**
     * @Fields vpnApplicant 申请人
     */
    private String vpnApplicant;
    /**
     * @Fields vpnDept 部门
     */
    private String vpnDept;
    /**
     * @Fields vpnApplicantTel 手机号
     */
    private String vpnApplicantTel;
    /**
     * @Fields vpnStartTime 生效开始时间
     */
    private String vpnStartTime;
    /**
     * @Fields vpnEndTime 生效结束时间
     */
    private String vpnEndTime;
    /**
     * @Fields vpnReason 申请原因
     */
    private String vpnReason;
    /**
     * @Fields vpnApplyType VPN申请状态
     */
    private String vpnApplyType;
    /**
     * 页数
     */
    private int pageNum;
    /**
     * 页面大小
     */
    private int pageSize;
    /**
     * @Fields vpnApplyType VPN申请账号
     */
    private String vpnAccount;
    /**
     * @Fields vpnApplyType VPN申请账号密码
     */
    private String vpnPassword;

    public String getVpnAccount() {
        return vpnAccount;
    }

    public void setVpnAccount(String vpnAccount) {
        this.vpnAccount = vpnAccount;
    }

    public String getVpnPassword() {
        return vpnPassword;
    }

    public void setVpnPassword(String vpnPassword) {
        this.vpnPassword = vpnPassword;
    }
    public VpnInfoDTO() {
    }

    public String getVpnInnerSeq() {
        return vpnInnerSeq;
    }

    public void setVpnInnerSeq(String vpnInnerSeq) {
        this.vpnInnerSeq = vpnInnerSeq;
    }

    public String getVpnApplicant() {
        return vpnApplicant;
    }

    public void setVpnApplicant(String vpnApplicant) {
        this.vpnApplicant = vpnApplicant;
    }

    public String getVpnDept() {
        return vpnDept;
    }

    public void setVpnDept(String vpnDept) {
        this.vpnDept = vpnDept;
    }

    public String getVpnApplicantTel() {
        return vpnApplicantTel;
    }

    public void setVpnApplicantTel(String vpnApplicantTel) {
        this.vpnApplicantTel = vpnApplicantTel;
    }

    public String getVpnStartTime() {
        return vpnStartTime;
    }

    public void setVpnStartTime(String vpnStartTime) {
        this.vpnStartTime = vpnStartTime;
    }

    public String getVpnEndTime() {
        return vpnEndTime;
    }

    public void setVpnEndTime(String vpnEndTime) {
        this.vpnEndTime = vpnEndTime;
    }

    public String getVpnReason() {
        return vpnReason;
    }

    public void setVpnReason(String vpnReason) {
        this.vpnReason = vpnReason;
    }

    public String getVpnApplyType() {
        return vpnApplyType;
    }

    public void setVpnApplyType(String vpnApplyType) {
        this.vpnApplyType = vpnApplyType;
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

    @Override
    public String toString() {
        return "VpnInfoDTO{" +
                "vpnInnerSeq='" + vpnInnerSeq + '\'' +
                ", vpnApplicant='" + vpnApplicant + '\'' +
                ", vpnDept='" + vpnDept + '\'' +
                ", vpnApplicantTel='" + vpnApplicantTel + '\'' +
                ", vpnStartTime='" + vpnStartTime + '\'' +
                ", vpnEndTime='" + vpnEndTime + '\'' +
                ", vpnReason='" + vpnReason + '\'' +
                ", vpnApplyType='" + vpnApplyType + '\'' +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", vpnAccount='" + vpnAccount + '\'' +
                ", vpnPassword='" + vpnPassword + '\'' +
                '}';
    }
}
