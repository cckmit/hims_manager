package com.cmpay.lemon.monitor.bo;


import com.cmpay.lemon.framework.annotation.DataObject;
import com.cmpay.lemon.framework.page.PageInfo;

import java.util.List;

@DataObject
public class DemandCurperiodHistoryRspBO {
    List<DemandCurperiodHistoryBO> demandCurperiodHistoryBOList;
    PageInfo<DemandCurperiodHistoryBO> pageInfo;

    public List<DemandCurperiodHistoryBO> getDemandCurperiodHistoryBOList() {
        return demandCurperiodHistoryBOList;
    }

    public void setDemandCurperiodHistoryBOList(List<DemandCurperiodHistoryBO> demandCurperiodHistoryBOList) {
        this.demandCurperiodHistoryBOList = demandCurperiodHistoryBOList;
    }

    public PageInfo<DemandCurperiodHistoryBO> getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo<DemandCurperiodHistoryBO> pageInfo) {
        this.pageInfo = pageInfo;
    }
}