package com.cmpay.lemon.monitor.dto;


import com.cmpay.framework.data.response.PageableRspDTO;
import com.cmpay.lemon.framework.annotation.DataObject;

import java.util.ArrayList;
import java.util.List;

@DataObject
public class DemandCurperiodHistoryRspDTO extends PageableRspDTO {
    private List<DemandCurperiodHistoryDTO> demandCurperiodHistoryDTOList = new ArrayList<>();

    public List<DemandCurperiodHistoryDTO> getDemandCurperiodHistoryDTOList() {
        return demandCurperiodHistoryDTOList;
    }

    public void setDemandCurperiodHistoryDTOList(List<DemandCurperiodHistoryDTO> demandCurperiodHistoryDTOList) {
        this.demandCurperiodHistoryDTOList = demandCurperiodHistoryDTOList;
    }
}