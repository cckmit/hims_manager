package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.response.PageableRspDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhou_xiong
 */
public class ProductionConditionRspDTO extends PageableRspDTO {
    private List<ProductionDTO> productionList = new ArrayList<>();

    public List<ProductionDTO> getProductionList() {
        return productionList;
    }

    public void setProductionList(List<ProductionDTO> productionList) {
        this.productionList = productionList;
    }

    @Override
    public String toString() {
        return "ProductionConditionRspDTO{" +
                "productionList=" + productionList +
                '}';
    }
}
