/*
 * @ClassName DemandEaseDevelopmentDO
 * @Description
 * @version 1.0
 * @Date 2020-08-31 14:33:51
 */
package com.cmpay.lemon.monitor.dto;


import com.cmpay.framework.data.response.PageableRspDTO;

import java.util.List;

public class DemandEaseDevelopmentRspDTO extends PageableRspDTO {
 private List<DemandEaseDevelopmentDTO> demandEaseDevelopmentDTOList;

    public List<DemandEaseDevelopmentDTO> getDemandEaseDevelopmentDTOList() {
        return demandEaseDevelopmentDTOList;
    }

    public void setDemandEaseDevelopmentDTOList(List<DemandEaseDevelopmentDTO> demandEaseDevelopmentDTOList) {
        this.demandEaseDevelopmentDTOList = demandEaseDevelopmentDTOList;
    }

    @Override
    public String toString() {
        return "DemandEaseDevelopmentRspDTO{" +
                "demandEaseDevelopmentDTOList=" + demandEaseDevelopmentDTOList +
                '}';
    }
}
