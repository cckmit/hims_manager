package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.response.PageableRspDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhou_xiong
 */
public class ErcdmgErrorComditionRspDTO extends PageableRspDTO {
    private List<ErcdmgErrorComditionDTO> errorComditionDTOS = new ArrayList<>();

    public List<ErcdmgErrorComditionDTO> getErrorComditionDTOS() {
        return errorComditionDTOS;
    }

    public void setErrorComditionDTOS(List<ErcdmgErrorComditionDTO> errorComditionDTOS) {
        this.errorComditionDTOS = errorComditionDTOS;
    }

    @Override
    public String toString() {
        return "ErcdmgErrorComditionRspDTO{" +
                "errorComditionDTOS=" + errorComditionDTOS +
                '}';
    }
}
