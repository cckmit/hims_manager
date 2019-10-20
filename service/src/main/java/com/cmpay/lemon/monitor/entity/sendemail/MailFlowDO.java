package com.cmpay.lemon.monitor.entity.sendemail;

import java.util.Date;

public class MailFlowDO {

    private int mailSerialNumber;
    private String mailSubject;
    private String mailSender;
    private String mailReceiver;
    private String mailAnnex;
    private String mailSendContent;
    private Date flowTime;
    private String employeeEmail;
    private String emailPassword;
    private String employeeName;
    private String groupTime;;
    public MailFlowDO() {
    }

    public MailFlowDO(String mailSubject, String mailSender,
                        String mailReceiver, String mailSendContent) {
        super();
        this.mailSubject = mailSubject;
        this.mailSender = mailSender;
        this.mailReceiver = mailReceiver;
        this.mailSendContent = mailSendContent;
    }

    public MailFlowDO(String mailSubject, String mailSender,
                        String mailReceiver, String mailAnnex, String mailSendContent) {
        super();
        this.mailSubject = mailSubject;
        this.mailSender = mailSender;
        this.mailReceiver = mailReceiver;
        this.mailAnnex = mailAnnex;
        this.mailSendContent = mailSendContent;
    }

    public int getMailSerialNumber() {
        return mailSerialNumber;
    }

    public void setMailSerialNumber(int mailSerialNumber) {
        this.mailSerialNumber = mailSerialNumber;
    }

    public String getMailSubject() {
        return mailSubject;
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    public String getMailSender() {
        return mailSender;
    }

    public void setMailSender(String mailSender) {
        this.mailSender = mailSender;
    }

    public String getMailReceiver() {
        return mailReceiver;
    }

    public void setMailReceiver(String mailReceiver) {
        this.mailReceiver = mailReceiver;
    }

    public String getMailAnnex() {
        return mailAnnex;
    }

    public void setMailAnnex(String mailAnnex) {
        this.mailAnnex = mailAnnex;
    }

    public String getMailSendContent() {
        return mailSendContent;
    }

    public void setMailSendContent(String mailSendContent) {
        this.mailSendContent = mailSendContent;
    }

    public Date getFlowTime() {
        return flowTime;
    }

    public void setFlowTime(Date flowTime) {
        this.flowTime = flowTime;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public String getEmailPassword() {
        return emailPassword;
    }

    public void setEmailPassword(String emailPassword) {
        this.emailPassword = emailPassword;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getGroupTime() {
        return groupTime;
    }

    public void setGroupTime(String groupTime) {
        this.groupTime = groupTime;
    }
}
