package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.response.PageableRspDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: wlr
 */
public class ProductionDefectsRspDTO extends PageableRspDTO {
    private List<ProductionDefectsDTO> productionDefectsDTOList ;

    public List<ProductionDefectsDTO> getProductionDefectsDTOList() {
        return productionDefectsDTOList;
    }

    public void setProductionDefectsDTOList(List<ProductionDefectsDTO> productionDefectsDTOList) {
        this.productionDefectsDTOList = productionDefectsDTOList;
    }
}
