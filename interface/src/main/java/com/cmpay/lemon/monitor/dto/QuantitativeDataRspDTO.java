/*
 * @ClassName QuantitativeDataDO
 * @Description
 * @version 1.0
 * @Date 2020-11-02 16:52:35
 */
package com.cmpay.lemon.monitor.dto;


import com.cmpay.framework.data.request.GenericDTO;
import com.cmpay.framework.data.response.PageableRspDTO;

import java.util.ArrayList;
import java.util.List;

public class QuantitativeDataRspDTO  extends PageableRspDTO {
    private List<QuantitativeDataDTO> quantitativeDataDTOArrayList = new ArrayList<>();

    public List<QuantitativeDataDTO> getQuantitativeDataDTOArrayList() {
        return quantitativeDataDTOArrayList;
    }

    public void setQuantitativeDataDTOArrayList(List<QuantitativeDataDTO> quantitativeDataDTOArrayList) {
        this.quantitativeDataDTOArrayList = quantitativeDataDTOArrayList;
    }

    @Override
    public String toString() {
        return "QuantitativeDataRspDTO{" +
                "quantitativeDataDTOArrayList=" + quantitativeDataDTOArrayList +
                '}';
    }
}
