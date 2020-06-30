package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.response.PageableRspDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ty
 */
public class DemandQualityRspDTO extends PageableRspDTO {
    private List<DemandQualityDTO> demandQualityDtos = new ArrayList<>();

    public List<DemandQualityDTO> getDemandQualityDtos() {
        return demandQualityDtos;
    }

    public void setDemandQualityDtos(List<DemandQualityDTO> demandQualityDtos) {
        this.demandQualityDtos = demandQualityDtos;
    }

    @Override
    public String toString() {
        return "DemandQualityRspDTO{" +
                "demandQualityDtos=" + demandQualityDtos +
                '}';
    }
}
