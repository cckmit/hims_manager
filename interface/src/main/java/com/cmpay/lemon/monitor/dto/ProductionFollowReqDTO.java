/*
 * @ClassName ProductionFollowDO
 * @Description
 * @version 1.0
 * @Date 2021-02-02 16:03:37
 */
package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.request.GenericDTO;
import com.cmpay.framework.data.response.PageableRspDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class ProductionFollowReqDTO extends PageableRspDTO {
    private List<ProductionFollowDTO> productionFollowDTOList = new ArrayList<>();

    public List<ProductionFollowDTO> getProductionFollowDTOList() {
        return productionFollowDTOList;
    }

    public void setProductionFollowDTOList(List<ProductionFollowDTO> productionFollowDTOList) {
        this.productionFollowDTOList = productionFollowDTOList;
    }

    @Override
    public String toString() {
        return "ProductionFollowReqDTO{" +
                "productionFollowDTOList=" + productionFollowDTOList +
                '}';
    }
}
