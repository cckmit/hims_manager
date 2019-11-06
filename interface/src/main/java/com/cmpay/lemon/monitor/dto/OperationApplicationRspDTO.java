package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.response.PageableRspDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhou_xiong
 */
public class OperationApplicationRspDTO extends PageableRspDTO {
    private List<OperationApplicationDTO> operationApplicationList = new ArrayList<>();

    public List<OperationApplicationDTO> getOperationApplicationList() {
        return operationApplicationList;
    }

    public void setOperationApplicationList(List<OperationApplicationDTO> operationApplicationList) {
        this.operationApplicationList = operationApplicationList;
    }

    @Override
    public String toString() {
        return "OperationApplicationRspDTO{" +
                "operationApplicationList=" + operationApplicationList +
                '}';
    }
}
