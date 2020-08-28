/*
 * @ClassName SupportWorkloadDO
 * @Description
 * @version 1.0
 * @Date 2020-08-27 15:02:17
 */
package com.cmpay.lemon.monitor.bo;

import com.cmpay.lemon.framework.page.PageInfo;

import java.util.List;

/**
 * @author ty
 */
public class SupportWorkloadRspBO {
    private List<SupportWorkloadBO> supportWorkloadBOList ;

    PageInfo<SupportWorkloadBO> pageInfo;

    public List<SupportWorkloadBO> getSupportWorkloadBOList() {
        return supportWorkloadBOList;
    }

    public void setSupportWorkloadBOList(List<SupportWorkloadBO> supportWorkloadBOList) {
        this.supportWorkloadBOList = supportWorkloadBOList;
    }

    public PageInfo<SupportWorkloadBO> getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo<SupportWorkloadBO> pageInfo) {
        this.pageInfo = pageInfo;
    }

    @Override
    public String toString() {
        return "SupportWorkloadRspBO{" +
                "supportWorkloadBOList=" + supportWorkloadBOList +
                '}';
    }
}
