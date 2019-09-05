package com.cmpay.lemon.monitor.entity.sendemail;

import com.cmpay.lemon.monitor.entity.Constant;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

public class SimpleMailSender {
    private static Logger logger = Logger.getLogger(SimpleMailSender.class);


    private static final String MAIL_HOST = "smtp.qiye.163.com";

    private static final String USER = "code_review@hisuntech.com";

    private static final String PASSWORD = "hisun@248!@#";

    private static final int MAX_SEND_FAIL_NUMBER = 5;

    /**
     * 去除失败邮箱
     * @param mails
     * @param failMails
     * @return
     * @throws Exception
     */
    private Address[] excludeFailMails(String[] mails, String failMails) throws Exception {
        Address[] tmpAddress = new InternetAddress[mails.length];
        int mailNum = 0;
        for (String mail : mails) {
            if (failMails.indexOf(mail) < 0) {
                tmpAddress[mailNum] = new InternetAddress(mail);
                mailNum++;
            }
        }
        Address[] mailsAddress = new InternetAddress[mailNum];
        logger.info("excludeFailMails info mails:{}" + mails.toString());
        logger.info("excludeFailMails info failMails:{}" + failMails);
        System.arraycopy(tmpAddress, 0, mailsAddress, 0, mailNum);
        return mailsAddress;
    }

    /**
     * 设置邮箱内容及其文件
     *
     * @param message
     * @param mailInfo
     * @return
     * @throws Exception
     */
    private MimeMessage setMailContent(MimeMessage message, MailSenderInfo mailInfo) throws Exception {

        Multipart mainPart = new MimeMultipart();

        // 设置邮箱内容
        BodyPart html = new MimeBodyPart();
        html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
        mainPart.addBodyPart(html);

        // 设置邮箱文件
        Vector<File> vectorFiles = mailInfo.getFile();
        if (vectorFiles != null && vectorFiles.size() > 0) {
            Enumeration efile = vectorFiles.elements();
            while (efile.hasMoreElements()) {
                String fileName = ((File) efile.nextElement()).toString();
                FileDataSource fds = new FileDataSource(fileName);
                MimeBodyPart mdpFile = new MimeBodyPart();
                mdpFile.setDataHandler(new DataHandler(fds));
                mdpFile.setFileName(MimeUtility.encodeText(fds.getName()));
                mainPart.addBodyPart(mdpFile);
            }
            vectorFiles.removeAllElements();
        }
        message.setContent(mainPart);
        return message;
    }

    /**
     * 以HTML格式发送邮件
     *
     * @param mailInfo 待发送的邮件信息,存储发件人基本信息(Ps:账号、密码)
     */
    public boolean sendHtmlMail(MailSenderInfo mailInfo) {
        Gson gson = new Gson();
        // 记录失败原因
        String failMails = "";
        // 用来记录错误次数
        int failNumber = 0;

        // 是否需要授权认证
        MyAuthenticator authenticator = null;
        if (mailInfo.isValidate()) {
            authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
        }
        // 获取邮箱配置文件
        Properties pro = mailInfo.getProperties();
        Session sendMailSession = Session.getInstance(pro, authenticator);
        MimeMessage mailMessage = new MimeMessage(sendMailSession);

        while (true) {
            try {
                // 设置邮箱地址
                Address from = new InternetAddress(mailInfo.getFromAddress());
                mailMessage.setFrom(from);
                // 设置接收者邮箱
                Address[] toMails = excludeFailMails( mailInfo.getToAddress(), failMails);
                mailMessage.setRecipients(Message.RecipientType.TO, toMails);
                // 设置抄送者邮箱
                if (mailInfo.getCcs() != null && mailInfo.getCcs().length > 0) {
                    Address[] ccMails = excludeFailMails(mailInfo.getCcs(), failMails);
                    mailMessage.setRecipients(Message.RecipientType.CC, ccMails);
                }
                // 设置邮箱主题
                mailMessage.setSubject(mailInfo.getSubject());
                mailMessage.setSentDate(new Date());
                // 设置邮箱内容及其文件
                setMailContent(mailMessage, mailInfo);

                // 发送邮件
                Transport transport = sendMailSession.getTransport("smtp");
                transport.connect(MAIL_HOST, USER, PASSWORD);
                transport.sendMessage(mailMessage, mailMessage.getAllRecipients());
                transport.close();
                return true;
            } catch (Exception e) {
                //System.out.println("邮件发送失败错误原因："+e.toString());
                String errorMessage = e.getCause().toString();
                String[] errors = errorMessage.split(":");
                logger.error("邮件发送失败:" + errors.toString());
                String errorInfo = errors[errors.length - 1].trim();
                // 判断错误原因是否为邮箱
                if (errorInfo.indexOf("@") > -1) {
                    if (failMails.indexOf(errorInfo) < 0) {
                        failMails = failMails + errorInfo +";";
                    }else{
                        failNumber++;
                    }
                } else {
                    failNumber++;
                    logger.error("邮件发送失败 系统报错{}:" + errorInfo);
                }
                // 当其错误次数达到上限（系统报错、相同邮件报错）
                if (failNumber >= MAX_SEND_FAIL_NUMBER){
                    logger.error("邮件发送失败超过最大次数");
                    return false;
                }
            }
        }
    }


    public static void test() {
        System.out.println("cdsdX");
        String touser = "zou_xin@hisuntech.com";
        //记录邮箱信息
        MailFlowDO bnb = new MailFlowDO("【审核】", Constant.P_EMAIL_NAME, "liu_chang@hisuntech.com", "");
        MailSenderInfo mailInfo = new MailSenderInfo();
        // 设置邮件服务器类型
        mailInfo.setMailServerHost("smtp.qiye.163.com");
        //设置端口号
        mailInfo.setMailServerPort("25");
        //设置是否验证
        mailInfo.setValidate(true);
        //设置用户名、密码、发送人地址
        mailInfo.setUserName(Constant.P_EMAIL_NAME);
        mailInfo.setPassword(Constant.P_EMAIL_PSWD);// 您的邮箱密码
        mailInfo.setFromAddress(Constant.P_EMAIL_NAME);

        String[] mailToAddress = new String[]{touser,"liu_@hisunte.com","7417@qq.com"};
        mailInfo.setToAddress(mailToAddress);
        mailInfo.setCcs(mailToAddress);


        StringBuffer sb = new StringBuffer();
        mailInfo.setSubject("测试邮件");
        sb.append("<table border='1' style='border-collapse: collapse;background-color: white; white-space: nowrap;'>");

        mailInfo.setContent("各位领导好！<br/>&nbsp;&nbsp;以下，烦请审批，谢谢！<br/>" + sb.toString());
        // 这个类主要来发送邮件
        SimpleMailSender sms = new SimpleMailSender();
        boolean isSend = sms.sendHtmlMail(mailInfo);// 发送html格式
        if (!isSend) {
            System.out.println("审批邮件发送失败！");
        } else {
            System.out.println("审批邮件发送成功！");
        }
    }

}
