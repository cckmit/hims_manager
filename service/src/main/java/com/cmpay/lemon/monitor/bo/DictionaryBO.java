package com.cmpay.lemon.monitor.bo;

import java.util.Date;
import java.util.List;

/**
 * @author: zhou_xiong
 */
public class DictionaryBO {
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

    private List<DictionaryBO> dictionaryBOList;

    public DictionaryBO() {
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

    public List<DictionaryBO> getDictionaryBOList() {
        return dictionaryBOList;
    }

    public void setDictionaryBOList(List<DictionaryBO> dictionaryBOList) {
        this.dictionaryBOList = dictionaryBOList;
    }

    @Override
    public String toString() {
        return "DictionaryBO{" +
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
                ", dictionaryBOList=" + dictionaryBOList +
                '}';
    }
}
