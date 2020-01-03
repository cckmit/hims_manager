package com.cmpay.lemon.monitor.service.impl.preproduction;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.cmpay.lemon.common.Env;
import com.cmpay.lemon.common.exception.BusinessException;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.common.utils.JudgeUtils;
import com.cmpay.lemon.common.utils.StringUtils;
import com.cmpay.lemon.framework.page.PageInfo;
import com.cmpay.lemon.framework.security.SecurityUtils;
import com.cmpay.lemon.framework.utils.LemonUtils;
import com.cmpay.lemon.framework.utils.PageUtils;
import com.cmpay.lemon.monitor.bo.*;
import com.cmpay.lemon.monitor.dao.*;
import com.cmpay.lemon.monitor.entity.Constant;
import com.cmpay.lemon.monitor.entity.*;
import com.cmpay.lemon.monitor.entity.sendemail.*;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.SystemUserService;
import com.cmpay.lemon.monitor.service.demand.ReqTaskService;
import com.cmpay.lemon.monitor.service.preproduction.PreProductionService;
import com.cmpay.lemon.monitor.service.productTime.ProductTimeService;
import com.cmpay.lemon.monitor.service.production.OperationProductionService;
import com.cmpay.lemon.monitor.utils.*;
import com.cmpay.lemon.monitor.utils.wechatUtil.schedule.BoardcastScheduler;
import com.jcraft.jsch.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

/**
 * 预投产管理：查询及状态变更
 */
@Service
public class PreProductionServiceImpl implements PreProductionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PreProductionServiceImpl.class);
    @Autowired
    private IPreproductionExtDao iPreproductionExtDao;
    @Autowired
    private IOperationApplicationDao operationApplicationDao;

    @Autowired
    private IProductionPicDao productionPicDao;
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
    SystemUserService userService;

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
                () -> BeanConvertUtils.convertList(iPreproductionExtDao.find(preproductionDO), PreproductionBO.class));
        return pageInfo;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void update(PreproductionBO productionBO){
        PreproductionDO preproductionDO = new PreproductionDO();
        BeanConvertUtils.convert(preproductionDO, productionBO);
        iPreproductionExtDao.update(preproductionDO);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void add(PreproductionBO productionBO){
        PreproductionDO preproductionDO = new PreproductionDO();
        BeanConvertUtils.convert(preproductionDO, productionBO);
        preproductionDO.setPreStatus("预投产提出");
        //预投产验证结果
        preproductionDO.setProAdvanceResult("未通过");
        //预投产部署结果
        preproductionDO.setProductionDeploymentResult("未部署");
        System.err.println(preproductionDO);
        iPreproductionExtDao.insert(preproductionDO);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void updateAllProduction(HttpServletRequest request, HttpServletResponse response, String taskIdStr){
//        //获取登录用户名
//        String currentUser = userService.getFullname(SecurityUtils.getLoginName());
//        //生成流水记录
//        ScheduleDO scheduleBean =new ScheduleDO(currentUser);
//        String[] pro_number_list=taskIdStr.split("~");
//        if(pro_number_list[0].equals("1")){
//            //return ajaxDoneError("请填写进行此操作原因");
//            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
//            MsgEnum.ERROR_CUSTOM.setMsgInfo("请填写进行此操作原因");
//            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
//        }
//        if(pro_number_list[0].equals("ytc")){
//            if(pro_number_list.length==1){
//                //return ajaxDoneError("请选择投产进行操作!");
//                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
//                MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择投产进行操作");
//                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
//            }
//            for(int i=2;i<pro_number_list.length;i++){
//                ProductionDO beanCheck=operationProductionDao.findProductionBean(pro_number_list[i]);
//                if(!(beanCheck.getProStatus().equals("投产提出") || (beanCheck.getProStatus().equals("投产待部署") && beanCheck.getProType().equals("救火更新")))){
//                    //return ajaxDoneError("当前投产状态的投产信息不可修改!");
//                    MsgEnum.ERROR_CUSTOM.setMsgInfo("");
//                    MsgEnum.ERROR_CUSTOM.setMsgInfo("当前投产状态的投产信息不可修改");
//                    BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
//                }
//                if(beanCheck.getProductionDeploymentResult().equals("已部署")){
//                    //return ajaxDoneError("当前投产预投产已部署，不可重复操作!");
//                    MsgEnum.ERROR_CUSTOM.setMsgInfo("");
//                    MsgEnum.ERROR_CUSTOM.setMsgInfo("当前投产预投产已部署，不可重复操作!");
//                    BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
//                }
//                ProductionDO bean=operationProductionDao.findProductionBean(pro_number_list[i]);
//                bean.setProductionDeploymentResult("已部署");
//                operationProductionDao.updateProduction(bean);
//                ScheduleDO sBean=new ScheduleDO();
//                sBean.setPreOperation(bean.getProStatus());
//                ScheduleDO schedule=new ScheduleDO(bean.getProNumber(), currentUser, "预投产已部署", sBean.getPreOperation(), sBean.getPreOperation(), "预投产已提前部署");
//                operationProductionDao.insertSchedule(schedule);
//            }
//            return;
//        }
//        if(pro_number_list[0].equals("yztg")){
//            if(pro_number_list.length==1){
//                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
//                MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择投产进行操作");
//                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
//            }
//            for(int i=2;i<pro_number_list.length;i++){
//                ProductionDO beanCheck=operationProductionDao.findProductionBean(pro_number_list[i]);
//                if(!currentUser.equals(beanCheck.getProManager()) && !currentUser.equals(beanCheck.getProApplicant()) && !currentUser.equals(beanCheck.getDevelopmentLeader())){
//                    MsgEnum.ERROR_CUSTOM.setMsgInfo("");
//                    MsgEnum.ERROR_CUSTOM.setMsgInfo("只有负责该投产的产品经理,申请人以及开发负责人才能验证通过!");
//                    BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
//                }
//                if(beanCheck.getIsAdvanceProduction().equals("否")){
//                    MsgEnum.ERROR_CUSTOM.setMsgInfo("");
//                    MsgEnum.ERROR_CUSTOM.setMsgInfo("当前投产不做预投产验证,不可操作!");
//                    BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
//                }
//                if(beanCheck.getProAdvanceResult().equals("通过")){
//                    MsgEnum.ERROR_CUSTOM.setMsgInfo("");
//                    MsgEnum.ERROR_CUSTOM.setMsgInfo("当前预投产验证已通过,不可重复操作!");
//                    BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
//                }
//
//                ProductionDO bean=operationProductionDao.findProductionBean(pro_number_list[i]);
//                bean.setProAdvanceResult("通过");
//                operationProductionDao.updateProduction(bean);
//                ScheduleDO sBean=new ScheduleDO();
//                sBean.setPreOperation(bean.getProStatus());
//                ScheduleDO schedule=new ScheduleDO(bean.getProNumber(), currentUser, "预投产验证通过", sBean.getPreOperation(), sBean.getPreOperation(), "预投产验证已通过");
//                operationProductionDao.insertSchedule(schedule);
//                //是否预投产验证为“是”时，预投产验证结果为“通过”，需求当前阶段变更为“完成预投产”
//                if (bean.getIsAdvanceProduction().equals("是") && bean.getProAdvanceResult().equals("通过")) {
//                    DemandDO demand = reqTaskService.findById1(bean.getProNumber());
//                    if (!JudgeUtils.isNull(demand)) {
//                        demand.setPreCurPeriod("160");
//                        DemandBO demandBO =  new DemandBO();
//                        BeanUtils.copyPropertiesReturnDest(demandBO, demand);
//                        reqTaskService.update(demandBO);
//                    }
//                }
//            }
//            return ;//ajaxDoneSuccess("预投产验证通过");
//        }
//        String pro_status_after = "";
//        String pro_status_before = "";
//        if (pro_number_list[0].equals("dtc")) {
//            if (pro_number_list.length == 1) {
//                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
//                MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择投产进行操作");
//                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
//            }
//            pro_status_before = "投产提出";
//            pro_status_after = "投产待部署";
//            scheduleBean.setOperationReason("通过");
//        }
//        else if (pro_number_list[0].equals("dh")) {
//            pro_status_before = "投产提出";
//            pro_status_after = "投产打回";
//            if ((pro_number_list.length == 1) || (pro_number_list.length == 2)) {
//                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
//                MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择投产进行操作");
//                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
//            }
//            scheduleBean.setOperationReason(pro_number_list[1]);
//        }
//        else if (pro_number_list[0].equals("dyz")) {
//            if (pro_number_list.length == 1){
//                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
//                MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择投产进行操作");
//                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
//            }
//            pro_status_before = "投产待部署";
//            pro_status_after = "部署完成待验证";
//            scheduleBean.setOperationReason("通过");
//        } else if (pro_number_list[0].equals("qx")) {
//            pro_status_before = "投产待部署";
//            pro_status_after = "投产取消";
//            if ((pro_number_list.length == 1) || (pro_number_list.length == 2)){
//                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
//                MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择投产进行操作");
//                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
//            }
//            scheduleBean.setOperationReason(pro_number_list[1]);
//        } else if (pro_number_list[0].equals("yzwc")) {
//            if (pro_number_list.length == 1){
//                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
//                MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择投产进行操作");
//                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
//            }
//            pro_status_before = "部署完成待验证";
//            pro_status_after = "投产验证完成";
//            scheduleBean.setOperationReason("通过");
//        } else if (pro_number_list[0].equals("ht")) {
//            pro_status_before = "部署完成待验证";
//            pro_status_after = "投产回退";
//            if ((pro_number_list.length == 1) || (pro_number_list.length == 2)){
//                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
//                MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择投产进行操作");
//                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
//            }
//            scheduleBean.setOperationReason(pro_number_list[1]);
//        }
//        scheduleBean.setPreOperation(pro_status_before);
//        scheduleBean.setAfterOperation(pro_status_after);
//        scheduleBean.setOperationType(pro_status_after);
//        for (int i = 2; i < pro_number_list.length; ++i) {
//            String status = operationProductionDao.findProductionBean(pro_number_list[i]).getProStatus();
//            if (pro_status_after.equals("投产取消")) {
//                String applicant = operationProductionDao.findProductionBean(pro_number_list[i]).getProApplicant();
//                String pro_manager = operationProductionDao.findProductionBean(pro_number_list[i]).getProManager();
//                if ((!(currentUser.equals(applicant))) && (!(currentUser.equals(pro_manager)))) {
//                    MsgEnum.ERROR_CUSTOM.setMsgInfo("");
//                    MsgEnum.ERROR_CUSTOM.setMsgInfo("您不是投产申请人或者负责该投产的产品经理!");
//                    BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
//                }
//            }
//            if(!(pro_status_before.equals(status) || (pro_status_after.equals("投产打回") && (status.equals("投产待部署")) ) || (pro_status_after.equals("投产回退") && status.equals("投产验证完成")) ||(pro_status_after.equals("投产取消") && (status.equals("投产提出")|| status.equals("投产待部署"))))){
//                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
//                MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择符合当前操作类型的正确投产状态!");
//                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
//            }
//        }
//        for (int j = 2; j < pro_number_list.length; ++j) {
//            String status = operationProductionDao.findProductionBean(pro_number_list[j]).getProStatus();
//            SimpleDateFormat sdfmonth =new SimpleDateFormat("yyyy-MM");
//            String month = sdfmonth.format(operationProductionDao.findProductionBean(pro_number_list[j]).getProDate());
//            scheduleBean.setProNumber(pro_number_list[j]);
//            scheduleBean.setPreOperation(status);
//
//            boolean isSend = true;
//
//            if ((((pro_status_before.equals(status)) || ((pro_status_after.equals("投产打回")) && (status.equals("投产待部署"))) || ((pro_status_after.equals("投产回退")) && (status.equals("投产验证完成"))) || ((pro_status_after.equals("投产取消")) && (((status.equals("投产提出")) || (status.equals("投产待部署"))))))) && ((
//                    (pro_status_after.equals("投产打回")) || (pro_status_after.equals("投产回退")) || (pro_status_after.equals("投产取消")))))
//            {
//                MailFlowConditionDO mfva = new MailFlowConditionDO();
//                mfva.setEmployeeName(operationProductionDao.findProductionBean(pro_number_list[j]).getProApplicant());
//                MailFlowDO mfba = operationProductionDao.searchUserEmail(mfva);
//                ProductionDO bean = operationProductionDao.findProductionBean(pro_number_list[j]);
//                MailFlowDO bnb = new MailFlowDO("投产不合格结果反馈", "code_review@hisuntech.com", mfba.getEmployeeEmail(), "");
//
//                // 创建邮件信息
//                MultiMailSenderInfo mailInfo = new MultiMailSenderInfo();
//                mailInfo.setMailServerHost("smtp.qiye.163.com");
//                mailInfo.setMailServerPort("25");
//                mailInfo.setValidate(true);
//                mailInfo.setUsername(Constant.EMAIL_NAME);
//                mailInfo.setPassword(Constant.EMAIL_PSWD);
//                mailInfo.setFromAddress(Constant.EMAIL_NAME);
//
//                String[] mailToAddress = mfba.getEmployeeEmail().split(";");
//                mailInfo.setReceivers(mailToAddress);
//                String mess = null;
//                if (pro_status_after.equals("投产打回")) {
//                    mess = pro_status_after;
//                }
//                if (pro_status_after.equals("投产回退")) {
//                    mess = pro_status_after;
//                }
//                if (pro_status_after.equals("投产取消")) {
//                    mess = pro_status_after;
//                }
//
//                mailInfo.setSubject("【" + mess + "通知】");
//                mailInfo.setContent("你好:<br/>由于【" + pro_number_list[1] + "】，您的" + pro_number_list[j] + bean.getProNeed() + ",中止投产流程。");
//
//                // 这个类主要来发送邮件
//                isSend = MultiMailsender.sendMailtoMultiTest(mailInfo);
//
//                operationProductionDao.addMailFlow(bnb);
//            }
//
//            ProductionDO productionBean = operationProductionDao.findProductionBean(pro_number_list[j]);
//
//            if ((((!(productionBean.getProType().equals("正常投产"))) || (!(productionBean.getIsOperationProduction().equals("是"))))) && (productionBean.getProStatus().equals("部署完成待验证")))
//            {
//                MailFlowConditionDO mfva = new MailFlowConditionDO();
//                mfva.setEmployeeName(operationProductionDao.findProductionBean(pro_number_list[j]).getProApplicant());
//                MailFlowDO mfba = operationProductionDao.searchUserEmail(mfva);
//
//                MailFlowConditionDO mfwa = new MailFlowConditionDO();
//                mfwa.setEmployeeName(operationProductionDao.findProductionBean(pro_number_list[j]).getIdentifier());
//                MailFlowDO mfaa = operationProductionDao.searchUserEmail(mfwa);
//
//                List<ProductionDO> bean=new ArrayList<ProductionDO>();
//                bean.add(operationProductionDao.findExportExcelList(pro_number_list[j]));
//                File file = sendExportExcel_Result(bean);
//
//                MailFlowDO bnb = new MailFlowDO("投产部署完成待验证结果反馈", "code_review@hisuntech.com", mfba.getEmployeeEmail() + ";" + mfaa.getEmployeeEmail(), file.getName(), "");
//                // 创建邮件信息
//                MultiMailSenderInfo mailInfo = new MultiMailSenderInfo();
//                mailInfo.setMailServerHost("smtp.qiye.163.com");
//                mailInfo.setMailServerPort("25");
//                mailInfo.setValidate(true);
//                mailInfo.setUsername(Constant.EMAIL_NAME);
//                mailInfo.setPassword(Constant.EMAIL_PSWD);
//                mailInfo.setFromAddress(Constant.EMAIL_NAME);
//
//                Vector filesv = new Vector();
//                filesv.add(file);
//                mailInfo.setFile(filesv);
//
//                String[] mailToAddress = (mfba.getEmployeeEmail()+";"+mfaa.getEmployeeEmail()).split(";");
//                mailInfo.setReceivers(mailToAddress);
//                StringBuffer sb = new StringBuffer();
//                if (productionBean.getProType().equals("救火更新")) {
//                    mailInfo.setSubject("【救火更新部署完成待验证结果通知】-" + productionBean.getProNeed() + "-" + productionBean.getProNumber() + "-" + productionBean.getProApplicant());
//                    sb.append("<table border='1' style='border-collapse: collapse;background-color: white; white-space: nowrap;'>");
//                    sb.append("<tr><td colspan='6' style='text-align: center;font-weight: bold;'>救火更新结果反馈</td></tr>");
//                    sb.append("<tr><td style='font-weight: bold;'>更新标题</td><td colspan='5'>" + productionBean.getProNeed() + "</td></tr>");
//                    sb.append("<tr><td style='font-weight: bold;'>申请部门</td><td>" + productionBean.getApplicationDept() + "</td><td style='font-weight: bold;'>申请人</td><td>" + productionBean.getProApplicant() + "</td>");
//                    sb.append("<td style='font-weight: bold;'>联系方式</td><td>" + productionBean.getApplicantTel() + "</td></tr>");
//                    sb.append("<tr><td style='font-weight: bold;'>投产编号</td><td>" + productionBean.getProNumber() + "</td><td style='font-weight: bold;'>复核人</td><td>" + productionBean.getProChecker() + "</td>");
//                    sb.append("<td style='font-weight: bold;'>联系方式</td><td>" + productionBean.getCheckerTel() + "</td></tr>");
//                    sb.append("<tr><td style='font-weight: bold;'>验证测试类型</td><td>" + productionBean.getValidation() + "</td><td style='font-weight: bold;'>验证人</td><td>" + productionBean.getIdentifier() + "</td>");
//                    sb.append("<td style='font-weight: bold;'>联系方式</td><td>" + productionBean.getIdentifierTel() + "</td></tr>");
//                    sb.append("<tr><td style='font-weight: bold;'>不走正常投产原因</td><td colspan='5'>" + productionBean.getUrgentReasonPhrase() + "</td></tr>");
//                    sb.append("<tr><td style='font-weight: bold;'>当天不投产的影响</td><td colspan='5'>" + productionBean.getNotProductionImpact() + "</td></tr>");
//                    sb.append("<tr><td style='font-weight: bold;'>更新要求完成时间</td><td colspan='5'>" + productionBean.getCompletionUpdate() + "</td></tr>");
//                    sb.append("<tr><td colspan='6' style='font-weight: bold;'>如需提前至当日24点前更新，需补充填写以下内容：</td></tr>");
//                    sb.append("<tr><td style='font-weight: bold;'>提前实施原因</td><td colspan='5'>" + productionBean.getEarlyImplementation() + "</td></tr>");
//                    sb.append("<tr><td rowspan='2' style='font-weight: bold;' >是否影响客户使用</td><td  rowspan='2'>" + productionBean.getInfluenceUse() + "</td>");
//                    sb.append("<td style='font-weight: bold;' >如不影响客户使用，请简要描述原因</td><td colspan='3'>" + productionBean.getInfluenceUseReason() + "</td></tr>");
//                    sb.append("<tr><td style='font-weight: bold;' >如影响客户使用，描述具体影响范围</td><td colspan='3'>" + productionBean.getInfluenceUseInf() + "</td></tr>");
//                    sb.append(" <tr><td style='font-weight: bold;'>更新时间及预计操作时长</td><td colspan='5'>" + productionBean.getOperatingTime() + "</td></tr></table>");
//                }
//                if ((productionBean.getProType().equals("正常投产")) && (productionBean.getIsOperationProduction().equals("否")))
//                {
//                    mailInfo.setSubject("【正常投产(非投产日)部署完成待验证结果通知】-" + productionBean.getProNeed() + "-" + productionBean.getProNumber() + "-" + productionBean.getProApplicant());
//                    sb.append("<table border='1' style='border-collapse: collapse;background-color: white; white-space: nowrap;'>");
//                    sb.append("<tr><td colspan='6' style='text-align: center;font-weight: bold;'>正常投产(非投产日)结果反馈</td></tr>");
//                    sb.append("<tr><td style='font-weight: bold;'>需求名称及内容简述</td><td colspan='5'>" + productionBean.getProNeed() + "</td></tr>");
//                    sb.append("<tr><td style='font-weight: bold;'>申请部门</td><td>" + productionBean.getApplicationDept() + "</td><td style='font-weight: bold;'>申请人</td><td>" + productionBean.getProApplicant() + "</td>");
//                    sb.append("<td style='font-weight: bold;'>联系方式</td><td>" + productionBean.getApplicantTel() + "</td></tr>");
//                    sb.append("<tr><td style='font-weight: bold;'>投产编号</td><td>" + productionBean.getProNumber() + "</td><td style='font-weight: bold;'>复核人</td><td>" + productionBean.getProChecker() + "</td>");
//                    sb.append("<td style='font-weight: bold;'>联系方式</td><td>" + productionBean.getCheckerTel() + "</td></tr>");
//                    sb.append("<tr><td style='font-weight: bold;'>验证测试类型</td><td>" + productionBean.getValidation() + "</td><td style='font-weight: bold;'>验证人</td><td>" + productionBean.getIdentifier() + "</td>");
//                    sb.append("<td style='font-weight: bold;'>联系方式</td><td>" + productionBean.getIdentifierTel() + "</td></tr>");
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                    sb.append("<tr><td style='font-weight: bold;'>计划投产日期</td><td>" + sdf.format(productionBean.getProDate()) + "</td><td style='font-weight: bold;'>产品所属模块</td><td>" + productionBean.getProModule() + "</td>");
//                    sb.append("<td style='font-weight: bold;'>基地业务负责人</td><td>" + productionBean.getBusinessPrincipal() + "</td></tr>");
//                    sb.append("<tr><td style='font-weight: bold;'>产品经理</td><td>" + productionBean.getProManager() + "</td><td style='font-weight: bold;'>是否涉及证书</td><td>" + productionBean.getIsRefCerificate() + "</td>");
//                    sb.append("<td style='font-weight: bold;'>开发负责人</td><td>" + productionBean.getDevelopmentLeader() + "</td></tr>");
//                    sb.append("<tr><td style='font-weight: bold;'>审批人</td><td>" + productionBean.getApprover() + "</td><td style='font-weight: bold;'>是否需要运维监控</td><td>" + productionBean.getProOperation() + "</td>");
//                    if ((productionBean.getUpdateOperator() != null) && (!(productionBean.getUpdateOperator().equals("")))) {
//                        sb.append("<td style='font-weight: bold;'>版本更新操作人</td><td>" + productionBean.getUpdateOperator() + "</td></tr>");
//                    }
//                    else {
//                        sb.append("<td style='font-weight: bold;'>版本更新操作人</td><td>暂未录入</td></tr>");
//                    }
//                    if (productionBean.getIsAdvanceProduction().equals("否")) {
//                        sb.append("<tr><td style='font-weight: bold;'>不做预投产验证原因</td><td colspan='5'>" + productionBean.getNotAdvanceReason() + "</td></tr>");
//                    }
//                    sb.append("<tr><td style='font-weight: bold;'>不走正常投产原因</td><td colspan='5'>" + productionBean.getUnusualReasonPhrase() + "</td></tr>");
//                    sb.append("<tr><td style='font-weight: bold;'>备注 (影响范围,其它补充说明)</td><td colspan='5'>" + productionBean.getRemark() + "</td></tr></table>");
//                }
//
//                mailInfo.setContent("你好：<br/>&nbsp;&nbsp;本次投产部署完成，请知悉。谢谢！<br/>" + sb.toString());
//
//                isSend = MultiMailsender.sendMailtoMultiTest(mailInfo);
//
//                operationProductionDao.addMailFlow(bnb);
//                if ((file.isFile()) && (file.exists())) {
//                    file.delete();
//                }
//            }
//            if ((((!(productionBean.getProType().equals("正常投产"))) || (!(productionBean.getIsOperationProduction().equals("是"))))) && (productionBean.getProStatus().equals("投产验证完成")))
//            {
//                List bean = new ArrayList();
//                bean.add(operationProductionDao.findExportExcelList(pro_number_list[j]));
//                File file = sendExportExcel_Result( bean);
//
//                MailFlowDO bnb = new MailFlowDO("投产申请结果反馈", "code_review@hisuntech.com", productionBean.getMailRecipient(), file.getName(), "");
//
//                // 创建邮件信息
//                MultiMailSenderInfo mailInfo = new MultiMailSenderInfo();
//                mailInfo.setMailServerHost("smtp.qiye.163.com");
//                mailInfo.setMailServerPort("25");
//                mailInfo.setValidate(true);
//                mailInfo.setUsername(Constant.EMAIL_NAME);
//                mailInfo.setPassword(Constant.EMAIL_PSWD);
//                mailInfo.setFromAddress(Constant.EMAIL_NAME);
//
//                Vector filesv = new Vector();
//                filesv.add(file);
//                mailInfo.setFile(filesv);
//
//                String[] mailToAddress = productionBean.getMailRecipient().split(";");
//                //String[] mailToAddress = {"tu_yi@hisuntech.com","wu_lr@hisuntech.com","huangyan@hisuntech.com"};
//                mailInfo.setReceivers(mailToAddress);
//                mailInfo.setCcs(productionBean.getMailCopyPerson().split(";"));
//                StringBuffer sb = new StringBuffer();
//                if (productionBean.getProType().equals("救火更新")) {
//                    mailInfo.setSubject("【救火更新投产结果通报】-" + productionBean.getProNeed() + "-" + productionBean.getProNumber() + "-" + productionBean.getProApplicant());
//                    sb.append("<table border='1' style='border-collapse: collapse;background-color: white; white-space: nowrap;'>");
//                    sb.append("<tr><td colspan='6' style='text-align: center;font-weight: bold;'>救火更新结果反馈</td></tr>");
//                    sb.append("<tr><td style='font-weight: bold;'>更新标题</td><td colspan='5'>" + productionBean.getProNeed() + "</td></tr>");
//                    sb.append("<tr><td style='font-weight: bold;'>申请部门</td><td>" + productionBean.getApplicationDept() + "</td><td style='font-weight: bold;'>申请人</td><td>" + productionBean.getProApplicant() + "</td>");
//                    sb.append("<td style='font-weight: bold;'>联系方式</td><td>" + productionBean.getApplicantTel() + "</td></tr>");
//                    sb.append("<tr><td style='font-weight: bold;'>投产编号</td><td>" + productionBean.getProNumber() + "</td><td style='font-weight: bold;'>复核人</td><td>" + productionBean.getProChecker() + "</td>");
//                    sb.append("<td style='font-weight: bold;'>联系方式</td><td>" + productionBean.getCheckerTel() + "</td></tr>");
//                    sb.append("<tr><td style='font-weight: bold;'>验证测试类型</td><td>" + productionBean.getValidation() + "</td><td style='font-weight: bold;'>验证人</td><td>" + productionBean.getIdentifier() + "</td>");
//                    sb.append("<td style='font-weight: bold;'>联系方式</td><td>" + productionBean.getIdentifierTel() + "</td></tr>");
//                    sb.append("<tr><td style='font-weight: bold;'>不走正常投产原因</td><td colspan='5'>" + productionBean.getUrgentReasonPhrase() + "</td></tr>");
//                    sb.append("<tr><td style='font-weight: bold;'>当天不投产的影响</td><td colspan='5'>" + productionBean.getNotProductionImpact() + "</td></tr>");
//                    sb.append("<tr><td style='font-weight: bold;'>更新要求完成时间</td><td colspan='5'>" + productionBean.getCompletionUpdate() + "</td></tr>");
//                    sb.append("<tr><td colspan='6' style='font-weight: bold;'>如需提前至当日24点前更新，需补充填写以下内容：</td></tr>");
//                    sb.append("<tr><td style='font-weight: bold;'>提前实施原因</td><td colspan='5'>" + productionBean.getEarlyImplementation() + "</td></tr>");
//                    sb.append("<tr><td rowspan='2' style='font-weight: bold;' >是否影响客户使用</td><td  rowspan='2'>" + productionBean.getInfluenceUse() + "</td>");
//                    sb.append("<td style='font-weight: bold;' >如不影响客户使用，请简要描述原因</td><td colspan='3'>" + productionBean.getInfluenceUseReason() + "</td></tr>");
//                    sb.append("<tr><td style='font-weight: bold;' >如影响客户使用，描述具体影响范围</td><td colspan='3'>" + productionBean.getInfluenceUseInf() + "</td></tr>");
//                    sb.append(" <tr><td style='font-weight: bold;'>更新时间及预计操作时长</td><td colspan='5'>" + productionBean.getOperatingTime() + "</td></tr></table>");
//                }
//                if ((productionBean.getProType().equals("正常投产")) && (productionBean.getIsOperationProduction().equals("否")))
//                {
//                    mailInfo.setSubject("【正常投产(非投产日)投产结果通报】-" + productionBean.getProNeed() + "-" + productionBean.getProNumber() + "-" + productionBean.getProApplicant());
//                    sb.append("<table border='1' style='border-collapse: collapse;background-color: white; white-space: nowrap;'>");
//                    sb.append("<tr><td colspan='6' style='text-align: center;font-weight: bold;'>正常投产(非投产日)结果反馈</td></tr>");
//                    sb.append("<tr><td style='font-weight: bold;'>需求名称及内容简述</td><td colspan='5'>" + productionBean.getProNeed() + "</td></tr>");
//                    sb.append("<tr><td style='font-weight: bold;'>申请部门</td><td>" + productionBean.getApplicationDept() + "</td><td style='font-weight: bold;'>申请人</td><td>" + productionBean.getProApplicant() + "</td>");
//                    sb.append("<td style='font-weight: bold;'>联系方式</td><td>" + productionBean.getApplicantTel() + "</td></tr>");
//                    sb.append("<tr><td style='font-weight: bold;'>投产编号</td><td>" + productionBean.getProNumber() + "</td><td style='font-weight: bold;'>复核人</td><td>" + productionBean.getProChecker() + "</td>");
//                    sb.append("<td style='font-weight: bold;'>联系方式</td><td>" + productionBean.getCheckerTel() + "</td></tr>");
//                    sb.append("<tr><td style='font-weight: bold;'>验证测试类型</td><td>" + productionBean.getValidation() + "</td><td style='font-weight: bold;'>验证人</td><td>" + productionBean.getIdentifier() + "</td>");
//                    sb.append("<td style='font-weight: bold;'>联系方式</td><td>" + productionBean.getIdentifierTel() + "</td></tr>");
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                    sb.append("<tr><td style='font-weight: bold;'>计划投产日期</td><td>" + sdf.format(productionBean.getProDate()) + "</td><td style='font-weight: bold;'>产品所属模块</td><td>" + productionBean.getProModule() + "</td>");
//                    sb.append("<td style='font-weight: bold;'>基地业务负责人</td><td>" + productionBean.getBusinessPrincipal() + "</td></tr>");
//                    sb.append("<tr><td style='font-weight: bold;'>产品经理</td><td>" + productionBean.getProManager() + "</td><td style='font-weight: bold;'>是否涉及证书</td><td>" + productionBean.getIsRefCerificate() + "</td>");
//                    sb.append("<td style='font-weight: bold;'>开发负责人</td><td>" + productionBean.getDevelopmentLeader() + "</td></tr>");
//                    sb.append("<tr><td style='font-weight: bold;'>审批人</td><td>" + productionBean.getApprover() + "</td><td style='font-weight: bold;'>是否需要运维监控</td><td>" + productionBean.getProOperation() + "</td>");
//                    if ((productionBean.getUpdateOperator() != null) && (!(productionBean.getUpdateOperator().equals("")))) {
//                        sb.append("<td style='font-weight: bold;'>版本更新操作人</td><td>" + productionBean.getUpdateOperator() + "</td></tr>");
//                    }
//                    else {
//                        sb.append("<td style='font-weight: bold;'>版本更新操作人</td><td>暂未录入</td></tr>");
//                    }
//                    if (productionBean.getIsAdvanceProduction().equals("否")) {
//                        sb.append("<tr><td style='font-weight: bold;'>不做预投产验证原因</td><td colspan='5'>" + productionBean.getNotAdvanceReason() + "</td></tr>");
//                    }
//                    sb.append("<tr><td style='font-weight: bold;'>不走正常投产原因</td><td colspan='5'>" + productionBean.getUnusualReasonPhrase() + "</td></tr>");
//                    sb.append("<tr><td style='font-weight: bold;'>备注 (影响范围,其它补充说明)</td><td colspan='5'>" + productionBean.getRemark() + "</td></tr></table>");
//                }
//
//                mailInfo.setContent("你好：<br/>&nbsp;&nbsp;本次投产验证完成，请知悉。谢谢！<br/>" + sb.toString());
//
//                SimpleMailSender sms = new SimpleMailSender();
//                isSend = MultiMailsender.sendMailtoMultiTest(mailInfo);
//
//                operationProductionDao.addMailFlow(bnb);
//                if ((file.isFile()) && (file.exists())) {
//                    file.delete();
//                }
//            }
//            operationProductionDao.insertSchedule(scheduleBean);
//            operationProductionDao.updateProduction(new ProductionDO(pro_number_list[j], pro_status_after));
//            // 根据编号查询需求信息
//            DemandDO demanddo = new DemandDO();
//            demanddo.setReqNo(pro_number_list[j]);
//            List<DemandDO> demandBOList = demandDao.find(demanddo);
//            if(!demandBOList.isEmpty()){
//                for(int i=0;i<demandBOList.size();i++){
//                    // 投产月份  = 需求实施月份时 ，改变需求状态
//                    if(demandBOList.get(i).getReqImplMon().compareTo(month)==0){
//                        DemandDO demand = demandBOList.get(i);
//                        if (!JudgeUtils.isNull(demand)) {
//                            //投产状态为“投产待部署”时，需求当前阶段变更为“待投产”  16
//                            if (pro_status_after.equals("投产待部署") || (pro_status_after.equals("投产回退") && pro_status_before.equals("部署完成待验证")) ) {
//                                demand.setPreCurPeriod("170");
//                                demand.setReqSts("20");
//                                if(pro_status_after.equals("投产回退")){
//                                    // 登记需求变更明细表
//                                    DemandStateHistoryDO demandStateHistoryDO = new DemandStateHistoryDO();
//                                    demandStateHistoryDO.setReqInnerSeq(demand.getReqInnerSeq());
//                                    demandStateHistoryDO.setReqNo(demand.getReqNo());
//                                    demandStateHistoryDO.setOldReqSts(reqTaskService.reqStsCheck(demand.getReqSts()));
//                                    demandStateHistoryDO.setReqSts("提出");
//                                    demandStateHistoryDO.setRemarks("投产回退");
//                                    demandStateHistoryDO.setReqNm(demand.getReqNm());
//                                    //依据内部需求编号查唯一标识
//                                    String identificationByReqInnerSeq = demandChangeDetailsDao.getIdentificationByReqInnerSeq(demand.getReqInnerSeq());
//                                    if(identificationByReqInnerSeq==null){
//                                        identificationByReqInnerSeq=demand.getReqInnerSeq();
//                                    }
//                                    demandStateHistoryDO.setIdentification(identificationByReqInnerSeq);
//                                    //登记需求状态历史表
//                                    //获取当前操作员
//                                    demandStateHistoryDO.setCreatUser(userService.getFullname(SecurityUtils.getLoginName()));
//                                    demandStateHistoryDO.setCreatTime(LocalDateTime.now());
//                                    demandStateHistoryDao.insert(demandStateHistoryDO);
//                                }
//
//                                DemandBO demandBO =  new DemandBO();
//                                BeanUtils.copyPropertiesReturnDest(demandBO, demand);
//                                reqTaskService.update(demandBO);
//                            }
//                            if (pro_status_after.equals("部署完成待验证")) {
//                                //投产状态为“投产验证完成”时，需求当前阶段为“已投产”  17
//                                demand.setPreCurPeriod("180");
//                                demand.setReqSts("50");
//                                // 登记需求变更明细表
//                                DemandStateHistoryDO demandStateHistoryDO = new DemandStateHistoryDO();
//                                demandStateHistoryDO.setReqInnerSeq(demand.getReqInnerSeq());
//                                demandStateHistoryDO.setReqNo(demand.getReqNo());
//                                demandStateHistoryDO.setOldReqSts(reqTaskService.reqStsCheck(demand.getReqSts()));
//                                demandStateHistoryDO.setReqSts("已完成");
//                                demandStateHistoryDO.setRemarks("部署完成待验证");
//                                demandStateHistoryDO.setReqNm(demand.getReqNm());
//                                //依据内部需求编号查唯一标识
//                                String identificationByReqInnerSeq = demandChangeDetailsDao.getIdentificationByReqInnerSeq(demand.getReqInnerSeq());
//                                if(identificationByReqInnerSeq==null){
//                                    identificationByReqInnerSeq=demand.getReqInnerSeq();
//                                }
//                                demandStateHistoryDO.setIdentification(identificationByReqInnerSeq);
//                                //登记需求状态历史表
//                                //获取当前操作员
//                                demandStateHistoryDO.setCreatUser(userService.getFullname(SecurityUtils.getLoginName()));
//                                demandStateHistoryDO.setCreatTime(LocalDateTime.now());
//                                demandStateHistoryDao.insert(demandStateHistoryDO);
//
//                                DemandBO demandBO =  new DemandBO();
//                                BeanUtils.copyPropertiesReturnDest(demandBO, demand);
//                                reqTaskService.update(demandBO);
//                            }
//                        }
//                    }
//                    // 投产月份  < 需求实施月份时 ，说明该月需求数据为异常数据，需求之前已经投产，故删除需求
//                    if(demandBOList.get(i).getReqImplMon().compareTo(month)>0){
//                        DemandDO demand = demandBOList.get(i);
//                        demandDao.delete(demand.getReqInnerSeq());
//                    }
//                }
//            }
//            if (!(isSend)) {
//                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
//                MsgEnum.ERROR_CUSTOM.setMsgInfo("批量操作成功,但您有邮件没有发送成功,请及时联系系统维护人员!");
//                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
//            }
//        }
        return ;//ajaxDoneSuccess("批量操作成功");
    }

    /**
     * 投产包上传
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

        //依据环境配置路径
        String path="";
        if(LemonUtils.getEnv().equals(Env.SIT)) {
            path= "/home/devms/temp/preproduction/propkg/";
        }
        else if(LemonUtils.getEnv().equals(Env.DEV)) {
            path= "/home/devadm/temp/preproduction/propkg/";
        }else {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("当前配置环境路径有误，请尽快联系管理员!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        File fileDir = new File(path + reqNumber);
        File filePath = new File(fileDir.getPath()+"/"+file.getOriginalFilename());
        if(fileDir.exists()){
            File[] oldFile = fileDir.listFiles();
            for(File o:oldFile) o.delete();
        }else{
            fileDir.mkdir();
        }
        try {
            file.transferTo(filePath);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("文件上传失败");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        } catch (IOException e) {
            e.printStackTrace();
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("文件上传失败");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        bean.setProPkgTime(new Date(new java.util.Date().getTime()));
        bean.setProPkgName(file.getOriginalFilename());
        iPreproductionExtDao.updatePropkg(bean);
    }
    //投产包下载
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void pkgDownload(HttpServletRequest request, HttpServletResponse response, String proNumber){
        response.reset();
        try (OutputStream output = response.getOutputStream();
             BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output)) {
            //依据环境配置路径
            String path="";
            if(LemonUtils.getEnv().equals(Env.SIT)) {
                path= "/home/devms/temp/preproduction/propkg/";
            }
            else if(LemonUtils.getEnv().equals(Env.DEV)) {
                path= "/home/devadm/temp/preproduction/propkg/";
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
}
