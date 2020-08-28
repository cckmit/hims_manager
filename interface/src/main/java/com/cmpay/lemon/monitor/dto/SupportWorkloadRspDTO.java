/*
 * @ClassName SupportWorkloadDO
 * @Description
 * @version 1.0
 * @Date 2020-08-27 15:02:17
 */
package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.response.PageableRspDTO;

import java.util.List;

/**
 * @author ty
 */
public class SupportWorkloadRspDTO extends PageableRspDTO {
    private List<SupportWorkloadDTO> supportWorkloadDTOList;

    public List<SupportWorkloadDTO> getSupportWorkloadDTOList() {
        return supportWorkloadDTOList;
    }

    public void setSupportWorkloadDTOList(List<SupportWorkloadDTO> supportWorkloadDTOList) {
        this.supportWorkloadDTOList = supportWorkloadDTOList;
    }

    @Override
    public String toString() {
        return "SupportWorkloadRspDTO{" +
                "supportWorkloadDTOList=" + supportWorkloadDTOList +
                '}';
    }
}
