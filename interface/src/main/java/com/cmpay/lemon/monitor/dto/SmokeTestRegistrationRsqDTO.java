package com.cmpay.lemon.monitor.dto;

/*
 * @ClassName DemandNameChangeDO
 * @Description
 * @version 1.0
 * @Date 2020-06-03 10:21:06
 */




import com.cmpay.framework.data.response.PageableRspDTO;

import java.util.List;

public class SmokeTestRegistrationRsqDTO extends PageableRspDTO {
    private List<SmokeTestRegistrationDTO> smokeTestRegistrationDTOList;

    public List<SmokeTestRegistrationDTO> getSmokeTestRegistrationDTOList() {
        return smokeTestRegistrationDTOList;
    }

    public void setSmokeTestRegistrationDTOList(List<SmokeTestRegistrationDTO> smokeTestRegistrationDTOList) {
        this.smokeTestRegistrationDTOList = smokeTestRegistrationDTOList;
    }
}

