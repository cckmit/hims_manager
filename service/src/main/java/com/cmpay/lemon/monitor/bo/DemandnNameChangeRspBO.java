package com.cmpay.lemon.monitor.bo;

import com.cmpay.lemon.framework.page.PageInfo;

import java.util.List;
/**
 * @author TY
 */
public class DemandnNameChangeRspBO {
    List<DemandNameChangeBO> demandNameChangeBOS;
    PageInfo<DemandNameChangeBO> pageInfo;

    public List<DemandNameChangeBO> getDemandNameChangeBOS() {
        return demandNameChangeBOS;
    }

    public void setDemandNameChangeBOS(List<DemandNameChangeBO> demandNameChangeBOS) {
        this.demandNameChangeBOS = demandNameChangeBOS;
    }

    public PageInfo<DemandNameChangeBO> getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo<DemandNameChangeBO> pageInfo) {
        this.pageInfo = pageInfo;
    }

    @Override
    public String toString() {
        return "DemandnNameChangeRspBO{" +
                "demandNameChangeBOS=" + demandNameChangeBOS +
                ", pageInfo=" + pageInfo +
                '}';
    }
}
