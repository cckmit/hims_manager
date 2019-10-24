package com.cmpay.lemon.monitor.controller.production;


import com.cmpay.framework.data.request.GenericDTO;
import com.cmpay.framework.data.response.GenericRspDTO;
import com.cmpay.lemon.common.exception.BusinessException;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.framework.data.NoBody;
import com.cmpay.lemon.framework.security.SecurityUtils;
import com.cmpay.lemon.monitor.bo.DemandBO;
import com.cmpay.lemon.monitor.bo.MailGroupBO;
import com.cmpay.lemon.monitor.bo.ProductionBO;
import com.cmpay.lemon.monitor.bo.ProductionRspBO;
import com.cmpay.lemon.monitor.constant.MonitorConstants;
import com.cmpay.lemon.monitor.dto.ProductionConditionReqDTO;
import com.cmpay.lemon.monitor.dto.ProductionConditionRspDTO;
import com.cmpay.lemon.monitor.dto.ProductionDTO;
import com.cmpay.lemon.monitor.dto.ProductionInputReqDTO;
import com.cmpay.lemon.monitor.entity.Constant;
import com.cmpay.lemon.monitor.entity.ProductionPicDO;
import com.cmpay.lemon.monitor.entity.ScheduleDO;
import com.cmpay.lemon.monitor.entity.sendemail.*;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.demand.ReqTaskService;
import com.cmpay.lemon.monitor.service.production.OperationProductionService;
import com.cmpay.lemon.monitor.utils.BaseUtil;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


@RestController
@RequestMapping(value = MonitorConstants.Production_PATH)
public class OperationProductionController {
    @Autowired
    private OperationProductionService operationProductionService;

    @Autowired
    private ReqTaskService reqTaskService;
    /**
     * 分页需求列表
     *
     * @param reqDTO
     * @return
     */
    @RequestMapping("/list")
    public GenericRspDTO<ProductionConditionRspDTO> getUserInfoPage(@RequestBody ProductionConditionReqDTO reqDTO) {
        if(reqDTO.getProDateStart()!=null && !reqDTO.getProDateStart().equals("") && (reqDTO.getProDateEnd()==null || reqDTO.getProDateEnd().equals(""))){
            reqDTO.setProDate(reqDTO.getProDateStart());
        }
        if(reqDTO.getProDateEnd()!=null && !reqDTO.getProDateEnd().equals("") && (reqDTO.getProDateStart()==null || reqDTO.getProDateStart().equals(""))){
            reqDTO.setProDate(reqDTO.getProDateEnd());
        }
        ProductionBO productionBO = BeanUtils.copyPropertiesReturnDest(new ProductionBO(), reqDTO);
        ProductionRspBO productionRspBO = operationProductionService.find(productionBO);
        ProductionConditionRspDTO rspDTO = new ProductionConditionRspDTO();
        rspDTO.setProductionList(BeanConvertUtils.convertList(productionRspBO.getProductionList(), ProductionDTO.class));
        rspDTO.setPageNum(productionRspBO.getPageInfo().getPageNum());
        rspDTO.setPages(productionRspBO.getPageInfo().getPages());
        rspDTO.setTotal(productionRspBO.getPageInfo().getTotal());
        rspDTO.setPageSize(productionRspBO.getPageInfo().getPageSize());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, rspDTO);
    }

    // 基地工作量导出
    @RequestMapping("/productionOut")
    public void exportExcel(@RequestBody ProductionConditionReqDTO reqDTO, HttpServletRequest request, HttpServletResponse response) {
        ProductionBO productionBO = BeanUtils.copyPropertiesReturnDest(new ProductionBO(), reqDTO);
        System.err.println(productionBO);
        operationProductionService.exportExcel(request,response,productionBO);
    }
    @RequestMapping("/updateAllProduction")
    public GenericRspDTO<NoBody> updateAllProduction(@RequestParam("taskIdStr") String taskIdStr, HttpServletRequest request, HttpServletResponse response){
        System.err.println(taskIdStr);
        operationProductionService.updateAllProduction(request,response,taskIdStr);
        return GenericRspDTO.newSuccessInstance();
    }

    //获取邮件组
    @RequestMapping("/mailGroupSearch")
    public void productionInput(@RequestBody GenericDTO<NoBody> req , HttpServletRequest request, HttpServletResponse response){
        List<MailGroupBO> lst =operationProductionService.searchMailGroupList();
        System.err.println(111);
    }


    //保存全部投产记录
    @RequestMapping(value = "/productionInput", method = RequestMethod.POST)
    public GenericRspDTO<NoBody>  productionInput( ProductionInputReqDTO reqDTO,HttpServletRequest request)  {

        MultipartFile file=null;
        String isApproveProduct1 = request.getParameter("isApproveProduct");
        Boolean isApproveProduct=false;
        if( isApproveProduct1!=null&&isApproveProduct1.equals("true")){
            isApproveProduct=true;
        }
        if(reqDTO.getAttachment()) {
            List<MultipartFile> files1 = ((MultipartHttpServletRequest) request).getFiles("file");
            file = files1.get(0);
        }
        System.err.println(reqDTO.toString());
        if(file!=null){
            System.err.println(" 收到文件!");
        }else{
            System.err.println(" 未收到文件!");
        }
        ProductionBO bean = new ProductionBO();
        BeanUtils.copyPropertiesReturnDest(bean,reqDTO);
        bean.setProStatus("投产提出");
        boolean isSend = true;
        //后台判断数据
        if (!bean.getProNumber().matches(".*[a-zA-z].*")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 您的投产编号填写错误");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getProNumber() == null || bean.getProNumber().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 您的投产编号不能为空");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getProType().equals("救火更新")) {
            if (bean.getOperatingTime() == null || bean.getOperatingTime().equals("")) {
                MsgEnum.ERROR_IMPORT.setMsgInfo(" 更新预计操作时长不能为空");
                BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
            }
        }
        if (bean.getProNeed() == null || bean.getProNeed().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 需求名不能为空");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getProType() == null || bean.getProType().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 投产类型不能为空");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getProDate() == null || bean.getProDate().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 投产日期不能为空");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getApplicationDept() == null || bean.getApplicationDept().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 申请部门不能为空");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getProApplicant() == null || bean.getProApplicant().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 申请人不能为空");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getApplicantTel() == null || bean.getApplicantTel().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 申请人联系方式不能为空");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getProModule() == null || bean.getProModule().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 产品模块不能为空");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getBusinessPrincipal() == null || bean.getBusinessPrincipal().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 基地业务负责人不能为空");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getBasePrincipal() == null || bean.getBasePrincipal().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 基地技术负责人不能为空");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getProManager() == null || bean.getProManager().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 您有必填字段为空,请不要使用ie或360浏览器录入!12");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getIsUpDatabase() == null || bean.getIsUpDatabase().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 您有必填字段为空,请不要使用ie或360浏览器录入!13");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getIsUpStructure() == null || bean.getIsUpStructure().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 您有必填字段为空,请不要使用ie或360浏览器录入!14");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getProOperation() == null || bean.getProOperation().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 您有必填字段为空,请不要使用ie或360浏览器录入!15");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getIsRefCerificate() == null || bean.getIsRefCerificate().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 您有必填字段为空,请不要使用ie或360浏览器录入!16");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getIsAdvanceProduction() == null || bean.getIsAdvanceProduction().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 您有必填字段为空,请不要使用ie或360浏览器录入!17");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getIsAdvanceProduction().equals("否")) {
            if (bean.getNotAdvanceReason() == null || bean.getNotAdvanceReason().equals("")) {
                MsgEnum.ERROR_IMPORT.setMsgInfo(" 您有必填字段为空,请不要使用ie或360浏览器录入!18");
                BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
            }
        }
        if (bean.getIsAdvanceProduction().equals("是")) {
            if (bean.getProAdvanceResult() == null || bean.getProAdvanceResult().equals("")) {
                MsgEnum.ERROR_IMPORT.setMsgInfo(" 您有必填字段为空,请不要使用ie或360浏览器录入!19");
                BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
            }
        }
        if (bean.getIdentifier() == null || bean.getIdentifier().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 您有必填字段为空,请不要使用ie或360浏览器录入!20");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getIdentifierTel() == null || bean.getIdentifierTel().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 您有必填字段为空,请不要使用ie或360浏览器录入!21");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getProChecker() == null || bean.getProChecker().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 您有必填字段为空,请不要使用ie或360浏览器录入!22");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getCheckerTel() == null || bean.getCheckerTel().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 您有必填字段为空,请不要使用ie或360浏览器录入!23");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getValidation() == null || bean.getValidation().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 您有必填字段为空,请不要使用ie或360浏览器录入!24");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getDevelopmentLeader() == null || bean.getDevelopmentLeader().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 您有必填字段为空,请不要使用ie或360浏览器录入!25");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getApprover() == null || bean.getApprover().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 您有必填字段为空,请不要使用ie或360浏览器录入!26");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getRemark() == null || bean.getRemark().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 您有必填字段为空,请不要使用ie或360浏览器录入!27");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }

        if (bean.getMailLeader() == null || bean.getMailLeader().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 您有必填字段为空,请不要使用ie或360浏览器录入!28");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }

        System.err.println("参数验证完毕");

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
        SendEmailConfig config = new SendEmailConfig();
        //投产日正常投产，是否超时11点需要审批的投产
        System.err.println(isApproveProduct+";"+bean.getProType()+";"+bean.getIsOperationProduction());
        if (isApproveProduct && bean.getProType().equals("正常投产") && bean.getIsOperationProduction().equals("是")) {
            // 设置投产录入状态
            bean.setProStatus("投产待审批");
            //收件人
            List<String> receiver_users = new ArrayList<String>();
            //添加申请人部门经理邮箱地址
            receiver_users.add(bean.getProApplicant());
            receiver_users.add(bean.getDevelopmentLeader());
            String receiver_mail = bean.getMailLeader()+";"+operationProductionService.findManagerMailByUserName(receiver_users) + ";" + config.getNormalMailTo(false)+";wu_lr@hisuntech.com";
            //todo 收件人需要添加两人必选先注释 先用自己的邮件代替
            //+";tian_qun@hisuntech.com;huang_jh@hisuntech.com";
            // 邮件去重
            receiver_mail = BaseUtil.distinctStr(receiver_mail, ";");
            //记录邮箱信息
            MailFlowBean bnb = new MailFlowBean("【投产录入审批申请】", Constant.P_EMAIL_NAME, receiver_mail, "");
            bean.setMailRecipient(receiver_mail);
            //todo 抄送人需要添加两人必选先注释 先用自己的邮件代替
            //bean.setMailCopyPerson("tian_qun@hisuntech.com;huang_jh@hisuntech.com");
            bean.setMailCopyPerson("wu_lr@hisuntech.com");
            mailInfo.setToAddress(receiver_mail.split(";"));
            mailInfo.setSubject("【投产录入审批申请】-" + bean.getProNeed() + "-" + bean.getProNumber() + "-" + bean.getProApplicant());
            mailInfo.setContent("武金艳、肖铧：<br/>&nbsp;&nbsp;由于超过正常投产录入时间，投产无法正常录入，现申请投产审批，烦请审批！");
            // 这个类主要来发送邮件
            SimpleMailSender sms = new SimpleMailSender();
            isSend = sms.sendHtmlMail(mailInfo);// 发送html格式
            operationProductionService.addMailFlow(bnb);
        }
        //正常投产；投产日投产；不投产验证
        if (bean.getProType().equals("正常投产") && bean.getIsOperationProduction().equals("是") && bean.getIsAdvanceProduction().equals("否")) {
            //添加各人员部门经理邮箱地址
            //收件人
            List<String> receiver_users = new ArrayList<String>();
            //添加申请人部门经理邮箱地址
            receiver_users.add(bean.getProApplicant());
            //添加产品部门经理邮箱地址
            receiver_users.add(bean.getProManager());
            //添加开发负责人部门经理邮箱地址
            receiver_users.add(bean.getDevelopmentLeader());
            String receiver_mail = operationProductionService.findManagerMailByUserName(receiver_users) + ";" + config.getNormalMailTo(false);
            // 邮件去重
            receiver_mail = BaseUtil.distinctStr(receiver_mail, ";");
            //投产信息记录邮箱
            bean.setMailRecipient(receiver_mail);
            //记录邮箱信息
            mailInfo.setToAddress(receiver_mail.split(";"));
            mailInfo.setSubject("【预投产不验证申请】-" + bean.getProNeed() + "-" + bean.getProNumber() + "-" + bean.getProApplicant());
            mailInfo.setContent("武金艳、肖铧：<br/>&nbsp;&nbsp;由于" + bean.getNotAdvanceReason() + "，预投产无法验证，现申请预投产不验证，烦请审批！");
            // 这个类主要来发送邮件
            SimpleMailSender sms = new SimpleMailSender();
            isSend = sms.sendHtmlMail(mailInfo);// 发送html格式
            operationProductionService.addMailFlow(new MailFlowBean("【预投产不验证申请】", Constant.P_EMAIL_NAME, receiver_mail, ""));
        }
        //非投产日正常投产
        if (bean.getIsOperationProduction() != null && bean.getProType().equals("正常投产") && bean.getIsOperationProduction().equals("否")) {
            List<ProductionBO> unusuaList = new ArrayList<ProductionBO>();
            unusuaList.add(bean);
            File unusualFile = operationProductionService.exportUnusualExcel( unusuaList);
            // 附件
            Vector<File> filesv = new Vector<File>();
            // 添加附件信息
            if (!file.isEmpty()) {
                filesv = this.setVectorFile(file,filesv,bean);
            }
            filesv.add(unusualFile);
            mailInfo.setFile(filesv);
            // 收件人邮箱
            List<String> receiver_users = new ArrayList<String>();
            List<String> copy_users = new ArrayList<String>();
            //添加申请人部门经理邮箱地址
            receiver_users.add(bean.getProApplicant());
            //添加部门经理邮箱地址
            //todo 先去掉写死
             //copy_users.add(bean.getApplicationDept());
            System.err.println("申请人部门经理邮箱"+bean.getProApplicant());
            copy_users.add(bean.getProApplicant());
            boolean is_advance_production = true;
            // 是否投产验证
            if (bean.getIsAdvanceProduction().equals("否")) {
                is_advance_production = false;
                //添加产品部门经理邮箱地址
                receiver_users.add(bean.getProManager());
                //添加开发负责人部门经理邮箱地址
                receiver_users.add(bean.getDevelopmentLeader());
            }
            //相关人员邮件
            String receiver_mail = operationProductionService.findManagerMailByUserName(receiver_users) + ";" + config.getAbnormalMailTo(is_advance_production) + ";" + bean.getMailRecipient();
            System.err.println("相关人员邮件"+receiver_mail);
            //系统配置邮件、救火更新收件人邮件
            //收件人去重
            receiver_mail = BaseUtil.distinctStr(receiver_mail, ";");
            System.err.println("相关人员邮件去重后"+receiver_mail);
            //抄送人邮箱
            String copy_mail = operationProductionService.findManagerMailByUserName(copy_users) + ";" + config.getAbnormalMailCopy();
            if (bean.getMailCopyPerson() != null && !bean.getMailCopyPerson().equals("")) {
                copy_mail += ";" + bean.getMailCopyPerson();
            }
            //去重抄送人邮箱
            copy_mail = BaseUtil.distinctStr(copy_mail, ";");
            //投产信息记录邮箱
            bean.setMailRecipient(receiver_mail);
            bean.setMailCopyPerson(copy_mail);
            mailInfo.setToAddress(receiver_mail.split(";"));
            mailInfo.setCcs(copy_mail.split(";"));
            System.err.println("收件人"+ mailInfo.getToAddress()[0]);
            System.err.println("抄送人人数"+ mailInfo.getCcs().length);
            System.err.println("抄送人第一个"+ mailInfo.getCcs()[0]);
            mailInfo.setSubject("【正常投产(非投产日)审核】-" + bean.getProNeed() + "-" + bean.getProNumber() + "-" + bean.getProApplicant());
            //拼接邮件内容
            mailInfo.setContent("各位领导好:<br/>&nbsp;&nbsp;本次投产申请详细内容请参见下表<br/>烦请审批，谢谢！<br/>" + EmailConfig.setProEmailContent(bean));
            // 这个类主要来发送邮件
            SimpleMailSender sms = new SimpleMailSender();
            isSend = sms.sendHtmlMail(mailInfo);// 发送html格式
            operationProductionService.addMailFlow(new MailFlowBean("【正常投产(非投产日)审核】", Constant.P_EMAIL_NAME, receiver_mail, unusualFile.getName(), "wu_lr@hisuntech.com"));
            if (unusualFile.isFile() && unusualFile.exists()) {
                unusualFile.delete();
            }
        }


        //非投救火更新
        if (bean.getProType().equals("救火更新")) {
            bean.setIsOperationProduction("");
            bean.setProStatus("投产待部署");
            List<ProductionBO> list = new ArrayList<ProductionBO>();
            list.add(bean);


            List<String> receiver_users = new ArrayList<String>();
            List<String> copy_users = new ArrayList<String>();

            // 添加申请人
            receiver_users.add(bean.getProApplicant());
            //todo
            // 添加部门经理 抄送
            //copy_users.add(bean.getApplicationDept());
            copy_users.add("余宽");
            // 附件
            Vector<File> files = new Vector<File>();
            File file_fire = operationProductionService.exportUrgentExcel(list);
            // 添加救火附件
            if (!file.isEmpty()) {
                files = this.setVectorFile(file,files,bean);
            }
            files.add(file_fire);
            mailInfo.setFile(files);
            // 收件人邮箱
            String base_receiver_mail = bean.getMailRecipient();
            System.err.println("救火更新收件人"+base_receiver_mail);
            if (bean.getIsAdvanceProduction().equals("是")) {
                base_receiver_mail +=  config.getFireMailTo(true);
                System.err.println("救火更新收件人2"+base_receiver_mail);
            } else {
                // todo 注释 写死
                // 添加产品经理
                //receiver_users.add(bean.getProManager());
                // 添加开发负责人
               // receiver_users.add(bean.getDevelopmentLeader());
                System.err.println(bean.getProManager()+"      产品经理开发负责人       "+bean.getDevelopmentLeader());
                receiver_users.add("江琼");
                base_receiver_mail += ";" + bean.getMailLeader() + ";" + config.getFireMailTo(false);
            }
            //去重
            System.err.println("救火更新去重前："+base_receiver_mail);
            base_receiver_mail=base_receiver_mail +";"+ operationProductionService.findManagerMailByUserName(receiver_users);
            System.err.println(base_receiver_mail);
            String receiver_mail = BaseUtil.distinctStr(base_receiver_mail, ";");
            System.err.println("救火更新去重后："+receiver_mail);
            // 抄送人邮箱
            String copy_mail = operationProductionService.findManagerMailByUserName(copy_users) + ";" + config.getFireMailCopy();
            System.err.println("救火更新抄送人："+copy_mail);
            if (bean.getMailCopyPerson() != null && !bean.getMailCopyPerson().equals("")) {
                copy_mail += ";" + bean.getMailCopyPerson();
            }
            copy_mail = BaseUtil.distinctStr(copy_mail, ";");

            //记录邮箱信息
            bean.setMailRecipient(receiver_mail);
            bean.setMailCopyPerson(copy_mail);
            //记录邮箱信息
            mailInfo.setToAddress(receiver_mail.split(";"));
            mailInfo.setCcs(copy_mail.split(";"));
            //保存抄送人
//	          bean.setMail_copy_person(mailCopySum);
            mailInfo.setSubject("【救火更新审核】-" + bean.getProNeed() + "-" + bean.getProNumber() + "-" + bean.getProApplicant());

            mailInfo.setContent("各位领导好:<br/>&nbsp;&nbsp;本次投产申请详细内容请参见下表<br/>烦请审批，谢谢！<br/>" + EmailConfig.setFireEmailContent(bean));
            // 这个类主要来发送邮件
            SimpleMailSender sms = new SimpleMailSender();
            isSend = sms.sendHtmlMail(mailInfo);// 发送html格式
            operationProductionService.addMailFlow(new MailFlowBean("【救火更新审核】", Constant.P_EMAIL_NAME, receiver_mail, "", ""));
            if (file_fire != null && file_fire.isFile() && file_fire.exists()) {
                file_fire.delete();
            }
        }



        // 将其原先的lis循环查找相同pro_number编号的投产信息 更新为查找一条记录是否存在
        ProductionBO productionBean = operationProductionService.searchProdutionDetail(bean.getProNumber());
        bean.setProductionDeploymentResult("未部署");
        if (productionBean != null) {
            operationProductionService.updateAllProduction(bean);

            //生成流水记录
            ScheduleDO scheduleBean = new ScheduleDO(bean.getProNumber(), SecurityUtils.getLoginName(), "重新录入", productionBean.getProStatus(), bean.getProStatus(), "无");
            operationProductionService.addScheduleBean(scheduleBean);
            //是否预投产验证为“否”时，需求当前阶段变更为“完成预投产”
            if (bean.getIsAdvanceProduction().equals("否")) {
                DemandBO vo = new DemandBO();
                vo.setReqNo(bean.getProNumber());
                List<DemandBO> list = reqTaskService.getReqTaskByUK(vo);
                if (list.size() != 0) {
                    DemandBO demand = list.get(0);
                    demand.setPreCurPeriod("160");
                    reqTaskService.updatePreCurPeriod(demand);
                }

            }
            if (!isSend) {
                MsgEnum.ERROR_IMPORT.setMsgInfo(" 您有必填字段为空,请不要使用ie或360浏览器录入!");
                return GenericRspDTO.newInstance(MsgEnum.ERROR_IMPORT);
            }
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 投产已重新提交");
            return GenericRspDTO.newInstance(MsgEnum.ERROR_IMPORT);
        }

        operationProductionService.addProduction(bean);
        //生成流水记录
        ScheduleDO scheduleBean= new ScheduleDO(bean.getProNumber(),  SecurityUtils.getLoginName(), "录入", "", "投产提出", "无");
        operationProductionService.addScheduleBean(scheduleBean);
        //是否预投产验证为“否”时，需求当前阶段变更为“完成预投产”
        if (bean.getIsAdvanceProduction().equals("否")) {
            DemandBO vo = new DemandBO();
            vo.setReqNo(bean.getProNumber());
            List<DemandBO> list = reqTaskService.getReqTaskByUK(vo);
            if (list.size() != 0) {
                DemandBO DemandBO = list.get(0);
                DemandBO.setPreCurPeriod("160");
                reqTaskService.updatePreCurPeriod(DemandBO);
            }
        }
        if (!isSend) {
            //自定义类型成功
            MsgEnum.CUSTOMSUCCESS.setMsgInfo(" 投产已录入,但您有邮件没有发送成功,请及时联系系统维护人员!");
            return GenericRspDTO.newInstance(MsgEnum.CUSTOMSUCCESS);
        }
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS);
    }


    private Vector<File> setVectorFile(MultipartFile file,Vector<File> files,ProductionBO bean){
        //todo 先改成本机写死路径
        //String path = "/Users/zouxin/Desktop/tmpFile";
        String path="D:\\home\\devadm\\temp\\import";
//			String path = "/home/hims/upload/product";
        String fileName = file.getOriginalFilename();
        File tmp_file = new File(path + File.separator + bean.getProNumber() + "_" + fileName);
        try {
            if (!tmp_file.exists()) {
                file.transferTo(new File(path + File.separator + bean.getProNumber() + "_" + fileName));
                operationProductionService.addProductionPicBean(new ProductionPicDO(bean.getProNumber(), fileName, path));
            } else {
                file.transferTo(new File(path + File.separator + bean.getProNumber() + "_" + fileName));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        files.add(tmp_file);
        return  files;
    }


}
