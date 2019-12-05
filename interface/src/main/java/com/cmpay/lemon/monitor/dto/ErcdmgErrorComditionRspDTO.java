package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.response.PageableRspDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhou_xiong
 */
public class ErcdmgErrorComditionRspDTO extends PageableRspDTO {
    //人员信息
    private List<ErcdmgPordUserDTO> ercdmgPordUserDTOS = new ArrayList<>();
    //错误码信息
    private List<ErcdmgErrorComditionDTO> errorComditionDTOS = new ArrayList<>();
    //错误码更新信息
    private List<ErcdmgUpdmgnDTO> ercdmgUpdmgnDTOS = new ArrayList<>();
    public List<ErcdmgErrorComditionDTO> getErrorComditionDTOS() {
        return errorComditionDTOS;
    }

    public void setErrorComditionDTOS(List<ErcdmgErrorComditionDTO> errorComditionDTOS) {
        this.errorComditionDTOS = errorComditionDTOS;
    }

    public List<ErcdmgPordUserDTO> getErcdmgPordUserDTOS() {
        return ercdmgPordUserDTOS;
    }

    public void setErcdmgPordUserDTOS(List<ErcdmgPordUserDTO> ercdmgPordUserDTOS) {
        this.ercdmgPordUserDTOS = ercdmgPordUserDTOS;
    }

    public List<ErcdmgUpdmgnDTO> getErcdmgUpdmgnDTOS() {
        return ercdmgUpdmgnDTOS;
    }

    public void setErcdmgUpdmgnDTOS(List<ErcdmgUpdmgnDTO> ercdmgUpdmgnDTOS) {
        this.ercdmgUpdmgnDTOS = ercdmgUpdmgnDTOS;
    }

    @Override
    public String toString() {
        return "ErcdmgErrorComditionRspDTO{" +
                "ercdmgPordUserDTOS=" + ercdmgPordUserDTOS +
                ", errorComditionDTOS=" + errorComditionDTOS +
                ", ercdmgUpdmgnDTOS=" + ercdmgUpdmgnDTOS +
                '}';
    }
}
