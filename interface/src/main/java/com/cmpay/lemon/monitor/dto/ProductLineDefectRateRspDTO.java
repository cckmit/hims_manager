package com.cmpay.lemon.monitor.dto;

import java.util.List;

public class ProductLineDefectRateRspDTO {

    private List<ProductLineDefectsDTO> productLineDefectsList;

    public List<ProductLineDefectsDTO> getProductLineDefectsList() {
        return productLineDefectsList;
    }

    public void setProductLineDefectsList(List<ProductLineDefectsDTO> productLineDefectsList) {
        this.productLineDefectsList = productLineDefectsList;
    }
}
