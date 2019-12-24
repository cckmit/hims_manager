package com.cmpay.lemon.monitor.service.impl.systemOperation;

import com.cmpay.lemon.common.Env;
import com.cmpay.lemon.common.exception.BusinessException;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.common.utils.StringUtils;
import com.cmpay.lemon.framework.page.PageInfo;
import com.cmpay.lemon.framework.security.SecurityUtils;
import com.cmpay.lemon.framework.utils.LemonUtils;
import com.cmpay.lemon.framework.utils.PageUtils;
import com.cmpay.lemon.monitor.bo.OperationApplicationBO;
import com.cmpay.lemon.monitor.bo.OperationApplicationRspBO;
import com.cmpay.lemon.monitor.dao.IOperationApplicationDao;
import com.cmpay.lemon.monitor.dao.IOperationProductionDao;
import com.cmpay.lemon.monitor.dao.IUserRoleExtDao;
import com.cmpay.lemon.monitor.entity.Constant;
import com.cmpay.lemon.monitor.entity.OperationApplicationDO;
import com.cmpay.lemon.monitor.entity.ScheduleDO;
import com.cmpay.lemon.monitor.entity.UserRoleDO;
import com.cmpay.lemon.monitor.entity.sendemail.*;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.SystemUserService;
import com.cmpay.lemon.monitor.service.production.OperationProductionService;
import com.cmpay.lemon.monitor.service.systemOperation.OperationApplicationService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import com.cmpay.lemon.monitor.utils.DateUtil;
import com.cmpay.lemon.monitor.utils.ExcelOperationDetailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

/**
 * 投产管理：查询及状态变更
 */
@Service
public class OperationApplicationServiceImpl implements OperationApplicationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OperationApplicationServiceImpl.class);
    //超级管理员
    private static final Long SUPERADMINISTRATOR =(long)10506;
    //团队主管
    private static final Long SUPERADMINISTRATOR1 =(long)5004;
    //运维部署组
    private static final Long SUPERADMINISTRATOR2 =(long)5005;
    @Autowired
    private IOperationProductionDao operationProductionDao;
    @Autowired
    OperationProductionService operationProductionService;
    @Autowired
    IOperationApplicationDao operationApplicationDao;

    public final static String RELATIVE_PATH_DEVADM = "/home/devadm/temp/sysopr/";

    public final static String RELATIVE_PATH_DEVMS = "/home/devms/temp/sysopr/";

    @Autowired
    private IUserRoleExtDao userRoleExtDao;
    @Autowired
    SystemUserService userService;
    /**
     * 系统操作分页查询
     * @param productionBO
     * @return
     */
    @Override
    public OperationApplicationRspBO find(OperationApplicationBO productionBO) {
        PageInfo<OperationApplicationBO> pageInfo = getPageInfo(productionBO);
        List<OperationApplicationBO> productionBOList = BeanConvertUtils.convertList(pageInfo.getList(), OperationApplicationBO.class);
        OperationApplicationRspBO productionRspBO = new OperationApplicationRspBO();
        productionRspBO.setProductionList(productionBOList);
        productionRspBO.setPageInfo(pageInfo);
        return productionRspBO;
    }

    private PageInfo<OperationApplicationBO> getPageInfo(OperationApplicationBO productionBO) {
        OperationApplicationDO productionDO = new OperationApplicationDO();
        BeanConvertUtils.convert(productionDO, productionBO);
        PageInfo<OperationApplicationBO> pageInfo = PageUtils.pageQueryWithCount(productionBO.getPageNum(), productionBO.getPageSize(),
                () -> BeanConvertUtils.convertList(operationApplicationDao.findPageBreakByCondition(productionDO), OperationApplicationBO.class));
        return pageInfo;
    }


    @Override
    public void systemOperationEntry(List<MultipartFile> files, OperationApplicationBO bean, HttpServletRequest request) {
        bean.setProposeDate(new Date(new java.util.Date().getTime()));
        bean.setOperNumber("SYS-OPR-" + DateUtil.date2String(new Date(new java.util.Date().getTime()), "yyyyMMddhhmmss"));
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
                        File fileNumber=null;
                        //归类文件，创建编号文件夹
                        if(LemonUtils.getEnv().equals(Env.SIT)) {
                             fileNumber = new File(RELATIVE_PATH_DEVMS + bean.getOperNumber());
                        }
                        else if(LemonUtils.getEnv().equals(Env.DEV)) {
                             fileNumber = new File(RELATIVE_PATH_DEVADM + bean.getOperNumber());
                        }else {
                            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                            MsgEnum.ERROR_CUSTOM.setMsgInfo("当前配置环境路径有误，请尽快联系管理员!");
                            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
                        }

                      //  File fileNumber = new File("D:\\home\\devadm\\temp");
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
        // 创建邮件信息
        MultiMailSenderInfo mailInfo = new MultiMailSenderInfo();
        mailInfo.setMailServerHost("smtp.qiye.163.com");
        mailInfo.setMailServerPort("25");
        mailInfo.setValidate(true);
        mailInfo.setUsername(Constant.EMAIL_NAME);
        mailInfo.setPassword(Constant.EMAIL_PSWD);
        mailInfo.setFromAddress(Constant.EMAIL_NAME);
        //添加申请人邮箱地址
        SendEmailConfig config = new SendEmailConfig();
        MailFlowConditionDO vo = new MailFlowConditionDO();
        vo.setEmployeeName(bean.getOperApplicant());
        MailFlowDO mflow = operationProductionService.searchUserEmail(vo);
        if(ObjectUtils.isEmpty(mflow)){
            MsgEnum.ERROR_CUSTOM.setMsgInfo("申请人输入有误，无法查找对应邮箱");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        String[] mailToAddressDemo = null;
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
        mailInfo.setReceivers(mManager.getEmployeeEmail().split(";"));
        mailInfo.setCcs(mflow.getEmployeeEmail().split(";"));
        mailInfo.setSubject("【" + bean.getSysOperType() + "审批】-" + bean.getOperRequestContent() + "-" + bean.getOperApplicant());
        mailInfo.setContent("你好！<br/>&nbsp;&nbsp;你的部门新增了【" + bean.getSysOperType() + "-" + bean.getOperRequestContent() + "】一类系统操作申请，烦请及时去线上审批，谢谢！<br/>");
        // 这个类主要来发送邮件
        boolean isSend = MultiMailsender.sendMailtoMultiTest(mailInfo);
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


    @Override
    public  void doOperationDownload(HttpServletRequest request, HttpServletResponse response, String taskIdStr)throws Exception{
        String[] pro_number_list=taskIdStr.split("~");
        if(pro_number_list.length == 0){
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("请先查出需要导出的投产操作明细记录!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        List<ScheduleDO> list=new ArrayList<ScheduleDO>();
        for(int i=0;i<pro_number_list.length;i++){
            list.add(operationProductionDao.findProExcelList(pro_number_list[i]));
        }
        exportOperationExcel(response,request, list);
    }
    @Override
    public  void doAllOperationDownload(HttpServletRequest request, HttpServletResponse response, OperationApplicationBO operationApplicationBO)throws Exception{
        OperationApplicationDO operationApplicationDO = new OperationApplicationDO();
        BeanConvertUtils.convert(operationApplicationDO, operationApplicationBO);
        List<OperationApplicationDO> li  = operationApplicationDao.findPageBreakByCondition(operationApplicationDO);
        List<ScheduleDO> list=new ArrayList<ScheduleDO>();
        for(int i=0;i<li.size();i++){
            list.add(operationProductionDao.findProExcelList(li.get(i).getOperNumber()));
        }
        exportOperationExcel(response,request, list);
    }
    public void exportOperationExcel(HttpServletResponse response,HttpServletRequest request,
                                     List<ScheduleDO> list) throws Exception {
        String fileName = "投产操作明细表" + DateUtil.date2String(new java.util.Date(), "yyyyMMddhhmmss") + ".xls";
        OutputStream os = null;
        response.reset();
        try {
            String path="";
            if(LemonUtils.getEnv().equals(Env.SIT)) {
                path= "/home/devms/temp/propkg/";
            }
            else if(LemonUtils.getEnv().equals(Env.DEV)) {
                path= "/home/devadm/temp/propkg/";
            }else {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("当前配置环境路径有误，请尽快联系管理员!");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            String filePath = path + fileName;
            ExcelOperationDetailUtil util = new ExcelOperationDetailUtil();
            String createFile = util.createCzlcExcel(filePath, list,null);
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
            response.setHeader(CONTENT_DISPOSITION, "attchement;filename=" + fileName);
            try {
                os = response.getOutputStream();
                os.write(org.apache.commons.io.FileUtils.readFileToByteArray(new File(filePath)));
                os.flush();
            } catch (IOException e) {
                throw e;
            } finally {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        throw e;
                    }
                }
            }
            // 删除文件
            new File(createFile).delete();
        } catch (UnsupportedEncodingException e) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("投产操作明细报表导出失败");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        } catch (Exception e) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("投产操作明细报表导出失败");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public  void updateAllOperationApplication(HttpServletRequest request, HttpServletResponse response, String taskIdStr){
        //获取登录用户名
        String currentUser =  userService.getFullname(SecurityUtils.getLoginName());
        //生成流水记录
        ScheduleDO scheduleBean =new ScheduleDO(currentUser);
        String[] pro_number_list=taskIdStr.split("~");
        if(pro_number_list[0].equals("1")){
            //return ajaxDoneError("请填写进行此操作原因");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("请填写进行此操作原因");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        if(pro_number_list.length==2){
            //return ajaxDoneError("请选择投产进行操作!");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择投产进行操作!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        String pro_status_after="";
        String pro_status_before="";
        if(pro_number_list[0].equals("dtc")){
            if(pro_number_list.length==1){
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择投产进行操作!");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            pro_status_before="提出";
            pro_status_after="审批通过待部署";
            scheduleBean.setOperationReason("通过");

        }else if(pro_number_list[0].equals("dh")){
            pro_status_before="提出";
            pro_status_after="审批不通过";
            scheduleBean.setOperationReason(pro_number_list[1]);
        }else if(pro_number_list[0].equals("qx")){
            pro_status_before="提出";
            pro_status_after="取消";
            scheduleBean.setOperationReason(pro_number_list[1]);
        }else if(pro_number_list[0].equals("czwc")){
            if(pro_number_list.length==1){
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择投产进行操作!");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            pro_status_before="审批通过待部署";
            pro_status_after="操作完成";
            scheduleBean.setOperationReason("通过");
        }else if(pro_number_list[0].equals("yzwc")){
            if(pro_number_list.length==1){
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择投产进行操作!");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            pro_status_before="操作完成";
            pro_status_after="验证完成";
            scheduleBean.setOperationReason("通过");
        }
        scheduleBean.setPreOperation(pro_status_before);
        scheduleBean.setAfterOperation(pro_status_after);
        scheduleBean.setOperationType(pro_status_after);
        for(int i=2;i<pro_number_list.length;i++){
            OperationApplicationDO bean=operationApplicationDao.findBaseOperationalApplicationInfo(pro_number_list[i]);
            String status=bean.getOperStatus();
            scheduleBean.setProNumber(pro_number_list[i]);
            scheduleBean.setPreOperation(status);
            if(pro_status_after.equals("取消")){
                String applicant=operationApplicationDao.findBaseOperationalApplicationInfo(pro_number_list[i]).getOperApplicant();
                if(!currentUser.equals(applicant) && !isDepartmentManager(SUPERADMINISTRATOR1)&&!isDepartmentManager(SUPERADMINISTRATOR2)&&!isDepartmentManager(SUPERADMINISTRATOR)){
                    MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                    MsgEnum.ERROR_CUSTOM.setMsgInfo("对不起，您不是投产申请人、团队主管、运维部署组或超级管理员!");
                    BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
                }
            }
            if(pro_status_after.equals("审批不通过") || pro_status_after.equals("审批通过待部署")){
                if(!isDepartmentManager(SUPERADMINISTRATOR1)&&!isDepartmentManager(SUPERADMINISTRATOR)){
                    MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                    MsgEnum.ERROR_CUSTOM.setMsgInfo("对不起,您不是团队主管或超级管理员!");
                    BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
                }
            }
            if(!(pro_status_before.equals(status) || pro_status_after.equals("验证完成") && (status.equals("操作完成")) ||(pro_status_after.equals("取消") && (status.equals("提出")|| status.equals("审批通过待部署"))) ||(pro_status_after.equals("审批不通过") && (status.equals("提出")|| status.equals("审批通过待部署"))))){
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择符合当前操作类型的正确投产状态！");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            // 已审批通过待部署的系统操作不能审批不通过
            if(pro_status_after.equals("审批不通过") && "审批通过待部署".equals(status)){
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择符合当前操作类型的正确投产状态！");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
        }
        for(int j=2;j<pro_number_list.length;j++){
            OperationApplicationDO bean=operationApplicationDao.findBaseOperationalApplicationInfo(pro_number_list[j]);
            String status=bean.getOperStatus();
            scheduleBean.setProNumber(pro_number_list[j]);
            scheduleBean.setPreOperation(status);

            operationApplicationDao.updateOperationalApplication(new OperationApplicationDO(pro_number_list[j],pro_status_after));
            operationProductionDao.insertSchedule(scheduleBean);
            if(pro_status_after.equals("审批通过待部署")){
                //发送审批邮件
                String operName=bean.getSysOperType();

                String receiverMail=bean.getMailRecipient();
                String copyToMail=null;
                SendEmailConfig config = new SendEmailConfig();
                if(bean.getSysOperType().equals("数据变更")){
                    copyToMail=bean.getMailCopyPerson();
                    if(bean.getIsRefSql().equals("是")){
                        //增加DBA
                        receiverMail=receiverMail+";"+config.getDbaMailTo();
                    }

                }else{
                    //追加部门经理抄送人
                    copyToMail=bean.getMailCopyPerson();
                    if(bean.getIsRefSql().equals("是")){
                        //武金艳、肖铧、董建敏、朱明华、增加DBA
                        receiverMail=receiverMail+";"+config.getSqlMailTo()+";"+config.getDbaMailTo();
                    }else{
                        //武金艳、肖铧、董建敏、朱明华
                        receiverMail=receiverMail+";"+config.getSqlMailTo();
                    }
                }

                //非七方审核追加附件
                Vector<File> filesv = new Vector<File>() ;
//     			if(!bean.getSys_oper_type().equals("数据变更")){
                /**
                 * 附件
                 */
                //获取邮件附件
                File motherFile=null;
                //归类文件，创建编号文件夹
                if(LemonUtils.getEnv().equals(Env.SIT)) {
                    motherFile = new File(RELATIVE_PATH_DEVMS + bean.getOperNumber());
                }
                else if(LemonUtils.getEnv().equals(Env.DEV)) {
                    motherFile = new File(RELATIVE_PATH_DEVADM + bean.getOperNumber());
                }else {
                    MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                    MsgEnum.ERROR_CUSTOM.setMsgInfo("当前配置环境路径有误，请尽快联系管理员!");
                    BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
                }
                File[] childFile=motherFile.listFiles();
                if(childFile!=null){
                    for(File file:childFile){
                        filesv.add(file) ;
                    }
                }
//     			}

                //记录邮箱信息
                MailFlowDO bnb=new MailFlowDO("【"+operName+"审核】",Constant.P_EMAIL_NAME, receiverMail,"");
                // 创建邮件信息
                MultiMailSenderInfo mailInfo = new MultiMailSenderInfo();
                mailInfo.setMailServerHost("smtp.qiye.163.com");
                mailInfo.setMailServerPort("25");
                mailInfo.setValidate(true);
                mailInfo.setUsername(Constant.EMAIL_NAME);
                mailInfo.setPassword(Constant.EMAIL_PSWD);
                mailInfo.setFromAddress(Constant.EMAIL_NAME);

                /**
                 * 收件人邮箱
                 */
                String receiverMail_1 = receiverMail.replace(',',';');
                String[] mailToAddressDemo=receiverMail_1.split(";");
                //收件人去重复
                List<String> result = new ArrayList<String>();
                boolean flag;
                for(int i=0;i<mailToAddressDemo.length;i++){
                    flag = false;
                    for(int k=0;k<result.size();k++){
                        if(mailToAddressDemo[i].equals(result.get(k))){
                            flag = true;
                            break;
                        }
                    }
                    if(!flag){
                        result.add(mailToAddressDemo[i]);
                    }
                }

                String[] mailToAddress =(String[]) result.toArray(new String[result.size()]);
                mailInfo.setReceivers(mailToAddress);
                if(copyToMail!=null){
                    mailInfo.setCcs(copyToMail.split(";"));
                }
                //非七方审核添加附件
                if(filesv!=null && filesv.size()!=0){
                    mailInfo.setFile(filesv);
                }

                StringBuffer sb=new StringBuffer();
                mailInfo.setSubject("【"+operName+"审核】-"+bean.getOperRequestContent()+"-"+bean.getOperApplicant());
                sb.append("<table border='1' style='border-collapse: collapse;background-color: white; white-space: nowrap;'>");
                sb.append("<tr><td colspan='6' style='text-align: center;font-weight: bold;'>"+operName+"申请表</td></tr> ");
                sb.append("<tr><td style='font-weight: bold;'>申请部门:</td><td>"+bean.getApplicationSector()+"</td>");
                sb.append("<td style='font-weight: bold;'>申请人:</td><td>"+bean.getOperApplicant()+"</td>");
                sb.append("<td style='font-weight: bold;'>联系方式:</td><td>"+bean.getApplicantTel()+"</td></tr>");
                sb.append("<tr><td style='font-weight: bold;'>操作类型:</td><td>"+bean.getOperType()+"</td>");
                sb.append("<td style='font-weight: bold;'>验证人:</td><td>"+bean.getIdentifier()+"</td>");
                sb.append("<td style='font-weight: bold;'>联系方式：</td><td>"+bean.getIdentifierTel()+"</td></tr>");
                sb.append("<tr><td style='font-weight: bold;'>验证测试类型：</td><td>"+bean.getValidationType()+"</td>");
                sb.append("<td style='font-weight: bold;'>验证说明：</td><td colspan='3'>"+bean.getValidationInstruction()+"</td></tr>");
                if (operName.equals("数据变更")) {
                    sb.append("<tr><td style='font-weight: bold;'>是否有回退方案：</td><td colspan='5'>" + bean.getIsBackWay() + "</td></tr>");
                }
                sb.append("<tr><td style='font-weight: bold;'>系统操作原因描述：</td><td colspan='5'>"+bean.getOperApplicationReason()+"</td></tr>");
                sb.append("<tr><td style='font-weight: bold;'>影响分析描述：</td><td colspan='5'>"+bean.getAnalysis()+"</td></tr>");
                sb.append("<tr><td style='font-weight: bold;'>更新要求完成时间说明：</td><td colspan='5'>"+bean.getCompletionUpdate()+"</td></tr>");
                sb.append("<tr><td style='font-weight: bold;'>备注：</td><td colspan='5'>"+bean.getRemark()+"</td></tr></table>");
                mailInfo.setContent("各位领导好！<br/>&nbsp;&nbsp;以下"+operName+"，烦请审批，谢谢！<br/>"+sb.toString());
                // 这个类主要来发送邮件
                boolean isSend = MultiMailsender.sendMailtoMultiTest(mailInfo);
                if(!isSend){
                    MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                    MsgEnum.ERROR_CUSTOM.setMsgInfo("审批邮件发送失败!");
                    BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
                }else{
                    operationProductionDao.addMailFlow(bnb);
                }

            }
            //操作完成通知申请人
            if(pro_status_after.equals("操作完成")){
                //发送审批邮件
                String operName=bean.getSysOperType();
                //添加申请人邮箱地址

                MailFlowConditionDO vo=new MailFlowConditionDO();
                vo.setEmployeeName(bean.getOperApplicant());
                MailFlowDO mflow=operationProductionDao.searchUserEmail(vo);

                // 创建邮件信息
                MultiMailSenderInfo mailInfo = new MultiMailSenderInfo();
                mailInfo.setMailServerHost("smtp.qiye.163.com");
                mailInfo.setMailServerPort("25");
                mailInfo.setValidate(true);
                mailInfo.setUsername(Constant.EMAIL_NAME);
                mailInfo.setPassword(Constant.EMAIL_PSWD);
                mailInfo.setFromAddress(Constant.EMAIL_NAME);

                //记录邮箱信息
                MailFlowDO bnb=new MailFlowDO("【"+operName+"结果通报】",Constant.P_EMAIL_NAME, mflow.getEmployeeEmail(),"");
                mailInfo.setReceivers(mflow.getEmployeeEmail().split(";"));
                StringBuffer sb=new StringBuffer();

                mailInfo.setSubject("【"+operName+"结果通报】-"+bean.getOperRequestContent()+"-"+bean.getOperApplicant());
                sb.append("<table border='1' style='border-collapse: collapse;background-color: white; white-space: nowrap;'>");
                sb.append("<tr><td colspan='6' style='text-align: center;font-weight: bold;'>"+operName+"结果反馈</td></tr> ");
                sb.append("<tr><td style='font-weight: bold;'>申请部门:</td><td>"+bean.getApplicationSector()+"</td>");
                sb.append("<td style='font-weight: bold;'>申请人:</td><td>"+bean.getOperApplicant()+"</td>");
                sb.append("<td style='font-weight: bold;'>联系方式:</td><td>"+bean.getApplicantTel()+"</td></tr>");
                sb.append("<tr><td style='font-weight: bold;'>操作类型:</td><td>"+bean.getOperType()+"</td>");
                sb.append("<td style='font-weight: bold;'>验证人:</td><td>"+bean.getIdentifier()+"</td>");
                sb.append("<td style='font-weight: bold;'>联系方式：</td><td>"+bean.getIdentifierTel()+"</td></tr>");
                sb.append("<tr><td style='font-weight: bold;'>验证测试类型：</td><td>"+bean.getValidationType()+"</td>");
                sb.append("<td style='font-weight: bold;'>验证说明：</td><td>"+bean.getValidationInstruction()+"</td></tr>");
                sb.append("<tr><td style='font-weight: bold;'>开发负责人：</td><td>"+bean.getDevelopmentLeader()+"</td>");
                sb.append("<tr><td style='font-weight: bold;'>系统操作原因描述：</td><td colspan='5'>"+bean.getOperApplicationReason()+"</td></tr>");
                sb.append("<tr><td style='font-weight: bold;'>影响分析描述：</td><td colspan='5'>"+bean.getAnalysis()+"</td></tr>");
                sb.append("<tr><td style='font-weight: bold;'>更新要求完成时间说明：</td><td colspan='5'>"+bean.getCompletionUpdate()+"</td></tr>");
                sb.append("<tr><td style='font-weight: bold;'>备注：</td><td colspan='5'>"+bean.getRemark()+"</td></tr></table>");
                mailInfo.setContent("你好：<br/>&nbsp;&nbsp;本次系统操作实施完成，请知悉。谢谢！<br/>"+sb.toString());
                // 这个类主要来发送邮件
                boolean isSend = MultiMailsender.sendMailtoMultiTest(mailInfo);
                if(!isSend){
                    MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                    MsgEnum.ERROR_CUSTOM.setMsgInfo("邮件通知申请人发送失败!");
                    BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
                }else{
                    operationProductionDao.addMailFlow(bnb);
                }

            }


            //异常状态邮件发送

            if(pro_status_before.equals(status) ||(pro_status_after.equals("取消") && (status.equals("提出")|| status.equals("审批通过待部署"))) ||(pro_status_after.equals("审批不通过") && (status.equals("提出")|| status.equals("审批通过待部署")))){
                if(pro_status_after.equals("取消") || pro_status_after.equals("审批不通过") ){
                    //发送申请人邮箱地址
                    MailFlowConditionDO mfva=new MailFlowConditionDO();
                    mfva.setEmployeeName(operationApplicationDao.findBaseOperationalApplicationInfo(pro_number_list[j]).getOperApplicant());
                    MailFlowDO mfba=operationProductionDao.searchUserEmail(mfva);
                    //记录邮箱信息
                    String mess=null;
                    if(pro_status_after.equals("取消")){
                        mess=pro_status_after;
                    }
                    if(pro_status_after.equals("审批不通过")){
                        mess=pro_status_after;
                    }

                    MailFlowDO bnb=new MailFlowDO("【系统操作("+mess+")结果反馈】",Constant.P_EMAIL_NAME, mfba.getEmployeeEmail(),"");
                    // 创建邮件信息
                    MultiMailSenderInfo mailInfo = new MultiMailSenderInfo();
                    mailInfo.setMailServerHost("smtp.qiye.163.com");
                    mailInfo.setMailServerPort("25");
                    mailInfo.setValidate(true);
                    mailInfo.setUsername(Constant.EMAIL_NAME);
                    mailInfo.setPassword(Constant.EMAIL_PSWD);
                    mailInfo.setFromAddress(Constant.EMAIL_NAME);
                    /**
                     * 收件人邮箱
                     */
                    String[] mailToAddress = mfba.getEmployeeEmail().split(";");
                    //String[] mailToAddress = {"tu_yi@hisuntech.com","wu_lr@hisuntech.com","huangyan@hisuntech.com"};
                    mailInfo.setReceivers(mailToAddress);
                    mailInfo.setSubject("【系统操作("+mess+")结果反馈】");
                    mailInfo.setContent("你好:<br/>由于【"+pro_number_list[1]+"】,您的"
                            +operationApplicationDao.findBaseOperationalApplicationInfo(pro_number_list[j]).getOperRequestContent()+",此系统操作中止审批流程");
                    // 这个类主要来发送邮件
                    boolean isSend = MultiMailsender.sendMailtoMultiTest(mailInfo);
                    if(isSend){
                        operationProductionDao.addMailFlow(bnb);
                    }
                }

            }
        }


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
    public void systemOperationUpdate(List<MultipartFile> files, OperationApplicationBO bean, HttpServletRequest request) {
        //获取登录用户名
        String currentUser = userService.getFullname(SecurityUtils.getLoginName());
        //处理多文件上传
        String filePath = null;
        File fl = null;
        String[] attachFileNames = null;
        if (bean.getOperStatus().equals("提出")) {
            // 判断文件是否为空
            if (files != null && !files.equals("")) {
                for (MultipartFile file : files) {
                    if (!file.isEmpty()) {
                        try {
                            //归类文件，创建编号文件夹
                            File fileNumber=null;
                            //归类文件，创建编号文件夹
                            if(LemonUtils.getEnv().equals(Env.SIT)) {
                                fileNumber = new File(RELATIVE_PATH_DEVMS + bean.getOperNumber());
                            }
                            else if(LemonUtils.getEnv().equals(Env.DEV)) {
                                fileNumber = new File(RELATIVE_PATH_DEVADM + bean.getOperNumber());
                            }else {
                                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                                MsgEnum.ERROR_CUSTOM.setMsgInfo("当前配置环境路径有误，请尽快联系管理员!");
                                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
                            }

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
                ScheduleDO schedule = new ScheduleDO(bean.getOperNumber(), currentUser, "文件上传", bean.getOperStatus(), bean.getOperStatus(), "系统操作审批信息更新");
                operationProductionDao.insertSchedule(schedule);
                return;
            } else {
                if (!(bean.getOperApplicant().equals(currentUser))) {
                    MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                    MsgEnum.ERROR_CUSTOM.setMsgInfo("非当前投产申请人不得更改信息！");
                    BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
                }
                OperationApplicationDO operationApplicationDO = BeanUtils.copyPropertiesReturnDest(new OperationApplicationDO(), bean);
                operationApplicationDao.updateAllOperationalApplication(operationApplicationDO);
                ScheduleDO schedule = new ScheduleDO(bean.getOperNumber(), currentUser, "修改", bean.getOperStatus(), bean.getOperStatus(), "系统操作审批信息更新");
                operationProductionDao.insertSchedule(schedule);
            }
        }
        if(bean.getOperStatus().equals("审批通过待部署")){
            // 判断文件是否为空
            if (files != null && !files.equals("")) {
                for (MultipartFile file : files) {
                    if (!file.isEmpty()) {
                        try {
                            //归类文件，创建编号文件夹
                            File fileNumber=null;
                            //归类文件，创建编号文件夹
                            if(LemonUtils.getEnv().equals(Env.SIT)) {
                                fileNumber = new File(RELATIVE_PATH_DEVMS + bean.getOperNumber());
                            }
                            else if(LemonUtils.getEnv().equals(Env.DEV)) {
                                fileNumber = new File(RELATIVE_PATH_DEVADM + bean.getOperNumber());
                            }else {
                                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                                MsgEnum.ERROR_CUSTOM.setMsgInfo("当前配置环境路径有误，请尽快联系管理员!");
                                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
                            }

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
                ScheduleDO schedule=new ScheduleDO(bean.getOperNumber(), currentUser, "文件上传", bean.getOperStatus(),bean.getOperStatus(), "系统操作审批信息更新");
                operationProductionDao.insertSchedule(schedule);
                return ;

            }else{
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("当前系统操作信息不可修改！");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }

        }
        if(!bean.getOperStatus().equals("提出") && !bean.getOperStatus().equals("审批通过待部署")){
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("当前系统操作信息不可修改！");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }

        return ;
    }

    //投产包下载
    @Override
    public void pkgDownload(HttpServletRequest request, HttpServletResponse response, String proNumber){
        response.reset();
        try (OutputStream output = response.getOutputStream();
             BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output)) {
            // File fileDir=new File(request.getSession().getServletContext().getRealPath("/") + RELATIVE_PATH +proNumber);
            //File fileDir = new File(RELATIVE_PATH + proNumber);
            File fileDir=null;
            //归类文件，创建编号文件夹
            if(LemonUtils.getEnv().equals(Env.SIT)) {
                fileDir = new File(RELATIVE_PATH_DEVMS + proNumber);
            }
            else if(LemonUtils.getEnv().equals(Env.DEV)) {
                fileDir = new File(RELATIVE_PATH_DEVADM + proNumber);
            }else {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("当前配置环境路径有误，请尽快联系管理员!");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            File[] pkgFile=fileDir.listFiles();
            if(pkgFile==null){
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("系统操作附件未上传！");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            //压缩包名称
            String zipPath="";
            if(LemonUtils.getEnv().equals(Env.SIT)) {
                zipPath= "/home/devms/temp/propkg/";
            }
            else if(LemonUtils.getEnv().equals(Env.DEV)) {
                zipPath= "/home/devadm/temp/propkg/";
            }else {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("当前配置环境路径有误，请尽快联系管理员!");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            String zipName =DateUtil.date2String(new java.util.Date(), "yyyyMMddHHmmss") + ".zip";
            //压缩文件
            File zip = new File(zipPath + zipName);
            ZipFiles(pkgFile, zip, true);
            response.setHeader(CONTENT_DISPOSITION, "attchement;filename=" + zipName);
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
            output.write(org.apache.commons.io.FileUtils.readFileToByteArray(zip));
            bufferedOutPut.flush();
            // 删除文件
            zip.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String ZipFiles(File[] srcfile, File zipfile, boolean flag) {
        try {
            byte[] buf = new byte[1024];
            FileOutputStream fileOutputStream = new FileOutputStream(zipfile);
            CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream, new CRC32());
            ZipOutputStream out = new ZipOutputStream(cos);

            for (int i = 0; i < srcfile.length; i++) {
                if (srcfile[i] != null) {
                    FileInputStream in = new FileInputStream(srcfile[i]);
                    if (flag) {
                        System.err.println(srcfile[i].getPath());
                        String path = srcfile[i].getPath().substring(srcfile[i].getPath().lastIndexOf("/") + 1);
                        out.putNextEntry(new ZipEntry( path));
                    } else {
                        out.putNextEntry(new ZipEntry(srcfile[i].getPath()));
                    }

                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    out.closeEntry();
                    in.close();
                }
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}
