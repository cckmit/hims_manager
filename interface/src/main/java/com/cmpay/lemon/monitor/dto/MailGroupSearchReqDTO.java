package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.response.PageableRspDTO;

import java.util.Date;

public class MailGroupSearchReqDTO extends PageableRspDTO {

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
    private String orderDirection;

    public MailGroupSearchReqDTO() {
    }

    public MailGroupSearchReqDTO(String mailGroupName,
                       String mailUser, String mailGroupDesc) {
        this.mailGroupName = mailGroupName;
        this.mailUser = mailUser;
        this. mailGroupDesc = mailGroupDesc;
    }

    public MailGroupSearchReqDTO(String mailGroupId, String mailGroupName,
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
    public int getPageNum() {
        return pageNum;
    }

    @Override
    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrderDirection() {
        return orderDirection;
    }

    public void setOrderDirection(String orderDirection) {
        this.orderDirection = orderDirection;
    }
}
