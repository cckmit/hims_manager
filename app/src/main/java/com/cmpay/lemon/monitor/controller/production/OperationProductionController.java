package com.cmpay.lemon.monitor.controller.production;


import com.cmpay.framework.data.response.GenericRspDTO;
import com.cmpay.lemon.common.exception.BusinessException;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.framework.data.NoBody;
import com.cmpay.lemon.framework.security.SecurityUtils;
import com.cmpay.lemon.monitor.bo.DemandBO;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


@RestController
@RequestMapping(value = MonitorConstants.Production_PATH)
public class OperationProductionController {
    @Autowired
    private OperationProductionService OperationProductionService;

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
        ProductionRspBO productionRspBO = OperationProductionService.find(productionBO);
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
        OperationProductionService.exportExcel(request,response,productionBO);
    }
    @RequestMapping("/updateAllProduction")
    public GenericRspDTO<NoBody> updateAllProduction(@RequestParam("taskIdStr") String taskIdStr, HttpServletRequest request, HttpServletResponse response){
        System.err.println(taskIdStr);
        OperationProductionService.updateAllProduction(request,response,taskIdStr);
        return GenericRspDTO.newSuccessInstance();
    }

    //导入
    @RequestMapping("/productionInputest")
    public void productionInput(@RequestBody ProductionInputReqDTO reqDTO, HttpServletRequest request, HttpServletResponse response){
        System.err.println(reqDTO.toString());
        System.err.println(111);

    }

  //  @RequestParam MultipartFile file, boolean isApproveProduct,
    //保存全部投产记录
    @RequestMapping(value = "/productionInput", method = RequestMethod.POST)
    public GenericRspDTO<NoBody>  productionInput(@RequestBody ProductionInputReqDTO reqDTO, HttpServletResponse response, HttpServletRequest request)  {
        System.err.println("1111111111111");
         MultipartFile file = new MultipartFile() {
            @Override
            public String getName() {
                return null;
            }

            @Override
            public String getOriginalFilename() {
                return null;
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public long getSize() {
                return 0;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return new byte[0];
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return null;
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {

            }
        };
        boolean isApproveProduct=false;
        ProductionBO bean = new ProductionBO();
        BeanUtils.copyPropertiesReturnDest(bean,reqDTO);
        bean.setProStatus("投产提出");
        boolean isSend = true;
        //后台判断数据
        if (!bean.getProNumber().matches(".*[a-zA-z].*")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 您的投产编号填写错误,请不要使用ie、QQ或360浏览器录入!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getProNumber() == null || bean.getProNumber().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 您有必填字段为空,请不要使用ie或360浏览器录入!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getProType().equals("救火更新")) {
            if (bean.getOperatingTime() == null || bean.getOperatingTime().equals("")) {
                MsgEnum.ERROR_IMPORT.setMsgInfo(" 您有必填字段为空,请不要使用ie或360浏览器录入!");
                BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
            }
        }
        if (bean.getProNeed() == null || bean.getProNeed().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 您有必填字段为空,请不要使用ie或360浏览器录入!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getProType() == null || bean.getProType().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 您有必填字段为空,请不要使用ie或360浏览器录入!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getProDate() == null || bean.getProDate().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 您有必填字段为空,请不要使用ie或360浏览器录入!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getApplicationDept() == null || bean.getApplicationDept().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 您有必填字段为空,请不要使用ie或360浏览器录入!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getProApplicant() == null || bean.getProApplicant().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 您有必填字段为空,请不要使用ie或360浏览器录入!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getApplicantTel() == null || bean.getApplicantTel().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 您有必填字段为空,请不要使用ie或360浏览器录入!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getProModule() == null || bean.getProModule().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 您有必填字段为空,请不要使用ie或360浏览器录入!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getBusinessPrincipal() == null || bean.getBusinessPrincipal().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 您有必填字段为空,请不要使用ie或360浏览器录入!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getBasePrincipal() == null || bean.getBasePrincipal().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 您有必填字段为空,请不要使用ie或360浏览器录入!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getProManager() == null || bean.getProManager().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 您有必填字段为空,请不要使用ie或360浏览器录入!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getIsUpDatabase() == null || bean.getIsUpDatabase().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 您有必填字段为空,请不要使用ie或360浏览器录入!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getIsUpStructure() == null || bean.getIsUpStructure().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 您有必填字段为空,请不要使用ie或360浏览器录入!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getProOperation() == null || bean.getProOperation().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 您有必填字段为空,请不要使用ie或360浏览器录入!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getIsRefCerificate() == null || bean.getIsRefCerificate().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 您有必填字段为空,请不要使用ie或360浏览器录入!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getIsAdvanceProduction() == null || bean.getIsAdvanceProduction().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 您有必填字段为空,请不要使用ie或360浏览器录入!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getIsAdvanceProduction().equals("否")) {
            if (bean.getNotAdvanceReason() == null || bean.getNotAdvanceReason().equals("")) {
                MsgEnum.ERROR_IMPORT.setMsgInfo(" 您有必填字段为空,请不要使用ie或360浏览器录入!");
                BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
            }
        }
        if (bean.getIsAdvanceProduction().equals("是")) {
            if (bean.getProAdvanceResult() == null || bean.getProAdvanceResult().equals("")) {
                MsgEnum.ERROR_IMPORT.setMsgInfo(" 您有必填字段为空,请不要使用ie或360浏览器录入!");
                BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
            }
        }
        if (bean.getIdentifier() == null || bean.getIdentifier().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 您有必填字段为空,请不要使用ie或360浏览器录入!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getIdentifierTel() == null || bean.getIdentifierTel().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 您有必填字段为空,请不要使用ie或360浏览器录入!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getProChecker() == null || bean.getProChecker().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 您有必填字段为空,请不要使用ie或360浏览器录入!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getCheckerTel() == null || bean.getCheckerTel().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 您有必填字段为空,请不要使用ie或360浏览器录入!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getValidation() == null || bean.getValidation().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 您有必填字段为空,请不要使用ie或360浏览器录入!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getDevelopmentLeader() == null || bean.getDevelopmentLeader().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 您有必填字段为空,请不要使用ie或360浏览器录入!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getApprover() == null || bean.getApprover().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 您有必填字段为空,请不要使用ie或360浏览器录入!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        if (bean.getRemark() == null || bean.getRemark().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 您有必填字段为空,请不要使用ie或360浏览器录入!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }

        if (bean.getMailLeader() == null || bean.getMailLeader().equals("")) {
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 您有必填字段为空,请不要使用ie或360浏览器录入!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
        }
        //发邮件通知
        MailSenderInfo mailInfo = new MailSenderInfo();

        SendEmailConfig config = new SendEmailConfig();
        if (isApproveProduct && bean.getProType().equals("正常投产") && bean.getIsOperationProduction().equals("是")) {
            // 设置投产录入状态
            bean.setProStatus("投产待审批");

            //收件人
            List<String> receiver_users = new ArrayList<String>();
            //添加申请人部门经理邮箱地址
            receiver_users.add(bean.getProApplicant());
            receiver_users.add(bean.getDevelopmentLeader());
            String receiver_mail = bean.getMailLeader()+";"+OperationProductionService.findManagerMailByUserName(receiver_users) + ";" + config.getNormalMailTo(false);
            //todo 收件人需要添加两人必选先注释
            //+";tian_qun@hisuntech.com;huang_jh@hisuntech.com";
            // 邮件去重
            receiver_mail = BaseUtil.distinctStr(receiver_mail, ";");

            //记录邮箱信息
            MailFlowBean bnb = new MailFlowBean("【投产录入审批申请】", Constant.P_EMAIL_NAME, receiver_mail, "");
            bean.setMailRecipient(receiver_mail);
            bean.setMailCopyPerson("tian_qun@hisuntech.com;huang_jh@hisuntech.com");
            mailInfo.setToAddress(receiver_mail.split(";"));
            mailInfo.setSubject("【投产录入审批申请】-" + bean.getProNeed() + "-" + bean.getProNumber() + "-" + bean.getProApplicant());
            mailInfo.setContent("武金艳、肖铧：<br/>&nbsp;&nbsp;由于超过正常投产录入时间，投产无法正常录入，现申请投产审批，烦请审批！");
            // 这个类主要来发送邮件
            SimpleMailSender sms = new SimpleMailSender();

            isSend = sms.sendHtmlMail(mailInfo);// 发送html格式

            OperationProductionService.addMailFlow(bnb);
        }
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
            String receiver_mail = OperationProductionService.findManagerMailByUserName(receiver_users) + ";" + config.getNormalMailTo(false);
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

            OperationProductionService.addMailFlow(new MailFlowBean("【预投产不验证申请】", Constant.P_EMAIL_NAME, receiver_mail, ""));

        }

        if (bean.getIsOperationProduction() != null && bean.getProType().equals("正常投产") && bean.getIsOperationProduction().equals("否")) {
            List<ProductionBO> unusuaList = new ArrayList<ProductionBO>();
            unusuaList.add(bean);
            File unusualFile = OperationProductionService.exportUnusualExcel(response, unusuaList);
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
            copy_users.add(bean.getApplicationDept());


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
            String receiver_mail = OperationProductionService.findManagerMailByUserName(receiver_users) + ";" + config.getAbnormalMailTo(is_advance_production) + ";" + bean.getMailRecipient();
            //系统配置邮件、救火更新收件人邮件
            //收件人去重
            receiver_mail = BaseUtil.distinctStr(receiver_mail, ";");

            //抄送人邮箱
            String copy_mail = OperationProductionService.findManagerMailByUserName(copy_users) + ";" + config.getAbnormalMailCopy();
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
            mailInfo.setSubject("【正常投产(非投产日)审核】-" + bean.getProNeed() + "-" + bean.getProNumber() + "-" + bean.getProApplicant());
            //拼接邮件内容

            mailInfo.setContent("各位领导好:<br/>&nbsp;&nbsp;本次投产申请详细内容请参见下表<br/>烦请审批，谢谢！<br/>" + EmailConfig.setProEmailContent(bean));

            // 这个类主要来发送邮件
            SimpleMailSender sms = new SimpleMailSender();
            isSend = sms.sendHtmlMail(mailInfo);// 发送html格式

            OperationProductionService.addMailFlow(new MailFlowBean("【正常投产(非投产日)审核】", Constant.P_EMAIL_NAME, receiver_mail, unusualFile.getName(), ""));
            if (unusualFile.isFile() && unusualFile.exists()) {
                unusualFile.delete();
            }

        }

        if (bean.getProType().equals("救火更新")) {
            bean.setIsOperationProduction("");
            bean.setProStatus("投产待部署");
            List<ProductionBO> list = new ArrayList<ProductionBO>();
            list.add(bean);


            List<String> receiver_users = new ArrayList<String>();
            List<String> copy_users = new ArrayList<String>();

            // 添加申请人
            receiver_users.add(bean.getProApplicant());
            // 添加部门经理 抄送
            copy_users.add(bean.getApplicationDept());

            // 附件
            Vector<File> files = new Vector<File>();
            File file_fire = OperationProductionService.exportUrgentExcel(response, list);
            // 添加救火附件
            if (!file.isEmpty()) {
                files = this.setVectorFile(file,files,bean);
            }
            files.add(file_fire);
            mailInfo.setFile(files);
            // 收件人邮箱
            String base_receiver_mail = bean.getMailRecipient();
            if (bean.getIsAdvanceProduction().equals("是")) {
                base_receiver_mail += ";" + config.getFireMailTo(true);
            } else {
                // 添加产品经理
                receiver_users.add(bean.getProManager());
                // 添加开发负责人
                receiver_users.add(bean.getDevelopmentLeader());
                base_receiver_mail += ";" + bean.getMailLeader() + ";" + config.getFireMailTo(false);
            }
            //去重
            String receiver_mail = BaseUtil.distinctStr(base_receiver_mail + OperationProductionService.findManagerMailByUserName(receiver_users), ";");

            // 抄送人邮箱
            String copy_mail = OperationProductionService.findManagerMailByUserName(copy_users) + ";" + config.getFireMailCopy();
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
            OperationProductionService.addMailFlow(new MailFlowBean("【救火更新审核】", Constant.P_EMAIL_NAME, receiver_mail, "", ""));
            if (file_fire != null && file_fire.isFile() && file_fire.exists()) {
                file_fire.delete();
            }
        }
        // 将其原先的lis循环查找相同pro_number编号的投产信息 更新为查找一条记录是否存在
        ProductionBO productionBean = OperationProductionService.searchProdutionDetail(bean.getProNumber());
        bean.setProductionDeploymentResult("未部署");
        if (productionBean != null) {
            OperationProductionService.updateAllProduction(bean);

            //生成流水记录
            ScheduleDO scheduleBean = new ScheduleDO(bean.getProNumber(), SecurityUtils.getLoginName(), "重新录入", productionBean.getProStatus(), bean.getProStatus(), "无");
            OperationProductionService.addScheduleBean(scheduleBean);
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

        OperationProductionService.addProduction(bean);
        //生成流水记录
        ScheduleDO scheduleBean= new ScheduleDO(bean.getProNumber(),  SecurityUtils.getLoginName(), "录入", "", "投产提出", "无");
        OperationProductionService.addScheduleBean(scheduleBean);
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
            MsgEnum.ERROR_IMPORT.setMsgInfo(" 投产已录入,但您有邮件没有发送成功,请及时联系系统维护人员!");
            return GenericRspDTO.newInstance(MsgEnum.ERROR_IMPORT);
        }
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS);
    }


    private Vector<File> setVectorFile(MultipartFile file,Vector<File> files,ProductionBO bean){
        String path = "/Users/zouxin/Desktop/tmpFile";
//			String path = "/home/hims/upload/product";
        String fileName = file.getOriginalFilename();
        File tmp_file = new File(path + File.separator + bean.getProNumber() + "_" + fileName);
        try {
            if (!tmp_file.exists()) {
                file.transferTo(new File(path + File.separator + bean.getProNumber() + "_" + fileName));
                OperationProductionService.addProductionPicBean(new ProductionPicDO(bean.getProNumber(), fileName, path));
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
