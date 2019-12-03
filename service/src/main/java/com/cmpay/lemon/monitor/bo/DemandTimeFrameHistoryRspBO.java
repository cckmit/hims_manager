package com.cmpay.lemon.monitor.bo;

import com.cmpay.framework.data.response.PageableRspDTO;
import com.cmpay.lemon.framework.page.PageInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhou_xiong
 */
public class DemandTimeFrameHistoryRspBO extends PageableRspDTO {
    private List<DemandTimeFrameHistoryBO> demandTimeFrameHistoryBOList = new ArrayList<>();
    PageInfo<DemandTimeFrameHistoryBO> pageInfo;

    public List<DemandTimeFrameHistoryBO> getDemandTimeFrameHistoryBOList() {
        return demandTimeFrameHistoryBOList;
    }

    public void setDemandTimeFrameHistoryBOList(List<DemandTimeFrameHistoryBO> demandTimeFrameHistoryBOList) {
        this.demandTimeFrameHistoryBOList = demandTimeFrameHistoryBOList;
    }

    public PageInfo<DemandTimeFrameHistoryBO> getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo<DemandTimeFrameHistoryBO> pageInfo) {
        this.pageInfo = pageInfo;
    }
}
