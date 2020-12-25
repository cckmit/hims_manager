package com.cmpay.lemon.monitor.service.preproduction;


import com.cmpay.lemon.monitor.bo.*;
import com.cmpay.lemon.monitor.entity.sendemail.MultiMailSenderInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: zhou_xiong
 */
public interface PreProductionService {

    /**
     * 分页查询
     *
     * @param productionBO
     * @return
     */
    PreproductionRspBO find(PreproductionBO productionBO);
    void update(PreproductionBO productionBO);
    void updateAllProduction(String str);
    void updateAllProductionDBA(String str);
    void updateAllProductionBBZ(String str);
    void updateAllProductionVerificationFailed(String str);
    void add(PreproductionBO productionBO);
    void doBatchImport(MultipartFile file,String reqNumber);
    void updateProductionPackage(MultipartFile file,String reqNumber);
    //投产包下载
    void pkgDownload(HttpServletRequest request, HttpServletResponse response, String str);
    //投产包下载
    void dbaDownload(HttpServletRequest request, HttpServletResponse response, String str);
    DemandBO verifyAndQueryTheProductionNumber(String proNumber);
    PreproductionBO againProductionNumber(String proNumber);
    void updateState(AutomatedProductionCallbackReqBO productionCallbackBO);

    void automatedProductionCallback(AutomatedProductionCallbackReqBO productionCallbackBO);

}
