package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.response.PageableRspDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhou_xiong
 */
public class DemandRspDTO extends PageableRspDTO {
    private List<DemandDTO> demandDTOList = new ArrayList<>();

    public List<DemandDTO> getDemandDTOList() {
        return demandDTOList;
    }

    public void setDemandDTOList(List<DemandDTO> demandDTOList) {
        this.demandDTOList = demandDTOList;
    }

    @Override
    public String toString() {
        return "CenterInfoPageQueryRspDTO{" +
                "centerDTOList=" + demandDTOList +
                '}';
    }
}
