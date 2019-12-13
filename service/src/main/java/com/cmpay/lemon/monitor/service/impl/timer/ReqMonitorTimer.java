package com.cmpay.lemon.monitor.service.impl.timer;


import com.cmpay.lemon.common.utils.StringUtils;
import com.cmpay.lemon.monitor.bo.DemandBO;
import com.cmpay.lemon.monitor.bo.ProductionTimeBO;
import com.cmpay.lemon.monitor.entity.DemandDO;
import com.cmpay.lemon.monitor.entity.OperationApplicationDO;
import com.cmpay.lemon.monitor.entity.ProductionDO;
import com.cmpay.lemon.monitor.service.demand.ReqPlanService;
import com.cmpay.lemon.monitor.service.demand.ReqTaskService;
import com.cmpay.lemon.monitor.service.productTime.ProductTimeService;
import com.cmpay.lemon.monitor.service.production.OperationProductionService;
import com.cmpay.lemon.monitor.utils.DateUtil;
import com.cmpay.lemon.monitor.utils.SendExcelProductionVerificationIsNotTimely;
import com.cmpay.lemon.monitor.utils.wechatUtil.schedule.BoardcastScheduler;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ReqMonitorTimer {
	private static Logger logger = Logger.getLogger(ReqMonitorTimer.class);
	@Autowired
	private ReqTaskService reqTaskService;
	@Autowired
	private ReqPlanService reqPlanService;

	@Autowired
	private ProductTimeService productTimeService;
	@Autowired
	private OperationProductionService operationProductionService;
	@Autowired
	private BoardcastScheduler boardcastScheduler;

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
	
	/**
	 *  每周一更新投产时间
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
		boardcastScheduler.pushTimeOutWarning("投产时间周定时变更");
	}


	/*
	 *投产不及时验证清单发送企业微信
	 *每天中午12点执行
	 * */
	@Scheduled(cron = "1 0 12 * * ?")
	public void productionVerificationIsNotTimely() {
		//项目启动日期开始当天计算
		String date="2019-12-13";
		//获得投产验证不及时清单
		List<ProductionDO> productionDOList = operationProductionService.getProductionVerificationIsNotTimely(date);
		//获得系统录入验证不及时清单
		List<OperationApplicationDO> operationApplicationDOList = operationProductionService.getSystemEntryVerificationIsNotTimelyList(date);
		Map<String, Integer> map = new HashMap<>();
		productionDOList.forEach(m->{
			String applicationDept = m.getApplicationDept();
			boolean exist = map.containsKey(applicationDept);
			if(exist){
				map.put(applicationDept,map.get(applicationDept)+1);
			}else{
				map.put(applicationDept,1);
			}
		});
		String body="投产验证不及时清单汇总"+"\n";
		for(Map.Entry<String, Integer> entry : map.entrySet()){
			String mapKey = entry.getKey();
			Integer mapValue = entry.getValue();
			body=body+ mapKey+":"+mapValue+"条"+"\n";
		}
		body=body+ "\n";
		if(!operationApplicationDOList.isEmpty()) {
			operationApplicationDOList.forEach(m -> {
				String applicationDept = m.getApplicationSector();
				boolean exist = map.containsKey(applicationDept);
				if (exist) {
					map.put(applicationDept, map.get(applicationDept) + 1);
				} else {
					map.put(applicationDept, 1);
				}
			});
			body = body + "操作录入不及时验证清单汇总" + "\n";
			for (Map.Entry<String, Integer> entry : map.entrySet()) {
				String mapKey = entry.getKey();
				Integer mapValue = entry.getValue();
				body = body + mapKey + ":" + mapValue + "条" +"\n";
			}
		}
		body=body+"详情如下";
		SendExcelProductionVerificationIsNotTimely sendExcelProductionVerificationIsNotTimely = new SendExcelProductionVerificationIsNotTimely();
		File file=null;
		try{
			String excel = "\\Unverified_List_" + DateUtil.date2String(new Date(), "yyyyMMdd") + ".xls";
			sendExcelProductionVerificationIsNotTimely.createExcel(excel, productionDOList, null, operationApplicationDOList);
			file=new File(excel);

		}catch (Exception e){
			e.printStackTrace();
		}
		boardcastScheduler.pushValidationNotTimelyChecklist(body,file);
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
				Map<String, String>  map = reqPlanService.getMailbox(reqTask.getReqInnerSeq());
				sendTo = map.get("proMemberEmail");
				copyTo = map.get("devpEmail") + map.get("testDevpEmail") + "xiao_hua@hisuntech.com;wujinyan@hisuntech.com;";
				System.err.println(sendTo);
				System.err.println(copyTo);
//				sendTo = "tu_yi@hisuntech.com";
//				copyTo = "wu_lr@hisuntech.com";
				//发送邮件
				reqPlanService.sendMail("tu_yi@hisuntech.com", "tu_yi@hisuntech.com", content.toString(), subject, null);
				//设置异常类型
				if (reqTask.getReqAbnorType().indexOf("01") != -1) {
					reqTask.setReqAbnorType("03,");
				}else {
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
				Map<String, String>  map = reqPlanService.getMailbox(reqTask.getReqInnerSeq());
				sendTo = map.get("proMemberEmail");
				copyTo = map.get("devpEmail") + map.get("testDevpEmail") + "xiao_hua@hisuntech.com;wujinyan@hisuntech.com;";
//				sendTo = "tu_yi@hisuntech.com";
//				copyTo = "wu_lr@hisuntech.com";
				//发送邮件
				reqPlanService.sendMail(sendTo, copyTo, content.toString(), subject, null);
				//设置异常类型
				if (reqTask.getReqAbnorType().indexOf("01") != -1) {
					reqTask.setReqAbnorType("04,");
				}else {
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
				Map<String, String>  map = reqPlanService.getMailbox(reqTask.getReqInnerSeq());
				sendTo = map.get("proMemberEmail");
				copyTo = map.get("devpEmail") + map.get("testDevpEmail") + "xiao_hua@hisuntech.com;wujinyan@hisuntech.com;";
//				sendTo = "tu_yi@hisuntech.com";
//				copyTo = "wu_lr@hisuntech.com";
				//发送邮件
				reqPlanService.sendMail(sendTo, copyTo, content.toString(), subject, null);
				//设置异常类型
				if (reqTask.getReqAbnorType().indexOf("01") != -1) {
					reqTask.setReqAbnorType("05,");
				}else {
					reqTask.setReqAbnorType(reqTask.getReqAbnorType() + "05,");
				}
				reqTaskService.updateReqAbnorType(reqTask);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("需求异常状态监控失败："+e.getMessage());
		}
	}


	//todo
	// 需求预警 ：“需求定稿时间”或“UAT更新时间”或“测试完成时间”或“预投产时间”、“投产时间”小于1天，系统每日发送微信推送。
	// 日终，根据系统当前时间离关键里程碑计划完成时间剩余天数小于等于1且进度滞后于里程碑阶段的需求，每日上午8点-8点30分发送需求预警邮件通知
	//
	@Scheduled(cron = "0 0 12 * * ?")
	public void progressAlertPush() {
		DemandDO demandDO = new DemandDO();
		String month = DateUtil.date2String(new Date(), "yyyy-MM");
		demandDO.setReqImplMon(month);
		List<DemandBO> demandBOlist = reqPlanService.getNormalExecutionDemand(demandDO);
		demandBOlist.forEach(m->{
			if(StringUtils.isEmpty(m.getPreCurPeriod())||StringUtils.isEmpty(m.getDevpLeadDept())){
				return;
			}else {
				int PreCurPeriod = Integer.parseInt(m.getPreCurPeriod());
				//需求定稿
				if (PreCurPeriod<30) {
					if(StringUtils.isEmpty(m.getPrdFinshTm())) {
						return;
					}else{
						int date =dateDifference(DateUtil.date2String(new Date(), "yyyy-MM-dd"), m.getPrdFinshTm());
						if(date==1){
							String body ="主导部门:"+m.getDevpLeadDept()+"\n"+"需求编号:"+m.getReqNo() +"\n"+ m.getReqNm()+"\n"+"计划于明日达到阶段性目标【需求定稿】，请按时更新状态或提交文档，若计划有变，请及时变更";
							boardcastScheduler.pushTimeOutWarning(body);
						}
					}

				}
				//UAT更新
				else if (PreCurPeriod<120) {
					if(StringUtils.isEmpty(m.getUatUpdateTm())) {
						return;
					}else{
						int date = dateDifference(DateUtil.date2String(new Date(), "yyyy-MM-dd"), m.getUatUpdateTm());
						if(date==1){
							String body ="主导部门:"+m.getDevpLeadDept()+"\n"+"需求编号:"+m.getReqNo() +"\n"+ m.getReqNm()+"\n"+"计划于明日达到阶段性目标【UAT更新】，请按时更新状态或提交文档，若计划有变，请及时变更";
							boardcastScheduler.pushTimeOutWarning(body);
						}
					}

				}
				//测试完成
				else if (PreCurPeriod<130) {
					if(StringUtils.isEmpty(m.getTestFinshTm())) {
						return;
					}else{
						int date =dateDifference(DateUtil.date2String(new Date(), "yyyy-MM-dd"),	m.getTestFinshTm());
						if(date==1){
							String body ="主导部门:"+m.getDevpLeadDept()+"\n"+"需求编号:"+m.getReqNo() +"\n"+ m.getReqNm()+"\n"+"计划于明日达到阶段性目标【UAT测试完成】，请按时更新状态或提交文档，若计划有变，请及时变更";
							boardcastScheduler.pushTimeOutWarning(body);
						}
					}

				}
				//预投产
				else if (PreCurPeriod<160) {
					if(StringUtils.isEmpty(m.getPreTm())) {
						return;
					}else{
						int date =dateDifference(DateUtil.date2String(new Date(), "yyyy-MM-dd"),	m.getPreTm());
						if(date==1){
							String body ="主导部门:"+m.getDevpLeadDept()+"\n"+"需求编号:"+m.getReqNo() +"\n"+ m.getReqNm()+"\n"+"计划于明日达到阶段性目标【预投产】，请按时更新状态或提交文档，若计划有变，请及时变更";
							boardcastScheduler.pushTimeOutWarning(body);
						}
					}
				}
				//投产时间
				else if (PreCurPeriod<180) {
					if(StringUtils.isEmpty(m.getExpPrdReleaseTm())) {
						return;
					}else{
						int date =dateDifference(DateUtil.date2String(new Date(), "yyyy-MM-dd"),	m.getExpPrdReleaseTm());
						if(date==1){
							String body ="主导部门:"+m.getDevpLeadDept()+"\n"+"需求编号:"+m.getReqNo() +"\n"+ m.getReqNm()+"\n"+"计划于明日达到阶段性目标【投产】，请按时更新状态或提交文档，若计划有变，请及时变更";
							boardcastScheduler.pushTimeOutWarning(body);
						}
					}
				}
			}
		});

	}

	private int  dateDifference(String date1,String date2) {
		int betweenDate =0;
		try {
			//设置转换的日期格式
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			//开始时间
			Date startDate = sdf.parse(date1);
			//结束时间
			Date endDate = sdf.parse(date2);
			//得到相差的天数 betweenDate
			 betweenDate = (int)(endDate.getTime() - startDate.getTime()) / (60 * 60 * 24 * 1000);
			//打印控制台相差的天数
		}catch (Exception e){
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
