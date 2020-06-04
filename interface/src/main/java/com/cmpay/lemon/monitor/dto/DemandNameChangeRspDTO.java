package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.response.PageableRspDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhou_xiong
 */
public class DemandNameChangeRspDTO extends PageableRspDTO {
    private List<DemandNameChangeDTO> demandDTOList = new ArrayList<>();

    public List<DemandNameChangeDTO> getDemandDTOList() {
        return demandDTOList;
    }

    public void setDemandDTOList(List<DemandNameChangeDTO> demandDTOList) {
        this.demandDTOList = demandDTOList;
    }

    @Override
    public String toString() {
        return "DemandNameChangeRspDTO{" +
                "demandDTOList=" + demandDTOList +
                '}';
    }
}
