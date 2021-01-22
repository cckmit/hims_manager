package com.cmpay.lemon.monitor.service.impl.sendmail;

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

}
