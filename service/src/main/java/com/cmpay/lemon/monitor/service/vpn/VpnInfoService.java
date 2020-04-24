package com.cmpay.lemon.monitor.service.vpn;


import com.cmpay.lemon.monitor.bo.VpnInfoBO;
import com.cmpay.lemon.monitor.bo.VpnInfoRspBO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface VpnInfoService {
    void add(VpnInfoBO vpnInfoBO);
    VpnInfoRspBO find(VpnInfoBO vpnInfoBO);
    void update(VpnInfoBO vpnInfoBO);
    void updateAllProduction(HttpServletRequest request, HttpServletResponse response, String str);

}
