package com.cmpay.lemon.monitor.service.defects;


import com.cmpay.lemon.monitor.bo.ProductionDefectsBO;
import com.cmpay.lemon.monitor.bo.ProductionDefectsRspBO;
import com.cmpay.lemon.monitor.bo.SmokeTestRegistrationBO;
import com.cmpay.lemon.monitor.bo.SmokeTestRegistrationRspBO;

public  interface DefectsService {

    ProductionDefectsRspBO findDefectAll(ProductionDefectsBO productionDefectsBO);

    SmokeTestRegistrationRspBO smokeTestFailedQuery(SmokeTestRegistrationBO smokeTestRegistrationBO);
}
