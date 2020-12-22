package com.cmpay.lemon.monitor.service.impl.preproduction;

import com.cmpay.lemon.common.Env;
import com.cmpay.lemon.common.exception.BusinessException;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.common.utils.JudgeUtils;
import com.cmpay.lemon.common.utils.StringUtils;
import com.cmpay.lemon.framework.autoconfigure.schedule.ScheduleAutoConfiguration;
import com.cmpay.lemon.framework.page.PageInfo;
import com.cmpay.lemon.framework.security.SecurityUtils;
import com.cmpay.lemon.framework.utils.LemonUtils;
import com.cmpay.lemon.framework.utils.PageUtils;
import com.cmpay.lemon.monitor.bo.*;
import com.cmpay.lemon.monitor.dao.*;
import com.cmpay.lemon.monitor.entity.*;
import com.cmpay.lemon.monitor.entity.sendemail.*;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.SystemUserService;
import com.cmpay.lemon.monitor.service.automaticCommissioningInterface.AutomaticCommissioningInterfaceService;
import com.cmpay.lemon.monitor.service.demand.ReqPlanService;
import com.cmpay.lemon.monitor.service.demand.ReqTaskService;
import com.cmpay.lemon.monitor.service.preproduction.PreProductionService;
import com.cmpay.lemon.monitor.service.productTime.ProductTimeService;
import com.cmpay.lemon.monitor.service.sendmail.SendMailService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import com.cmpay.lemon.monitor.utils.FtpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

/**
 * 预投产管理：查询及状态变更
 */
@Service
public class PreProductionServiceImpl implements PreProductionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PreProductionServiceImpl.class);
    //超级管理员
    private static final Long SUPERADMINISTRATOR =(long)10506;
    //团队主管
    private static final Long SUPERADMINISTRATOR1 =(long)5004;
    //运维部署组
    private static final Long SUPERADMINISTRATOR2 =(long)5005;
    @Autowired
    private IPreproductionExtDao iPreproductionExtDao;
    @Autowired
    private IOperationApplicationDao operationApplicationDao;

    @Autowired
    private AutomaticCommissioningInterfaceService automaticCommissioningInterfaceService;
    @Autowired
    private ReqPlanService reqPlanService;
    @Autowired
    private ProductTimeService productTimeService;
    @Autowired
    private ReqTaskService reqTaskService;
    @Autowired
    IPermiUserDao permiUserDao;
    @Autowired
    ITPermiDeptDao permiDeptDao;
    @Autowired
    IDemandExtDao demandDao;
    @Autowired
    private IUserRoleExtDao userRoleExtDao;
    @Autowired
    SystemUserService userService;
    @Autowired
    private IOperationProductionDao operationProductionDao;
    @Autowired
    private IAutomatedProductionRegistrationDao automatedProductionRegistrationDao;
    @Autowired
    private IPlanDao planDao;
    @Autowired
    private SendMailService sendMailService;

    @Override
    public PreproductionRspBO find(PreproductionBO productionBO) {
        PageInfo<PreproductionBO> pageInfo = getPageInfo(productionBO);
        List<PreproductionBO> productionBOList = BeanConvertUtils.convertList(pageInfo.getList(), PreproductionBO.class);
        PreproductionRspBO productionRspBO = new PreproductionRspBO();
        productionRspBO.setPreproductionBOList(productionBOList);
        productionRspBO.setPageInfo(pageInfo);
        return productionRspBO;
    }

    private PageInfo<PreproductionBO> getPageInfo(PreproductionBO productionBO) {
        PreproductionDO preproductionDO = new PreproductionDO();
        BeanConvertUtils.convert(preproductionDO, productionBO);
        PageInfo<PreproductionBO> pageInfo = PageUtils.pageQueryWithCount(productionBO.getPageNum(), productionBO.getPageSize(),
                () -> BeanConvertUtils.convertList(iPreproductionExtDao.findList(preproductionDO), PreproductionBO.class));
        return pageInfo;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void update(PreproductionBO productionBO){
        PreproductionDO preproductionDO = new PreproductionDO();
        BeanConvertUtils.convert(preproductionDO, productionBO);
        iPreproductionExtDao.update(preproductionDO);
    }

    /**
     * 预投产录入
     * @param productionBO
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void add(PreproductionBO productionBO){
        PreproductionDO preproductionDO = new PreproductionDO();
        productionBO.setPreStatus("预投产提出");
        //预投产验证结果
        productionBO.setProAdvanceResult("未验证");
        //预投产部署结果
        productionBO.setProductionDeploymentResult("未部署");
        // 如果 是否需要dba操作为"是"，则dba操作完成为：否，否则不赋值
        if(productionBO.getIsDbaOperation().equals("是")){
            productionBO.setIsDbaOperationComplete("否");
        }else{
            productionBO.setIsDbaOperationComplete("");
        }
        BeanConvertUtils.convert(preproductionDO, productionBO);
        System.err.println(preproductionDO);
        //获取登录用户名
        String currentUser = userService.getFullname(SecurityUtils.getLoginName());
        // 将其原先的lis循环查找相同pro_number编号的投产信息 更新为查找一条记录是否存在
        PreproductionBO preproductionBean = this.searchProdutionDetail(preproductionDO.getPreNumber());
        if (JudgeUtils.isNotNull(preproductionBean)) {
            iPreproductionExtDao.updateAgain(preproductionDO);
            //生成流水记录
            ScheduleDO scheduleBean = new ScheduleDO(preproductionDO.getPreNumber(), userService.getFullname(SecurityUtils.getLoginName()), "重新录入", preproductionDO.getPreStatus(), preproductionDO.getPreStatus(), "预投产重新录入");
            operationProductionDao.insertSchedule(scheduleBean);

        }else{
            ScheduleDO sBean=new ScheduleDO();
            sBean.setPreOperation(preproductionDO.getPreStatus());
            ScheduleDO schedule=new ScheduleDO(preproductionDO.getPreNumber(), currentUser, "预投产录入", sBean.getPreOperation(), sBean.getPreOperation(), "预投产录入");
            operationProductionDao.insertSchedule(schedule);

            iPreproductionExtDao.insert(preproductionDO);
        }
        // 发送邮件通知
        // 创建邮件信息
        MultiMailSenderInfo mailInfo = new MultiMailSenderInfo();
        mailInfo.setMailServerHost("smtp.qiye.163.com");
        mailInfo.setMailServerPort("25");
        mailInfo.setValidate(true);
        mailInfo.setUsername(Constant.EMAIL_NAME);
        mailInfo.setPassword(Constant.EMAIL_PSWD);
        mailInfo.setFromAddress(Constant.EMAIL_NAME);
        // 根据部门获取部门经理
        DemandDO demandDO =planDao.searchDeptUserEmail(preproductionDO.getApplicationDept());
        // 收件人 mailRecipient + 部门经理
        String mailToAddress  = preproductionDO.getMailRecipient()+";"+demandDO.getMonRemark();
        if(LemonUtils.getEnv().equals(Env.SIT)) {
            mailInfo.setReceivers(mailToAddress.split(";"));
        }
        else if(LemonUtils.getEnv().equals(Env.DEV)) {
            mailToAddress = "tu_yi@hisuntech.com";
            mailInfo.setReceivers(mailToAddress.split(";"));
        }
        // 抄送人 mailCopyPerson  申请人 开发负责人
        // 获取申请人邮箱 开发负责人邮箱
        List<String> list = new ArrayList<>();
        list.add(preproductionDO.getPreApplicant());
        list.add(preproductionDO.getDevelopmentLeader());
        String [] nameList = new String[list.size()];
        nameList = list.toArray(nameList);
        DemandDO demandDO1 =planDao.searchUserLEmail(nameList);
        if (JudgeUtils.isNotNull(demandDO1)){
            mailInfo.setCcs(demandDO1.getMonRemark().split(";"));
        }
        mailInfo.setSubject("【预投产通知】-" + preproductionDO.getPreNeed() + "-" + preproductionDO.getPreNumber() + "-" + preproductionDO.getPreApplicant());

        StringBuffer sb = new StringBuffer();
        sb.append("<table border='1' style='border-collapse: collapse;background-color: white; white-space: nowrap;'>");
        sb.append("<tr><td colspan='6' style='text-align: center;font-weight: bold;'>预投产通知邮件</td></tr>");
        sb.append("<tr><td style='font-weight: bold;'>预投产编号</td><td>" + preproductionDO.getPreNumber() + "</td><td style='font-weight: bold;'>需求名称</td><td>" + preproductionDO.getPreNeed() + "</td></tr>");
        sb.append("<tr><td style='font-weight: bold;'>申请部门</td><td>" + preproductionDO.getApplicationDept() + "</td><td style='font-weight: bold;'>计划预投产日期</td><td>" + preproductionDO.getPreDate() + "</td></tr>");
        sb.append("<tr><td style='font-weight: bold;'>预投产申请人</td><td>" + preproductionDO.getPreApplicant() + "</td><td style='font-weight: bold;'>申请人联系方式</td><td>" + preproductionDO.getApplicantTel() + "</td></tr>");
        sb.append("<tr><td style='font-weight: bold;'>开发负责人</td><td>" + preproductionDO.getDevelopmentLeader() + "</td><td style='font-weight: bold;'>产品经理</td><td>" + preproductionDO.getPreManager() + "</td></tr>");
        sb.append("<tr><td style='font-weight: bold;'>验证人</td><td>" + preproductionDO.getIdentifier() + "</td><td style='font-weight: bold;'>验证人联系方式</td><td>" + preproductionDO.getIdentifierTel() + "</td></tr>");
        sb.append("<tr><td style='font-weight: bold;'>验证复核人</td><td>" + preproductionDO.getProChecker() + "</td><td style='font-weight: bold;'>金科负责人邮箱</td><td>" + preproductionDO.getMailRecipient() + "</td></tr>");
        sb.append("<tr><td style='font-weight: bold;'>是否有DBA操作</td><td>" + preproductionDO.getIsDbaOperation() + "</td><td style='font-weight: bold;'>DBA操作是否完成</td><td>" + preproductionDO.getIsDbaOperationComplete() + "</td></tr>");
        sb.append("<tr><td style='font-weight: bold;'>备注</td><td colspan='5'>" + preproductionDO.getRemark() + "</td></tr></table>");
        mailInfo.setContent("各位领导好:<br/>&nbsp;&nbsp;本次预投产申请详细内容请参见下表<br/>烦请查看，谢谢！<br/>" + sb.toString());
//        // 这个类主要来发送邮件
        sendMailService.sendMail(mailInfo);
    }

    // 判断是否为角色权限
    public boolean isDepartmentManager(Long juese ){
        //查询该操作员角色
        UserRoleDO userRoleDO = new UserRoleDO();
        userRoleDO.setRoleId(juese);
        userRoleDO.setUserNo(Long.parseLong(SecurityUtils.getLoginUserId()));
        List<UserRoleDO> userRoleDOS =new LinkedList<>();
        userRoleDOS  = userRoleExtDao.find(userRoleDO);
        if (!userRoleDOS.isEmpty()){
            return true ;
        }
        return false ;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void updateAllProduction(String taskIdStr){
        //获取登录用户名
        String currentUser = userService.getFullname(SecurityUtils.getLoginName());
        //生成流水记录
        ScheduleDO scheduleBean =new ScheduleDO(currentUser);
        String[] pro_number_list=taskIdStr.split("~");
        if(pro_number_list[0].equals("1")){
            //return ajaxDoneError("请填写进行此操作原因");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("请填写进行此操作原因");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }

        String pro_status_after = "";
        String pro_status_before = "";
        // 预投产待部署
        if(pro_number_list[0].equals("dbs")){
            if(pro_number_list.length==1){
                //return ajaxDoneError("请选择投产进行操作!");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择投产进行操作");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            pro_status_before = "预投产提出";
            pro_status_after = "预投产待部署";
            scheduleBean.setOperationReason("预投产待部署");
        }
        //预投产已部署
        else if(pro_number_list[0].equals("ytc")){
            if(pro_number_list.length==1){
                //return ajaxDoneError("请选择投产进行操作!");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择投产进行操作");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            pro_status_before = "预投产待部署";
            pro_status_after = "预投产部署待验证";
            scheduleBean.setOperationReason("预投产已部署");
        }//预投产验证通过
        else if(pro_number_list[0].equals("yztg")){
            if(pro_number_list.length==1){
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择投产进行操作");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            pro_status_before = "预投产部署待验证";
            pro_status_after = "预投产验证完成";
            scheduleBean.setOperationReason("预投产验证已通过");
        } else if (pro_number_list[0].equals("dh")) {
            pro_status_before = "预投产待部署";
            pro_status_after = "预投产打回";
            if ((pro_number_list.length == 1) || (pro_number_list.length == 2)) {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择投产进行操作");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            scheduleBean.setOperationReason(pro_number_list[1]);
        }else if (pro_number_list[0].equals("qx")) {
            pro_status_before = "预投产提出";
            pro_status_after = "预投产取消";
            if ((pro_number_list.length == 1) || (pro_number_list.length == 2)){
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择投产进行操作");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            scheduleBean.setOperationReason(pro_number_list[1]);
        }else if (pro_number_list[0].equals("ht")) {
            pro_status_before = "预投产部署待验证";
            pro_status_after = "预投产回退";
            if ((pro_number_list.length == 1) || (pro_number_list.length == 2)){
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择投产进行操作");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            scheduleBean.setOperationReason(pro_number_list[1]);
        }
        scheduleBean.setPreOperation(pro_status_before);
        scheduleBean.setAfterOperation(pro_status_after);
        scheduleBean.setOperationType(pro_status_after);

        for (int i = 2; i < pro_number_list.length; ++i) {
            String status = iPreproductionExtDao.get(pro_number_list[i]).getPreStatus();
            if (pro_status_after.equals("预投产取消")) {
                String applicant = iPreproductionExtDao.get(pro_number_list[i]).getPreApplicant();
                String pro_manager = iPreproductionExtDao.get(pro_number_list[i]).getPreManager();
                if ((!(currentUser.equals(applicant))) && (!(currentUser.equals(pro_manager)))) {
                    MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                    MsgEnum.ERROR_CUSTOM.setMsgInfo("您不是投产申请人或者负责该投产的产品经理!");
                    BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
                }
            }
            if(!(pro_status_before.equals(status) || (pro_status_after.equals("预投产打回") && (status.equals("预投产待部署")) ) || (pro_status_after.equals("预投产回退") && status.equals("预投产验证完成")) ||(pro_status_after.equals("预投产取消") && (status.equals("预投产提出")|| status.equals("预投产待部署"))))){
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择符合当前操作类型的正确投产状态!");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
        }

        for (int j = 2; j < pro_number_list.length; ++j) {
            String status = iPreproductionExtDao.get(pro_number_list[j]).getPreStatus();
            SimpleDateFormat sdfmonth =new SimpleDateFormat("yyyy-MM");
            String month = sdfmonth.format(iPreproductionExtDao.get(pro_number_list[j]).getPreDate());
            scheduleBean.setProNumber(pro_number_list[j]);
            scheduleBean.setPreOperation(status);
            // 创建邮件信息
            MultiMailSenderInfo mailInfo = new MultiMailSenderInfo();
            mailInfo.setMailServerHost("smtp.qiye.163.com");
            mailInfo.setMailServerPort("25");
            mailInfo.setValidate(true);
            mailInfo.setUsername(Constant.EMAIL_NAME);
            mailInfo.setPassword(Constant.EMAIL_PSWD);
            mailInfo.setFromAddress(Constant.EMAIL_NAME);
            //boolean isSend = true;
            if ((((pro_status_before.equals(status)) || ((pro_status_after.equals("预投产打回")) && (status.equals("预投产待部署"))) || ((pro_status_after.equals("预投产回退")) && (status.equals("预投产验证完成"))|| (status.equals("预投产部署待验证"))) || ((pro_status_after.equals("预投产取消")) && (((status.equals("预投产提出")) || (status.equals("预投产待部署"))))))) &&
                    (((pro_status_after.equals("预投产打回")) || (pro_status_after.equals("预投产回退")) || (pro_status_after.equals("预投产取消")))))
            {
                MailFlowConditionDO mfva = new MailFlowConditionDO();
                mfva.setEmployeeName(iPreproductionExtDao.get(pro_number_list[j]).getPreApplicant());
                MailFlowDO mfba = operationProductionDao.searchUserEmail(mfva);
                //状态变更
                PreproductionDO bean = iPreproductionExtDao.get(pro_number_list[j]);
                bean.setPreStatus(pro_status_after);
                iPreproductionExtDao.updatePreSts(bean);
                // 获取更改前的状态
                String PreOperation = bean.getPreStatus();
                MailFlowDO bnb = new MailFlowDO("预投产不合格结果反馈", "code_review@hisuntech.com", mfba.getEmployeeEmail(), "");

                String[] mailToAddress = mfba.getEmployeeEmail().split(";");
                // 收件人 预投产申请人
                mailInfo.setReceivers(mailToAddress);
                String mess = null;
                if (pro_status_after.equals("预投产打回")) {
                    mess = pro_status_after;
                }
                if (pro_status_after.equals("预投产回退")) {
                    mess = pro_status_after;
                }
                if (pro_status_after.equals("预投产取消")) {
                    mess = pro_status_after;
//                    // 自动化调用投产取消接口
//                    AutoCancellationProductionBO autoCancellationProductionBO = new AutoCancellationProductionBO();
//                    autoCancellationProductionBO.setProNumber(bean.getPreNumber());
//                    autoCancellationProductionBO.setReason(pro_number_list[1]);
//                    System.err.println(Thread.currentThread().getName());
//                    String msg = automaticCommissioningInterfaceService.autoCancellationProduction(autoCancellationProductionBO);
//                    if("ERROR".equals(msg)){
//                        MsgEnum.SUCCESS.setMsgInfo("");
//                        MsgEnum.SUCCESS.setMsgInfo("调用自动化投产取消接口失败！请刷新后再试，或人工联系投产人员。");
//                        BusinessException.throwBusinessException(MsgEnum.SUCCESS);
//                    }
                }

                mailInfo.setSubject("【" + mess + "通知】");
                mailInfo.setContent("你好:<br/>由于【" + pro_number_list[1] + "】，您的" + pro_number_list[j] + bean.getPreNeed() + ",中止预投产流程。");

                // 这个类主要来发送邮件
                sendMailService.sendMail(mailInfo);
                operationProductionDao.addMailFlow(bnb);
                ScheduleDO schedule=new ScheduleDO(bean.getPreNumber(), currentUser, bean.getPreStatus(), PreOperation, bean.getPreStatus(), mess);
                operationProductionDao.insertSchedule(schedule);
            }

            if(pro_status_after.equals("预投产待部署")){
                PreproductionDO  beanCheck=iPreproductionExtDao.get(pro_number_list[j]);
                if(beanCheck.getProductionDeploymentResult().equals("已部署")){
                    MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                    MsgEnum.ERROR_CUSTOM.setMsgInfo("当前投产预投产已部署，不可重复操作!");
                    BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
                }
                // todo 调用自动化投产接口 提供投产编号，包名，环境
//                AutomatedProductionBO automatedProductionBO = new AutomatedProductionBO();
//                automatedProductionBO.setEnv("0");
//                automatedProductionBO.setProNumber(beanCheck.getPreNumber());
//                automatedProductionBO.setProPkgName(beanCheck.getProPkgName());
//                System.err.println(Thread.currentThread().getName());
//                String msg = automaticCommissioningInterfaceService.automatedProduction(automatedProductionBO);
//                if("ERROR".equals(msg)){
//                    MsgEnum.SUCCESS.setMsgInfo("");
//                    MsgEnum.SUCCESS.setMsgInfo("调用增加自动化投产包接口调用失败！请刷新后再试。");
//                    BusinessException.throwBusinessException(MsgEnum.SUCCESS);
//                }
//                // addpack();
                PreproductionDO  bean=iPreproductionExtDao.get(pro_number_list[j]);
                bean.setPreStatus("预投产待部署");
                iPreproductionExtDao.updatePreSts(bean);
            }

            if(pro_status_after.equals("预投产部署待验证")){
                PreproductionDO  beanCheck=iPreproductionExtDao.get(pro_number_list[j]);
                if(beanCheck.getProductionDeploymentResult().equals("已部署")){
                    //return ajaxDoneError("当前投产预投产已部署，不可重复操作!");
                    MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                    MsgEnum.ERROR_CUSTOM.setMsgInfo(beanCheck.getPreNeed() + "-" + beanCheck.getPreNumber() + "当前投产预投产已部署，不可重复操作!");
                    BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
                }
                PreproductionDO  bean=iPreproductionExtDao.get(pro_number_list[j]);
                System.err.println(bean);
                bean.setProductionDeploymentResult("已部署");
                bean.setPreStatus("预投产部署待验证");
                iPreproductionExtDao.updatePreSts(bean);
            }

            if(pro_status_after.equals("预投产验证完成")){
                PreproductionDO  beanCheck=iPreproductionExtDao.get(pro_number_list[j]);
                if(!currentUser.equals(beanCheck.getPreManager()) && !currentUser.equals(beanCheck.getPreApplicant()) && !currentUser.equals(beanCheck.getDevelopmentLeader())){
                    MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                    MsgEnum.ERROR_CUSTOM.setMsgInfo("只有负责该投产的产品经理,申请人以及开发负责人才能验证通过!");
                    BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
                }
                if(beanCheck.getProAdvanceResult().equals("通过")){
                    MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                    MsgEnum.ERROR_CUSTOM.setMsgInfo(beanCheck.getPreNeed() + "-" + beanCheck.getPreNumber() + "当前预投产验证已通过,不可重复操作!");
                    BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
                }

                PreproductionDO  bean=iPreproductionExtDao.get(pro_number_list[j]);
                bean.setProAdvanceResult("通过");
                bean.setPreStatus("预投产验证完成");
                iPreproductionExtDao.updatePreSts(bean);
                ScheduleDO sBean=new ScheduleDO();
                sBean.setPreOperation(bean.getPreStatus());
                ScheduleDO schedule=new ScheduleDO(bean.getPreNumber(), currentUser, "预投产验证通过", "预投产部署待验证", sBean.getPreOperation(), "预投产验证已通过");
                operationProductionDao.insertSchedule(schedule);
                //是否预投产验证为“是”时，预投产验证结果为“通过”，需求当前阶段变更为“完成预投产”
                if ( bean.getProAdvanceResult().equals("通过")) {
                    // 根据编号查询需求信息
                    DemandDO demanddo = new DemandDO();
                    demanddo.setReqNo(pro_number_list[j]);
                    List<DemandDO> demandBOList = demandDao.find(demanddo);
                    if(!demandBOList.isEmpty()){
                        for(int i =0;i<demandBOList.size();i++){
                            // 投产月份  = 需求实施月份时 ，改变需求状态
                            if(demandBOList.get(i).getReqImplMon().compareTo(month)==0){
                                DemandDO demand = demandBOList.get(i);
                                if (!JudgeUtils.isNull(demand)) {
                                    //投产状态为“投产待部署”时，需求当前阶段变更为“完成预投产”  16
                                    demand.setPreCurPeriod("160");
                                    DemandBO demandBO = new DemandBO();
                                    BeanConvertUtils.convert(demandBO, demand);
                                    //登记需求阶段记录表
                                    String remarks="预投产状态自动修改";
                                    reqPlanService.registrationDemandPhaseRecordForm(demandBO,remarks);
                                    demand.setReqSts("20");
                                    demandDao.updateOperation(demand);
                                }
                            }
                        }
                    }
                }
                // 邮件通知
                // 根据部门获取部门经理
                DemandDO demandDO =planDao.searchDeptUserEmail(bean.getApplicationDept());
                // 收件人 mailRecipient + 部门经理
                String mailToAddress  = bean.getMailRecipient()+";"+demandDO.getMonRemark();
                if(LemonUtils.getEnv().equals(Env.SIT)) {
                    mailInfo.setReceivers(mailToAddress.split(";"));
                }
                else if(LemonUtils.getEnv().equals(Env.DEV)) {
                    mailToAddress = "tu_yi@hisuntech.com";
                    mailInfo.setReceivers(mailToAddress.split(";"));
                }
                // 抄送人 mailCopyPerson  申请人 开发负责人
                // 获取申请人邮箱 开发负责人邮箱
                List<String> list = new ArrayList<>();
                list.add(bean.getPreApplicant());
                list.add(bean.getDevelopmentLeader());
                String [] nameList = new String[list.size()];
                nameList = list.toArray(nameList);
                DemandDO demandDO1 =planDao.searchUserLEmail(nameList);
                if (JudgeUtils.isNotNull(demandDO1)){
                    mailInfo.setCcs(demandDO1.getMonRemark().split(";"));
                }
                mailInfo.setSubject("【预投产结果通报】-" + bean.getPreNeed() + "-" + bean.getPreNumber() + "-" + bean.getPreApplicant());
                StringBuffer sb = new StringBuffer();
                sb.append("<table border='1' style='border-collapse: collapse;background-color: white; white-space: nowrap;'>");
                sb.append("<tr><td colspan='6' style='text-align: center;font-weight: bold;'>预投产验证结果反馈</td></tr>");
                sb.append("<tr><td style='font-weight: bold;'>预投产编号</td><td>" + bean.getPreNumber() + "</td><td style='font-weight: bold;'>需求名称</td><td>" + bean.getPreNeed() + "</td></tr>");
                sb.append("<tr><td style='font-weight: bold;'>申请部门</td><td>" + bean.getApplicationDept() + "</td><td style='font-weight: bold;'>计划预投产日期</td><td>" + bean.getPreDate() + "</td></tr>");
                sb.append("<tr><td style='font-weight: bold;'>预投产申请人</td><td>" + bean.getPreApplicant() + "</td><td style='font-weight: bold;'>申请人联系方式</td><td>" + bean.getApplicantTel() + "</td></tr>");
                sb.append("<tr><td style='font-weight: bold;'>开发负责人</td><td>" + bean.getDevelopmentLeader() + "</td><td style='font-weight: bold;'>产品经理</td><td>" + bean.getPreManager() + "</td></tr>");
                sb.append("<tr><td style='font-weight: bold;'>验证人</td><td>" + bean.getIdentifier() + "</td><td style='font-weight: bold;'>验证人联系方式</td><td>" + bean.getIdentifierTel() + "</td></tr>");
                sb.append("<tr><td style='font-weight: bold;'>验证复核人</td><td>" + bean.getProChecker() + "</td><td style='font-weight: bold;'>金科负责人邮箱</td><td>" + bean.getMailRecipient() + "</td></tr>");
                sb.append("<tr><td style='font-weight: bold;'>是否有DBA操作</td><td>" + bean.getIsDbaOperation() + "</td><td style='font-weight: bold;'>DBA操作是否完成</td><td>" + bean.getIsDbaOperationComplete() + "</td></tr>");
                sb.append("<tr><td style='font-weight: bold;'>备注</td><td colspan='5'>" + bean.getRemark() + "</td></tr></table>");
                mailInfo.setContent("各位好：<br/>&nbsp;&nbsp;本次预投产验证已完成，验证通过无问题，请知悉。谢谢！<br/>" + sb.toString());
//        // 这个类主要来发送邮件
                sendMailService.sendMail(mailInfo);

            }

        }
        return ;
    }

    /**
     * DBA操作完成
     * @param taskIdStr
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void updateAllProductionDBA(String taskIdStr){
        //获取登录用户名
        String currentUser = userService.getFullname(SecurityUtils.getLoginName());
        //生成流水记录
        ScheduleDO scheduleBean =new ScheduleDO(currentUser);
        // 所有预投产编号数组
        String[] pro_number_list=taskIdStr.split("~");
        if(pro_number_list[0].equals("1")){
            //return ajaxDoneError("请填写进行此操作原因");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("请填写进行此操作原因");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        // 先判断所选择的预投产编号是否需要DBA操作，如果不需要，则提示报错
        for (int i = 2; i < pro_number_list.length; ++i) {
            // 获取预投产当前状态
            String status = iPreproductionExtDao.get(pro_number_list[i]).getPreStatus();
            // 获取是否需要DBA操作字段
            String isDbaOperation = iPreproductionExtDao.get(pro_number_list[i]).getIsDbaOperation();
            // 获取DBA操作是否完成字段
            String isDbaOperationComplete = iPreproductionExtDao.get(pro_number_list[i]).getIsDbaOperationComplete();
            // 先判断预投产状态是否符合需求
            //DBA操作完成不需要达到预投产待部署状态
/*            if(!"预投产待部署".equals(status)){
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo(iPreproductionExtDao.get(pro_number_list[i]).getPreNumber()+"的预投产状态不为【预投产待部署】，请更新正确的预投产状态！");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }*/
            // 判断是否需要DBA操作， 需要再判断，是否已经操作完成
            if("是".equals(isDbaOperation)){
                // 需要DBA操作，
                if("是".equals(isDbaOperationComplete)){
                    MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                    MsgEnum.ERROR_CUSTOM.setMsgInfo(iPreproductionExtDao.get(pro_number_list[i]).getPreNumber()+"DBA操作已完成，请不要重复操作！");
                    BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
                }
            }else{
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo(iPreproductionExtDao.get(pro_number_list[i]).getPreNumber()+"不需要DBA操作，请选择正确状态的预投产需求！");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }

        }

        for (int j = 2; j < pro_number_list.length; ++j) {
            PreproductionDO preproductionDO = iPreproductionExtDao.get(pro_number_list[j]);
            preproductionDO.setPreNumber(pro_number_list[j]);
            preproductionDO.setIsDbaOperation("是");
            preproductionDO.setIsDbaOperationComplete("是");
            // 更新DBA操作是否完成标志
            iPreproductionExtDao.updatePreDBA(preproductionDO);
            // 记录操作
            scheduleBean.setProNumber(pro_number_list[j]);
            scheduleBean.setProOperator(currentUser);
            scheduleBean.setOperationType("DBA操作完成");
            scheduleBean.setPreOperation(preproductionDO.getPreStatus());
            scheduleBean.setOperationReason("预投产DBA操作完成");
            scheduleBean.setAfterOperation(preproductionDO.getPreStatus());
            operationProductionDao.insertSchedule(scheduleBean);
            // 邮件通知版本组，及时更新状态
            // 发送邮件通知
            // 创建邮件信息
            MultiMailSenderInfo mailInfo = new MultiMailSenderInfo();
            mailInfo.setMailServerHost("smtp.qiye.163.com");
            mailInfo.setMailServerPort("25");
            mailInfo.setValidate(true);
            mailInfo.setUsername(Constant.EMAIL_NAME);
            mailInfo.setPassword(Constant.EMAIL_PSWD);
            mailInfo.setFromAddress(Constant.EMAIL_NAME);
            // 收件人 版本组
            String[] mailToAddress  ={"version_it@hisuntech.com"};
            if(LemonUtils.getEnv().equals(Env.SIT)) {
                mailInfo.setReceivers(mailToAddress);
            }
            else if(LemonUtils.getEnv().equals(Env.DEV)) {
                mailInfo.setReceivers("tu_yi@hisuntech.com".split(";"));
            }
            // 抄送人 申请人和开发负责人
            List<String> list = new ArrayList<>();
            list.add(preproductionDO.getPreApplicant());
            list.add(preproductionDO.getDevelopmentLeader());
            String [] nameList = new String[list.size()];
            nameList = list.toArray(nameList);
            DemandDO demandDO1 =planDao.searchUserLEmail(nameList);
            if (JudgeUtils.isNotNull(demandDO1)){
                mailInfo.setCcs(demandDO1.getMonRemark().split(";"));
            }
            mailInfo.setSubject("【预投产DBA操作完成通知】-" + preproductionDO.getPreNeed() + "-" + preproductionDO.getPreNumber() + "-" + preproductionDO.getPreApplicant());
            StringBuffer sb = new StringBuffer();
            sb.append("<table border='1' style='border-collapse: collapse;background-color: white; white-space: nowrap;'>");
            sb.append("<tr><td colspan='6' style='text-align: center;font-weight: bold;'>预投产DBA操作完成通知</td></tr>");
            sb.append("<tr><td style='font-weight: bold;'>预投产编号</td><td>" + preproductionDO.getPreNumber() + "</td><td style='font-weight: bold;'>需求名称</td><td>" + preproductionDO.getPreNeed() + "</td></tr>");
            sb.append("<tr><td style='font-weight: bold;'>申请部门</td><td>" + preproductionDO.getApplicationDept() + "</td><td style='font-weight: bold;'>计划预投产日期</td><td>" + preproductionDO.getPreDate() + "</td></tr>");
            sb.append("<tr><td style='font-weight: bold;'>预投产申请人</td><td>" + preproductionDO.getPreApplicant() + "</td><td style='font-weight: bold;'>申请人联系方式</td><td>" + preproductionDO.getApplicantTel() + "</td></tr>");
            sb.append("<tr><td style='font-weight: bold;'>开发负责人</td><td>" + preproductionDO.getDevelopmentLeader() + "</td><td style='font-weight: bold;'>产品经理</td><td>" + preproductionDO.getPreManager() + "</td></tr>");
            sb.append("<tr><td style='font-weight: bold;'>验证人</td><td>" + preproductionDO.getIdentifier() + "</td><td style='font-weight: bold;'>验证人联系方式</td><td>" + preproductionDO.getIdentifierTel() + "</td></tr>");
            sb.append("<tr><td style='font-weight: bold;'>验证复核人</td><td>" + preproductionDO.getProChecker() + "</td><td style='font-weight: bold;'>金科负责人邮箱</td><td>" + preproductionDO.getMailRecipient() + "</td></tr>");
            sb.append("<tr><td style='font-weight: bold;'>是否有DBA操作</td><td>" + preproductionDO.getIsDbaOperation() + "</td><td style='font-weight: bold;'>DBA操作是否完成</td><td>" + preproductionDO.getIsDbaOperationComplete() + "</td></tr>");
            sb.append("<tr><td style='font-weight: bold;'>备注</td><td colspan='5'>" + preproductionDO.getRemark() + "</td></tr></table>");
            mailInfo.setContent(preproductionDO.getPreNeed() + "-" + preproductionDO.getPreNumber() + "的DBA操作已完成:<br/>&nbsp;&nbsp;请版本组尽快执行版本组操作包，并及时更新研发管理系统状态！<br/>"+ sb.toString());
//        // 这个类主要来发送邮件
            sendMailService.sendMail(mailInfo);

        }
    }

    /**
     * 版本组操作完成
     * @param taskIdStr
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void updateAllProductionBBZ(String taskIdStr){
        //获取登录用户名
        String currentUser = userService.getFullname(SecurityUtils.getLoginName());
        //生成流水记录
        ScheduleDO scheduleBean =new ScheduleDO(currentUser);
        String[] pro_number_list=taskIdStr.split("~");
        if(pro_number_list[0].equals("1")){
            //return ajaxDoneError("请填写进行此操作原因");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("请填写进行此操作原因");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        // 先判断所选择的预投产编号是否需要DBA操作，如果需要，则判断是否DBA操作完成
        for (int i = 2; i < pro_number_list.length; ++i) {
            // 获取预投产当前状态
            String status = iPreproductionExtDao.get(pro_number_list[i]).getPreStatus();
            // 获取是否需要DBA操作字段
            String isDbaOperation = iPreproductionExtDao.get(pro_number_list[i]).getIsDbaOperation();
            // 获取DBA操作是否完成字段
            String isDbaOperationComplete = iPreproductionExtDao.get(pro_number_list[i]).getIsDbaOperationComplete();
            // 先判断预投产状态是否符合需求
            if(!"预投产待部署".equals(status)){
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo(iPreproductionExtDao.get(pro_number_list[i]).getPreNumber()+"的预投产状态不为【预投产待部署】，请更新正确的预投产状态！");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            // 判断是否需要DBA操作， 需要再判断，是否已经操作完成
            if("是".equals(isDbaOperation)){
                // 需要DBA操作，但DBA操作未完成
                if("否".equals(isDbaOperationComplete)){
                    MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                    MsgEnum.ERROR_CUSTOM.setMsgInfo(iPreproductionExtDao.get(pro_number_list[i]).getPreNumber()+"DBA操作未完成，请先完成DBA操作！");
                    BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
                }
            }

        }
        for (int j = 2; j < pro_number_list.length; ++j) {
            PreproductionDO preproductionDO = iPreproductionExtDao.get(pro_number_list[j]);
            preproductionDO.setPreNumber(pro_number_list[j]);
            preproductionDO.setProductionDeploymentResult("已部署");
            preproductionDO.setPreStatus("预投产部署待验证");
            iPreproductionExtDao.updatePreSts(preproductionDO);

            // 记录操作
            scheduleBean.setProNumber(pro_number_list[j]);
            scheduleBean.setOperationType("版本组操作完成");
            scheduleBean.setPreOperation("预投产待部署");
            scheduleBean.setOperationReason("预投产版本组操作完成");
            scheduleBean.setAfterOperation("预投产部署待验证");
            operationProductionDao.insertSchedule(scheduleBean);
            // 邮件通知版本组，及时更新状态
            // 发送邮件通知
            // 创建邮件信息
            MultiMailSenderInfo mailInfo = new MultiMailSenderInfo();
            mailInfo.setMailServerHost("smtp.qiye.163.com");
            mailInfo.setMailServerPort("25");
            mailInfo.setValidate(true);
            mailInfo.setUsername(Constant.EMAIL_NAME);
            mailInfo.setPassword(Constant.EMAIL_PSWD);
            mailInfo.setFromAddress(Constant.EMAIL_NAME);
            // 邮件通知 申请人 开发负责人
            List<String> list = new ArrayList<>();
            list.add(preproductionDO.getPreApplicant());
            list.add(preproductionDO.getDevelopmentLeader());
            String [] nameList = new String[list.size()];
            nameList = list.toArray(nameList);
            DemandDO demandDO1 =planDao.searchUserLEmail(nameList);
            mailInfo.setReceivers(demandDO1.getMonRemark().split(";"));

            mailInfo.setSubject("【预投产版本组操作完成通知】-" + preproductionDO.getPreNeed() + "-" + preproductionDO.getPreNumber() + "-" + preproductionDO.getPreApplicant());
            StringBuffer sb = new StringBuffer();
            sb.append("<table border='1' style='border-collapse: collapse;background-color: white; white-space: nowrap;'>");
            sb.append("<tr><td colspan='6' style='text-align: center;font-weight: bold;'>预投产版本组操作完成通知</td></tr>");
            sb.append("<tr><td style='font-weight: bold;'>预投产编号</td><td>" + preproductionDO.getPreNumber() + "</td><td style='font-weight: bold;'>需求名称</td><td>" + preproductionDO.getPreNeed() + "</td></tr>");
            sb.append("<tr><td style='font-weight: bold;'>申请部门</td><td>" + preproductionDO.getApplicationDept() + "</td><td style='font-weight: bold;'>计划预投产日期</td><td>" + preproductionDO.getPreDate() + "</td></tr>");
            sb.append("<tr><td style='font-weight: bold;'>预投产申请人</td><td>" + preproductionDO.getPreApplicant() + "</td><td style='font-weight: bold;'>申请人联系方式</td><td>" + preproductionDO.getApplicantTel() + "</td></tr>");
            sb.append("<tr><td style='font-weight: bold;'>开发负责人</td><td>" + preproductionDO.getDevelopmentLeader() + "</td><td style='font-weight: bold;'>产品经理</td><td>" + preproductionDO.getPreManager() + "</td></tr>");
            sb.append("<tr><td style='font-weight: bold;'>验证人</td><td>" + preproductionDO.getIdentifier() + "</td><td style='font-weight: bold;'>验证人联系方式</td><td>" + preproductionDO.getIdentifierTel() + "</td></tr>");
            sb.append("<tr><td style='font-weight: bold;'>验证复核人</td><td>" + preproductionDO.getProChecker() + "</td><td style='font-weight: bold;'>金科负责人邮箱</td><td>" + preproductionDO.getMailRecipient() + "</td></tr>");
            sb.append("<tr><td style='font-weight: bold;'>是否有DBA操作</td><td>" + preproductionDO.getIsDbaOperation() + "</td><td style='font-weight: bold;'>DBA操作是否完成</td><td>" + preproductionDO.getIsDbaOperationComplete() + "</td></tr>");
            sb.append("<tr><td style='font-weight: bold;'>备注</td><td colspan='5'>" + preproductionDO.getRemark() + "</td></tr></table>");
            mailInfo.setContent(preproductionDO.getPreNeed() + "-" + preproductionDO.getPreNumber() + "的版本组操作已完成:<br/>&nbsp;&nbsp;请及时验证，并及时更新研发管理系统状态！<br/>"+sb.toString());
//        // 这个类主要来发送邮件
            sendMailService.sendMail(mailInfo);

        }

    }
    /**
     * 版本组投产包上传
     * @param file
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void doBatchImport(MultipartFile file,String reqNumber) {
        if (file.isEmpty()) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择上传文件!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        String currentUser =  userService.getFullname(SecurityUtils.getLoginName());
        PreproductionDO bean = null;
        bean = iPreproductionExtDao.get(reqNumber);
        if(!currentUser.equals(bean.getPreApplicant())&&!currentUser.equals(bean.getDevelopmentLeader())){
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("只有负责投产的申请提出人或开发负责人才能上传投产包!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        // 判断两个操作包 包名不能一致
        if(JudgeUtils.isNotEmpty(bean.getDdlPkgName()) ){
            if(file.getOriginalFilename().equals(bean.getDdlPkgName())){
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("版本组操作包包名不能与DBA操作包包名一致!");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
        }
        //依据环境配置路径
        String path="";
        if(LemonUtils.getEnv().equals(Env.SIT)) {
            path= "/home/devms/temp/preproduction/propkg/bbz/";
        }
        else if(LemonUtils.getEnv().equals(Env.DEV)) {
            path= "/home/devadm/temp/preproduction/propkg/bbz/";
        }else {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("当前配置环境路径有误，请尽快联系管理员!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        File fileDir = new File(path + reqNumber);
        File filePath = new File(fileDir.getPath()+"/"+file.getOriginalFilename());
        LOGGER.info(fileDir.getPath());
        LOGGER.info(filePath.getPath());
        if(fileDir.exists()){
            File[] oldFile = fileDir.listFiles();
            for(File o:oldFile) o.delete();
        }else{
            fileDir.mkdir();
        }
//        boolean flag = true;
        try {
            file.transferTo(filePath);
//            // 将服务器中的投产包上传到ftp服务器
//            FileInputStream in=new FileInputStream(filePath);
//            flag = FtpUtil.uploadFile("10.9.102.186", 21, "admin", "admin", "/home/ftpuser/www/images", "/2020/04/09", file.getOriginalFilename(), in);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("版本组操作包上传失败");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        } catch (IOException e) {
            e.printStackTrace();
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("版本组操作包上传失败");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
//        if(!flag){
//            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
//            MsgEnum.ERROR_CUSTOM.setMsgInfo("文件上传FTP服务器失败，请重新上传");
//            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
//        }
        // 发送邮件通知
        // 创建邮件信息
        MultiMailSenderInfo mailInfo = new MultiMailSenderInfo();
        mailInfo.setMailServerHost("smtp.qiye.163.com");
        mailInfo.setMailServerPort("25");
        mailInfo.setValidate(true);
        mailInfo.setUsername(Constant.EMAIL_NAME);
        mailInfo.setPassword(Constant.EMAIL_PSWD);
        mailInfo.setFromAddress(Constant.EMAIL_NAME);

        bean.setProPkgTime(LocalDateTime.now());
        bean.setProPkgName(file.getOriginalFilename());
        // 更新包信息
        iPreproductionExtDao.updatePropkg(bean);
        //更新预投产状态 ，当当前状态为预投产提出时
        if(bean.getPreStatus().equals("预投产提出")){
            if(bean.getIsDbaOperation().equals("是")){
                //如果需要DBA操作，则再判断DAB操作包是否已经上传
                if(JudgeUtils.isNotEmpty(bean.getDdlPkgName())){
                    bean.setPreStatus("预投产待部署");
                    iPreproductionExtDao.updatePreSts(bean);
                    //生成流水记录
                    ScheduleDO scheduleBean =new ScheduleDO(currentUser);
                    // 记录操作
                    scheduleBean.setProNumber(bean.getPreNumber());
                    scheduleBean.setOperationType("预投产包上传");
                    scheduleBean.setPreOperation("预投产提出");
                    scheduleBean.setOperationReason("预投产包上传完成");
                    scheduleBean.setAfterOperation("预投产待部署");
                    operationProductionDao.insertSchedule(scheduleBean);
                    // 邮件通知DBA和版本组更新预投产 ，备注DBA先操作ddlSQL
                    // 获取DBA邮箱组
                    MailGroupDO mp = operationProductionDao.findMailGroupBeanDetail("10");
                    // 收件人 版本组加dba
                    if(LemonUtils.getEnv().equals(Env.SIT)) {
                        mailInfo.setReceivers(("version_it@hisuntech.com;"+ mp.getMailUser()).split(";"));
                    }
                    else if(LemonUtils.getEnv().equals(Env.DEV)) {
                        mailInfo.setReceivers(("tu_yi@hisuntech.com;").split(";"));
                    }
                    // 抄送人  申请人 开发负责人
                    // 获取申请人邮箱 开发负责人邮箱
                    List<String> list = new ArrayList<>();
                    list.add(bean.getPreApplicant());
                    list.add(bean.getDevelopmentLeader());
                    String [] nameList = new String[list.size()];
                    nameList = list.toArray(nameList);
                    DemandDO demandDO =planDao.searchUserLEmail(nameList);
                    if (JudgeUtils.isNotNull(demandDO)){
                        mailInfo.setCcs(demandDO.getMonRemark().split(";"));
                    }
                    mailInfo.setSubject("【预投产待部署通知】-" + bean.getPreNeed() + "-" + bean.getPreNumber() + "-" + bean.getPreApplicant());
                    StringBuffer sb = new StringBuffer();
                    sb.append("<table border='1' style='border-collapse: collapse;background-color: white; white-space: nowrap;'>");
                    sb.append("<tr><td colspan='6' style='text-align: center;font-weight: bold;'>预投产待部署通知</td></tr>");
                    sb.append("<tr><td style='font-weight: bold;'>预投产编号</td><td>" + bean.getPreNumber() + "</td><td style='font-weight: bold;'>需求名称</td><td>" + bean.getPreNeed() + "</td></tr>");
                    sb.append("<tr><td style='font-weight: bold;'>申请部门</td><td>" + bean.getApplicationDept() + "</td><td style='font-weight: bold;'>计划预投产日期</td><td>" + bean.getPreDate() + "</td></tr>");
                    sb.append("<tr><td style='font-weight: bold;'>预投产申请人</td><td>" + bean.getPreApplicant() + "</td><td style='font-weight: bold;'>申请人联系方式</td><td>" + bean.getApplicantTel() + "</td></tr>");
                    sb.append("<tr><td style='font-weight: bold;'>开发负责人</td><td>" + bean.getDevelopmentLeader() + "</td><td style='font-weight: bold;'>产品经理</td><td>" + bean.getPreManager() + "</td></tr>");
                    sb.append("<tr><td style='font-weight: bold;'>验证人</td><td>" + bean.getIdentifier() + "</td><td style='font-weight: bold;'>验证人联系方式</td><td>" + bean.getIdentifierTel() + "</td></tr>");
                    sb.append("<tr><td style='font-weight: bold;'>验证复核人</td><td>" + bean.getProChecker() + "</td><td style='font-weight: bold;'>金科负责人邮箱</td><td>" + bean.getMailRecipient() + "</td></tr>");
                    sb.append("<tr><td style='font-weight: bold;'>是否有DBA操作</td><td>" + bean.getIsDbaOperation() + "</td><td style='font-weight: bold;'>DBA操作是否完成</td><td>" + bean.getIsDbaOperationComplete() + "</td></tr>");
                    sb.append("<tr><td style='font-weight: bold;'>备注</td><td colspan='5'>" + bean.getRemark() + "</td></tr></table>");
                    mailInfo.setContent(bean.getPreNeed() + "-" + bean.getPreNumber() + "的预投产包均已上传:<br/>&nbsp;&nbsp;请版本组和DBA及时取包、部署，（先执行DBA操作包，后执行版本组操作包）并及时更新研发管理系统状态！<br/>"+sb.toString());
                    //                  // 这个类主要来发送邮件
                    sendMailService.sendMail(mailInfo);
                }
            } else{
                bean.setPreStatus("预投产待部署");
                // 不需要DBA操作，直接更新状态
                iPreproductionExtDao.updatePreSts(bean);
                //生成流水记录
                ScheduleDO scheduleBean =new ScheduleDO(currentUser);
                // 记录操作
                scheduleBean.setProNumber(bean.getPreNumber());
                scheduleBean.setOperationType("预投产包上传");
                scheduleBean.setPreOperation("预投产提出");
                scheduleBean.setOperationReason("预投产包上传完成");
                scheduleBean.setAfterOperation("预投产待部署");
                operationProductionDao.insertSchedule(scheduleBean);
                // 邮件通知版本组更新预投产
                if(LemonUtils.getEnv().equals(Env.SIT)) {
                    mailInfo.setReceivers(("version_it@hisuntech.com;").split(";"));
                }
                else if(LemonUtils.getEnv().equals(Env.DEV)) {
                    mailInfo.setReceivers(("tu_yi@hisuntech.com;").split(";"));
                }
                // 抄送人  申请人 开发负责人
                // 获取申请人邮箱 开发负责人邮箱
                List<String> list = new ArrayList<>();
                list.add(bean.getPreApplicant());
                list.add(bean.getDevelopmentLeader());
                String [] nameList = new String[list.size()];
                nameList = list.toArray(nameList);
                DemandDO demandDO =planDao.searchUserLEmail(nameList);
                if (JudgeUtils.isNotNull(demandDO)){
                    mailInfo.setCcs(demandDO.getMonRemark().split(";"));
                }
                mailInfo.setSubject("【预投产待部署通知】-" + bean.getPreNeed() + "-" + bean.getPreNumber() + "-" + bean.getPreApplicant());
                StringBuffer sb = new StringBuffer();
                sb.append("<table border='1' style='border-collapse: collapse;background-color: white; white-space: nowrap;'>");
                sb.append("<tr><td colspan='6' style='text-align: center;font-weight: bold;'>预投产待部署通知</td></tr>");
                sb.append("<tr><td style='font-weight: bold;'>预投产编号</td><td>" + bean.getPreNumber() + "</td><td style='font-weight: bold;'>需求名称</td><td>" + bean.getPreNeed() + "</td></tr>");
                sb.append("<tr><td style='font-weight: bold;'>申请部门</td><td>" + bean.getApplicationDept() + "</td><td style='font-weight: bold;'>计划预投产日期</td><td>" + bean.getPreDate() + "</td></tr>");
                sb.append("<tr><td style='font-weight: bold;'>预投产申请人</td><td>" + bean.getPreApplicant() + "</td><td style='font-weight: bold;'>申请人联系方式</td><td>" + bean.getApplicantTel() + "</td></tr>");
                sb.append("<tr><td style='font-weight: bold;'>开发负责人</td><td>" + bean.getDevelopmentLeader() + "</td><td style='font-weight: bold;'>产品经理</td><td>" + bean.getPreManager() + "</td></tr>");
                sb.append("<tr><td style='font-weight: bold;'>验证人</td><td>" + bean.getIdentifier() + "</td><td style='font-weight: bold;'>验证人联系方式</td><td>" + bean.getIdentifierTel() + "</td></tr>");
                sb.append("<tr><td style='font-weight: bold;'>验证复核人</td><td>" + bean.getProChecker() + "</td><td style='font-weight: bold;'>金科负责人邮箱</td><td>" + bean.getMailRecipient() + "</td></tr>");
                sb.append("<tr><td style='font-weight: bold;'>是否有DBA操作</td><td>" + bean.getIsDbaOperation() + "</td><td style='font-weight: bold;'>DBA操作是否完成</td><td>" + bean.getIsDbaOperationComplete() + "</td></tr>");
                sb.append("<tr><td style='font-weight: bold;'>备注</td><td colspan='5'>" + bean.getRemark() + "</td></tr></table>");
                mailInfo.setContent(bean.getPreNeed() + "-" + bean.getPreNumber() + "的预投产包均已上传:<br/>&nbsp;&nbsp;请版本组及时取包、部署，并及时更新研发管理系统状态！<br/>"+sb.toString());
                // 这个类主要来发送邮件
                sendMailService.sendMail(mailInfo);
            }
        }

    }

    /**
     * 上传DBA投产包
     * @param file
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void updateProductionPackage(MultipartFile file,String reqNumber) {
        if (file.isEmpty()) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择上传文件!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        String currentUser =  userService.getFullname(SecurityUtils.getLoginName());
        PreproductionDO bean = null;
        bean = iPreproductionExtDao.get(reqNumber);
        if(!currentUser.equals(bean.getPreApplicant())&&!currentUser.equals(bean.getDevelopmentLeader())){
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("只有负责投产的申请提出人或开发负责人才能上传投产包!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        // 判断两个操作包 包名不能一致
        if(JudgeUtils.isNotEmpty(bean.getProPkgName())){
            if(file.getOriginalFilename().equals(bean.getProPkgName())){
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("DBA操作包包名不能与版本组操作包包名一致!");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
        }
        //依据环境配置路径
        String path="";
        if(LemonUtils.getEnv().equals(Env.SIT)) {
            path= "/home/devms/temp/preproduction/propkg/dba/";
        }
        else if(LemonUtils.getEnv().equals(Env.DEV)) {
            path= "/home/devadm/temp/preproduction/propkg/dba/";
        }else {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("当前配置环境路径有误，请尽快联系管理员!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        File fileDir = new File(path + reqNumber);
        File filePath = new File(fileDir.getPath()+"/"+file.getOriginalFilename());
        LOGGER.info(fileDir.getPath());
        LOGGER.info(filePath.getPath());
        if(fileDir.exists()){
            File[] oldFile = fileDir.listFiles();
            for(File o:oldFile) o.delete();
        }else{
            fileDir.mkdir();
        }
//        boolean flag = true;
        try {
            file.transferTo(filePath);
//            // 将服务器中的投产包上传到ftp服务器
//            FileInputStream in=new FileInputStream(filePath);
//            flag = FtpUtil.uploadFile("10.9.102.186", 21, "admin", "admin", "/home/ftpuser/www/images", "/2020/04/09", file.getOriginalFilename(), in);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("DBA操作包上传失败");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        } catch (IOException e) {
            e.printStackTrace();
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("DBA操作包上传失败");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
//        // 投产包上传ftp服务器成功
//        if(flag){
//            // 上传成功后调用自动化投产接口，调用自动化投产接口 提供投产编号，包名，环境
//            AutomatedProductionBO automatedProductionBO = new AutomatedProductionBO();
//            automatedProductionBO.setEnv("0");
//            automatedProductionBO.setProNumber(reqNumber);
//            automatedProductionBO.setProPkgName(file.getOriginalFilename());
//            automaticCommissioningInterfaceService.automatedProduction(automatedProductionBO);
//
//        }else{
//            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
//            MsgEnum.ERROR_CUSTOM.setMsgInfo("文件上传FTP服务器失败，请重新上传");
//            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
//        }
        bean.setDdlPkgTime(LocalDateTime.now());
        bean.setDdlPkgName(file.getOriginalFilename());
        iPreproductionExtDao.updateDbapkg(bean);

        MultiMailSenderInfo mailInfo = new MultiMailSenderInfo();
        mailInfo.setMailServerHost("smtp.qiye.163.com");
        mailInfo.setMailServerPort("25");
        mailInfo.setValidate(true);
        mailInfo.setUsername(Constant.EMAIL_NAME);
        mailInfo.setPassword(Constant.EMAIL_PSWD);
        mailInfo.setFromAddress(Constant.EMAIL_NAME);
        // 通知DBA DBA操作包已上传
        // 获取DBA邮箱组
        MailGroupDO mp = operationProductionDao.findMailGroupBeanDetail("10");
        if(LemonUtils.getEnv().equals(Env.SIT)) {
            mailInfo.setReceivers(("deng_shj@hisuntech.com;wujinyan@hisuntech.com;xiao_hua@hisuntech.com;"+mp.getMailUser()).split(";"));
        }
        else if(LemonUtils.getEnv().equals(Env.DEV)) {
            mailInfo.setReceivers(("tu_yi@hisuntech.com;").split(";"));
        }
        // 获取申请人邮箱 开发负责人邮箱
        List<String> list = new ArrayList<>();
        list.add(bean.getPreApplicant());
        list.add(bean.getDevelopmentLeader());
        String [] nameList = new String[list.size()];
        nameList = list.toArray(nameList);
        DemandDO demandDO =planDao.searchUserLEmail(nameList);
        if (JudgeUtils.isNotNull(demandDO)){
            mailInfo.setCcs(demandDO.getMonRemark().split(";"));
        }
        StringBuffer sb = new StringBuffer();
        sb.append("<table border='1' style='border-collapse: collapse;background-color: white; white-space: nowrap;'>");
        sb.append("<tr><td colspan='6' style='text-align: center;font-weight: bold;'>DBA操作包上传通知</td></tr>");
        sb.append("<tr><td style='font-weight: bold;'>预投产编号</td><td>" + bean.getPreNumber() + "</td><td style='font-weight: bold;'>需求名称</td><td>" + bean.getPreNeed() + "</td></tr>");
        sb.append("<tr><td style='font-weight: bold;'>申请部门</td><td>" + bean.getApplicationDept() + "</td><td style='font-weight: bold;'>计划预投产日期</td><td>" + bean.getPreDate() + "</td></tr>");
        sb.append("<tr><td style='font-weight: bold;'>预投产申请人</td><td>" + bean.getPreApplicant() + "</td><td style='font-weight: bold;'>申请人联系方式</td><td>" + bean.getApplicantTel() + "</td></tr>");
        sb.append("<tr><td style='font-weight: bold;'>开发负责人</td><td>" + bean.getDevelopmentLeader() + "</td><td style='font-weight: bold;'>产品经理</td><td>" + bean.getPreManager() + "</td></tr>");
        sb.append("<tr><td style='font-weight: bold;'>验证人</td><td>" + bean.getIdentifier() + "</td><td style='font-weight: bold;'>验证人联系方式</td><td>" + bean.getIdentifierTel() + "</td></tr>");
        sb.append("<tr><td style='font-weight: bold;'>验证复核人</td><td>" + bean.getProChecker() + "</td><td style='font-weight: bold;'>金科负责人邮箱</td><td>" + bean.getMailRecipient() + "</td></tr>");
        sb.append("<tr><td style='font-weight: bold;'>是否有DBA操作</td><td>" + bean.getIsDbaOperation() + "</td><td style='font-weight: bold;'>DBA操作是否完成</td><td>" + bean.getIsDbaOperationComplete() + "</td></tr>");
        sb.append("<tr><td style='font-weight: bold;'>备注</td><td colspan='5'>" + bean.getRemark() + "</td></tr></table>");
        mailInfo.setContent(bean.getPreNeed() + "-" + bean.getPreNumber() + "的DBA操作包已上传:<br/>&nbsp;&nbsp;烦请各位领导查看，并请DBA及时取包、部署，并及时更新研发管理系统状态！<br/>"+sb.toString());
        mailInfo.setSubject("【DBA操作包上传通知】-" + bean.getPreNeed() + "-" + bean.getPreNumber() + "-" + bean.getPreApplicant());

        Vector<File> filesv = new Vector<File>() ;
        /**
         * 附件
         */
        //获取邮件附件
        File motherFile=null;
        //归类文件，创建编号文件夹
        if(LemonUtils.getEnv().equals(Env.SIT)) {
            motherFile = new File("/home/devms/temp/preproduction/propkg/dba/" + bean.getPreNumber());
        }
        else if(LemonUtils.getEnv().equals(Env.DEV)) {
            motherFile = new File("/home/devadm/temp/preproduction/propkg/dba/" + bean.getPreNumber());
        }else {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("当前配置环境路径有误，请尽快联系管理员!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        File[] childFile=motherFile.listFiles();
        if(childFile!=null){
            for(File file1:childFile){
                filesv.add(file1) ;
            }
        }
        //添加附件
        if(filesv!=null && filesv.size()!=0){
            mailInfo.setFile(filesv);
        }
        // 这个类主要来发送邮件
        sendMailService.sendMail(mailInfo);
        //更新预投产状态 ，当当前状态为预投产提出时
        if(bean.getPreStatus().equals("预投产提出")){
            if(bean.getIsDbaOperation().equals("是")){
                //如果需要DBA操作，则再判断DAB操作包是否已经上传
                if(JudgeUtils.isNotEmpty(bean.getProPkgName()) ){
                    bean.setPreStatus("预投产待部署");
                    iPreproductionExtDao.updatePreSts(bean);
                    //生成流水记录
                    ScheduleDO scheduleBean =new ScheduleDO(currentUser);
                    // 记录操作
                    scheduleBean.setProNumber(bean.getPreNumber());
                    scheduleBean.setOperationType("预投产包上传");
                    scheduleBean.setPreOperation("预投产提出");
                    scheduleBean.setOperationReason("预投产包上传完成");
                    scheduleBean.setAfterOperation("预投产待部署");
                    operationProductionDao.insertSchedule(scheduleBean);
                    // 邮件通知DBA和版本组更新预投产 ，备注DBA先操作ddlSQL
                    MultiMailSenderInfo mailInfo1 = new MultiMailSenderInfo();
                    mailInfo1.setMailServerHost("smtp.qiye.163.com");
                    mailInfo1.setMailServerPort("25");
                    mailInfo1.setValidate(true);
                    mailInfo1.setUsername(Constant.EMAIL_NAME);
                    mailInfo1.setPassword(Constant.EMAIL_PSWD);
                    mailInfo1.setFromAddress(Constant.EMAIL_NAME);
                    // 接收人 版本组、DBA
                    // 获取DBA邮箱组
                    MailGroupDO mp1 = operationProductionDao.findMailGroupBeanDetail("10");
                    if(LemonUtils.getEnv().equals(Env.SIT)) {
                        mailInfo1.setReceivers(("version_it@hisuntech.com;"+ mp.getMailUser()).split(";"));
                    }
                    else if(LemonUtils.getEnv().equals(Env.DEV)) {
                        mailInfo1.setReceivers(("tu_yi@hisuntech.com;").split(";"));
                    }
                    // 抄送人  申请人 开发负责人
                    // 获取申请人邮箱 开发负责人邮箱
                    List<String> list1 = new ArrayList<>();
                    list1.add(bean.getPreApplicant());
                    list1.add(bean.getDevelopmentLeader());
                    String [] nameList1 = new String[list.size()];
                    nameList1 = list1.toArray(nameList1);
                    DemandDO demandDO1 =planDao.searchUserLEmail(nameList1);
                    if (JudgeUtils.isNotNull(demandDO1)){
                        mailInfo1.setCcs(demandDO1.getMonRemark().split(";"));
                    }
                    mailInfo1.setContent(bean.getPreNeed() + "-" + bean.getPreNumber() + "的预投产包均已上传:<br/>&nbsp;&nbsp;请DBA和版本组及时取包、部署，（先执行DBA操作包，后执行版本组操作包）并及时更新研发管理系统状态！<br/>"+sb.toString());
                    mailInfo1.setSubject("【预投产待部署通知】-" + bean.getPreNeed() + "-" + bean.getPreNumber() + "-" + bean.getPreApplicant());
                    // 这个类主要来发送邮件
                    sendMailService.sendMail(mailInfo1);
                }
            }
        }

    }
    //版本组投产包下载
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void pkgDownload(HttpServletRequest request, HttpServletResponse response, String proNumber){
        response.reset();
        try (OutputStream output = response.getOutputStream();
             BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output)) {
            //依据环境配置路径
            String path="";
            if(LemonUtils.getEnv().equals(Env.SIT)) {
                path= "/home/devms/temp/preproduction/propkg/bbz/";
            }
            else if(LemonUtils.getEnv().equals(Env.DEV)) {
                path= "/home/devadm/temp/preproduction/propkg/bbz/";
            }else {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("当前配置环境路径有误，请尽快联系管理员!");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }

            File fileDir = new File(path + proNumber);
            File[] pkgFile=fileDir.listFiles();
            File fileSend=null;
            if(pkgFile!=null&&pkgFile.length>0){
                fileSend = pkgFile[0];
            }
            response.setHeader("Content-Disposition", "attachment; filename="  + new String(fileSend.getName().getBytes(Constant.CHARSET_GB2312), Constant.CHARSET_ISO8859));
            response.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS, CONTENT_DISPOSITION);
            //告诉浏览器允许所有的域访问
            //注意 * 不能满足带有cookie的访问,Origin 必须是全匹配
            //resp.addHeader("Access-Control-Allow-Origin", "*");
            //解决办法通过获取Origin请求头来动态设置
            String origin = request.getHeader("Origin");
            if (StringUtils.isNotBlank(origin)) {
                response.addHeader("Access-Control-Allow-Origin", origin);
            }
            //允许带有cookie访问
            response.addHeader("Access-Control-Allow-Credentials", "true");
            //告诉浏览器允许跨域访问的方法
            response.addHeader("Access-Control-Allow-Methods", "*");
            //告诉浏览器允许带有Content-Type,header1,header2头的请求访问
            //resp.addHeader("Access-Control-Allow-Headers", "Content-Type,header1,header2");
            //设置支持所有的自定义请求头
            String headers = request.getHeader("Access-Control-Request-Headers");
            if (StringUtils.isNotBlank(headers)) {
                response.addHeader("Access-Control-Allow-Headers", headers);
            }
            //告诉浏览器缓存OPTIONS预检请求1小时,避免非简单请求每次发送预检请求,提升性能
            response.addHeader("Access-Control-Max-Age", "3600");
            response.setContentType("application/octet-stream; charset=utf-8");
            output.write(org.apache.commons.io.FileUtils.readFileToByteArray(fileSend));
            bufferedOutPut.flush();
        } catch (Exception e) {
            e.printStackTrace();
            BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
        }
    }
    //DBA投产包下载
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void dbaDownload(HttpServletRequest request, HttpServletResponse response, String proNumber){
        response.reset();
        try (OutputStream output = response.getOutputStream();
             BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output)) {
            //依据环境配置路径
            String path="";
            if(LemonUtils.getEnv().equals(Env.SIT)) {
                path= "/home/devms/temp/preproduction/propkg/dba/";
            }
            else if(LemonUtils.getEnv().equals(Env.DEV)) {
                path= "/home/devadm/temp/preproduction/propkg/dba/";
            }else {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("当前配置环境路径有误，请尽快联系管理员!");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }

            File fileDir = new File(path + proNumber);
            File[] pkgFile=fileDir.listFiles();
            File fileSend=null;
            if(pkgFile!=null&&pkgFile.length>0){
                fileSend = pkgFile[0];
            }
            response.setHeader("Content-Disposition", "attachment; filename="  + new String(fileSend.getName().getBytes(Constant.CHARSET_GB2312), Constant.CHARSET_ISO8859));
            response.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS, CONTENT_DISPOSITION);
            //告诉浏览器允许所有的域访问
            //注意 * 不能满足带有cookie的访问,Origin 必须是全匹配
            //resp.addHeader("Access-Control-Allow-Origin", "*");
            //解决办法通过获取Origin请求头来动态设置
            String origin = request.getHeader("Origin");
            if (StringUtils.isNotBlank(origin)) {
                response.addHeader("Access-Control-Allow-Origin", origin);
            }
            //允许带有cookie访问
            response.addHeader("Access-Control-Allow-Credentials", "true");
            //告诉浏览器允许跨域访问的方法
            response.addHeader("Access-Control-Allow-Methods", "*");
            //告诉浏览器允许带有Content-Type,header1,header2头的请求访问
            //resp.addHeader("Access-Control-Allow-Headers", "Content-Type,header1,header2");
            //设置支持所有的自定义请求头
            String headers = request.getHeader("Access-Control-Request-Headers");
            if (StringUtils.isNotBlank(headers)) {
                response.addHeader("Access-Control-Allow-Headers", headers);
            }
            //告诉浏览器缓存OPTIONS预检请求1小时,避免非简单请求每次发送预检请求,提升性能
            response.addHeader("Access-Control-Max-Age", "3600");
            response.setContentType("application/octet-stream; charset=utf-8");
            output.write(org.apache.commons.io.FileUtils.readFileToByteArray(fileSend));
            bufferedOutPut.flush();
        } catch (Exception e) {
            e.printStackTrace();
            BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
        }
    }

    @Override
    public DemandBO verifyAndQueryTheProductionNumber(String proNumber) {
        //未投产,req前缀投产编号则查询该编号对应的需求计划
        DemandDO demandDO = new DemandDO();
        if(proNumber.startsWith("REQ")&& !proNumber.startsWith("REQS")) {
            //查询该是编号是否已经投产
            PreproductionBO productionBO = this.searchProdutionDetail(proNumber);
            if(productionBO!=null && !productionBO.getPreStatus().equals("预投产取消")&& !productionBO.getPreStatus().equals("预投产打回")&& !productionBO.getPreStatus().equals("预投产回退")){
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("该预投产编号已经预投产!");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            demandDO.setReqNo(proNumber);
            demandDO.setReqSts("20");
            List<DemandDO> demandDOList = demandDao.find(demandDO);
            if(demandDOList.isEmpty()){
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("该预投产编号未在需求计划中存在，或需求状态不为进行中，请确认后重新填写!");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }else {
                demandDO = demandDOList.get(0);
            }
        }
        DemandBO demandBO = BeanUtils.copyPropertiesReturnDest(new DemandBO(), demandDO);
        return demandBO;
    }
    // 重新投产查询数据
    @Override
    public PreproductionBO againProductionNumber(String proNumber) {
            PreproductionBO productionBO = this.searchProdutionDetail(proNumber);
            if(productionBO!=null && !productionBO.getPreStatus().equals("预投产取消")&& !productionBO.getPreStatus().equals("预投产打回")&& !productionBO.getPreStatus().equals("预投产回退")){
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("该预投产编号已经预投产!");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
        return productionBO;
    }
    public PreproductionBO searchProdutionDetail(String proNumber) {
        PreproductionBO productionBO=null;
        PreproductionDO productionBean = iPreproductionExtDao.get(proNumber);
        if(productionBean!=null) {
            productionBO= BeanUtils.copyPropertiesReturnDest(new PreproductionBO(), productionBean);
        }
        return productionBO;
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void updateState(AutomatedProductionCallbackReqBO productionCallbackBO){
        PreproductionDO  beanCheck = iPreproductionExtDao.get(productionCallbackBO.getProNumber());
        if (JudgeUtils.isNull(beanCheck)) {
            LOGGER.error("id为[{}]的记录不存在", productionCallbackBO.getProNumber());
            MsgEnum.CUSTOMSUCCESS.setMsgCd("464");
            MsgEnum.CUSTOMSUCCESS.setMsgInfo("需求编号异常，需求编号不存在");
            BusinessException.throwBusinessException(MsgEnum.CUSTOMSUCCESS);
        }
        //生成流水记录
        ScheduleDO scheduleBean =new ScheduleDO("自动预投产");
        SimpleDateFormat sdfmonth =new SimpleDateFormat("yyyy-MM");
        String month = sdfmonth.format(iPreproductionExtDao.get(productionCallbackBO.getProNumber()).getPreDate());
        String pro_status_after = "";
        String pro_status_before = "";
        boolean isSend = true;
        MailFlowConditionDO mfva = new MailFlowConditionDO();
        mfva.setEmployeeName(iPreproductionExtDao.get(productionCallbackBO.getProNumber()).getPreApplicant());
        MailFlowDO mfba = operationProductionDao.searchUserEmail(mfva);
        // 创建邮件信息，通知预投产申请人
        MultiMailSenderInfo mailInfo = new MultiMailSenderInfo();
        mailInfo.setMailServerHost("smtp.qiye.163.com");
        mailInfo.setMailServerPort("25");
        mailInfo.setValidate(true);
        mailInfo.setUsername(Constant.EMAIL_NAME);
        mailInfo.setPassword(Constant.EMAIL_PSWD);
        mailInfo.setFromAddress(Constant.EMAIL_NAME);

        String[] mailToAddress = mfba.getEmployeeEmail().split(";");
        mailInfo.setReceivers(mailToAddress);
        //如果状态为0，即部署成功
        if("0".equals(productionCallbackBO.getStatus())){
            pro_status_before = "预投产待部署";
            pro_status_after = "预投产部署待验证";
            scheduleBean.setOperationReason("预投产已部署");

            if(beanCheck.getProductionDeploymentResult().equals("已部署")){
                //return ajaxDoneError("当前投产预投产已部署，不可重复操作!");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("当前投产预投产已部署，不可重复操作!");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            PreproductionDO  bean=iPreproductionExtDao.get(productionCallbackBO.getProNumber());
            bean.setProductionDeploymentResult("已部署");
            bean.setPreStatus("预投产部署待验证");
            iPreproductionExtDao.updatePreSts(bean);
            mailInfo.setSubject("【" + pro_status_after + "通知】");
            mailInfo.setContent("你好:<br/>您的需求" + productionCallbackBO.getProNumber() + bean.getPreNeed() + "已经预投产部署成功，请及时验证并更新状态。");
            // 这个类主要来发送邮件
            isSend = MultiMailsender.sendMailtoMultiTest(mailInfo);
            if (!(isSend)) {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("【预投产部署待验证】邮件发送失败,请及时联系系统维护人员!");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
        }
//        else{
//            pro_status_before = "预投产提出";
//            pro_status_after = "预投产打回";
//            scheduleBean.setOperationReason("预投产部署失败");
//
//            PreproductionDO bean = iPreproductionExtDao.get(productionCallbackBO.getProNumber());
//            //更新状态
//            bean.setPreStatus("pro_status_after");
//            iPreproductionExtDao.updatePreSts(bean);
//
//            mailInfo.setSubject("【" + pro_status_after + "通知】");
//            mailInfo.setContent("你好:<br/>由于【预投产部署失败】，您的" + productionCallbackBO.getProNumber() + bean.getPreNeed() + ",中止预投产流程。如需重新预投产，请走重新投产流程。");
//            // 这个类主要来发送邮件
//            isSend = MultiMailsender.sendMailtoMultiTest(mailInfo);
//            if (!(isSend)) {
//                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
//                MsgEnum.ERROR_CUSTOM.setMsgInfo("【预投产部署失败】邮件发送,请及时联系系统维护人员!");
//                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
//            }
//        }
        scheduleBean.setPreOperation(pro_status_before);
        scheduleBean.setAfterOperation(pro_status_after);
        scheduleBean.setOperationType(pro_status_after);
        scheduleBean.setProNumber(productionCallbackBO.getProNumber());
        operationProductionDao.insertSchedule(scheduleBean);
    }

    @Override
    public void automatedProductionCallback(AutomatedProductionCallbackReqBO productionCallbackBO) {
            //判断环境，预投产
            if(productionCallbackBO.getEnv().equals("0")){
                this.updateState(productionCallbackBO);
                return;
            }
        //判断环境，生产投产
            else if(productionCallbackBO.getEnv().equals("1")){


                return;
             }else {
                MsgEnum.CUSTOMSUCCESS.setMsgCd("465");
                MsgEnum.CUSTOMSUCCESS.setMsgInfo("投产环境参数异常");
                BusinessException.throwBusinessException(MsgEnum.CUSTOMSUCCESS);
            }

    }
}
