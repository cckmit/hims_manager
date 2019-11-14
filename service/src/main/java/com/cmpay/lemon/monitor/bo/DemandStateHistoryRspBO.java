package com.cmpay.lemon.monitor.bo;

import com.cmpay.lemon.framework.page.PageInfo;

import java.util.List;

public class DemandStateHistoryRspBO {
    List<DemandStateHistoryBO> demandStateHistoryBOList;
    PageInfo<DemandStateHistoryBO> pageInfo;

    public List<DemandStateHistoryBO> getDemandStateHistoryBOList() {
        return demandStateHistoryBOList;
    }

    public void setDemandStateHistoryBOList(List<DemandStateHistoryBO> demandStateHistoryBOList) {
        this.demandStateHistoryBOList = demandStateHistoryBOList;
    }

    public PageInfo<DemandStateHistoryBO> getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo<DemandStateHistoryBO> pageInfo) {
        this.pageInfo = pageInfo;
    }
}
