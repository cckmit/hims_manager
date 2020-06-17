package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.response.PageableRspDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhou_xiong
 */
public class ProductionDefectsRspDTO extends PageableRspDTO {
    private List<ProductionDefectsDTO> productionDefectsDTOList = new ArrayList<>();

    public List<ProductionDefectsDTO> getProductionDefectsDTOList() {
        return productionDefectsDTOList;
    }

    public void setProductionDefectsDTOList(List<ProductionDefectsDTO> productionDefectsDTOList) {
        this.productionDefectsDTOList = productionDefectsDTOList;
    }
}
