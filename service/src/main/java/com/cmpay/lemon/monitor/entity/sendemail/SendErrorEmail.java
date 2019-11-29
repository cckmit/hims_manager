package com.cmpay.lemon.monitor.entity.sendemail;

import com.cmpay.lemon.monitor.utils.DateUtil;

import java.util.Date;

public class SendErrorEmail {
	public static void sendEmail(String subject,String fromusername,String frompassword,String toUserName,String contentStr) {
      	 MailInfo mailInfo = new MailInfo();
           mailInfo.setMailServerHost("smtp.qiye.163.com");  
           mailInfo.setMailServerPort("25");  
           mailInfo.setValidate(true);  
           mailInfo.setUsername(fromusername);  
           mailInfo.setPassword(frompassword);// 您的邮箱密码  
           mailInfo.setFromAddress(fromusername);  
           mailInfo.setToAddress(toUserName);  
           mailInfo.setSubject(subject);  
           String dateStr= DateUtil.date2String(new Date(),DateUtil.PATTERN_STANDARD);
           StringBuffer demo = new StringBuffer();  
           demo.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">")  
           .append("<html>")  
           .append("<head>")  
           .append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">")  
           .append("<title>") 
           .append(subject) 
           .append("</title>") 
           .append("<style type=\"text/css\">")  
           .append(".test{font-family:\"Microsoft Yahei\";font-size: 16px;color: black;}")  
           .append(".test1{font-family:\"Microsoft Yahei\";font-size: 14px;color: black;}")  
           .append("</style>")  
           .append("</head>")  
           .append("<body>")  
           .append("<span class=\"test\">您好！<br/>&nbsp;&nbsp;&nbsp;&nbsp;")
           .append("</span>")
           .append("<span class=\"test1\">")
           .append(contentStr)
           .append("</span>")
           .append("</span>")
           .append("<span class=\"test1\">")
           .append("<br/>邮件发送时间："+dateStr)
           .append("</span>")
           .append("</body>")  
           .append("</html>");  
           mailInfo.setContent(demo.toString());
           SimpleMail.sendHtmlMail(mailInfo);// 发送html格式
		}

}
