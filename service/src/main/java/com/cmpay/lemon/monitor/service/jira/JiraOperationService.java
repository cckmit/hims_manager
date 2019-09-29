package com.cmpay.lemon.monitor.service.jira;

import com.cmpay.lemon.monitor.bo.DemandBO;
import com.cmpay.lemon.monitor.entity.DemandDO;

import java.util.List;

public interface JiraOperationService {

    void createEpic(DemandBO demandBO);

    void batchCreateEpic(List<DemandDO> demandDOList);

}
