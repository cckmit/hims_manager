/*
 * @ClassName DemandEaseDevelopmentDO
 * @Description
 * @version 1.0
 * @Date 2020-08-31 14:33:51
 */
package com.cmpay.lemon.monitor.bo;


import com.cmpay.lemon.framework.page.PageInfo;

import java.util.List;

public class DemandEaseDevelopmentRspBO {
    private List<DemandEaseDevelopmentBO> demandEaseDevelopmentBOList;
    private PageInfo<DemandEaseDevelopmentBO> pageInfo;

    public PageInfo<DemandEaseDevelopmentBO> getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo<DemandEaseDevelopmentBO> pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<DemandEaseDevelopmentBO> getDemandEaseDevelopmentBOList() {
        return demandEaseDevelopmentBOList;
    }

    public void setDemandEaseDevelopmentBOList(List<DemandEaseDevelopmentBO> demandEaseDevelopmentBOList) {
        this.demandEaseDevelopmentBOList = demandEaseDevelopmentBOList;
    }

    @Override
    public String toString() {
        return "DemandEaseDevelopmentRspBO{" +
                "demandEaseDevelopmentBOList=" + demandEaseDevelopmentBOList +
                ", pageInfo=" + pageInfo +
                '}';
    }
}
