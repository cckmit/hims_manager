/*
 * @ClassName CenterDO
 * @Description 
 * @version 1.0
 * @Date 2019-07-25 11:01:18
 */
package com.cmpay.lemon.monitor.entity;

import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;

import java.util.Date;

@DataObject
public class DictionaryDO extends BaseDO {
    private String seqId;
    private String name;
    private String value;
    private String jp;
    private String qp;
    private String dicId;
    private String remark;
    private String sort;
    private Integer mailJdId;
    private String userType;
    private String userName;
    private String userMail;
    private Date creatTime;

    public DictionaryDO() {
    }

    public String getSeqId() {
        return seqId;
    }

    public void setSeqId(String seqId) {
        this.seqId = seqId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getJp() {
        return jp;
    }

    public void setJp(String jp) {
        this.jp = jp;
    }

    public String getQp() {
        return qp;
    }

    public void setQp(String qp) {
        this.qp = qp;
    }

    public String getDicId() {
        return dicId;
    }

    public void setDicId(String dicId) {
        this.dicId = dicId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Integer getMailJdId() {
        return mailJdId;
    }

    public void setMailJdId(Integer mailJdId) {
        this.mailJdId = mailJdId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public Date getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
    }

    @Override
    public String toString() {
        return "DictionaryDO{" +
                "seqId='" + seqId + '\'' +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", jp='" + jp + '\'' +
                ", qp='" + qp + '\'' +
                ", dicId='" + dicId + '\'' +
                ", remark='" + remark + '\'' +
                ", sort='" + sort + '\'' +
                ", mailJdId=" + mailJdId +
                ", userType='" + userType + '\'' +
                ", userName='" + userName + '\'' +
                ", userMail='" + userMail + '\'' +
                ", creatTime=" + creatTime +
                '}';
    }
}