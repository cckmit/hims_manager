package com.cmpay.lemon.monitor.service.automaticCommissioningInterface;

import com.cmpay.lemon.monitor.bo.AutoCancellationProductionBO;
import com.cmpay.lemon.monitor.bo.AutomatedProductionBO;

public interface AutomaticCommissioningInterfaceService {

    String automatedProduction(AutomatedProductionBO automatedProductionBO);
    String autoCancellationProduction(AutoCancellationProductionBO autoCancellationProductionBO);
}
