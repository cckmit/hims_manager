package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.response.PageableRspDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhou_xiong
 */
public class DemandStateHistoryRspDTO extends PageableRspDTO {
    private List<DemandStateHistoryDTO> demandStateHistoryDTOList = new ArrayList<>();

    public List<DemandStateHistoryDTO> getDemandStateHistoryDTOList() {
        return demandStateHistoryDTOList;
    }

    public void setDemandStateHistoryDTOList(List<DemandStateHistoryDTO> demandStateHistoryDTOList) {
        this.demandStateHistoryDTOList = demandStateHistoryDTOList;
    }
}
