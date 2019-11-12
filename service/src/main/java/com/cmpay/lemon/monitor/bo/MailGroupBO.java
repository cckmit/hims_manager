/*
 * @ClassName CenterDO
 * @Description 
 * @version 1.0
 * @Date 2019-07-25 11:01:18
 */
package com.cmpay.lemon.monitor.bo;

import com.cmpay.lemon.framework.annotation.DataObject;

import java.util.Date;

@DataObject
public class MailGroupBO  {
    private String mailGroupId;
    private String mailGroupName;
    private String mailUser;
    private String mailGroupDesc;
    private Date groupTime;
    /**
     * 页数
     */
    private int pageNum;
    /**
     * 页面大小
     */
    private int pageSize;

    public MailGroupBO() {
    }

    public MailGroupBO(String mailGroupName,
                         String mailUser, String mailGroupDesc) {
        this.mailGroupName = mailGroupName;
        this.mailUser = mailUser;
        this. mailGroupDesc = mailGroupDesc;
    }

    public MailGroupBO(String mailGroupId, String mailGroupName,
                         String mailUser, String mailGroupDesc) {
        super();
        this.mailGroupId = mailGroupId;
        this.mailGroupName = mailGroupName;
        this.mailUser = mailUser;
        this.mailGroupDesc = mailGroupDesc;
    }

    public String getMailGroupId() {
        return mailGroupId;
    }

    public void setMailGroupId(String mailGroupId) {
        this.mailGroupId = mailGroupId;
    }

    public String getMailGroupName() {
        return mailGroupName;
    }

    public void setMailGroupName(String mailGroupName) {
        this.mailGroupName = mailGroupName;
    }

    public String getMailUser() {
        return mailUser;
    }

    public void setMailUser(String mailUser) {
        this.mailUser = mailUser;
    }

    public String getMailGroupDesc() {
        return mailGroupDesc;
    }

    public void setMailGroupDesc(String mailGroupDesc) {
        this.mailGroupDesc = mailGroupDesc;
    }

    public Date getGroupTime() {
        return groupTime;
    }

    public void setGroupTime(Date groupTime) {
        this.groupTime = groupTime;
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
        return "MailGroupBO{" +
                "mailGroupId='" + mailGroupId + '\'' +
                ", mailGroupName='" + mailGroupName + '\'' +
                ", mailUser='" + mailUser + '\'' +
                ", mailGroupDesc='" + mailGroupDesc + '\'' +
                ", groupTime=" + groupTime +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }
}