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
import com.cmpay.lemon.monitor.service.production.OperationProductionService;
import com.cmpay.lemon.monitor.service.reportForm.ReqDataCountService;
import com.cmpay.lemon.monitor.service.workload.ReqWorkLoadService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import com.cmpay.lemon.monitor.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@Transactional(rollbackFor = Exception.class)
@Service
public class ReqDataCountServiceImpl implements ReqDataCountService {
    //30 需求状态为暂停
    private static final String REQSUSPEND = "30";
    //40 需求状态为取消
    private static final String REQCANCEL = "40";
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
    private static final String FINISHPRD1 = "180";

    private static final String XINGQISAN = "星期三";

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
    IJiraBasicInfoDao jiraBasicInfoDao;
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
    private ReqWorkLoadService reqWorkLoadService;
    @Autowired
    private IProCheckTimeOutStatisticsExtDao proCheckTimeOutStatisticsExtDao;
    @Autowired
    private IProUnhandledIssuesExtDao proUnhandledIssuesExtDao;
    @Autowired
    private IOperationProductionDao iOperationProductionDao;
    @Autowired
    private IBuildFailedCountDao iBuildFailedCountDao;
    @Autowired
    private ISmokeTestFailedCountExtDao iSmokeTestFailedCountDao;
    @Autowired
    private IOrganizationStructureDao iOrganizationStructureDao;
    @Autowired
    private IDictionaryExtDao dictionaryDao;
    @Autowired
    private IUserExtDao iUserDao;
    @Autowired
    private ITestProgressDetailExtDao testProgressDetailExtDao;
    @Autowired
    private IMonthWorkdayDao monthWorkdayDao;
    @Autowired
    private  IProductionVerificationIsNotTimelyExtDao iProductionVerificationIsNotTimelyExtDao;
    @Resource
    private IDemandEaseDevelopmentExtDao easeDevelopmentExtDao;
    @Autowired
    private IProblemExtDao iProblemDao;


    /**
     * 本月需求完成情况：进行中、已投产需求数
     */
    @Override
    public Map getProg(String report_start_mon, String report_end_mon) {
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
    public Map getProgDetl(String report_start_mon, String report_end_mon) {
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
     *
     * @param DataMap
     * @return jsonStr
     */
    public String strToJson(Map DataMap) {
        String bigJsonStr = "";

        Set treeSet = new TreeSet();
        treeSet = DataMap.keySet();

        String smallJson = "";

        StringBuilder bigJson = new StringBuilder();

        for (Iterator localIterator = treeSet.iterator(); localIterator.hasNext(); ) {
            String amtSrc = (String) localIterator.next();

            smallJson = new StringBuilder().append("{\"value\":\"").append((String) DataMap.get(amtSrc)).append("\",\"name\":\"").append(amtSrc).append("\"},").toString();
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
        impl.forEach(m ->
                reqDataCountBOS.add(BeanUtils.copyPropertiesReturnDest(new ReqDataCountBO(), m))
        );

        return reqDataCountBOS;
    }

    @Override
    public List<ReqDataCountBO> getImplByDept(String reqImplMon) {
        List<ReqDataCountBO> reqDataCountBOS = new LinkedList<>();
        reqDataCountDao.getImplByDept(reqImplMon).forEach(m ->
                reqDataCountBOS.add(BeanUtils.copyPropertiesReturnDest(new ReqDataCountBO(), m))
        );
        return reqDataCountBOS;
    }

    @Override
    public List<ReqDataCountBO> getComp(String reqImplMon) {
        List<ReqDataCountBO> reqDataCountBOS = new LinkedList<>();
        List<ReqDataCountDO> comp = reqDataCountDao.getComp(reqImplMon);
        comp.forEach(m ->
                reqDataCountBOS.add(BeanUtils.copyPropertiesReturnDest(new ReqDataCountBO(), m))
        );
        return reqDataCountBOS;
    }

    @Override
    public List<ReqDataCountBO> getCompByDept(String reqImplMon) {
        List<ReqDataCountBO> reqDataCountBOS = new LinkedList<>();
        reqDataCountDao.getCompByDept(reqImplMon).forEach(m ->
                reqDataCountBOS.add(BeanUtils.copyPropertiesReturnDest(new ReqDataCountBO(), m))
        );

        return reqDataCountBOS;
    }

    public List<ReqDataCountBO> getCompByDept2(String reqImplMon) {
        List<ReqDataCountBO> reqDataCountBOS = new LinkedList<>();
        reqDataCountDao.getCompByDept2(reqImplMon).forEach(m ->
                reqDataCountBOS.add(BeanUtils.copyPropertiesReturnDest(new ReqDataCountBO(), m))
        );

        return reqDataCountBOS;
    }

    @Override
    public List<ReqDataCountBO> getReqSts(String reqImplMon) {
        List<ReqDataCountBO> reqDataCountBOS = new LinkedList<>();
        List<ReqDataCountDO> impl = reqDataCountDao.getReqSts(reqImplMon);
        impl.forEach(m ->
                reqDataCountBOS.add(BeanUtils.copyPropertiesReturnDest(new ReqDataCountBO(), m))
        );
        return reqDataCountBOS;
    }

    @Override
    public List<ScheduleBO> getProduction(String reqImplMon) {
        List<ScheduleBO> reqDataCountBOS = new LinkedList<>();
        List<ScheduleDO> impl = reqDataCountDao.getProduction(reqImplMon);
        impl.forEach(m ->
                reqDataCountBOS.add(BeanUtils.copyPropertiesReturnDest(new ScheduleBO(), m))
        );
        return reqDataCountBOS;
    }
    @Override
    public List<DemandBO> getReportForm6(String reqImplMon, String devpLeadDept, String productMng, String firstLevelOrganization) {
        List<DemandBO> reqDataCountBOS = new LinkedList<>();
        ReqDateCountDO reqDateCountDO = new ReqDateCountDO();
        reqDateCountDO.setReqImplMon(reqImplMon);
        reqDateCountDO.setDevpLeadDept(devpLeadDept);
        reqDateCountDO.setProductMng(productMng);
        reqDateCountDO.setFirstLevelOrganization(firstLevelOrganization);
        List<DemandDO> impl = reqDataCountDao.getReportForm6(reqDateCountDO);
        impl.forEach(m ->
                reqDataCountBOS.add(BeanUtils.copyPropertiesReturnDest(new DemandBO(), m))
        );
        for (int i = 0; i < reqDataCountBOS.size(); i++) {
            int sum = 0;
            //如果当前阶段为已完成，则需要上次6个文档
            if (FINISHPRD == Integer.parseInt(reqDataCountBOS.get(i).getPreCurPeriod2())) {
                if (reqDataCountBOS.get(i).getActPrdUploadTm() == null || reqDataCountBOS.get(i).getActPrdUploadTm() == "") {
                    sum = sum + 1;
                }
                if (reqDataCountBOS.get(i).getActWorkloadUploadTm() == null || reqDataCountBOS.get(i).getActWorkloadUploadTm() == "") {
                    sum = sum + 1;
                }
                if (reqDataCountBOS.get(i).getActSitUploadTm() == null || reqDataCountBOS.get(i).getActSitUploadTm() == "") {
                    sum = sum + 1;
                }
//				if(reqDataCountBOS.get(i).getActUatUploadTm()==null || reqDataCountBOS.get(i).getActUatUploadTm()==""){
//					sum=sum+1;
//				}
                if (reqDataCountBOS.get(i).getActPreUploadTm() == null || reqDataCountBOS.get(i).getActPreUploadTm() == "") {
                    sum = sum + 1;
                }
                if (reqDataCountBOS.get(i).getActProductionUploadTm() == null || reqDataCountBOS.get(i).getActProductionUploadTm() == "") {
                    sum = sum + 1;
                }
            }
            //预投产
            else if (FINISHPRETEST <= Integer.parseInt(reqDataCountBOS.get(i).getPreCurPeriod2())  && FINISHPRD > Integer.parseInt(reqDataCountBOS.get(i).getPreCurPeriod2())) {
                if (reqDataCountBOS.get(i).getActPrdUploadTm() == null || reqDataCountBOS.get(i).getActPrdUploadTm() == "") {
                    sum = sum + 1;
                }
                if (reqDataCountBOS.get(i).getActWorkloadUploadTm() == null || reqDataCountBOS.get(i).getActWorkloadUploadTm() == "") {
                    sum = sum + 1;
                }
                if (reqDataCountBOS.get(i).getActSitUploadTm() == null || reqDataCountBOS.get(i).getActSitUploadTm() == "") {
                    sum = sum + 1;
                }
//				if(reqDataCountBOS.get(i).getActUatUploadTm()==null || reqDataCountBOS.get(i).getActUatUploadTm()==""){
//                    sum=sum+1;
//				}
                if (reqDataCountBOS.get(i).getActPreUploadTm() == null || reqDataCountBOS.get(i).getActPreUploadTm() == "") {
                    sum = sum + 1;
                }
            }
            // uat
            else if (FINISHUATTEST <= Integer.parseInt(reqDataCountBOS.get(i).getPreCurPeriod2())&& FINISHPRETEST > Integer.parseInt(reqDataCountBOS.get(i).getPreCurPeriod2())) {
                if (reqDataCountBOS.get(i).getActPrdUploadTm() == null || reqDataCountBOS.get(i).getActPrdUploadTm() == "") {
                    sum = sum + 1;
                }
                if (reqDataCountBOS.get(i).getActWorkloadUploadTm() == null || reqDataCountBOS.get(i).getActWorkloadUploadTm() == "") {
                    sum = sum + 1;
                }
                if (reqDataCountBOS.get(i).getActSitUploadTm() == null || reqDataCountBOS.get(i).getActSitUploadTm() == "") {
                    sum = sum + 1;
                }
//				if(reqDataCountBOS.get(i).getActUatUploadTm()==null || reqDataCountBOS.get(i).getActUatUploadTm()==""){
//                    sum=sum+1;
//				}
            }
            // sit
            else if (FINISHSITTEST <= Integer.parseInt(reqDataCountBOS.get(i).getPreCurPeriod2()) && FINISHUATTEST > Integer.parseInt(reqDataCountBOS.get(i).getPreCurPeriod2())) {
                if (reqDataCountBOS.get(i).getActPrdUploadTm() == null || reqDataCountBOS.get(i).getActPrdUploadTm() == "") {
                    sum = sum + 1;
                }
                if (reqDataCountBOS.get(i).getActWorkloadUploadTm() == null || reqDataCountBOS.get(i).getActWorkloadUploadTm() == "") {
                    sum = sum + 1;
                }
                if (reqDataCountBOS.get(i).getActSitUploadTm() == null || reqDataCountBOS.get(i).getActSitUploadTm() == "") {
                    sum = sum + 1;
                }
            }
            // 技术文档
            else if (TECHDOCCONFIRM <= Integer.parseInt(reqDataCountBOS.get(i).getPreCurPeriod2()) && FINISHSITTEST > Integer.parseInt(reqDataCountBOS.get(i).getPreCurPeriod2())) {
                if (reqDataCountBOS.get(i).getActPrdUploadTm() == null || reqDataCountBOS.get(i).getActPrdUploadTm() == "") {
                    sum = sum + 1;
                }
                if (reqDataCountBOS.get(i).getActWorkloadUploadTm() == null || reqDataCountBOS.get(i).getActWorkloadUploadTm() == "") {
                    sum = sum + 1;
                }
            }
            // 需求定搞
            else if (REQCONFIRM <= Integer.parseInt(reqDataCountBOS.get(i).getPreCurPeriod2())) {
                if (reqDataCountBOS.get(i).getActPrdUploadTm() == null || reqDataCountBOS.get(i).getActPrdUploadTm() == "") {
                    sum = sum + 1;
                }
            }
            reqDataCountBOS.get(i).setNoUpload(sum);
        }
        return reqDataCountBOS;
    }

    @Override
    public List<WorkingHoursBO> getReportForm7(String devpLeadDept, String date, String date1, String date2) {
        List<WorkingHoursBO> workingHoursBOS = new LinkedList<>();
        List<WorkingHoursDO> impl = null;
        WorkingHoursDO workingHoursDO = new WorkingHoursDO();
        workingHoursDO.setDevpLeadDept(devpLeadDept);
        if (StringUtils.isBlank(date) && StringUtils.isBlank(date1) && StringUtils.isBlank(date2)) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如日期、周、月!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        if (StringUtils.isNotBlank(date) && StringUtils.isBlank(date1) && StringUtils.isBlank(date2)) {
            workingHoursDO.setSelectTime(date);
            impl = iWorkingHoursDao.findSum(workingHoursDO);
        }
        if (StringUtils.isNotBlank(date1) && StringUtils.isBlank(date) && StringUtils.isBlank(date2)) {
            workingHoursDO.setSelectTime(date1);
            impl = iWorkingHoursDao.findWeekSum(workingHoursDO);
        }
        if (StringUtils.isNotBlank(date2) && StringUtils.isBlank(date) && StringUtils.isBlank(date1)) {
            workingHoursDO.setSelectTime(date2);
            impl = iWorkingHoursDao.findMonthSum(workingHoursDO);
            // 获取当前月份的天数
            int day = DateUtil.getDaysByYearMonth(date2);
            for(int j=0;j<impl.size();j++){
                List<String> listDay = new LinkedList<>();
                for(int i=1;i<=day;i++){
                    WorkingHoursDO workingHoursDO1 = new WorkingHoursDO();
                    if(i<10){
                        String day1 = date2 +"-0"+i ;
                        workingHoursDO1.setSelectTime(day1);
                    }
                    if(i>=10){
                        String day1 = date2 +"-"+ i;
                        workingHoursDO1.setSelectTime(day1);
                    }
                    workingHoursDO1.setDisplayname(impl.get(j).getDisplayname());
                    workingHoursDO1.setDevpLeadDept(devpLeadDept);
                    List<WorkingHoursDO> impl1 = iWorkingHoursDao.findSum(workingHoursDO1);
                    if(impl1.isEmpty()){
                        listDay.add("0");
                    }else{
                        listDay.add(getWorkHours(impl1.get(0).getSumTime())+"");
                    }

                }
                impl.get(j).setListDay(listDay);
                impl.get(j).setSumTime(getWorkHours(impl.get(j).getSumTime())+"");
            }


        }
        impl.forEach(m ->
                workingHoursBOS.add(BeanUtils.copyPropertiesReturnDest(new WorkingHoursBO(), m))
        );
        return workingHoursBOS;
    }
    public Double getWorkHours(String value){
        Long time = Long.parseLong(value);
        return (double) (Math.round(time* 100 /  3600)/ 100.0);
    }
    @Override
    public List<WorkingHoursBO> getReportForm7B(String devpLeadDept, String date, String date1, String date2) {
        List<WorkingHoursBO> workingHoursBOS = new LinkedList<>();
        List<WorkingHoursDO> impl = null;
        WorkingHoursDO workingHoursDO = new WorkingHoursDO();
        workingHoursDO.setDevpLeadDept(devpLeadDept);
        if (StringUtils.isBlank(date) && StringUtils.isBlank(date1) && StringUtils.isBlank(date2)) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如日期、周、月!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        if (StringUtils.isNotBlank(date) && StringUtils.isBlank(date1) && StringUtils.isBlank(date2)) {
            workingHoursDO.setSelectTime(date);
            impl = iWorkingHoursDao.findSumB(workingHoursDO);
        }
        if (StringUtils.isNotBlank(date1) && StringUtils.isBlank(date) && StringUtils.isBlank(date2)) {
            workingHoursDO.setSelectTime(date1);
            impl = iWorkingHoursDao.findWeekSumB(workingHoursDO);
        }
        if (StringUtils.isNotBlank(date2) && StringUtils.isBlank(date) && StringUtils.isBlank(date1)) {
            workingHoursDO.setSelectTime(date2);
            impl = iWorkingHoursDao.findMonthSumB(workingHoursDO);
        }

        impl.forEach(m ->
                workingHoursBOS.add(BeanUtils.copyPropertiesReturnDest(new WorkingHoursBO(), m))
        );
        return workingHoursBOS;
    }

    @Override
    public List<String> getReportForm8(String date) {
        List<String> workingHoursBOS = new LinkedList<>();
        List<DepartmentWorkDO> impl = iWorkingHoursDao.findDeptHours(date);
        impl.forEach(m ->
                workingHoursBOS.add(m.getWorkHoursToString())
        );
        return workingHoursBOS;
    }

    ;

    @Override
    public List<WorkingHoursBO> findEpicKeyHours(String epic) {
        List<WorkingHoursBO> workingHoursBOS = new LinkedList<>();
        System.err.println(epic);
        List<WorkingHoursDO> impl = iWorkingHoursDao.findEpicKeyHours(epic);
        System.err.println(impl);
        impl.forEach(m ->
                workingHoursBOS.add(BeanUtils.copyPropertiesReturnDest(new WorkingHoursBO(), m))
        );
        System.err.println(workingHoursBOS);
        return workingHoursBOS;
    }

    @Override
    public DemandQualityBO findDemandQuality(String epic) {
        DemandQualityBO demandQualityBO = new DemandQualityBO();
        DemandQualityDO demandQualityDO = iDemandQualityDao.get(epic);
        if (demandQualityDO == null || "".equals(demandQualityBO)) {
            return demandQualityBO;
        }
        BeanUtils.copyPropertiesReturnDest(demandQualityBO, demandQualityDO);
        return demandQualityBO;
    }

    @Override
    public WorkingHoursBO getReportForm10(String devpLeadDept, String date1, String date2) {
        WorkingHoursBO workingHoursBO = new WorkingHoursBO();
        List<WorkingHoursDO> impl = null;
        WorkingHoursDO workingHoursDO = new WorkingHoursDO();
        workingHoursDO.setDevpLeadDept(devpLeadDept);
        if (StringUtils.isBlank(date1) && StringUtils.isBlank(date2)) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如周、月!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        impl = iWorkingHoursDao.findDeptView(workingHoursDO);
        if (impl != null && impl.size() >= 0) {
            BeanUtils.copyPropertiesReturnDest(workingHoursBO, impl.get(0));
            DefectDetailsDO defectDetailsDO = new DefectDetailsDO();
            if (StringUtils.isNotBlank(date1) && StringUtils.isBlank(date2)) {
                defectDetailsDO.setRegistrationDate(date1.substring(0,7));
            }
            if (StringUtils.isNotBlank(date2) && StringUtils.isBlank(date1)) {
                defectDetailsDO.setRegistrationDate(date2);
            }
            List<DefectDetailsDO> list = defectDetailsExtDao.findList(defectDetailsDO);
            if(list == null ||list.size()<0){
                workingHoursBO.setMeanDefect("0");
            }else{
                //获取当前月份
                Date date = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
                String month = simpleDateFormat.format(date);
                int day = 1;
                //如果选择的月份是当前月
                if(month.equals(defectDetailsDO.getRegistrationDate())){
                    day = DateUtil.getDaysByMonth();
                }else{
                    day = DateUtil.getDaysByYearMonth(defectDetailsDO.getRegistrationDate());
                }
                // 获取当前月份的天数

                int a = list.size();
                int b = Integer.parseInt(workingHoursBO.getSumDept()) * day;
                //保留两位小数
                DecimalFormat df = new DecimalFormat("0.##");
                String meanDefect = df.format((float)a/b);
                workingHoursBO.setMeanDefect(meanDefect);
            }
        }
        System.err.println(workingHoursBO);
        return workingHoursBO;
    }

    /**
     * 团队视图 柱状图
     *
     * @param devpLeadDept
     * @param date1
     * @param date2
     * @return
     */
    @Override
    public DemandHoursRspBO getDeptWorkHours(String devpLeadDept, String date1, String date2) {
        WorkingHoursDO workingHoursDO = new WorkingHoursDO();
        workingHoursDO.setDevpLeadDept(devpLeadDept);
        List<WorkingHoursDO> impl = null;
        System.err.println(date1 + "=====" + date2);
        if (StringUtils.isBlank(date1) && StringUtils.isBlank(date2)) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如周、月!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        // 查询周
        if (StringUtils.isNotBlank(date1) && StringUtils.isBlank(date2)) {
            workingHoursDO.setSelectTime(date1);
            impl = iWorkingHoursDao.findWeekSum(workingHoursDO);
            System.err.println(impl);
        }
        // 查询月
        if (StringUtils.isNotBlank(date2) && StringUtils.isBlank(date1)) {
            workingHoursDO.setSelectTime(date2);
            impl = iWorkingHoursDao.findMonthSum(workingHoursDO);
            System.err.println(impl);
        }
        System.err.println(impl);
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        String sum = "";
        double sumx = 0;
        DecimalFormat df = new DecimalFormat("0.##");
        if (impl != null && impl.size() >= 0) {
            for (int i = 0; i < impl.size(); i++) {
                SumBos.add(impl.get(i).getDisplayname());
                sumx = sumx + getWorkHoursTime(Integer.parseInt(impl.get(i).getSumTime()));
                workingHoursBOS.add(df.format(getWorkHoursTime(Integer.parseInt(impl.get(i).getSumTime()))));
            }
            sum = df.format(sumx);
        }
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO.setSum(sum);
        demandHoursRspBO.setListSum(SumBos);
        demandHoursRspBO.setStringList(workingHoursBOS);
        System.err.println(demandHoursRspBO);
        return demandHoursRspBO;
    }

    @Override
    public DemandHoursRspBO getDeptWorkHoursAndPoint(String devpLeadDept, String date1, String date2) {
        WorkingHoursDO workingHoursDO = new WorkingHoursDO();
        DecimalFormat df = new DecimalFormat("0");
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        workingHoursDO.setDevpLeadDept(devpLeadDept);
        List<WorkingHoursDO> impl = null;
        DemandBO demandBO = new DemandBO();
        demandBO.setDevpLeadDept(devpLeadDept);
        if (StringUtils.isBlank(date1) && StringUtils.isBlank(date2)) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如周、月!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        // 查询周
        if (StringUtils.isNotBlank(date1) && StringUtils.isBlank(date2)) {
            workingHoursDO.setSelectTime(date1);
            impl = iWorkingHoursDao.findWeekSumB(workingHoursDO);
            //demandBO.setReqImplMon(date1.substring(0,7));
            if (impl != null && impl.size() >= 0) {
                workingHoursBOS.add("工时");
                if (!"0".equals(impl.get(0).getSumTime())) {
                    SumBos.add(df.format(getWorkHours(Integer.parseInt(impl.get(0).getSumTime()))));
                } else {
                    SumBos.add("0");
                }
            } else {
                workingHoursBOS.add("工时");
                SumBos.add("0");
            }
//            // 获取当月的功能点
//            System.err.println(demandBO);
//            List<Double> dept =reqWorkLoadService.getExportCountForDevp(demandBO);
//            if(dept.get(0) != null){
//                workingHoursBOS.add("功能点");
//                SumBos.add(Double.toString(dept.get(0)));
//            }else {
            workingHoursBOS.add("功能点");
            SumBos.add("0");
//            }
        }
        // 查询月
        if (StringUtils.isNotBlank(date2) && StringUtils.isBlank(date1)) {
            workingHoursDO.setSelectTime(date2);
            impl = iWorkingHoursDao.findMonthSumB(workingHoursDO);
            demandBO.setReqImplMon(date2);

            if (impl != null && impl.size() >= 0) {
                workingHoursBOS.add("工时");
                if (!"0".equals(impl.get(0).getSumTime())) {
                    SumBos.add(df.format(getWorkHours(Integer.parseInt(impl.get(0).getSumTime()))));
                } else {
                    SumBos.add("0");
                }
            } else {
                workingHoursBOS.add("工时");
                SumBos.add("0");
            }
            // 获取当月的功能点
            List<Double> dept = reqWorkLoadService.getExportCountForDevp(demandBO);
            if (dept.get(0) != null) {
                workingHoursBOS.add("功能点");
                SumBos.add(df.format(dept.get(0)));
            } else {
                workingHoursBOS.add("功能点");
                SumBos.add("0");
            }
        }

        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO.setStringList(workingHoursBOS);
        demandHoursRspBO.setListSum(SumBos);
        return demandHoursRspBO;
    }

    public DemandHoursRspBO getDeptWorkHoursAndPoint2(String devpLeadDept, String date1, String date2) {
        WorkingHoursDO workingHoursDO = new WorkingHoursDO();
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        workingHoursDO.setDevpLeadDept(devpLeadDept);
        List<WorkingHoursDO> impl = null;
        System.err.println(date1 + "=====" + date2);
        DemandBO demandBO = new DemandBO();
        demandBO.setDevpLeadDept(devpLeadDept);
        if (StringUtils.isBlank(date1) && StringUtils.isBlank(date2)) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如周、月!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        // 查询周
        if (StringUtils.isNotBlank(date1) && StringUtils.isBlank(date2)) {
            workingHoursDO.setSelectTime(date1);
            impl = iWorkingHoursDao.findWeekSumB(workingHoursDO);
            //demandBO.setReqImplMon(date1.substring(0,7));
            System.err.println(impl);
            if (impl != null && impl.size() >= 0) {
                workingHoursBOS.add("工时");
                if (!"0".equals(impl.get(0).getSumTime())) {
                    DecimalFormat df = new DecimalFormat("0");
                    SumBos.add(df.format(getWorkHoursTime(Integer.parseInt(impl.get(0).getSumTime()))));
                } else {
                    SumBos.add("0");
                }
            } else {
                workingHoursBOS.add("工时");
                SumBos.add("0");
            }
            workingHoursBOS.add("功能点");
            SumBos.add("0");
        }
        // 查询月
        if (StringUtils.isNotBlank(date2) && StringUtils.isBlank(date1)) {
            workingHoursDO.setSelectTime(date2);
            impl = iWorkingHoursDao.findMonthSumB(workingHoursDO);
            demandBO.setReqImplMon(date2);

            if (impl != null && impl.size() >= 0) {
                workingHoursBOS.add("工时");
                if (!"0".equals(impl.get(0).getSumTime())) {
                    DecimalFormat df = new DecimalFormat("0.##");
                    SumBos.add(df.format(getWorkHours(Integer.parseInt(impl.get(0).getSumTime()))));
                } else {
                    SumBos.add("0");
                }
            } else {
                workingHoursBOS.add("工时");
                SumBos.add("0");
            }
            // 获取当月的功能点
            System.err.println(demandBO);
            List<Double> dept = reqWorkLoadService.getExportCountForDevp(demandBO);
            if (dept.get(0) != null) {
                workingHoursBOS.add("功能点");
                DecimalFormat df = new DecimalFormat("0.##");
                SumBos.add(df.format(dept.get(0)));
            } else {
                workingHoursBOS.add("功能点");
                SumBos.add("0");
            }
        }

        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO.setStringList(workingHoursBOS);
        demandHoursRspBO.setListSum(SumBos);
        return demandHoursRspBO;
    }

    public DemandHoursRspBO getDeptWorkHoursAndPoint3(String devpLeadDept, String date1, String date2) {
        WorkingHoursDO workingHoursDO = new WorkingHoursDO();
        DecimalFormat df = new DecimalFormat("0.###");
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        workingHoursDO.setDevpLeadDept(devpLeadDept);
        List<WorkingHoursDO> impl = null;
        DemandBO demandBO = new DemandBO();
        demandBO.setDevpLeadDept(devpLeadDept);
        if (StringUtils.isBlank(date1) && StringUtils.isBlank(date2)) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如周、月!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        // 查询周
        if (StringUtils.isNotBlank(date1) && StringUtils.isBlank(date2)) {
            workingHoursDO.setSelectTime(date1);
            impl = iWorkingHoursDao.findWeekSumB(workingHoursDO);
            //demandBO.setReqImplMon(date1.substring(0,7));
            if (impl != null && impl.size() >= 0) {
                workingHoursBOS.add("工时");
                if (!"0".equals(impl.get(0).getSumTime())) {
                    SumBos.add(df.format(getWorkHours(Integer.parseInt(impl.get(0).getSumTime()))));
                } else {
                    SumBos.add("0");
                }
            } else {
                workingHoursBOS.add("工时");
                SumBos.add("0");
            }
//            // 获取当月的功能点
//            System.err.println(demandBO);
//            List<Double> dept =reqWorkLoadService.getExportCountForDevp(demandBO);
//            if(dept.get(0) != null){
//                workingHoursBOS.add("功能点");
//                SumBos.add(Double.toString(dept.get(0)));
//            }else {
            workingHoursBOS.add("功能点");
            SumBos.add("0");
//            }
        }
        // 查询月
        if (StringUtils.isNotBlank(date2) && StringUtils.isBlank(date1)) {
            workingHoursDO.setSelectTime(date2);
            impl = iWorkingHoursDao.findMonthSumB(workingHoursDO);
            demandBO.setReqImplMon(date2);

            if (impl != null && impl.size() >= 0) {
                workingHoursBOS.add("工时");
                if (!"0".equals(impl.get(0).getSumTime())) {
                    SumBos.add(df.format(getWorkHours(Integer.parseInt(impl.get(0).getSumTime()))));
                } else {
                    SumBos.add("0");
                }
            } else {
                workingHoursBOS.add("工时");
                SumBos.add("0");
            }
            // 获取当月的功能点
            List<Double> dept = reqWorkLoadService.getExportCountForDevp(demandBO);
            if (dept.get(0) != null) {
                workingHoursBOS.add("功能点");
                SumBos.add(df.format(dept.get(0)));
            } else {
                workingHoursBOS.add("功能点");
                SumBos.add("0");
            }
        }

        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO.setStringList(workingHoursBOS);
        demandHoursRspBO.setListSum(SumBos);
        return demandHoursRspBO;
    }

    @Override
    public DemandHoursRspBO getDeptProduction(String devpLeadDept, String date1, String date2) {
        WorkingHoursDO workingHoursDO = new WorkingHoursDO();
        workingHoursDO.setDevpLeadDept(devpLeadDept);
        List<ProductionDO> impl = null;
        System.err.println(date1 + "=====" + date2);
        if (StringUtils.isBlank(date1) && StringUtils.isBlank(date2)) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如周、月!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        // 查询周
        if (StringUtils.isNotBlank(date1) && StringUtils.isBlank(date2)) {
            workingHoursDO.setSelectTime(date1);
            impl = iWorkingHoursDao.findListDeptWeek(workingHoursDO);
            System.err.println(impl);
        }
        // 查询月
        if (StringUtils.isNotBlank(date2) && StringUtils.isBlank(date1)) {
            workingHoursDO.setSelectTime(date2);
            impl = iWorkingHoursDao.findListDeptMonth(workingHoursDO);
            System.err.println(impl);
        }
        System.err.println(impl);
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        String sum = "";
        int a = 0;
        int b = 0;
        int c = 0;
        if (impl != null && impl.size() >= 0) {
            System.err.println(impl.size());
            sum = impl.size() + "";
            for (int i = 0; i < impl.size(); i++) {
                if ("正常投产".equals(impl.get(i).getProType()) && "是".equals(impl.get(i).getIsOperationProduction())) {
                    a = a + 1;
                }
                if ("正常投产".equals(impl.get(i).getProType()) && "否".equals(impl.get(i).getIsOperationProduction())) {
                    b = b + 1;
                }
                if ("救火更新".equals(impl.get(i).getProType())) {
                    c = c + 1;
                }
            }
            if (a > 0) {
                String a1 = "{'value': '" + a + "', 'name': '正常投产'}";
                workingHoursBOS.add(a1);
                SumBos.add("正常投产");
            }
            if (b > 0) {
                String b1 = "{'value': '" + b + "', 'name': '非投产日正常投产'}";
                workingHoursBOS.add(b1);
                SumBos.add("非投产日正常投产");
            }
            if (c > 0) {
                String c1 = "{'value': '" + c + "', 'name': '救火更新'}";
                workingHoursBOS.add(c1);
                SumBos.add("救火更新");
            }
        } else {
            sum = "0";
        }
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO.setStringList(workingHoursBOS);
        demandHoursRspBO.setListSum(SumBos);
        demandHoursRspBO.setSum(sum);
        return demandHoursRspBO;
    }

    public static String testDate(String newtime) {
        String dayNames[] = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar c = Calendar.getInstance();// 获得一个日历的zhi实例
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            c.setTime(sdf.parse(newtime));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return dayNames[c.get(Calendar.DAY_OF_WEEK) - 1];
    }

    /**
     * 团队需求异常情况
     *
     * @param devpLeadDept
     * @param date1
     * @param date2
     * @return
     */
    @Override
    public DemandHoursRspBO getDemandDispose(String devpLeadDept, String date1, String date2) {
        List<DemandDO> impl = null;
        WorkingHoursDO workingHoursDO = new WorkingHoursDO();
        workingHoursDO.setDevpLeadDept(devpLeadDept);
        workingHoursDO.setSelectTime(date2);
        impl = demandDao.findListDeptDemand(workingHoursDO);
        System.err.println(impl);

        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        String sum = "";
        int a = 0;
        int b = 0;
        int c = 0;
        if (impl != null && impl.size() >= 0) {
            System.err.println(impl.size());
            sum = impl.size() + "";
            for (int i = 0; i < impl.size(); i++) {
                if (!impl.get(i).getReqSts().equals("30") && !impl.get(i).getReqSts().equals("40")) {
                    a = a + 1;
                }
                if (impl.get(i).getReqSts().equals("30")) {
                    b = b + 1;
                }
                if (impl.get(i).getReqSts().equals("40")) {
                    c = c + 1;
                }
            }
            if (a > 0) {
                String a1 = "{'value': '" + a + "', 'name': '正常需求'}";
                workingHoursBOS.add(a1);
                SumBos.add("正常需求");
            }
            if (b > 0) {
                String b1 = "{'value': '" + b + "', 'name': '取消需求'}";
                workingHoursBOS.add(b1);
                SumBos.add("取消需求");
            }
            if (c > 0) {
                String c1 = "{'value': '" + c + "', 'name': '暂停需求'}";
                workingHoursBOS.add(c1);
                SumBos.add("暂停需求");
            }
        } else {
            sum = "0";
        }
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO.setStringList(workingHoursBOS);
        demandHoursRspBO.setListSum(SumBos);
        demandHoursRspBO.setSum(sum);
        return demandHoursRspBO;
    }

    /**
     * 团队需求主导需求
     *
     * @param devpLeadDept
     * @param date1
     * @param date2
     * @return
     */
    @Override
    public DemandHoursRspBO getDemandCoordinate(String devpLeadDept, String date1, String date2) {
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
        if (impl != null && impl.size() >= 0) {
            workingHoursBOS.add(impl.size() + "");
            SumBos.add("主导需求");
            sum = sum + impl.size();
        } else {
            workingHoursBOS.add("0");
            SumBos.add("主导需求");
        }
        if (impl2 != null && impl2.size() != 0) {
            workingHoursBOS.add(impl2.size() + "");
            SumBos.add("配合需求");
            sum = sum + impl2.size();
        } else {
            workingHoursBOS.add("0");
            SumBos.add("配合需求");
        }
        String simx = sum + "";
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO.setStringList(workingHoursBOS);
        demandHoursRspBO.setListSum(SumBos);
        demandHoursRspBO.setSum(simx);
        return demandHoursRspBO;
    }

    /**
     * 按照月查询团队缺陷
     *
     * @param devpLeadDept
     * @param date1
     * @param date2
     * @return
     */
    @Override
    public DemandHoursRspBO getDeptFlawNumber(String devpLeadDept, String date1, String date2) {
        List<DefectDetailsDO> impl = null;
        DefectDetailsDO workingHoursDO = new DefectDetailsDO();
        if(!"产品测试团队".equals(devpLeadDept)){
            workingHoursDO.setProblemHandlerDepartment(devpLeadDept);
        }
        List<String> list = getSixMonth(date2);
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        for (int i = 0; i < list.size(); i++) {
            workingHoursDO.setRegistrationDate(list.get(i));
            impl = defectDetailsExtDao.findList(workingHoursDO);
            SumBos.add(list.get(i));
            if (impl != null && impl.size() >= 0) {
                workingHoursBOS.add(impl.size() + "");
            } else {
                workingHoursBOS.add("0");
            }

        }
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO.setStringList(workingHoursBOS);
        demandHoursRspBO.setListSum(SumBos);
        return demandHoursRspBO;
    }

    /**
     * 团队视图按月份查询评审问题
     *
     * @param devpLeadDept
     * @param date1
     * @param date2
     * @return
     */
    @Override
    public DemandHoursRspBO getDeptIssueNumber(String devpLeadDept, String date1, String date2) {
        List<IssueDetailsDO> impl = null;
        IssueDetailsDO workingHoursDO = new IssueDetailsDO();
        workingHoursDO.setIssueDepartment(devpLeadDept);
        List<String> list = getSixMonth(date2);
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        for (int i = 0; i < list.size(); i++) {
            workingHoursDO.setRegistrationDate(list.get(i));
            impl = issueDetailsExtDao.findList(workingHoursDO);
            SumBos.add(list.get(i));
            if (impl != null && impl.size() >= 0) {
                workingHoursBOS.add(impl.size() + "");
            } else {
                workingHoursBOS.add("0");
            }
        }
        System.err.println(impl);
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO.setStringList(workingHoursBOS);
        demandHoursRspBO.setListSum(SumBos);
        return demandHoursRspBO;
    }

    /**
     * 团队生产问题
     *
     * @param devpLeadDept
     * @param date1
     * @param date2
     * @return
     */
    @Override
    public DemandHoursRspBO getDeptProductionDefects(String devpLeadDept, String date1, String date2) {
        List<ProductionDefectsDO> impl = null;
        ProductionDefectsDO workingHoursDO = new ProductionDefectsDO();
        workingHoursDO.setProblemattributiondept(devpLeadDept);
        List<String> list = getSixMonth(date2);
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        for (int i = 0; i < list.size(); i++) {
            workingHoursDO.setProcessstartdate(list.get(i));
            impl = productionDefectsExtDao.findMonthList(workingHoursDO);
            SumBos.add(list.get(i));
            if (impl != null && impl.size() >= 0) {
                workingHoursBOS.add(impl.size() + "");
            } else {
                workingHoursBOS.add("0");
            }
        }
        System.err.println(impl);
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO.setStringList(workingHoursBOS);
        demandHoursRspBO.setListSum(SumBos);
        return demandHoursRspBO;
    }

    /**
     * 团队投产验证不及时
     *
     * @param devpLeadDept
     * @param date1
     * @param date2
     * @return
     */
    @Override
    public DemandHoursRspBO getDeptProCheckTimeOutStatistics(String devpLeadDept, String date1, String date2) {
        List<ProCheckTimeOutStatisticsDO> impl = null;
        ProCheckTimeOutStatisticsDO workingHoursDO = new ProCheckTimeOutStatisticsDO();
        workingHoursDO.setDepartment(devpLeadDept);
        List<String> list = getSixMonth(date2);
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        for (int i = 0; i < list.size(); i++) {
            workingHoursDO.setRegistrationdate(list.get(i));
            impl = proCheckTimeOutStatisticsExtDao.findMonth(workingHoursDO);
            SumBos.add(list.get(i));
            int sum = 0;
            if (impl != null && impl.size() >= 0) {
                for (int j = 0; j < impl.size(); j++) {
                    sum = sum + impl.get(j).getCount();
                }
                workingHoursBOS.add(sum + "");
            } else {
                workingHoursBOS.add("0");
            }
        }
        System.err.println(impl);
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO.setStringList(workingHoursBOS);
        demandHoursRspBO.setListSum(SumBos);
        return demandHoursRspBO;
    }

    /**
     * 团队缺陷未解决
     *
     * @param devpLeadDept
     * @param date1
     * @param date2
     * @return
     */
    @Override
    public DemandHoursRspBO getDeptProUnhandledIssues1(String devpLeadDept, String date1, String date2) {
        List<ProUnhandledIssuesDO> impl = null;
        ProUnhandledIssuesDO workingHoursDO = new ProUnhandledIssuesDO();
        workingHoursDO.setDepartment(devpLeadDept);
        List<String> list = getSixMonth(date2);
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        for (int i = 0; i < list.size(); i++) {
            workingHoursDO.setProductionDate(list.get(i));
            impl = proUnhandledIssuesExtDao.findMonth(workingHoursDO);
            SumBos.add(list.get(i));
            int sum = 0;
            if (impl != null && impl.size() >= 0) {
                for (int j = 0; j < impl.size(); j++) {
                    if(impl.get(j).getDefectsNumber()>0){
                        sum = sum + 1;
                    }
                }
                workingHoursBOS.add(sum + "");
            } else {
                workingHoursBOS.add("0");
            }
        }
        System.err.println(impl);
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO.setStringList(workingHoursBOS);
        demandHoursRspBO.setListSum(SumBos);
        return demandHoursRspBO;
    }

    /**
     * 团队问题未解决
     *
     * @param devpLeadDept
     * @param date1
     * @param date2
     * @return
     */
    @Override
    public DemandHoursRspBO getDeptProUnhandledIssues2(String devpLeadDept, String date1, String date2) {
        List<ProUnhandledIssuesDO> impl = null;
        ProUnhandledIssuesDO workingHoursDO = new ProUnhandledIssuesDO();
        workingHoursDO.setDepartment(devpLeadDept);
        List<String> list = getSixMonth(date2);
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        for (int i = 0; i < list.size(); i++) {
            workingHoursDO.setProductionDate(list.get(i));
            impl = proUnhandledIssuesExtDao.findMonth(workingHoursDO);
            SumBos.add(list.get(i));
            int sum = 0;
            if (impl != null && impl.size() >= 0) {
                for (int j = 0; j < impl.size(); j++) {
                    if(impl.get(j).getProblemNumber()>0){
                        sum = sum + 1;
                    }
                }
                workingHoursBOS.add(sum + "");
            } else {
                workingHoursBOS.add("0");
            }
        }
        System.err.println(impl);
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO.setStringList(workingHoursBOS);
        demandHoursRspBO.setListSum(SumBos);
        return demandHoursRspBO;
    }

    public static java.sql.Date strToDate(String strDate) {
        String str = strDate;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        java.util.Date d = null;
        try {
            d = format.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        java.sql.Date date = new java.sql.Date(d.getTime());
        return date;
    }

    /**
     * 投产回退
     *
     * @param devpLeadDept
     * @param date1
     * @param date2
     * @return
     */
    @Override
    public DemandHoursRspBO getDeptProductionBack(String devpLeadDept, String date1, String date2) {
        List<ProductionDO> impl = null;
        WorkingHoursDO workingHoursDO = new WorkingHoursDO();
        workingHoursDO.setDevpLeadDept(devpLeadDept);

        List<String> list = getSixMonth(date2);
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        for (int i = 0; i < list.size(); i++) {
            workingHoursDO.setSelectTime(list.get(i));
            impl = iOperationProductionDao.findMonth(workingHoursDO);
            SumBos.add(list.get(i));
            int sum = 0;
            if (impl != null && impl.size() >= 0) {
                for (int j = 0; j < impl.size(); j++) {
                    if ("投产回退".equals(impl.get(j).getProStatus())) {
                        sum = sum + 1;
                    }
                }
                workingHoursBOS.add(sum + "");
            } else {
                workingHoursBOS.add("0");
            }
        }
        System.err.println(impl);
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO.setStringList(workingHoursBOS);
        demandHoursRspBO.setListSum(SumBos);
        return demandHoursRspBO;
    }

    /**
     * 团队周期汇总
     *
     * @param devpLeadDept
     * @param date1
     * @param date2
     * @return
     */
    @Override
    public DemandHoursRspBO getDeptSum(String devpLeadDept, String date1, String date2) {
        List<ProductionDO> impl = null;
        WorkingHoursDO workingHoursDO = new WorkingHoursDO();
        workingHoursDO.setDevpLeadDept(devpLeadDept);
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        // 缺陷问题
        DefectDetailsDO defectDetailsDO = new DefectDetailsDO();
        defectDetailsDO.setProblemHandlerDepartment(devpLeadDept);
        defectDetailsDO.setRegistrationDate(date1);
        List<DefectDetailsDO> impl1 = null;
        impl1 = defectDetailsExtDao.findWeekList(defectDetailsDO);
        SumBos.add("缺陷问题");
        if (impl1 != null && impl1.size() != 0) {
            workingHoursBOS.add(impl1.size() + "");
        } else {
            workingHoursBOS.add("0");
        }
        // 评审问题
        IssueDetailsDO issueDetailsDO = new IssueDetailsDO();
        issueDetailsDO.setIssueDepartment(devpLeadDept);
        issueDetailsDO.setRegistrationDate(date1);
        List<IssueDetailsDO> impl2 = null;
        impl2 = issueDetailsExtDao.findWeekList(issueDetailsDO);
        SumBos.add("评审问题");
        if (impl2 != null && impl2.size() != 0) {
            workingHoursBOS.add(impl2.size() + "");
        } else {
            workingHoursBOS.add("0");
        }
        // 生产问题
        List<ProductionDefectsDO> impl3 = null;
        ProductionDefectsDO productionDefectsDO = new ProductionDefectsDO();
        productionDefectsDO.setProblemattributiondept(devpLeadDept);
        productionDefectsDO.setProcessstartdate(date1);
        impl3 = productionDefectsExtDao.findWeekList(productionDefectsDO);
        SumBos.add("生产问题");
        if (impl3 != null && impl3.size() != 0) {
            workingHoursBOS.add(impl3.size() + "");
        } else {
            workingHoursBOS.add("0");
        }
        //投产未验证
        List<ProCheckTimeOutStatisticsDO> impl4 = null;
        ProCheckTimeOutStatisticsDO proCheckTimeOutStatisticsDO = new ProCheckTimeOutStatisticsDO();
        proCheckTimeOutStatisticsDO.setDepartment(devpLeadDept);
        proCheckTimeOutStatisticsDO.setRegistrationdate(date1);
        impl4 = proCheckTimeOutStatisticsExtDao.findWeek(proCheckTimeOutStatisticsDO);
        SumBos.add("投产验证不及时");
        int sum = 0;
        if (impl4 != null && impl4.size() != 0) {
            for (int j = 0; j < impl4.size(); j++) {
                sum = sum + impl4.get(j).getCount();
            }
            workingHoursBOS.add(sum + "");
        } else {
            workingHoursBOS.add("0");
        }

        //缺陷未解决
        List<ProUnhandledIssuesDO> impl5 = null;
        ProUnhandledIssuesDO proUnhandledIssuesDO = new ProUnhandledIssuesDO();
        proUnhandledIssuesDO.setDepartment(devpLeadDept);
        proUnhandledIssuesDO.setProductionDate(date1);
        impl5 = proUnhandledIssuesExtDao.findMonth(proUnhandledIssuesDO);
        SumBos.add("投产缺陷未解决");
        int sumx = 0;
        if (impl5 != null && impl5.size() != 0) {
            for (int j = 0; j < impl5.size(); j++) {
                if(impl5.get(j).getDefectsNumber()>0){
                    sumx = sumx + 1;
                }
            }
            workingHoursBOS.add(sumx + "");
        } else {
            workingHoursBOS.add("0");
        }
        //问题未解决
        SumBos.add("投产问题未解决");
        int sumt = 0;
        if (impl5 != null && impl5.size() != 0) {
            for (int j = 0; j < impl5.size(); j++) {
                sumt = sumt + impl5.get(j).getProblemNumber();
            }
            workingHoursBOS.add(sumt + "");
        } else {
            workingHoursBOS.add("0");
        }
        // 投产回退
        workingHoursDO.setSelectTime(date1);
        impl = iOperationProductionDao.findWeek(workingHoursDO);
        SumBos.add("投产回退");
        int suma = 0;
        if (impl != null && impl.size() >= 0) {
            for (int j = 0; j < impl.size(); j++) {
                if ("投产回退".equals(impl.get(j).getProStatus())) {
                    suma = suma + 1;
                }
            }
            workingHoursBOS.add(suma + "");
        } else {
            workingHoursBOS.add("0");
        }
        System.err.println(impl);
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO.setStringList(workingHoursBOS);
        demandHoursRspBO.setListSum(SumBos);
        return demandHoursRspBO;
    }

    @Override
    public DemandHoursRspBO getBuildFailedCount(String devpLeadDept, String date1, String date2) {
        List<BuildFailedCountDO> impl = null;
        BuildFailedCountDO buildFailedCountDO = new BuildFailedCountDO();
        buildFailedCountDO.setDepartment(devpLeadDept);
        impl = iBuildFailedCountDao.find(buildFailedCountDO);
        int a = 0;
        int b = 0;
        int c = 0;
        if (impl != null && impl.size() >= 0) {
            for (int i = 0; i < impl.size(); i++) {
                if (impl.get(i).getCount() == 1) {
                    a = a + 1;
                }
                if (impl.get(i).getCount() == 2) {
                    b = b + 1;
                }
                if (impl.get(i).getCount() >= 3) {
                    c = c + 1;
                }
            }
        }
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        SumBos.add("一次");
        workingHoursBOS.add(a + "");
        SumBos.add("二次");
        workingHoursBOS.add(b + "");
        SumBos.add("三次及以上");
        workingHoursBOS.add(c + "");
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO.setStringList(workingHoursBOS);
        demandHoursRspBO.setListSum(SumBos);
        return demandHoursRspBO;
    }

    @Override
    public DemandHoursRspBO getSmokeTestFailedCount(String devpLeadDept, String date1, String date2) {
        List<SmokeTestFailedCountDO> impl = null;
        SmokeTestFailedCountDO smokeTestFailedCountDO = new SmokeTestFailedCountDO();
        smokeTestFailedCountDO.setDepartment(devpLeadDept);
        if (StringUtils.isBlank(date1) && StringUtils.isBlank(date2)) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如周、月!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        // 查询周
        if (StringUtils.isNotBlank(date1) && StringUtils.isBlank(date2)) {
            smokeTestFailedCountDO.setTestDate(date1);
            impl = iSmokeTestFailedCountDao.findWeek(smokeTestFailedCountDO);
        }
        // 查询月
        if (StringUtils.isNotBlank(date2) && StringUtils.isBlank(date1)) {
            smokeTestFailedCountDO.setTestDate(date2);
            impl = iSmokeTestFailedCountDao.findMonth(smokeTestFailedCountDO);
        }
        int a = 0;
        int b = 0;
        int c = 0;
        if (impl != null && impl.size() >= 0) {
            for (int i = 0; i < impl.size(); i++) {
                if (impl.get(i).getCount() == 1) {
                    a = a + 1;
                }
                if (impl.get(i).getCount() == 2) {
                    b = b + 1;
                }
                if (impl.get(i).getCount() >= 3) {
                    c = c + 1;
                }
            }
        }
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        SumBos.add("一次");
        workingHoursBOS.add(a + "");
        SumBos.add("二次");
        workingHoursBOS.add(b + "");
        SumBos.add("三次及以上");
        workingHoursBOS.add(c + "");
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO.setStringList(workingHoursBOS);
        demandHoursRspBO.setListSum(SumBos);
        return demandHoursRspBO;
    }

    /**
     * 中心人数
     *
     * @return
     */
    @Override
    public DemandHoursRspBO getReportForm12() {
        List<WorkingHoursDO> impl = iWorkingHoursDao.findSumPer();
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        if (impl != null && impl.size() >= 0) {
            demandHoursRspBO.setSum(impl.get(0).getSumTime());
        }
        System.err.println(demandHoursRspBO);
        return demandHoursRspBO;
    }

    /**
     * 需求进度异常柱状图
     * @param list
     * @return
     */
    @Override
    public DemandHoursRspBO planSearch(List<DemandBO> list){
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        List<WorkingHoursDO> workingHoursDOList = null;
        //二级部门集合
        List<OrganizationStructureDO> impl = iOrganizationStructureDao.find(new OrganizationStructureDO());
        DecimalFormat df = new DecimalFormat("0.##");
        if(list == null || list.size()<=0){
            return demandHoursRspBO;
        }
        // 二级团队集合
        List<String> deptName = new LinkedList<>();
        for (int i = 0; i < impl.size(); i++) {
            int sum = 0;
            // 各个部门的数量
            for(int j=0;j< list.size();j++){
                if(impl.get(i).getSecondlevelorganization().equals(list.get(j).getDevpLeadDept())){
                    sum =sum+1;
                }
            }
            String str = "{ product: '" + impl.get(i).getSecondlevelorganization() + "', 进度异常需求数: '" + sum + "'}";
            workingHoursBOS.add(str);

        }
        SumBos.add("product");
        SumBos.add("进度异常需求数");

        demandHoursRspBO.setStringList(workingHoursBOS);
        demandHoursRspBO.setListSum(SumBos);
        return demandHoursRspBO;
    }
    /**
     * 各部门中心功能点和工时
     *
     * @param date1
     * @param date2
     * @return
     */
    @Override
    public DemandHoursRspBO getCentreWorkHours(String date1, String date2) {
        System.err.println("中心功能点和工时");
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        List<WorkingHoursDO> workingHoursDOList = null;
        List<OrganizationStructureDO> impl = iOrganizationStructureDao.find(new OrganizationStructureDO());
        DecimalFormat df = new DecimalFormat("0.##");
        // 二级团队集合
        List<String> deptName = new LinkedList<>();
        for (int i = 0; i < impl.size(); i++) {
            deptName.add(impl.get(i).getSecondlevelorganization());
            // 查询周
            if (StringUtils.isNotBlank(date1) && StringUtils.isBlank(date2)) {
                //根据部门获取部门人数
                WorkingHoursDO workingHoursDO = new WorkingHoursDO();
                workingHoursDO.setDevpLeadDept(impl.get(i).getSecondlevelorganization());
                workingHoursDOList = iWorkingHoursDao.findDeptView(workingHoursDO);
                String sum = workingHoursDOList.get(0).getSumDept();
                int sumx = 5 * 8 * Integer.parseInt(sum);
                DemandHoursRspBO demandHoursRspBO = getDeptWorkHoursAndPoint2(impl.get(i).getSecondlevelorganization(), date1, date2);
                List<String> listSum = demandHoursRspBO.getListSum();
                double  d1 = (double) (Math.round((long) Integer.parseInt(listSum.get(0)) * 100 / sumx) / 100.0);
                String str = "{ products: '" + impl.get(i).getSecondlevelorganization() + "', 工时标准工时: '" + df.format(d1)  + "'}";
                // { product: '产品测试团队', 工时: '70', 功能点: '150'},
                workingHoursBOS.add(str);
            }
            //查询月
            if (StringUtils.isNotBlank(date2) && StringUtils.isBlank(date1)) {
                DemandHoursRspBO demandHoursRspBO = getDeptWorkHoursAndPoint(impl.get(i).getSecondlevelorganization(), date1, date2);
                List<String> listSum = demandHoursRspBO.getListSum();
                //根据部门获取部门人数
                WorkingHoursDO workingHoursDO = new WorkingHoursDO();
                workingHoursDO.setDevpLeadDept(impl.get(i).getSecondlevelorganization());
                workingHoursDOList = iWorkingHoursDao.findDeptView(workingHoursDO);
                String sum = workingHoursDOList.get(0).getSumDept();
                // 获取当前月份的天数
                MonthWorkdayDO monthWorkdayDO = monthWorkdayDao.getMonth(date2);
                int day = 21;
                if(JudgeUtils.isNotNull(monthWorkdayDO)){
                    day = monthWorkdayDao.getMonth(date2).getWorkPastDay();
                }
                int sumx = day * Integer.parseInt(sum);
                double d1 = 0.00;
                double d2 = 0.00;
                if(Integer.parseInt(listSum.get(0)) != 0){
                    d1 = (double) (Math.round((long) Integer.parseInt(listSum.get(0)) * 100 / sumx) / 100.0);
                    d2 = (double) (Math.round((long) Integer.parseInt(listSum.get(1)) * 100 / Integer.parseInt(listSum.get(0))) / 100.0);
                }
                String str = "{ product: '" + impl.get(i).getSecondlevelorganization() + "', 工时标准工时: '" + df.format(d1) + "'}";
                // { product: '产品测试团队', 工时: '70', 功能点: '150'},
                workingHoursBOS.add(str);
            }

        }
        SumBos.add("product");
        if (StringUtils.isNotBlank(date1) && StringUtils.isBlank(date2)) {
            SumBos.add("工时标准工时");
        }
        if (StringUtils.isNotBlank(date2) && StringUtils.isBlank(date1)) {
            SumBos.add("工时标准工时");
        }
        //{sum='null', listSum=[0, 0], stringList=[工时, 功能点]}
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO.setStringList(workingHoursBOS);
        demandHoursRspBO.setListSum(SumBos);
        return demandHoursRspBO;
    }
    @Override
    public DemandHoursRspBO getCentreWorkHours2(String date1, String date2) {
        System.err.println("中心功能点和工时");
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        List<WorkingHoursDO> workingHoursDOList = null;
        List<OrganizationStructureDO> impl = iOrganizationStructureDao.find(new OrganizationStructureDO());
        DecimalFormat df = new DecimalFormat("0.##");
        // 二级团队集合
        List<String> deptName = new LinkedList<>();
        //中台汇总
        double sumZhoTai = 0;
        int sumQiTa = 0;
        int x =0;
        for (int i = 0; i < impl.size(); i++) {
            deptName.add(impl.get(i).getSecondlevelorganization());
            //查询月
            if (StringUtils.isNotBlank(date2) && StringUtils.isBlank(date1)) {
                DemandHoursRspBO demandHoursRspBO = getDeptWorkHoursAndPoint(impl.get(i).getSecondlevelorganization(), date1, date2);
                List<String> listSum = demandHoursRspBO.getListSum();
                double d2 = 0.00;
                //判断部门 基准工作量
                if("话费充值研发组".equals(impl.get(i).getSecondlevelorganization())){
                    sumQiTa = 371;
                    d2 = (double) (Math.round(Double.valueOf(listSum.get(1)) * 100 / sumQiTa) / 100.0);
                    String rate = listSum.get(1)+"/"+sumQiTa;
                    String str = "{ product: '" + impl.get(i).getSecondlevelorganization() + "', 功能点基准工作量: '" + df.format(d2) + "', rate: '" + rate + "'}";
                    // { product: '产品测试团队', 工时: '70', 功能点: '150'},
                    workingHoursBOS.add(str);
                }
                if("聚合支付研发组".equals(impl.get(i).getSecondlevelorganization())){
                    sumQiTa = 234;
                    d2 = (double) (Math.round(Double.valueOf(listSum.get(1)) * 100 / sumQiTa) / 100.0);
                    String rate = listSum.get(1)+"/"+sumQiTa;
                    String str = "{ product: '" + impl.get(i).getSecondlevelorganization() + "', 功能点基准工作量: '" + df.format(d2) + "', rate: '" + rate + "'}";
                    // { product: '产品测试团队', 工时: '70', 功能点: '150'},
                    workingHoursBOS.add(str);
                }
                if("营销活动研发组".equals(impl.get(i).getSecondlevelorganization())){
                    sumQiTa = 382;
                    d2 = (double) (Math.round(Double.valueOf(listSum.get(1)) * 100 / sumQiTa) / 100.0);
                    String rate = listSum.get(1)+"/"+sumQiTa;
                    String str = "{ product: '" + impl.get(i).getSecondlevelorganization() + "', 功能点基准工作量: '" + df.format(d2) + "', rate: '" + rate + "'}";
                    // { product: '产品测试团队', 工时: '70', 功能点: '150'},
                    workingHoursBOS.add(str);
                }
                if("渠道产品研发组".equals(impl.get(i).getSecondlevelorganization())){
                    sumQiTa = 527;
                    d2 = (double) (Math.round(Double.valueOf(listSum.get(1)) * 100 / sumQiTa) / 100.0);
                    String rate = listSum.get(1)+"/"+sumQiTa;
                    String str = "{ product: '" + impl.get(i).getSecondlevelorganization() + "', 功能点基准工作量: '" + df.format(d2) + "', rate: '" + rate + "'}";
                    // { product: '产品测试团队', 工时: '70', 功能点: '150'},
                    workingHoursBOS.add(str);
                }
                if("信用购机研发组".equals(impl.get(i).getSecondlevelorganization())){
                    sumQiTa = 868;
                    d2 = (double) (Math.round(Double.valueOf(listSum.get(1)) * 100 / sumQiTa) / 100.0);
                    String rate = listSum.get(1)+"/"+sumQiTa;
                    String str = "{ product: '" + impl.get(i).getSecondlevelorganization() + "', 功能点基准工作量: '" + df.format(d2) + "', rate: '" + rate + "'}";
                    // { product: '产品测试团队', 工时: '70', 功能点: '150'},
                    workingHoursBOS.add(str);
                }
                if("号码借研发组".equals(impl.get(i).getSecondlevelorganization())){
                    sumQiTa = 203;
                    d2 = (double) (Math.round(Double.valueOf(listSum.get(1)) * 100 / sumQiTa) / 100.0);
                    String rate = listSum.get(1)+"/"+sumQiTa;
                    String str = "{ product: '" + impl.get(i).getSecondlevelorganization() + "', 功能点基准工作量: '" + df.format(d2) + "', rate: '" + rate + "'}";
                    // { product: '产品测试团队', 工时: '70', 功能点: '150'},
                    workingHoursBOS.add(str);
                }
                if("商户业务研发团队".equals(impl.get(i).getSecondlevelorganization())){
                    sumQiTa = 483;
                    d2 = (double) (Math.round(Double.valueOf(listSum.get(1)) * 100 / sumQiTa) / 100.0);
                    String rate = listSum.get(1)+"/"+sumQiTa;
                    String str = "{ product: '" + impl.get(i).getSecondlevelorganization() + "', 功能点基准工作量: '" + df.format(d2) + "', rate: '" + rate + "'}";
                    // { product: '产品测试团队', 工时: '70', 功能点: '150'},
                    workingHoursBOS.add(str);
                }
                if("智慧食堂研发团队".equals(impl.get(i).getSecondlevelorganization())){
                    sumQiTa = 187;
                    d2 = (double) (Math.round(Double.valueOf(listSum.get(1)) * 100 / sumQiTa) / 100.0);
                    String rate = listSum.get(1)+"/"+sumQiTa;
                    String str = "{ product: '" + impl.get(i).getSecondlevelorganization() + "', 功能点基准工作量: '" + df.format(d2) + "', rate: '" + rate + "'}";
                    // { product: '产品测试团队', 工时: '70', 功能点: '150'},
                    workingHoursBOS.add(str);
                }
                if("银行&公共中心研发组".equals(impl.get(i).getSecondlevelorganization())||"用户&清算&账务研发组".equals(impl.get(i).getSecondlevelorganization())
                ||"支付研发组".equals(impl.get(i).getSecondlevelorganization()) ||"营销研发组".equals(impl.get(i).getSecondlevelorganization())){
                    x = x+1;
                    sumQiTa = 1785;
                    sumZhoTai = sumZhoTai +  Double.valueOf(listSum.get(1));
                    if(x==4){
                        d2 = (double) (Math.round( sumZhoTai * 100 / sumQiTa) / 100.0);
                        String rate = sumZhoTai+"/"+sumQiTa;
                        String str = "{ product: '" + "业务中台" + "', 功能点基准工作量: '" + df.format(d2) + "', rate: '" + rate + "'}";
                        // { product: '产品测试团队', 工时: '70', 功能点: '150'},
                        workingHoursBOS.add(str);
                    }

                }
                if("平台架构研发团队".equals(impl.get(i).getSecondlevelorganization())){
                    sumQiTa = 250;
                    d2 = (double) (Math.round(Double.valueOf(listSum.get(1)) * 100 / sumQiTa) / 100.0);
                    String rate = listSum.get(1)+"/"+sumQiTa;
                    String str = "{ product: '" + impl.get(i).getSecondlevelorganization() + "', 功能点基准工作量: '" + df.format(d2) + "', rate: '" + rate + "'}";
                    // { product: '产品测试团队', 工时: '70', 功能点: '150'},
                    workingHoursBOS.add(str);
                }
                if("前端技术研发团队".equals(impl.get(i).getSecondlevelorganization())){
                    sumQiTa = 675;
                    d2 = (double) (Math.round(Double.valueOf(listSum.get(1)) * 100 / sumQiTa) / 100.0);
                    String rate = listSum.get(1)+"/"+sumQiTa;
                    String str = "{ product: '" + impl.get(i).getSecondlevelorganization() + "', 功能点基准工作量: '" + df.format(d2) + "', rate: '" + rate + "'}";
                    // { product: '产品测试团队', 工时: '70', 功能点: '150'},
                    workingHoursBOS.add(str);
                }
            }

        }
        SumBos.add("product");
        if (StringUtils.isNotBlank(date2) && StringUtils.isBlank(date1)) {
            SumBos.add("功能点基准工作量");
        }
        //{sum='null', listSum=[0, 0], stringList=[工时, 功能点]}
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO.setStringList(workingHoursBOS);
        demandHoursRspBO.setListSum(SumBos);
        return demandHoursRspBO;
    }

    /**
     * 中心总共功能点和工时
     *
     * @param date1
     * @param date2
     * @return
     */
    @Override
    public DemandHoursRspBO getCentreWorkHoursPoint(String date1, String date2) {
        System.err.println("中心总共功能点和工时");
        WorkingHoursDO workingHoursDO = new WorkingHoursDO();
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        List<WorkingHoursDO> impl = null;
        System.err.println(date1 + "=====" + date2);
        DemandBO demandBO = new DemandBO();
        if (StringUtils.isBlank(date1) && StringUtils.isBlank(date2)) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如周、月!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        // 查询周
        if (StringUtils.isNotBlank(date1) && StringUtils.isBlank(date2)) {
            workingHoursDO.setSelectTime(date1);
            impl = iWorkingHoursDao.findWeekSumB(workingHoursDO);
            //demandBO.setReqImplMon(date1.substring(0,6));
            System.err.println(impl);
            if (impl != null && impl.size() >= 0) {
                workingHoursBOS.add("工时");
                if (!"0".equals(impl.get(0).getSumTime())) {
                    DecimalFormat df = new DecimalFormat("0");
                    SumBos.add(df.format(getWorkHours(Integer.parseInt(impl.get(0).getSumTime()))));
                } else {
                    SumBos.add("0");
                }
            } else {
                workingHoursBOS.add("工时");
                SumBos.add("0");
            }
            workingHoursBOS.add("功能点");
            SumBos.add("0");
        }
        // 查询月
        if (StringUtils.isNotBlank(date2) && StringUtils.isBlank(date1)) {
            // 月标准工时 人天
            workingHoursBOS.add("标准工时");
            List<WorkingHoursDO> list = iWorkingHoursDao.findSumPer();
            MonthWorkdayDO monthWorkdayDO = monthWorkdayDao.getMonth(date2);
            int day = 21;
            if(JudgeUtils.isNotNull(monthWorkdayDO)){
                day = monthWorkdayDao.getMonth(date2).getWorkSumDay();
            }
            int workTime = day * Integer.parseInt(list.get(0).getSumTime());
            SumBos.add(workTime+"");
            workingHoursDO.setSelectTime(date2);
            impl = iWorkingHoursDao.findMonthSumB(workingHoursDO);
            demandBO.setReqImplMon(date2);
            System.err.println("yuegos" + impl);
            if (impl != null && impl.size() >= 0) {
                workingHoursBOS.add("工时");
                if (!"0".equals(impl.get(0).getSumTime())) {
                    DecimalFormat df = new DecimalFormat("0");
                    SumBos.add(df.format(getWorkHours(Integer.parseInt(impl.get(0).getSumTime()))));
                } else {
                    SumBos.add("0");
                }

            } else {
                workingHoursBOS.add("工时");
                SumBos.add("0");
            }
            workingHoursBOS.add("");
            SumBos.add("");
            workingHoursBOS.add("基准工作量");
            SumBos.add("6382.75");
            // 获取当月的功能点
            System.err.println(demandBO);
            List<Double> dept = reqWorkLoadService.getExportCountForDevp2(demandBO);
            if (dept.get(0) != null) {
                workingHoursBOS.add("功能点");
                DecimalFormat df = new DecimalFormat("0");
                SumBos.add(df.format(dept.get(0)));
            } else {
                workingHoursBOS.add("功能点");
                SumBos.add("0");
            }
        }

        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO.setStringList(workingHoursBOS);
        demandHoursRspBO.setListSum(SumBos);
        System.err.println("中心功能点，工时" + demandHoursRspBO);
        return demandHoursRspBO;
    }

    /**
     * 中心投产情况
     *
     * @param date1
     * @param date2
     * @return
     */
    @Override
    public DemandHoursRspBO getCentreProduction(String date1, String date2) {
        System.err.println("中心投产情况");
        WorkingHoursDO workingHoursDO = new WorkingHoursDO();
        List<ProductionDO> impl = null;
        System.err.println(date1 + "=====" + date2);
        if (StringUtils.isBlank(date1) && StringUtils.isBlank(date2)) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如周、月!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        // 查询周
        if (StringUtils.isNotBlank(date1) && StringUtils.isBlank(date2)) {
            workingHoursDO.setSelectTime(date1);
            impl = iWorkingHoursDao.findListDeptWeek(workingHoursDO);
        }
        // 查询月
        if (StringUtils.isNotBlank(date2) && StringUtils.isBlank(date1)) {
            workingHoursDO.setSelectTime(date2);
            impl = iWorkingHoursDao.findListDeptMonth(workingHoursDO);
        }
        System.err.println(impl);
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        String sum = "";
        int a = 0;
        int b = 0;
        int c = 0;
        if (impl != null && impl.size() >= 0) {
            System.err.println(impl.size());
            sum = impl.size() + "";
            for (int i = 0; i < impl.size(); i++) {
                if ("正常投产".equals(impl.get(i).getProType()) && "是".equals(impl.get(i).getIsOperationProduction())) {
                    a = a + 1;
                }
                if ("正常投产".equals(impl.get(i).getProType()) && "否".equals(impl.get(i).getIsOperationProduction())) {
                    b = b + 1;
                }
                if ("救火更新".equals(impl.get(i).getProType())) {
                    c = c + 1;
                }
            }
            if (a > 0) {
                String a1 = "{'value': '" + a + "', 'name': '正常投产'}";
                workingHoursBOS.add(a1);
                SumBos.add("正常投产");
            }
            if (b > 0) {
                String b1 = "{'value': '" + b + "', 'name': '非投产日正常投产'}";
                workingHoursBOS.add(b1);
                SumBos.add("非投产日正常投产");
            }
            if (c > 0) {
                String c1 = "{'value': '" + c + "', 'name': '救火更新'}";
                workingHoursBOS.add(c1);
                SumBos.add("救火更新");
            }
        } else {
            sum = "0";
        }
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO.setStringList(workingHoursBOS);
        demandHoursRspBO.setListSum(SumBos);
        demandHoursRspBO.setSum(sum);
        return demandHoursRspBO;
    }

    @Override
    public DemandHoursRspBO getCentreProductionDept(String date1, String date2) {
        System.err.println("中心各部门投产情况");
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        double sum = 0;
        List<OrganizationStructureDO> impl2 = iOrganizationStructureDao.find(new OrganizationStructureDO());
        // 二级团队集合
        List<String> deptName = new LinkedList<>();
        for (int j = 0; j < impl2.size(); j++) {
            deptName.add(impl2.get(j).getSecondlevelorganization());

            WorkingHoursDO workingHoursDO = new WorkingHoursDO();
            workingHoursDO.setDevpLeadDept(impl2.get(j).getSecondlevelorganization());
            List<ProductionDO> impl = null;
            System.err.println(date1 + "=====" + date2);
            if (StringUtils.isBlank(date1) && StringUtils.isBlank(date2)) {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如周、月!");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            // 查询周
            if (StringUtils.isNotBlank(date1) && StringUtils.isBlank(date2)) {
                workingHoursDO.setSelectTime(date1);
                impl = iWorkingHoursDao.findListDeptWeek(workingHoursDO);
                System.err.println(impl);
            }
            // 查询月
            if (StringUtils.isNotBlank(date2) && StringUtils.isBlank(date1)) {
                workingHoursDO.setSelectTime(date2);
                impl = iWorkingHoursDao.findListDeptMonth(workingHoursDO);
                System.err.println(impl);
            }
            System.err.println(impl);
            int a = 0;
            int b = 0;
            int c = 0;
            if (impl != null && impl.size() >= 0) {
                System.err.println(impl.size());
                sum = sum + impl.size();
                for (int i = 0; i < impl.size(); i++) {
                    if ("正常投产".equals(impl.get(i).getProType()) && "是".equals(impl.get(i).getIsOperationProduction())) {
                        a = a + 1;
                    }
                    if ("正常投产".equals(impl.get(i).getProType()) && "否".equals(impl.get(i).getIsOperationProduction())) {
                        b = b + 1;
                    }
                    if ("救火更新".equals(impl.get(i).getProType())) {
                        c = c + 1;
                    }
                }
                String str = "{ product: '" + impl2.get(j).getSecondlevelorganization() + "', 正常投产: '" + a + "', 非投产日正常投产: '" + b + "', 救火更新: '" + c + "'}";
                workingHoursBOS.add(str);
            }

        }
        //{sum='null', listSum=[0, 0], stringList=[工时, 功能点]}
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO.setStringList(workingHoursBOS);
        demandHoursRspBO.setListSum(SumBos);
        demandHoursRspBO.setSum(sum + "");
        System.err.println(demandHoursRspBO);
        return demandHoursRspBO;
    }


    @Override
    public DemandHoursRspBO getTestStaffWork(String date1, String date2) {
        System.err.println(date1);
        System.err.println(date2);
        System.err.println("测试部人员工作");
        List<String> workingHoursBOS = new LinkedList<>();
        if (StringUtils.isBlank(date1) && StringUtils.isBlank(date2)) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如周、月!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        //获得测试部人员信息
        UserDO userDO = new UserDO();
        userDO.setDepartment("产品测试团队");
        List<UserDO> userDOS = iUserDao.find(userDO);
        String startTime = "";
        String endTime = "";
        //月计算
        if (JudgeUtils.isBlank(date1) && JudgeUtils.isNotBlank(date2)) {

            //获取下个月时间
            SimpleDateFormat simpleDateFormatMonth = new SimpleDateFormat("yyyy-MM");
            try {
                Date month = simpleDateFormatMonth.parse(date2);
                Calendar c = Calendar.getInstance();
                c.setTime(month);
                c.add(Calendar.MONTH, 1);
                startTime = date2;
                endTime = simpleDateFormatMonth.format(c.getTime());
            } catch (Exception e) {
                //todo  时间格式不对
            }
        }
        //周计算
        else if (JudgeUtils.isNotBlank(date1) && JudgeUtils.isBlank(date2)) {
            //获取下个月时间
            SimpleDateFormat simpleDateFormatMonth = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date day = simpleDateFormatMonth.parse(date1);
                Calendar c = Calendar.getInstance();
                c.setTime(day);
                c.add(Calendar.DATE, 7);
                startTime = date1;
                endTime = simpleDateFormatMonth.format(c.getTime());
            } catch (Exception e) {
                //todo  时间格式不对
            }
        } else {
            //todo  时间格式不对
        }

        WorkingHoursDO workingHoursDO = new WorkingHoursDO();
        workingHoursDO.setStartTime(startTime);
        workingHoursDO.setEndTime(endTime);
        int a1 = 0;
        int b1 = 0;
        int c1 = 0;
        int d1 = 0;
        for(UserDO m:userDOS) {
            DefectDetailsDO defectDetailsDO = new DefectDetailsDO();
            defectDetailsDO.setDefectRegistrant(m.getFullname());
            workingHoursDO.setDisplayname(m.getFullname());
            List<WorkingHoursDO> workingHoursDOS = new LinkedList<>();
            List<DefectDetailsDO> list = new LinkedList<>();
            // 查询周
            if (StringUtils.isNotBlank(date1) && StringUtils.isBlank(date2)) {
                workingHoursDO.setSelectTime(date1);
                workingHoursDOS = iWorkingHoursDao.queryByTimeCycle(workingHoursDO);
                defectDetailsDO.setRegistrationDate(date1);
                list = defectDetailsExtDao.findWeekList(defectDetailsDO);
            }
            // 查询月
            if (StringUtils.isNotBlank(date2) && StringUtils.isBlank(date1)) {
                workingHoursDO.setSelectTime(date2);
                workingHoursDOS = iWorkingHoursDao.queryByTimeCycle(workingHoursDO);
                defectDetailsDO.setRegistrationDate(date2);

                list = defectDetailsExtDao.findList(defectDetailsDO);
            }
            int a = 0;
            int b = 0;
            int d = 0;
            int c = 0;
            if (JudgeUtils.isNotEmpty(workingHoursDOS)) {
                for (int i = 0; i < workingHoursDOS.size(); i++) {
                    a = a + workingHoursDOS.get(i).getCaseWritingNumber();
                    b = b + workingHoursDOS.get(i).getCaseExecutionNumber();
                    d = d + workingHoursDOS.get(i).getCaseCompletedNumber();
                }
            }
            c = c + list.size();
            a1 = a1 + a;
            b1 = b1 + b;
            c1 = c1 + c;
            d1 = d1 + d;
            String str = "{ product: '" + m.getFullname() + "', 案例编写数: '" + a + "', 案例执行数: '" + b + "', 案例完成数: '" + d + "', 缺陷数: '" + c + "'}";
            workingHoursBOS.add(str);

        }
        String name = "案例编写数: " + a1 + ", 案例执行数: " + b1 + ", 案例完成数: " + d1 + ", 缺陷数: " + c1 ;
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO.setStringList(workingHoursBOS);
        demandHoursRspBO.setSum(name);
        return demandHoursRspBO;
    }


    @Override
    public DemandHoursRspBO getCentreBuildFailedCount(String date1, String date2) {
        List<BuildFailedCountDO> impl = null;
        BuildFailedCountDO buildFailedCountDO = new BuildFailedCountDO();
        impl = iBuildFailedCountDao.find(buildFailedCountDO);
        int a = 0;
        int b = 0;
        int c = 0;
        if (impl != null && impl.size() >= 0) {
            for (int i = 0; i < impl.size(); i++) {
                if (impl.get(i).getCount() == 1) {
                    a = a + 1;
                }
                if (impl.get(i).getCount() == 2) {
                    b = b + 1;
                }
                if (impl.get(i).getCount() >= 3) {
                    c = c + 1;
                }
            }
        }
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        SumBos.add("一次");
        workingHoursBOS.add(a + "");
        SumBos.add("二次");
        workingHoursBOS.add(b + "");
        SumBos.add("三次及以上");
        workingHoursBOS.add(c + "");
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO.setStringList(workingHoursBOS);
        demandHoursRspBO.setListSum(SumBos);
        return demandHoursRspBO;
    }

    @Override
    public DemandHoursRspBO getCentreSmokeTestFailedCount(String date1, String date2) {
        List<SmokeTestFailedCountDO> impl = null;
        SmokeTestFailedCountDO smokeTestFailedCountDO = new SmokeTestFailedCountDO();
        if (StringUtils.isBlank(date1) && StringUtils.isBlank(date2)) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如周、月!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        // 查询周
        if (StringUtils.isNotBlank(date1) && StringUtils.isBlank(date2)) {
            smokeTestFailedCountDO.setTestDate(date1);
            impl = iSmokeTestFailedCountDao.findWeek(smokeTestFailedCountDO);
        }
        // 查询月
        if (StringUtils.isNotBlank(date2) && StringUtils.isBlank(date1)) {
            smokeTestFailedCountDO.setTestDate(date2);
            impl = iSmokeTestFailedCountDao.findMonth(smokeTestFailedCountDO);
        }

        int a = 0;
        int b = 0;
        int c = 0;
        if (impl != null && impl.size() >= 0) {
            for (int i = 0; i < impl.size(); i++) {
                if (impl.get(i).getCount() == 1) {
                    a = a + 1;
                }
                if (impl.get(i).getCount() == 2) {
                    b = b + 1;
                }
                if (impl.get(i).getCount() >= 3) {
                    c = c + 1;
                }
            }
        }
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        SumBos.add("一次");
        workingHoursBOS.add(a + "");
        SumBos.add("二次");
        workingHoursBOS.add(b + "");
        SumBos.add("三次及以上");
        workingHoursBOS.add(c + "");
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO.setStringList(workingHoursBOS);
        demandHoursRspBO.setListSum(SumBos);
        return demandHoursRspBO;
    }

    /**
     * 按照月查询缺陷
     *
     * @param date1
     * @param date2
     * @return
     */
    @Override
    public DemandHoursRspBO getCentreFlawNumber(String date1, String date2) {
        List<DefectDetailsDO> impl = null;
        DefectDetailsDO workingHoursDO = new DefectDetailsDO();
        List<String> list = getSixMonth(date2);
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        for (int i = 0; i < list.size(); i++) {
            workingHoursDO.setRegistrationDate(list.get(i));
            impl = defectDetailsExtDao.findList(workingHoursDO);
            SumBos.add(list.get(i));
            if (impl != null && impl.size() >= 0) {
                workingHoursBOS.add(impl.size() + "");
            } else {
                workingHoursBOS.add("0");
            }

        }
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO.setStringList(workingHoursBOS);
        demandHoursRspBO.setListSum(SumBos);
        return demandHoursRspBO;
    }
    @Override
    public DemandHoursRspBO getCentreFlawNumberA(String date1, String date2) {
        List<ProductionDefectsDO> impl = null;
        ProductionDefectsDO workingHoursDO = new ProductionDefectsDO();
        List<String> list = getSixMonth(date2);
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        for (int i = 0; i < list.size(); i++) {
            workingHoursDO.setProcessstartdate(list.get(i));
            impl = productionDefectsExtDao.findList(workingHoursDO);
            SumBos.add(list.get(i));
            if (impl != null && impl.size() >= 0) {
                workingHoursBOS.add(impl.size() + "");
            } else {
                workingHoursBOS.add("0");
            }

        }
        System.err.println(impl);
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO.setStringList(workingHoursBOS);
        demandHoursRspBO.setListSum(SumBos);
        return demandHoursRspBO;
    }

    /**
     * 团队视图按月份查询评审问题
     *
     * @param date1
     * @param date2
     * @return
     */
    @Override
    public DemandHoursRspBO getCentreIssueNumber(String date1, String date2) {
        List<IssueDetailsDO> impl = null;
        IssueDetailsDO workingHoursDO = new IssueDetailsDO();
        List<String> list = getSixMonth(date2);
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        for (int i = 0; i < list.size(); i++) {
            workingHoursDO.setRegistrationDate(list.get(i));
            impl = issueDetailsExtDao.findList(workingHoursDO);
            SumBos.add(list.get(i));
            if (impl != null && impl.size() >= 0) {
                workingHoursBOS.add(impl.size() + "");
            } else {
                workingHoursBOS.add("0");
            }
        }
        System.err.println(impl);
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO.setStringList(workingHoursBOS);
        demandHoursRspBO.setListSum(SumBos);
        return demandHoursRspBO;
    }

    /**
     * 团队投产问题
     *
     * @param date1
     * @param date2
     * @return
     */
    @Override
    public DemandHoursRspBO getCentreProductionDefects(String date1, String date2) {
        List<ProblemDO> impl = null;
        ProblemDO workingHoursDO = new ProblemDO();
        List<String> list = getSixMonth(date2);
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        for (int i = 0; i < list.size(); i++) {
            workingHoursDO.setReqStartMon(list.get(i));
            impl = iProblemDao.findMonthList(workingHoursDO);
            SumBos.add(list.get(i));
            if (impl != null && impl.size() >= 0) {
                workingHoursBOS.add(impl.size() + "");
            } else {
                workingHoursBOS.add("0");
            }
        }
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO.setStringList(workingHoursBOS);
        demandHoursRspBO.setListSum(SumBos);
        return demandHoursRspBO;
    }

    /**
     * 中心团队投产验证不及时
     *
     * @param date1
     * @param date2
     * @return
     */
    @Override
    public DemandHoursRspBO getCentreProCheckTimeOutStatistics(String date1, String date2) {
        List<ProductionVerificationIsNotTimelyDO> impl = null;
        ProductionVerificationIsNotTimelyDO workingHoursDO = new ProductionVerificationIsNotTimelyDO();
        List<String> list = getSixMonth(date2);
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        for (int i = 0; i < list.size(); i++) {
            workingHoursDO.setProDate(list.get(i));
            impl = iProductionVerificationIsNotTimelyExtDao.findMonth(workingHoursDO);
            SumBos.add(list.get(i));
            int sum = 0;
            if (impl != null && impl.size() >= 0) {
                for (int j = 0; j < impl.size(); j++) {
                    sum = sum + Integer.parseInt(impl.get(j).getSumDay());
                }
                workingHoursBOS.add(sum + "");
            } else {
                workingHoursBOS.add("0");
            }
        }
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO.setStringList(workingHoursBOS);
        demandHoursRspBO.setListSum(SumBos);
        return demandHoursRspBO;
    }

    /**
     * 中心团队缺陷未解决
     *
     * @param date1
     * @param date2
     * @return
     */
    @Override
    public DemandHoursRspBO getCentreProUnhandledIssues1(String date1, String date2) {
        List<ProUnhandledIssuesDO> impl = null;
        ProUnhandledIssuesDO workingHoursDO = new ProUnhandledIssuesDO();
        List<String> list = getSixMonth(date2);
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        for (int i = 0; i < list.size(); i++) {
            workingHoursDO.setProductionDate(list.get(i));
            impl = proUnhandledIssuesExtDao.findMonth(workingHoursDO);
            SumBos.add(list.get(i));
            int sum = 0;
            if (impl != null && impl.size() >= 0) {
                for (int j = 0; j < impl.size(); j++) {
                    if(impl.get(j).getDefectsNumber() > 0){
                        sum = sum + 1;
                    }
                }
                workingHoursBOS.add(sum + "");
            } else {
                workingHoursBOS.add("0");
            }
        }
        System.err.println(impl);
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO.setStringList(workingHoursBOS);
        demandHoursRspBO.setListSum(SumBos);
        return demandHoursRspBO;
    }

    /**
     * 中心团队问题未解决
     *
     * @param date1
     * @param date2
     * @return
     */
    @Override
    public DemandHoursRspBO getCentreProUnhandledIssues2(String date1, String date2) {
        List<ProUnhandledIssuesDO> impl = null;
        ProUnhandledIssuesDO workingHoursDO = new ProUnhandledIssuesDO();
        List<String> list = getSixMonth(date2);
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        for (int i = 0; i < list.size(); i++) {
            workingHoursDO.setProductionDate(list.get(i));
            impl = proUnhandledIssuesExtDao.findMonth(workingHoursDO);
            SumBos.add(list.get(i));
            int sum = 0;
            if (impl != null && impl.size() >= 0) {
                for (int j = 0; j < impl.size(); j++) {
                    if(impl.get(j).getProblemNumber() >0){
                        sum = sum + 1;
                    }
                }
                workingHoursBOS.add(sum + "");
            } else {
                workingHoursBOS.add("0");
            }
        }
        System.err.println(impl);
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO.setStringList(workingHoursBOS);
        demandHoursRspBO.setListSum(SumBos);
        return demandHoursRspBO;
    }

    /**
     * 按照月查询缺陷 中心各团队
     *
     * @param date1
     * @param date2
     * @return
     */
    @Override
    public DemandHoursRspBO getCentreFlawNumberDept(String date1, String date2) {
        // 查询各个部门集合
        List<OrganizationStructureDO> dos = iOrganizationStructureDao.find(new OrganizationStructureDO());
        List<DefectDetailsDO> impl = null;
        DefectDetailsDO workingHoursDO = new DefectDetailsDO();
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        for (int i = 0; i < dos.size(); i++) {
            // 去掉4个项目组和质量监督、产品测试
            if("质量监督组".equals(dos.get(i).getSecondlevelorganization())||"产品测试团队".equals(dos.get(i).getSecondlevelorganization())||"资金归集项目组".equals(dos.get(i).getSecondlevelorganization())
                    ||"设计项目组".equals(dos.get(i).getSecondlevelorganization()) ||"团体组织交费项目组".equals(dos.get(i).getSecondlevelorganization()) ||"客服中间层项目组".equals(dos.get(i).getSecondlevelorganization())){
                continue;
            }
            System.err.println(date1 + "=====" + date2);
            if (StringUtils.isBlank(date1) && StringUtils.isBlank(date2)) {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如周、月!");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            workingHoursDO.setProblemHandlerDepartment(dos.get(i).getSecondlevelorganization());
            // 查询周
            if (StringUtils.isNotBlank(date1) && StringUtils.isBlank(date2)) {
                workingHoursDO.setRegistrationDate(date1);
                impl = defectDetailsExtDao.findWeekList(workingHoursDO);
            }
            // 查询月
            if (StringUtils.isNotBlank(date2) && StringUtils.isBlank(date1)) {
                workingHoursDO.setRegistrationDate(date2);
                impl = defectDetailsExtDao.findList(workingHoursDO);
            }
            SumBos.add(dos.get(i).getSecondlevelorganization());
            if (impl != null && impl.size() >= 0) {
                workingHoursBOS.add(impl.size() + "");
            } else {
                workingHoursBOS.add("0");
            }
        }
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO.setStringList(workingHoursBOS);
        demandHoursRspBO.setListSum(SumBos);
        return demandHoursRspBO;
    }

    @Override
    public DemandHoursRspBO getCentreFlawNumberDeptA(String date1, String date2) {
        // 查询各个部门集合
        List<OrganizationStructureDO> dos = iOrganizationStructureDao.find(new OrganizationStructureDO());
        List<ProductionDefectsDO> impl = null;
        ProductionDefectsDO workingHoursDO = new ProductionDefectsDO();
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        for (int i = 0; i < dos.size(); i++) {
            // 去掉4个项目组和质量监督、产品测试
            if("质量监督组".equals(dos.get(i).getSecondlevelorganization())||"产品测试团队".equals(dos.get(i).getSecondlevelorganization())||"资金归集项目组".equals(dos.get(i).getSecondlevelorganization())
                    ||"设计项目组".equals(dos.get(i).getSecondlevelorganization()) ||"团体组织交费项目组".equals(dos.get(i).getSecondlevelorganization()) ||"客服中间层项目组".equals(dos.get(i).getSecondlevelorganization())){
                continue;
            }
            System.err.println(date1 + "=====" + date2);
            if (StringUtils.isBlank(date1) && StringUtils.isBlank(date2)) {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如周、月!");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            workingHoursDO.setProblemattributiondept(dos.get(i).getSecondlevelorganization());
            // 查询周
            if (StringUtils.isNotBlank(date1) && StringUtils.isBlank(date2)) {
                workingHoursDO.setProcessstartdate(date1);
                impl = productionDefectsExtDao.findWeekList(workingHoursDO);
            }
            // 查询月
            if (StringUtils.isNotBlank(date2) && StringUtils.isBlank(date1)) {
                workingHoursDO.setProcessstartdate(date2);
                impl = productionDefectsExtDao.findList(workingHoursDO);
            }
            SumBos.add(dos.get(i).getSecondlevelorganization());
            if (impl != null && impl.size() >= 0) {
                workingHoursBOS.add(impl.size() + "");
            } else {
                workingHoursBOS.add("0");
            }
        }
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO.setStringList(workingHoursBOS);
        demandHoursRspBO.setListSum(SumBos);
        return demandHoursRspBO;
    }

    /**
     * 团队视图按月份查询评审问题 中心各团队
     *
     * @param date1
     * @param date2
     * @return
     */
    @Override
    public DemandHoursRspBO getCentreIssueNumberDept(String date1, String date2) {
        // 查询各个部门集合
        List<OrganizationStructureDO> dos = iOrganizationStructureDao.find(new OrganizationStructureDO());
        List<IssueDetailsDO> impl = null;
        IssueDetailsDO workingHoursDO = new IssueDetailsDO();
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        for (int i = 0; i < dos.size(); i++) {
            // 去掉4个项目组和质量监督、产品测试
            if("质量监督组".equals(dos.get(i).getSecondlevelorganization())||"产品测试团队".equals(dos.get(i).getSecondlevelorganization())||"资金归集项目组".equals(dos.get(i).getSecondlevelorganization())
                    ||"设计项目组".equals(dos.get(i).getSecondlevelorganization()) ||"团体组织交费项目组".equals(dos.get(i).getSecondlevelorganization()) ||"客服中间层项目组".equals(dos.get(i).getSecondlevelorganization())){
                continue;
            }
            System.err.println(date1 + "=====" + date2);
            if (StringUtils.isBlank(date1) && StringUtils.isBlank(date2)) {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如周、月!");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            workingHoursDO.setIssueDepartment(dos.get(i).getSecondlevelorganization());
            // 查询周
            if (StringUtils.isNotBlank(date1) && StringUtils.isBlank(date2)) {
                workingHoursDO.setRegistrationDate(date1);
                impl = issueDetailsExtDao.findWeekList(workingHoursDO);
            }
            // 查询月
            if (StringUtils.isNotBlank(date2) && StringUtils.isBlank(date1)) {
                workingHoursDO.setRegistrationDate(date2);
                impl = issueDetailsExtDao.findList(workingHoursDO);
            }
            SumBos.add(dos.get(i).getSecondlevelorganization());
            if (impl != null && impl.size() >= 0) {
                workingHoursBOS.add(impl.size() + "");
            } else {
                workingHoursBOS.add("0");
            }
        }
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO.setStringList(workingHoursBOS);
        demandHoursRspBO.setListSum(SumBos);
        return demandHoursRspBO;
    }

    /**
     * 团队生产问题 中心各团队
     *
     * @param date1
     * @param date2
     * @return
     */
    @Override
    public DemandHoursRspBO getCentreProductionDefectsDept(String date1, String date2) {
        List<OrganizationStructureDO> dos = iOrganizationStructureDao.find(new OrganizationStructureDO());
        List<ProblemDO> impl = null;
        ProblemDO workingHoursDO = new ProblemDO();
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        for (int i = 0; i < dos.size(); i++) {
            // 去掉4个项目组和质量监督、产品测试
            if("质量监督组".equals(dos.get(i).getSecondlevelorganization())||"产品测试团队".equals(dos.get(i).getSecondlevelorganization())||"资金归集项目组".equals(dos.get(i).getSecondlevelorganization())
                    ||"设计项目组".equals(dos.get(i).getSecondlevelorganization()) ||"团体组织交费项目组".equals(dos.get(i).getSecondlevelorganization()) ||"客服中间层项目组".equals(dos.get(i).getSecondlevelorganization())){
                continue;
            }
            if (StringUtils.isBlank(date1) && StringUtils.isBlank(date2)) {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如周、月!");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            workingHoursDO.setDevpLeadDept(dos.get(i).getSecondlevelorganization());
            // 查询周
            if (StringUtils.isNotBlank(date1) && StringUtils.isBlank(date2)) {
                workingHoursDO.setReqStartMon(date1);
                impl = iProblemDao.findWeekList(workingHoursDO);
            }
            // 查询月
            if (StringUtils.isNotBlank(date2) && StringUtils.isBlank(date1)) {
                workingHoursDO.setReqStartMon(date2);
                impl = iProblemDao.findMonthList(workingHoursDO);
            }
            SumBos.add(dos.get(i).getSecondlevelorganization());
            if (impl != null && impl.size() >= 0) {
                workingHoursBOS.add(impl.size() + "");
            } else {
                workingHoursBOS.add("0");
            }
        }
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO.setStringList(workingHoursBOS);
        demandHoursRspBO.setListSum(SumBos);
        return demandHoursRspBO;
    }

    /**
     * 中心团队投产验证不及时 中心各团队
     *
     * @param date1
     * @param date2
     * @return
     */
    @Override
    public DemandHoursRspBO getCentreProCheckTimeOutStatisticsDept(String date1, String date2) {
        List<OrganizationStructureDO> dos = iOrganizationStructureDao.find(new OrganizationStructureDO());
        List<ProductionVerificationIsNotTimelyDO> impl = null;
        ProductionVerificationIsNotTimelyDO workingHoursDO = new ProductionVerificationIsNotTimelyDO();
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        for (int i = 0; i < dos.size(); i++) {
            // 去掉4个项目组和质量监督、产品测试
            if("质量监督组".equals(dos.get(i).getSecondlevelorganization())||"产品测试团队".equals(dos.get(i).getSecondlevelorganization())||"资金归集项目组".equals(dos.get(i).getSecondlevelorganization())
                    ||"设计项目组".equals(dos.get(i).getSecondlevelorganization()) ||"团体组织交费项目组".equals(dos.get(i).getSecondlevelorganization()) ||"客服中间层项目组".equals(dos.get(i).getSecondlevelorganization())){
                continue;
            }
            if (StringUtils.isBlank(date1) && StringUtils.isBlank(date2)) {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如周、月!");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            workingHoursDO.setDepartment(dos.get(i).getSecondlevelorganization());
            // 查询周
            if (StringUtils.isNotBlank(date1) && StringUtils.isBlank(date2)) {
                workingHoursDO.setProDate(date1);
                impl = iProductionVerificationIsNotTimelyExtDao.findWeek(workingHoursDO);
            }
            // 查询月
            if (StringUtils.isNotBlank(date2) && StringUtils.isBlank(date1)) {
                workingHoursDO.setProDate(date2);
                impl = iProductionVerificationIsNotTimelyExtDao.findMonth(workingHoursDO);
            }
            SumBos.add(dos.get(i).getSecondlevelorganization());
            int sum = 0;
            if (impl != null && impl.size() >= 0) {
                for (int j = 0; j < impl.size(); j++) {
                    sum = sum + Integer.parseInt(impl.get(j).getSumDay());
                }
                workingHoursBOS.add(sum + "");
            } else {
                workingHoursBOS.add("0");
            }
        }
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO.setStringList(workingHoursBOS);
        demandHoursRspBO.setListSum(SumBos);
        return demandHoursRspBO;
    }

    /**
     * 中心团队缺陷未解决 中心各团队
     *
     * @param date1
     * @param date2
     * @return
     */
    @Override
    public DemandHoursRspBO getCentreProUnhandledIssues1Dept(String date1, String date2) {
        List<OrganizationStructureDO> dos = iOrganizationStructureDao.find(new OrganizationStructureDO());
        List<ProUnhandledIssuesDO> impl = null;
        ProUnhandledIssuesDO workingHoursDO = new ProUnhandledIssuesDO();
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        for (int i = 0; i < dos.size(); i++) {
            // 去掉4个项目组和质量监督、产品测试
            if("质量监督组".equals(dos.get(i).getSecondlevelorganization())||"产品测试团队".equals(dos.get(i).getSecondlevelorganization())||"资金归集项目组".equals(dos.get(i).getSecondlevelorganization())
                    ||"设计项目组".equals(dos.get(i).getSecondlevelorganization()) ||"团体组织交费项目组".equals(dos.get(i).getSecondlevelorganization()) ||"客服中间层项目组".equals(dos.get(i).getSecondlevelorganization())){
                continue;
            }
            if (StringUtils.isBlank(date1) && StringUtils.isBlank(date2)) {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如周、月!");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            workingHoursDO.setDepartment(dos.get(i).getSecondlevelorganization());
            // 查询周
            if (StringUtils.isNotBlank(date1) && StringUtils.isBlank(date2)) {
                workingHoursDO.setProductionDate(date1);
                impl = proUnhandledIssuesExtDao.findWeek(workingHoursDO);
            }
            // 查询月
            if (StringUtils.isNotBlank(date2) && StringUtils.isBlank(date1)) {
                workingHoursDO.setProductionDate(date2);
                impl = proUnhandledIssuesExtDao.findMonth(workingHoursDO);
            }
            SumBos.add(dos.get(i).getSecondlevelorganization());
            int sum = 0;
            if (impl != null && impl.size() >= 0) {
                for (int j = 0; j < impl.size(); j++) {
                    if(impl.get(j).getDefectsNumber() > 0){
                        sum = sum + 1;
                    }
                }
                workingHoursBOS.add(sum + "");
            } else {
                workingHoursBOS.add("0");
            }
        }
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO.setStringList(workingHoursBOS);
        demandHoursRspBO.setListSum(SumBos);
        return demandHoursRspBO;
    }

    /**
     * 中心团队问题未解决 中心各团队
     *
     * @param date1
     * @param date2
     * @return
     */
    @Override
    public DemandHoursRspBO getCentreProUnhandledIssues2Dept(String date1, String date2) {
        List<OrganizationStructureDO> dos = iOrganizationStructureDao.find(new OrganizationStructureDO());
        List<ProUnhandledIssuesDO> impl = null;
        ProUnhandledIssuesDO workingHoursDO = new ProUnhandledIssuesDO();
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        for (int i = 0; i < dos.size(); i++) {
            // 去掉4个项目组和质量监督、产品测试
            if("质量监督组".equals(dos.get(i).getSecondlevelorganization())||"产品测试团队".equals(dos.get(i).getSecondlevelorganization())||"资金归集项目组".equals(dos.get(i).getSecondlevelorganization())
                    ||"设计项目组".equals(dos.get(i).getSecondlevelorganization()) ||"团体组织交费项目组".equals(dos.get(i).getSecondlevelorganization()) ||"客服中间层项目组".equals(dos.get(i).getSecondlevelorganization())){
                continue;
            }
            if (StringUtils.isBlank(date1) && StringUtils.isBlank(date2)) {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如周、月!");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            workingHoursDO.setDepartment(dos.get(i).getSecondlevelorganization());
            // 查询周
            if (StringUtils.isNotBlank(date1) && StringUtils.isBlank(date2)) {
                workingHoursDO.setProductionDate(date1);
                impl = proUnhandledIssuesExtDao.findWeek(workingHoursDO);
            }
            // 查询月
            if (StringUtils.isNotBlank(date2) && StringUtils.isBlank(date1)) {
                workingHoursDO.setProductionDate(date2);
                impl = proUnhandledIssuesExtDao.findMonth(workingHoursDO);
            }
            SumBos.add(dos.get(i).getSecondlevelorganization());
            int sum = 0;
            if (impl != null && impl.size() >= 0) {
                for (int j = 0; j < impl.size(); j++) {
                    if(impl.get(j).getProblemNumber() > 0){
                        sum = sum + 1;
                    }

                }
                workingHoursBOS.add(sum + "");
            } else {
                workingHoursBOS.add("0");
            }
        }
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO.setStringList(workingHoursBOS);
        demandHoursRspBO.setListSum(SumBos);
        return demandHoursRspBO;
    }


    @Override
    public DemandHoursRspBO getCentreDispose(String date1, String date2) {
        List<DemandDO> impl = null;
        WorkingHoursDO workingHoursDO = new WorkingHoursDO();
        workingHoursDO.setSelectTime(date2);
        impl = demandDao.findListDeptDemand(workingHoursDO);
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        String sum = "";
        int a = 0;
        int b = 0;
        int c = 0;
        if (impl != null && impl.size() >= 0) {
            // 查询月需求进度异常需求的个数
            DemandBO demandBO = new DemandBO();
            demandBO.setReqImplMon(date2);
            demandBO.setReqAbnorType("02");
            demandBO.setPageNum(1);
            demandBO.setPageSize(500);
            DemandRspBO demandRspBO = reqPlanService.findDemand(demandBO);
            int d =0;
            if(demandRspBO.getDemandBOList()!=null ||demandRspBO.getDemandBOList().size()>0){
                d = demandRspBO.getDemandBOList().size();
            }
            sum = impl.size() + "";
            for (int i = 0; i < impl.size(); i++) {
                if (!impl.get(i).getReqSts().equals("30") && !impl.get(i).getReqSts().equals("40")) {
                    a = a + 1;
                }
                if (impl.get(i).getReqSts().equals("30")) {
                    b = b + 1;
                }
                if (impl.get(i).getReqSts().equals("40")) {
                    c = c + 1;
                }
            }
            if (a > 0) {
                int f = a - d;
                String a1 = "{'value': '" + f + "', 'name': '正常需求'}";
                workingHoursBOS.add(a1);
                SumBos.add("正常需求");
            }
            if (b > 0) {
                String b1 = "{'value': '" + b + "', 'name': '取消需求'}";
                workingHoursBOS.add(b1);
                SumBos.add("取消需求");
            }
            if (c > 0) {
                String c1 = "{'value': '" + c + "', 'name': '暂停需求'}";
                workingHoursBOS.add(c1);
                SumBos.add("暂停需求");
            }
            String d1 = "{'value': '" + d + "', 'name': '异常需求'}";
            workingHoursBOS.add(d1);
            SumBos.add("异常需求");
        } else {
            sum = "0";
        }

        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO.setStringList(workingHoursBOS);
        demandHoursRspBO.setListSum(SumBos);
        demandHoursRspBO.setSum(sum);
        return demandHoursRspBO;
    }

    /**
     * 获取指定月份的前六个月
     *
     * @param date
     * @return
     */
    public static List<String> getSixMonth(String date) {
        //返回值
        List<String> list = new ArrayList<String>();
        int month = Integer.parseInt(date.substring(5, 7));
        int year = Integer.parseInt(date.substring(0, 4));
        for (int i = 5; i >= 0; i--) {
            if (month > 6) {
                if (month - i >= 10) {
                    list.add(year + "-" + String.valueOf(month - i));
                } else {
                    list.add(year + "-0" + String.valueOf(month - i));
                }
            } else {
                if (month - i <= 0) {
                    if (month - i + 12 >= 10) {
                        list.add(String.valueOf(year - 1) + "-" + String.valueOf(month - i + 12));
                    } else {
                        list.add(String.valueOf(year - 1) + "-0" + String.valueOf(month - i + 12));
                    }
                } else {
                    if (month - i >= 10) {
                        list.add(String.valueOf(year) + "-" + String.valueOf(month - i));
                    } else {
                        list.add(String.valueOf(year) + "-0" + String.valueOf(month - i));
                    }
                }
            }
        }
        return list;

    }

    @Override
    public WorkingHoursBO getReportForm11(String displayname, String date1, String date2) {
        WorkingHoursBO workingHoursBO = new WorkingHoursBO();
        List<WorkingHoursDO> impl = null;
        WorkingHoursDO workingHoursDO = new WorkingHoursDO();
        workingHoursDO.setDisplayname(displayname);
        System.err.println(date1 + "=====" + date2);
        if (StringUtils.isBlank(date1) && StringUtils.isBlank(date2)) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如周、月!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        // 查询周
        if (StringUtils.isNotBlank(date1) && StringUtils.isBlank(date2)) {
            workingHoursDO.setSelectTime(date1);
            impl = iWorkingHoursDao.findWeekView(workingHoursDO);
            System.err.println(impl);
        }
        // 查询月
        if (StringUtils.isNotBlank(date2) && StringUtils.isBlank(date1)) {
            workingHoursDO.setSelectTime(date2);
            impl = iWorkingHoursDao.findMonthView(workingHoursDO);
            System.err.println(impl);
        }
        if (impl != null && impl.size() >= 0) {
            BeanUtils.copyPropertiesReturnDest(workingHoursBO, impl.get(0));
        }
        System.err.println(impl);
        return workingHoursBO;
    }

    @Override
    public List<WorkingHoursBO> getDemandStaffView(String displayname, String date1, String date2) {
        List<WorkingHoursBO> workingHoursBos = new LinkedList<>();
        List<WorkingHoursDO> impl = null;
        WorkingHoursDO workingHoursDO = new WorkingHoursDO();
        workingHoursDO.setDisplayname(displayname);
        System.err.println(date1 + "=====" + date2);
        if (StringUtils.isBlank(date1) && StringUtils.isBlank(date2)) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如周、月!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        // 查询周
        if (StringUtils.isNotBlank(date1) && StringUtils.isBlank(date2)) {
            workingHoursDO.setSelectTime(date1);
            impl = iWorkingHoursDao.findListWeekView(workingHoursDO);
            System.err.println(impl);
        }
        // 查询月
        if (StringUtils.isNotBlank(date2) && StringUtils.isBlank(date1)) {
            workingHoursDO.setSelectTime(date2);
            impl = iWorkingHoursDao.findListMonthView(workingHoursDO);
            System.err.println(impl);
        }
        impl.forEach(m ->
                workingHoursBos.add(BeanUtils.copyPropertiesReturnDest(new WorkingHoursBO(), m))
        );
        if (impl != null && impl.size() >= 0) {
            for (int i = 0; i < impl.size(); i++) {
                DemandJiraDO demandJiraDO = new DemandJiraDO();
                if (impl.get(i).getEpickey() == null || impl.get(i).getEpickey() == "") {
                    continue;
                }
                demandJiraDO.setJiraKey(impl.get(i).getEpickey());
                // 根据jiraKey获取内部编号
                List<DemandJiraDO> demandJiraDos = demandJiraDao.find(demandJiraDO);
                // 根据内部编号获取 需求名及需求编号
                if (demandJiraDos != null && demandJiraDos.size() != 0) {
                    DemandDO demandDO = demandDao.get(demandJiraDos.get(demandJiraDos.size() - 1).getReqInnerSeq());
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
     *
     * @param devpLeadDept
     * @param date1
     * @param date2
     * @return
     */
    @Override
    public List<WorkingHoursBO> getDeptStaffView(String devpLeadDept, String date1, String date2) {
        List<WorkingHoursBO> workingHoursBos = new LinkedList<>();
        List<WorkingHoursDO> impl = null;
        WorkingHoursDO workingHoursDO = new WorkingHoursDO();
        workingHoursDO.setDevpLeadDept(devpLeadDept);
        System.err.println(date1 + "=====" + date2);
        if (StringUtils.isBlank(date1) && StringUtils.isBlank(date2)) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如周、月!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        // 查询周
        if (StringUtils.isNotBlank(date1) && StringUtils.isBlank(date2)) {
            workingHoursDO.setSelectTime(date1);
            impl = iWorkingHoursDao.findListWeekViewDept(workingHoursDO);
            System.err.println(impl);
        }
        // 查询月
        if (StringUtils.isNotBlank(date2) && StringUtils.isBlank(date1)) {
            workingHoursDO.setSelectTime(date2);
            impl = iWorkingHoursDao.findListMonthViewDept(workingHoursDO);
            System.err.println(impl);
        }
        impl.forEach(m ->
                workingHoursBos.add(BeanUtils.copyPropertiesReturnDest(new WorkingHoursBO(), m))
        );
        if (impl != null && impl.size() >= 0) {
            for (int i = 0; i < impl.size(); i++) {
                DemandJiraDO demandJiraDO = new DemandJiraDO();
                if (impl.get(i).getEpickey() == null || impl.get(i).getEpickey() == "") {
                    continue;
                }
                demandJiraDO.setJiraKey(impl.get(i).getEpickey());
                // 根据jiraKey获取内部编号
                List<DemandJiraDO> demandJiraDos = demandJiraDao.find(demandJiraDO);
                // 根据内部编号获取 需求名及需求编号
                if (demandJiraDos != null && demandJiraDos.size() != 0) {
                    DemandDO demandDO = demandDao.get(demandJiraDos.get(demandJiraDos.size() - 1).getReqInnerSeq());
                    workingHoursBos.get(i).setReqNo(demandDO.getReqNo());
                    workingHoursBos.get(i).setReqNm(demandDO.getReqNm());
                }
            }
        }
        System.err.println("112222222231132132" + workingHoursBos);
        return workingHoursBos;
    }

    /**
     * 需求任务与其它任务工时间饼图
     *
     * @param displayname
     * @param date1
     * @param date2
     * @return
     */
    @Override
    public DemandHoursRspBO getDemandStaffTask(String displayname, String date1, String date2) {
        DecimalFormat df = new DecimalFormat("0.##");
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        List<WorkingHoursDO> impl = null;
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        String sum = "";
        double sumx = 0;
        WorkingHoursDO workingHoursDO = new WorkingHoursDO();
        workingHoursDO.setDisplayname(displayname);
        System.err.println(date1 + "=====" + date2);
        if (StringUtils.isBlank(date1) && StringUtils.isBlank(date2)) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如周、月!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        // 查询周
        if (StringUtils.isNotBlank(date1) && StringUtils.isBlank(date2)) {
            workingHoursDO.setSelectTime(date1);
            impl = iWorkingHoursDao.findListWeekView(workingHoursDO);
        }
        // 查询月
        if (StringUtils.isNotBlank(date2) && StringUtils.isBlank(date1)) {
            workingHoursDO.setSelectTime(date2);
            impl = iWorkingHoursDao.findListMonthView(workingHoursDO);
        }
        int sumDemand = 0;
        for (int i = 0; i < impl.size(); i++) {
            String sumTime = "";
            // 获取其它任务的工时间
            if (impl.get(i).getEpickey() == null || impl.get(i).getEpickey() == "") {
                String sumTime1 = impl.get(i).getTimespnet();
                sumx = sumx + getWorkHours(Integer.parseInt(sumTime1));
                // {value: 5, name: '需求任务'}
                String qtDemand = "{'value': '" + df.format(getWorkHours(Integer.parseInt(sumTime1))) + "', 'name': '其它任务'}";

                workingHoursBOS.add(String.valueOf(qtDemand));
                SumBos.add("其它任务");
            } else {
                //需求任务的工时间
                sumTime = impl.get(i).getTimespnet();
                sumDemand = sumDemand + Integer.parseInt(sumTime);
            }

        }
        sumx = sumx + getWorkHours(sumDemand);
        String Demand = "{'value': '" + df.format(getWorkHours(sumDemand)) + "', 'name': '需求任务'}";
        workingHoursBOS.add(Demand);
        SumBos.add("需求任务");
        sum = df.format(sumx);
        demandHoursRspBO.setListSum(SumBos);
        demandHoursRspBO.setSum(sum);
        demandHoursRspBO.setStringList(workingHoursBOS);
        return demandHoursRspBO;
    }

    @Override
    public DefectDetailsRspBO getDemandDefectDetails(String displayname, String date1, String date2) {
        System.err.println("缺陷问题");
        List<DefectDetailsDO> impl = null;
        List<DefectDetailsBO> defectDetailsBOList = null;
        DefectDetailsDO defectDetailsDO = new DefectDetailsDO();
        defectDetailsDO.setAssignee(displayname);
        System.err.println(date1 + "=====" + date2);
        if (StringUtils.isBlank(date1) && StringUtils.isBlank(date2)) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如周、月!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        // 查询周
        if (StringUtils.isNotBlank(date1) && StringUtils.isBlank(date2)) {
            defectDetailsDO.setRegistrationDate(date1);
            impl = defectDetailsExtDao.findWeekList(defectDetailsDO);
            System.err.println(impl);
        }
        // 查询月
        if (StringUtils.isNotBlank(date2) && StringUtils.isBlank(date1)) {
            defectDetailsDO.setRegistrationDate(date2);
            impl = defectDetailsExtDao.findList(defectDetailsDO);
            System.err.println(impl);
        }
        DefectDetailsRspBO defectDetailsRspBO = new DefectDetailsRspBO();
        defectDetailsBOList = BeanConvertUtils.convertList(impl, DefectDetailsBO.class);
        defectDetailsRspBO.setDefectDetailsBos(defectDetailsBOList);
        return defectDetailsRspBO;
    }

    /**
     * 个人视图评审问题
     *
     * @param displayname
     * @param date1
     * @param date2
     * @return
     */
    @Override
    public IssueDetailsRspBO getDemandIssueDetails(String displayname, String date1, String date2) {
        IssueDetailsDO issueDetailsDO = new IssueDetailsDO();
        List<IssueDetailsDO> impl = null;
        List<IssueDetailsBO> issueDetailsBOList = null;
        issueDetailsDO.setAssignee(displayname);
        if (StringUtils.isBlank(date1) && StringUtils.isBlank(date2)) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如周、月!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        // 查询周
        if (StringUtils.isNotBlank(date1) && StringUtils.isBlank(date2)) {
            issueDetailsDO.setRegistrationDate(date1);
            impl = issueDetailsExtDao.findWeekList(issueDetailsDO);
            System.err.println(impl);
        }
        // 查询月
        if (StringUtils.isNotBlank(date2) && StringUtils.isBlank(date1)) {
            issueDetailsDO.setRegistrationDate(date2);
            impl = issueDetailsExtDao.findList(issueDetailsDO);
            System.err.println(impl);
        }
        IssueDetailsRspBO issueDetailsRspBO = new IssueDetailsRspBO();
        issueDetailsBOList = BeanConvertUtils.convertList(impl, IssueDetailsBO.class);
        issueDetailsRspBO.setIssueDetailsBOList(issueDetailsBOList);
        return issueDetailsRspBO;
    }

    @Override
    public ProductionDefectsRspBO getDemandProductionDetails(String displayname, String date1, String date2) {
        ProductionDefectsDO productionDefectsDO = new ProductionDefectsDO();
        List<ProductionDefectsDO> impl = null;
        List<ProductionDefectsBO> productionDefectsBOList = null;
        productionDefectsDO.setPersonincharge(displayname);
        if (StringUtils.isBlank(date1) && StringUtils.isBlank(date2)) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如周、月!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        // 查询周
        if (StringUtils.isNotBlank(date1) && StringUtils.isBlank(date2)) {
            productionDefectsDO.setProcessstartdate(date1);
            impl = productionDefectsExtDao.findWeekList(productionDefectsDO);
            System.err.println(impl);
        }
        // 查询月
        if (StringUtils.isNotBlank(date2) && StringUtils.isBlank(date1)) {
            productionDefectsDO.setProcessstartdate(date2);
            impl = productionDefectsExtDao.findMonthList(productionDefectsDO);
            System.err.println(impl);
        }
        ProductionDefectsRspBO productionDefectsRspBO = new ProductionDefectsRspBO();
        productionDefectsBOList = BeanConvertUtils.convertList(impl, ProductionDefectsBO.class);
        productionDefectsRspBO.setProductionDefectsBOList(productionDefectsBOList);
        return productionDefectsRspBO;
    }

    @Override
    public DemandHoursRspBO getDemandHours(String epic) {
        DecimalFormat df = new DecimalFormat("0.##");
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        List<String> workingHoursBOS = new LinkedList<>();
        DemandResourceInvestedDO demandResourceInvestedDO = new DemandResourceInvestedDO();
        demandResourceInvestedDO.setEpicKey(epic);
        List<DemandResourceInvestedDO> impl = iDemandResourceInvestedDao.find(demandResourceInvestedDO);
        impl.forEach(m ->
                workingHoursBOS.add(m.getWorkHoursToString())
        );
        List<String> listSumBos = new LinkedList<>();
        impl.forEach(m ->
                listSumBos.add(m.getDepartment())
        );
        String sum = "";
        List<String> SumBos = new LinkedList<>();
        impl.forEach(m ->
                SumBos.add(m.getValue())
        );
        int sum1 = 0;
        if (SumBos != null || SumBos.size() != 0) {
            for (int i = 0; i < SumBos.size(); i++) {
                int sumx = Integer.parseInt(SumBos.get(i));
                sum1 += sumx;
            }
            System.err.println(sum1);
            double s = getWorkHours(sum1);
            sum = df.format(s);
        }
        demandHoursRspBO.setListSum(listSumBos);
        demandHoursRspBO.setSum(sum);
        demandHoursRspBO.setStringList(workingHoursBOS);
        System.err.println(demandHoursRspBO);
        return demandHoursRspBO;
    }

    @Override
    public DemandHoursRspBO getDemandHoursRole(String epic) {
        DecimalFormat df = new DecimalFormat("0.##");
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        List<String> workingHoursBOS = new LinkedList<>();
        WorkingHoursDO workingHoursDO = new WorkingHoursDO();
        workingHoursDO.setEpickey(epic);
        List<WorkingHoursDO> impl = iWorkingHoursDao.getDemandHoursRole(workingHoursDO);
        if (impl == null || impl.size() == 0) {
            demandHoursRspBO.setSum("0");
            return demandHoursRspBO;
        }
        impl.forEach(m ->
                workingHoursBOS.add(m.getWorkHoursToString())
        );
        List<String> listSumBos = new LinkedList<>();
        impl.forEach(m ->
                listSumBos.add(m.getRoletype())
        );
        String sum = "";
        List<String> SumBos = new LinkedList<>();
        impl.forEach(m ->
                SumBos.add(m.getTimespnet())
        );
        int sum1 = 0;
        if (SumBos != null || SumBos.size() != 0) {
            for (int i = 0; i < SumBos.size(); i++) {
                int sumx = Integer.parseInt(SumBos.get(i));
                sum1 += sumx;
            }
            double s = getWorkHours(sum1);
            sum = df.format(s);
        }
        demandHoursRspBO.setListSum(listSumBos);
        demandHoursRspBO.setSum(sum);
        demandHoursRspBO.setStringList(workingHoursBOS);
        System.err.println(demandHoursRspBO);
        return demandHoursRspBO;
    }

    @Override
    public DemandHoursRspBO getWorkLoad(String epic) {
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        System.err.println(epic + "功能点=========================");
        DemandDO demand = demandDao.get(epic);
        String str = getWorkLoad(demand);
        if (str == null || "".equals(str)) {
            System.err.println("未录入功能点");
            demandHoursRspBO.setSum("0");
            return demandHoursRspBO;
        }
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        String sum = "";
        String[] pro_number_list = str.split(";");
        System.err.println(pro_number_list);
        double sumx = 0;
        for (int i = 0; i < pro_number_list.length; i++) {
            System.err.println(pro_number_list[i]);
            String[] dept_list = pro_number_list[i].split(":");
            System.err.println(dept_list[0]);
            System.err.println(dept_list[1]);
            SumBos.add(dept_list[0]);
            String dept = "{'value': '" + dept_list[1] + "', 'name': '" + dept_list[0] + "'}";
            workingHoursBOS.add(dept);
            sumx += Double.valueOf(dept_list[1]);
        }
        DecimalFormat df = new DecimalFormat("0.##");
        demandHoursRspBO.setListSum(SumBos);
        demandHoursRspBO.setSum(df.format(sumx));
        demandHoursRspBO.setStringList(workingHoursBOS);
        System.err.println(demandHoursRspBO);
        return demandHoursRspBO;
    }

    /**
     * 根据epic获取缺陷类型数
     *
     * @param epic
     * @return
     */
    @Override
    public DemandHoursRspBO getFlawNumber(String epic) {
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        ProblemStatisticDO problemStatisticDO = problemStatisticDao.get(epic);
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        String sum = "";
        int sumx = 0;
        if (problemStatisticDO == null || "".equals(problemStatisticDO)) {
            demandHoursRspBO.setSum("0");
            return demandHoursRspBO;
        }
        /**
         * @Fields externalDefectsNumber 外围平台缺陷数
         */
        if (problemStatisticDO.getExternalDefectsNumber() != 0) {
            SumBos.add("外围平台缺陷数");
            workingHoursBOS.add(problemStatisticDO.getExternalDefectsNumber() + "");
            sumx = sumx + problemStatisticDO.getExternalDefectsNumber();
        }
        /**
         * @Fields versionDefectsNumber 版本更新缺陷数
         */
        if (problemStatisticDO.getVersionDefectsNumber() != 0) {
            SumBos.add("版本更新缺陷数");
            workingHoursBOS.add(problemStatisticDO.getVersionDefectsNumber() + "");
            sumx = sumx + problemStatisticDO.getVersionDefectsNumber();
        }
        /**
         * @Fields parameterDefectsNumber 参数配置缺陷数
         */
        if (problemStatisticDO.getParameterDefectsNumber() != 0) {
            SumBos.add("参数配置缺陷数");
            workingHoursBOS.add(problemStatisticDO.getParameterDefectsNumber() + "");
            sumx = sumx + problemStatisticDO.getParameterDefectsNumber();
        }
        /**
         * @Fields functionDefectsNumber 功能设计缺陷数
         */
        if (problemStatisticDO.getFunctionDefectsNumber() != 0) {
            SumBos.add("功能设计缺陷数");
            workingHoursBOS.add(problemStatisticDO.getFunctionDefectsNumber() + "");
            sumx = sumx + problemStatisticDO.getFunctionDefectsNumber();
        }
        /**
         * @Fields processDefectsNumber 流程优化缺陷数
         */
        if (problemStatisticDO.getProcessDefectsNumber() != 0) {
            SumBos.add("流程优化缺陷数");
            workingHoursBOS.add(problemStatisticDO.getProcessDefectsNumber() + "");
            sumx = sumx + problemStatisticDO.getProcessDefectsNumber();
        }
        /**
         * @Fields promptDefectsNumber 提示语优化缺陷数
         */
        if (problemStatisticDO.getPromptDefectsNumber() != 0) {
            SumBos.add("提示语优化缺陷数");
            workingHoursBOS.add(problemStatisticDO.getPromptDefectsNumber() + "");
            sumx = sumx + problemStatisticDO.getPromptDefectsNumber();
        }
        /**
         * @Fields pageDefectsNumber 页面设计缺陷数
         */
        if (problemStatisticDO.getPageDefectsNumber() != 0) {
            SumBos.add("页面设计缺陷数");
            workingHoursBOS.add(problemStatisticDO.getPageDefectsNumber() + "");
            sumx = sumx + problemStatisticDO.getPageDefectsNumber();
        }
        /**
         * @Fields backgroundDefectsNumber 后台应用缺陷数
         */
        if (problemStatisticDO.getBackgroundDefectsNumber() != 0) {
            SumBos.add("后台应用缺陷数");
            workingHoursBOS.add(problemStatisticDO.getBackgroundDefectsNumber() + "");
            sumx = sumx + problemStatisticDO.getBackgroundDefectsNumber();
        }
        /**
         * @Fields modifyDefectsNumber 修改引入问题缺陷数
         */
        if (problemStatisticDO.getModifyDefectsNumber() != 0) {
            SumBos.add("修改引入问题缺陷数");
            workingHoursBOS.add(problemStatisticDO.getModifyDefectsNumber() + "");
            sumx = sumx + problemStatisticDO.getModifyDefectsNumber();
        }
        /**
         * @Fields designDefectsNumber 技术设计缺陷数
         */
        if (problemStatisticDO.getDesignDefectsNumber() != 0) {
            SumBos.add("技术设计缺陷数");
            workingHoursBOS.add(problemStatisticDO.getDesignDefectsNumber() + "");
            sumx = sumx + problemStatisticDO.getDesignDefectsNumber();
        }
        /**
         * @Fields invalidDefectsNumber 无效问题数
         */
        if (problemStatisticDO.getInvalidDefectsNumber() != 0) {
            SumBos.add("无效问题数");
            workingHoursBOS.add(problemStatisticDO.getInvalidDefectsNumber() + "");
            sumx = sumx + problemStatisticDO.getInvalidDefectsNumber();
        }
        sum = String.valueOf(sumx);
        demandHoursRspBO.setListSum(SumBos);
        demandHoursRspBO.setSum(sum);
        demandHoursRspBO.setStringList(workingHoursBOS);
        return demandHoursRspBO;
    }

    /**
     * 根据epic获取评审问题类型数
     *
     * @param epic
     * @return
     */
    @Override
    public DemandHoursRspBO getReviewNumber(String epic) {
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        ProblemStatisticDO problemStatisticDO = problemStatisticDao.get(epic);
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        String sum = "";
        int sumx = 0;
        if (problemStatisticDO == null || "".equals(problemStatisticDO)) {
            demandHoursRspBO.setSum("0");
            return demandHoursRspBO;
        }
        /**
         * @Fields requirementsReviewNumber 需求评审问题数
         */
        if (problemStatisticDO.getRequirementsReviewNumber() != 0) {
            SumBos.add("需求评审问题数");
            workingHoursBOS.add(problemStatisticDO.getRequirementsReviewNumber() + "");
            sumx = sumx + problemStatisticDO.getRequirementsReviewNumber();
        }
        /**
         * @Fields codeReviewNumber 代码评审问题数
         */
        if (problemStatisticDO.getCodeReviewNumber() != 0) {
            SumBos.add("代码评审问题数");
            workingHoursBOS.add(problemStatisticDO.getCodeReviewNumber() + "");
            sumx = sumx + problemStatisticDO.getCodeReviewNumber();
        }
        /**
         * @Fields testReviewNumber 测试案例评审问题数
         */
        if (problemStatisticDO.getTestReviewNumber() != 0) {
            SumBos.add("测试案例评审问题数");
            workingHoursBOS.add(problemStatisticDO.getTestReviewNumber() + "");
            sumx = sumx + problemStatisticDO.getTestReviewNumber();
        }
        /**
         * @Fields productionReviewNumber 投产方案评审问题数
         */
        if (problemStatisticDO.getProductionReviewNumber() != 0) {
            SumBos.add("投产方案评审问题数");
            workingHoursBOS.add(problemStatisticDO.getProductionReviewNumber() + "");
            sumx = sumx + problemStatisticDO.getProductionReviewNumber();
        }
        /**
         * @Fields technicalReviewNumber 技术方案评审问题数
         */
        if (problemStatisticDO.getTechnicalReviewNumber() != 0) {
            SumBos.add("技术方案评审问题数");
            workingHoursBOS.add(problemStatisticDO.getTechnicalReviewNumber() + "");
            sumx = sumx + problemStatisticDO.getTechnicalReviewNumber();
        }
        sum = String.valueOf(sumx);
        demandHoursRspBO.setListSum(SumBos);
        demandHoursRspBO.setSum(sum);
        demandHoursRspBO.setStringList(workingHoursBOS);
        return demandHoursRspBO;
    }

    /**
     * 毫秒转人天
     *
     * @param time
     * @return
     */
    public Double getWorkHours(int time) {

        return (double) (Math.round((long) time * 100 / 28800) / 100.0);
    }

    /**
     * 毫秒转小时
     *
     * @param time
     * @return
     */
    public Double getWorkHoursTime(int time) {
        return (double) (Math.round((long) time * 100 / 3600) / 100.0);
    }

    /**
     * 获取工作量
     *
     * @param demand
     * @return
     */
    public String getWorkLoad(DemandDO demand) {
        if (com.cmpay.lemon.common.utils.StringUtils.isNotEmpty(demand.getDevpLeadDept()) && com.cmpay.lemon.common.utils.StringUtils.isEmpty(demand.getDevpCoorDept())) {
            demand.setLeadDeptPro(demand.getDevpLeadDept() + ":100%;");
            if (demand.getTotalWorkload() == 0) {
                demand.setLeadDeptWorkload(demand.getDevpLeadDept() + ":0.00;");
            } else {
                demand.setLeadDeptWorkload(demand.getDevpLeadDept() + ":" + String.format("%.2f", Double.valueOf(demand.getTotalWorkload())) + ";");
            }
            //updateReqWorkLoad(demand);
        }
        //本月工作量
        int monInputWorkload = demand.getMonInputWorkload() + demand.getInputWorkload();
        //主导部门本月工作量
        String LeadDeptCurMonWorkLoad = "";
        String lead = demand.getLeadDeptPro();
        String req_sts = demand.getReqSts();
        // && !("30".equals(req_sts)) 去调判断状态为取消
        if (com.cmpay.lemon.common.utils.StringUtils.isNotBlank(lead) && monInputWorkload != 0) {
            String[] leadSplit = lead.replaceAll("%", "").split(":");
            leadSplit[1] = leadSplit[1].replaceAll(";", "");
            LeadDeptCurMonWorkLoad = leadSplit[0] + ":" + String.format("%.2f", (Double.valueOf(leadSplit[1]) / 100) * monInputWorkload) + ";";
        }

        //配合部门本月工作量
        String CoorDevpCurMonWorkLoad = "";
        //配合工作量百分比
        String CoorDevpPer = "";
        String[] coorList = new String[20];
        String coor = demand.getCoorDeptPro();
        // && !("30".equals(req_sts)) 去调判断状态为取消
        if (com.cmpay.lemon.common.utils.StringUtils.isNotBlank(coor) && monInputWorkload != 0) {
            coorList = demand.getCoorDeptPro().split(";");
            for (int i = 0; i < coorList.length; i++) {
                if (com.cmpay.lemon.common.utils.StringUtils.isNotBlank(coorList[i])) {
                    String[] CoorDevpCurMonWorkLoadSplit = coorList[i].split(":");
                    if (com.cmpay.lemon.common.utils.StringUtils.isNotBlank(CoorDevpCurMonWorkLoadSplit[0]) && com.cmpay.lemon.common.utils.StringUtils.isNotBlank(CoorDevpCurMonWorkLoadSplit[1])) {
                        CoorDevpPer = String.format("%.2f", ((Double.valueOf(CoorDevpCurMonWorkLoadSplit[1].replaceAll("%", ""))) / 100) * monInputWorkload);
                        CoorDevpCurMonWorkLoad += CoorDevpCurMonWorkLoadSplit[0] + ":" + CoorDevpPer + ";";
                    }
                }
            }
        }
        DemandHoursRspBO demand1 = new DemandHoursRspBO();
        String str = "";
        str = LeadDeptCurMonWorkLoad + CoorDevpCurMonWorkLoad;
        return str;
    }

    @Override
    public List<WorkingHoursBO> findList(String displayName, String date, String date1, String date2) {
        System.err.println(displayName + "++++++" + date + "++++++" + date1 + "++++++" + date2);
        List<WorkingHoursBO> workingHoursBOS = new LinkedList<>();
        List<WorkingHoursDO> impl = null;
        WorkingHoursDO workingHoursDO = new WorkingHoursDO();
        workingHoursDO.setDisplayname(displayName);
        if (StringUtils.isBlank(date) && StringUtils.isBlank(date1) && StringUtils.isBlank(date2)) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如日期、周、月!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        if (StringUtils.isNotBlank(date) && StringUtils.isBlank(date1) && StringUtils.isBlank(date2)) {
            workingHoursDO.setSelectTime(date);
            impl = iWorkingHoursDao.findList(workingHoursDO);
        }
        if (StringUtils.isNotBlank(date1) && StringUtils.isBlank(date) && StringUtils.isBlank(date2)) {
            workingHoursDO.setSelectTime(date1);
            impl = iWorkingHoursDao.findListWeek(workingHoursDO);
        }
        if (StringUtils.isNotBlank(date2) && StringUtils.isBlank(date) && StringUtils.isBlank(date1)) {
            workingHoursDO.setSelectTime(date2);
            impl = iWorkingHoursDao.findListMonth(workingHoursDO);
        }
        impl.forEach(m ->
                workingHoursBOS.add(BeanUtils.copyPropertiesReturnDest(new WorkingHoursBO(), m))
        );
        return workingHoursBOS;
    }

    @Override
    public List<ReqDataCountBO> getStageByJd(String reqImplMon) {
        List<ReqDataCountBO> reqDataCountBOS = new LinkedList<>();
        reqDataCountDao.getStageByJd(reqImplMon).forEach(m ->
                reqDataCountBOS.add(BeanUtils.copyPropertiesReturnDest(new ReqDataCountBO(), m))
        );
        return reqDataCountBOS;
    }

    @Override
    public List<ReqDataCountBO> selectDetl(ReqMngBO vo) {
        List<ReqDataCountDO> lst = reqDataCountDao.selectDetl(BeanUtils.copyPropertiesReturnDest(new ReqMngDO(), vo));
        String time = DateUtil.date2String(new Date(), "yyyy-MM-dd");
        for (int i = 0; i < lst.size(); i++) {
            String reqAbnorType = lst.get(i).getReqUnusual();
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
            } else if (reqAbnorType.indexOf("01") != -1) {
                lst.get(i).setReqUnusual("正常");
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
                lst.get(i).setReqUnusual(reqAbnorTypeAll);
            } else {
                lst.get(i).setReqUnusual(reqAbnorTypeAll);
            }
        }
        List<ReqDataCountBO> reqDataCountBOS = new LinkedList<>();
        lst.forEach(m ->
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
        if (JudgeUtils.isNotEmpty(reportLista)) {
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
            reportLista.forEach(m -> {
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
    public void downloadDemandUploadDocumentBO(String month, String devpLeadDept, String productMng, String firstLevelOrganization, HttpServletResponse response) {
        List<DemandUploadDocumentBO> DemandTypeStatisticsBOList = new ArrayList<>();
        List<DemandBO> reportLista = new ArrayList<>();
        reportLista = this.getReportForm6(month, devpLeadDept, productMng, firstLevelOrganization);
        if (JudgeUtils.isNotEmpty(reportLista)) {
            reportLista.forEach(m -> {
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
        List<ReqDataCountBO> reportListb = this.getImplByDept(month);
        List<DemandImplementationReportBO> demandImplementationReportBOList = new ArrayList<>();
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
        if (JudgeUtils.isNotEmpty(reportLista)) {
            reportLista.forEach(m ->
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
        List<DemandCompletionReportBO> demandCompletionReportBOList = new ArrayList<>();
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
        List<DemandCompletionReportBO> demandCompletionReportBOList = new ArrayList<>();
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
        List<BaseOwnershipDepartmentStatisticsBO> BaseOwnershipDepartmentStatisticsBOList = new ArrayList<>();
        reportLista = this.getStageByJd(month);
        if (JudgeUtils.isNotEmpty(reportLista)) {
            reportLista.forEach(m -> {
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
        String time = DateUtil.date2String(new Date(), "yyyy-MM-dd");
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
        String date = "";
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return productionBOList;

    }


    private PageInfo<DemandBO> getPageInfo(DemandBO demandBO) {
        DemandDO demandDO = new DemandDO();
        BeanConvertUtils.convert(demandDO, demandBO);
        PageInfo<DemandBO> pageInfo = PageUtils.pageQueryWithCount(demandBO.getPageNum(), demandBO.getPageSize(),
                () -> BeanConvertUtils.convertList(reqDataCountDao.find(demandDO), DemandBO.class));
        return pageInfo;
    }

    @Override
    public List<ProductionVerificationIsNotTimelyBO> listOfUntimelyStatusChanges(String dept,String selectTime1,String selectTime2) {
        List<ProductionVerificationIsNotTimelyBO> listOfUntimelyStatusChangesBos = new LinkedList<>();
        //获得投产验证不及时清单
        List<ProductionDO> productionDOList = new LinkedList<>();
        //获得系统录入验证不及时清单
        List<OperationApplicationDO> operationApplicationDOList = new LinkedList<>();
        if (StringUtils.isBlank(selectTime1) && StringUtils.isBlank(selectTime2)) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如周、月!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        // 查询周
        if (StringUtils.isNotBlank(selectTime1) && StringUtils.isBlank(selectTime2)) {
            productionDOList = operationProductionService.getProductionVerificationIsNotTimely2(selectTime1, dept);
            operationApplicationDOList = operationProductionService.getSystemEntryVerificationIsNotTimelyList2(selectTime1, dept);
        }
        // 查询月
        if (StringUtils.isNotBlank(selectTime2) && StringUtils.isBlank(selectTime1)) {
            productionDOList = operationProductionService.getProductionVerificationIsNotTimely3(selectTime2, dept);
            operationApplicationDOList = operationProductionService.getSystemEntryVerificationIsNotTimelyList3(selectTime2, dept);
        }
        if (JudgeUtils.isEmpty(productionDOList) && JudgeUtils.isEmpty(operationApplicationDOList)) {
            return listOfUntimelyStatusChangesBos;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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
                    listOfUntimelyStatusChangesBos.add(productionVerificationIsNotTimelyBO);
                }
            }
            if (operationApplicationDOList != null && operationApplicationDOList.size() != 0) {
                for (int i = 0; i < operationApplicationDOList.size(); i++) {
                    ProductionVerificationIsNotTimelyBO productionVerificationIsNotTimelyBO = new ProductionVerificationIsNotTimelyBO();
                    productionVerificationIsNotTimelyBO.setProNumber(operationApplicationDOList.get(i).getOperNumber());
                    productionVerificationIsNotTimelyBO.setProNeed(operationApplicationDOList.get(i).getOperRequestContent());
                    productionVerificationIsNotTimelyBO.setProType(operationApplicationDOList.get(i).getSysOperType());
                    productionVerificationIsNotTimelyBO.setValidation("");
                    productionVerificationIsNotTimelyBO.setProDate(sdf.format(operationApplicationDOList.get(i).getProposeDate()));
                    productionVerificationIsNotTimelyBO.setIdentifier(operationApplicationDOList.get(i).getIdentifier());
                    productionVerificationIsNotTimelyBO.setDepartment(operationApplicationDOList.get(i).getApplicationSector());
                    Calendar c1 = Calendar.getInstance();
                    Calendar c2 = Calendar.getInstance();
                    c1.setTime(sdf.parse(sdf.format(new Date())));
                    c2.setTime(sdf.parse(sdf.format(operationApplicationDOList.get(i).getProposeDate())));
                    long day = (sdf.parse(sdf.format(new Date())).getTime() - sdf.parse(sdf.format(operationApplicationDOList.get(i).getProposeDate())).getTime()) / (24 * 60 * 60 * 1000);
                    productionVerificationIsNotTimelyBO.setSumDay(day + "");
                    listOfUntimelyStatusChangesBos.add(productionVerificationIsNotTimelyBO);
                }
            }
        } catch (ParseException e) {
        }
        return listOfUntimelyStatusChangesBos;
    }

    //工时计算
    @Override
    public  List<TestStatisticsRspBO> testStatisticsList(TestStatisticsBO destStatisticsBO) {
        String startTime = "";
        String endTime = "";
        //月计算
        if (JudgeUtils.isBlank(destStatisticsBO.getSelectTime1()) && JudgeUtils.isNotBlank(destStatisticsBO.getSelectTime2())) {

            //获取下个月时间
            SimpleDateFormat simpleDateFormatMonth = new SimpleDateFormat("yyyy-MM");
            try {
                Date month = simpleDateFormatMonth.parse(destStatisticsBO.getSelectTime2());
            Calendar c = Calendar.getInstance();
            c.setTime(month);
            c.add(Calendar.MONTH, 1);
            startTime = destStatisticsBO.getSelectTime2();
            endTime = simpleDateFormatMonth.format(c.getTime());
        } catch (Exception e) {
            //todo  时间格式不对
        }
        }
        //周计算
        else if (JudgeUtils.isBlank(destStatisticsBO.getSelectTime2()) && JudgeUtils.isNotBlank(destStatisticsBO.getSelectTime1())) {
            //获取下个月时间
            SimpleDateFormat simpleDateFormatMonth = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date day = simpleDateFormatMonth.parse(destStatisticsBO.getSelectTime1());
                Calendar c = Calendar.getInstance();
                c.setTime(day);
                c.add(Calendar.DATE, 7);
                startTime = destStatisticsBO.getSelectTime1();
                endTime = simpleDateFormatMonth.format(c.getTime());
            } catch (Exception e) {
                //todo  时间格式不对
            }
        } else {
            //todo  时间格式不对
        }

        WorkingHoursDO workingHoursDO = new WorkingHoursDO();
        workingHoursDO.setStartTime(startTime);
        workingHoursDO.setEndTime(endTime);
        workingHoursDO.setDevpLeadDept("产品测试团队");
        //如果时个人查询
        if (destStatisticsBO.getQueryFlag().equals("personal")) {
            workingHoursDO.setDisplayname(destStatisticsBO.getPersonName());
        }
        //查出登记工时list
        List<WorkingHoursDO> workingHoursList = iWorkingHoursDao.queryByTimeCycle(workingHoursDO);
        //利用maq去重
        HashMap<String, TestStatisticsRspBO> testDataStatisticsMap = new HashMap<>();
        workingHoursList.forEach(m -> {
            if (JudgeUtils.isBlank(m.getEpickey())) {
                return;
            }

            TestStatisticsRspBO testStatisticsRspBO = testDataStatisticsMap.get(m.getEpickey());
            //为空新建，不为空则累计数据
            if (JudgeUtils.isNull(testStatisticsRspBO)) {
                TestStatisticsRspBO testStatisticsRspBO1 = new TestStatisticsRspBO();
                testStatisticsRspBO1.setPeriod(workingHoursDO.getStartTime() + "---" + workingHoursDO.getEndTime());
                DemandJiraDO demandJiraDO = new DemandJiraDO();
                demandJiraDO.setJiraKey(m.getEpickey());
                // 根据jiraKey获取内部编号
                List<DemandJiraDO> demandJiraDos = demandJiraDao.find(demandJiraDO);
                // 根据内部编号获取 需求名及需求编号
                if (JudgeUtils.isNotEmpty(demandJiraDos)) {
                    DemandDO demandDO = demandDao.get(demandJiraDos.get(demandJiraDos.size() - 1).getReqInnerSeq());
                    if(JudgeUtils.isBlank(demandDO.getReqNm())){
                        System.err.println(demandJiraDos.get(demandJiraDos.size() - 1).getReqInnerSeq());
                    }
                    testStatisticsRspBO1.setDemandName(demandDO.getReqNm());
                }else{
                    testStatisticsRspBO1.setDemandName("自建任务："+m.getEpickey());
                }
                testStatisticsRspBO1.setWorkingHours(m.getTimespnet());
                testStatisticsRspBO1.setCaseCompletedNumber(m.getCaseCompletedNumber());
                testStatisticsRspBO1.setCaseExecutionNumber(m.getCaseExecutionNumber());
                testStatisticsRspBO1.setCaseWritingNumber(m.getCaseWritingNumber());
                testDataStatisticsMap.put(m.getEpickey(),testStatisticsRspBO1);
            }else{
                int workingHours = Integer.parseInt(m.getTimespnet());
                int totalWorkingHours = Integer.parseInt(testStatisticsRspBO.getWorkingHours());
                totalWorkingHours=workingHours+totalWorkingHours;
                testStatisticsRspBO.setWorkingHours(String.valueOf(totalWorkingHours));
                testStatisticsRspBO.setCaseCompletedNumber(m.getCaseCompletedNumber()+testStatisticsRspBO.getCaseCompletedNumber());
                testStatisticsRspBO.setCaseExecutionNumber(m.getCaseExecutionNumber()+testStatisticsRspBO.getCaseExecutionNumber());
                testStatisticsRspBO.setCaseWritingNumber(m.getCaseWritingNumber()+testStatisticsRspBO.getCaseWritingNumber());
                testDataStatisticsMap.put(m.getEpickey(),testStatisticsRspBO);
            }
        });
        List<TestStatisticsRspBO> testStatisticsRspBOlist = new LinkedList<>();
        for (Map.Entry<String, TestStatisticsRspBO> entry : testDataStatisticsMap.entrySet()) {
            TestStatisticsRspBO testStatisticsRspBO = entry.getValue();
            String key = entry.getKey();
            //工时计算秒转换为小时
            int parseInt = Integer.parseInt(testStatisticsRspBO.getWorkingHours());
            String WorkingHours = String.format("%.2f", (float) parseInt / (float) 3600) + "小时";
            testStatisticsRspBO.setWorkingHours(WorkingHours);
            DefectDetailsDO defectDetailsDO = new DefectDetailsDO();
            defectDetailsDO.setEndTime(endTime);
            defectDetailsDO.setStartTime(startTime);
            defectDetailsDO.setEpicKey(key);
            if (destStatisticsBO.getQueryFlag().equals("personal")) {
                defectDetailsDO.setDefectRegistrant(destStatisticsBO.getPersonName());
            }
            List<DefectDetailsDO> defectDetailList = defectDetailsExtDao.findByTime(defectDetailsDO);
            if(JudgeUtils.isNotEmpty(defectDetailList)){
                testStatisticsRspBO.setDefectsNumber(defectDetailList.size());
            }

            testStatisticsRspBOlist.add(testStatisticsRspBO);
        }
        return testStatisticsRspBOlist;

    }

    //获得产品线月缺陷率
    @Override
    public  List<ProductLineDefectsBO> productLineDefectRate(String month) {
        //  查找产品线
        DictionaryDO dictionaryDO = new DictionaryDO();
        dictionaryDO.setDicId("PRD_LINE");
        List<DictionaryDO> dictionaryDOList = dictionaryDao.getDicByDicId(dictionaryDO);

        // 产品线只展示最新数据
        dictionaryDOList = dictionaryDOList.subList(0,44);
        LinkedList<ProductLineDefectsBO> productLineDefectsList = new LinkedList();
        for(int i=0;i<dictionaryDOList.size();i++){

            ProductLineDefectsBO productLineDefectsBO = new ProductLineDefectsBO();
            productLineDefectsBO.setProductLine(dictionaryDOList.get(i).getValue());
            //问题数默认为0
            productLineDefectsBO.setDefectsNumber("0");

            DemandDO demandDO1 = new DemandDO();
            demandDO1.setReqImplMon(month);
            demandDO1.setReqPrdLine(dictionaryDOList.get(i).getName());
            List<DemandDO> demandDOS1 = demandDao.find(demandDO1);
            //工作量和缺陷计数器
            int workload =0;
            int defectsNumber=0;
            if(JudgeUtils.isNotEmpty(demandDOS1)){
                for(int j=0;j<demandDOS1.size();j++){
                    //月初阶段 ,
                    int preMonPeriod = 0;
                    if(JudgeUtils.isNotBlank(demandDOS1.get(j).getPreMonPeriod())){
                        preMonPeriod= Integer.parseInt(demandDOS1.get(j).getPreMonPeriod());
                    }
                    //当前阶段
                    int preCurPeriod=0;
                    if(JudgeUtils.isNotBlank(demandDOS1.get(j).getPreMonPeriod())){
                        preCurPeriod = Integer.parseInt(demandDOS1.get(j).getPreCurPeriod());
                    }
                    //月初阶段未到uat测试  当前阶段已经达到完成uat测试的需求 的工作量计算
                    if(preMonPeriod>=140||preCurPeriod<140){
                            continue;
                    }
                    workload=workload+demandDOS1.get(j).getTotalWorkload();
                    //从搜索到的需求中  找到对应的jira编号并搜索该需求所创建的缺陷数
                    DemandJiraDO demandJiraDO = new DemandJiraDO();
                    demandJiraDO.setReqInnerSeq(demandDOS1.get(j).getReqInnerSeq());
                    List<DemandJiraDO> demandJiraDOS = demandJiraDao.find(demandJiraDO);
                    if(JudgeUtils.isEmpty(demandJiraDOS)){
                        continue;
                    }
                    String jiraKey = demandJiraDOS.get(0).getJiraKey();
                    DefectDetailsDO defectDetailsDO = new DefectDetailsDO();
                    defectDetailsDO.setEpicKey(jiraKey);
                    List<DefectDetailsDO> defectDetailsDOList = defectDetailsExtDao.find(defectDetailsDO);
                    defectsNumber=defectsNumber+defectDetailsDOList.size();
                }

            }
            productLineDefectsBO.setDefectsNumber(String.valueOf(defectsNumber));
            productLineDefectsBO.setWorkload(String.valueOf(workload));
            if(workload==0){
                productLineDefectsBO.setDefectRate("0.00%");
            }else{
                String defectRate = String.format("%.2f", (float) Integer.parseInt(productLineDefectsBO.getDefectsNumber()) / (float) Integer.parseInt(productLineDefectsBO.getWorkload()) * 100) + "%";
                productLineDefectsBO.setDefectRate(defectRate);
            }
            //如果工作量或者缺陷数为0则不增加
            if(workload !=0&&defectsNumber!=0){
                productLineDefectsList.add(productLineDefectsBO);
            }
        }
        return productLineDefectsList;
    }

    @Override
    public List<ProductLineDefectsBO> departmentalDefectRate(String month) {
        //  查找部门
        //二级部门集合
        List<OrganizationStructureDO> impl = iOrganizationStructureDao.find(new OrganizationStructureDO());
        LinkedList<ProductLineDefectsBO> productLineDefectsList = new LinkedList();
        // 先将二级部门的数据统计出来，后面再统计一级团队
        for(int i=0;i<impl.size();i++){
//            // 去掉4个项目组和质量监督、产品测试
//            if("质量监督组".equals(impl.get(i).getSecondlevelorganization())||"产品测试团队".equals(impl.get(i).getSecondlevelorganization())||"资金归集项目组".equals(impl.get(i).getSecondlevelorganization())
//                    ||"设计项目组".equals(impl.get(i).getSecondlevelorganization()) ||"团体组织交费项目组".equals(impl.get(i).getSecondlevelorganization()) ||"客服中间层项目组".equals(impl.get(i).getSecondlevelorganization())){
//                continue;
//            }
            ProductLineDefectsBO productLineDefectsBO = new ProductLineDefectsBO();
            productLineDefectsBO.setProductLine(impl.get(i).getSecondlevelorganization());
            //问题数默认为0
            productLineDefectsBO.setDefectsNumber("0");

            // 获取各部门功能点
            DemandHoursRspBO demandHoursRspBO = getDeptWorkHoursAndPoint3(impl.get(i).getSecondlevelorganization(), null, month);
            List<String> listSum = demandHoursRspBO.getListSum();
            // 部门cr功能点
            double functionPoint =  Double.valueOf(listSum.get(1));
            //工作量和缺陷计数器
            int defectsNumber=0;

            // 统计部门的有效plog数
            DefectDetailsDO defectDetailsDO = new DefectDetailsDO();
            defectDetailsDO.setProblemHandlerDepartment(impl.get(i).getSecondlevelorganization());
            defectDetailsDO.setRegistrationDate(month);
            List<DefectDetailsDO> defectDetailsDOList = defectDetailsExtDao.findValidList(defectDetailsDO);
            if(JudgeUtils.isNotEmpty(defectDetailsDOList)){
                defectsNumber = defectDetailsDOList.size();
            }
            productLineDefectsBO.setDefectsNumber(String.valueOf(defectsNumber));
            productLineDefectsBO.setWorkload(String.valueOf(functionPoint));
            productLineDefectsList.add(productLineDefectsBO);
        }
        LinkedList<ProductLineDefectsBO> productLineDefectsList2 = new LinkedList();
        DemandEaseDevelopmentDO easeDevelopmentDO = new DemandEaseDevelopmentDO();
        easeDevelopmentDO.setReqImplMon(month);
        //获取所有的一级团队
        List<String> firstLevelOrganizationList = iOrganizationStructureDao.findFirstLevelOrganization(new OrganizationStructureDO());
        // 获取一级团队支撑工作量汇总数据
        List<DemandEaseDevelopmentDO>  mon_input_workload_list = easeDevelopmentExtDao.easeDevelopmentWorkloadCountForDevp(easeDevelopmentDO);
        for(int i=0;i<firstLevelOrganizationList.size();i++){
            double work = 0;
            int defectsNumber = 0;
            ProductLineDefectsBO productLineDefectsBO2 = new ProductLineDefectsBO();
            // 一级团队
            productLineDefectsBO2.setProductLine(firstLevelOrganizationList.get(i));
            productLineDefectsBO2.setWorkload(0+"");
            // 判断一级团队是否存在支撑工作量
            for(int j=0;j<mon_input_workload_list.size();j++){
                if(firstLevelOrganizationList.get(i).equals(mon_input_workload_list.get(j).getFirstLevelOrganization())){
                    productLineDefectsBO2.setWorkload(mon_input_workload_list.get(j).getDevelopmentworkload());
                }
            }
            for(int j=0;j<productLineDefectsList.size();j++) {
                if ("质量监督组".equals(firstLevelOrganizationList.get(i))) {
                    if ("质量监督组".equals(productLineDefectsList.get(j).getProductLine())) {
                        work = Double.valueOf(productLineDefectsBO2.getWorkload()) + Double.valueOf(productLineDefectsList.get(j).getWorkload());
                        productLineDefectsBO2.setWorkload(work+"");
                        productLineDefectsBO2.setDefectsNumber(productLineDefectsList.get(j).getDefectsNumber());
                    }

                }
                if ("平台架构研发团队".equals(firstLevelOrganizationList.get(i))) {
                    if ("平台架构研发团队".equals(productLineDefectsList.get(j).getProductLine())) {
                        work = Double.valueOf(productLineDefectsBO2.getWorkload()) + Double.valueOf(productLineDefectsList.get(j).getWorkload());
                        productLineDefectsBO2.setWorkload(work+"");
                        productLineDefectsBO2.setDefectsNumber(productLineDefectsList.get(j).getDefectsNumber());
                    }
                }
                if ("产品测试团队".equals(firstLevelOrganizationList.get(i))) {
                    if ("产品测试团队".equals(productLineDefectsList.get(j).getProductLine())) {
                        work = Double.valueOf(productLineDefectsBO2.getWorkload()) + Double.valueOf(productLineDefectsList.get(j).getWorkload());
                        productLineDefectsBO2.setWorkload(work+"");
                        productLineDefectsBO2.setDefectsNumber(productLineDefectsList.get(j).getDefectsNumber());
                    }
                }
                if ("前端技术研发团队".equals(firstLevelOrganizationList.get(i))) {
                    if ("前端技术研发团队".equals(productLineDefectsList.get(j).getProductLine())) {
                        work = Double.valueOf(productLineDefectsBO2.getWorkload()) + Double.valueOf(productLineDefectsList.get(j).getWorkload());
                        productLineDefectsBO2.setWorkload(work+"");
                        productLineDefectsBO2.setDefectsNumber(productLineDefectsList.get(j).getDefectsNumber());
                    }
                }
                if ("金融业务研发团队".equals(firstLevelOrganizationList.get(i))) {
                    if ("信用购机研发组".equals(productLineDefectsList.get(j).getProductLine()) || "号码借研发组".equals(productLineDefectsList.get(j).getProductLine())) {
                        work = Double.valueOf(productLineDefectsBO2.getWorkload()) + Double.valueOf(productLineDefectsList.get(j).getWorkload());
                        defectsNumber = defectsNumber + Integer.parseInt(productLineDefectsList.get(j).getDefectsNumber());
                        productLineDefectsBO2.setWorkload(work+"");
                        productLineDefectsBO2.setDefectsNumber(defectsNumber+"");
                    }
                }
                if ("客户业务研发团队".equals(firstLevelOrganizationList.get(i))) {
                    if ("营销活动研发组".equals(productLineDefectsList.get(j).getProductLine()) || "渠道产品研发组".equals(productLineDefectsList.get(j).getProductLine())) {
                        work = Double.valueOf(productLineDefectsBO2.getWorkload()) + Double.valueOf(productLineDefectsList.get(j).getWorkload());
                        defectsNumber = defectsNumber + Integer.parseInt(productLineDefectsList.get(j).getDefectsNumber());
                        productLineDefectsBO2.setWorkload(work+"");
                        productLineDefectsBO2.setDefectsNumber(defectsNumber+"");
                    }
                }
                if ("支付业务研发团队".equals(firstLevelOrganizationList.get(i))) {
                    if ("聚合支付研发组".equals(productLineDefectsList.get(j).getProductLine()) || "话费充值研发组".equals(productLineDefectsList.get(j).getProductLine())) {
                        work = Double.valueOf(productLineDefectsBO2.getWorkload()) + Double.valueOf(productLineDefectsList.get(j).getWorkload());
                        defectsNumber = defectsNumber + Integer.parseInt(productLineDefectsList.get(j).getDefectsNumber());
                        productLineDefectsBO2.setWorkload(work+"");
                        productLineDefectsBO2.setDefectsNumber(defectsNumber+"");
                    }
                }
                if ("商户业务研发团队".equals(firstLevelOrganizationList.get(i))) {
                    if ("商户业务研发团队".equals(productLineDefectsList.get(j).getProductLine())) {
                        work = Double.valueOf(productLineDefectsBO2.getWorkload()) + Double.valueOf(productLineDefectsList.get(j).getWorkload());
                        productLineDefectsBO2.setWorkload(work+"");
                        productLineDefectsBO2.setDefectsNumber(productLineDefectsList.get(j).getDefectsNumber());
                    }
                }
                if ("智慧食堂研发团队".equals(firstLevelOrganizationList.get(i))) {
                    if ("智慧食堂研发团队".equals(productLineDefectsList.get(j).getProductLine())) {
                        work = Double.valueOf(productLineDefectsBO2.getWorkload()) + Double.valueOf(productLineDefectsList.get(j).getWorkload());
                        productLineDefectsBO2.setWorkload(work+"");
                        productLineDefectsBO2.setDefectsNumber(productLineDefectsList.get(j).getDefectsNumber());
                    }
                }
                if ("业务中台研发团队".equals(firstLevelOrganizationList.get(i))) {
                    if ("银行&公共中心研发组".equals(productLineDefectsList.get(j).getProductLine()) || "用户&清算&账务研发组".equals(productLineDefectsList.get(j).getProductLine())
                            || "支付研发组".equals(productLineDefectsList.get(j).getProductLine())|| "营销研发组".equals(productLineDefectsList.get(j).getProductLine()) ) {
                        work = Double.valueOf(productLineDefectsBO2.getWorkload()) + Double.valueOf(productLineDefectsList.get(j).getWorkload());
                        defectsNumber = defectsNumber + Integer.parseInt(productLineDefectsList.get(j).getDefectsNumber());
                        productLineDefectsBO2.setWorkload(work+"");
                        productLineDefectsBO2.setDefectsNumber(defectsNumber+"");
                    }
                }
                if ("资金归集项目组".equals(firstLevelOrganizationList.get(i))) {
                    if ("资金归集项目组".equals(productLineDefectsList.get(j).getProductLine())) {
                        work = Double.valueOf(productLineDefectsBO2.getWorkload()) + Double.valueOf(productLineDefectsList.get(j).getWorkload());
                        productLineDefectsBO2.setWorkload(work+"");
                        productLineDefectsBO2.setDefectsNumber(productLineDefectsList.get(j).getDefectsNumber());
                    }
                }
                if ("设计项目组".equals(firstLevelOrganizationList.get(i))) {
                    if ("设计项目组".equals(productLineDefectsList.get(j).getProductLine())) {
                        work = Double.valueOf(productLineDefectsBO2.getWorkload()) + Double.valueOf(productLineDefectsList.get(j).getWorkload());
                        productLineDefectsBO2.setWorkload(work+"");
                        productLineDefectsBO2.setDefectsNumber(productLineDefectsList.get(j).getDefectsNumber());
                    }
                }
                if ("团体组织交费项目组".equals(firstLevelOrganizationList.get(i))) {
                    if ("团体组织交费项目组".equals(productLineDefectsList.get(j).getProductLine())) {
                        work = Double.valueOf(productLineDefectsBO2.getWorkload()) + Double.valueOf(productLineDefectsList.get(j).getWorkload());
                        productLineDefectsBO2.setWorkload(work+"");
                        productLineDefectsBO2.setDefectsNumber(productLineDefectsList.get(j).getDefectsNumber());
                    }
                }
                if ("客服中间层项目组".equals(firstLevelOrganizationList.get(i))) {
                    if ("客服中间层项目组".equals(productLineDefectsList.get(j).getProductLine())) {
                        work = Double.valueOf(productLineDefectsBO2.getWorkload()) + Double.valueOf(productLineDefectsList.get(j).getWorkload());
                        productLineDefectsBO2.setWorkload(work+"");
                        productLineDefectsBO2.setDefectsNumber(productLineDefectsList.get(j).getDefectsNumber());
                    }
                }
            }
            if(work==0){
                productLineDefectsBO2.setDefectRate("0.00%");
            }else{
                String defectRate = String.format("%.2f", (float) Integer.parseInt(productLineDefectsBO2.getDefectsNumber()) /  Double.valueOf(productLineDefectsBO2.getWorkload()) * 100) + "%";
                productLineDefectsBO2.setDefectRate(defectRate);
            }
            System.err.println(productLineDefectsBO2);
            productLineDefectsList2.add(productLineDefectsBO2);
        }
        return productLineDefectsList2;
    }

    @Override
    public List<DemandTestStatusBO> demandTestStatusList() {
        //获取当月时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        String month = simpleDateFormat.format(new Date());
        DemandDO demandDO1 = new DemandDO();
        demandDO1.setReqImplMon(month);
        List<DemandTestStatusBO> demandTestStatusBOList = new LinkedList<>();
        //查询当月正在执行的需求（即不为需求提出，取消，暂停，删除）
        List<DemandDO> demandDOS1 = demandDao.QueryIsExecutingDemand(demandDO1);
        if(JudgeUtils.isNotEmpty(demandDOS1)){
            demandDOS1.forEach(m->{
                //月初阶段
                int preMonPeriod = 0;
                if(JudgeUtils.isNotBlank(m.getPreMonPeriod())){
                    preMonPeriod= Integer.parseInt(m.getPreMonPeriod());
                }
                //若月初状态已经为测试完成 ，及上月已经测试完成则不展示
                if(preMonPeriod>=140){
                    return;
                }
                DemandTestStatusBO demandTestStatusBO = new DemandTestStatusBO();
                demandTestStatusBO.setReqNm(m.getReqNm());
                demandTestStatusBO.setReqNo(m.getReqNo());
                //从搜索到的需求中  找到对应的jira编号并搜索该需求所创建的缺陷数
                DemandJiraDO demandJiraDO = new DemandJiraDO();
                demandJiraDO.setReqInnerSeq(m.getReqInnerSeq());
                List<DemandJiraDO> demandJiraDOS = demandJiraDao.find(demandJiraDO);

                if(JudgeUtils.isEmpty(demandJiraDOS)){
                    return;
                }
                String jiraKey = demandJiraDOS.get(0).getJiraKey();
                DefectDetailsDO defectDetailsDO = new DefectDetailsDO();
                defectDetailsDO.setEpicKey(jiraKey);
                //依据jirakey查找对应缺陷
                List<DefectDetailsDO> defectDetailsDOList = defectDetailsExtDao.find(defectDetailsDO);
                //缺陷数
                demandTestStatusBO.setDefectsNumber(defectDetailsDOList.size());
                //缺陷率  缺陷数/工作量
                String defectRate = String.format("%.2f", (float) demandTestStatusBO.getDefectsNumber() / (float) m.getTotalWorkload() * 100) + "%";
                demandTestStatusBO.setDefectRate(defectRate);
                JiraBasicInfoDO jiraBasicInfoDO = jiraBasicInfoDao.get(jiraKey);
                //当日新录入 暂未定时拉去存入数据库的需求 暂不统计
               if(JudgeUtils.isNull(jiraBasicInfoDO)){
                   return;
               }
                //测试案例总数
                if (JudgeUtils.isBlank(jiraBasicInfoDO.getTestCaseNumber())){
                    demandTestStatusBO.setTestCaseNumber("0");
                }else{
                    demandTestStatusBO.setTestCaseNumber(jiraBasicInfoDO.getTestCaseNumber());
                }

                WorkingHoursDO workingHoursDO = new WorkingHoursDO();
                workingHoursDO.setRoletype("测试人员");
                workingHoursDO.setEpickey(jiraKey);
                List<WorkingHoursDO> workingHoursList = iWorkingHoursDao.find(workingHoursDO);
                if(JudgeUtils.isNotEmpty(workingHoursList)){
                    for(int i=0;i<workingHoursList.size();i++){
                        //累加计算测试完成案例及测试执行案例
                        demandTestStatusBO.setCaseCompletedNumber(workingHoursList.get(i).getCaseCompletedNumber()+demandTestStatusBO.getCaseCompletedNumber());
                        demandTestStatusBO.setCaseExecutionNumber(workingHoursList.get(i).getCaseExecutionNumber()+demandTestStatusBO.getCaseExecutionNumber());
                    }
                }
                //缺陷发现率
                if(demandTestStatusBO.getCaseCompletedNumber()!=0) {
                    String defectDiscoveryRate = String.format("%.2f", (float) demandTestStatusBO.getDefectsNumber() / (float) demandTestStatusBO.getCaseCompletedNumber() * 100) + "%";
                    demandTestStatusBO.setDefectDiscoveryRate(defectDiscoveryRate);
                }else{
                    demandTestStatusBO.setDefectDiscoveryRate("NaN%");
                }
                //测试进度
                System.err.println(demandTestStatusBO.getTestCaseNumber());
                int intValue = Double.valueOf(demandTestStatusBO.getTestCaseNumber()).intValue();
                if(intValue!=0) {
                    String testProgress = String.format("%.2f", (float) demandTestStatusBO.getCaseCompletedNumber() / (float) intValue * 100) + "%";
                    demandTestStatusBO.setTestProgress(testProgress);
                }else{
                    demandTestStatusBO.setTestProgress("NaN%");
                }

                demandTestStatusBOList.add(demandTestStatusBO);
            });
        }
        return demandTestStatusBOList;
    }

    @Override
    public List<DemandTestStatusBO> demandTestStatusList2() {
        //获取当月时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        String month = simpleDateFormat.format(new Date());
        DemandDO demandDO1 = new DemandDO();
        demandDO1.setReqImplMon(month);
        List<DemandTestStatusBO> demandTestStatusBOList = new LinkedList<>();
        //查询当月正在执行的需求
        List<DemandDO> demandDOS1 = demandDao.QueryIsExecutingDemand2(demandDO1);
        if(JudgeUtils.isNotEmpty(demandDOS1)){
            demandDOS1.forEach(m->{
                DemandTestStatusBO demandTestStatusBO = new DemandTestStatusBO();
                demandTestStatusBO.setReqNm(m.getReqNm());
                demandTestStatusBO.setReqNo(m.getReqNo());
                //从搜索到的需求中  找到对应的jira编号并搜索该需求所创建的缺陷数
                DemandJiraDO demandJiraDO = new DemandJiraDO();
                demandJiraDO.setReqInnerSeq(m.getReqInnerSeq());
                List<DemandJiraDO> demandJiraDOS = demandJiraDao.find(demandJiraDO);

                if(JudgeUtils.isEmpty(demandJiraDOS)){
                    return;
                }
                String jiraKey = demandJiraDOS.get(0).getJiraKey();
                DefectDetailsDO defectDetailsDO = new DefectDetailsDO();
                defectDetailsDO.setEpicKey(jiraKey);
                //依据jirakey查找对应缺陷
                List<DefectDetailsDO> defectDetailsDOList = defectDetailsExtDao.find(defectDetailsDO);
                //缺陷数
                demandTestStatusBO.setDefectsNumber(defectDetailsDOList.size());
                //缺陷率  缺陷数/工作量
                String defectRate = String.format("%.2f", (float) demandTestStatusBO.getDefectsNumber() / (float) m.getTotalWorkload() * 100) + "%";
                demandTestStatusBO.setDefectRate(defectRate);
                JiraBasicInfoDO jiraBasicInfoDO = jiraBasicInfoDao.get(jiraKey);
                //当日新录入 暂未定时拉去存入数据库的需求 暂不统计
                if(JudgeUtils.isNull(jiraBasicInfoDO)){
                    return;
                }
                //测试案例总数
                if (JudgeUtils.isBlank(jiraBasicInfoDO.getTestCaseNumber())){
                    demandTestStatusBO.setTestCaseNumber("0");
                }else{
                    demandTestStatusBO.setTestCaseNumber(jiraBasicInfoDO.getTestCaseNumber());
                }

                WorkingHoursDO workingHoursDO = new WorkingHoursDO();
                workingHoursDO.setRoletype("测试人员");
                workingHoursDO.setEpickey(jiraKey);
                List<WorkingHoursDO> workingHoursList = iWorkingHoursDao.find(workingHoursDO);
                if(JudgeUtils.isNotEmpty(workingHoursList)){
                    for(int i=0;i<workingHoursList.size();i++){
                        //累加计算测试完成案例及测试执行案例
                        demandTestStatusBO.setCaseCompletedNumber(workingHoursList.get(i).getCaseCompletedNumber()+demandTestStatusBO.getCaseCompletedNumber());
                        demandTestStatusBO.setCaseExecutionNumber(workingHoursList.get(i).getCaseExecutionNumber()+demandTestStatusBO.getCaseExecutionNumber());
                    }
                }
                //缺陷发现率
                if(demandTestStatusBO.getCaseCompletedNumber()!=0) {
                    String defectDiscoveryRate = String.format("%.2f", (float) demandTestStatusBO.getDefectsNumber() / (float) demandTestStatusBO.getCaseCompletedNumber() * 100) + "%";
                    demandTestStatusBO.setDefectDiscoveryRate(defectDiscoveryRate);
                }else{
                    demandTestStatusBO.setDefectDiscoveryRate("NaN%");
                }
                //测试进度
                System.err.println(demandTestStatusBO.getTestCaseNumber());
                int intValue = Double.valueOf(demandTestStatusBO.getTestCaseNumber()).intValue();
                if(intValue!=0) {
                    String testProgress = String.format("%.2f", (float) demandTestStatusBO.getCaseCompletedNumber() / (float) intValue * 100) + "%";
                    demandTestStatusBO.setTestProgress(testProgress);
                }else{
                    demandTestStatusBO.setTestProgress("NaN%");
                }

                demandTestStatusBOList.add(demandTestStatusBO);
            });
        }
        return demandTestStatusBOList;
    }

    @Override
    public List<DefectProcesStatusBO> defectProcesStatus() {
        DefectDetailsDO defectDetailsDO = new DefectDetailsDO();
        List<DefectDetailsDO> defectDetailsDOList = defectDetailsExtDao.findUnfinishedDefects(defectDetailsDO);
        HashMap<String, DefectProcesStatusBO> defectProcesStatusMap = new HashMap<>();
        defectDetailsDOList.forEach(m->{
            DefectProcesStatusBO defectProcesStatusBO = defectProcesStatusMap.get(m.getDefectsDepartment());
            if(JudgeUtils.isNotNull(defectProcesStatusBO)){
                if(m.getDefectStatus().equals("处理中")){
                    defectProcesStatusBO.setProcess(defectProcesStatusBO.getProcess()+1);
                }else if(m.getDefectStatus().equals("待处理")){
                    defectProcesStatusBO.setPending(defectProcesStatusBO.getPending()+1);
                }else if(m.getDefectStatus().equals("待更新")){
                    defectProcesStatusBO.setPendingUpgrade(defectProcesStatusBO.getPendingUpgrade()+1);
                }else if(m.getDefectStatus().equals("待重测")){
                    defectProcesStatusBO.setWithRetest(defectProcesStatusBO.getWithRetest()+1);
                }else if(m.getDefectStatus().equals("问题冻结")){
                    defectProcesStatusBO.setProblemFreeze(defectProcesStatusBO.getProblemFreeze()+1);
                }
                defectProcesStatusMap.put(m.getDefectsDepartment(),defectProcesStatusBO);
            }else{
                defectProcesStatusBO=new DefectProcesStatusBO();
                if(m.getDefectStatus().equals("处理中")){
                    defectProcesStatusBO.setProcess(defectProcesStatusBO.getProcess()+1);
                }else if(m.getDefectStatus().equals("待处理")){
                    defectProcesStatusBO.setPending(defectProcesStatusBO.getPending()+1);
                }else if(m.getDefectStatus().equals("待更新")){
                    defectProcesStatusBO.setPendingUpgrade(defectProcesStatusBO.getPendingUpgrade()+1);
                }else if(m.getDefectStatus().equals("待重测")){
                    defectProcesStatusBO.setWithRetest(defectProcesStatusBO.getWithRetest()+1);
                }else if(m.getDefectStatus().equals("问题冻结")){
                    defectProcesStatusBO.setProblemFreeze(defectProcesStatusBO.getProblemFreeze()+1);
                }
                defectProcesStatusMap.put(m.getDefectsDepartment(),defectProcesStatusBO);
            }
        });
        List<DefectProcesStatusBO> DefectProcesStatusBOList = new LinkedList<>();

        for (Map.Entry<String, DefectProcesStatusBO> entry : defectProcesStatusMap.entrySet()) {
            DefectProcesStatusBO defectProcesStatusBO = entry.getValue();
            defectProcesStatusBO.setDepartment(entry.getKey());
            DefectProcesStatusBOList.add(defectProcesStatusBO);
        }
        return  DefectProcesStatusBOList;
    }

    @Override
    public OnlineLeakageRateBO getOnlineLeakageRate(String month) {

        SimpleDateFormat simpleDateFormatMonth = new SimpleDateFormat("yyyy-MM");
        String[] months = new String[6];
        months[5]=month;
         for(int i=months.length-2;i>=0;i--){
             //获取下个月时间
             try {
                 Date month1 = simpleDateFormatMonth.parse(month);
                 Calendar c = Calendar.getInstance();
                 c.setTime(month1);
                 c.add(Calendar.MONTH, i-5);
                 String month2 = simpleDateFormatMonth.format(c.getTime());
                 months[i]=month2;
             } catch (Exception e) {
                 //todo  时间格式不对
             }
         }
        String[] leakageRate = new String[6];
      for(int i=0;i<months.length;i++){
          ProductionDefectsDO productionDefectsDO = new ProductionDefectsDO();
          productionDefectsDO.setProcessstartdate(months[i]);
          List<ProductionDefectsDO> list = productionDefectsExtDao.findMonthList(productionDefectsDO);
          DefectDetailsDO defectDetailsDO = new DefectDetailsDO();
          defectDetailsDO.setRegistrationDate(months[i]);
          List<DefectDetailsDO> list1 = defectDetailsExtDao.findList(defectDetailsDO);
          if(list.size()==0||list1.size()==0){
              leakageRate[i]="0";
          }else{
              String rate = String.format("%.2f", (float) list.size() / (float) (list1.size() + list.size()) * 100);
              leakageRate[i]=rate;
          }

      }
        OnlineLeakageRateBO onlineLeakageRateBO = new OnlineLeakageRateBO();

        onlineLeakageRateBO.setMonths(months);
        onlineLeakageRateBO.setLeakageRate(leakageRate);
    return onlineLeakageRateBO;

    }

    @Override
    public OnlineLeakageRateBO getDeptDefectRate(String devpLeadDept, String month) {
        //获得6个月周期的月份
        SimpleDateFormat simpleDateFormatMonth = new SimpleDateFormat("yyyy-MM");
        String[] months = new String[6];
        months[5]=month;
        for(int i=months.length-2;i>=0;i--){
            //获取下个月时间
            try {
                Date month1 = simpleDateFormatMonth.parse(month);
                Calendar c = Calendar.getInstance();
                c.setTime(month1);
                c.add(Calendar.MONTH, i-5);
                String month2 = simpleDateFormatMonth.format(c.getTime());
                months[i]=month2;
            } catch (Exception e) {
                //todo  时间格式不对
            }
        }
        String[] leakageRate = new String[6];
        for(int i=0;i<months.length;i++){
            //按照月份，部门查找需求
            DemandDO demandDO1 = new DemandDO();
            demandDO1.setReqImplMon(months[i]);
            demandDO1.setDevpLeadDept(devpLeadDept);
            List<DemandDO> demandDOS = demandDao.find(demandDO1);
            int workload =0;
            int defectsNumber=0;
            if(JudgeUtils.isNotEmpty(demandDOS)){
                for(int j=0;j<demandDOS.size();j++){
                    //月初阶段 ,
                    int preMonPeriod = 0;
                    if(JudgeUtils.isNotBlank(demandDOS.get(j).getPreMonPeriod())){
                        preMonPeriod= Integer.parseInt(demandDOS.get(j).getPreMonPeriod());
                    }
                    //当前阶段
                    int preCurPeriod=0;
                    if(JudgeUtils.isNotBlank(demandDOS.get(j).getPreMonPeriod())){
                        preCurPeriod = Integer.parseInt(demandDOS.get(j).getPreCurPeriod());
                    }
                    //月初阶段未到uat测试  当前阶段已经达到完成uat测试的需求 的工作量计算
                    if(preMonPeriod>=140||preCurPeriod<140){
                        continue;
                    }
                    workload=workload+demandDOS.get(j).getTotalWorkload();
                    //从搜索到的需求中  找到对应的jira编号并搜索该需求所创建的缺陷数
                    DemandJiraDO demandJiraDO = new DemandJiraDO();
                    demandJiraDO.setReqInnerSeq(demandDOS.get(j).getReqInnerSeq());
                    List<DemandJiraDO> demandJiraDOS = demandJiraDao.find(demandJiraDO);
                    if(JudgeUtils.isEmpty(demandJiraDOS)){
                        continue;
                    }
                    String jiraKey = demandJiraDOS.get(0).getJiraKey();
                    DefectDetailsDO defectDetailsDO = new DefectDetailsDO();
                    defectDetailsDO.setEpicKey(jiraKey);
                    List<DefectDetailsDO> defectDetailsDOList = defectDetailsExtDao.find(defectDetailsDO);
                    //缺陷数增加
                    defectsNumber=defectsNumber+defectDetailsDOList.size();
                }
            }
            if(workload==0||defectsNumber==0){
                leakageRate[i]="0";
            }else{
                //计算百分比并保留两位小数
                String rate = String.format("%.2f", (float) defectsNumber / (float)workload  * 100);
                leakageRate[i]=rate;
            }
        }
        OnlineLeakageRateBO onlineLeakageRateBO = new OnlineLeakageRateBO();
        onlineLeakageRateBO.setMonths(months);
        onlineLeakageRateBO.setLeakageRate(leakageRate);
        return onlineLeakageRateBO;
    }
    @Override
    public void downloadReportForm7(String devpLeadDept,String date,String date1,String date2, HttpServletResponse response){
        List<WorkloadMonthBO> workloadMonthBOArrayList = new ArrayList<>();
        List<WorkingHoursBO> reportLista = new ArrayList<>();
        reportLista = this.getReportForm7(devpLeadDept,date,date1,date2);
        if (JudgeUtils.isNotEmpty(reportLista)) {
            reportLista.forEach(m -> {
                WorkloadMonthBO workloadMonthBO = new WorkloadMonthBO();
                // 姓名喝汇总
                workloadMonthBO.setDisplayname(m.getDisplayname());
                workloadMonthBO.setSumTime(m.getSumTime());
                // 判断m.getListDay().size()的大小
                if(m.getListDay().size() == 31){
                    workloadMonthBO.setA1(m.getListDay().get(0));
                    workloadMonthBO.setA2(m.getListDay().get(1));
                    workloadMonthBO.setA3(m.getListDay().get(2));
                    workloadMonthBO.setA4(m.getListDay().get(3));
                    workloadMonthBO.setA5(m.getListDay().get(4));
                    workloadMonthBO.setA6(m.getListDay().get(5));
                    workloadMonthBO.setA7(m.getListDay().get(6));
                    workloadMonthBO.setA8(m.getListDay().get(7));
                    workloadMonthBO.setA9(m.getListDay().get(8));
                    workloadMonthBO.setA10(m.getListDay().get(9));
                    workloadMonthBO.setA11(m.getListDay().get(10));
                    workloadMonthBO.setA12(m.getListDay().get(11));
                    workloadMonthBO.setA13(m.getListDay().get(12));
                    workloadMonthBO.setA14(m.getListDay().get(13));
                    workloadMonthBO.setA15(m.getListDay().get(14));
                    workloadMonthBO.setA16(m.getListDay().get(15));
                    workloadMonthBO.setA17(m.getListDay().get(16));
                    workloadMonthBO.setA18(m.getListDay().get(17));
                    workloadMonthBO.setA19(m.getListDay().get(18));
                    workloadMonthBO.setA20(m.getListDay().get(19));
                    workloadMonthBO.setA21(m.getListDay().get(20));
                    workloadMonthBO.setA22(m.getListDay().get(21));
                    workloadMonthBO.setA23(m.getListDay().get(22));
                    workloadMonthBO.setA24(m.getListDay().get(23));
                    workloadMonthBO.setA25(m.getListDay().get(24));
                    workloadMonthBO.setA26(m.getListDay().get(25));
                    workloadMonthBO.setA27(m.getListDay().get(26));
                    workloadMonthBO.setA28(m.getListDay().get(27));
                    workloadMonthBO.setA29(m.getListDay().get(28));
                    workloadMonthBO.setA30(m.getListDay().get(29));
                    workloadMonthBO.setA31(m.getListDay().get(30));

                }
                // 判断m.getListDay().size()的大小
                if(m.getListDay().size() == 30){
                    workloadMonthBO.setA1(m.getListDay().get(0));
                    workloadMonthBO.setA2(m.getListDay().get(1));
                    workloadMonthBO.setA3(m.getListDay().get(2));
                    workloadMonthBO.setA4(m.getListDay().get(3));
                    workloadMonthBO.setA5(m.getListDay().get(4));
                    workloadMonthBO.setA6(m.getListDay().get(5));
                    workloadMonthBO.setA7(m.getListDay().get(6));
                    workloadMonthBO.setA8(m.getListDay().get(7));
                    workloadMonthBO.setA9(m.getListDay().get(8));
                    workloadMonthBO.setA10(m.getListDay().get(9));
                    workloadMonthBO.setA11(m.getListDay().get(10));
                    workloadMonthBO.setA12(m.getListDay().get(11));
                    workloadMonthBO.setA13(m.getListDay().get(12));
                    workloadMonthBO.setA14(m.getListDay().get(13));
                    workloadMonthBO.setA15(m.getListDay().get(14));
                    workloadMonthBO.setA16(m.getListDay().get(15));
                    workloadMonthBO.setA17(m.getListDay().get(16));
                    workloadMonthBO.setA18(m.getListDay().get(17));
                    workloadMonthBO.setA19(m.getListDay().get(18));
                    workloadMonthBO.setA20(m.getListDay().get(19));
                    workloadMonthBO.setA21(m.getListDay().get(20));
                    workloadMonthBO.setA22(m.getListDay().get(21));
                    workloadMonthBO.setA23(m.getListDay().get(22));
                    workloadMonthBO.setA24(m.getListDay().get(23));
                    workloadMonthBO.setA25(m.getListDay().get(24));
                    workloadMonthBO.setA26(m.getListDay().get(25));
                    workloadMonthBO.setA27(m.getListDay().get(26));
                    workloadMonthBO.setA28(m.getListDay().get(27));
                    workloadMonthBO.setA29(m.getListDay().get(28));
                    workloadMonthBO.setA30(m.getListDay().get(29));
                }
                // 判断m.getListDay().size()的大小
                if(m.getListDay().size() == 29){
                    workloadMonthBO.setA1(m.getListDay().get(0));
                    workloadMonthBO.setA2(m.getListDay().get(1));
                    workloadMonthBO.setA3(m.getListDay().get(2));
                    workloadMonthBO.setA4(m.getListDay().get(3));
                    workloadMonthBO.setA5(m.getListDay().get(4));
                    workloadMonthBO.setA6(m.getListDay().get(5));
                    workloadMonthBO.setA7(m.getListDay().get(6));
                    workloadMonthBO.setA8(m.getListDay().get(7));
                    workloadMonthBO.setA9(m.getListDay().get(8));
                    workloadMonthBO.setA10(m.getListDay().get(9));
                    workloadMonthBO.setA11(m.getListDay().get(10));
                    workloadMonthBO.setA12(m.getListDay().get(11));
                    workloadMonthBO.setA13(m.getListDay().get(12));
                    workloadMonthBO.setA14(m.getListDay().get(13));
                    workloadMonthBO.setA15(m.getListDay().get(14));
                    workloadMonthBO.setA16(m.getListDay().get(15));
                    workloadMonthBO.setA17(m.getListDay().get(16));
                    workloadMonthBO.setA18(m.getListDay().get(17));
                    workloadMonthBO.setA19(m.getListDay().get(18));
                    workloadMonthBO.setA20(m.getListDay().get(19));
                    workloadMonthBO.setA21(m.getListDay().get(20));
                    workloadMonthBO.setA22(m.getListDay().get(21));
                    workloadMonthBO.setA23(m.getListDay().get(22));
                    workloadMonthBO.setA24(m.getListDay().get(23));
                    workloadMonthBO.setA25(m.getListDay().get(24));
                    workloadMonthBO.setA26(m.getListDay().get(25));
                    workloadMonthBO.setA27(m.getListDay().get(26));
                    workloadMonthBO.setA28(m.getListDay().get(27));
                    workloadMonthBO.setA29(m.getListDay().get(28));
                }
                // 判断m.getListDay().size()的大小
                if(m.getListDay().size() == 28){
                    workloadMonthBO.setA1(m.getListDay().get(0));
                    workloadMonthBO.setA2(m.getListDay().get(1));
                    workloadMonthBO.setA3(m.getListDay().get(2));
                    workloadMonthBO.setA4(m.getListDay().get(3));
                    workloadMonthBO.setA5(m.getListDay().get(4));
                    workloadMonthBO.setA6(m.getListDay().get(5));
                    workloadMonthBO.setA7(m.getListDay().get(6));
                    workloadMonthBO.setA8(m.getListDay().get(7));
                    workloadMonthBO.setA9(m.getListDay().get(8));
                    workloadMonthBO.setA10(m.getListDay().get(9));
                    workloadMonthBO.setA11(m.getListDay().get(10));
                    workloadMonthBO.setA12(m.getListDay().get(11));
                    workloadMonthBO.setA13(m.getListDay().get(12));
                    workloadMonthBO.setA14(m.getListDay().get(13));
                    workloadMonthBO.setA15(m.getListDay().get(14));
                    workloadMonthBO.setA16(m.getListDay().get(15));
                    workloadMonthBO.setA17(m.getListDay().get(16));
                    workloadMonthBO.setA18(m.getListDay().get(17));
                    workloadMonthBO.setA19(m.getListDay().get(18));
                    workloadMonthBO.setA20(m.getListDay().get(19));
                    workloadMonthBO.setA21(m.getListDay().get(20));
                    workloadMonthBO.setA22(m.getListDay().get(21));
                    workloadMonthBO.setA23(m.getListDay().get(22));
                    workloadMonthBO.setA24(m.getListDay().get(23));
                    workloadMonthBO.setA25(m.getListDay().get(24));
                    workloadMonthBO.setA26(m.getListDay().get(25));
                    workloadMonthBO.setA27(m.getListDay().get(26));
                    workloadMonthBO.setA28(m.getListDay().get(27));
                }
                workloadMonthBOArrayList.add(workloadMonthBO);
            });
        }
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), WorkloadMonthBO.class, workloadMonthBOArrayList);
        try (OutputStream output = response.getOutputStream();
             BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output)) {
            // 判断数据
            if (workbook == null) {
                BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
            }
            // 设置excel的文件名称
            String excelName = "部门员工工时月统计" + DateUtil.date2String(new Date(), "yyyyMMddHHmmss") + ".xls";
            response.setHeader(CONTENT_DISPOSITION, "attchement;filename=" + excelName);
            response.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS, CONTENT_DISPOSITION);
            workbook.write(bufferedOutPut);
            bufferedOutPut.flush();
        } catch (IOException e) {
            BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
        }
    }
    @Override
    public TestProgressDetailRspBO testProgressDetail(String selectTime2){
        if (StringUtils.isBlank(selectTime2) ) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如周!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        TestProgressDetailRspBO testProgressDetailRspBO = new TestProgressDetailRspBO();
        // 根据传入的星期一，获取上周五，和本周四的日期
        //上周五
        String startTime =  DateUtil.getFriday(selectTime2);
        //本周四
        String endTime =  DateUtil.getThursday(selectTime2);
        TestProgressDetailDO testProgressDetailDO = new TestProgressDetailDO();
        testProgressDetailDO.setStartTime(startTime);
        testProgressDetailDO.setEndTime(endTime);
        //查询产品线
        List<String> listLine =  testProgressDetailExtDao.findListLine(testProgressDetailDO);
        if(listLine == null || listLine.size() <=0){
            return testProgressDetailRspBO;
        }
        List<TestProgressDetailBO> list = new ArrayList<>();
        //循环产品线，查询该产品线的需求测试进度
        for(int i= 0;i<listLine.size();i++){
            TestProgressDetailBO testProgressDetailBO = new TestProgressDetailBO();
            testProgressDetailBO.setReqPrdLine(listLine.get(i));
             int completionNumber = 0;
             int underwayNumber = 0;
             int caseExecutionNumber = 0;
             int plogNumber = 0;
             testProgressDetailDO.setReqPrdLine(listLine.get(i));
            // 根据产品线查询，周期内有多少个需求epic
            List<String> listEpic =  testProgressDetailExtDao.findListEpic(testProgressDetailDO);
            //如果listEpic为空，则跳过该产品线
            if(listEpic == null || listEpic.size() <=0){
               continue;
            }
            // 在根据epic查询周期内部的每天需求进度情况
            for(int j=0;j<listEpic.size();j++){
                testProgressDetailDO.setEpickey(listEpic.get(j));
                //单个需求周期内的测试进度情况
                List<TestProgressDetailDO> listDate =  testProgressDetailExtDao.findListDate(testProgressDetailDO);
                // 查询前一个周期的测试进度
                List<TestProgressDetailDO> listPriorDate =  testProgressDetailExtDao.findListPriorDate(testProgressDetailDO);

                if(listDate == null || listDate.size() <=0){
                    continue;
                }
                //判断进度 if进度为NaN 并且测试案例执行数和测试案例完成数不为0则进行中需求+1
                //进度
                String testProgress =  listDate.get(listDate.size()-1).getTestProgress();
                //测试案例执行数
                int caseExecution = Integer.parseInt(listDate.get(listDate.size()-1).getCaseExecutionNumber());
                //测试案例完成数
                int CaseCompleted = Integer.parseInt(listDate.get(listDate.size()-1).getCaseCompletedNumber());
                //缺陷数
                int defects = Integer.parseInt(listDate.get(listDate.size()-1).getDefectsNumber());
                //判断需求是否进行中
                if(testProgress.equals("NaN%")){
                    if(caseExecution!=0 ||CaseCompleted!=0){
                        underwayNumber = underwayNumber+1;
                    }
                }
                if(!testProgress.equals("NaN%")){
                    double progressNumber = Double.valueOf(testProgress.replaceAll("%",""));
                    if(progressNumber>=100){
                        completionNumber = completionNumber+1;
                    }
                    if(progressNumber>0&&progressNumber<100){
                        underwayNumber = underwayNumber+1;
                    }
                }
                //如果本周期之前没有数据，则测试案例执行数，和plog数就为本周最后一条记录的数值
                if(listPriorDate == null || listPriorDate.size() <=0){
                    caseExecutionNumber = caseExecutionNumber + caseExecution;
                    plogNumber = plogNumber + defects;
                }else{
                    //测试案例执行数
                    int caseExecution1 = caseExecution - Integer.parseInt(listPriorDate.get(listPriorDate.size()-1).getCaseExecutionNumber());
                    //缺陷数
                    int defects1 = defects -Integer.parseInt(listPriorDate.get(listPriorDate.size()-1).getDefectsNumber());
                    caseExecutionNumber = caseExecutionNumber + caseExecution1;
                    plogNumber = plogNumber + defects1;

                }


            }
            testProgressDetailBO.setCompletionNumber(completionNumber);
            testProgressDetailBO.setCaseExecutionNumber(caseExecutionNumber);
            testProgressDetailBO.setUnderwayNumber(underwayNumber);
            testProgressDetailBO.setPlogNumber(plogNumber);
            list.add(testProgressDetailBO);
        }
        testProgressDetailRspBO.setTestProgressDetailBOList(list);
        return testProgressDetailRspBO;
    }
    @Override
    public DemandHoursRspBO departmentalDefectRate2(String selectTime1, String selectTime2){
        List<ProductLineDefectsBO> impl = null;
        List<String> list = getSixMonth(selectTime2);
        List<String> workingHoursBOS = new LinkedList<>();
        List<String> listRate = new LinkedList<>();
        List<String> SumBos = new LinkedList<>();
        // 循环月份 查询每个月的团队代码缺陷率
        for (int i = 0; i < list.size(); i++) {
            impl = departmentalDefectRate(list.get(i));
            SumBos.add(list.get(i));
            int sum = 0;
            double defectsNumber = 0;
            double workload = 0;
            if (impl != null && impl.size() >= 0) {
                for (int j = 0; j < impl.size(); j++) {
                    defectsNumber = defectsNumber + Double.valueOf(impl.get(j).getDefectsNumber());
                    workload = workload + Double.valueOf(impl.get(j).getWorkload());
                }
                if(workload==0){
                    workingHoursBOS.add("0");
                    listRate.add(defectsNumber +"/"+0);
                }else{
                    String defectRate = String.format("%.2f", defectsNumber / workload * 100);
                    workingHoursBOS.add(defectRate);
                    listRate.add(defectsNumber +"/"+workload);
                }
            } else {
                workingHoursBOS.add("0");
            }
        }
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO.setStringList(workingHoursBOS);
        demandHoursRspBO.setListSum(SumBos);
        demandHoursRspBO.setListRate(listRate);
        return demandHoursRspBO;
    }

    /**
     * 缺陷详情数据
     * @param selectTime1
     * @param selectTime2
     * @param seriesName
     * @param name
     * @return
     */
    @Override
    public DefectDetailsRspBO getDetails(String selectTime1, String selectTime2,String seriesName,String name){
        List<DefectDetailsDO> defectDetailsDOList = new LinkedList<>();
        if (StringUtils.isBlank(selectTime1) && StringUtils.isBlank(selectTime2)) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如周、月!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        DefectDetailsDO defectDetailsDO = new DefectDetailsDO();
        defectDetailsDO.setProblemHandlerDepartment(name);
        // 查询周
        if (StringUtils.isNotBlank(selectTime1) && StringUtils.isBlank(selectTime2)) {
            defectDetailsDO.setRegistrationDate(selectTime1);
            defectDetailsDOList = defectDetailsExtDao.findWeekList(defectDetailsDO);
        }
        // 查询月
        if (StringUtils.isNotBlank(selectTime2) && StringUtils.isBlank(selectTime1)) {
            defectDetailsDO.setRegistrationDate(selectTime2);
            defectDetailsDOList = defectDetailsExtDao.findList(defectDetailsDO);
        }
        List<DefectDetailsBO> defectDetailsBOList = new LinkedList<>();
        defectDetailsBOList = BeanConvertUtils.convertList(defectDetailsDOList, DefectDetailsBO.class);

        System.err.println(defectDetailsBOList.size());
        DefectDetailsRspBO defectDetailsRspBO = new DefectDetailsRspBO();
         defectDetailsRspBO.setDefectDetailsBos(defectDetailsBOList);

        return defectDetailsRspBO;
    }

    @Override
    public IssueDetailsRspBO getIssueDetails(String selectTime1, String selectTime2,String seriesName,String name){
        List<IssueDetailsDO> issueDetailsDOLinkedList = new LinkedList<>();
        if (StringUtils.isBlank(selectTime1) && StringUtils.isBlank(selectTime2)) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如周、月!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        IssueDetailsDO issueDetailsDO = new IssueDetailsDO();
        issueDetailsDO.setIssueDepartment(name);
        // 查询周
        if (StringUtils.isNotBlank(selectTime1) && StringUtils.isBlank(selectTime2)) {
            issueDetailsDO.setRegistrationDate(selectTime1);
            issueDetailsDOLinkedList = issueDetailsExtDao.findWeekList(issueDetailsDO);
        }
        // 查询月
        if (StringUtils.isNotBlank(selectTime2) && StringUtils.isBlank(selectTime1)) {
            issueDetailsDO.setRegistrationDate(selectTime2);
            issueDetailsDOLinkedList = issueDetailsExtDao.findList(issueDetailsDO);
        }
        List<IssueDetailsBO> issueDetailsBOList = new LinkedList<>();
        issueDetailsBOList = BeanConvertUtils.convertList(issueDetailsDOLinkedList, IssueDetailsBO.class);

        IssueDetailsRspBO issueDetailsRspBO = new IssueDetailsRspBO();
        issueDetailsRspBO.setIssueDetailsBOList(issueDetailsBOList);

        return issueDetailsRspBO;
    }

    @Override
    public ProductionDefectsRspBO getProDefectDetails(String selectTime1, String selectTime2,String seriesName,String name){
        List<ProductionDefectsDO> productionDefectsDOLinkedList = new LinkedList<>();
        if (StringUtils.isBlank(selectTime1) && StringUtils.isBlank(selectTime2)) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如周、月!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        ProductionDefectsDO productionDefectsDO = new ProductionDefectsDO();
        productionDefectsDO.setProblemattributiondept(name);
        // 查询周
        if (StringUtils.isNotBlank(selectTime1) && StringUtils.isBlank(selectTime2)) {
            productionDefectsDO.setProcessstartdate(selectTime1);
            productionDefectsDOLinkedList = productionDefectsExtDao.findWeekList(productionDefectsDO);
        }
        // 查询月
        if (StringUtils.isNotBlank(selectTime2) && StringUtils.isBlank(selectTime1)) {
            productionDefectsDO.setProcessstartdate(selectTime2);
            productionDefectsDOLinkedList = productionDefectsExtDao.findList(productionDefectsDO);
        }
        List<ProductionDefectsBO> productionDefectsBOList = new LinkedList<>();
        productionDefectsBOList = BeanConvertUtils.convertList(productionDefectsDOLinkedList, ProductionDefectsBO.class);

        ProductionDefectsRspBO productionDefectsRspBO = new ProductionDefectsRspBO();
        productionDefectsRspBO.setProductionDefectsBOList(productionDefectsBOList);
        return productionDefectsRspBO;
    }

    @Override
    public ProUnhandledIssuesRspBO getProUnhandledIssuesDetails(String selectTime1, String selectTime2,String seriesName,String name){
        List<ProUnhandledIssuesDO> proUnhandledIssuesDOLinkedList = new LinkedList<>();
        if (StringUtils.isBlank(selectTime1) && StringUtils.isBlank(selectTime2)) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如周、月!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        ProUnhandledIssuesDO proUnhandledIssuesDO = new ProUnhandledIssuesDO();
        proUnhandledIssuesDO.setDepartment(name);
        // 查询周
        if (StringUtils.isNotBlank(selectTime1) && StringUtils.isBlank(selectTime2)) {
            proUnhandledIssuesDO.setProductionDate(selectTime1);
            proUnhandledIssuesDOLinkedList = proUnhandledIssuesExtDao.findWeek(proUnhandledIssuesDO);
        }
        // 查询月
        if (StringUtils.isNotBlank(selectTime2) && StringUtils.isBlank(selectTime1)) {
            proUnhandledIssuesDO.setProductionDate(selectTime2);
            proUnhandledIssuesDOLinkedList = proUnhandledIssuesExtDao.findMonth(proUnhandledIssuesDO);
        }
        List<ProUnhandledIssuesBO> proUnhandledIssuesBOList = new LinkedList<>();
        proUnhandledIssuesBOList = BeanConvertUtils.convertList(proUnhandledIssuesDOLinkedList, ProUnhandledIssuesBO.class);
        if(!proUnhandledIssuesBOList.isEmpty()){
            //iterator遍历
            Iterator<ProUnhandledIssuesBO> it = proUnhandledIssuesBOList.iterator();
            while(it.hasNext()){
                ProUnhandledIssuesBO proUnhandledIssuesBO = it.next();
                if("评审问题未解决数".equals(seriesName)){
                    if(proUnhandledIssuesBO.getProblemNumber() == 0){
                        it.remove();
                    }
                }
                if("缺陷未解决数".equals(seriesName)){
                    if(proUnhandledIssuesBO.getDefectsNumber() == 0){
                        it.remove();
                    }
                }
            }
        }
        ProUnhandledIssuesRspBO proUnhandledIssuesRspBO = new ProUnhandledIssuesRspBO();
        proUnhandledIssuesRspBO.setProUnhandledIssuesBOList(proUnhandledIssuesBOList);
        return proUnhandledIssuesRspBO;
    }

    @Override
    public SmokeTestFailedCountRspBO getSmokeTestFailedCount2(String selectTime1, String selectTime2,int count ){
        List<SmokeTestFailedCountDO> smokeTestFailedCountDOLinkedList = new LinkedList<>();
        if (StringUtils.isBlank(selectTime1) && StringUtils.isBlank(selectTime2)) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如周、月!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        SmokeTestFailedCountDO smokeTestFailedCountDO = new SmokeTestFailedCountDO();
        smokeTestFailedCountDO.setCount(count);
        // 查询周
        if (StringUtils.isNotBlank(selectTime1) && StringUtils.isBlank(selectTime2)) {
            smokeTestFailedCountDO.setTestDate(selectTime1);
            smokeTestFailedCountDOLinkedList = iSmokeTestFailedCountDao.findWeek(smokeTestFailedCountDO);
        }
        // 查询月
        if (StringUtils.isNotBlank(selectTime2) && StringUtils.isBlank(selectTime1)) {
            smokeTestFailedCountDO.setTestDate(selectTime2);
            smokeTestFailedCountDOLinkedList = iSmokeTestFailedCountDao.findMonth(smokeTestFailedCountDO);
        }
        List<SmokeTestFailedCountBO> smokeTestFailedCountBOList = new LinkedList<>();
        smokeTestFailedCountBOList = BeanConvertUtils.convertList(smokeTestFailedCountDOLinkedList, SmokeTestFailedCountBO.class);

        SmokeTestFailedCountRspBO smokeTestFailedCountRspBO = new SmokeTestFailedCountRspBO();
        smokeTestFailedCountRspBO.setSmokeTestFailedCountBOList(smokeTestFailedCountBOList);
        return smokeTestFailedCountRspBO;
    }
}

