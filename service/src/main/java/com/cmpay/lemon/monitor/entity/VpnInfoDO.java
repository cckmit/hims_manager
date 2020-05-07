/*
 * @ClassName VpnInfoDO
 * @Description 
 * @version 1.0
 * @Date 2020-04-22 09:53:42
 */
package com.cmpay.lemon.monitor.entity;

import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;


@DataObject
public class VpnInfoDO extends BaseDO {
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

    @Override
    public String toString() {
        return "VpnInfoDO{" +
                "vpnInnerSeq='" + vpnInnerSeq + '\'' +
                ", vpnApplicant='" + vpnApplicant + '\'' +
                ", vpnDept='" + vpnDept + '\'' +
                ", vpnApplicantTel='" + vpnApplicantTel + '\'' +
                ", vpnStartTime='" + vpnStartTime + '\'' +
                ", vpnEndTime='" + vpnEndTime + '\'' +
                ", vpnReason='" + vpnReason + '\'' +
                ", vpnApplyType='" + vpnApplyType + '\'' +
                ", vpnAccount='" + vpnAccount + '\'' +
                ", vpnPassword='" + vpnPassword + '\'' +
                '}';
    }
}