package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.request.GenericDTO;

import java.util.List;

public class ProductionVerificationIsNotTimelyRspDTO extends GenericDTO {
    List<ProductionVerificationIsNotTimelyDTO> productionVerificationIsNotTimelyDTOList;

    public List<ProductionVerificationIsNotTimelyDTO> getProductionVerificationIsNotTimelyDTOList() {
        return productionVerificationIsNotTimelyDTOList;
    }

    public void setProductionVerificationIsNotTimelyDTOList(List<ProductionVerificationIsNotTimelyDTO> productionVerificationIsNotTimelyDTOList) {
        this.productionVerificationIsNotTimelyDTOList = productionVerificationIsNotTimelyDTOList;
    }

    @Override
    public String toString() {
        return "ProductionVerificationIsNotTimelyRspDTO{" +
                "productionVerificationIsNotTimelyDTOList=" + productionVerificationIsNotTimelyDTOList +
                '}';
    }
}
