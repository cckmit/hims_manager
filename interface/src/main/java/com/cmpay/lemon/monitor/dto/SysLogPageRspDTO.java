package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.response.PageableRspDTO;

import java.util.List;

/**
 * Created on 2018/12/25
 *
 * @author zhou_xiong
 */
public class SysLogPageRspDTO extends PageableRspDTO {
    private List<SysLogDTO> sysLogDTOList ;

    public List<SysLogDTO> getSysLogDTOList() {
        return sysLogDTOList;
    }

    public void setSysLogDTOList(List<SysLogDTO> sysLogDTOList) {
        this.sysLogDTOList = sysLogDTOList;
    }

    @Override
    public String toString() {
        return "SysLogPageRspDTO{" +
                "sysLogDTOList=" + sysLogDTOList +
                '}';
    }
}
