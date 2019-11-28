package com.cmpay.lemon.monitor.entity;

import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;

import java.time.LocalDateTime;
import java.util.Date;

@DataObject
public class ErcdmgUpdmgnDO extends BaseDO {
    private static final long serialVersionUID = 1L;
    private String updateNo;
    private String prodUserId;
    private String prodUserName;
    private java.util.Date updateDate;
    private String content;
    private String count;
    private String curtState;
    private String curtStateName;
    private java.util.Date createTime;


    public ErcdmgUpdmgnDO() {
    }

    public String getUpdateNo() {
        return updateNo;
    }

    public void setUpdateNo(String updateNo) {
        this.updateNo = updateNo;
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

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "ErcdmgUpdmgnDO{" +
                "updateNo='" + updateNo + '\'' +
                ", prodUserId='" + prodUserId + '\'' +
                ", prodUserName='" + prodUserName + '\'' +
                ", updateDate=" + updateDate +
                ", content='" + content + '\'' +
                ", count='" + count + '\'' +
                ", curtState='" + curtState + '\'' +
                ", curtStateName='" + curtStateName + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}