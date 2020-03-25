/*
 * @ClassName DemandChangeDetailsDO
 * @Description
 * @version 1.0
 * @Date 2019-11-13 10:55:00
 */
package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.response.PageableRspDTO;

import java.util.ArrayList;
import java.util.List;


public class DemandChangeDetailsRspDTO extends PageableRspDTO {
    private List<DemandStateHistoryDTO> demandStateHistoryDTOList = new ArrayList<>();

    public List<DemandStateHistoryDTO> getDemandStateHistoryDTOList() {
        return demandStateHistoryDTOList;
    }

    public void setDemandStateHistoryDTOList(List<DemandStateHistoryDTO> demandStateHistoryDTOList) {
        this.demandStateHistoryDTOList = demandStateHistoryDTOList;
    }
}