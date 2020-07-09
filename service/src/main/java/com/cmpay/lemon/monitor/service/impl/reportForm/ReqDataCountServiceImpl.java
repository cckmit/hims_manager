package com.cmpay.lemon.monitor.service.impl.reportForm;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.cmpay.lemon.common.exception.BusinessException;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.common.utils.JudgeUtils;
import com.cmpay.lemon.framework.page.PageInfo;
import com.cmpay.lemon.framework.utils.PageUtils;
import com.cmpay.lemon.monitor.bo.*;
import com.cmpay.lemon.monitor.dao.*;
import com.cmpay.lemon.monitor.entity.*;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.demand.ReqPlanService;
import com.cmpay.lemon.monitor.service.impl.demand.ReqPlanServiceImpl;
import com.cmpay.lemon.monitor.service.impl.workload.ReqWorkLoadServiceImpl;
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
import java.text.ParseException;
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

    private static final String XINGQISAN= "星期三";

	private static final Logger LOGGER = LoggerFactory.getLogger(ReqPlanServiceImpl.class);
	@Autowired
	private IDemandExtDao demandDao;
	@Autowired
	private IReqDataCountDao reqDataCountDao;
	@Autowired
	private IWorkingHoursExtDao iWorkingHoursDao;
	@Autowired
	private IDemandQualityExtDao iDemandQualityDao;
	@Autowired
	private ReqPlanService reqPlanService;
    @Autowired
    private IDemandResourceInvestedDao iDemandResourceInvestedDao;
	@Autowired
	IDemandJiraDao demandJiraDao;
	@Autowired
	private OperationProductionService operationProductionService;
    @Autowired
    private IProblemStatisticDao problemStatisticDao;
    @Autowired
    private IDefectDetailsExtDao defectDetailsExtDao;
    @Autowired
    private IIssueDetailsExtDao issueDetailsExtDao;
    @Autowired
    private IProductionDefectsExtDao productionDefectsExtDao;
    @Autowired
	private ReqWorkLoadServiceImpl reqWorkLoadService;



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
	public List<DemandBO> getReportForm6(String reqImplMon,String devpLeadDept,String productMng,String firstLevelOrganization) {
		List<DemandBO> reqDataCountBOS = new LinkedList<>();
		ReqDateCountDO reqDateCountDO = new ReqDateCountDO();
		reqDateCountDO.setReqImplMon(reqImplMon);
		reqDateCountDO.setDevpLeadDept(devpLeadDept);
		reqDateCountDO.setProductMng(productMng);
        reqDateCountDO.setFirstLevelOrganization(firstLevelOrganization);
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
	public List<WorkingHoursBO> findEpicKeyHours(String epic){
		List<WorkingHoursBO> workingHoursBOS = new LinkedList<>();
		System.err.println(epic);
		List<WorkingHoursDO> impl = iWorkingHoursDao.findEpicKeyHours(epic);
		System.err.println(impl);
		impl.forEach(m->
				workingHoursBOS.add(BeanUtils.copyPropertiesReturnDest(new WorkingHoursBO(), m))
		);
		System.err.println(workingHoursBOS);
		return workingHoursBOS;
	}
	@Override
	public DemandQualityBO findDemandQuality(String epic){
		DemandQualityBO demandQualityBO = new DemandQualityBO();
		DemandQualityDO demandQualityDO = iDemandQualityDao.get(epic);
        BeanUtils.copyPropertiesReturnDest(demandQualityBO, demandQualityDO);
		return demandQualityBO;
	}
	@Override
    public WorkingHoursBO getReportForm10(String devpLeadDept,String date1,String date2){
	    WorkingHoursBO workingHoursBO = new WorkingHoursBO();
        List<WorkingHoursDO> impl = null;
        WorkingHoursDO workingHoursDO = new WorkingHoursDO();
        workingHoursDO.setDevpLeadDept(devpLeadDept);
        System.err.println(date1+"====="+date2);
        if(StringUtils.isBlank(date1)&&StringUtils.isBlank(date2)){
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如周、月!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        impl = iWorkingHoursDao.findDeptView(workingHoursDO);
        System.err.println(impl);
        if(impl!=null &&impl.size()!=0){
            BeanUtils.copyPropertiesReturnDest(workingHoursBO, impl.get(0));
        }
	    return  workingHoursBO;
    }

    /**
     * 团队视图 柱状图
     * @param devpLeadDept
     * @param date1
     * @param date2
     * @return
     */
    @Override
    public DemandHoursRspBO getDeptWorkHours(String devpLeadDept,String date1,String date2){
        WorkingHoursDO workingHoursDO = new WorkingHoursDO();
        workingHoursDO.setDevpLeadDept(devpLeadDept);
        List<WorkingHoursDO> impl = null;
        System.err.println(date1+"====="+date2);
        if(StringUtils.isBlank(date1)&&StringUtils.isBlank(date2)){
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如周、月!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        // 查询周
        if(StringUtils.isNotBlank(date1)&&StringUtils.isBlank(date2)){
            workingHoursDO.setSelectTime(date1);
            impl = iWorkingHoursDao.findWeekSum(workingHoursDO);
            System.err.println(impl);
        }
        // 查询月
        if(StringUtils.isNotBlank(date2)&&StringUtils.isBlank(date1)){
            workingHoursDO.setSelectTime(date2);
            impl = iWorkingHoursDao.findMonthSum(workingHoursDO);
            System.err.println(impl);
        }
        System.err.println(impl);
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        String sum = "";
        double sumx = 0;
        if(impl!=null &&impl.size()!=0){
            for(int i=0;i<impl.size();i++){
                SumBos.add(impl.get(i).getDisplayname());
                sumx = sumx + getWorkHoursTime(Integer.parseInt(impl.get(i).getSumTime()));
                workingHoursBOS.add(String.valueOf(getWorkHoursTime(Integer.parseInt(impl.get(i).getSumTime()))));
            }
            sum = String.valueOf(sumx);
        }
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO.setSum(sum);
        demandHoursRspBO.setListSum(SumBos);
        demandHoursRspBO.setStringList(workingHoursBOS);
        System.err.println(demandHoursRspBO);
        return  demandHoursRspBO;
    }

	@Override
	public DemandHoursRspBO getDeptWorkHoursAndPoint(String devpLeadDept,String date1,String date2){
		WorkingHoursDO workingHoursDO = new WorkingHoursDO();
		workingHoursDO.setDevpLeadDept(devpLeadDept);
		List<WorkingHoursDO> impl = null;
		System.err.println(date1+"====="+date2);
		DemandBO demandBO = new DemandBO();
		demandBO.setDevpLeadDept(devpLeadDept);
		if(StringUtils.isBlank(date1)&&StringUtils.isBlank(date2)){
			MsgEnum.ERROR_CUSTOM.setMsgInfo("");
			MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如周、月!");
			BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
		}
		// 查询周
		if(StringUtils.isNotBlank(date1)&&StringUtils.isBlank(date2)){
			workingHoursDO.setSelectTime(date1);
			impl = iWorkingHoursDao.findWeekSumB(workingHoursDO);
			demandBO.setReqImplMon(date1.substring(0,6));
			System.err.println(impl);
		}
		// 查询月
		if(StringUtils.isNotBlank(date2)&&StringUtils.isBlank(date1)){
			workingHoursDO.setSelectTime(date2);
			impl = iWorkingHoursDao.findMonthSumB(workingHoursDO);
			demandBO.setReqImplMon(date2);
			System.err.println(impl);
		}
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        if(impl!=null &&impl.size()!=0){
            workingHoursBOS.add("工时");
            SumBos.add(Double.toString(getWorkHours(Integer.parseInt(impl.get(0).getSumTime()))));
        }
		// 获取当月的功能点
		System.err.println(demandBO);
		List<Double> dept =reqWorkLoadService.getExportCountForDevp(demandBO);
       if(dept.get(0) != null){
           workingHoursBOS.add("功能点");
           SumBos.add(Double.toString(dept.get(0)));
       }
		DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO.setStringList(workingHoursBOS);
        demandHoursRspBO.setListSum(SumBos);
		return  demandHoursRspBO;
	}

    @Override
    public DemandHoursRspBO getDeptProduction(String devpLeadDept,String date1,String date2){
        WorkingHoursDO workingHoursDO = new WorkingHoursDO();
        workingHoursDO.setDevpLeadDept(devpLeadDept);
        List<ProductionDO> impl = null;
        System.err.println(date1+"====="+date2);
        if(StringUtils.isBlank(date1)&&StringUtils.isBlank(date2)){
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如周、月!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        // 查询周
        if(StringUtils.isNotBlank(date1)&&StringUtils.isBlank(date2)){
            workingHoursDO.setSelectTime(date1);
            impl = iWorkingHoursDao.findListDeptWeek(workingHoursDO);
            System.err.println(impl);
        }
        // 查询月
        if(StringUtils.isNotBlank(date2)&&StringUtils.isBlank(date1)){
            workingHoursDO.setSelectTime(date2);
            impl = iWorkingHoursDao.findListDeptMonth(workingHoursDO);
            System.err.println(impl);
        }
        System.err.println(impl);
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        String sum = "";
        int a =0;
        int b =0;
        int c =0;
        if(impl!=null &&impl.size()!=0){
            System.err.println(impl.size());
           sum = impl.size()+"";
           for(int i=0;i<impl.size();i++){
               if(impl.get(i).getProType().equals("正常投产")&&XINGQISAN.equals(testDate(impl.get(i).getProDate().toString()))){
                   a =a+1;
               }
               if(impl.get(i).getProType().equals("正常投产")&&!XINGQISAN.equals(testDate(impl.get(i).getProDate().toString()))){
                    b=b+1;
               }
               if(impl.get(i).getProType().equals("救火更新")){
                   c =c+1;
               }
           }
           if (a >0){
               String a1 =  "{'value': '"+a+"', 'name': '正常投产'}";
               workingHoursBOS.add(a1);
               SumBos.add("正常投产");
           }
            if (b >0){
                String b1 =  "{'value': '"+b+"', 'name': '非投产日正常投产'}";
                workingHoursBOS.add(b1);
                SumBos.add("非投产日正常投产");
            }
            if (c >0){
                String c1 =  "{'value': '"+c+"', 'name': '救火更新'}";
                workingHoursBOS.add(c1);
                SumBos.add("救火更新");
            }
        }else{
            sum ="0";
        }
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO.setStringList(workingHoursBOS);
        demandHoursRspBO.setListSum(SumBos);
        demandHoursRspBO.setSum(sum);
        return  demandHoursRspBO;
    }
    public static String testDate(String newtime) {
        String dayNames[] = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
        Calendar c = Calendar.getInstance();// 获得一个日历的zhi实例
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            c.setTime(sdf.parse(newtime));
        } catch (ParseException e) {
        // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return dayNames[c.get(Calendar.DAY_OF_WEEK)-1];
    }

    /**
     * 团队需求异常情况
     * @param devpLeadDept
     * @param date1
     * @param date2
     * @return
     */
    @Override
    public DemandHoursRspBO getDemandDispose(String devpLeadDept,String date1,String date2){
        List<DemandDO> impl = null;
        WorkingHoursDO workingHoursDO = new WorkingHoursDO();
        workingHoursDO.setDevpLeadDept(devpLeadDept);
        workingHoursDO.setSelectTime(date2);
        impl = demandDao.findListDeptDemand(workingHoursDO);
        System.err.println(impl);

        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        String sum = "";
        int a =0;
        int b =0;
        int c =0;
        if(impl!=null &&impl.size()!=0){
            System.err.println(impl.size());
            sum = impl.size()+"";
            for(int i=0;i<impl.size();i++){
                if(!impl.get(i).getReqSts().equals("30")&&!impl.get(i).getReqSts().equals("40")){
                    a =a+1;
                }
                if(impl.get(i).getReqSts().equals("30")){
                    b=b+1;
                }
                if(impl.get(i).getReqSts().equals("40")){
                    c =c+1;
                }
            }
            if (a >0){
                String a1 =  "{'value': '"+a+"', 'name': '正常需求'}";
                workingHoursBOS.add(a1);
                SumBos.add("正常需求");
            }
            if (b >0){
                String b1 =  "{'value': '"+b+"', 'name': '取消需求'}";
                workingHoursBOS.add(b1);
                SumBos.add("取消需求");
            }
            if (c >0){
                String c1 =  "{'value': '"+c+"', 'name': '暂停需求'}";
                workingHoursBOS.add(c1);
                SumBos.add("暂停需求");
            }
        }else{
            sum ="0";
        }
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO.setStringList(workingHoursBOS);
        demandHoursRspBO.setListSum(SumBos);
        demandHoursRspBO.setSum(sum);
        return demandHoursRspBO;
    }

    /**
     * 团队需求主导需求
     * @param devpLeadDept
     * @param date1
     * @param date2
     * @return
     */
    @Override
    public DemandHoursRspBO getDemandCoordinate(String devpLeadDept,String date1,String date2){
        List<DemandDO> impl = null;
        List<DemandDO> impl2 = null;
        WorkingHoursDO workingHoursDO = new WorkingHoursDO();
        workingHoursDO.setDevpLeadDept(devpLeadDept);
        workingHoursDO.setSelectTime(date2);
        impl = demandDao.findListDeptDemand(workingHoursDO);
        impl2 = demandDao.findListDevpCoorDeptDemand(workingHoursDO);
        System.err.println(impl);
        System.err.println(impl2);
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        int sum = 0;
        if(impl!=null &&impl.size()!=0){
            workingHoursBOS.add(impl.size()+"");
            SumBos.add("主导需求");
            sum = sum + impl.size();
        }else {
            workingHoursBOS.add("0");
            SumBos.add("主导需求");
        }
        if(impl2!=null &&impl2.size()!=0){
            workingHoursBOS.add(impl2.size()+"");
            SumBos.add("配合需求");
            sum = sum + impl2.size();
        }else {
            workingHoursBOS.add("0");
            SumBos.add("配合需求");
        }
        String simx = sum+"";
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO.setStringList(workingHoursBOS);
        demandHoursRspBO.setListSum(SumBos);
        demandHoursRspBO.setSum(simx);
        return demandHoursRspBO;
    }
	@Override
	public WorkingHoursBO getReportForm11(String displayname,String date1,String date2){
		WorkingHoursBO workingHoursBO = new WorkingHoursBO();
		List<WorkingHoursDO> impl = null;
		WorkingHoursDO workingHoursDO = new WorkingHoursDO();
		workingHoursDO.setDisplayname(displayname);
		System.err.println(date1+"====="+date2);
		if(StringUtils.isBlank(date1)&&StringUtils.isBlank(date2)){
			MsgEnum.ERROR_CUSTOM.setMsgInfo("");
			MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如周、月!");
			BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
		}
		// 查询周
		if(StringUtils.isNotBlank(date1)&&StringUtils.isBlank(date2)){
			workingHoursDO.setSelectTime(date1);
			impl = iWorkingHoursDao.findWeekView(workingHoursDO);
			System.err.println(impl);
		}
		// 查询月
		if(StringUtils.isNotBlank(date2)&&StringUtils.isBlank(date1)){
			workingHoursDO.setSelectTime(date2);
			impl = iWorkingHoursDao.findMonthView(workingHoursDO);
			System.err.println(impl);
		}
		if(impl!=null &&impl.size()!=0){
			BeanUtils.copyPropertiesReturnDest(workingHoursBO, impl.get(0));
		}
		System.err.println(impl);
		return workingHoursBO;
	}

	@Override
	public List<WorkingHoursBO> getDemandStaffView(String displayname,String date1,String date2){
		List<WorkingHoursBO> workingHoursBos = new LinkedList<>();
		List<WorkingHoursDO> impl = null;
		WorkingHoursDO workingHoursDO = new WorkingHoursDO();
		workingHoursDO.setDisplayname(displayname);
		System.err.println(date1+"====="+date2);
		if(StringUtils.isBlank(date1)&&StringUtils.isBlank(date2)){
			MsgEnum.ERROR_CUSTOM.setMsgInfo("");
			MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如周、月!");
			BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
		}
		// 查询周
		if(StringUtils.isNotBlank(date1)&&StringUtils.isBlank(date2)){
			workingHoursDO.setSelectTime(date1);
			impl = iWorkingHoursDao.findListWeekView(workingHoursDO);
			System.err.println(impl);
		}
		// 查询月
		if(StringUtils.isNotBlank(date2)&&StringUtils.isBlank(date1)){
			workingHoursDO.setSelectTime(date2);
			impl = iWorkingHoursDao.findListMonthView(workingHoursDO);
			System.err.println(impl);
		}
		impl.forEach(m->
				workingHoursBos.add(BeanUtils.copyPropertiesReturnDest(new WorkingHoursBO(), m))
		);
		if(impl!=null && impl.size()!=0){
			for (int i=0;i<impl.size();i++){
				DemandJiraDO demandJiraDO = new DemandJiraDO();
				if (impl.get(i).getEpickey() ==null || impl.get(i).getEpickey()==""){
					continue;
				}
				demandJiraDO.setJiraKey(impl.get(i).getEpickey());
				// 根据jiraKey获取内部编号
				List<DemandJiraDO> demandJiraDos = demandJiraDao.find(demandJiraDO);
				// 根据内部编号获取 需求名及需求编号
				if(demandJiraDos!=null && demandJiraDos.size()!=0){
					DemandDO demandDO = demandDao.get(demandJiraDos.get(demandJiraDos.size()-1).getReqInnerSeq());
					workingHoursBos.get(i).setReqNo(demandDO.getReqNo());
					workingHoursBos.get(i).setReqNm(demandDO.getReqNm());
				}
			}
		}
		System.err.println(workingHoursBos);
		return workingHoursBos;
	}

    /**
     * 团队需求情况
     * @param devpLeadDept
     * @param date1
     * @param date2
     * @return
     */
    @Override
    public List<WorkingHoursBO> getDeptStaffView(String devpLeadDept,String date1,String date2){
        List<WorkingHoursBO> workingHoursBos = new LinkedList<>();
        List<WorkingHoursDO> impl = null;
        WorkingHoursDO workingHoursDO = new WorkingHoursDO();
        workingHoursDO.setDevpLeadDept(devpLeadDept);
        System.err.println(date1+"====="+date2);
        if(StringUtils.isBlank(date1)&&StringUtils.isBlank(date2)){
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如周、月!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        // 查询周
        if(StringUtils.isNotBlank(date1)&&StringUtils.isBlank(date2)){
            workingHoursDO.setSelectTime(date1);
            impl = iWorkingHoursDao.findListWeekViewDept(workingHoursDO);
            System.err.println(impl);
        }
        // 查询月
        if(StringUtils.isNotBlank(date2)&&StringUtils.isBlank(date1)){
            workingHoursDO.setSelectTime(date2);
            impl = iWorkingHoursDao.findListMonthViewDept(workingHoursDO);
            System.err.println(impl);
        }
        impl.forEach(m->
                workingHoursBos.add(BeanUtils.copyPropertiesReturnDest(new WorkingHoursBO(), m))
        );
        if(impl!=null && impl.size()!=0){
            for (int i=0;i<impl.size();i++){
                DemandJiraDO demandJiraDO = new DemandJiraDO();
                if (impl.get(i).getEpickey() ==null || impl.get(i).getEpickey()==""){
                    continue;
                }
                demandJiraDO.setJiraKey(impl.get(i).getEpickey());
                // 根据jiraKey获取内部编号
                List<DemandJiraDO> demandJiraDos = demandJiraDao.find(demandJiraDO);
                // 根据内部编号获取 需求名及需求编号
                if(demandJiraDos!=null && demandJiraDos.size()!=0){
                    DemandDO demandDO = demandDao.get(demandJiraDos.get(demandJiraDos.size()-1).getReqInnerSeq());
                    workingHoursBos.get(i).setReqNo(demandDO.getReqNo());
                    workingHoursBos.get(i).setReqNm(demandDO.getReqNm());
                }
            }
        }
        System.err.println("112222222231132132"+workingHoursBos);
        return workingHoursBos;
    }
	/**
	 * 需求任务与其它任务工时间饼图
	 * @param displayname
	 * @param date1
	 * @param date2
	 * @return
	 */
	@Override
	public DemandHoursRspBO getDemandStaffTask(String displayname,String date1,String date2){
		DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
		List<WorkingHoursDO> impl = null;
		List<String> workingHoursBOS = new LinkedList<>();
		List<String> SumBos = new LinkedList<>();
		String sum = "";
		double sumx=0;
		WorkingHoursDO workingHoursDO = new WorkingHoursDO();
		workingHoursDO.setDisplayname(displayname);
		System.err.println(date1+"====="+date2);
		if(StringUtils.isBlank(date1)&&StringUtils.isBlank(date2)){
			MsgEnum.ERROR_CUSTOM.setMsgInfo("");
			MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如周、月!");
			BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
		}
		// 查询周
		if(StringUtils.isNotBlank(date1)&&StringUtils.isBlank(date2)){
			workingHoursDO.setSelectTime(date1);
			impl = iWorkingHoursDao.findListWeekView(workingHoursDO);
			System.err.println(impl);
		}
		// 查询月
		if(StringUtils.isNotBlank(date2)&&StringUtils.isBlank(date1)){
			workingHoursDO.setSelectTime(date2);
			impl = iWorkingHoursDao.findListMonthView(workingHoursDO);
			System.err.println(impl);
		}
		int sumDemand = 0;
		for(int i=0;i<impl.size();i++){
			String sumTime = "";
			// 获取其它任务的工时间
			if(impl.get(i).getEpickey()==null||impl.get(i).getEpickey()==""){
				String sumTime1 = impl.get(i).getTimespnet();
				sumx = sumx + getWorkHours(Integer.parseInt(sumTime1));
				// {value: 5, name: '需求任务'}
                String qtDemand =  "{'value': '"+getWorkHours(Integer.parseInt(sumTime1))+"', 'name': '其它任务'}";

				workingHoursBOS.add(String.valueOf(qtDemand));
				SumBos.add("其它任务");
			}
			//需求任务的工时间
			sumTime = impl.get(i).getTimespnet();
			sumDemand = sumDemand + Integer.parseInt(sumTime);
		}
        sumx = sumx + getWorkHours(sumDemand);
        String Demand =  "{'value': '"+getWorkHours(sumDemand)+"', 'name': '需求任务'}";
		workingHoursBOS.add(Demand);
		SumBos.add("需求任务");
		sum =String.valueOf(sumx);
		System.err.println(sum);
		demandHoursRspBO.setListSum(SumBos);
		demandHoursRspBO.setSum(sum);
		demandHoursRspBO.setStringList(workingHoursBOS);
		System.err.println(demandHoursRspBO);
		return demandHoursRspBO;
	}
	@Override
    public DefectDetailsRspBO getDemandDefectDetails(String displayname,String date1,String date2){
        System.err.println("缺陷问题");
        List<DefectDetailsDO> impl = null;
        List<DefectDetailsBO> defectDetailsBOList = null;
        DefectDetailsDO defectDetailsDO = new DefectDetailsDO();
        defectDetailsDO.setAssignee(displayname);
        System.err.println(date1+"====="+date2);
        if(StringUtils.isBlank(date1)&&StringUtils.isBlank(date2)){
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如周、月!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        // 查询周
        if(StringUtils.isNotBlank(date1)&&StringUtils.isBlank(date2)){
            defectDetailsDO.setRegistrationDate(date1);
            impl = defectDetailsExtDao.findWeekList(defectDetailsDO);
            System.err.println(impl);
        }
        // 查询月
        if(StringUtils.isNotBlank(date2)&&StringUtils.isBlank(date1)){
            defectDetailsDO.setRegistrationDate(date2);
            impl = defectDetailsExtDao.findList(defectDetailsDO);
            System.err.println(impl);
        }
        DefectDetailsRspBO defectDetailsRspBO = new DefectDetailsRspBO();
        defectDetailsBOList =BeanConvertUtils.convertList(impl, DefectDetailsBO.class);
        defectDetailsRspBO.setDefectDetailsBos(defectDetailsBOList);
	    return  defectDetailsRspBO;
    }

    /**
     * 个人视图评审问题
     * @param displayname
     * @param date1
     * @param date2
     * @return
     */
    @Override
    public IssueDetailsRspBO getDemandIssueDetails(String displayname,String date1,String date2){
	    IssueDetailsDO issueDetailsDO = new IssueDetailsDO();
        List<IssueDetailsDO> impl = null;
        List<IssueDetailsBO> issueDetailsBOList = null;
	    issueDetailsDO.setAssignee(displayname);
        if(StringUtils.isBlank(date1)&&StringUtils.isBlank(date2)){
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如周、月!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        // 查询周
        if(StringUtils.isNotBlank(date1)&&StringUtils.isBlank(date2)){
            issueDetailsDO.setRegistrationDate(date1);
            impl = issueDetailsExtDao.findWeekList(issueDetailsDO);
            System.err.println(impl);
        }
        // 查询月
        if(StringUtils.isNotBlank(date2)&&StringUtils.isBlank(date1)){
            issueDetailsDO.setRegistrationDate(date2);
            impl = issueDetailsExtDao.findList(issueDetailsDO);
            System.err.println(impl);
        }
        IssueDetailsRspBO issueDetailsRspBO = new IssueDetailsRspBO();
        issueDetailsBOList =BeanConvertUtils.convertList(impl, IssueDetailsBO.class);
        issueDetailsRspBO.setIssueDetailsBOList(issueDetailsBOList);
        return issueDetailsRspBO;
    }
    @Override
    public ProductionDefectsRspBO getDemandProductionDetails(String displayname,String date1,String date2){
        ProductionDefectsDO productionDefectsDO = new ProductionDefectsDO();
        List<ProductionDefectsDO> impl = null;
        List<ProductionDefectsBO> productionDefectsBOList = null;
        productionDefectsDO.setPersonincharge(displayname);
        if(StringUtils.isBlank(date1)&&StringUtils.isBlank(date2)){
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如周、月!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        // 查询周
        if(StringUtils.isNotBlank(date1)&&StringUtils.isBlank(date2)){
            productionDefectsDO.setProcessstartdate(date1);
            impl = productionDefectsExtDao.findWeekList(productionDefectsDO);
            System.err.println(impl);
        }
        // 查询月
        if(StringUtils.isNotBlank(date2)&&StringUtils.isBlank(date1)){
            productionDefectsDO.setProcessstartdate(date2);
            impl = productionDefectsExtDao.findMonthList(productionDefectsDO);
            System.err.println(impl);
        }
        ProductionDefectsRspBO productionDefectsRspBO = new ProductionDefectsRspBO();
        productionDefectsBOList =BeanConvertUtils.convertList(impl, ProductionDefectsBO.class);
        productionDefectsRspBO.setProductionDefectsBOList(productionDefectsBOList);
        return productionDefectsRspBO;
    }
    @Override
    public DemandHoursRspBO getDemandHours(String epic){
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        List<String> workingHoursBOS = new LinkedList<>();
        DemandResourceInvestedDO demandResourceInvestedDO = new DemandResourceInvestedDO();
        demandResourceInvestedDO.setEpicKey(epic);
        List<DemandResourceInvestedDO> impl = iDemandResourceInvestedDao.find(demandResourceInvestedDO);
        impl.forEach(m->
                workingHoursBOS.add(m.getWorkHoursToString())
        );
        List<String> listSumBos = new LinkedList<>();
        impl.forEach(m->
                listSumBos.add(m.getDepartment())
        );
        String sum = "";
        List<String> SumBos = new LinkedList<>();
        impl.forEach(m->
                SumBos.add(m.getValue())
        );
        int sum1 = 0;
        if(SumBos !=null || SumBos.size()!=0){
            for (int i=0;i<SumBos.size();i++){
                int sumx = Integer.parseInt(SumBos.get(i));
                sum1+=sumx;
            }
            System.err.println(sum1);
            double s = getWorkHours(sum1);
            sum = String.valueOf(s);
            System.err.println(sum);
        }
        demandHoursRspBO.setListSum(listSumBos);
        demandHoursRspBO.setSum(sum);
        demandHoursRspBO.setStringList(workingHoursBOS);
        System.err.println(demandHoursRspBO);
        return demandHoursRspBO;
    }

    @Override
    public DemandHoursRspBO getWorkLoad(String epic){
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        System.err.println(epic+"功能点=========================");
        DemandDO demand = demandDao.get(epic);
        String str =  getWorkLoad(demand);
        if(str== null || "".equals(str)){
            System.err.println("未录入功能点");
            return demandHoursRspBO;
        }
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        String sum = "";
        String[] pro_number_list=str.split(";");
        System.err.println(pro_number_list);
        double sumx = 0;
        for(int i =0;i<pro_number_list.length;i++){
            System.err.println(pro_number_list[i]);
            String[] dept_list=pro_number_list[i].split(":");
            System.err.println(dept_list[0]);
            System.err.println(dept_list[1]);
            SumBos.add(dept_list[0]);
            String dept = "{'value': '"+dept_list[1]+"', 'name': '"+dept_list[0]+"'}";
            workingHoursBOS.add(dept);
            sumx+=Double.valueOf(dept_list[1]);
        }
        sum = String.valueOf(sumx);
        System.err.println(sum);
        demandHoursRspBO.setListSum(SumBos);
        demandHoursRspBO.setSum(sum);
        demandHoursRspBO.setStringList(workingHoursBOS);
        System.err.println(demandHoursRspBO);
        return demandHoursRspBO;
    }

	/**
	 * 根据epic获取缺陷类型数
	 * @param epic
	 * @return
	 */
	@Override
	public DemandHoursRspBO getFlawNumber(String epic){
		DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        ProblemStatisticDO problemStatisticDO = problemStatisticDao.get(epic);
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        String sum = "";
        int sumx=0;
        /**
         * @Fields externalDefectsNumber 外围平台缺陷数
         */
        if(problemStatisticDO.getExternalDefectsNumber()!= 0){
            SumBos.add("外围平台缺陷数");
            workingHoursBOS.add(problemStatisticDO.getExternalDefectsNumber()+"");
            sumx = sumx + problemStatisticDO.getExternalDefectsNumber();
        }
        /**
         * @Fields versionDefectsNumber 版本更新缺陷数
         */
        if(problemStatisticDO.getVersionDefectsNumber()!= 0){
            SumBos.add("版本更新缺陷数");
            workingHoursBOS.add(problemStatisticDO.getVersionDefectsNumber()+"");
            sumx = sumx + problemStatisticDO.getVersionDefectsNumber();
        }
        /**
         * @Fields parameterDefectsNumber 参数配置缺陷数
         */
        if(problemStatisticDO.getParameterDefectsNumber()!= 0){
            SumBos.add("参数配置缺陷数");
            workingHoursBOS.add(problemStatisticDO.getParameterDefectsNumber()+"");
            sumx = sumx + problemStatisticDO.getParameterDefectsNumber();
        }
        /**
         * @Fields functionDefectsNumber 功能设计缺陷数
         */
        if(problemStatisticDO.getFunctionDefectsNumber()!= 0){
            SumBos.add("功能设计缺陷数");
            workingHoursBOS.add(problemStatisticDO.getFunctionDefectsNumber()+"");
            sumx = sumx + problemStatisticDO.getFunctionDefectsNumber();
        }
        /**
         * @Fields processDefectsNumber 流程优化缺陷数
         */
        if(problemStatisticDO.getProcessDefectsNumber()!= 0){
            SumBos.add("流程优化缺陷数");
            workingHoursBOS.add(problemStatisticDO.getProcessDefectsNumber()+"");
            sumx = sumx +problemStatisticDO.getProcessDefectsNumber();
        }
        /**
         * @Fields promptDefectsNumber 提示语优化缺陷数
         */
        if(problemStatisticDO.getPromptDefectsNumber()!= 0){
            SumBos.add("提示语优化缺陷数");
            workingHoursBOS.add(problemStatisticDO.getPromptDefectsNumber()+"");
            sumx = sumx + problemStatisticDO.getPromptDefectsNumber();
        }
        /**
         * @Fields pageDefectsNumber 页面设计缺陷数
         */
        if(problemStatisticDO.getPageDefectsNumber()!= 0){
            SumBos.add("页面设计缺陷数");
            workingHoursBOS.add(problemStatisticDO.getPageDefectsNumber()+"");
            sumx = sumx + problemStatisticDO.getPageDefectsNumber();
        }
        /**
         * @Fields backgroundDefectsNumber 后台应用缺陷数
         */
        if(problemStatisticDO.getBackgroundDefectsNumber()!= 0){
            SumBos.add("后台应用缺陷数");
            workingHoursBOS.add(problemStatisticDO.getBackgroundDefectsNumber()+"");
            sumx = sumx + problemStatisticDO.getBackgroundDefectsNumber();
        }
        /**
         * @Fields modifyDefectsNumber 修改引入问题缺陷数
         */
        if(problemStatisticDO.getModifyDefectsNumber() != 0){
            SumBos.add("修改引入问题缺陷数");
            workingHoursBOS.add(problemStatisticDO.getModifyDefectsNumber()+"");
            sumx = sumx + problemStatisticDO.getModifyDefectsNumber();
        }
        /**
         * @Fields designDefectsNumber 技术设计缺陷数
         */
        if(problemStatisticDO.getDesignDefectsNumber()!= 0){
            SumBos.add("技术设计缺陷数");
            workingHoursBOS.add(problemStatisticDO.getDesignDefectsNumber()+"");
            sumx = sumx + problemStatisticDO.getDesignDefectsNumber();
        }
        /**
         * @Fields invalidDefectsNumber 无效问题数
         */
        if(problemStatisticDO.getInvalidDefectsNumber() != 0){
            SumBos.add("无效问题数");
            workingHoursBOS.add(problemStatisticDO.getInvalidDefectsNumber()+"");
            sumx = sumx + problemStatisticDO.getInvalidDefectsNumber();
        }
        sum =String.valueOf(sumx);
        demandHoursRspBO.setListSum(SumBos);
        demandHoursRspBO.setSum(sum);
        demandHoursRspBO.setStringList(workingHoursBOS);
		return demandHoursRspBO;
	}

	/**
	 * 根据epic获取评审问题类型数
	 * @param epic
	 * @return
	 */
	@Override
	public DemandHoursRspBO getReviewNumber(String epic){
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        ProblemStatisticDO problemStatisticDO = problemStatisticDao.get(epic);
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        String sum = "";
        int sumx=0;
        /**
         * @Fields requirementsReviewNumber 需求评审问题数
         */
        if(problemStatisticDO.getRequirementsReviewNumber()!= 0){
            SumBos.add("需求评审问题数");
            workingHoursBOS.add(problemStatisticDO.getRequirementsReviewNumber()+"");
            sumx = sumx + problemStatisticDO.getRequirementsReviewNumber();
        }
        /**
         * @Fields codeReviewNumber 代码评审问题数
         */
        if(problemStatisticDO.getCodeReviewNumber()!= 0){
            SumBos.add("代码评审问题数");
            workingHoursBOS.add(problemStatisticDO.getCodeReviewNumber()+"");
            sumx = sumx + problemStatisticDO.getCodeReviewNumber();
        }
        /**
         * @Fields testReviewNumber 测试案例评审问题数
         */
        if(problemStatisticDO.getTestReviewNumber()!= 0){
            SumBos.add("测试案例评审问题数");
            workingHoursBOS.add(problemStatisticDO.getTestReviewNumber()+"");
            sumx = sumx + problemStatisticDO.getTestReviewNumber();
        }
        /**
         * @Fields productionReviewNumber 投产方案评审问题数
         */
        if(problemStatisticDO.getProductionReviewNumber()!= 0){
            SumBos.add("投产方案评审问题数");
            workingHoursBOS.add(problemStatisticDO.getProductionReviewNumber()+"");
            sumx = sumx + problemStatisticDO.getProductionReviewNumber();
        }
        /**
         * @Fields technicalReviewNumber 技术方案评审问题数
         */
        if(problemStatisticDO.getTechnicalReviewNumber()!= 0){
            SumBos.add("技术方案评审问题数");
            workingHoursBOS.add(problemStatisticDO.getTechnicalReviewNumber()+"");
            sumx = sumx + problemStatisticDO.getTechnicalReviewNumber();
        }
        sum =String.valueOf(sumx);
        demandHoursRspBO.setListSum(SumBos);
        demandHoursRspBO.setSum(sum);
        demandHoursRspBO.setStringList(workingHoursBOS);
        return demandHoursRspBO;
	}

	/**
	 * 毫秒转人天
	 * @param time
	 * @return
	 */
    public Double getWorkHours(int time){
        return (double) (Math.round(time* 100 /  28800)/ 100.0);
    }

    /**
     * 毫秒转小时
     * @param time
     * @return
     */
    public Double getWorkHoursTime(int time){
        return (double) (Math.round(time* 100 /  3600)/ 100.0);
    }

    /**
     * 获取工作量
     * @param demand
     * @return
     */
    public  String getWorkLoad(DemandDO demand) {
        if (com.cmpay.lemon.common.utils.StringUtils.isNotEmpty(demand.getDevpLeadDept()) && com.cmpay.lemon.common.utils.StringUtils.isEmpty(demand.getDevpCoorDept())) {
            demand.setLeadDeptPro(demand.getDevpLeadDept()+":100%;");
            if (demand.getTotalWorkload() == 0){
                demand.setLeadDeptWorkload(demand.getDevpLeadDept()+":0.00;");
            }else {
                demand.setLeadDeptWorkload(demand.getDevpLeadDept()+":"+String.format("%.2f",Double.valueOf(demand.getTotalWorkload()))+";");
            }
            //updateReqWorkLoad(demand);
        }
        //本月工作量
        int monInputWorkload = demand.getMonInputWorkload()+demand.getInputWorkload();
        //主导部门本月工作量
        String LeadDeptCurMonWorkLoad = "";
        String lead = demand.getLeadDeptPro();
        String req_sts = demand.getReqSts();
        // && !("30".equals(req_sts)) 去调判断状态为取消
        if(com.cmpay.lemon.common.utils.StringUtils.isNotBlank(lead) && monInputWorkload != 0  ){
            String[] leadSplit = lead.replaceAll("%", "").split(":");
            leadSplit[1] = leadSplit[1].replaceAll(";","");
            LeadDeptCurMonWorkLoad = leadSplit[0]+":"+String.format("%.2f",(Double.valueOf(leadSplit[1])/100)*monInputWorkload)+";";
        }

        //配合部门本月工作量
        String CoorDevpCurMonWorkLoad = "";
        //配合工作量百分比
        String CoorDevpPer = "";
        String[] coorList = new String[20];
        String coor = demand.getCoorDeptPro();
        // && !("30".equals(req_sts)) 去调判断状态为取消
        if (com.cmpay.lemon.common.utils.StringUtils.isNotBlank(coor) && monInputWorkload != 0 ){
            coorList = demand.getCoorDeptPro().split(";");
            for (int i = 0; i < coorList.length;i++){
                if (com.cmpay.lemon.common.utils.StringUtils.isNotBlank(coorList[i])){
                    String[] CoorDevpCurMonWorkLoadSplit = coorList[i].split(":");
                    if (com.cmpay.lemon.common.utils.StringUtils.isNotBlank(CoorDevpCurMonWorkLoadSplit[0]) && com.cmpay.lemon.common.utils.StringUtils.isNotBlank(CoorDevpCurMonWorkLoadSplit[1])){
                        CoorDevpPer = String.format("%.2f",((Double.valueOf(CoorDevpCurMonWorkLoadSplit[1].replaceAll("%", "")))/100)*monInputWorkload);
                        CoorDevpCurMonWorkLoad += CoorDevpCurMonWorkLoadSplit[0]+":"+CoorDevpPer+";";
                    }
                }
            }
        }
        DemandHoursRspBO demand1 = new DemandHoursRspBO();
        String str ="";
        str = LeadDeptCurMonWorkLoad+CoorDevpCurMonWorkLoad;
        return  str;
    }
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
	public void downloadDemandUploadDocumentBO(String month,String devpLeadDept,String productMng,String firstLevelOrganization ,HttpServletResponse response) {
		List<DemandUploadDocumentBO> DemandTypeStatisticsBOList = new ArrayList<>();
		List<DemandBO> reportLista = new ArrayList<>();
		reportLista = this.getReportForm6(month, devpLeadDept, productMng, firstLevelOrganization);
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

