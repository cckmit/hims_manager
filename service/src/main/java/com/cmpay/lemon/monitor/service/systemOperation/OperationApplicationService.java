package com.cmpay.lemon.monitor.service.systemOperation;


import com.cmpay.lemon.monitor.bo.*;

/**
 * @author: zhou_xiong
 */
public interface OperationApplicationService {

    /**
     * 分页查询
     *
     * @param operationApplicationBO
     * @return
     */
    OperationApplicationRspBO find(OperationApplicationBO operationApplicationBO);

}
