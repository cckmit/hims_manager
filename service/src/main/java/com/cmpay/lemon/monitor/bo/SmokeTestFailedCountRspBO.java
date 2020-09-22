/*
 * @ClassName SmokeTestFailedCountDO
 * @Description
 * @version 1.0
 * @Date 2020-09-16 16:08:58
 */
package com.cmpay.lemon.monitor.bo;


import com.cmpay.lemon.framework.page.PageInfo;

import java.util.List;

/**
 * @author ty
 */
public class SmokeTestFailedCountRspBO {
    List<SmokeTestFailedCountBO> smokeTestFailedCountBOList;
    PageInfo<SmokeTestRegistrationBO> pageInfo;

    public List<SmokeTestFailedCountBO> getSmokeTestFailedCountBOList() {
        return smokeTestFailedCountBOList;
    }

    public void setSmokeTestFailedCountBOList(List<SmokeTestFailedCountBO> smokeTestFailedCountBOList) {
        this.smokeTestFailedCountBOList = smokeTestFailedCountBOList;
    }

    public PageInfo<SmokeTestRegistrationBO> getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo<SmokeTestRegistrationBO> pageInfo) {
        this.pageInfo = pageInfo;
    }

    @Override
    public String toString() {
        return "SmokeTestFailedCountRspBO{" +
                "smokeTestFailedCountBOList=" + smokeTestFailedCountBOList +
                ", pageInfo=" + pageInfo +
                '}';
    }
}
