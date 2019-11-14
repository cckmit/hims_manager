package com.cmpay.lemon.monitor.bo;

import com.cmpay.lemon.framework.page.PageInfo;

import java.util.List;

public class DemandChangeDetailsRspBO {
    List<DemandChangeDetailsBO> demandBOList;
    PageInfo<DemandChangeDetailsBO> pageInfo;

    public List<DemandChangeDetailsBO> getDemandBOList() {
        return demandBOList;
    }

    public void setDemandBOList(List<DemandChangeDetailsBO> demandBOList) {
        this.demandBOList = demandBOList;
    }

    public PageInfo<DemandChangeDetailsBO> getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo<DemandChangeDetailsBO> pageInfo) {
        this.pageInfo = pageInfo;
    }
}
