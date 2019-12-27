package com.cmpay.lemon.monitor.dto;

import java.util.List;

public class ProductionTimeReqDTO  {
    List<ProductionTimeDTO> productTime;

    public List<ProductionTimeDTO> getProductTime() {
        return productTime;
    }

    public void setProductTime(List<ProductionTimeDTO> productTime) {
        this.productTime = productTime;
    }
}
