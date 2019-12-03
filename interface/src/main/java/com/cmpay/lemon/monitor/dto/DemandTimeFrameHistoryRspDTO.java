package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.response.PageableRspDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhou_xiong
 */
public class DemandTimeFrameHistoryRspDTO extends PageableRspDTO {
    private List<DemandTimeFrameHistoryDTO> demandTimeFrameHistoryDTOList = new ArrayList<>();

    public List<DemandTimeFrameHistoryDTO> getDemandTimeFrameHistoryDTOList() {
        return demandTimeFrameHistoryDTOList;
    }

    public void setDemandTimeFrameHistoryDTOList(List<DemandTimeFrameHistoryDTO> demandTimeFrameHistoryDTOList) {
        this.demandTimeFrameHistoryDTOList = demandTimeFrameHistoryDTOList;
    }
}
