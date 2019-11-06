package com.cmpay.lemon.monitor.service.impl.production;

import com.cmpay.lemon.common.exception.BusinessException;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.framework.security.SecurityUtils;
import com.cmpay.lemon.monitor.bo.OperationApplicationBO;
import com.cmpay.lemon.monitor.dao.IOperationApplicationDao;
import com.cmpay.lemon.monitor.entity.Constant;
import com.cmpay.lemon.monitor.entity.OperationApplicationDO;
import com.cmpay.lemon.monitor.entity.ScheduleDO;
import com.cmpay.lemon.monitor.entity.sendemail.*;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.production.OperationApplicationService;
import com.cmpay.lemon.monitor.service.production.OperationProductionService;
import com.cmpay.lemon.monitor.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OperationApplicationServiceImpl  implements OperationApplicationService {
    @Autowired
    OperationProductionService operationProductionService;
    @Autowired
    IOperationApplicationDao operationApplicationDao;

    public final static String RELATIVE_PATH = "upload/sysopr/";

    @Override
    public void systemOperationEntry(List<MultipartFile> files, OperationApplicationBO bean, HttpServletRequest request) {
        System.err.println(bean.toString());

        bean.setProposeDate(new Date());
        bean.setOperNumber("SYS-OPR-" + DateUtil.date2String(new Date(), "yyyyMMddhhmmss"));
        bean.setOperStatus("提出");
        //处理多文件上传
        String filePath = null;
        File fl = null;
        String[] attachFileNames = null;
        // 判断文件是否为空
        if (files != null && !files.equals("")) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    try {
                        //归类文件，创建编号文件夹
                        System.err.println(request.getSession().getServletContext().getRealPath("/") +
                                RELATIVE_PATH + bean.getOperNumber());
                     /*   File fileNumber = new File(request.getSession().getServletContext().getRealPath("/") +
                                RELATIVE_PATH + bean.getOperNumber());*/
                        File fileNumber = new File("D:\\home\\devadm\\temp");
                        fileNumber.mkdir();
                        // 文件保存路径
                        filePath = fileNumber.getPath() + "/" + file.getOriginalFilename();
                        // 转存文件
                        fl = new File(filePath);
                        attachFileNames = fl.getAbsolutePath().split(";");
                        for (int i = 0; i < attachFileNames.length; i++) {
                            if ("\\".equals(File.separator)) attachFileNames[i].replace("/", "\\");
                            else if ("/".equals(File.separator)) attachFileNames[i].replace("\\", "/");
                        }
                        file.transferTo(fl);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        //发邮件通知
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
        //添加申请人邮箱地址
        SendEmailConfig config = new SendEmailConfig();
        MailFlowConditionDO vo = new MailFlowConditionDO();
        vo.setEmployeeName(bean.getOperApplicant());
        MailFlowDO mflow = operationProductionService.searchUserEmail(vo);
        System.err.println( mflow.getEmployeeEmail());
        String[] mailToAddressDemo = null;
        System.err.println(bean.getMailRecipient());
        if (bean.getMailRecipient() != null && !bean.getMailRecipient().equals("")) {
            mailToAddressDemo = (bean.getMailRecipient() + ";" + config.getExcuteMailTo() + ";" + mflow.getEmployeeEmail()).split(";");
        } else {
            mailToAddressDemo = (config.getExcuteMailTo() + ";" + mflow.getEmployeeEmail()).split(";");
        }
        //收件人去重复
        List<String> result = new ArrayList<String>();
        boolean flag;
        for (int i = 0; i < mailToAddressDemo.length; i++) {
            flag = false;
            for (int j = 0; j < result.size(); j++) {
                if (mailToAddressDemo[i].equals(result.get(j))) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                result.add(mailToAddressDemo[i]);
            }
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.size(); i++) {
            if (i == result.size() - 1) {
                sb.append(result.get(i));
            } else {
                sb.append(result.get(i) + ";");
            }

        }

        String mailSum = sb.toString().replaceAll(";;", ";");
        bean.setMailRecipient(mailSum);
        //添加部门经理邮箱地址
        MailFlowConditionDO voManager = new MailFlowConditionDO();
        System.err.println(bean.getApplicationSector());
        voManager.setEmployeeName(operationProductionService.findDeptManager(bean.getApplicationSector()).getDeptManagerName());
        MailFlowDO mManager = operationProductionService.searchUserEmail(voManager);
        if (bean.getSysOperType().equals("数据变更")) {
            String[] mail_copy_person = null;
            if (bean.getMailCopyPerson() != null && !bean.getMailCopyPerson().equals("")) {
                mail_copy_person = (bean.getMailCopyPerson() + ";" + config.getExcuteMailCopy() + ";" + mManager.getEmployeeEmail()).split(";");
            } else {
                mail_copy_person = (config.getExcuteMailCopy() + ";" + mManager.getEmployeeEmail()).split(";");
            }

            //抄送人去重复
            List<String> resultCopy = new ArrayList<String>();
            boolean flagCopy;
            for (int i = 0; i < mail_copy_person.length; i++) {
                flagCopy = false;
                for (int j = 0; j < resultCopy.size(); j++) {
                    if (mail_copy_person[i].equals(resultCopy.get(j))) {
                        flagCopy = true;
                        break;
                    }
                }
                if (!flagCopy) {
                    resultCopy.add(mail_copy_person[i]);
                }
            }
            String[] mailCopyPerson = (String[]) resultCopy.toArray(new String[resultCopy.size()]);
            //保存邮件抄送人
            StringBuffer sbCopyMail = new StringBuffer();
            for (int i = 0; i < mailCopyPerson.length; i++) {
                if (i == mailCopyPerson.length - 1) {
                    sbCopyMail.append(mailCopyPerson[i]);
                } else {
                    sbCopyMail.append(mailCopyPerson[i] + ";");
                }
            }
            String mailCopySum = sbCopyMail.toString().replace(";;", ";");
            bean.setMailCopyPerson(mailCopySum.toString());

            if (bean.getMailRecipient() == null || bean.getMailRecipient().equals("")) {
                MsgEnum.ERROR_IMPORT.setMsgInfo(" 输入字段不能为空");
                BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
            }

        } else {
            bean.setMailCopyPerson(mManager.getEmployeeEmail());
        }

        //后台判断数据
        if (bean.getOperNumber() == null || bean.getOperNumber().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 输入字段不能为空");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getOperRequestContent() == null || bean.getOperRequestContent().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 输入字段不能为空");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getProposeDate() == null || bean.getProposeDate().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 输入字段不能为空");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getIsRefSql() == null || bean.getIsRefSql().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 输入字段不能为空");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getSysOperType() == null || bean.getSysOperType().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 输入字段不能为空");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getOperType() == null || bean.getOperType().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 输入字段不能为空");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getApplicationSector() == null || bean.getApplicationSector().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 输入字段不能为空");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getApplicantTel() == null || bean.getApplicantTel().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 输入字段不能为空");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getOperApplicant() == null || bean.getOperApplicant().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 输入字段不能为空");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getOperType() == null || bean.getOperType().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 输入字段不能为空");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }

        if (bean.getIdentifier() == null || bean.getIdentifier().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 输入字段不能为空");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getIdentifierTel() == null || bean.getIdentifierTel().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 输入字段不能为空");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getValidationType() == null || bean.getValidationType().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 输入字段不能为空");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getValidationInstruction() == null || bean.getValidationInstruction().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 输入字段不能为空");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getDevelopmentLeader() == null || bean.getDevelopmentLeader().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 输入字段不能为空");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getOperApplicationReason() == null || bean.getOperApplicationReason().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 输入字段不能为空");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getAnalysis() == null || bean.getAnalysis().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 输入字段不能为空");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getCompletionUpdate() == null || bean.getCompletionUpdate().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 输入字段不能为空");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getRemark() == null || bean.getRemark().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 输入字段不能为空");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }

        this.addOperationalApplication(bean);
        ScheduleDO scheduleBean = new ScheduleDO(bean.getOperNumber(), SecurityUtils.getLoginName(), "录入", "", "提出", "");
        operationProductionService.addScheduleBean(scheduleBean);

        /**
         * 发送给申请人部门经理,通知及时审批
         */
        //记录邮箱信息
        MailFlowBean bnb = new MailFlowBean("【" + bean.getSysOperType() + "审批】-" + bean.getOperRequestContent() + "-" + bean.getOperApplicant(), Constant.P_EMAIL_NAME, mManager.getEmployeeEmail() + ";" + mflow.getEmployeeEmail(), "");
     /*   MailSenderInfo mailInfo = new MailSenderInfo();
        // 设置邮件服务器类型
        mailInfo.setMailServerHost("smtp.qiye.163.com");
        //设置端口号
        mailInfo.setMailServerPort("25");
        //设置是否验证
        mailInfo.setValidate(true);
        //设置用户名、密码、发送人地址
        mailInfo.setUserName(Constant.P_EMAIL_NAME);
        mailInfo.setPassword(Constant.P_EMAIL_PSWD);// 您的邮箱密码
        mailInfo.setFromAddress(Constant.P_EMAIL_NAME);*/
        mailInfo.setToAddress(mManager.getEmployeeEmail().split(";"));
        mailInfo.setCcs(mflow.getEmployeeEmail().split(";"));
        mailInfo.setSubject("【" + bean.getSysOperType() + "审批】-" + bean.getOperRequestContent() + "-" + bean.getOperApplicant());
        mailInfo.setContent("你好！<br/>&nbsp;&nbsp;你的部门新增了【" + bean.getSysOperType() + "-" + bean.getOperRequestContent() + "】一类系统操作申请，烦请及时去线上审批，谢谢！<br/>");
        // 这个类主要来发送邮件
        SimpleMailSender sms = new SimpleMailSender();
        boolean isSend = sms.sendHtmlMail(mailInfo);// 发送html格式
        operationProductionService.addMailFlow(bnb);
        if (!isSend) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 邮件发送失败");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }

    }

    @Override
    public void addOperationalApplication(OperationApplicationBO bean) {
        if (!bean.getSysOperType().equals("数据变更")){
            bean.setIsBackWay("");
        }
        OperationApplicationDO operationApplicationDO = BeanUtils.copyPropertiesReturnDest(new OperationApplicationDO(), bean);
        operationApplicationDao.insertOperationalApplication(operationApplicationDO);
    }

}
