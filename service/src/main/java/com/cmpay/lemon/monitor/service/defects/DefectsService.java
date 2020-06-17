package com.cmpay.lemon.monitor.service.defects;


import com.cmpay.lemon.monitor.bo.ProductionDefectsBO;
import com.cmpay.lemon.monitor.bo.ProductionDefectsRspBO;

public  interface DefectsService {

    ProductionDefectsRspBO findDefectAll(ProductionDefectsBO productionDefectsBO);
}
