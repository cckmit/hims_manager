package com.cmpay.lemon.monitor.bo;

import java.util.Date;

/**
 * @author: zhou_xiong
 */

public class ErcdmgErrorComditionBO {
    private String id;
    private String errorCd;
    private String prodMod;
    private String prodModName;
    private String appScen;
    private String techTip;
    private String busnTip;
    private String curtState;
    private String curtStateName;
    private String errorState;
    private String techUserId;//技术负责人id
    private String techUserName;
    private String prodUserId;
    private String prodUserName;//产品经理名称
    private java.util.Date entryDate;
    private java.util.Date updateDate;//期望更新时间
    private java.util.Date createDatetime;
    private String createUserId;
    private String audiRoleName;
    private String remark;
    private String updateNo;
    private String buscnl;
    private String synflg;
    private String cr;
    private String ftpUploadStatus;   //修改后的上传状态  0未上传，1已经上传
    private java.util.Date lastUpdateDate;

    private java.util.Date strDate;
    private Date endDate;
    private String strDateStr;
    private String endDateStr;
    private String desc;

    /**
     * 页数
     */
    private int pageNum;
    /**
     * 页面大小
     */
    private int pageSize;

    public ErcdmgErrorComditionBO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getErrorCd() {
        return errorCd;
    }

    public void setErrorCd(String errorCd) {
        this.errorCd = errorCd;
    }

    public String getProdMod() {
        return prodMod;
    }

    public void setProdMod(String prodMod) {
        this.prodMod = prodMod;
    }

    public String getProdModName() {
        return prodModName;
    }

    public void setProdModName(String prodModName) {
        this.prodModName = prodModName;
    }

    public String getAppScen() {
        return appScen;
    }

    public void setAppScen(String appScen) {
        this.appScen = appScen;
    }

    public String getTechTip() {
        return techTip;
    }

    public void setTechTip(String techTip) {
        this.techTip = techTip;
    }

    public String getBusnTip() {
        return busnTip;
    }

    public void setBusnTip(String busnTip) {
        this.busnTip = busnTip;
    }

    public String getCurtState() {
        return curtState;
    }

    public void setCurtState(String curtState) {
        this.curtState = curtState;
    }

    public String getCurtStateName() {
        return curtStateName;
    }

    public void setCurtStateName(String curtStateName) {
        this.curtStateName = curtStateName;
    }

    public String getErrorState() {
        return errorState;
    }

    public void setErrorState(String errorState) {
        this.errorState = errorState;
    }

    public String getTechUserId() {
        return techUserId;
    }

    public void setTechUserId(String techUserId) {
        this.techUserId = techUserId;
    }

    public String getTechUserName() {
        return techUserName;
    }

    public void setTechUserName(String techUserName) {
        this.techUserName = techUserName;
    }

    public String getProdUserId() {
        return prodUserId;
    }

    public void setProdUserId(String prodUserId) {
        this.prodUserId = prodUserId;
    }

    public String getProdUserName() {
        return prodUserName;
    }

    public void setProdUserName(String prodUserName) {
        this.prodUserName = prodUserName;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getAudiRoleName() {
        return audiRoleName;
    }

    public void setAudiRoleName(String audiRoleName) {
        this.audiRoleName = audiRoleName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUpdateNo() {
        return updateNo;
    }

    public void setUpdateNo(String updateNo) {
        this.updateNo = updateNo;
    }

    public String getBuscnl() {
        return buscnl;
    }

    public void setBuscnl(String buscnl) {
        this.buscnl = buscnl;
    }

    public String getSynflg() {
        return synflg;
    }

    public void setSynflg(String synflg) {
        this.synflg = synflg;
    }

    public String getCr() {
        return cr;
    }

    public void setCr(String cr) {
        this.cr = cr;
    }

    public String getFtpUploadStatus() {
        return ftpUploadStatus;
    }

    public void setFtpUploadStatus(String ftpUploadStatus) {
        this.ftpUploadStatus = ftpUploadStatus;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Date getStrDate() {
        return strDate;
    }

    public void setStrDate(Date strDate) {
        this.strDate = strDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStrDateStr() {
        return strDateStr;
    }

    public void setStrDateStr(String strDateStr) {
        this.strDateStr = strDateStr;
    }

    public String getEndDateStr() {
        return endDateStr;
    }

    public void setEndDateStr(String endDateStr) {
        this.endDateStr = endDateStr;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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
        return "ErcdmgErrorComditionBO{" +
                "id='" + id + '\'' +
                ", id='" + id + '\'' +
                ", errorCd='" + errorCd + '\'' +
                ", prodMod='" + prodMod + '\'' +
                ", prodModName='" + prodModName + '\'' +
                ", appScen='" + appScen + '\'' +
                ", techTip='" + techTip + '\'' +
                ", busnTip='" + busnTip + '\'' +
                ", curtState='" + curtState + '\'' +
                ", curtStateName='" + curtStateName + '\'' +
                ", errorState='" + errorState + '\'' +
                ", techUserId='" + techUserId + '\'' +
                ", techUserName='" + techUserName + '\'' +
                ", prodUserId='" + prodUserId + '\'' +
                ", prodUserName='" + prodUserName + '\'' +
                ", entryDate=" + entryDate +
                ", updateDate=" + updateDate +
                ", createDatetime=" + createDatetime +
                ", createUserId='" + createUserId + '\'' +
                ", audiRoleName='" + audiRoleName + '\'' +
                ", remark='" + remark + '\'' +
                ", updateNo='" + updateNo + '\'' +
                ", buscnl='" + buscnl + '\'' +
                ", synflg='" + synflg + '\'' +
                ", cr='" + cr + '\'' +
                ", ftpUploadStatus='" + ftpUploadStatus + '\'' +
                ", lastUpdateDate=" + lastUpdateDate +
                ", strDate=" + strDate +
                ", endDate=" + endDate +
                ", strDateStr='" + strDateStr + '\'' +
                ", endDateStr='" + endDateStr + '\'' +
                ", desc='" + desc + '\'' +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }
}

