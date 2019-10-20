package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.request.GenericDTO;

import java.util.List;

public class ProductionTimeRspDTO extends GenericDTO {
    List<ProductionTimeDTO> productTimes;

    String minProDate;

    String maxProDate;

    public List<ProductionTimeDTO> getProductTimes() {
        return productTimes;
    }

    public void setProductTimes(List<ProductionTimeDTO> productTimes) {
        this.productTimes = productTimes;
    }

    public String getMinProDate() {
        return minProDate;
    }

    public void setMinProDate(String minProDate) {
        this.minProDate = minProDate;
    }

    public String getMaxProDate() {
        return maxProDate;
    }

    public void setMaxProDate(String maxProDate) {
        this.maxProDate = maxProDate;
    }
}
