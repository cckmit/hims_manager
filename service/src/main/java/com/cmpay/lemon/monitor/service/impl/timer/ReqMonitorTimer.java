package com.cmpay.lemon.monitor.service.impl.timer;


import com.cmpay.lemon.common.Env;
import com.cmpay.lemon.common.utils.JudgeUtils;
import com.cmpay.lemon.common.utils.StringUtils;
import com.cmpay.lemon.framework.utils.LemonUtils;
import com.cmpay.lemon.monitor.bo.DemandBO;
import com.cmpay.lemon.monitor.bo.ProductionTimeBO;
import com.cmpay.lemon.monitor.bo.ProductionVerificationIsNotTimelyBO;
import com.cmpay.lemon.monitor.dao.*;
import com.cmpay.lemon.monitor.entity.*;
import com.cmpay.lemon.monitor.entity.sendemail.MailFlowDO;
import com.cmpay.lemon.monitor.entity.sendemail.MailGroupDO;
import com.cmpay.lemon.monitor.entity.sendemail.MultiMailSenderInfo;
import com.cmpay.lemon.monitor.entity.sendemail.MultiMailsender;
import com.cmpay.lemon.monitor.service.demand.ReqPlanService;
import com.cmpay.lemon.monitor.service.demand.ReqTaskService;
import com.cmpay.lemon.monitor.service.gitlab.GitlabSubmitCodeDateService;
import com.cmpay.lemon.monitor.service.jira.JiraDataCollationService;
import com.cmpay.lemon.monitor.service.productTime.ProductTimeService;
import com.cmpay.lemon.monitor.service.production.OperationProductionService;
import com.cmpay.lemon.monitor.service.reportForm.ReqDataCountService;
import com.cmpay.lemon.monitor.service.sendmail.SendMailService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import com.cmpay.lemon.monitor.utils.DateUtil;
import com.cmpay.lemon.monitor.utils.SendExcelProductionVerificationIsNotTimely;
import com.cmpay.lemon.monitor.utils.wechatUtil.schedule.BoardcastScheduler;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReqMonitorTimer {
    private static Logger logger = Logger.getLogger(ReqMonitorTimer.class);
    @Autowired
    private ReqTaskService reqTaskService;
    @Autowired
    private ReqPlanService reqPlanService;
    @Autowired
    private IOperationProductionDao operationProductionDao;
    @Autowired
    private SendMailService sendMailService;
    @Autowired
    private ProductTimeService productTimeService;
    @Autowired
    private OperationProductionService operationProductionService;
    @Autowired
    private BoardcastScheduler boardcastScheduler;
    @Autowired
    private JiraDataCollationService jiraDataCollationService;
    @Autowired
    private IProCheckTimeOutStatisticsExtDao proCheckTimeOutStatisticsDao;
    @Autowired
    private IMonthWorkdayDao monthWorkdayDao;
    @Autowired
    private IUserExtDao userExtDao;
    @Autowired
    private IProductionVerificationIsNotTimelyExtDao iProductionVerificationIsNotTimelyExtDao;
    @Autowired
    private ReqDataCountService reqDataCountService;
    @Autowired
    private GitlabSubmitCodeDateService gitlabSubmitCodeDateService;
    @Autowired
    private IPlanDao planDao;

//	@Autowired
//	private OperationProductionServiceMgr operationProductionServiceMgr;
//
//	@Autowired
//	private ProductTimeServiceMgr productTimeServiceMgr;

    /**
     * 月底未完成的需求自动改为存量需求
     */
//	public void insertStockReq() {
//		Calendar c = Calendar.getInstance();
//		c.add(Calendar.MONTH, -1);
//		SimpleDateFormat simpleDateFormatMonth = new SimpleDateFormat("yyyy-MM");
//		String month = simpleDateFormatMonth.format(c.getTime());
//		reqTaskService.updateUnFinishReq(month);
//	}
    @Scheduled(cron = "0 40 23 * * ?")
    public void getIssueModifiedWithinOneDay() {
        //如果是dev环境则不处理
        if (LemonUtils.getEnv().equals(Env.DEV)) {
            return;
        }
        jiraDataCollationService.getIssueModifiedWithinOneDay();
    }
    /**
     * 每天晚上11点30定时处理统计
     */
    @Scheduled(cron = "0 30 23 * * ?")
    public void getQuantitativeDataMonth() {
        //如果是dev环境则不处理
        if (LemonUtils.getEnv().equals(Env.DEV)) {
            return;
        }
        //reqDataCountService.test();
    }
    /**
     * 每天凌晨1点定时处理统计前一天测试部需求测试进度表
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void TestProgressDetailOneDay() {
        //如果是dev环境则不处理
        if (LemonUtils.getEnv().equals(Env.DEV)) {
            return;
        }
        jiraDataCollationService.TestProgressDetailOneDay();
    }

    /**
     * 每天凌晨2点定时处理统计前一天的gitlab代码提交情况
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void getGitLabDate() {
        //如果是dev环境则不处理
        if (LemonUtils.getEnv().equals(Env.DEV)) {
            return;
        }
        gitlabSubmitCodeDateService.getGitlabDate();
    }
    /**
     * 需求累计投入资源
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void demandInputResourceStatistics() {
        //如果是dev环境则不处理
        if (LemonUtils.getEnv().equals(Env.DEV)) {
            return;
        }
        reqTaskService.demandInputResourceStatistics();
    }

    /**
     * 每周一更新投产时间
     */
    @Scheduled(cron = "0 0 1 ? * 1")
    public void updateProductTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat simpleDateFormatMonth = new SimpleDateFormat("yyyy-MM-dd");

        ProductionTimeBO productionTimeBO = new ProductionTimeBO();

        //本周投产日
        c.add(Calendar.DATE, 2);
        String day = simpleDateFormatMonth.format(c.getTime());
        productionTimeBO.setId(Integer.valueOf(3));
        productionTimeBO.setTime(day);
        productTimeService.updateProductTime(productionTimeBO);
        //下周预投产时间
        c.add(Calendar.DATE, 7);
        day = simpleDateFormatMonth.format(c.getTime());
        productionTimeBO.setId(Integer.valueOf(4));
        productionTimeBO.setTime(day);
        productTimeService.updateProductTime(productionTimeBO);
        //boardcastScheduler.pushTimeOutWarning("投产时间周定时变更");
    }
    // 每月工作日定时任务
    @Scheduled(cron = "0 0 1 * * ?")
    public void getMonthWorkday() {
        // 获取当前月份
        String month = DateUtil.date2String(new Date(), "yyyy-MM");
        // 获取当前日期
        String date = DateUtil.date2String(new Date(), "yyyy-MM-dd");
        // 放假日期集合
        String [] vacation = {"2021-01-01","2021-02-11","2021-02-12","2021-02-15","2021-02-16","2021-02-17","2021-04-05","2021-05-03"
                ,"2021-05-04","2021-05-05","2021-06-14","2021-09-20","2021-09-21","2021-10-01","2021-10-04","2021-10-05","2021-10-06"
                ,"2021-10-07"};
        // 国家调休日
        String [] workDay = {"2021-02-07","2021-02-20","2021-04-25","2021-05-08","2021-09-18","2021-09-26","2021-10-09"};
        MonthWorkdayDO monthWorkdayDO = monthWorkdayDao.getMonth(month);
        int weekday = monthWorkdayDO.getWorkPastDay();
        //  判断当前日期是否未国家法定放假日期，是，则跳出
        boolean res = Arrays.asList(vacation).contains(date);
        if(res){
            return;
        }
        //当前日期不是周末
        if(DateUtil.isWeekend(date) == false  || Arrays.asList(workDay).contains(date)){
            // 判断当前日期是否未国家调休日
            monthWorkdayDO.setWorkPastDay(weekday+1);
            monthWorkdayDao.update(monthWorkdayDO);
        }
    }

    /*
     *搜索1天之前状态为“投产待部署”的投产记录与状态为“审批通过待部署”的系统操作记录
     *提醒运维同事确认这些投产/系统操作是否已经部署完成，如果部署已完成，请及时更新状态，如果是回退，也请更新到对应的状态。
     *如果确实既没部署，也没有回退，则不用更新状态。
     *邮件发送给 it.version@hisuntech.com
     **/

    @Scheduled(cron = "10 0 12 * * ?")
    public void listOfUntimelyStatusChanges() {
        //如果是dev环境则不处理
        if (LemonUtils.getEnv().equals(Env.DEV)) {
            return;
        }
        //该功能计算起始时间
        String date = "2020-07-01";
        //获得投产待部署不及时清单
        List<ProductionDO> productionDOList = operationProductionService.getTheListOfProductionToBeDeployed(date);
        //获得审批通过待部署清单
        List<OperationApplicationDO> operationApplicationDOList = operationProductionService.getApprovalAndPassTheToDoList(date);
        //若俩清单为空则无邮箱发送
        if (JudgeUtils.isEmpty(productionDOList) && JudgeUtils.isEmpty(operationApplicationDOList)) {
            return;
        }
        //邮件信息
        MultiMailSenderInfo mailInfo = new MultiMailSenderInfo();
        mailInfo.setMailServerHost("smtp.qiye.163.com");
        mailInfo.setMailServerPort("25");
        mailInfo.setValidate(true);
        mailInfo.setUsername(Constant.EMAIL_NAME);
        mailInfo.setPassword(Constant.EMAIL_PSWD);
        mailInfo.setFromAddress(Constant.EMAIL_NAME);
        //收件人;version_it@hisuntech.com
        String result = "version_it@hisuntech.com";
        String[] mailToAddress = result.split(";");
        mailInfo.setReceivers(mailToAddress);
        //抄送人
        result = "wang_yw@hisuntech.com;wang_gang@hisuntech.com;peng_long@hisuntech.com";
        mailInfo.setCcs(result.split(";"));
        mailInfo.setSubject("【待部署状态变更未及时更新清单】");
        StringBuffer sb = new StringBuffer();
        sb.append("<table border ='1' style='width:1000px;border-collapse: collapse;background-color: white;'>");
        sb.append("<tr>");
        sb.append("<th>投产编号</th><th>需求名称及内容简述</th><th>投产类型</th>");
        sb.append("<th>计划投产日期</th><th>开发负责人</th><th>投产申请人</th><th>投产状态</th>");
        sb.append("</tr>");
        if (JudgeUtils.isNotEmpty(productionDOList)) {
            for (int i = 0; i < productionDOList.size(); i++) {
                sb.append("<tr>");
                sb.append("<td style='white-space: nowrap;'>" + productionDOList.get(i).getProNumber() + "</td>");//投产编号
                sb.append("<td style='white-space: nowrap;'>" + productionDOList.get(i).getProNeed() + "</td>");//需求名称及内容简述
                sb.append("<td style='white-space: nowrap;'>" + productionDOList.get(i).getProType() + "</td>");//投产类型
                sb.append("<td style='white-space: nowrap;'>" + productionDOList.get(i).getProDate() + "</td>");//计划投产日期
                sb.append("<td style='white-space: nowrap;'>" + productionDOList.get(i).getDevelopmentLeader() + "</td>");//开发负责人
                sb.append("<td style='white-space: nowrap;'>" + productionDOList.get(i).getProApplicant() + "</td>");//投产申请人
                sb.append("<td style='white-space: nowrap;'>" + productionDOList.get(i).getProStatus() + "</td>");//投产状态
                sb.append("</tr>");
            }
        }
        if (JudgeUtils.isNotEmpty(operationApplicationDOList)) {
            for (int i = 0; i < operationApplicationDOList.size(); i++) {
                sb.append("<tr>");
                sb.append("<td style='white-space: nowrap;'>" + operationApplicationDOList.get(i).getOperNumber() + "</td>");//投产编号
                sb.append("<td style='white-space: nowrap;'>" + operationApplicationDOList.get(i).getOperRequestContent() + "</td>");//需求名称及内容简述
                sb.append("<td style='white-space: nowrap;'>" + operationApplicationDOList.get(i).getSysOperType() + "</td>");//投产类型
                sb.append("<td style='white-space: nowrap;'>" + operationApplicationDOList.get(i).getProposeDate() + "</td>");//计划投产日期
                sb.append("<td style='white-space: nowrap;'>" + operationApplicationDOList.get(i).getDevelopmentLeader() + "</td>");//开发负责人
                sb.append("<td style='white-space: nowrap;'>" + operationApplicationDOList.get(i).getOperApplicant() + "</td>");//申请人
                sb.append("<td style='white-space: nowrap;'>" + operationApplicationDOList.get(i).getOperStatus() + "</td>");//投产状态
                sb.append("</tr>");
            }
        }
        sb.append("</table>");
        mailInfo.setContent("版本组:<br/>&nbsp;&nbsp;&nbsp;&nbsp;以下投产记录或系统操作记录目前处于待部署状态清单，请确认最新部署状态，并更新。<br/><br/>&nbsp;&nbsp;&nbsp;&nbsp;如果投产已回退，也请更新对应状态。如果既没有部署也没有回退则无需操作。<br/><br/>&nbsp;&nbsp;&nbsp;&nbsp;如有任何问题请及时反馈与沟通。<br/><br/>" + sb.toString());
        boolean isSend = MultiMailsender.sendMailtoMultiTest(mailInfo);

    }

    // 每天19点，发送当日投产清单邮件
    @Scheduled(cron = "0 0 19 * * ?")
    public void productionAuditToEmail(){
        // 查询当天的投产需求
        // 获取当前日期
        String date = DateUtil.date2String(new Date(), "yyyy-MM-dd");
        ProductionDO productionDO = new ProductionDO();
        productionDO.setProDate(new java.sql.Date(new java.util.Date().getTime()));
        List<ProductionDO>  list = operationProductionDao.findProductionAudit(productionDO);
        if(JudgeUtils.isNotEmpty(list)){
            // 总经理邮件组
            MailGroupDO mp = operationProductionDao.findMailGroupBeanDetail("7");
            // 部门主管邮件组
            MailGroupDO mp2 = operationProductionDao.findMailGroupBeanDetail("9");
            // 创建邮件信息
            MultiMailSenderInfo mailInfo = new MultiMailSenderInfo();
            mailInfo.setMailServerHost("smtp.qiye.163.com");
            mailInfo.setMailServerPort("25");
            mailInfo.setValidate(true);
            mailInfo.setUsername(Constant.EMAIL_NAME);
            mailInfo.setPassword(Constant.EMAIL_PSWD);
            mailInfo.setFromAddress(Constant.EMAIL_NAME);
            String[] mailToAddress = (mp.getMailUser()+";"+mp2.getMailUser()).split(";");
            mailInfo.setReceivers(mailToAddress);
            mailInfo.setSubject("【当日投产清单 - "+date+"】");
           //组织发送内容
            StringBuffer sb = new StringBuffer();
            sb.append("<table border ='1' style='width:1500px;border-collapse: collapse;background-color: white;'>");
            sb.append("<tr><th>投产编号</th><th>需求名称及内容简述</th><th>投产类型</th><th>计划投产日期</th><th>申请部门</th><th>投产申请人</th><th>影响范围</th><th>验证方案</th><th>回退/应急方案</th><th>审核人</th></tr>");
            for (int i = 0; i < list.size(); i++) {
                ProductionDO bean = list.get(i);
                //投产编号
                sb.append("<tr><td>" + bean.getProNumber() + "</td>");
                //需求名称及内容简述
                sb.append("<td >" + bean.getProNeed() + "</td>");
                //投产类型
                sb.append("<td style='white-space: nowrap;'>" + bean.getProType() + "</td>");
                // 日期转换
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                //计划投产日期
                if (bean.getProDate() != null){
                    sb.append("<td style='white-space: nowrap;'>" + sdf.format(bean.getProDate()) + "</td>");
                }
                //申请部门
                sb.append("<td style='white-space: nowrap;'>" + bean.getApplicationDept() + "</td>");
                //投产申请人
                sb.append("<td style='white-space: nowrap;'>" + bean.getProApplicant() + "</td>");
                //影响范围
                if (JudgeUtils.isNotNull(bean.getRemark())){
                    sb.append("<td style='white-space: nowrap;'>" + bean.getRemark() + "</td>");
                }else {
                    sb.append("<td style='white-space: nowrap;'>" + "" + "</td>");
                }
                //验证方案
                if (JudgeUtils.isNotNull(bean.getProofScheme())){
                    sb.append("<td style='white-space: nowrap;'>" + bean.getProofScheme() + "</td>");
                }else {
                    sb.append("<td style='white-space: nowrap;'>" + "" + "</td>");
                }
                //回退/应急方案
                if (JudgeUtils.isNotNull(bean.getCrashProgramme())){
                    sb.append("<td style='white-space: nowrap;'>" + bean.getCrashProgramme() + "</td>");
                }else{
                    sb.append("<td style='white-space: nowrap;'>" + "" + "</td>");
                }
                //审核人
                if (JudgeUtils.isNotNull(bean.getProAudit())){
                    sb.append("<td style='white-space: nowrap;'>" + bean.getProAudit() + "</td>");
                }else{
                    sb.append("<td style='white-space: nowrap;'>" + "" + "</td>");
                }
            }
            sb.append("</table>");
            mailInfo.setContent("大家好!<br/>&nbsp;&nbsp; 以下是今日投产清单,如有任何问题请及时反馈与沟通。<br/>" + sb.toString());
           // 这个类主要来发送邮件
            sendMailService.sendMail(mailInfo);
        }

    }
    /*
     *投产不及时验证清单发送企业微信
     *每天中午12点10秒执行，避免和别的微信推送内容冲突
     * */
    @Scheduled(cron = "10 0 12 * * ?")
    public void productionVerificationIsNotTimely() throws ParseException {
        //测试环境不发通知
        if (LemonUtils.getEnv().equals(Env.DEV)) {
            return;
        }
        List<String> devpCoorDepts = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //项目启动日期开始当天计算
        String date = "2020-09-01";
        //获得投产验证不及时清单
        List<ProductionDO> productionDOList = operationProductionService.getProductionVerificationIsNotTimely(date);
        //获得系统录入验证不及时清单
        //List<OperationApplicationDO> operationApplicationDOList = operationProductionService.getSystemEntryVerificationIsNotTimelyList(date);

        List<ProductionVerificationIsNotTimelyBO> listOfUntimelyStatusChangesBos = new LinkedList<>();
        if (JudgeUtils.isEmpty(productionDOList)) {
            return ;
        }
        try {
            if (productionDOList != null && productionDOList.size() != 0) {
                for (int i = 0; i < productionDOList.size(); i++) {
                    ProductionVerificationIsNotTimelyBO productionVerificationIsNotTimelyBO = new ProductionVerificationIsNotTimelyBO();
                    productionVerificationIsNotTimelyBO.setProNumber(productionDOList.get(i).getProNumber());
                    productionVerificationIsNotTimelyBO.setProNeed(productionDOList.get(i).getProNeed());
                    productionVerificationIsNotTimelyBO.setProType(productionDOList.get(i).getProType());
                    productionVerificationIsNotTimelyBO.setValidation(productionDOList.get(i).getValidation());
                    productionVerificationIsNotTimelyBO.setProDate(sdf.format(productionDOList.get(i).getProDate()));
                    productionVerificationIsNotTimelyBO.setIdentifier(productionDOList.get(i).getIdentifier());
                    productionVerificationIsNotTimelyBO.setDepartment(productionDOList.get(i).getApplicationDept());
                    productionVerificationIsNotTimelyBO.setProStatus(productionDOList.get(i).getProStatus());
                    Calendar c1 = Calendar.getInstance();
                    Calendar c2 = Calendar.getInstance();
                    c1.setTime(sdf.parse(sdf.format(new Date())));
                    c2.setTime(sdf.parse(sdf.format(productionDOList.get(i).getProDate())));
                    long day = 0;
                    if("当晚验证".equals(productionDOList.get(i).getValidation())){
                        day = (sdf.parse(sdf.format(new Date())).getTime() - sdf.parse(sdf.format(productionDOList.get(i).getProDate())).getTime()) / (24 * 60 * 60 * 1000);
                        if(day<=0){
                            continue;
                        }
                    }
                    if("隔日验证".equals(productionDOList.get(i).getValidation())){
                        day = ( (sdf.parse(sdf.format(new Date())).getTime() - sdf.parse(sdf.format(productionDOList.get(i).getProDate())).getTime()) / (24 * 60 * 60 * 1000) ) -2;
                        if(day<=0){
                            continue;
                        }
                    }
                    if("待业务触发验证".equals(productionDOList.get(i).getValidation())){
                        day = ((sdf.parse(sdf.format(new Date())).getTime() - sdf.parse(sdf.format(productionDOList.get(i).getProDate())).getTime()) / (24 * 60 * 60 * 1000) ) -7;
                        if(day<=0){
                            continue;
                        }
                    }
                    productionVerificationIsNotTimelyBO.setSumDay(day + "");
                    devpCoorDepts.add(productionDOList.get(i).getIdentifier());
                    listOfUntimelyStatusChangesBos.add(productionVerificationIsNotTimelyBO);
                }
            }
//            if (operationApplicationDOList != null && operationApplicationDOList.size() != 0) {
//                for (int i = 0; i < operationApplicationDOList.size(); i++) {
//                    ProductionVerificationIsNotTimelyBO productionVerificationIsNotTimelyBO = new ProductionVerificationIsNotTimelyBO();
//                    productionVerificationIsNotTimelyBO.setProNumber(operationApplicationDOList.get(i).getOperNumber());
//                    productionVerificationIsNotTimelyBO.setProNeed(operationApplicationDOList.get(i).getOperRequestContent());
//                    productionVerificationIsNotTimelyBO.setProType(operationApplicationDOList.get(i).getSysOperType());
//                    productionVerificationIsNotTimelyBO.setValidation("");
//                    productionVerificationIsNotTimelyBO.setProDate(sdf.format(operationApplicationDOList.get(i).getProposeDate()));
//                    productionVerificationIsNotTimelyBO.setIdentifier(operationApplicationDOList.get(i).getIdentifier());
//                    productionVerificationIsNotTimelyBO.setDepartment(operationApplicationDOList.get(i).getApplicationSector());
//                    productionVerificationIsNotTimelyBO.setProStatus(operationApplicationDOList.get(i).getOperStatus());
//                    Calendar c1 = Calendar.getInstance();
//                    Calendar c2 = Calendar.getInstance();
//                    c1.setTime(sdf.parse(sdf.format(new Date())));
//                    c2.setTime(sdf.parse(sdf.format(operationApplicationDOList.get(i).getProposeDate())));
//                    long day = (sdf.parse(sdf.format(new Date())).getTime() - sdf.parse(sdf.format(operationApplicationDOList.get(i).getProposeDate())).getTime()) / (24 * 60 * 60 * 1000);
//                    productionVerificationIsNotTimelyBO.setSumDay(day + "");
//                    devpCoorDepts.add(operationApplicationDOList.get(i).getIdentifier());
//                    listOfUntimelyStatusChangesBos.add(productionVerificationIsNotTimelyBO);
//                }
//            }
        } catch (ParseException e) {
        }

        for (int i =0;i<listOfUntimelyStatusChangesBos.size();i++){
            ProductionVerificationIsNotTimelyDO productionVerificationIsNotTimelyDO =  new ProductionVerificationIsNotTimelyDO();
            BeanConvertUtils.convert(productionVerificationIsNotTimelyDO, listOfUntimelyStatusChangesBos.get(i));
            ProductionVerificationIsNotTimelyDO productionVerificationIsNotTimelyDO1  = iProductionVerificationIsNotTimelyExtDao.get(productionVerificationIsNotTimelyDO.getProNumber());
            if(JudgeUtils.isNull(productionVerificationIsNotTimelyDO1)){
                iProductionVerificationIsNotTimelyExtDao.insert(productionVerificationIsNotTimelyDO);
            }else{
                iProductionVerificationIsNotTimelyExtDao.update(productionVerificationIsNotTimelyDO);
            }

        }



        Map<String, Integer> map = new HashMap<>();
        for(int i=0;i<productionDOList.size();i++){
            String applicationDept = productionDOList.get(i).getApplicationDept();
            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            c1.setTime(sdf.parse(sdf.format(new Date())));
            c2.setTime(sdf.parse(sdf.format(productionDOList.get(i).getProDate())));
            long day = 0;
            if("当晚验证".equals(productionDOList.get(i).getValidation())){
                day = (sdf.parse(sdf.format(new Date())).getTime() - sdf.parse(sdf.format(productionDOList.get(i).getProDate())).getTime()) / (24 * 60 * 60 * 1000);
                if(day<=0){
                    continue;
                }
            }
            if("隔日验证".equals(productionDOList.get(i).getValidation())){
                day = ( (sdf.parse(sdf.format(new Date())).getTime() - sdf.parse(sdf.format(productionDOList.get(i).getProDate())).getTime()) / (24 * 60 * 60 * 1000) ) -2;
                if(day<=0){
                    continue;
                }
            }
            if("待业务触发验证".equals(productionDOList.get(i).getValidation())){
                day = ((sdf.parse(sdf.format(new Date())).getTime() - sdf.parse(sdf.format(productionDOList.get(i).getProDate())).getTime()) / (24 * 60 * 60 * 1000) ) -7;
                if(day<=0){
                    continue;
                }
            }
            boolean exist = map.containsKey(applicationDept);
            if (exist) {
                map.put(applicationDept, map.get(applicationDept) + 1);
            } else {
                map.put(applicationDept, 1);
            }
        }

        String body = "投产验证不及时清单汇总" + "\n";
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String mapKey = entry.getKey();
            Integer mapValue = entry.getValue();
            body = body + mapKey + ":" + mapValue + "条" + "\n";
        }
        body = body + "\n";
//        if (!operationApplicationDOList.isEmpty()) {
//            operationApplicationDOList.forEach(m -> {
//                String applicationDept = m.getApplicationSector();
//                boolean exist = map.containsKey(applicationDept);
//                if (exist) {
//                    map.put(applicationDept, map.get(applicationDept) + 1);
//                } else {
//                    map.put(applicationDept, 1);
//                }
//            });
//            body = body + "操作录入不及时验证清单汇总" + "\n";
//            for (Map.Entry<String, Integer> entry : map.entrySet()) {
//                String mapKey = entry.getKey();
//                Integer mapValue = entry.getValue();
//                body = body + mapKey + ":" + mapValue + "条" + "\n";
//            }
//        }
        body = body + "详情如下";
        SendExcelProductionVerificationIsNotTimely sendExcelProductionVerificationIsNotTimely = new SendExcelProductionVerificationIsNotTimely();
        File file = null;
        try {
            String excel = "\\Unverified_List_" + DateUtil.date2String(new Date(), "yyyyMMdd") + ".xls";
            sendExcelProductionVerificationIsNotTimely.createExcel(excel, listOfUntimelyStatusChangesBos, null, null);
            file = new File(excel);

        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuffer sb = new StringBuffer();
        sb.append("<table border='1' style='border-collapse: collapse;background-color: white; white-space: nowrap;'>");
        sb.append("<tr><th>投产编号/系统操作编号</th><th>投产/操作内容简述</th><th>投产/操作类型</th><th>验证类型</th><th>投产/操作日期</th>");
        sb.append("<th>申请部门</th><th>验证人</th><th>当前状态</th><th>已投产/操作天数</th></tr>");
        Map<String, Integer> hashMap = new HashMap<>();
        for (int i = 0; i < listOfUntimelyStatusChangesBos.size(); i++) {
            sb.append("<tr><td >" + listOfUntimelyStatusChangesBos.get(i).getProNumber() + "</td>");//投产编号/系统操作编号
            sb.append("<td >" + listOfUntimelyStatusChangesBos.get(i).getProNeed() + "</td>");//投产/操作内容简述
            sb.append("<td >" + listOfUntimelyStatusChangesBos.get(i).getProType() + "</td>");//投产/操作类型
            sb.append("<td >" + listOfUntimelyStatusChangesBos.get(i).getValidation() + "</td>");//验证方式
            sb.append("<td >" + listOfUntimelyStatusChangesBos.get(i).getProDate() + "</td>");//投产/操作日期
            sb.append("<td >" + listOfUntimelyStatusChangesBos.get(i).getDepartment() + "</td>");//申请部门
            sb.append("<td >" + listOfUntimelyStatusChangesBos.get(i).getIdentifier() + "</td>");//验证人
            sb.append("<td >" + listOfUntimelyStatusChangesBos.get(i).getProStatus() + "</td>");//当前状态
           sb.append("<td >" + listOfUntimelyStatusChangesBos.get(i).getSumDay() + "</td></tr>");//已投产/操作天数
        }
        sb.append("</table>");

        for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
            ProCheckTimeOutStatisticsDO proCheckTimeOutStatisticsDO = new ProCheckTimeOutStatisticsDO();
            String mapKey = entry.getKey();
            Integer mapValue = entry.getValue();
            proCheckTimeOutStatisticsDO.setCount(mapValue);
            proCheckTimeOutStatisticsDO.setDepartment(mapKey);
            proCheckTimeOutStatisticsDO.setRegistrationdate(DateUtil.date2String(new Date(), "yyyy-MM-dd"));
            proCheckTimeOutStatisticsDao.insert(proCheckTimeOutStatisticsDO);
        }

        //如果有内容则调用企业微信应用和邮件发送推送
        if (!productionDOList.isEmpty()) {
            //邮件信息推送
            MultiMailSenderInfo mailInfo = new MultiMailSenderInfo();
            mailInfo.setMailServerHost("smtp.qiye.163.com");
            mailInfo.setMailServerPort("25");
            mailInfo.setValidate(true);
            mailInfo.setUsername(Constant.EMAIL_NAME);
            mailInfo.setPassword(Constant.EMAIL_PSWD);
            mailInfo.setFromAddress(Constant.EMAIL_NAME);
            //去重
            List<String> list=(List) devpCoorDepts.stream().distinct().collect(Collectors.toList());
//            String [] nameList = new String[list.size()];
//            nameList = list.toArray(nameList);
            String nameList ="";
            for (int i=0;i<list.size();i++){
                DemandDO demandDO =planDao.searchUserEmail(list.get(i));
                if(JudgeUtils.isNotNull(demandDO)){
                    nameList = nameList+demandDO.getMonRemark()+";";
                }
            }
            System.err.println(nameList);
            // 获取所有部门主管的邮箱
            MailGroupDO mp = operationProductionDao.findMailGroupBeanDetail("9");
            String result = mp.getMailUser()+nameList;
            String[] mailToAddress = result.split(";");
            mailInfo.setReceivers(mailToAddress);
            //抄送人
            result = "wang_yw@hisuntech.com;xiao_hua@hisuntech.com;wujinyan@hisuntech.com;";
            mailInfo.setCcs(result.split(";"));
            //添加附件
            Vector filesv = new Vector();
            filesv.add(file);
            mailInfo.setFile(filesv);
            mailInfo.setSubject("【投产验证不及时清单】");
            mailInfo.setContent("各位好！<br/>&nbsp;&nbsp;投产验证不及时清单,详情请参见附件,如已验证完成请及时修改状态<br/><br/>" + sb);
            boolean isSend = MultiMailsender.sendMailtoMultiTest(mailInfo);
            //企业微信信息推送
            boardcastScheduler.pushValidationNotTimelyChecklist(body, file);
        }
        file.delete();
    }

/**
 * 需求月底反馈 每月1号 系统统计月底实际完成阶段未达到月初预计完成阶段的需求，并邮件通知产品经理对需求目标未完成情况进行原因反馈，
 */
//	public void monthFeedBack() {
//		logger.error("需初反馈。。。。。。。。");
//		Calendar c = Calendar.getInstance();
//		c.add(Calendar.MONTH, -1);
//		SimpleDateFormat simpleDateFormatMonth = new SimpleDateFormat("yyyy-MM");
//		String month = simpleDateFormatMonth.format(c.getTime());
//		try {
//			reqTaskService.exportMonFeedExcel("2019-04");
//		} catch (ServiceException e) {
//			logger.error("需求月底反馈列表文件生成失败");
//		}
//		Vector<File> attachFiles = new Vector<File>();
//		attachFiles.add(new File("/tmp/export/req/" + "2019-04" + "需求月底反馈清单" + ".xls"));
//		// 查询列表
//		StringBuilder content = new StringBuilder();
//		content.append("各位产品经理，大家好！");
//		content.append("<br/>");
//		content.append("&nbsp;&nbsp;新的月份开始了，我们需要回顾上月需求完成情况，请大家在收到邮件三天内对上月需求进行月底反馈。");
//		content.append("三天后对于未及时反馈的需求我们将记录QA问题，希望大家尽快完成，同时感谢各位产品经理的配合，谢谢！");
//		String subject = month + "月需求月底反馈";
//		//reqTaskService.sendMail(Constant.PM_DEPT_MAIL, null, content.toString(), subject, attachFiles);
//	}

    /**
     * 每月四号 反馈周期为三天，预期未反馈的需求，系统自动将数据记录QA问题。
     */
//	public void addQAQuestion() {
//		Calendar c = Calendar.getInstance();
//		c.add(Calendar.MONTH, -1);
//		SimpleDateFormat simpleDateFormatMonth = new SimpleDateFormat("yyyy-MM");
//		String month = simpleDateFormatMonth.format(c.getTime());
//		ReqMngVO vo = new ReqMngVO();
//		vo.setReq_start_mon(month);
//		List<Demand> reqTasks = reqTaskService.findMonFeedBack(vo);
//		for (int i = 0; i < reqTasks.size(); i++) {
//			Demand reqFeedBack = new Demand();
//			if (StringUtils.isBlank(reqFeedBack.getEnd_mon_remark())) {
//				// TODO 记录QA问题
//
//			}
//		}
//
//	}
    /*
     * 每日需求进度异常监控 日终，系统判断需求实际实施完成阶段与计划完成阶段是否存在异常，对于存在异常的需求，每天中午十二点发送进度异常邮件通知
     */
     @Scheduled(cron = "0 0 12 * * ?")
    public void dailyAbnormalMonitor() {
        if (LemonUtils.getEnv().equals(Env.DEV)) {
            return;
        }
        Calendar c = Calendar.getInstance();
        SimpleDateFormat simpleDateFormatMonth = new SimpleDateFormat("yyyy-MM");
        String month = simpleDateFormatMonth.format(c.getTime());
        List<DemandBO> prdFinishAbnor = reqTaskService.getPrdFnishAbnor(month);
        List<DemandBO> uatUpdateAbnor = reqTaskService.getUatUpdateAbnor(month);
        List<DemandBO> testFinishAbnor = reqTaskService.getTestFnishAbnor(month);
        //项目组成员及QA、产品
        String sendTo = "";
        //主导部门主管、配合部门主管、测试、研发中心主管
        String copyTo = "";

        try {
            for (int i = 0; i < prdFinishAbnor.size(); i++) {
                DemandBO reqTask = prdFinishAbnor.get(i);
                String name = reqTask.getReqNo() + reqTask.getReqNm();
                int day = new Integer(reqTask.getMonRemark());
                String content = dailyAbnorReqContent(name, "需求定稿", day);
                String subject = name + "需求进度异常";
                //获取相关人员邮箱
                Map<String, String> map = reqPlanService.getMailbox(reqTask.getReqInnerSeq());
                sendTo = map.get("proMemberEmail");
                copyTo = map.get("devpEmail") + map.get("testDevpEmail") + "xiao_hua@hisuntech.com;wujinyan@hisuntech.com;";
//				sendTo = "tu_yi@hisuntech.com";
//				copyTo = "wu_lr@hisuntech.com";
                //发送邮件
                reqPlanService.sendMail(sendTo, copyTo, content.toString(), subject, null);
                //设置异常类型
                if (reqTask.getReqAbnorType().indexOf("01") != -1) {
                    reqTask.setReqAbnorType("03,");
                } else {
                    reqTask.setReqAbnorType(reqTask.getReqAbnorType() + "03,");
                }
                reqTaskService.updateReqAbnorType(reqTask);
            }
            for (int i = 0; i < uatUpdateAbnor.size(); i++) {
                DemandBO reqTask = uatUpdateAbnor.get(i);
                String name = reqTask.getReqNo() + reqTask.getReqNm();
                int day = new Integer(reqTask.getMonRemark());
                String content = dailyAbnorReqContent(name, "UAT更新", day);
                String subject = name + "开发进度异常";
                //获取相关人员邮箱
                Map<String, String> map = reqPlanService.getMailbox(reqTask.getReqInnerSeq());
                sendTo = map.get("proMemberEmail");
                copyTo = map.get("devpEmail") + map.get("testDevpEmail") + "xiao_hua@hisuntech.com;wujinyan@hisuntech.com;";
//				sendTo = "tu_yi@hisuntech.com";
//				copyTo = "wu_lr@hisuntech.com";
                //发送邮件
                reqPlanService.sendMail(sendTo, copyTo, content.toString(), subject, null);
                //设置异常类型
                if (reqTask.getReqAbnorType().indexOf("01") != -1) {
                    reqTask.setReqAbnorType("04,");
                } else {
                    reqTask.setReqAbnorType(reqTask.getReqAbnorType() + "04,");
                }
                reqTaskService.updateReqAbnorType(reqTask);
            }
            for (int i = 0; i < testFinishAbnor.size(); i++) {
                DemandBO reqTask = testFinishAbnor.get(i);
                String name = reqTask.getReqNo() + reqTask.getReqNm();
                int day = new Integer(reqTask.getMonRemark());
                String content = dailyAbnorReqContent(name, "UAT测试", day);
                String subject = name + "测试进度异常";
                //获取相关人员邮箱
                Map<String, String> map = reqPlanService.getMailbox(reqTask.getReqInnerSeq());
                sendTo = map.get("proMemberEmail");
                copyTo = map.get("devpEmail") + map.get("testDevpEmail") + "xiao_hua@hisuntech.com;wujinyan@hisuntech.com;";
//				sendTo = "tu_yi@hisuntech.com";
//				copyTo = "wu_lr@hisuntech.com";
                //发送邮件
                reqPlanService.sendMail(sendTo, copyTo, content.toString(), subject, null);
                //设置异常类型
                if (reqTask.getReqAbnorType().indexOf("01") != -1) {
                    reqTask.setReqAbnorType("05,");
                } else {
                    reqTask.setReqAbnorType(reqTask.getReqAbnorType() + "05,");
                }
                reqTaskService.updateReqAbnorType(reqTask);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("需求异常状态监控失败：" + e.getMessage());
        }
    }


    //todo
    // 需求预警 ：“需求定稿时间”或“UAT更新时间”或“测试完成时间”或“预投产时间”、“投产时间”小于1天，系统每日发送微信推送。
    // 日终，根据系统当前时间离关键里程碑计划完成时间剩余天数小于等于1且进度滞后于里程碑阶段的需求，每日上午8点-8点30分发送需求预警邮件通知
    //
    @Scheduled(cron = "0 0 12 * * ?")
    public void progressAlertPush() {
        if (LemonUtils.getEnv().equals(Env.DEV)) {
            return;
        }
        DemandDO demandDO = new DemandDO();
        String month = DateUtil.date2String(new Date(), "yyyy-MM");
        demandDO.setReqImplMon(month);
        //获得正常执行需求
        List<DemandBO> demandBOlist = reqPlanService.getNormalExecutionDemand(demandDO);
        demandBOlist.forEach(m -> {
            //没有部门或者没有当前需求状态的跳过
            if (StringUtils.isEmpty(m.getPreCurPeriod()) || StringUtils.isEmpty(m.getDevpLeadDept())) {
                return;
            } else {
                int PreCurPeriod = Integer.parseInt(m.getPreCurPeriod());
                //需求定稿
                if (PreCurPeriod < 30) {
                    if (StringUtils.isEmpty(m.getPrdFinshTm())) {
                        return;
                    } else {
                        int date = dateDifference(DateUtil.date2String(new Date(), "yyyy-MM-dd"), m.getPrdFinshTm());
                        if (date == 1) {
                            String deptManagerName = operationProductionService.findDeptManager(m.getDevpLeadDept()).getDeptManagerName();
                            UserDO userDO = new UserDO();
                            userDO.setFullname(deptManagerName);
                            List<UserDO> userDOS = userExtDao.find(userDO);
                            String username ="";
                            if(JudgeUtils.isNotEmpty(userDOS)){
                                username = userDOS.get(0).getUsername();
                            }
                            String body = "主导部门:" + m.getDevpLeadDept() + "\n" + "需求编号:" + m.getReqNo() + "\n" + m.getReqNm() + "\n" + "计划于明日达到阶段性目标【需求定稿】，请按时更新状态或提交文档，若计划有变，请及时变更";
                            boardcastScheduler.verificationCompleteReminder(username,body);
                        }
                    }

                }
                //UAT更新
                else if (PreCurPeriod < 120) {
                    if (StringUtils.isEmpty(m.getUatUpdateTm())) {
                        return;
                    } else {
                        int date = dateDifference(DateUtil.date2String(new Date(), "yyyy-MM-dd"), m.getUatUpdateTm());
                        if (date == 1) {
                            String body = "主导部门:" + m.getDevpLeadDept() + "\n" + "需求编号:" + m.getReqNo() + "\n" + m.getReqNm() + "\n" + "计划于明日达到阶段性目标【UAT更新】，请按时更新状态或提交文档，若计划有变，请及时变更";
                            String deptManagerName = operationProductionService.findDeptManager(m.getDevpLeadDept()).getDeptManagerName();
                            UserDO userDO = new UserDO();
                            userDO.setFullname(deptManagerName);
                            List<UserDO> userDOS = userExtDao.find(userDO);
                            String username ="";
                            if(JudgeUtils.isNotEmpty(userDOS)){
                                username = userDOS.get(0).getUsername();
                            }
                            boardcastScheduler.verificationCompleteReminder(username,body);
                        }
                    }

                }
                //测试完成
                else if (PreCurPeriod < 130) {
                    if (StringUtils.isEmpty(m.getTestFinshTm())) {
                        return;
                    } else {
                        int date = dateDifference(DateUtil.date2String(new Date(), "yyyy-MM-dd"), m.getTestFinshTm());
                        if (date == 1) {
                            String body = "主导部门:" + m.getDevpLeadDept() + "\n" + "需求编号:" + m.getReqNo() + "\n" + m.getReqNm() + "\n" + "计划于明日达到阶段性目标【UAT测试完成】，请按时更新状态或提交文档，若计划有变，请及时变更";
                            String deptManagerName = operationProductionService.findDeptManager(m.getDevpLeadDept()).getDeptManagerName();
                            UserDO userDO = new UserDO();
                            userDO.setFullname(deptManagerName);
                            List<UserDO> userDOS = userExtDao.find(userDO);
                            String username ="";
                            if(JudgeUtils.isNotEmpty(userDOS)){
                                username = userDOS.get(0).getUsername();
                            }
                            boardcastScheduler.verificationCompleteReminder(username,body);
                        }
                    }

                }
                //预投产
                else if (PreCurPeriod < 160) {
                    if (StringUtils.isEmpty(m.getPreTm())) {
                        return;
                    } else {
                        int date = dateDifference(DateUtil.date2String(new Date(), "yyyy-MM-dd"), m.getPreTm());
                        if (date == 1) {
                            String body = "主导部门:" + m.getDevpLeadDept() + "\n" + "需求编号:" + m.getReqNo() + "\n" + m.getReqNm() + "\n" + "计划于明日达到阶段性目标【预投产】，请按时更新状态或提交文档，若计划有变，请及时变更";
                            String deptManagerName = operationProductionService.findDeptManager(m.getDevpLeadDept()).getDeptManagerName();
                            UserDO userDO = new UserDO();
                            userDO.setFullname(deptManagerName);
                            List<UserDO> userDOS = userExtDao.find(userDO);
                            String username ="";
                            if(JudgeUtils.isNotEmpty(userDOS)){
                                username = userDOS.get(0).getUsername();
                            }
                            boardcastScheduler.verificationCompleteReminder(username,body);
                        }
                    }
                }
                //投产时间
                else if (PreCurPeriod < 180) {
                    if (StringUtils.isEmpty(m.getExpPrdReleaseTm())) {
                        return;
                    } else {
                        int date = dateDifference(DateUtil.date2String(new Date(), "yyyy-MM-dd"), m.getExpPrdReleaseTm());
                        if (date == 1) {
                            String body = "主导部门:" + m.getDevpLeadDept() + "\n" + "需求编号:" + m.getReqNo() + "\n" + m.getReqNm() + "\n" + "计划于明日达到阶段性目标【投产】，请按时更新状态或提交文档，若计划有变，请及时变更";
                            String deptManagerName = operationProductionService.findDeptManager(m.getDevpLeadDept()).getDeptManagerName();
                            UserDO userDO = new UserDO();
                            userDO.setFullname(deptManagerName);
                            List<UserDO> userDOS = userExtDao.find(userDO);
                            String username ="";
                            if(JudgeUtils.isNotEmpty(userDOS)){
                                username = userDOS.get(0).getUsername();
                            }
                            boardcastScheduler.verificationCompleteReminder(username,body);
                        }
                    }
                }
            }
        });

    }

    private int dateDifference(String date1, String date2) {
        int betweenDate = 0;
        try {
            //设置转换的日期格式
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //开始时间
            Date startDate = sdf.parse(date1);
            //结束时间
            Date endDate = sdf.parse(date2);
            //得到相差的天数 betweenDate
            betweenDate = (int) (endDate.getTime() - startDate.getTime()) / (60 * 60 * 24 * 1000);
            //打印控制台相差的天数
        } catch (Exception e) {
            e.printStackTrace();
        }
        return betweenDate;
    }

    // 需求预警 ：当系统时间与PRD完成时间、UAT更新时间、测试完成时间小于2天，系统每日发送提示邮件。
    // 日终，根据系统当前时间离关键里程碑计划完成时间剩余天数小于等于2且进度滞后于里程碑阶段的需求，每日上午8点-8点30分发送需求预警邮件通知
//	public void dailyProcessWarn() {
//		List<DemandBO> prdFinishWarnList = reqTaskService.getPrdFnishWarn();
//		// 项目组成员及主导部门主管、配合部门主管、QA
//		String sendTo ="";
//		// 产品研究部主管、产品测试部主管、中心经理
//		String copyTo ="";
//		for (int i = 0; i < prdFinishWarnList.size(); i++) {
//			DemandBO reqTask = prdFinishWarnList.get(i);
//			String name = reqTask.getReqNo() + reqTask.getReqNm();
//			String subject = name + "需求实施进度预警通知";
//			String content = dailyProcessContent(name, reqTask.getPrdFinshTm(), "需求定稿", reqTask.getProductMng());
//			//reqPlanService.sendMail(sendTo, null, content, subject, null);
//		}
//		List<DemandBO> uatUpdateWarnList = reqTaskService.getUatUpdateWarn();
//		for (int i = 0; i < uatUpdateWarnList.size(); i++) {
//			DemandBO reqTask = uatUpdateWarnList.get(i);
//			String name = reqTask.getReqNo() + reqTask.getReqNm();
//			String subject = name + "需求实施进度预警通知";
//			String content = dailyProcessContent(name, reqTask.getUatUpdateTm(), "uat更新", reqTask.getProjectMng());
//			//reqPlanService.sendMail(sendTo, null, content.toString(), subject, null);
//		}
//		List<DemandBO> testFinishWarnList = reqTaskService.getTestFnishWarn();
//		for (int i = 0; i < testFinishWarnList.size(); i++) {
//			DemandBO reqTask = testFinishWarnList.get(i);
//			String name = reqTask.getReqNo() + reqTask.getReqNm();
//			String subject = name + "需求实施进度预警通知";
//			String content = dailyProcessContent(name, reqTask.getTestFinshTm(), "uat测试", reqTask.getTestEng());
//			//reqPlanService.sendMail(sendTo, null, content.toString(), subject, null);
//		}
//	}

    // 日终，查询投产过程管理功能的数据进行状态同步
//	public void updateDemandStatus() {
//		SimpleDateFormat smf = new SimpleDateFormat("yyyy-MM-dd");
//		String cur_date = smf.format(new Date());
//		SimpleDateFormat smfMon = new SimpleDateFormat("yyyy-MM");
//		String cur_month = smfMon.format(new Date());
//		List<ProductionBean> productionBeans = operationProductionServiceMgr.findDayStatus(cur_date);
//		for (int i = 0; i < productionBeans.size(); i++) {
//			ProductionBean production = productionBeans.get(i);
//			String proStatus = production.getPro_status();
//			ReqMngVO reqTaskVO = new ReqMngVO();
//			reqTaskVO.setReq_no(production.getPro_number());
//			reqTaskVO.setReq_impl_mon(cur_month);
//			List<Demand> demandList = reqTaskService.findList(reqTaskVO);
//			if (demandList.size() != 1) {
//				continue;
//			} else {
//				Demand demand = demandList.get(0);
//				if ("投产待部署".equals(proStatus) || "部署完成待验证".equals(proStatus)) {
//					//待投产
//					demand.setPre_cur_period("16");
//				}
//				if ("投产验证完成".equals(proStatus)) {
//					demand.setPre_cur_period("17");
//					demand.setReq_sts("04");
//				}
//				try {
//					reqTaskService.updateReqTask(demand);
//				} catch (Exception e) {
//					logger.error("同步状态失败："+e.getMessage());
//				}
//			}
//		}
//	}

    private String dailyAbnorReqContent(String reqName, String reqPeriod, int day) {
        StringBuilder content = new StringBuilder();
        content.append("各位同事，大家好！");
        content.append("<br/>");
        content.append("&nbsp;&nbsp;  【" + reqName + "】需求在开发实施过程中，因" + reqPeriod + "未按计划完成，导致需求实施计划出现滞后情况,");
        content.append("预计需求实现最终目标延期" + day + "天，请主导部门及时跟进需求实施情况，并采取规避措施，降低需求延期风险，");
        content.append("同时请配合部门全力配合。感谢大家的辛苦付出！");
        return content.toString();
    }

    private String dailyProcessContent(String reqName, String date, String reqPeriod, String proMng) {
        StringBuilder content = new StringBuilder();
        content.append("各位同事，大家好！");
        content.append("<br/>");
        content.append("&nbsp;&nbsp;  " + reqName + "需求根据实施计划，将于" + date + "，完成" + reqPeriod);
        content.append("目前系统检测该阶段还未完成，距离完成时间还剩下2天，请" + proMng);
        content.append("同事撸起袖子加油干，确保需求按计划执行。");
        content.append("如有问题请及时向您的主管领导或主导部门寻求帮助，感谢您的辛苦付出！");
        return content.toString();
    }
}
