package com.cmpay.lemon.monitor.entity.sendemail;

import com.cmpay.lemon.common.Env;
import com.cmpay.lemon.common.exception.BusinessException;
import com.cmpay.lemon.framework.utils.LemonUtils;
import com.cmpay.lemon.monitor.bo.TestProgressDetailRspBO;
import com.cmpay.lemon.monitor.entity.Constant;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.impl.reportForm.ReqDataCountServiceImpl;
import com.cmpay.lemon.monitor.utils.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class MultiMailsender {
    private static Logger logger = Logger.getLogger(MultiMailsender.class);
    public static boolean sendMailtoMultiReceiver(MailSenderInfo mailInfo){
    	 // 判断是否需要身份认证
	      MyAuthenticator authenticator = null;
	     Properties pro = mailInfo.getProperties();
	      //如果需要身份认证，则创建一个密码验证器
	      if (mailInfo.isValidate()) {
	        authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
	      }
	      // 根据邮件会话属性和密码验证器构造一个发送邮件的session
	      Session sendMailSession = Session.getDefaultInstance(pro,authenticator);
	      try {
	      // 根据session创建一个邮件消息
	      Message mailMessage = new MimeMessage(sendMailSession);
	      // 创建邮件发送者地址
	      Address from = new InternetAddress(mailInfo.getFromAddress());
	      // 设置邮件消息的发送者
	      mailMessage.setFrom(from);

	      // 创建邮件的接收者地址，并设置到邮件消息中
	      String[] mailToAddress = mailInfo.getToAddress() ;
	      int len = mailToAddress.length ;
	      Address to[] = new InternetAddress[len] ;
	      for(int i=0;i<len;i++){
	          to[i] = new InternetAddress(mailToAddress[i]) ;
	      }
	      mailMessage.setRecipients(Message.RecipientType.TO,to);
	   // 获取抄送者信息
        String[] ccs = mailInfo.getCcs();
        if (ccs != null){
            // 为每个邮件接收者创建一个地址
            Address[] ccAdresses = new InternetAddress[ccs.length];
            for (int i=0; i<ccs.length; i++){
                ccAdresses[i] = new InternetAddress(ccs[i]);
            }
            // 将抄送者信息设置到邮件信息中，注意类型为Message.RecipientType.CC
            mailMessage.setRecipients(Message.RecipientType.CC, ccAdresses);
        }
	      // 设置邮件消息的主题
	      mailMessage.setSubject(mailInfo.getSubject());
	      // 设置邮件消息发送的时间
	      mailMessage.setSentDate(new Date());
	      // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
	      Multipart mainPart = new MimeMultipart();
	      // 创建一个包含HTML内容的MimeBodyPart
	      BodyPart html = new MimeBodyPart();
	      // 设置HTML内容
	      html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
	      mainPart.addBodyPart(html);
	      //向multipart中添加附件
	      Vector<File> file = mailInfo.getFile() ;
	      //file.add(new File("C:\\Users\\tuyi\\Desktop\\新建文本文档 (2).txt"));
	      file.add(new File("C:\\Users\\tuyi\\Desktop\\新建文本文档1.txt"));
	      String fileName = mailInfo.getFileName() ;
	      Enumeration<File> efile = file.elements() ;
	      while(efile.hasMoreElements()){
	          MimeBodyPart mdpFile = new MimeBodyPart() ;
	          fileName = efile.nextElement().toString() ;
	          FileDataSource fds = new FileDataSource(fileName) ;
	          mdpFile.setDataHandler(new DataHandler(fds)) ;
	          //这个方法可以解决乱码问题
	          String fileName1 = MimeUtility.encodeText(fds.getName()) ;
	          mdpFile.setFileName(fileName1) ;
	          mainPart.addBodyPart(mdpFile) ;
	      }
	      file.removeAllElements() ;
	      // 将MiniMultipart对象设置为邮件内容
	      mailMessage.setContent(mainPart);
	      // 发送邮件
	      Transport transport = sendMailSession.getTransport("smtp");
          transport.connect("smtp.qiye.163.com", Constant.P_EMAIL_NAME, Constant.P_EMAIL_PSWD);
          transport.sendMessage(mailMessage, mailMessage.getAllRecipients());

          transport.close();

	      return true;
	      } catch (Exception ex) {
	          ex.printStackTrace();
	      }
	      return false;
    }

    public static boolean sendMailtoMultiTest(MultiMailSenderInfo mailInfo){
        MyAuthenticator authenticator = null;
        if (mailInfo.isValidate()) {
            authenticator = new MyAuthenticator(mailInfo.getUsername(),
                    mailInfo.getPassword());
        }
        Session sendMailSession = Session.getInstance(mailInfo
                .getProperties(), authenticator);
        String fail_mail = "";
        while (true) {
            try {
                Message mailMessage = new MimeMessage(sendMailSession);
                // 创建邮件发送者地址
                Address from = new InternetAddress(mailInfo.getFromAddress());
                mailMessage.setFrom(from);
                // 创建邮件的接收者地址，并设置到邮件消息中
                String[] mailToAddress = mailInfo.getReceivers() ;
                if (mailToAddress != null) {
                    String mailTOAddressSTR = "";
                    for (String toMailAddress : mailToAddress) {
                        // ccMailAddress.indexOf("@hisuntech.com") > -1 &&
                        if (fail_mail.indexOf(toMailAddress) < 0){
                            mailTOAddressSTR += toMailAddress + ";";
                        }
                    }
                    mailTOAddressSTR = mailTOAddressSTR.substring(0, mailTOAddressSTR.length() - 1);

                    // 为每个邮件接收者创建一个地址
                    Address[] toAddresses = new InternetAddress[mailTOAddressSTR.split(";").length];
                    int i = 0;
                    for (String ccAddStr : mailTOAddressSTR.split(";")) {
                        toAddresses[i] = new InternetAddress(ccAddStr);
                        i++;
                    }
                    mailMessage.setRecipients(Message.RecipientType.TO, toAddresses);
                }
                // 获取抄送者信息
                String[] ccs = mailInfo.getCcs();
                if (ccs != null) {
                    String mailCCAddressSTR = "";
                    for (String ccMailAddress : ccs) {
                        // ccMailAddress.indexOf("@hisuntech.com") > -1 &&
                        if ( fail_mail.indexOf(ccMailAddress) < 0){
                            mailCCAddressSTR += ccMailAddress + ";";
                        }
                    }
                    mailCCAddressSTR = mailCCAddressSTR.substring(0, mailCCAddressSTR.length() - 1);

                    // 为每个邮件接收者创建一个地址
                    Address[] ccAddresses = new InternetAddress[mailCCAddressSTR.split(";").length];
                    int i = 0;
                    for (String ccAddStr : mailCCAddressSTR.split(";")) {
                        ccAddresses[i] = new InternetAddress(ccAddStr);
                        i++;
                    }
                    // 	将抄送者信息设置到邮件信息中，注意类型为Message.RecipientType.CC
                    mailMessage.setRecipients(Message.RecipientType.CC, ccAddresses);
                }

                mailMessage.setSubject(mailInfo.getSubject());
                mailMessage.setSentDate(new Date());
                // 设置邮件内容
                Multipart mainPart = new MimeMultipart();
                BodyPart html = new MimeBodyPart();
                html.setContent(mailInfo.getContent(), "text/html; charset=GBK");
                mainPart.addBodyPart(html);
                //向multipart中添加附件
                Vector<File> files = mailInfo.getFile() ;
                String fileName = mailInfo.getFileName() ;
                Enumeration<File> efile = files.elements() ;
                while(efile.hasMoreElements()){
                    MimeBodyPart mdpFile = new MimeBodyPart() ;
                    fileName = efile.nextElement().toString() ;
                    FileDataSource fds = new FileDataSource(fileName) ;
                    mdpFile.setDataHandler(new DataHandler(fds)) ;
                    //这个方法可以解决乱码问题
                    String fileName1 = MimeUtility.encodeText(fds.getName()) ;
                    mdpFile.setFileName(fileName1) ;
                    mainPart.addBodyPart(mdpFile) ;
                }
                //files.removeAllElements() ;
                // 将MiniMultipart对象设置为邮件内容
                mailMessage.setContent(mainPart);
                // 发送邮件
                Transport transport = sendMailSession.getTransport("smtp");
                transport.connect("smtp.qiye.163.com", Constant.P_EMAIL_NAME, Constant.P_EMAIL_PSWD);
                transport.sendMessage(mailMessage, mailMessage.getAllRecipients());
                transport.close();
                return true;
            } catch (Exception ex) {
                String testStr = ex.getCause().toString();
                String[] testarr = testStr.split(":");
                fail_mail += fail_mail + testarr[testarr.length - 1].trim() + ";";
                logger.error("邮件发送失败:" + testarr[testarr.length - 1].trim());
            }
        }
    }


    public static boolean sendMailtoMultiCC(MultiMailSenderInfo mailInfo){
        MyAuthenticator authenticator = null;
        if (mailInfo.isValidate()) {
            authenticator = new MyAuthenticator(mailInfo.getUsername(),
                    mailInfo.getPassword());
        }
        Session sendMailSession = Session.getInstance(mailInfo
                .getProperties(), authenticator);
        String fail_mail = "";
        while (true) {
            try {
                Message mailMessage = new MimeMessage(sendMailSession);
                // 创建邮件发送者地址
                Address from = new InternetAddress(mailInfo.getFromAddress());
                mailMessage.setFrom(from);
                // 创建邮件的接收者地址，并设置到邮件消息中
                Address to = new InternetAddress(mailInfo.getToAddress());
//                Address to = new InternetAddress("zou_xin@hisuntech.com");
                mailMessage.setRecipient(Message.RecipientType.TO, to);
                // 获取抄送者信息
                String[] ccs = mailInfo.getCcs();
                if (ccs != null) {
                    String mailCCAddressSTR = "";
                    for (String ccMailAddress : ccs) {
                        // ccMailAddress.indexOf("@hisuntech.com") > -1 &&
                        if ( fail_mail.indexOf(ccMailAddress) < 0){
                            mailCCAddressSTR += ccMailAddress + ";";
                        }
                    }
                    mailCCAddressSTR = mailCCAddressSTR.substring(0, mailCCAddressSTR.length() - 1);

                    // 为每个邮件接收者创建一个地址
                    Address[] ccAddresses = new InternetAddress[mailCCAddressSTR.split(";").length];
                    int i = 0;
                    for (String ccAddStr : mailCCAddressSTR.split(";")) {
                        ccAddresses[i] = new InternetAddress(ccAddStr);
                        i++;
                    }
                    // 	将抄送者信息设置到邮件信息中，注意类型为Message.RecipientType.CC
                    mailMessage.setRecipients(Message.RecipientType.CC, ccAddresses);

                }

                mailMessage.setSubject(mailInfo.getSubject());
                mailMessage.setSentDate(new Date());
                // 设置邮件内容
                Multipart mainPart = new MimeMultipart();
                BodyPart html = new MimeBodyPart();
                html.setContent(mailInfo.getContent(), "text/html; charset=GBK");
                mainPart.addBodyPart(html);

                if (mailInfo.getSubject().startsWith("和包错误码在线更新通知")) {
                    MimeBodyPart mdpFile = new MimeBodyPart();
                    FileDataSource fds =null;
                    if(LemonUtils.getEnv().equals(Env.SIT)) {
                       fds = new FileDataSource("/home/devms/alert-redis.sh");
                     }
                    else if(LemonUtils.getEnv().equals(Env.DEV)) {
                        fds = new FileDataSource("/home/devadm/alert-redis.sh");
                    }else {
                        MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                        MsgEnum.ERROR_CUSTOM.setMsgInfo("当前配置环境路径有误，请尽快联系管理员!");
                        BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
                    }
                    mdpFile.setDataHandler(new DataHandler(fds));
                    //这个方法可以解决乱码问题
                    String fileName1 = MimeUtility.encodeText(fds.getName());
                    mdpFile.setFileName(fileName1);
                    mainPart.addBodyPart(mdpFile);
                }

                mailMessage.setContent(mainPart);
                // 发送邮件
                Transport transport = sendMailSession.getTransport("smtp");
                transport.connect("smtp.qiye.163.com", Constant.P_EMAIL_NAME, Constant.P_EMAIL_PSWD);
                transport.sendMessage(mailMessage, mailMessage.getAllRecipients());
                transport.close();
                return true;
            } catch (Exception ex) {
                String testStr = ex.getCause().toString();
                String[] testarr = testStr.split(":");
                fail_mail += fail_mail + testarr[testarr.length - 1].trim() + ";";
                logger.error("邮件发送失败:" + testarr[testarr.length - 1].trim());
            }
        }
    }

    public static void main(String[] args) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String selectTime = DateUtil.date2String(new Date(), "yyyy-MM-dd");
            float betweenDate = 0;
            try {
                Date d1 = sdf.parse(org.apache.commons.lang.StringUtils.substring("2020-09-14 14:19:05", 0, 10));
                //登记工时开始时间
                Date d2 = sdf.parse(StringUtils.substring("2020-09-10 23:10:00", 0, 10));
                betweenDate = (float) (d1.getTime() - d2.getTime()) / (60 * 60 * 24 * 1000);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //周一时创建时间和工作登记开始时间超过四天则不计算
        System.err.println(betweenDate);
            if (betweenDate > 4) {
                System.err.println("超过四天");
            }


    }


}
