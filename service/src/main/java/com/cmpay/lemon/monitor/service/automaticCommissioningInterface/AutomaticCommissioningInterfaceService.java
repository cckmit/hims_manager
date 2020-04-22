package com.cmpay.lemon.monitor.service.automaticCommissioningInterface;

import com.cmpay.lemon.monitor.bo.AutoCancellationProductionBO;
import com.cmpay.lemon.monitor.bo.AutomatedProductionBO;

public interface AutomaticCommissioningInterfaceService {

    void automatedProduction(AutomatedProductionBO automatedProductionBO);
    void autoCancellationProduction(AutoCancellationProductionBO autoCancellationProductionBO);
}
