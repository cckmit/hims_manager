package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.response.PageableRspDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhou_xiong
 */
public class ScheduleRspDTO extends PageableRspDTO {
    private List<ScheduleDTO> scheduleDTOList = new ArrayList<>();

    public List<ScheduleDTO> getDemandDTOList() {
        return scheduleDTOList;
    }

    public void setDemandDTOList(List<ScheduleDTO> demandDTOList) {
        this.scheduleDTOList = demandDTOList;
    }

    @Override
    public String toString() {
        return "CenterInfoPageQueryRspDTO{" +
                "centerDTOList=" + scheduleDTOList +
                '}';
    }
}
