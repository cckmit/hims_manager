package com.cmpay.lemon.monitor.bo;

import com.cmpay.lemon.framework.page.PageInfo;

import java.util.List;
/**
 * @author TY
 */
public class DemandRspBO {
    List<DemandBO> demandBOList;
    PageInfo<DemandBO> pageInfo;

    public List<DemandBO> getDemandBOList() {
        return demandBOList;
    }

    public void setDemandBOList(List<DemandBO> demandBOList) {
        this.demandBOList = demandBOList;
    }

    public PageInfo<DemandBO> getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo<DemandBO> pageInfo) {
        this.pageInfo = pageInfo;
    }

    @Override
    public String toString() {
        return "DemandRspBO{" +
                "demandBOList=" + demandBOList +
                ", pageInfo=" + pageInfo +
                '}';
    }
}
