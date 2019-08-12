package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.response.GenericRspDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhou_xiong
 */
public class LogTypeRspDTO extends GenericRspDTO {
    List<LogTypeDTO> logTypeDTOList = new ArrayList<>();

    public List<LogTypeDTO> getLogTypeDTOList() {
        return logTypeDTOList;
    }

    public void setLogTypeDTOList(List<LogTypeDTO> logTypeDTOList) {
        this.logTypeDTOList = logTypeDTOList;
    }

    @Override
    public String toString() {
        return "LogTypeRspDTO{" +
                "logTypeDTOList=" + logTypeDTOList +
                '}';
    }
}
