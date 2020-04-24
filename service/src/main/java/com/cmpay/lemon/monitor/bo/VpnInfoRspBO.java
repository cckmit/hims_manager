package com.cmpay.lemon.monitor.bo;

import com.cmpay.lemon.framework.page.PageInfo;

import java.util.List;

public class VpnInfoRspBO {
    List<VpnInfoBO> vpnInfoBOList;
    PageInfo<VpnInfoBO> pageInfo;

    public List<VpnInfoBO> getVpnInfoBOList() {
        return vpnInfoBOList;
    }

    public void setVpnInfoBOList(List<VpnInfoBO> vpnInfoBOList) {
        this.vpnInfoBOList = vpnInfoBOList;
    }

    public PageInfo<VpnInfoBO> getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo<VpnInfoBO> pageInfo) {
        this.pageInfo = pageInfo;
    }
}
