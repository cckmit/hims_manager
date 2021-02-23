/*
 * @ClassName ProblemDO
 * @Description
 * @version 1.0
 * @Date 2020-09-25 15:17:26
 */
package com.cmpay.lemon.monitor.dto;


import com.cmpay.framework.data.response.PageableRspDTO;

import java.util.List;


public class ProductionFollowRspDTO extends PageableRspDTO {
    private List<ProductionFollowDTO> productionFollowDTOList ;

    public List<ProductionFollowDTO> getProductionFollowDTOList() {
        return productionFollowDTOList;
    }

    public void setProductionFollowDTOList(List<ProductionFollowDTO> productionFollowDTOList) {
        this.productionFollowDTOList = productionFollowDTOList;
    }

    @Override
    public String toString() {
        return "ProductionFollowRspDTO{" +
                "productionFollowDTOList=" + productionFollowDTOList +
                '}';
    }
}
