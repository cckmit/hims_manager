package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.response.PageableRspDTO;

import java.util.ArrayList;
import java.util.List;

public class MailGroupSearchRspDTO extends PageableRspDTO {

    private List<MailGroupDTO> mailGroupList = new ArrayList<>();

    public List<MailGroupDTO> getMailGroupList() {
        return mailGroupList;
    }

    public void setMailGroupBOList(List<MailGroupDTO> mailGroupList) {
        this.mailGroupList = mailGroupList;
    }
}
