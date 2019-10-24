/*
 * @ClassName CenterDO
 * @Description 
 * @version 1.0
 * @Date 2019-07-25 11:01:18
 */
package com.cmpay.lemon.monitor.entity.sendemail;

import com.cmpay.lemon.framework.annotation.DataObject;
import com.cmpay.lemon.monitor.entity.AbstractDO;

import java.io.Serializable;
import java.util.Date;

@DataObject
public class MailGroupDO extends AbstractDO {
    private String mailGroupId;
    private String mailGroupName;
    private String mailUser;
    private String mailGroupDesc;
    private Date groupTime;

    public MailGroupDO() {
    }

    public MailGroupDO(String mailGroupName,
                         String mailUser, String mailGroupDesc) {
        this.mailGroupName = mailGroupName;
        this.mailUser = mailUser;
        this. mailGroupDesc = mailGroupDesc;
    }

    public MailGroupDO(String mailGroupId, String mailGroupName,
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

    @Override
    public Serializable getId() {
        return null;
    }
}