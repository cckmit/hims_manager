package com.cmpay.lemon.monitor.service.errror;

import com.cmpay.lemon.monitor.bo.*;

/**
 * @author: tu_yi
 */
public interface ErrorService {

    /**
     * 分页查询
     *
     * @param ercdmgErrorComditionBO
     * @return
     */
    ErcdmgErrorComditionRspBO searchErroeList (ErcdmgErrorComditionBO ercdmgErrorComditionBO);
}
