package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.response.PageableRspDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhou_xiong
 */
public class PreproductionRspDTO extends PageableRspDTO {
    private List<PreproductionDTO> preproductionDTOList = new ArrayList<>();

    public List<PreproductionDTO> getPreproductionDTOList() {
        return preproductionDTOList;
    }

    public void setPreproductionDTOList(List<PreproductionDTO> preproductionDTOList) {
        this.preproductionDTOList = preproductionDTOList;
    }

    @Override
    public String toString() {
        return "PreproductionRspDTO{" +
                "preproductionDTOList=" + preproductionDTOList +
                '}';
    }
}
