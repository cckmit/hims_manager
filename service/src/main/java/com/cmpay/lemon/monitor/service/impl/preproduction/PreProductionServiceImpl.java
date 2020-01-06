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
    private IUserRoleExtDao userRoleExtDao;
    @Autowired
    SystemUserService userService;
    @Autowired
    private IOperationProductionDao operationProductionDao;

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
        //获取登录用户名
        String currentUser = userService.getFullname(SecurityUtils.getLoginName());
        preproductionDO.setPreStatus("预投产提出");
        //预投产验证结果
        preproductionDO.setProAdvanceResult("未通过");
        //预投产部署结果
        preproductionDO.setProductionDeploymentResult("未部署");

        ScheduleDO sBean=new ScheduleDO();
        sBean.setPreOperation(preproductionDO.getPreStatus());
        ScheduleDO schedule=new ScheduleDO(preproductionDO.getPreNumber(), currentUser, "预投产录入", sBean.getPreOperation(), sBean.getPreOperation(), "预投产录入");
        operationProductionDao.insertSchedule(schedule);

        iPreproductionExtDao.insert(preproductionDO);
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
    public void updateAllProduction(HttpServletRequest request, HttpServletResponse response, String taskIdStr){
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
        //预投产已部署
        if(pro_number_list[0].equals("ytc")){
            if(pro_number_list.length==1){
                //return ajaxDoneError("请选择投产进行操作!");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择投产进行操作");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            pro_status_before = "预投产提出";
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
            pro_status_before = "预投产提出";
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
            pro_status_before = "预投产验证完成";
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
            if(!(pro_status_before.equals(status)  || (pro_status_after.equals("预投产打回") && (status.equals("预投产提出")) ) || (pro_status_after.equals("预投产回退") && status.equals("预投产验证完成")) ||(pro_status_after.equals("预投产取消") && status.equals("预投产提出")))){
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


            boolean isSend = true;
            if ((((pro_status_before.equals(status))  || ((pro_status_after.equals("预投产回退")) && (status.equals("预投产验证完成"))) || ((pro_status_after.equals("预投产取消")) && ((status.equals("预投产提出") ))))) && ((
                    (pro_status_after.equals("预投产打回")) || (pro_status_after.equals("预投产回退")) || (pro_status_after.equals("预投产取消")))))
            {
                MailFlowConditionDO mfva = new MailFlowConditionDO();
                mfva.setEmployeeName(iPreproductionExtDao.get(pro_number_list[j]).getPreApplicant());
                MailFlowDO mfba = operationProductionDao.searchUserEmail(mfva);
                PreproductionDO bean = iPreproductionExtDao.get(pro_number_list[j]);
                MailFlowDO bnb = new MailFlowDO("预投产不合格结果反馈", "code_review@hisuntech.com", mfba.getEmployeeEmail(), "");

                // 创建邮件信息
                MultiMailSenderInfo mailInfo = new MultiMailSenderInfo();
                mailInfo.setMailServerHost("smtp.qiye.163.com");
                mailInfo.setMailServerPort("25");
                mailInfo.setValidate(true);
                mailInfo.setUsername(Constant.EMAIL_NAME);
                mailInfo.setPassword(Constant.EMAIL_PSWD);
                mailInfo.setFromAddress(Constant.EMAIL_NAME);

                String[] mailToAddress = mfba.getEmployeeEmail().split(";");
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
                }

                mailInfo.setSubject("【" + mess + "通知】");
                mailInfo.setContent("你好:<br/>由于【" + pro_number_list[1] + "】，您的" + pro_number_list[j] + bean.getPreNeed() + ",中止预投产流程。");

                // 这个类主要来发送邮件
                isSend = MultiMailsender.sendMailtoMultiTest(mailInfo);

                operationProductionDao.addMailFlow(bnb);
            }

            if(pro_status_after.equals("预投产部署待验证")){
                PreproductionDO  beanCheck=iPreproductionExtDao.get(pro_number_list[j]);
                if(beanCheck.getProductionDeploymentResult().equals("已部署")){
                    //return ajaxDoneError("当前投产预投产已部署，不可重复操作!");
                    MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                    MsgEnum.ERROR_CUSTOM.setMsgInfo("当前投产预投产已部署，不可重复操作!");
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
                    MsgEnum.ERROR_CUSTOM.setMsgInfo("当前预投产验证已通过,不可重复操作!");
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
                //预投产验证结果为“通过”，需求当前阶段变更为“完成预投产”
                if (bean.getProAdvanceResult().equals("通过")) {
                    DemandDO demand = reqTaskService.findById1(bean.getPreNumber());
                    if (!JudgeUtils.isNull(demand)) {
                        demand.setPreCurPeriod("160");
                        DemandBO demandBO =  new DemandBO();
                        BeanUtils.copyPropertiesReturnDest(demandBO, demand);
                        reqTaskService.update(demandBO);
                    }
                }
            }
            if(pro_status_after.equals("预投产取消")||pro_status_after.equals("预投产打回")||pro_status_after.equals("预投产回退")){
                PreproductionDO  bean=iPreproductionExtDao.get(pro_number_list[j]);
                bean.setPreStatus(pro_status_after);
                iPreproductionExtDao.updatePreSts(bean);
            }

            operationProductionDao.insertSchedule(scheduleBean);

            if (!(isSend)) {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("批量操作成功,但您有邮件没有发送成功,请及时联系系统维护人员!");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
        }
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
