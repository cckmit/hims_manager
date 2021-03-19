package com.cmpay.lemon.monitor.service.impl.sendmail;

import com.cmpay.lemon.common.utils.DateTimeUtils;
import com.cmpay.lemon.common.utils.JudgeUtils;
import com.cmpay.lemon.monitor.entity.sendemail.*;
import com.cmpay.lemon.monitor.service.sendmail.SendMailService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * 异步发邮件
 */
@Service
public class SendMailServiceImpl implements SendMailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SendMailServiceImpl.class);

    @Async
    @Override
    public void sendMail(MultiMailSenderInfo mailInfo){
         MultiMailsender.sendMailtoMultiTest( mailInfo);
    }

    @Async
    @Override
    public void sendListMail(List<MultiMailSenderInfo> mailInfo)  {
        if(JudgeUtils.isNotEmpty(mailInfo)){
            for (MultiMailSenderInfo mail :mailInfo) {
                try {
                    MultiMailsender.sendMailtoMultiTest(mail);
                    // 邮件间隔10秒
                    Thread.sleep(10000);
                }catch (Exception e ){
                    LOGGER.error("邮件发送异常：" + e.getMessage());
                }


            }

        }

    }
}
