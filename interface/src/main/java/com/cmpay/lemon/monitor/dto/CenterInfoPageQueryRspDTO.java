package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.response.PageableRspDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhou_xiong
 */
public class CenterInfoPageQueryRspDTO extends PageableRspDTO {
    private List<CenterDTO> centerDTOList = new ArrayList<>();

    public List<CenterDTO> getCenterDTOList() {
        return centerDTOList;
    }

    public void setCenterDTOList(List<CenterDTO> centerDTOList) {
        this.centerDTOList = centerDTOList;
    }

    @Override
    public String toString() {
        return "CenterInfoPageQueryRspDTO{" +
                "centerDTOList=" + centerDTOList +
                '}';
    }
}
