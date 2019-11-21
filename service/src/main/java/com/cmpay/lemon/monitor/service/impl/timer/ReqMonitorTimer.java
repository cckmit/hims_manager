package com.cmpay.lemon.monitor.service.impl.timer;


import com.cmpay.lemon.monitor.bo.DemandBO;
import com.cmpay.lemon.monitor.service.demand.ReqPlanService;
import com.cmpay.lemon.monitor.service.demand.ReqTaskService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ReqMonitorTimer {
	private static Logger logger = Logger.getLogger(ReqMonitorTimer.class);
	@Autowired
	private ReqTaskService reqTaskService;
	@Autowired
	private ReqPlanService reqPlanService;
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
//	public void updateProductTime() {
//		Calendar c = Calendar.getInstance();
//		SimpleDateFormat simpleDateFormatMonth = new SimpleDateFormat("yyyy-MM-dd");
//
//		ProductTimeBean productTimeBean = new ProductTimeBean();
//
//		//本周投产日
//		c.add(Calendar.DATE, 2);
//		String day = simpleDateFormatMonth.format(c.getTime());
//		productTimeBean.setId(Integer.valueOf(3));
//		productTimeBean.setTime(day);
//		productTimeServiceMgr.updateProductTime(productTimeBean);
//		//下周预投产时间
//		c.add(Calendar.DATE, 7);
//		day = simpleDateFormatMonth.format(c.getTime());
//		productTimeBean.setId(Integer.valueOf(4));
//		productTimeBean.setTime(day);
//		productTimeServiceMgr.updateProductTime(productTimeBean);
//	}
	

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
//				sendTo = "tu_yi@hisuntech.com";
//				copyTo = "wu_lr@hisuntech.com";
				//发送邮件
				reqPlanService.sendMail(sendTo, copyTo, content.toString(), subject, null);
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
//			reqPlanService.sendMail(sendTo, null, content, subject, null);
//		}
//		List<DemandBO> uatUpdateWarnList = reqTaskService.getUatUpdateWarn();
//		for (int i = 0; i < uatUpdateWarnList.size(); i++) {
//			DemandBO reqTask = uatUpdateWarnList.get(i);
//			String name = reqTask.getReqNo() + reqTask.getReqNm();
//			String subject = name + "需求实施进度预警通知";
//			String content = dailyProcessContent(name, reqTask.getUatUpdateTm(), "uat更新", reqTask.getProjectMng());
//			reqPlanService.sendMail(sendTo, null, content.toString(), subject, null);
//		}
//		List<DemandBO> testFinishWarnList = reqTaskService.getTestFnishWarn();
//		for (int i = 0; i < testFinishWarnList.size(); i++) {
//			DemandBO reqTask = testFinishWarnList.get(i);
//			String name = reqTask.getReqNo() + reqTask.getReqNm();
//			String subject = name + "需求实施进度预警通知";
//			String content = dailyProcessContent(name, reqTask.getTestFinshTm(), "uat测试", reqTask.getTestEng());
//			reqPlanService.sendMail(sendTo, null, content.toString(), subject, null);
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
