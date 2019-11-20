package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.response.PageableRspDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhou_xiong
 */
public class ProductionRspDTO extends PageableRspDTO {
    private List<ProductionDTO> productionList = new ArrayList<>();

    public List<ProductionDTO> getProductionList() {
        return productionList;
    }

    public void setProductionList(List<ProductionDTO> productionList) {
        this.productionList = productionList;
    }
}
