package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.response.PageableRspDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhou_xiong
 */
public class VpnInfoRspDTO extends PageableRspDTO {
    private List<VpnInfoDTO> vpnInfoDTOS = new ArrayList<>();

    public List<VpnInfoDTO> getVpnInfoDTOS() {
        return vpnInfoDTOS;
    }

    public void setVpnInfoDTOS(List<VpnInfoDTO> vpnInfoDTOS) {
        this.vpnInfoDTOS = vpnInfoDTOS;
    }

    @Override
    public String toString() {
        return "VpnInfoRspDTO{" +
                "vpnInfoDTOS=" + vpnInfoDTOS +
                '}';
    }
}
