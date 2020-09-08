package com.cmpay.lemon.monitor.service.defects;


import com.cmpay.lemon.monitor.bo.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public  interface DefectsService {

    ProductionDefectsRspBO findDefectAll(ProductionDefectsBO productionDefectsBO);

    SmokeTestRegistrationRspBO smokeTestFailedQuery(SmokeTestRegistrationBO smokeTestRegistrationBO);

    void getDownload(HttpServletResponse response,ProductionDefectsBO productionDefectsBO);
    void downloadZenQuestiont(HttpServletResponse response,ZenQuestiontBO zenQuestiontBO);
    void zennDataImport(MultipartFile file);
    ZenQuestiontRspBO zenQuestiontFindList(ZenQuestiontBO zenQuestiontBO);
}
