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
    private String mail_group_id;
    private String mail_group_name;
    private String mail_user;
    private String mail_group_desc;
    private Date group_time;

    public MailGroupDO() {
    }

    public MailGroupDO(String mailGroupName,
                         String mailUser, String mailGroupDesc) {
        mail_group_name = mailGroupName;
        mail_user = mailUser;
        mail_group_desc = mailGroupDesc;
    }

    public MailGroupDO(String mailGroupId, String mailGroupName,
                         String mailUser, String mailGroupDesc) {
        super();
        mail_group_id = mailGroupId;
        mail_group_name = mailGroupName;
        mail_user = mailUser;
        mail_group_desc = mailGroupDesc;
    }

    public String getMail_group_id() {
        return mail_group_id;
    }

    public void setMail_group_id(String mailGroupId) {
        mail_group_id = mailGroupId;
    }

    public String getMail_group_name() {
        return mail_group_name;
    }

    public void setMail_group_name(String mailGroupName) {
        mail_group_name = mailGroupName;
    }

    public String getMail_user() {
        return mail_user;
    }

    public void setMail_user(String mailUser) {
        mail_user = mailUser;
    }

    public String getMail_group_desc() {
        return mail_group_desc;
    }

    public void setMail_group_desc(String mailGroupDesc) {
        mail_group_desc = mailGroupDesc;
    }

    public Date getGroup_time() {
        return group_time;
    }

    public void setGroup_time(Date groupTime) {
        group_time = groupTime;
    }

    @Override
    public Serializable getId() {
        return null;
    }
}