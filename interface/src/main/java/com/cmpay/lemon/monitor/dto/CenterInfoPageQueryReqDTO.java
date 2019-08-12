package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.response.PageableRspDTO;

/**
 * @author: zhou_xiong
 */
public class CenterInfoPageQueryReqDTO extends PageableRspDTO {
    private String centerName;

    public String getCenterName() {
        return centerName;
    }

    public void setCenterName(String centerName) {
        this.centerName = centerName;
    }

    @Override
    public String toString() {
        return "CenterInfoPageQueryReqDTO{" +
                "centerName='" + centerName + '\'' +
                '}';
    }
}
