/*
 * @ClassName OnlineDefectDO
 * @Description
 * @version 1.0
 * @Date 2020-10-19 14:11:34
 */
package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.response.PageableRspDTO;

import java.util.List;


public class OnlineDefectRspDTO extends PageableRspDTO {
    private List<OnlineDefectDTO> onlineDefectDTOList;

    public List<OnlineDefectDTO> getOnlineDefectDTOList() {
        return onlineDefectDTOList;
    }

    public void setOnlineDefectDTOList(List<OnlineDefectDTO> onlineDefectDTOList) {
        this.onlineDefectDTOList = onlineDefectDTOList;
    }

    @Override
    public String toString() {
        return "OnlineDefectRspDTO{" +
                "onlineDefectDTOList=" + onlineDefectDTOList +
                '}';
    }
}
