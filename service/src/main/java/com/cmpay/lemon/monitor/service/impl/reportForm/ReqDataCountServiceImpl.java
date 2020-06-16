package com.cmpay.lemon.monitor.service.impl.reportForm;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.cmpay.lemon.common.exception.BusinessException;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.common.utils.JudgeUtils;
import com.cmpay.lemon.framework.page.PageInfo;
import com.cmpay.lemon.framework.utils.PageUtils;
import com.cmpay.lemon.monitor.bo.*;
import com.cmpay.lemon.monitor.dao.IDemandExtDao;
import com.cmpay.lemon.monitor.dao.IReqDataCountDao;
import com.cmpay.lemon.monitor.dao.IWorkingHoursDao;
import com.cmpay.lemon.monitor.entity.*;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.demand.ReqPlanService;
import com.cmpay.lemon.monitor.service.impl.demand.ReqPlanServiceImpl;
import com.cmpay.lemon.monitor.service.production.OperationProductionService;
import com.cmpay.lemon.monitor.service.reportForm.ReqDataCountService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import com.cmpay.lemon.monitor.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@Transactional(rollbackFor = Exception.class)
@Service
public class ReqDataCountServiceImpl implements ReqDataCountService {
	//30 需求状态为暂停
	private static final String REQSUSPEND ="30";
	//40 需求状态为取消
	private static final String REQCANCEL ="40";
	// 30 需求定稿
	private static final int REQCONFIRM = 30;
	// 50 技术方案定稿
	private static final int TECHDOCCONFIRM = 50;
	// 70 测试用例定稿
	private static final int TESTCASECONFIRM = 70;
	// 110 完成SIT测试
	private static final int FINISHSITTEST = 110;
	// 120 UAT版本更新
	private static final int UPDATEUAT = 120;
	// 140 完成UAT测试
	private static final int FINISHUATTEST = 140;
	// 160 完成预投产
	private static final int FINISHPRETEST = 160;
	// 180 完成产品发布
	private static final int FINISHPRD = 180;

    // 30 需求定稿
    private static final String REQCONFIRM1 = "30";
    // 50 技术方案定稿
    private static final String TECHDOCCONFIRM1 = "50";
    // 110 完成SIT测试
    private static final String FINISHSITTEST1 = "110";
    // 140 完成UAT测试
    private static final String FINISHUATTEST1 = "140";
    // 160 完成预投产
    private static final String FINISHPRETEST1 = "160";
    // 180 完成产品发布
    private static final String FINISHPRD1= "180";

	private static final Logger LOGGER = LoggerFactory.getLogger(ReqPlanServiceImpl.class);
	@Autowired
	private IDemandExtDao demandDao;
	@Autowired
	private IReqDataCountDao reqDataCountDao;
	@Autowired
	private IWorkingHoursDao iWorkingHoursDao;

	@Autowired
	private ReqPlanService reqPlanService;

	@Autowired
	private OperationProductionService operationProductionService;

	/**
	 * 本月需求完成情况：进行中、已投产需求数
	 */
	@Override
	public Map getProg(String report_start_mon ,String report_end_mon) {
		Map DataMap = new TreeMap();

		ReqDataCountDO reqDataCountDO = reqDataCountDao.getProg(report_start_mon, report_end_mon);

		if ((reqDataCountDO == null)) {
			return DataMap;
		}

		DataMap.put("已投产", reqDataCountDO.getReqOper());
		DataMap.put("进行中", reqDataCountDO.getReqIng());

		return DataMap;
	}

	/**
	 * 本月需求进度情况：需求阶段、开发阶段、测试阶段、预投产阶段、投产上线阶段
	 */
	@Override
	public Map getProgDetl( String report_start_mon, String report_end_mon) {
		Map DataMap = new TreeMap();

		ReqDataCountDO reqDataCountD0 = reqDataCountDao.getProgDetl(report_start_mon, report_end_mon);

		if ((reqDataCountD0 == null)) {
			return DataMap;
		}

		DataMap.put("需求阶段", reqDataCountD0.getReqPrd());
		DataMap.put("开发阶段", reqDataCountD0.getReqDevp());
		DataMap.put("测试阶段", reqDataCountD0.getReqTest());
		DataMap.put("预投产阶段", reqDataCountD0.getReqPre());
		DataMap.put("已投产", reqDataCountD0.getReqOper());

		return DataMap;
	}

	/**
	 * 按部门统计需求实施异常图表
	 */
	@Override
	public Map getAbnoByDept(String report_start_mon, String report_end_mon) {
		Map DataMap = new TreeMap();

		List<ReqDataCountDO> list = reqDataCountDao.getAbnoByDept(report_start_mon, report_end_mon);

		if ((list == null)) {
			return DataMap;
		}

		for (int i = 0; i < list.size(); i++) {
			DataMap.put(list.get(i).getDevpLeadDept(), list.get(i).getReqUnusual());
		}

		return DataMap;
	}

	/**
	 * 按产品线统计需求实施异常图表
	 */
	@Override
	public Map getAbnoByLine(String report_start_mon, String report_end_mon) {
		Map DataMap = new TreeMap();

		List<ReqDataCountDO> list = reqDataCountDao.getAbnoByLine(report_start_mon, report_end_mon);

		if ((list == null)) {
			return DataMap;
		}

		for (int i = 0; i < list.size(); i++) {
			DataMap.put(list.get(i).getReqPrdLine(), list.get(i).getReqUnusual());
		}

		return DataMap;
	}

	/**
	 * 各部门需求完成情况：进行中、已投产需求数（饼图）
	 */
	@Override
	public Map getProgByDept(String report_start_mon, String report_end_mon) {
		Map DataMap = new TreeMap();

		List<ReqDataCountDO> list = reqDataCountDao.getProgByDept(report_start_mon, report_end_mon);

		if ((list == null)) {
			return DataMap;
		}

		for (int i = 0; i < list.size(); i++) {
			Map Data = new TreeMap();
			Data.put("已投产", list.get(i).getReqOper());
			Data.put("进行中", list.get(i).getReqIng());

			DataMap.put(list.get(i).getDevpLeadDept(), Data);
		}

		return DataMap;
	}

	/**
	 * 各部门需求进度情况：需求阶段、开发阶段、测试阶段、预投产阶段、投产上线阶段（饼图）
	 */
	@Override
	public Map getProgDetlByDept(String report_start_mon, String report_end_mon) {
		Map DataMap = new TreeMap();

		List<ReqDataCountDO> list = reqDataCountDao.getProgDetlByDept(report_start_mon, report_end_mon);

		if ((list == null)) {
			return DataMap;
		}

		for (int i = 0; i < list.size(); i++) {
			Map Data = new TreeMap();
			Data.put("需求阶段", list.get(i).getReqPrd());
			Data.put("开发阶段", list.get(i).getReqDevp());
			Data.put("测试阶段", list.get(i).getReqTest());
			Data.put("预投产阶段", list.get(i).getReqPre());
			Data.put("已投产", list.get(i).getReqOper());

			DataMap.put(list.get(i).getReqPrdLine(), Data);
		}


		return DataMap;
	}


	/**
	 * map转json
	 * @param DataMap
	 * @return jsonStr
	 */
	public String strToJson(Map DataMap){
		String bigJsonStr = "";

		Set treeSet = new TreeSet();
		treeSet = DataMap.keySet();

		String smallJson = "";

		StringBuilder bigJson = new StringBuilder();

		for (Iterator localIterator = treeSet.iterator(); localIterator.hasNext(); ) { String amtSrc = (String)localIterator.next();

			smallJson = new StringBuilder().append("{\"value\":\"").append((String)DataMap.get(amtSrc)).append("\",\"name\":\"").append(amtSrc).append("\"},").toString();
			bigJson.append(smallJson);
		}

		bigJsonStr = bigJson.toString();

		bigJsonStr = new StringBuilder().append("{\"LineData\":[").append(bigJsonStr.substring(0, bigJsonStr.length() - 1)).append("]}").toString();

		return bigJsonStr;

	}

	@Override
	public List<ReqDataCountBO> getImpl(String ReqImplMon) {
		List<ReqDataCountBO> reqDataCountBOS = new LinkedList<>();
		List<ReqDataCountDO> impl = reqDataCountDao.getImpl(ReqImplMon);
		impl.forEach(m->
				reqDataCountBOS.add(BeanUtils.copyPropertiesReturnDest(new ReqDataCountBO(), m))
		);

		return reqDataCountBOS;
	}

	@Override
	public List<ReqDataCountBO> getImplByDept(String reqImplMon) {
		List<ReqDataCountBO> reqDataCountBOS = new LinkedList<>();
		reqDataCountDao.getImplByDept(reqImplMon).forEach(m->
				reqDataCountBOS.add(BeanUtils.copyPropertiesReturnDest(new ReqDataCountBO(), m))
		);
		return reqDataCountBOS;
	}

	@Override
	public List<ReqDataCountBO> getComp(String reqImplMon) {
		List<ReqDataCountBO> reqDataCountBOS = new LinkedList<>();
		List<ReqDataCountDO> comp = reqDataCountDao.getComp(reqImplMon);
		comp.forEach(m->
				reqDataCountBOS.add(BeanUtils.copyPropertiesReturnDest(new ReqDataCountBO(), m))
		);
		return reqDataCountBOS;
	}

	@Override
	public List<ReqDataCountBO> getCompByDept(String reqImplMon) {
		List<ReqDataCountBO> reqDataCountBOS = new LinkedList<>();
		reqDataCountDao.getCompByDept(reqImplMon).forEach(m->
				reqDataCountBOS.add(BeanUtils.copyPropertiesReturnDest(new ReqDataCountBO(), m))
		);

		return reqDataCountBOS;
	}

	public List<ReqDataCountBO> getCompByDept2(String reqImplMon) {
		List<ReqDataCountBO> reqDataCountBOS = new LinkedList<>();
		reqDataCountDao.getCompByDept2(reqImplMon).forEach(m->
				reqDataCountBOS.add(BeanUtils.copyPropertiesReturnDest(new ReqDataCountBO(), m))
		);

		return reqDataCountBOS;
	}
	@Override
	public List<ReqDataCountBO> getReqSts(String reqImplMon) {
		List<ReqDataCountBO> reqDataCountBOS = new LinkedList<>();
		List<ReqDataCountDO> impl = reqDataCountDao.getReqSts(reqImplMon);
		impl.forEach(m->
				reqDataCountBOS.add(BeanUtils.copyPropertiesReturnDest(new ReqDataCountBO(), m))
		);
		return reqDataCountBOS;
	}

	@Override
	public List<ScheduleBO> getProduction(String reqImplMon) {
		List<ScheduleBO> reqDataCountBOS = new LinkedList<>();
		List<ScheduleDO> impl = reqDataCountDao.getProduction(reqImplMon);
		impl.forEach(m->
				reqDataCountBOS.add(BeanUtils.copyPropertiesReturnDest(new ScheduleBO(), m))
		);
		return reqDataCountBOS;
	}

	@Override
	public List<DemandBO> getReportForm6(String reqImplMon,String devpLeadDept,String productMng) {
		List<DemandBO> reqDataCountBOS = new LinkedList<>();
		ReqDateCountDO reqDateCountDO = new ReqDateCountDO();
		reqDateCountDO.setReqImplMon(reqImplMon);
		reqDateCountDO.setDevpLeadDept(devpLeadDept);
		reqDateCountDO.setProductMng(productMng);
		List<DemandDO> impl = reqDataCountDao.getReportForm6(reqDateCountDO);
		impl.forEach(m->
				reqDataCountBOS.add(BeanUtils.copyPropertiesReturnDest(new DemandBO(), m))
		);

        for(int i =0;i<reqDataCountBOS.size();i++){
            int sum = 0;
            //如果当前阶段为已完成，则需要上次6个文档
			System.err.println(reqDataCountBOS.get(i));
            if(FINISHPRD1.equals(reqDataCountBOS.get(i).getPreCurPeriod2())){
				if(reqDataCountBOS.get(i).getActPrdUploadTm()==null || reqDataCountBOS.get(i).getActPrdUploadTm()==""){
					sum=sum+1;
				}
				if(reqDataCountBOS.get(i).getActWorkloadUploadTm()==null || reqDataCountBOS.get(i).getActWorkloadUploadTm()==""){
					sum=sum+1;
				}
				if(reqDataCountBOS.get(i).getActSitUploadTm()==null || reqDataCountBOS.get(i).getActSitUploadTm()==""){
					sum=sum+1;
				}
				if(reqDataCountBOS.get(i).getActUatUploadTm()==null || reqDataCountBOS.get(i).getActUatUploadTm()==""){
					sum=sum+1;
				}
				if(reqDataCountBOS.get(i).getActPreUploadTm()==null || reqDataCountBOS.get(i).getActPreUploadTm()==""){
					sum=sum+1;
				}
				if(reqDataCountBOS.get(i).getActProductionUploadTm()==null || reqDataCountBOS.get(i).getActProductionUploadTm()==""){
					sum=sum+1;
				}
                System.err.println(1111+"===="+sum);
			}
			//预投产
			else if(FINISHPRETEST1.compareTo(reqDataCountBOS.get(i).getPreCurPeriod2())<=0 &&FINISHPRD1.compareTo(reqDataCountBOS.get(i).getPreCurPeriod2())>0){
				if(reqDataCountBOS.get(i).getActPrdUploadTm()==null || reqDataCountBOS.get(i).getActPrdUploadTm()==""){
                    sum=sum+1;
				}
				if(reqDataCountBOS.get(i).getActWorkloadUploadTm()==null || reqDataCountBOS.get(i).getActWorkloadUploadTm()==""){
                    sum=sum+1;
				}
				if(reqDataCountBOS.get(i).getActSitUploadTm()==null || reqDataCountBOS.get(i).getActSitUploadTm()==""){
                    sum=sum+1;
				}
				if(reqDataCountBOS.get(i).getActUatUploadTm()==null || reqDataCountBOS.get(i).getActUatUploadTm()==""){
                    sum=sum+1;
				}
				if(reqDataCountBOS.get(i).getActPreUploadTm()==null || reqDataCountBOS.get(i).getActPreUploadTm()==""){
                    sum=sum+1;
				}
                System.err.println(22222+"===="+sum);
			}
			// uat
			else if(FINISHUATTEST1.compareTo(reqDataCountBOS.get(i).getPreCurPeriod2())<=0&&FINISHPRETEST1.compareTo(reqDataCountBOS.get(i).getPreCurPeriod2())>0){
				if(reqDataCountBOS.get(i).getActPrdUploadTm()==null || reqDataCountBOS.get(i).getActPrdUploadTm()==""){
                    sum=sum+1;
				}
				if(reqDataCountBOS.get(i).getActWorkloadUploadTm()==null || reqDataCountBOS.get(i).getActWorkloadUploadTm()==""){
                    sum=sum+1;
				}
				if(reqDataCountBOS.get(i).getActSitUploadTm()==null || reqDataCountBOS.get(i).getActSitUploadTm()==""){
                    sum=sum+1;
				}
				if(reqDataCountBOS.get(i).getActUatUploadTm()==null || reqDataCountBOS.get(i).getActUatUploadTm()==""){
                    sum=sum+1;
				}
                System.err.println(3333+"===="+sum);
			}
			// sit
			else if(FINISHSITTEST1.compareTo(reqDataCountBOS.get(i).getPreCurPeriod2())<=0&&FINISHUATTEST1.compareTo(reqDataCountBOS.get(i).getPreCurPeriod2())>0){
				if(reqDataCountBOS.get(i).getActPrdUploadTm()==null || reqDataCountBOS.get(i).getActPrdUploadTm()==""){
                    sum=sum+1;
				}
				if(reqDataCountBOS.get(i).getActWorkloadUploadTm()==null || reqDataCountBOS.get(i).getActWorkloadUploadTm()==""){
                    sum=sum+1;
				}
				if(reqDataCountBOS.get(i).getActSitUploadTm()==null || reqDataCountBOS.get(i).getActSitUploadTm()==""){
                    sum=sum+1;
				}
                System.err.println(4444+"===="+sum);
			}
			// 技术文档
			else if(TECHDOCCONFIRM1.compareTo(reqDataCountBOS.get(i).getPreCurPeriod2())<=0&&FINISHSITTEST1.compareTo(reqDataCountBOS.get(i).getPreCurPeriod2())>0){
				if(reqDataCountBOS.get(i).getActPrdUploadTm()==null || reqDataCountBOS.get(i).getActPrdUploadTm()==""){
                    sum=sum+1;
				}
				if(reqDataCountBOS.get(i).getActWorkloadUploadTm()==null || reqDataCountBOS.get(i).getActWorkloadUploadTm()==""){
                    sum=sum+1;
				}
                System.err.println(5555+"===="+sum);
			}
			// 需求定搞
			else if(REQCONFIRM1.compareTo(reqDataCountBOS.get(i).getPreCurPeriod2())<=0){
				if(reqDataCountBOS.get(i).getActPrdUploadTm()==null || reqDataCountBOS.get(i).getActPrdUploadTm()==""){
                    sum=sum+1;
				}
                System.err.println(6666+"===="+sum);
			}
            System.err.println(sum);
            reqDataCountBOS.get(i).setNoUpload(sum);
        }
		return reqDataCountBOS;
	}
	@Override
	public List<WorkingHoursBO> getReportForm7(String devpLeadDept,String date,String date1,String date2){
		System.err.println(devpLeadDept+"++++++"+date+"++++++"+date1+"++++++"+date2);
		List<WorkingHoursBO> workingHoursBOS = new LinkedList<>();
		List<WorkingHoursDO> impl = null;
		WorkingHoursDO workingHoursDO = new WorkingHoursDO();
		workingHoursDO.setDevpLeadDept(devpLeadDept);
		if(StringUtils.isBlank(date)&&StringUtils.isBlank(date1)&&StringUtils.isBlank(date2)){
			MsgEnum.ERROR_CUSTOM.setMsgInfo("");
			MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如日期、周、月!");
			BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
		}
		if(StringUtils.isNotBlank(date)&&StringUtils.isBlank(date1)&&StringUtils.isBlank(date2)){
			workingHoursDO.setSelectTime(date);
			impl = iWorkingHoursDao.findSum(workingHoursDO);
		}
		if(StringUtils.isNotBlank(date1)&&StringUtils.isBlank(date)&&StringUtils.isBlank(date2)){
			workingHoursDO.setSelectTime(date1);
			impl = iWorkingHoursDao.findWeekSum(workingHoursDO);
		}
		if(StringUtils.isNotBlank(date2)&&StringUtils.isBlank(date)&&StringUtils.isBlank(date1)){
			workingHoursDO.setSelectTime(date2);
			impl = iWorkingHoursDao.findMonthSum(workingHoursDO);
		}

		impl.forEach(m->
				workingHoursBOS.add(BeanUtils.copyPropertiesReturnDest(new WorkingHoursBO(), m))
		);
		return workingHoursBOS;
	}

	@Override
	public List<WorkingHoursBO> getReportForm7B(String devpLeadDept,String date,String date1,String date2){
		List<WorkingHoursBO> workingHoursBOS = new LinkedList<>();
		List<WorkingHoursDO> impl = null;
		WorkingHoursDO workingHoursDO = new WorkingHoursDO();
		workingHoursDO.setDevpLeadDept(devpLeadDept);
		if(StringUtils.isBlank(date)&&StringUtils.isBlank(date1)&&StringUtils.isBlank(date2)){
			MsgEnum.ERROR_CUSTOM.setMsgInfo("");
			MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如日期、周、月!");
			BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
		}
		if(StringUtils.isNotBlank(date)&&StringUtils.isBlank(date1)&&StringUtils.isBlank(date2)){
			workingHoursDO.setSelectTime(date);
			impl = iWorkingHoursDao.findSumB(workingHoursDO);
		}
		if(StringUtils.isNotBlank(date1)&&StringUtils.isBlank(date)&&StringUtils.isBlank(date2)){
			workingHoursDO.setSelectTime(date1);
			impl = iWorkingHoursDao.findWeekSumB(workingHoursDO);
		}
		if(StringUtils.isNotBlank(date2)&&StringUtils.isBlank(date)&&StringUtils.isBlank(date1)){
			workingHoursDO.setSelectTime(date2);
			impl = iWorkingHoursDao.findMonthSumB(workingHoursDO);
		}

		impl.forEach(m->
				workingHoursBOS.add(BeanUtils.copyPropertiesReturnDest(new WorkingHoursBO(), m))
		);
		return workingHoursBOS;
	}
	@Override
	public List<String> getReportForm8(String date){
		System.err.println(date+"++++++"+date);
		List<String> workingHoursBOS = new LinkedList<>();
		List<DepartmentWorkDO> impl = iWorkingHoursDao.findDeptHours(date);
		impl.forEach(m->
				workingHoursBOS.add(m.getWorkHoursToString())
		);
		return workingHoursBOS;
	};
	@Override
	public List<WorkingHoursBO> findList(String displayName,String date,String date1,String date2){
		System.err.println(displayName+"++++++"+date+"++++++"+date1+"++++++"+date2);
		List<WorkingHoursBO> workingHoursBOS = new LinkedList<>();
		List<WorkingHoursDO> impl =null;
		WorkingHoursDO workingHoursDO = new WorkingHoursDO();
		workingHoursDO.setDisplayname(displayName);
		if(StringUtils.isBlank(date)&&StringUtils.isBlank(date1)&&StringUtils.isBlank(date2)){
			MsgEnum.ERROR_CUSTOM.setMsgInfo("");
			MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如日期、周、月!");
			BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
		}
		if(StringUtils.isNotBlank(date)&&StringUtils.isBlank(date1)&&StringUtils.isBlank(date2)){
			workingHoursDO.setSelectTime(date);
			impl = iWorkingHoursDao.findList(workingHoursDO);
		}
		if(StringUtils.isNotBlank(date1)&&StringUtils.isBlank(date)&&StringUtils.isBlank(date2)){
			workingHoursDO.setSelectTime(date1);
			impl = iWorkingHoursDao.findListWeek(workingHoursDO);
		}
		if(StringUtils.isNotBlank(date2)&&StringUtils.isBlank(date)&&StringUtils.isBlank(date1)){
			workingHoursDO.setSelectTime(date2);
			impl = iWorkingHoursDao.findListMonth(workingHoursDO);
		}
		impl.forEach(m->
				workingHoursBOS.add(BeanUtils.copyPropertiesReturnDest(new WorkingHoursBO(), m))
		);
		return workingHoursBOS;
	}

	@Override
	public List<ReqDataCountBO> getStageByJd(String reqImplMon) {
		List<ReqDataCountBO> reqDataCountBOS = new LinkedList<>();
		reqDataCountDao.getStageByJd(reqImplMon).forEach(m->
				reqDataCountBOS.add(BeanUtils.copyPropertiesReturnDest(new ReqDataCountBO(), m))
		);
		return reqDataCountBOS;
	}

	@Override
	public List<ReqDataCountBO> selectDetl(ReqMngBO vo) {
		List<ReqDataCountDO> lst = reqDataCountDao.selectDetl(BeanUtils.copyPropertiesReturnDest(new ReqMngDO(), vo));
		String time= DateUtil.date2String(new Date(), "yyyy-MM-dd");
		for (int i =0; i < lst.size();i++){
			String reqAbnorType=lst.get(i).getReqUnusual();
			DemandBO demand = reqPlanService.findById(lst.get(i).getReqInnerSeq());
			String reqAbnorTypeAll = "";
			//当需求定稿时间、uat更新时间、测试完成时间、需求当前阶段、需求状态都不为空的时候，执行进度实时显示逻辑。
			if (StringUtils.isNotBlank(demand.getPrdFinshTm()) && StringUtils.isNotBlank(demand.getUatUpdateTm()) && StringUtils.isNotBlank(demand.getTestFinshTm()) && StringUtils.isNotBlank(demand.getPreCurPeriod()) && StringUtils.isNotBlank(demand.getReqSts())) {
				//当前时间大于预计时间，并且所处阶段小于30,并且需求状态不为暂停或取消（30，40）,则该需求进度异常
				if (time.compareTo(demand.getPrdFinshTm()) > 0 && Integer.parseInt(demand.getPreCurPeriod()) < 30 && "30".compareTo(demand.getReqSts()) != 0 && "40".compareTo(demand.getReqSts()) != 0) {
					reqAbnorTypeAll += "需求进度滞后,";
				}
				if (time.compareTo(demand.getUatUpdateTm()) > 0 && Integer.parseInt(demand.getPreCurPeriod()) >= 30 && Integer.parseInt(demand.getPreCurPeriod()) < 120 && "30".compareTo(demand.getReqSts()) != 0 && "40".compareTo(demand.getReqSts()) != 0) {
					reqAbnorTypeAll += "开发进度滞后,";
				}
				if (time.compareTo(demand.getTestFinshTm()) > 0 && Integer.parseInt(demand.getPreCurPeriod()) >= 120 && Integer.parseInt(demand.getPreCurPeriod()) < 140 && "30".compareTo(demand.getReqSts()) != 0 && "40".compareTo(demand.getReqSts()) != 0) {
					reqAbnorTypeAll += "测试进度滞后";
				}
				if (StringUtils.isBlank(reqAbnorTypeAll)) {
					reqAbnorTypeAll += "正常";
				}
			} else if(reqAbnorType.indexOf("01") != -1){
				lst.get(i).setReqUnusual("正常");
				continue;
			}else{
				if (reqAbnorType.indexOf("03") != -1) {
					reqAbnorTypeAll += "需求进度滞后,";
				}
				if (reqAbnorType.indexOf("04") != -1) {
					reqAbnorTypeAll += "开发进度滞后,";
				}
				if (reqAbnorType.indexOf("05") != -1) {
					reqAbnorTypeAll += "测试进度滞后";
				}
			}
			if (reqAbnorTypeAll.length() >= 1 && ',' == reqAbnorTypeAll.charAt(reqAbnorTypeAll.length()-1)){
				reqAbnorTypeAll = reqAbnorTypeAll.substring(0,reqAbnorTypeAll.length()-1);
				lst.get(i).setReqUnusual(reqAbnorTypeAll);
			}else{
				lst.get(i).setReqUnusual(reqAbnorTypeAll);
			}
		}
		List<ReqDataCountBO> reqDataCountBOS = new LinkedList<>();
		lst.forEach(m->
				reqDataCountBOS.add(BeanUtils.copyPropertiesReturnDest(new ReqDataCountBO(), m))
		);
		return reqDataCountBOS;
	}

	@Override
	public Integer findNumberByCondition(ReqMngBO vo) throws Exception {
		return reqDataCountDao.findNumberByCondition(BeanUtils.copyPropertiesReturnDest(new ReqMngDO(), vo));
	}

	@Override
	public Map selectByCenter(ReqMngBO vo) {
		Map reMap = new TreeMap();
		Map DataMap = new TreeMap();

		ReqDataCountDO reqDataCount = reqDataCountDao.selectByCenter(BeanUtils.copyPropertiesReturnDest(new ReqMngDO(), vo));

		if ((reqDataCount == null)) {
			return reMap;
		}

		DataMap.put("需求阶段", reqDataCount.getReqPrd());
		DataMap.put("开发阶段", reqDataCount.getReqDevp());
		DataMap.put("测试阶段", reqDataCount.getReqTest());
		DataMap.put("预投产阶段", reqDataCount.getReqPre());
		DataMap.put("已投产", reqDataCount.getReqOper());
		DataMap.put("需求异常", reqDataCount.getReqAbnormal());
		DataMap.put("取消需求", reqDataCount.getReqCancel());
		DataMap.put("暂停需求", reqDataCount.getReqSuspend());

		reMap.put("DataMap", DataMap);
		reMap.put("totle", reqDataCount.getTotal());

		return reMap;
	}

	@Override
	public Map selectByProduct(ReqMngBO vo) {
		Map reMap = new TreeMap();
		Map DataMap = new TreeMap();

		ReqDataCountDO reqDataCount = reqDataCountDao.selectByProduct(BeanUtils.copyPropertiesReturnDest(new ReqMngDO(), vo));

		if ((reqDataCount == null)) {
			return reMap;
		}

		DataMap.put("需求分析", reqDataCount.getReqPrd());
		DataMap.put("需求撰写", reqDataCount.getReqDevp());
		DataMap.put("需求定稿", reqDataCount.getReqTest());
		DataMap.put("预投产测试", reqDataCount.getReqPre());
		DataMap.put("需求进度异常", reqDataCount.getReqAbnormal());
		DataMap.put("取消需求", reqDataCount.getReqCancel());
		DataMap.put("暂停需求", reqDataCount.getReqSuspend());

		reMap.put("DataMap", DataMap);
		reMap.put("totle", reqDataCount.getTotal());

		return reMap;
	}

	@Override
	public Map selectByTest(ReqMngBO vo) {
		Map reMap = new TreeMap();
		Map DataMap = new TreeMap();

		ReqDataCountDO reqDataCount = reqDataCountDao.selectByTest(BeanUtils.copyPropertiesReturnDest(new ReqMngDO(), vo));

		if ((reqDataCount == null)) {
			return reMap;
		}

		DataMap.put("未启动测试", reqDataCount.getReqPrd());
		DataMap.put("测试进行中", reqDataCount.getReqDevp());
		DataMap.put("已完成测试", reqDataCount.getReqTest());
		DataMap.put("测试异常", reqDataCount.getReqAbnormal());
		DataMap.put("取消需求", reqDataCount.getReqCancel());
		DataMap.put("暂停需求", reqDataCount.getReqSuspend());

		reMap.put("DataMap", DataMap);
		reMap.put("totle", reqDataCount.getTotal());

		return reMap;
	}

	@Override
	public Map selectByEng(ReqMngBO vo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void downloadDemandTypeStatistics(String month, HttpServletResponse response) {
		DemandTypeStatisticsBO demandTypeStatisticsBO = new DemandTypeStatisticsBO();
		List<DemandTypeStatisticsBO> DemandTypeStatisticsBOList = new ArrayList<>();
		List<ReqDataCountBO> reportLista = new ArrayList<>();
		reportLista = this.getReqSts(month);
		if (JudgeUtils.isNotEmpty(reportLista)){
			demandTypeStatisticsBO.setReqIncre(reportLista.get(0).getReqIncre());
			demandTypeStatisticsBO.setReqJump(reportLista.get(0).getReqJump());
			demandTypeStatisticsBO.setReqReplace(reportLista.get(0).getReqReplace());
			demandTypeStatisticsBO.setReqTotal(reportLista.get(0).getReqTotal());
			demandTypeStatisticsBO.setReqStock(reportLista.get(0).getReqStock());
		}
		DemandTypeStatisticsBOList.add(demandTypeStatisticsBO);
		Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), DemandTypeStatisticsBO.class, DemandTypeStatisticsBOList);
		try (OutputStream output = response.getOutputStream();
			 BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output)) {
			// 判断数据
			if (workbook == null) {
				BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
			}
			// 设置excel的文件名称
			String excelName = "需求类型统计报表" + DateUtil.date2String(new Date(), "yyyyMMddHHmmss") + ".xls";
			response.setHeader(CONTENT_DISPOSITION, "attchement;filename=" + excelName);
			response.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS, CONTENT_DISPOSITION);
			workbook.write(bufferedOutPut);
			bufferedOutPut.flush();
		} catch (IOException e) {
			BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
		}
	}
	@Override
	public void downloadProductionTypeStatistics(String month, HttpServletResponse response) {
		List<DemandTypeProductionBO> DemandTypeStatisticsBOList = new ArrayList<>();
		List<ScheduleBO> reportLista = new ArrayList<>();
		reportLista = this.getProduction(month);
		if (JudgeUtils.isNotEmpty(reportLista)) {
			reportLista.forEach(m->{
				DemandTypeProductionBO demandTypeStatisticsBO = new DemandTypeProductionBO();
				demandTypeStatisticsBO.setProNumber(m.getProNumber());
				demandTypeStatisticsBO.setProNeed(m.getProNeed());
				demandTypeStatisticsBO.setProOperator(m.getProOperator());
				demandTypeStatisticsBO.setScheduleTime(m.getScheduleTime());
				demandTypeStatisticsBO.setApplicationDept(m.getApplicationDept());
				demandTypeStatisticsBO.setFirstLevelOrganization(m.getFirstLevelOrganization());
				DemandTypeStatisticsBOList.add(demandTypeStatisticsBO);
			});
		}
		Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), DemandTypeProductionBO.class, DemandTypeStatisticsBOList);
		try (OutputStream output = response.getOutputStream();
			 BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output)) {
			// 判断数据
			if (workbook == null) {
				BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
			}
			// 设置excel的文件名称
			String excelName = "投产录入不及时报表" + DateUtil.date2String(new Date(), "yyyyMMddHHmmss") + ".xls";
			response.setHeader(CONTENT_DISPOSITION, "attchement;filename=" + excelName);
			response.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS, CONTENT_DISPOSITION);
			workbook.write(bufferedOutPut);
			bufferedOutPut.flush();
		} catch (IOException e) {
			BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
		}
	}
	@Override
	public void downloadDemandUploadDocumentBO(String month,String devpLeadDept,String productMng, HttpServletResponse response) {
		List<DemandUploadDocumentBO> DemandTypeStatisticsBOList = new ArrayList<>();
		List<DemandBO> reportLista = new ArrayList<>();
		reportLista = this.getReportForm6(month, devpLeadDept, productMng);
		if (JudgeUtils.isNotEmpty(reportLista)) {
			reportLista.forEach(m->{
				DemandUploadDocumentBO demandTypeStatisticsBO = new DemandUploadDocumentBO();
				demandTypeStatisticsBO.setReqNo(m.getReqNo());
				demandTypeStatisticsBO.setReqNm(m.getReqNm());
				demandTypeStatisticsBO.setProjectStartTm(m.getProjectStartTm());
				demandTypeStatisticsBO.setActPrdUploadTm(m.getActPrdUploadTm());
				demandTypeStatisticsBO.setActWorkloadUploadTm(m.getActWorkloadUploadTm());
				demandTypeStatisticsBO.setActSitUploadTm(m.getActSitUploadTm());
				demandTypeStatisticsBO.setActUatUploadTm(m.getActUatUploadTm());
				demandTypeStatisticsBO.setActPreUploadTm(m.getActPreUploadTm());
				demandTypeStatisticsBO.setActProductionUploadTm(m.getActProductionUploadTm());
                demandTypeStatisticsBO.setFirstLevelOrganization(m.getFirstLevelOrganization());
				demandTypeStatisticsBO.setDevpLeadDept(m.getDevpLeadDept());
				demandTypeStatisticsBO.setProductMng(m.getProductMng());
				demandTypeStatisticsBO.setDevpEng(m.getDevpEng());
				demandTypeStatisticsBO.setFrontEng(m.getFrontEng());
				demandTypeStatisticsBO.setTestEng(m.getTestEng());
				demandTypeStatisticsBO.setPreCurPeriod(m.getPreCurPeriod());
                demandTypeStatisticsBO.setNoUpload(m.getNoUpload().toString());
				DemandTypeStatisticsBOList.add(demandTypeStatisticsBO);
			});
		}
		Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), DemandUploadDocumentBO.class, DemandTypeStatisticsBOList);
		try (OutputStream output = response.getOutputStream();
			 BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output)) {
			// 判断数据
			if (workbook == null) {
				BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
			}
			// 设置excel的文件名称
			String excelName = "需求文档上传情况报表" + DateUtil.date2String(new Date(), "yyyyMMddHHmmss") + ".xls";
			response.setHeader(CONTENT_DISPOSITION, "attchement;filename=" + excelName);
			response.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS, CONTENT_DISPOSITION);
			workbook.write(bufferedOutPut);
			bufferedOutPut.flush();
		} catch (IOException e) {
			BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
		}
	}
	@Override
	public void downloadDemandImplementationReport(String month, HttpServletResponse response) {
		List<ReqDataCountBO> reportLista = this.getImpl(month);
		List<ReqDataCountBO> reportListb  = this.getImplByDept(month);
		List<DemandImplementationReportBO> demandImplementationReportBOList =  new ArrayList<>();
		if (JudgeUtils.isNotEmpty(reportListb)) {
			reportListb.forEach(m ->
					{
						DemandImplementationReportBO demandImplementationReportBO = new DemandImplementationReportBO();
						demandImplementationReportBO.setFirstLevelOrganization(m.getFirstLevelOrganization());
						demandImplementationReportBO.setDevpLeadDept(m.getDevpLeadDept());
						demandImplementationReportBO.setReqDevp(m.getReqDevp());
						demandImplementationReportBO.setReqOper(m.getReqOper());
						demandImplementationReportBO.setReqPrd(m.getReqPrd());
						demandImplementationReportBO.setReqPre(m.getReqPre());
						demandImplementationReportBO.setReqTest(m.getReqTest());
						demandImplementationReportBO.setTotal(m.getTotal());
						demandImplementationReportBOList.add(demandImplementationReportBO);
					}
			);
		}
		if (JudgeUtils.isNotEmpty(reportLista)){
			reportLista.forEach(m->
					{
						DemandImplementationReportBO demandImplementationReportBO = new DemandImplementationReportBO();
						demandImplementationReportBO.setDevpLeadDept("合计");
						demandImplementationReportBO.setReqDevp(m.getReqDevp());
						demandImplementationReportBO.setReqOper(m.getReqOper());
						demandImplementationReportBO.setReqPrd(m.getReqPrd());
						demandImplementationReportBO.setReqPre(m.getReqPre());
						demandImplementationReportBO.setReqTest(m.getReqTest());
						demandImplementationReportBO.setTotal(m.getTotal());
						demandImplementationReportBOList.add(demandImplementationReportBO);
					}
			);
		}
		Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), DemandImplementationReportBO.class, demandImplementationReportBOList);
		try (OutputStream output = response.getOutputStream();
			 BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output)) {
			// 判断数据
			if (workbook == null) {
				BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
			}
			// 设置excel的文件名称
			String excelName = "需求实施情况" + DateUtil.date2String(new Date(), "yyyyMMddHHmmss") + ".xls";
			response.setHeader(CONTENT_DISPOSITION, "attchement;filename=" + excelName);
			response.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS, CONTENT_DISPOSITION);
			workbook.write(bufferedOutPut);
			bufferedOutPut.flush();
		} catch (IOException e) {
			e.printStackTrace();
			BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
		}
	}

	@Override
	public void downloadDemandCompletionReport(String month, HttpServletResponse response) {
		List<ReqDataCountBO> reportLista = this.getComp(month);
		List<ReqDataCountBO> reportListb = this.getCompByDept(month);
		List<DemandCompletionReportBO> demandCompletionReportBOList =  new ArrayList<>();
		if (JudgeUtils.isNotEmpty(reportListb)) {
			reportListb.forEach(m ->
					{
						DemandCompletionReportBO demandCompletionReportBO = new DemandCompletionReportBO();
						demandCompletionReportBO.setFirstLevelOrganization(m.getFirstLevelOrganization());
						demandCompletionReportBO.setDevpLeadDept(m.getDevpLeadDept());
						demandCompletionReportBO.setReqTotal(m.getReqTotal());
						demandCompletionReportBO.setReqAcceptance(m.getReqAcceptance());
						demandCompletionReportBO.setReqOper(m.getReqOper());
						demandCompletionReportBO.setReqFinish(m.getReqFinish());
						demandCompletionReportBO.setReqSuspend(m.getReqSuspend());
						demandCompletionReportBO.setReqCancel(m.getReqCancel());
						demandCompletionReportBO.setReqAbnormal(m.getReqAbnormal());
						demandCompletionReportBO.setReqAbnormalRate(m.getReqAbnormalRate());
						demandCompletionReportBO.setReqFinishRate(m.getReqFinishRate());
						demandCompletionReportBO.setTotal(m.getTotal());
						demandCompletionReportBO.setReqInaccuracyRate(m.getReqInaccuracyRate());
						demandCompletionReportBOList.add(demandCompletionReportBO);
					}
			);
		}
		if (JudgeUtils.isNotEmpty(reportLista)) {
			reportLista.forEach(m ->
					{
						DemandCompletionReportBO demandCompletionReportBO = new DemandCompletionReportBO();
						demandCompletionReportBO.setDevpLeadDept("汇总");
						demandCompletionReportBO.setReqTotal(m.getReqTotal());
						demandCompletionReportBO.setReqAcceptance(m.getReqAcceptance());
						demandCompletionReportBO.setReqOper(m.getReqOper());
						demandCompletionReportBO.setReqFinish(m.getReqFinish());
						demandCompletionReportBO.setReqSuspend(m.getReqSuspend());
						demandCompletionReportBO.setReqCancel(m.getReqCancel());
						demandCompletionReportBO.setReqAbnormal(m.getReqAbnormal());
						demandCompletionReportBO.setReqAbnormalRate(m.getReqAbnormalRate());
						demandCompletionReportBO.setReqFinishRate(m.getReqFinishRate());
						demandCompletionReportBO.setTotal(m.getTotal());
						demandCompletionReportBO.setReqInaccuracyRate(m.getReqInaccuracyRate());
						demandCompletionReportBOList.add(demandCompletionReportBO);
					}
			);
		}
		Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), DemandCompletionReportBO.class, demandCompletionReportBOList);
		try (OutputStream output = response.getOutputStream();
			 BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output)) {
			// 判断数据
			if (workbook == null) {
				BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
			}
			// 设置excel的文件名称
			String excelName = "需求完成情况报表" + DateUtil.date2String(new Date(), "yyyyMMddHHmmss") + ".xls";
			response.setHeader(CONTENT_DISPOSITION, "attchement;filename=" + excelName);
			response.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS, CONTENT_DISPOSITION);
			workbook.write(bufferedOutPut);
			bufferedOutPut.flush();
		} catch (IOException e) {
			e.printStackTrace();
			BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
		}

	}
	@Override
	public void downloadDemandCompletionReport2(String month, HttpServletResponse response) {
		List<ReqDataCountBO> reportListb = this.getCompByDept2(month);
		List<DemandCompletionReportBO> demandCompletionReportBOList =  new ArrayList<>();
		if (JudgeUtils.isNotEmpty(reportListb)) {
			reportListb.forEach(m ->
					{
						DemandCompletionReportBO demandCompletionReportBO = new DemandCompletionReportBO();
						demandCompletionReportBO.setFirstLevelOrganization(m.getFirstLevelOrganization());
						demandCompletionReportBO.setReqTotal(m.getReqTotal());
						demandCompletionReportBO.setReqAcceptance(m.getReqAcceptance());
						demandCompletionReportBO.setReqOper(m.getReqOper());
						demandCompletionReportBO.setReqFinish(m.getReqFinish());
						demandCompletionReportBO.setReqSuspend(m.getReqSuspend());
						demandCompletionReportBO.setReqCancel(m.getReqCancel());
						demandCompletionReportBO.setReqAbnormal(m.getReqAbnormal());
						demandCompletionReportBO.setReqAbnormalRate(m.getReqAbnormalRate());
						demandCompletionReportBO.setReqFinishRate(m.getReqFinishRate());
						demandCompletionReportBO.setTotal(m.getTotal());
						demandCompletionReportBO.setReqInaccuracyRate(m.getReqInaccuracyRate());
						demandCompletionReportBOList.add(demandCompletionReportBO);
					}
			);
		}
		Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), DemandCompletionReportBO.class, demandCompletionReportBOList);
		try (OutputStream output = response.getOutputStream();
			 BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output)) {
			// 判断数据
			if (workbook == null) {
				BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
			}
			// 设置excel的文件名称
			String excelName = "一级团队需求完成情况报表" + DateUtil.date2String(new Date(), "yyyyMMddHHmmss") + ".xls";
			response.setHeader(CONTENT_DISPOSITION, "attchement;filename=" + excelName);
			response.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS, CONTENT_DISPOSITION);
			workbook.write(bufferedOutPut);
			bufferedOutPut.flush();
		} catch (IOException e) {
			e.printStackTrace();
			BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
		}

	}

	@Override
	public void downloadBaseOwnershipDepartmentStatistics(String month, HttpServletResponse response) {
		List<ReqDataCountBO> reportLista = new ArrayList<>();
		List<BaseOwnershipDepartmentStatisticsBO> BaseOwnershipDepartmentStatisticsBOList =  new ArrayList<>();
		reportLista = this.getStageByJd(month);
		if (JudgeUtils.isNotEmpty(reportLista)) {
			reportLista.forEach(m->{
				BaseOwnershipDepartmentStatisticsBO baseOwnershipDepartmentStatisticsBO = new BaseOwnershipDepartmentStatisticsBO();
				baseOwnershipDepartmentStatisticsBO.setReqPrd(m.getReqPrd());
				baseOwnershipDepartmentStatisticsBO.setFinanceDevp(m.getFinanceDevp());
				baseOwnershipDepartmentStatisticsBO.setQualityDevp(m.getQualityDevp());
				baseOwnershipDepartmentStatisticsBO.setInnoDevp(m.getInnoDevp());
				baseOwnershipDepartmentStatisticsBO.setElecDevp(m.getElecDevp());
				baseOwnershipDepartmentStatisticsBO.setRiskDevp(m.getRiskDevp());
				baseOwnershipDepartmentStatisticsBO.setFinancialDevp(m.getFinancialDevp());
				baseOwnershipDepartmentStatisticsBO.setCommDevp(m.getCommDevp());
				baseOwnershipDepartmentStatisticsBO.setInfoDevp(m.getInfoDevp());
				baseOwnershipDepartmentStatisticsBO.setBusiDevp(m.getBusiDevp());
				baseOwnershipDepartmentStatisticsBO.setGoveDevp(m.getGoveDevp());
				baseOwnershipDepartmentStatisticsBO.setTotal(m.getTotal());
				BaseOwnershipDepartmentStatisticsBOList.add(baseOwnershipDepartmentStatisticsBO);
			});
		}
		Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), BaseOwnershipDepartmentStatisticsBO.class, BaseOwnershipDepartmentStatisticsBOList);
		try (OutputStream output = response.getOutputStream();
			 BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output)) {
			// 判断数据
			if (workbook == null) {
				BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
			}
			// 设置excel的文件名称
			String excelName = "基地归属部门统计" + DateUtil.date2String(new Date(), "yyyyMMddHHmmss") + ".xls";
			response.setHeader(CONTENT_DISPOSITION, "attchement;filename=" + excelName);
			response.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS, CONTENT_DISPOSITION);
			workbook.write(bufferedOutPut);
			bufferedOutPut.flush();
		} catch (IOException e) {
			BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
		}
	}
	@Override
	public DemandRspBO findDemand(DemandBO demandBO) {
		String time= DateUtil.date2String(new Date(), "yyyy-MM-dd");
		PageInfo<DemandBO> pageInfo = getPageInfo(demandBO);
		List<DemandBO> demandBOList = BeanConvertUtils.convertList(pageInfo.getList(), DemandBO.class);

		for (int i = 0; i < demandBOList.size(); i++) {
			String reqAbnorType = demandBOList.get(i).getReqAbnorType();
			String reqAbnorTypeAll = "";
			DemandBO demand = reqPlanService.findById(demandBOList.get(i).getReqInnerSeq());

			//当需求定稿时间、uat更新时间、测试完成时间、需求当前阶段、需求状态都不为空的时候，执行进度实时显示逻辑。
			if (com.cmpay.lemon.common.utils.StringUtils.isNotBlank(demand.getPrdFinshTm()) && com.cmpay.lemon.common.utils.StringUtils.isNotBlank(demand.getUatUpdateTm())
					&& com.cmpay.lemon.common.utils.StringUtils.isNotBlank(demand.getTestFinshTm()) && com.cmpay.lemon.common.utils.StringUtils.isNotBlank(demand.getPreCurPeriod())
					&& com.cmpay.lemon.common.utils.StringUtils.isNotBlank(demand.getReqSts())) {
				//当前时间大于预计时间，并且所处阶段小于30,并且需求状态不为暂停或取消（30，40）,则该需求进度异常
				if (time.compareTo(demand.getPrdFinshTm()) > 0 && Integer.parseInt(demand.getPreCurPeriod()) < REQCONFIRM
						&& REQSUSPEND.compareTo(demand.getReqSts()) != 0 && REQCANCEL.compareTo(demand.getReqSts()) != 0) {
					reqAbnorTypeAll += "需求进度滞后,";
				}
				if (time.compareTo(demand.getUatUpdateTm()) > 0 && Integer.parseInt(demand.getPreCurPeriod()) >= REQCONFIRM
						&& Integer.parseInt(demand.getPreCurPeriod()) < UPDATEUAT && REQSUSPEND.compareTo(demand.getReqSts()) != 0
						&& REQCANCEL.compareTo(demand.getReqSts()) != 0) {
					reqAbnorTypeAll += "开发进度滞后,";
				}
				if (time.compareTo(demand.getTestFinshTm()) > 0 && Integer.parseInt(demand.getPreCurPeriod()) >= UPDATEUAT
						&& Integer.parseInt(demand.getPreCurPeriod()) < FINISHUATTEST && REQSUSPEND.compareTo(demand.getReqSts()) != 0
						&& REQCANCEL.compareTo(demand.getReqSts()) != 0) {
					reqAbnorTypeAll += "测试进度滞后";
				}
				if (com.cmpay.lemon.common.utils.StringUtils.isBlank(reqAbnorTypeAll)) {
					reqAbnorTypeAll += "正常";
				}
			} else if (reqAbnorType.indexOf("01") != -1) {
				demandBOList.get(i).setReqAbnorType("正常");
				continue;
			} else {
				if (reqAbnorType.indexOf("03") != -1) {
					reqAbnorTypeAll += "需求进度滞后,";
				}
				if (reqAbnorType.indexOf("04") != -1) {
					reqAbnorTypeAll += "开发进度滞后,";
				}
				if (reqAbnorType.indexOf("05") != -1) {
					reqAbnorTypeAll += "测试进度滞后";
				}
			}

			if (reqAbnorTypeAll.length() >= 1 && ',' == reqAbnorTypeAll.charAt(reqAbnorTypeAll.length() - 1)) {
				reqAbnorTypeAll = reqAbnorTypeAll.substring(0, reqAbnorTypeAll.length() - 1);
				demandBOList.get(i).setReqAbnorType(reqAbnorTypeAll);
			} else {
				demandBOList.get(i).setReqAbnorType(reqAbnorTypeAll);
			}
		}
		DemandRspBO demandRspBO = new DemandRspBO();
		demandRspBO.setDemandBOList(demandBOList);
		demandRspBO.setPageInfo(pageInfo);
		return demandRspBO;
	}

	//投产未验证清单，功能已取消，无调用
	@Override
	public List<ProductionBO> getProductionVerificationIsNotTimely(int dayNumber) {
		String date="";
		List<ProductionDO> productionDOList = new LinkedList<>();
		productionDOList = operationProductionService.getProductionVerificationIsNotTimely(date);
		List<ProductionBO> productionBOList = new LinkedList<>();
		productionDOList.forEach(m -> {
			try {
				ProductionBO productionBO = new ProductionBO();
				productionBOList.add(BeanUtils.copyPropertiesReturnDest(productionBO, m));
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				//已投产天数
				Calendar c1 = Calendar.getInstance();
				Calendar c2 = Calendar.getInstance();
				c1.setTime(sdf.parse(sdf.format(new Date())));
				c2.setTime(sdf.parse(sdf.format(m.getProDate())));
				long day = (sdf.parse(sdf.format(new Date())).getTime() - sdf.parse(sdf.format(m.getProDate())).getTime()) / (24 * 60 * 60 * 1000);
				productionBO.setDayNumber(String.valueOf(day));
			}catch (Exception e){
				e.printStackTrace();
			}
		});
		return productionBOList;

	}

	private PageInfo<DemandBO>  getPageInfo(DemandBO demandBO) {
		DemandDO demandDO = new DemandDO();
		BeanConvertUtils.convert(demandDO, demandBO);
		PageInfo<DemandBO> pageInfo = PageUtils.pageQueryWithCount(demandBO.getPageNum(), demandBO.getPageSize(),
				() -> BeanConvertUtils.convertList(reqDataCountDao.find(demandDO), DemandBO.class));
		return pageInfo;
	}
}

