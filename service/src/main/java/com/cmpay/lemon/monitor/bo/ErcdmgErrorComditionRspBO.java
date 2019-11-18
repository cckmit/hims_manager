package com.cmpay.lemon.monitor.bo;

import com.cmpay.lemon.framework.page.PageInfo;

import java.util.List;

public class ErcdmgErrorComditionRspBO {
    List<ErcdmgErrorComditionBO> ercdmgErrorComditionBOList;
    PageInfo<ErcdmgErrorComditionBO> pageInfo;

    public List<ErcdmgErrorComditionBO> getErcdmgErrorComditionBOList() {
        return ercdmgErrorComditionBOList;
    }

    public void setErcdmgErrorComditionBOList(List<ErcdmgErrorComditionBO> ercdmgErrorComditionBOList) {
        this.ercdmgErrorComditionBOList = ercdmgErrorComditionBOList;
    }

    public PageInfo<ErcdmgErrorComditionBO> getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo<ErcdmgErrorComditionBO> pageInfo) {
        this.pageInfo = pageInfo;
    }
}
