package com.cmpay.lemon.monitor.service.production;


import com.cmpay.lemon.monitor.bo.DemandBO;
import com.cmpay.lemon.monitor.bo.DemandRspBO;
import com.cmpay.lemon.monitor.bo.ProductionBO;
import com.cmpay.lemon.monitor.bo.ProductionRspBO;
import com.cmpay.lemon.monitor.entity.DemandDO;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

/**
 * @author: zhou_xiong
 */
public interface OperationProductionService {

    /**
     * 分页查询
     *
     * @param productionBO
     * @return
     */
    ProductionRspBO find(ProductionBO productionBO);
    void exportExcel(HttpServletRequest request, HttpServletResponse response, ProductionBO productionBO);

    void updateAllProduction(HttpServletRequest request, HttpServletResponse response, String str);

}
