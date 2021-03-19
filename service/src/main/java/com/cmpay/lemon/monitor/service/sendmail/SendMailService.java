package com.cmpay.lemon.monitor.service.sendmail;


import com.cmpay.lemon.monitor.bo.AutomatedProductionCallbackReqBO;
import com.cmpay.lemon.monitor.bo.DemandBO;
import com.cmpay.lemon.monitor.bo.PreproductionBO;
import com.cmpay.lemon.monitor.bo.PreproductionRspBO;
import com.cmpay.lemon.monitor.entity.sendemail.MultiMailSenderInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author: zhou_xiong
 */
public interface SendMailService {
    void sendMail(MultiMailSenderInfo mailInfo);

    void sendListMail(List<MultiMailSenderInfo> mailInfo)  ;

}
