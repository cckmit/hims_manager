package com.cmpay.lemon.monitor.service.impl.vpn;

import com.cmpay.lemon.common.exception.BusinessException;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.common.utils.StringUtils;
import com.cmpay.lemon.framework.page.PageInfo;
import com.cmpay.lemon.framework.security.SecurityUtils;
import com.cmpay.lemon.framework.utils.PageUtils;
import com.cmpay.lemon.monitor.bo.*;
import com.cmpay.lemon.monitor.dao.IOperationProductionDao;
import com.cmpay.lemon.monitor.dao.IPreproductionExtDao;
import com.cmpay.lemon.monitor.dao.IVpnInfoDao;
import com.cmpay.lemon.monitor.entity.*;
import com.cmpay.lemon.monitor.entity.sendemail.*;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.SystemUserService;
import com.cmpay.lemon.monitor.service.vpn.VpnInfoService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import org.omg.PortableServer.SERVANT_RETENTION_POLICY_ID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class VpnInfoServiceImpl implements VpnInfoService {
    @Autowired
    private IVpnInfoDao iVpnInfoDao;
    @Autowired
    private IOperationProductionDao operationProductionDao;
    @Autowired
    private SystemUserService userService;
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void add(VpnInfoBO vpnInfoBO) {
        VpnInfoDO vpnInfoDO = new VpnInfoDO();
        BeanConvertUtils.convert(vpnInfoDO, vpnInfoBO);
        vpnInfoDO.setVpnInnerSeq(this.getNextInnerSeq());
        vpnInfoDO.setVpnApplyType("录入待审核");
        MailFlowConditionDO mfva = new MailFlowConditionDO();
        mfva.setEmployeeName(vpnInfoDO.getVpnApplicant());
        //获取VPN申请人的邮箱
        MailFlowDO mfba = operationProductionDao.searchUserEmail(mfva);
        //添加部门经理邮箱地址
        MailFlowConditionDO voDept = new MailFlowConditionDO();
        voDept.setEmployeeName(operationProductionDao.findDeptManager(vpnInfoDO.getVpnDept()).getDeptManagerName());
        MailFlowDO mvoDept = operationProductionDao.searchUserEmail(voDept);
        // 创建邮件信息
        MultiMailSenderInfo mailInfo = new MultiMailSenderInfo();
        mailInfo.setMailServerHost("smtp.qiye.163.com");
        mailInfo.setMailServerPort("25");
        mailInfo.setValidate(true);
        mailInfo.setUsername(Constant.EMAIL_NAME);
        mailInfo.setPassword(Constant.EMAIL_PSWD);
        mailInfo.setFromAddress(Constant.EMAIL_NAME);
        String[] mailToAddress  = mvoDept.getEmployeeEmail().split(";");
        mailInfo.setReceivers(mailToAddress);
        String[] mailToCss = mfba.getEmployeeEmail().split(";");
        mailInfo.setCcs(mailToCss);
        mailInfo.setSubject("【VPN审批申请】");
        StringBuffer sb = new StringBuffer();
        sb.append("<table border='1' style='border-collapse: collapse;background-color: white; white-space: nowrap;'>");
        sb.append("<tr><td colspan='6' style='text-align: center;font-weight: bold;'>VPN申请信息</td></tr>");
        sb.append("<tr><td style='font-weight: bold;'>申请人</td><td>" + vpnInfoDO.getVpnApplicant() + "</td><td style='font-weight: bold;'>申请部门</td><td>" + vpnInfoDO.getVpnDept() + "</td></tr>");
        sb.append("<tr><td style='font-weight: bold;'>手机号</td><td>" + vpnInfoDO.getVpnApplicantTel() + "</td><td style='font-weight: bold;'>当前审批状态</td><td>" + vpnInfoDO.getVpnApplyType() + "</td></tr>");
        sb.append("<tr><td style='font-weight: bold;'>生效开始时间</td><td>" + vpnInfoDO.getVpnStartTime() + "</td><td style='font-weight: bold;'>生效结束时间</td><td>" + vpnInfoDO.getVpnEndTime() + "</td></tr></table>");

        mailInfo.setContent(vpnInfoDO.getVpnApplicant()+" 部门主管好:<br/>由于【" + vpnInfoDO.getVpnReason() + "】故申请开通VPN账号以供使用，请及时审批。<br/>" + sb.toString());
        // 这个类主要来发送邮件
         boolean isSend = MultiMailsender.sendMailtoMultiTest(mailInfo);
        MailFlowDO bnb = new MailFlowDO("VPN部门经理审批申请", "code_review@hisuntech.com", mvoDept.getEmployeeEmail(), mailInfo.getContent());
        operationProductionDao.addMailFlow(bnb);
        if (!(isSend)) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("审批邮件发送失败!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        iVpnInfoDao.insert(vpnInfoDO);
    }

    @Override
    public void update(VpnInfoBO vpnInfoBO) {

    }

    @Override
    public VpnInfoRspBO find(VpnInfoBO vpnInfoBO) {
        PageInfo<VpnInfoBO> pageInfo = getPageInfo(vpnInfoBO);
        List<VpnInfoBO> vpnInfoBOList = BeanConvertUtils.convertList(pageInfo.getList(), VpnInfoBO.class);
        VpnInfoRspBO vpnInfoRspBO = new VpnInfoRspBO();
        vpnInfoRspBO.setVpnInfoBOList(vpnInfoBOList);
        vpnInfoRspBO.setPageInfo(pageInfo);
        return vpnInfoRspBO;
    }

    private PageInfo<VpnInfoBO> getPageInfo(VpnInfoBO vpnInfoBO) {
        VpnInfoDO vpnInfoDO = new VpnInfoDO();
        BeanConvertUtils.convert(vpnInfoDO, vpnInfoBO);
        System.err.println(iVpnInfoDao.find(vpnInfoDO));
        PageInfo<VpnInfoBO> pageInfo = PageUtils.pageQueryWithCount(vpnInfoBO.getPageNum(), vpnInfoBO.getPageSize(),
                () -> BeanConvertUtils.convertList(iVpnInfoDao.find(vpnInfoDO), VpnInfoBO.class));
        return pageInfo;
    }

    public String getNextInnerSeq() {
        // 从最大的值往后排
        VpnInfoBO vpnInfoBO = getMaxInnerSeq();
        if (vpnInfoBO == null) {
            return "XQ00000001";
        } else {
            String maxInnerSeq = vpnInfoBO.getVpnInnerSeq();
            if (StringUtils.isBlank(maxInnerSeq)) {
                return "XQ00000001";
            } else {
                int nextSeq = Integer.parseInt(maxInnerSeq.substring(2)) + 1;
                String innerSeq = StringUtils.leftPad(String.valueOf(nextSeq), 8, "0");
                return "XQ" + innerSeq;
            }
        }
    }
    public VpnInfoBO getMaxInnerSeq() {
        VpnInfoDO vpnInfoDO= null;
        vpnInfoDO= iVpnInfoDao.getMaxInnerSeq();
        if(vpnInfoDO != null){
            return BeanUtils.copyPropertiesReturnDest(new VpnInfoBO(), vpnInfoDO);
        }else {
            return null;
        }

    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void updateAllProduction(HttpServletRequest request, HttpServletResponse response, String taskIdStr){
        //获取登录用户名
        String currentUser = userService.getFullname(SecurityUtils.getLoginName());
        //生成流水记录
        ScheduleDO scheduleBean =new ScheduleDO(currentUser);
        String[] pro_number_list=taskIdStr.split("~");
        if(pro_number_list[0].equals("1")){
            //return ajaxDoneError("请填写进行此操作原因");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("请填写VPN开通情况信息！");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }

        String pro_status_after = "";
        String pro_status_before = "";
        // 部门主管审批
        if(pro_number_list[0].equals("bmzg")){
            if(pro_number_list.length==1){
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择VPN申请进行操作");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            pro_status_before = "录入待审核";
            pro_status_after = "部门主管审核通过";
            scheduleBean.setOperationReason("部门主管审核通过");
        }
        //总经理审批
        else if(pro_number_list[0].equals("zjl")){
            if(pro_number_list.length==1){
                //return ajaxDoneError("请选择投产进行操作!");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择VPN申请进行操作");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            pro_status_before = "部门主管审核通过";
            pro_status_after = "总经理审核通过";
            scheduleBean.setOperationReason("总经理审核通过");
        }
        //配置完成通过
        else if(pro_number_list[0].equals("pzwc")){
            if ((pro_number_list.length == 1) || (pro_number_list.length == 2)) {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择VPN申请进行操作");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            pro_status_before = "总经理审核通过";
            pro_status_after = "配置完成";
            scheduleBean.setOperationReason("VPN配置完成");
        }
        //打回
        else if (pro_number_list[0].equals("dh")) {
            pro_status_before = "录入待审核";
            pro_status_after = "审核不通过";
            if ((pro_number_list.length == 1) || (pro_number_list.length == 2)) {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择VPN申请进行操作");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            scheduleBean.setOperationReason("VPN审核不通过");
        }
        scheduleBean.setPreOperation(pro_status_before);
        scheduleBean.setAfterOperation(pro_status_after);
        scheduleBean.setOperationType(pro_status_after);

        for (int i = 2; i < pro_number_list.length; ++i) {
            String status = iVpnInfoDao.get(pro_number_list[i]).getVpnApplyType();
            System.err.println(pro_status_before);
            System.err.println(pro_status_after);
            System.err.println(status);
            if(!(pro_status_before.equals(status) || (pro_status_after.equals("部门主管审核通过") && (status.equals("录入待审核")) ) || (pro_status_after.equals("总经理审核通过") && status.equals("部门主管审核通过"))|| (pro_status_after.equals("配置完成") && status.equals("总经理审核通过")) ||(pro_status_after.equals("审核不通过") && (status.equals("录入待审核")|| status.equals("部门主管审核通过"))))){
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择符合当前操作类型的正确审批状态!");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
        }
        boolean isSend = true;
        for (int j = 2; j < pro_number_list.length; ++j) {
            String status = iVpnInfoDao.get(pro_number_list[j]).getVpnApplyType();
            SimpleDateFormat sdfmonth = new SimpleDateFormat("yyyy-MM");
            scheduleBean.setProNumber(pro_number_list[j]);
            scheduleBean.setPreOperation(status);
            VpnInfoDO  vpnInfoDO = iVpnInfoDao.get(pro_number_list[j]);

            MailFlowConditionDO mfva = new MailFlowConditionDO();
            mfva.setEmployeeName(iVpnInfoDao.get(pro_number_list[j]).getVpnApplicant());
            //获取VPN申请人的邮箱
            MailFlowDO mfba = operationProductionDao.searchUserEmail(mfva);

            //添加部门经理邮箱地址
            MailFlowConditionDO voDept = new MailFlowConditionDO();
            voDept.setEmployeeName(operationProductionDao.findDeptManager(iVpnInfoDao.get(pro_number_list[j]).getVpnDept()).getDeptManagerName());
            MailFlowDO mvoDept = operationProductionDao.searchUserEmail(voDept);
            //总经理邮箱
            MailGroupDO mp=operationProductionDao.findMailGroupBeanDetail("7");
            //安全中心
            MailGroupDO mp2=operationProductionDao.findMailGroupBeanDetail("8");
            // 创建邮件信息
            MultiMailSenderInfo mailInfo = new MultiMailSenderInfo();
            mailInfo.setMailServerHost("smtp.qiye.163.com");
            mailInfo.setMailServerPort("25");
            mailInfo.setValidate(true);
            mailInfo.setUsername(Constant.EMAIL_NAME);
            mailInfo.setPassword(Constant.EMAIL_PSWD);
            mailInfo.setFromAddress(Constant.EMAIL_NAME);

            if(pro_status_after.equals("部门主管审核通过")){
                StringBuffer sb = new StringBuffer();
                sb.append("<table border='1' style='border-collapse: collapse;background-color: white; white-space: nowrap;'>");
                sb.append("<tr><td colspan='6' style='text-align: center;font-weight: bold;'>VPN申请信息</td></tr>");
                sb.append("<tr><td style='font-weight: bold;'>申请人</td><td>" + vpnInfoDO.getVpnApplicant() + "</td><td style='font-weight: bold;'>申请部门</td><td>" + vpnInfoDO.getVpnDept() + "</td></tr>");
                sb.append("<tr><td style='font-weight: bold;'>手机号</td><td>" + vpnInfoDO.getVpnApplicantTel() + "</td><td style='font-weight: bold;'>当前审批状态</td><td>" + pro_status_after + "</td></tr>");
                sb.append("<tr><td style='font-weight: bold;'>生效开始时间</td><td>" + vpnInfoDO.getVpnStartTime() + "</td><td style='font-weight: bold;'>生效结束时间</td><td>" + vpnInfoDO.getVpnEndTime() + "</td></tr></table>");

                String[] mailToAddress  = mp.getMailUser().split(";");
                mailInfo.setReceivers(mailToAddress);
                System.err.println("收件人"+mailToAddress.toString());
                String[] mailToCss = (mfba.getEmployeeEmail()+";"+mvoDept.getEmployeeEmail()).split(";");
                System.err.println("抄送人"+mailToCss.toString());
                mailInfo.setCcs(mailToCss);
                mailInfo.setSubject("【VPN审批申请】");
                mailInfo.setContent("武姐、铧哥好:<br/>由于【" + vpnInfoDO.getVpnReason() + "】故申请开通VPN账号以供使用。"+currentUser+"已审批同意，请及时审批。<br/>" + sb.toString());
                // 这个类主要来发送邮件
                isSend = MultiMailsender.sendMailtoMultiTest(mailInfo);
                MailFlowDO bnb = new MailFlowDO("VPN总经理审批申请", "code_review@hisuntech.com", mp.getMailUser(), mailInfo.getContent());
                operationProductionDao.addMailFlow(bnb);
            }
            if(pro_status_after.equals("总经理审核通过")){
                StringBuffer sb = new StringBuffer();
                sb.append("<table border='1' style='border-collapse: collapse;background-color: white; white-space: nowrap;'>");
                sb.append("<tr><td colspan='6' style='text-align: center;font-weight: bold;'>VPN申请信息</td></tr>");
                sb.append("<tr><td style='font-weight: bold;'>申请人</td><td>" + vpnInfoDO.getVpnApplicant() + "</td><td style='font-weight: bold;'>申请部门</td><td>" + vpnInfoDO.getVpnDept() + "</td></tr>");
                sb.append("<tr><td style='font-weight: bold;'>手机号</td><td>" + vpnInfoDO.getVpnApplicantTel() + "</td><td style='font-weight: bold;'>当前审批状态</td><td>" + pro_status_after + "</td></tr>");
                sb.append("<tr><td style='font-weight: bold;'>申请账号</td><td>" + vpnInfoDO.getVpnAccount() + "</td><td style='font-weight: bold;'>账号密码</td><td>" + vpnInfoDO.getVpnPassword() + "</td></tr>");
                sb.append("<tr><td style='font-weight: bold;'>生效开始时间</td><td>" + vpnInfoDO.getVpnStartTime() + "</td><td style='font-weight: bold;'>生效结束时间</td><td>" + vpnInfoDO.getVpnEndTime() + "</td></tr></table>");

                // 安全中心人员
                String[] mailToAddress  = mp2.getMailUser().split(";");
                mailInfo.setReceivers(mailToAddress);
                System.err.println("收件人"+mailToAddress.toString());
                String[] mailToCss = (mfba.getEmployeeEmail()+";"+mvoDept.getEmployeeEmail()+";"+mp2.getMailUser()).split(";");
                System.err.println("抄送人"+mailToCss.toString());
                mailInfo.setCcs(mailToCss);
                mailInfo.setSubject("【VPN账号开通申请】");
                mailInfo.setContent("你好:<br/>由于【" + vpnInfoDO.getVpnReason() + "】，故申请开通奥美的VPN账号以供使用。领导已审批同意，望及时开通账号,并在研发管理系统中完成流程。<br/>" + sb.toString());

                // 这个类主要来发送邮件
                isSend = MultiMailsender.sendMailtoMultiTest(mailInfo);
                MailFlowDO bnb = new MailFlowDO("VPN账号开通申请", "code_review@hisuntech.com", mp2.getMailUser(), mailInfo.getContent());
                operationProductionDao.addMailFlow(bnb);
            }
            if(pro_status_after.equals("配置完成")){
                StringBuffer sb = new StringBuffer();
                sb.append("<table border='1' style='border-collapse: collapse;background-color: white; white-space: nowrap;'>");
                sb.append("<tr><td colspan='6' style='text-align: center;font-weight: bold;'>VPN账号信息</td></tr>");
                sb.append("<tr><td style='font-weight: bold;'>账号名</td><td>" + vpnInfoDO.getVpnAccount() + "</td><td style='font-weight: bold;'>账号密码</td><td>" + vpnInfoDO.getVpnPassword() + "</td></tr>");
                sb.append("<tr><td style='font-weight: bold;'>生效开始时间</td><td>" + vpnInfoDO.getVpnStartTime() + "</td><td style='font-weight: bold;'>生效结束时间</td><td>" + vpnInfoDO.getVpnEndTime() + "</td></tr></table>");

                String[] mailToAddress  = mp.getMailUser().split(";");
                mailInfo.setReceivers(mailToAddress);
                System.err.println("收件人"+mailToAddress.toString());
                String[] mailToCss = (mfba.getEmployeeEmail()+";"+mvoDept.getEmployeeEmail()).split(";");
                System.err.println("抄送人"+mailToCss.toString());
                mailInfo.setCcs(mailToCss);
                mailInfo.setSubject("【VPN账号开通通知】");
                mailInfo.setContent("你好:<br/>VPN账号已经配置开通具体信息如下：【" + pro_number_list[1] + "】<br/>" + sb.toString());
                // 这个类主要来发送邮件
                isSend = MultiMailsender.sendMailtoMultiTest(mailInfo);
                MailFlowDO bnb = new MailFlowDO("VPN总经理审批申请", "code_review@hisuntech.com", mp.getMailUser(), mailInfo.getContent());
                operationProductionDao.addMailFlow(bnb);
            }
            if(pro_status_after.equals("审核不通过")){
                String[] mailToAddress  = mfba.getEmployeeEmail().split(";");
                mailInfo.setReceivers(mailToAddress);
                mailInfo.setSubject("【VPN审批不通过通知】");
                mailInfo.setContent("你好:<br/>由于【" + pro_number_list[1] + "】，你申请的VPN，审批未通过！");
                // 这个类主要来发送邮件
                isSend = MultiMailsender.sendMailtoMultiTest(mailInfo);
                MailFlowDO bnb = new MailFlowDO("VPN总经理审批申请", "code_review@hisuntech.com", mp.getMailUser(), mailInfo.getContent());
                operationProductionDao.addMailFlow(bnb);
            }
            vpnInfoDO.setVpnApplyType(pro_status_after);
            System.err.println(vpnInfoDO);
            iVpnInfoDao.update(vpnInfoDO);

            if (!(isSend)) {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("审批邮件发送失败!");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }

        }




    }
}
