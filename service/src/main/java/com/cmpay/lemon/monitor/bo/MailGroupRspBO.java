package com.cmpay.lemon.monitor.bo;

import com.cmpay.lemon.framework.page.PageInfo;

import java.util.List;

public class MailGroupRspBO {
    List<MailGroupBO> MailGroupBOList;
    PageInfo<MailGroupBO> pageInfo;

    public List<MailGroupBO> getMailGroupBOList() {
        return MailGroupBOList;
    }

    public void setMailGroupBOList(List<MailGroupBO> mailGroupBOList) {
        MailGroupBOList = mailGroupBOList;
    }

    public PageInfo<MailGroupBO> getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo<MailGroupBO> pageInfo) {
        this.pageInfo = pageInfo;
    }
}
