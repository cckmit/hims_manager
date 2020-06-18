package com.cmpay.lemon.monitor.bo;

/*
 * @ClassName DemandNameChangeDO
 * @Description
 * @version 1.0
 * @Date 2020-06-03 10:21:06
 */


import com.cmpay.lemon.framework.page.PageInfo;

import java.util.List;

public class SmokeTestRegistrationRspBO {
    List<SmokeTestRegistrationBO> smokeTestRegistrationBOList;
    PageInfo<SmokeTestRegistrationBO> pageInfo;

    public List<SmokeTestRegistrationBO> getSmokeTestRegistrationBOList() {
        return smokeTestRegistrationBOList;
    }

    public void setSmokeTestRegistrationBOList(List<SmokeTestRegistrationBO> smokeTestRegistrationBOList) {
        this.smokeTestRegistrationBOList = smokeTestRegistrationBOList;
    }

    public PageInfo<SmokeTestRegistrationBO> getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo<SmokeTestRegistrationBO> pageInfo) {
        this.pageInfo = pageInfo;
    }
}

