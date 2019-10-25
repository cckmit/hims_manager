package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.response.PageableRspDTO;
import com.cmpay.lemon.monitor.bo.MailGroupBO;

import java.util.ArrayList;
import java.util.List;

public class MailGroupSearchRspDTO extends PageableRspDTO {

    private List<MailGroupBO> mailGroupList = new ArrayList<>();

    public List<MailGroupBO> getMailGroupList() {
        return mailGroupList;
    }

    public void setMailGroupBOList(List<MailGroupBO> mailGroupList) {
        this.mailGroupList = mailGroupList;
    }
}
