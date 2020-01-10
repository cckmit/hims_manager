package com.cmpay.lemon.monitor.service.preproduction;


import com.cmpay.lemon.monitor.bo.*;
import com.cmpay.lemon.monitor.entity.OperationApplicationDO;
import com.cmpay.lemon.monitor.entity.ProductionDO;
import com.cmpay.lemon.monitor.entity.ProductionPicDO;
import com.cmpay.lemon.monitor.entity.ScheduleDO;
import com.cmpay.lemon.monitor.entity.sendemail.MailFlowBean;
import com.cmpay.lemon.monitor.entity.sendemail.MailFlowConditionDO;
import com.cmpay.lemon.monitor.entity.sendemail.MailFlowDO;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
import java.util.Vector;

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
    void updateAllProduction(HttpServletRequest request, HttpServletResponse response, String str);
    void add(PreproductionBO productionBO);
    void doBatchImport(MultipartFile file,String reqNumber);
    //投产包下载
    void pkgDownload(HttpServletRequest request, HttpServletResponse response, String str);

    DemandBO verifyAndQueryTheProductionNumber(String proNumber);
}
