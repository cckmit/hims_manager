package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.response.PageableRspDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhou_xiong
 */
public class WorkingHoursRspDTO extends PageableRspDTO {
    private List<WorkingHoursDTO> vpnInfoDTOS = new ArrayList<>();
    private List<String> stringList = new ArrayList<>();

    public List<WorkingHoursDTO> getVpnInfoDTOS() {
        return vpnInfoDTOS;
    }

    public void setVpnInfoDTOS(List<WorkingHoursDTO> vpnInfoDTOS) {
        this.vpnInfoDTOS = vpnInfoDTOS;
    }

    public List<String> getStringList() {
        return stringList;
    }

    public void setStringList(List<String> stringList) {
        this.stringList = stringList;
    }
}
