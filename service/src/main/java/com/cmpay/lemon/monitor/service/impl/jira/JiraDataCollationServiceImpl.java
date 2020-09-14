package com.cmpay.lemon.monitor.service.impl.jira;

import com.cmpay.lemon.common.utils.JudgeUtils;
import com.cmpay.lemon.monitor.bo.DemandBO;
import com.cmpay.lemon.monitor.bo.DemandTestStatusBO;
import com.cmpay.lemon.monitor.bo.UserInfoBO;
import com.cmpay.lemon.monitor.bo.jira.JiraSubtasksBO;
import com.cmpay.lemon.monitor.bo.jira.JiraTaskBodyBO;
import com.cmpay.lemon.monitor.bo.jira.JiraWorklogBO;
import com.cmpay.lemon.monitor.dao.*;
import com.cmpay.lemon.monitor.entity.*;
import com.cmpay.lemon.monitor.service.SystemRoleService;
import com.cmpay.lemon.monitor.service.SystemUserService;
import com.cmpay.lemon.monitor.service.jira.JiraDataCollationService;
import com.cmpay.lemon.monitor.service.jira.JiraOperationService;
import com.cmpay.lemon.monitor.service.reportForm.ReqDataCountService;
import com.cmpay.lemon.monitor.utils.DateUtil;
import com.cmpay.lemon.monitor.utils.jira.JiraUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

@Service
public class JiraDataCollationServiceImpl implements JiraDataCollationService {
    @Autowired
    IDemandJiraDao demandJiraDao;
    @Autowired
    IDemandExtDao demandDao;
    @Autowired
    IJiraBasicInfoDao jiraBasicInfoDao;
    @Autowired
    IDemandJiraSubtaskDao demandJiraSubtaskDao;

    @Autowired
    IDemandJiraDevelopMasterTaskDao demandJiraDevelopMasterTaskDao;
    @Autowired
    JiraOperationService jiraOperationService;
    @Autowired
    IJiraWorklogDao jiraWorklogDao;

    @Autowired
    private IWorkingHoursExtDao iWorkingHoursDao;
    @Autowired
    SystemUserService systemUserService;
    @Autowired
    IUserExtDao userExtDao;
    @Autowired
    IDefectDetailsExtDao defectDetailsDao;

    @Autowired
    IIssueDetailsExtDao issueDetailsDao;
    @Autowired
    IProblemStatisticDao problemStatisticDao;
    @Autowired
    SystemRoleService systemRoleService;

    @Autowired
    private IUserRoleExtDao userRoleExtDao;
    @Autowired
    private IUserExtDao iUserDao;
    @Autowired
    private ReqDataCountService reqDataCountService;
    @Autowired
    private ITestProgressDetailExtDao testProgressDetailDao;

    @Autowired
    private IProUnhandledIssuesExtDao proUnhandledIssuesDao;
    @Async
    @Override
    public void getIssueModifiedWithinOneDay() {
        List<JiraTaskBodyBO> jiraTaskBodyBOList = new LinkedList<>();
        int i = 0;
        //拉取一定时间内有过修改的jira任务内容
        while (true) {
            List<JiraTaskBodyBO> jiraTaskBodyBOS = JiraUtil.batchQueryIssuesModifiedWithinOneDay(i);
            if (JudgeUtils.isEmpty(jiraTaskBodyBOS)) {
                break;
            }
            jiraTaskBodyBOList.addAll(jiraTaskBodyBOS);
            i = i + 50;
        }
//        JiraTaskBodyBO jiraTaskBodyBO1 = new JiraTaskBodyBO();
//        jiraTaskBodyBO1.setJiraKey("CMPAY-3675");
//        jiraTaskBodyBOList.add(jiraTaskBodyBO1);
        if (JudgeUtils.isNotEmpty(jiraTaskBodyBOList)) {
            HashSet<String> epicList = new HashSet<>();
            jiraTaskBodyBOList.forEach(m -> {
                try {
                    //依据jiraKey查找jira详情数据
                        JiraTaskBodyBO jiraTaskBodyBO = JiraUtil.GetIssue(m.getJiraKey());
                        //登记jira基础表，缺陷任务和问题登记
                        this.registerJiraBasicInfo(jiraTaskBodyBO);
                        //工时处理
                        this.registerWorklogs(jiraTaskBodyBO);
                        epicList.add(jiraTaskBodyBO.getEpicKey());
                } catch (Exception e) {

                    e.printStackTrace();
                }
            });
            //epic对应缺陷及评审问题登记问题统计表
            epicList.forEach(m -> {
                if (m == null) {
                    return;
                }
                ProblemStatisticDO problemStatisticDO = new ProblemStatisticDO();
                problemStatisticDO.setEpicKey(m);
                DefectDetailsDO defectDetailsDO = new DefectDetailsDO();
                defectDetailsDO.setEpicKey(m);
                List<DefectDetailsDO> defectDetailsDOList = defectDetailsDao.find(defectDetailsDO);
                if(JudgeUtils.isNotEmpty(defectDetailsDOList)){
                    for (int j = 0; j < defectDetailsDOList.size(); j++) {
                        if(JudgeUtils.isBlank(defectDetailsDOList.get(j).getDefectType())){
                            continue;
                        }
                        if (defectDetailsDOList.get(j).getDefectType().equals("环境问题-外围平台")) {
                            problemStatisticDO.setExternalDefectsNumber(problemStatisticDO.getExternalDefectsNumber() + 1);
                        } else if (defectDetailsDOList.get(j).getDefectType().equals("环境问题-版本更新")) {
                            problemStatisticDO.setVersionDefectsNumber(problemStatisticDO.getVersionDefectsNumber() + 1);
                        } else if (defectDetailsDOList.get(j).getDefectType().equals("环境问题-参数配置")) {
                            problemStatisticDO.setParameterDefectsNumber(problemStatisticDO.getParameterDefectsNumber() + 1);
                        } else if (defectDetailsDOList.get(j).getDefectType().equals("产品设计-功能设计")) {
                            problemStatisticDO.setFunctionDefectsNumber(problemStatisticDO.getFunctionDefectsNumber() + 1);
                        } else if (defectDetailsDOList.get(j).getDefectType().equals("产品设计-流程优化")) {
                            problemStatisticDO.setProcessDefectsNumber(problemStatisticDO.getProcessDefectsNumber() + 1);
                        } else if (defectDetailsDOList.get(j).getDefectType().equals("产品设计-提示语优化")) {
                            problemStatisticDO.setPromptDefectsNumber(problemStatisticDO.getPromptDefectsNumber() + 1);
                        } else if (defectDetailsDOList.get(j).getDefectType().equals("产品设计-页面设计")) {
                            problemStatisticDO.setPageDefectsNumber(problemStatisticDO.getPageDefectsNumber() + 1);
                        } else if (defectDetailsDOList.get(j).getDefectType().equals("程序问题-后台应用")) {
                            problemStatisticDO.setBackgroundDefectsNumber(problemStatisticDO.getBackgroundDefectsNumber() + 1);
                        } else if (defectDetailsDOList.get(j).getDefectType().equals("程序问题-前端开发")) {
                            problemStatisticDO.setFrontDefectsNumber(problemStatisticDO.getFrontDefectsNumber() + 1);
                        } else if (defectDetailsDOList.get(j).getDefectType().equals("程序问题-修改引入问题")) {
                            problemStatisticDO.setModifyDefectsNumber(problemStatisticDO.getModifyDefectsNumber() + 1);
                        } else if (defectDetailsDOList.get(j).getDefectType().equals("技术设计-技术设计")) {
                            problemStatisticDO.setDesignDefectsNumber(problemStatisticDO.getDesignDefectsNumber() + 1);
                        } else if (defectDetailsDOList.get(j).getDefectType().equals("无效问题")) {
                            problemStatisticDO.setInvalidDefectsNumber(problemStatisticDO.getInvalidDefectsNumber() + 1);
                        }
                    }
                }
                IssueDetailsDO issueDetailsDO = new IssueDetailsDO();
                issueDetailsDO.setEpicKey(m);
                List<IssueDetailsDO> issueDetailsDOList = issueDetailsDao.find(issueDetailsDO);
                if(JudgeUtils.isNotEmpty(issueDetailsDOList)){
                    for (int j = 0; j < issueDetailsDOList.size(); j++) {
                        if(JudgeUtils.isBlank(issueDetailsDOList.get(j).getIssueType())){
                            continue;
                        }
                        if (issueDetailsDOList.get(j).getIssueType().equals("需求评审")) {
                            problemStatisticDO.setRequirementsReviewNumber(problemStatisticDO.getRequirementsReviewNumber() + 1);
                        } else if (issueDetailsDOList.get(j).getIssueType().equals("技术方案评审")) {
                            problemStatisticDO.setVersionDefectsNumber(problemStatisticDO.getVersionDefectsNumber() + 1);
                        } else if (issueDetailsDOList.get(j).getIssueType().equals("代码评审")) {
                            problemStatisticDO.setCodeReviewNumber(problemStatisticDO.getCodeReviewNumber() + 1);
                        } else if (issueDetailsDOList.get(j).getIssueType().equals("测试案例评审")) {
                            problemStatisticDO.setTestReviewNumber(problemStatisticDO.getTestReviewNumber() + 1);
                        } else if (issueDetailsDOList.get(j).getIssueType().equals("投产方案评审")) {
                            problemStatisticDO.setProductionReviewNumber(problemStatisticDO.getProductionReviewNumber() + 1);
                        } else if (issueDetailsDOList.get(j).getIssueType().equals("其他评审")) {
                            problemStatisticDO.setOtherReviewsNumber(problemStatisticDO.getOtherReviewsNumber() + 1);
                        }
                    }
                }
                ProblemStatisticDO problemStatisticDO1 = problemStatisticDao.get(problemStatisticDO.getEpicKey());
                if (JudgeUtils.isNull(problemStatisticDO1)) {
                    problemStatisticDao.insert(problemStatisticDO);
                } else {
                    problemStatisticDao.update(problemStatisticDO);
                }


            });
        }


    }

    @Async
    @Override
    public void getDefectAndProblem(){
        List<JiraTaskBodyBO> jiraTaskBodyBOList = new LinkedList<>();
        int i = 0;
        //拉取一天内有过修改的jira任务内容
        while (true) {
            List<JiraTaskBodyBO> jiraTaskBodyBOS = JiraUtil.batchQueryIssuesModifiedWithinOneDay2(i);
            if (JudgeUtils.isEmpty(jiraTaskBodyBOS)) {
                break;
            }
            jiraTaskBodyBOList.addAll(jiraTaskBodyBOS);
            i = i + 50;
        }
        if (JudgeUtils.isNotEmpty(jiraTaskBodyBOList)) {
            HashSet<String> epicList = new HashSet<>();
            jiraTaskBodyBOList.forEach(m -> {
                try {
                    //依据jiraKey查找jira详情数据
                    JiraTaskBodyBO jiraTaskBodyBO = JiraUtil.GetIssue(m.getJiraKey());
                    //登记jira基础表，缺陷任务和问题登记
                    this.registerJiraBasicInfo(jiraTaskBodyBO);
                    epicList.add(jiraTaskBodyBO.getEpicKey());
                } catch (Exception e) {

                    e.printStackTrace();
                }
            });
            //epic对应缺陷及评审问题登记问题统计表
            epicList.forEach(m -> {
                if (m == null) {
                    return;
                }
                ProblemStatisticDO problemStatisticDO = new ProblemStatisticDO();
                problemStatisticDO.setEpicKey(m);
                DefectDetailsDO defectDetailsDO = new DefectDetailsDO();
                defectDetailsDO.setEpicKey(m);
                List<DefectDetailsDO> defectDetailsDOList = defectDetailsDao.find(defectDetailsDO);
                if(JudgeUtils.isNotEmpty(defectDetailsDOList)){
                    for (int j = 0; j < defectDetailsDOList.size(); j++) {
                        if(JudgeUtils.isBlank(defectDetailsDOList.get(j).getDefectType())){
                            continue;
                        }
                        if (defectDetailsDOList.get(j).getDefectType().equals("环境问题-外围平台")) {
                            problemStatisticDO.setExternalDefectsNumber(problemStatisticDO.getExternalDefectsNumber() + 1);
                        } else if (defectDetailsDOList.get(j).getDefectType().equals("环境问题-版本更新")) {
                            problemStatisticDO.setVersionDefectsNumber(problemStatisticDO.getVersionDefectsNumber() + 1);
                        } else if (defectDetailsDOList.get(j).getDefectType().equals("环境问题-参数配置")) {
                            problemStatisticDO.setParameterDefectsNumber(problemStatisticDO.getParameterDefectsNumber() + 1);
                        } else if (defectDetailsDOList.get(j).getDefectType().equals("产品设计-功能设计")) {
                            problemStatisticDO.setFunctionDefectsNumber(problemStatisticDO.getFunctionDefectsNumber() + 1);
                        } else if (defectDetailsDOList.get(j).getDefectType().equals("产品设计-流程优化")) {
                            problemStatisticDO.setProcessDefectsNumber(problemStatisticDO.getProcessDefectsNumber() + 1);
                        } else if (defectDetailsDOList.get(j).getDefectType().equals("产品设计-提示语优化")) {
                            problemStatisticDO.setPromptDefectsNumber(problemStatisticDO.getPromptDefectsNumber() + 1);
                        } else if (defectDetailsDOList.get(j).getDefectType().equals("产品设计-页面设计")) {
                            problemStatisticDO.setPageDefectsNumber(problemStatisticDO.getPageDefectsNumber() + 1);
                        } else if (defectDetailsDOList.get(j).getDefectType().equals("程序问题-后台应用")) {
                            problemStatisticDO.setBackgroundDefectsNumber(problemStatisticDO.getBackgroundDefectsNumber() + 1);
                        } else if (defectDetailsDOList.get(j).getDefectType().equals("程序问题-前端开发")) {
                            problemStatisticDO.setFrontDefectsNumber(problemStatisticDO.getFrontDefectsNumber() + 1);
                        } else if (defectDetailsDOList.get(j).getDefectType().equals("程序问题-修改引入问题")) {
                            problemStatisticDO.setModifyDefectsNumber(problemStatisticDO.getModifyDefectsNumber() + 1);
                        } else if (defectDetailsDOList.get(j).getDefectType().equals("技术设计-技术设计")) {
                            problemStatisticDO.setDesignDefectsNumber(problemStatisticDO.getDesignDefectsNumber() + 1);
                        } else if (defectDetailsDOList.get(j).getDefectType().equals("无效问题")) {
                            problemStatisticDO.setInvalidDefectsNumber(problemStatisticDO.getInvalidDefectsNumber() + 1);
                        }
                    }
                }
                IssueDetailsDO issueDetailsDO = new IssueDetailsDO();
                issueDetailsDO.setEpicKey(m);
                List<IssueDetailsDO> issueDetailsDOList = issueDetailsDao.find(issueDetailsDO);
                if(JudgeUtils.isNotEmpty(issueDetailsDOList)){
                    for (int j = 0; j < issueDetailsDOList.size(); j++) {
                        if(JudgeUtils.isBlank(issueDetailsDOList.get(j).getIssueType())){
                            continue;
                        }
                        if (issueDetailsDOList.get(j).getIssueType().equals("需求评审")) {
                            problemStatisticDO.setRequirementsReviewNumber(problemStatisticDO.getRequirementsReviewNumber() + 1);
                        } else if (issueDetailsDOList.get(j).getIssueType().equals("技术方案评审")) {
                            problemStatisticDO.setVersionDefectsNumber(problemStatisticDO.getVersionDefectsNumber() + 1);
                        } else if (issueDetailsDOList.get(j).getIssueType().equals("代码评审")) {
                            problemStatisticDO.setCodeReviewNumber(problemStatisticDO.getCodeReviewNumber() + 1);
                        } else if (issueDetailsDOList.get(j).getIssueType().equals("测试案例评审")) {
                            problemStatisticDO.setTestReviewNumber(problemStatisticDO.getTestReviewNumber() + 1);
                        } else if (issueDetailsDOList.get(j).getIssueType().equals("投产方案评审")) {
                            problemStatisticDO.setProductionReviewNumber(problemStatisticDO.getProductionReviewNumber() + 1);
                        } else if (issueDetailsDOList.get(j).getIssueType().equals("其他评审")) {
                            problemStatisticDO.setOtherReviewsNumber(problemStatisticDO.getOtherReviewsNumber() + 1);
                        }
                    }
                }
                ProblemStatisticDO problemStatisticDO1 = problemStatisticDao.get(problemStatisticDO.getEpicKey());
                if (JudgeUtils.isNull(problemStatisticDO1)) {
                    problemStatisticDao.insert(problemStatisticDO);
                } else {
                    problemStatisticDao.update(problemStatisticDO);
                }


            });
        }

        //获得投产需求 未完成的问题，//需要在统计完缺陷后执行
        getproductedsRemainingQuestions();
    }

    @Async
    @Override
    public void TestProgressDetailOneDay(){
        //获取当前日期的前一天
        String date = DateUtil.getBeforeDay();
        //获取当月时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        String month = simpleDateFormat.format(new Date());
        DemandDO demandDO = new DemandDO();
        demandDO.setReqImplMon(month);
        // 测试部需求进度
        List<DemandTestStatusBO>  list =  reqDataCountService.demandTestStatusList2();
        for(int i =0;i<list.size();i++){
            // 如果未填写需求测试案例总数，导致测试进度为NAN，则跳过 并且测试执行数，完成数，缺陷数都为0的情况跳过
            if("NaN%".equals(list.get(i).getTestProgress()) && list.get(i).getCaseCompletedNumber() == 0
            && list.get(i).getCaseExecutionNumber() == 0 && list.get(i).getDefectsNumber() == 0) {
                continue;
            }
            // 根据需求名，需求编号、月份 获取 需求的jira编号
            demandDO.setReqNo(list.get(i).getReqNo());
            demandDO.setReqNm(list.get(i).getReqNm());
            List<DemandDO> demandDos = demandDao.QueryIsExecutingDemand2(demandDO);

            //从搜索到的需求中  找到对应的jira编号
            DemandJiraDO demandJiraDO = new DemandJiraDO();
            demandJiraDO.setReqInnerSeq(demandDos.get(0).getReqInnerSeq());
            List<DemandJiraDO> demandJiraDOS = demandJiraDao.find(demandJiraDO);
            // jira编号
            String jiraKey = demandJiraDOS.get(0).getJiraKey();
            // 产品线
            String  reqPrdLind = demandDos.get(0).getReqPrdLine();
            TestProgressDetailDO testProgressDetailDO = new TestProgressDetailDO();
            testProgressDetailDO.setEpickey(jiraKey);
            testProgressDetailDO.setTestDate(date);
            testProgressDetailDO.setReqPrdLine(reqPrdLind);
            testProgressDetailDO.setTestProgress(list.get(i).getTestProgress());
            testProgressDetailDO.setTestCaseNumber(list.get(i).getTestCaseNumber());
            testProgressDetailDO.setCaseCompletedNumber(list.get(i).getCaseCompletedNumber()+"");
            testProgressDetailDO.setCaseExecutionNumber(list.get(i).getCaseExecutionNumber()+"");
            testProgressDetailDO.setDefectsNumber(list.get(i).getDefectsNumber()+"");
            // 根据epic和日期查询该天是否已存在数据
            TestProgressDetailDO testProgressDetailDO1 = new TestProgressDetailDO();
            testProgressDetailDO1.setEpickey(jiraKey);
            testProgressDetailDO1.setTestDate(date);
            List<TestProgressDetailDO> testProgressDetailDOList = testProgressDetailDao.find(testProgressDetailDO1);
            if(testProgressDetailDOList ==null || testProgressDetailDOList.size() <=0){
                // 根据epic查询是否存在历史数据
                TestProgressDetailDO testProgressDetailDO2 = new TestProgressDetailDO();
                testProgressDetailDO2.setEpickey(jiraKey);
                List<TestProgressDetailDO> testProgressDetailDos = testProgressDetailDao.find(testProgressDetailDO2);
                //为空，说明是新需求进行测试
                if(testProgressDetailDos ==null || testProgressDetailDos.size() <=0){
                    //插入
                    testProgressDetailDao.insert(testProgressDetailDO);
                }else{
                    // 比较历史数据最后一条，与当前数据，如果数据不变，则不插入
                    if(!testProgressDetailDO.getCaseCompletedNumber().equals(testProgressDetailDos.get(testProgressDetailDos.size()-1).getCaseCompletedNumber())
                    || !testProgressDetailDO.getCaseExecutionNumber().equals(testProgressDetailDos.get(testProgressDetailDos.size()-1).getCaseExecutionNumber())
                    || !testProgressDetailDO.getTestCaseNumber().equals(testProgressDetailDos.get(testProgressDetailDos.size()-1).getTestCaseNumber())
                    || !testProgressDetailDO.getDefectsNumber().equals(testProgressDetailDos.get(testProgressDetailDos.size()-1).getDefectsNumber())){
                        //测试案例总数，测试案例执行数，测试案例完成数，缺陷数，如果有不同，则添加记录
                        //插入
                        testProgressDetailDao.insert(testProgressDetailDO);
                    }
                }

            }else{
                //更新
                testProgressDetailDO.setId(testProgressDetailDOList.get(0).getId());
                testProgressDetailDao.update(testProgressDetailDO);
            }

        }
    }

    private void getproductedsRemainingQuestions() {
        ProUnhandledIssuesDO proUnhandledIssuesDO = new ProUnhandledIssuesDO();
        proUnhandledIssuesDO.setCalculateFlag("N");
        List<ProUnhandledIssuesDO> proUnhandledIssuesDOS = proUnhandledIssuesDao.find(proUnhandledIssuesDO);
        proUnhandledIssuesDOS.forEach(m->{
            DefectDetailsDO defectDetailsDO = new DefectDetailsDO();
            defectDetailsDO.setEpicKey(m.getJirakey());
            // 投产未解决缺陷数
            List<DefectDetailsDO> list = defectDetailsDao.findNotCompleted(defectDetailsDO);
            // 缺陷总数
            List<DefectDetailsDO> listSum = defectDetailsDao.findList(defectDetailsDO);
            if(JudgeUtils.isNotEmpty(list)){
                m.setDefectsNumber(list.size());
            }else{
                m.setDefectsNumber(0);
            }
            if(JudgeUtils.isNotEmpty(listSum)){
                m.setDefectsNumberSum(listSum.size());
            }else{
                m.setDefectsNumberSum(0);
            }
            IssueDetailsDO issueDetailsDO = new IssueDetailsDO();
            issueDetailsDO.setEpicKey(m.getJirakey());
            // 投产时间未解决评审问题数
            List<IssueDetailsDO> list1 = issueDetailsDao.findNotCompleted(issueDetailsDO);
            //评审问题总数
            List<IssueDetailsDO> list1Sum = issueDetailsDao.findList(issueDetailsDO);
            if(JudgeUtils.isNotEmpty(list1)){
                m.setProblemNumber(list1.size());
            }else{
                m.setProblemNumber(0);
            }
            if(JudgeUtils.isNotEmpty(list1Sum)){
                m.setProblemNumberSum(list1Sum.size());
            }else{
                m.setProblemNumberSum(0);
            }
            m.setCalculateFlag("Y");
            proUnhandledIssuesDao.update(m);
        });
    }

    /**
     * 投产提出转待部署时保持未解决的缺陷和评审问题数量
     * @param reqNo
     */
    @Override
    @Async
    public void inquiriesAboutRemainingProblems(String reqNo) {
        DemandDO demandDO = new DemandDO();
        demandDO.setReqNo(reqNo);
        List<DemandDO> demandDOS = demandDao.find(demandDO);
        if(JudgeUtils.isEmpty(demandDOS)){
            return;
        }
        DemandJiraDO demandJiraDO = demandJiraDao.get(demandDOS.get(0).getReqInnerSeq());

        if(JudgeUtils.isNull(demandJiraDO)){
            return;
        }
        ProUnhandledIssuesDO proUnhandledIssuesDO = new ProUnhandledIssuesDO();
        proUnhandledIssuesDO.setReqNo(reqNo);
        proUnhandledIssuesDO.setJirakey(demandJiraDO.getJiraKey());
        String selectTime = DateUtil.date2String(new Date(), "yyyy-MM-dd");
        proUnhandledIssuesDO.setProductionDate(selectTime);
        proUnhandledIssuesDO.setDepartment( demandDOS.get(0).getDevpLeadDept());

        //N为未处理标识  当天晚上定时任务会通过此表示查找对应的数据
        proUnhandledIssuesDO.setCalculateFlag("N");

        ProUnhandledIssuesDO proUnhandledIssuesDO1 = proUnhandledIssuesDao.get(reqNo);
        if(JudgeUtils.isNull(proUnhandledIssuesDO1)){
            proUnhandledIssuesDO.setProblemNumber(0);
            proUnhandledIssuesDO.setDefectsNumber(0);
            proUnhandledIssuesDO.setProblemNumberSum(0);
            proUnhandledIssuesDO.setDefectsNumberSum(0);
            proUnhandledIssuesDao.insert(proUnhandledIssuesDO);
        }else{
            proUnhandledIssuesDao.update(proUnhandledIssuesDO);
        }

    }

    @Override
    public void getEpicRelatedTasks(DemandBO demandBO) {
        //1.需求编号找到对应的epic
        DemandJiraDO demandJiraDO = demandJiraDao.get(demandBO.getReqInnerSeq());
        if (JudgeUtils.isNull(demandJiraDO)) {
            //未找到需求编号所对应的epic，创建epic
            jiraOperationService.createEpic(demandBO);
            demandJiraDO = demandJiraDao.get(demandBO.getReqInnerSeq());
        }
        //获取主任务
        DemandJiraDevelopMasterTaskDO demandJiraDevelopMasterTaskDO = new DemandJiraDevelopMasterTaskDO();
        demandJiraDevelopMasterTaskDO.setRelevanceEpic(demandJiraDO.getJiraKey());
        List<DemandJiraDevelopMasterTaskDO> demandJiraDevelopMasterTaskDOS = demandJiraDevelopMasterTaskDao.find(demandJiraDevelopMasterTaskDO);
        //登记epic及开发主任务的key
        LinkedList<String> JIRAKeys = new LinkedList<>();
        JIRAKeys.add(demandJiraDO.getJiraKey());
        demandJiraDevelopMasterTaskDOS.forEach(m -> JIRAKeys.add(m.getJiraKey()));

        //根据key查找对应数据
        JIRAKeys.forEach(m -> {
            JiraTaskBodyBO jiraTaskBodyBO = registerJiraBasicInfo(m, null);
            List<JiraSubtasksBO> subtasks = JiraUtil.getSubtasks(jiraTaskBodyBO);
            if (JudgeUtils.isNotEmpty(subtasks)) {
                for (int i = 0; i < subtasks.size(); i++) {
                    JiraTaskBodyBO jiraTaskBodyBO1 = this.registerJiraBasicInfo(subtasks.get(i).getSubtaskkey(), jiraTaskBodyBO.getDepartment());
                    DemandJiraSubtaskDO demandJiraSubtaskDO = new DemandJiraSubtaskDO();
                    demandJiraSubtaskDO.setSubtaskkey(jiraTaskBodyBO1.getJiraKey());
                    demandJiraSubtaskDO.setAssignee(jiraTaskBodyBO1.getAssignee());
                    demandJiraSubtaskDO.setSubtaskname(jiraTaskBodyBO1.getIssueName());
                    demandJiraSubtaskDO.setParenttaskkey(jiraTaskBodyBO.getJiraKey());
                    DemandJiraSubtaskDO demandJiraSubtaskDO1 = demandJiraSubtaskDao.get(jiraTaskBodyBO1.getJiraKey());
                    if (JudgeUtils.isNotNull(demandJiraSubtaskDO1)) {
                        demandJiraSubtaskDao.update(demandJiraSubtaskDO);
                    } else {
                        demandJiraSubtaskDao.insert(demandJiraSubtaskDO);
                    }
                    this.registerWorklogs(jiraTaskBodyBO1);
                }
            }
            this.registerWorklogs(jiraTaskBodyBO);
        });
    }

    @Async
    public JiraTaskBodyBO registerJiraBasicInfo(JiraTaskBodyBO jiraTaskBodyBO) {
        JiraBasicInfoDO jiraBasicInfoDO = new JiraBasicInfoDO();
        jiraBasicInfoDO.setJirakey(jiraTaskBodyBO.getJiraKey());
        jiraBasicInfoDO.setAggregatetimespent(jiraTaskBodyBO.getAggregatetimespent());
        jiraBasicInfoDO.setTimespent(jiraTaskBodyBO.getTimespent());
        jiraBasicInfoDO.setAssignee(jiraTaskBodyBO.getAssignee());
        jiraBasicInfoDO.setCreator(jiraTaskBodyBO.getCreator());
        jiraBasicInfoDO.setJiratype(jiraTaskBodyBO.getJiraType());
        jiraBasicInfoDO.setDescription(jiraTaskBodyBO.getIssueName());
        jiraBasicInfoDO.setEpickey(jiraTaskBodyBO.getEpicKey());
        jiraBasicInfoDO.setParenttaskkey(jiraTaskBodyBO.getParentTaskKey());
        jiraBasicInfoDO.setTestCaseNumber(jiraTaskBodyBO.getTestCaseNumber());
        if (JudgeUtils.isBlank(jiraTaskBodyBO.getEpicKey())) {
            JiraBasicInfoDO jiraBasicInfoDO1 = new JiraBasicInfoDO();
            jiraBasicInfoDO1.setJirakey(jiraTaskBodyBO.getParentTaskKey());
            List<JiraBasicInfoDO> jiraBasicInfoDOS = jiraBasicInfoDao.find(jiraBasicInfoDO1);
            jiraBasicInfoDO.setEpickey(jiraBasicInfoDOS.get(0).getEpickey());
            jiraTaskBodyBO.setEpicKey(jiraBasicInfoDOS.get(0).getEpickey());
        }
        if (jiraTaskBodyBO.getJiraType().equals("Epic")) {
            jiraTaskBodyBO.setEpicCreator(jiraTaskBodyBO.getCreator());
        } else if (JudgeUtils.isNotBlank(jiraTaskBodyBO.getEpicKey())) {
            JiraBasicInfoDO jiraBasicInfoDO1 = new JiraBasicInfoDO();
            jiraBasicInfoDO1.setJirakey(jiraTaskBodyBO.getEpicKey());
            List<JiraBasicInfoDO> jiraBasicInfoDOS = jiraBasicInfoDao.find(jiraBasicInfoDO1);
            jiraTaskBodyBO.setEpicCreator(jiraBasicInfoDOS.get(0).getCreator());
            //若为测试主任务需要将测试总案例数同步赋值epic信息
            if(jiraTaskBodyBO.getJiraType().equals("测试主任务")&&JudgeUtils.isNotBlank(jiraBasicInfoDO.getTestCaseNumber())){
                jiraBasicInfoDOS.get(0).setTestCaseNumber(jiraBasicInfoDO.getTestCaseNumber());
                jiraBasicInfoDao.update(jiraBasicInfoDOS.get(0));
            }
        }
        if (jiraTaskBodyBO.getJiraType().equals("测试主任务")) {
            jiraBasicInfoDO.setDepartment("产品测试团队");
        } else if (jiraTaskBodyBO.getJiraType().equals("测试子任务")) {
            jiraBasicInfoDO.setDepartment("产品测试团队");
        } else if (jiraTaskBodyBO.getJiraType().equals("开发子任务")) {
            jiraBasicInfoDO.setDepartment(systemUserService.getDepartmentByUser(jiraTaskBodyBO.getAssignee()));
        } else {
            jiraBasicInfoDO.setDepartment(jiraTaskBodyBO.getDepartment());
        }
        jiraBasicInfoDO.setPlanstarttime(jiraTaskBodyBO.getPlanStartTime());
        jiraBasicInfoDO.setPlanendtime(jiraTaskBodyBO.getPlanEndTime());
        JiraBasicInfoDO jiraBasicInfoDO1 = jiraBasicInfoDao.get(jiraTaskBodyBO.getJiraKey());
        if (JudgeUtils.isNotNull(jiraBasicInfoDO1)) {
            jiraBasicInfoDao.update(jiraBasicInfoDO);
        } else {
            jiraBasicInfoDao.insert(jiraBasicInfoDO);
        }
        //若是内部缺陷或者评审问题则需要登记内部权限或者评审问题表
        if (JudgeUtils.isNotBlank(jiraTaskBodyBO.getJiraType())) {
            if (jiraTaskBodyBO.getJiraType().equals("内部缺陷")) {
                DefectDetailsDO defectDetailsDO = new DefectDetailsDO();
                defectDetailsDO.setJireKey(jiraTaskBodyBO.getJiraKey());
                defectDetailsDO.setEpicKey(jiraTaskBodyBO.getEpicKey());
                defectDetailsDO.setDefectType(jiraTaskBodyBO.getProblemType());
                defectDetailsDO.setDefectStatus(jiraTaskBodyBO.getStatus());
                if(JudgeUtils.isNotBlank(jiraTaskBodyBO.getProblemHandler())){
                    defectDetailsDO.setAssignee(jiraTaskBodyBO.getProblemHandler());
                }else{
                    defectDetailsDO.setAssignee(jiraTaskBodyBO.getAssignee());
                }

                defectDetailsDO.setDefectRegistrant(jiraTaskBodyBO.getCreator());
                defectDetailsDO.setSecurityLevel(jiraTaskBodyBO.getSecurityLevel());
                defectDetailsDO.setDefectsDepartment(jiraTaskBodyBO.getDefectsDepartment());
                // 逻辑更新，归属部门可以为空
                if(JudgeUtils.isNotBlank(jiraTaskBodyBO.getDefectsDepartment())){
                    //如果归属部门填的是测试部，则根据经办人获取部门
                    if("产品测试团队".equals(jiraTaskBodyBO.getDefectsDepartment())){
                        // 经办人不为空，则根据姓名查询部门
                        if(JudgeUtils.isNotBlank(defectDetailsDO.getAssignee())){
                            UserDO userDO = new UserDO();
                            userDO.setFullname(defectDetailsDO.getAssignee());
                            List<UserDO> userDOS = iUserDao.find(userDO);
                            if(!userDOS.isEmpty()){
                                defectDetailsDO.setDefectsDepartment(userDOS.get(0).getDepartment());
                            }else{
                                defectDetailsDO.setDefectsDepartment("产品测试团队");
                            }
                        }
                    }
                }
                defectDetailsDO.setRegistrationDate(jiraTaskBodyBO.getCreateTime());
                defectDetailsDO.setDefectDetails(jiraTaskBodyBO.getDefectDetails());
                defectDetailsDO.setTestNumber(jiraTaskBodyBO.getRetestTimes());
                defectDetailsDO.setDefectName(jiraTaskBodyBO.getDefectName());
                //缺陷归属产品线
                if(JudgeUtils.isNotBlank(defectDetailsDO.getEpicKey())){
                    DemandJiraDO demandJiraDO = new DemandJiraDO();
                    demandJiraDO.setJiraKey(defectDetailsDO.getEpicKey());
                    // 根据epicKey获取内部编号
                    List<DemandJiraDO> demandJiraDos = demandJiraDao.find(demandJiraDO);
                    if(JudgeUtils.isNotEmpty(demandJiraDos)){
                        DemandDO demandDO = demandDao.get(demandJiraDos.get(demandJiraDos.size()-1).getReqInnerSeq());
                        defectDetailsDO.setProductLine(demandDO.getReqPrdLine());
                    }
                }

                DefectDetailsDO defectDetailsDO1 = defectDetailsDao.get(defectDetailsDO.getJireKey());
                if (JudgeUtils.isNull(defectDetailsDO1)) {
                    defectDetailsDao.insert(defectDetailsDO);
                } else {
                    defectDetailsDao.update(defectDetailsDO);
                }
            } else if (jiraTaskBodyBO.getJiraType().equals("评审问题")) {
                IssueDetailsDO issueDetailsDO = new IssueDetailsDO();
                issueDetailsDO.setJireKey(jiraTaskBodyBO.getJiraKey());
                issueDetailsDO.setEpicKey(jiraTaskBodyBO.getEpicKey());
                issueDetailsDO.setIssueType(jiraTaskBodyBO.getReviewQuestionType());
                issueDetailsDO.setIssueStatus(jiraTaskBodyBO.getStatus());
                issueDetailsDO.setAssignee(jiraTaskBodyBO.getAssignee());
                issueDetailsDO.setIssueDetails(jiraTaskBodyBO.getIssueName());
                issueDetailsDO.setRegistrationDate(jiraTaskBodyBO.getCreateTime());
                issueDetailsDO.setIssueRegistrant(jiraTaskBodyBO.getCreator());
                issueDetailsDO.setIssueDepartment(systemUserService.getDepartmentByUser(jiraTaskBodyBO.getAssignee()));
                IssueDetailsDO issueDetailsDO1 = issueDetailsDao.get(issueDetailsDO.getJireKey());
                if (JudgeUtils.isNull(issueDetailsDO1)) {
                    issueDetailsDao.insert(issueDetailsDO);
                } else {
                    issueDetailsDao.update(issueDetailsDO);
                }
            }
        }


        return jiraTaskBodyBO;
    }


    private JiraTaskBodyBO registerJiraBasicInfo(String m, String department) {
        JiraTaskBodyBO jiraTaskBodyBO = JiraUtil.GetIssue(m);
        JiraBasicInfoDO jiraBasicInfoDO = new JiraBasicInfoDO();
        jiraBasicInfoDO.setJirakey(jiraTaskBodyBO.getJiraKey());
        jiraBasicInfoDO.setAggregatetimespent(jiraTaskBodyBO.getAggregatetimespent());
        jiraBasicInfoDO.setTimespent(jiraTaskBodyBO.getTimespent());
        jiraBasicInfoDO.setAssignee(jiraTaskBodyBO.getAssignee());
        jiraBasicInfoDO.setJiratype(jiraTaskBodyBO.getJiraType());
        jiraBasicInfoDO.setDescription(jiraTaskBodyBO.getIssueName());
        if (jiraTaskBodyBO.getJiraType().equals("测试主任务")) {
            jiraBasicInfoDO.setDepartment("产品测试团队");
        } else if (jiraTaskBodyBO.getJiraType().equals("测试子任务")) {
            jiraBasicInfoDO.setDepartment("产品测试团队");
        } else if (jiraTaskBodyBO.getJiraType().equals("开发子任务")) {
            jiraBasicInfoDO.setDepartment(department);
        } else {
            jiraBasicInfoDO.setDepartment(jiraTaskBodyBO.getDepartment());
        }
        jiraBasicInfoDO.setPlanstarttime(jiraTaskBodyBO.getPlanStartTime());
        jiraBasicInfoDO.setPlanendtime(jiraTaskBodyBO.getPlanEndTime());
        JiraBasicInfoDO jiraBasicInfoDO1 = jiraBasicInfoDao.get(jiraTaskBodyBO.getJiraKey());
        if (JudgeUtils.isNotNull(jiraBasicInfoDO1)) {
            jiraBasicInfoDao.update(jiraBasicInfoDO);
        } else {
            jiraBasicInfoDao.insert(jiraBasicInfoDO);
        }


        return jiraTaskBodyBO;
    }

    @Async
    void registerWorklogs(JiraTaskBodyBO jiraTaskBodyBO) {
        List<JiraWorklogBO> worklogs = JiraUtil.getWorklogs(jiraTaskBodyBO);
        for (int i = 0; i < worklogs.size(); i++) {
            if(!worklogs.get(i).getActive()){
                continue;
            }
            JiraWorklogDO jiraWorklogDO = new JiraWorklogDO();
            jiraWorklogDO.setJiraworklogkey(worklogs.get(i).getJiraWorklogKey());
            jiraWorklogDO.setIssuekey(jiraTaskBodyBO.getJiraKey());
            jiraWorklogDO.setName(worklogs.get(i).getName());
            jiraWorklogDO.setDisplayname(worklogs.get(i).getDisplayname());
            jiraWorklogDO.setComment(worklogs.get(i).getComment());
            jiraWorklogDO.setCreatedtime(worklogs.get(i).getCreatedtime());
            jiraWorklogDO.setUpdatedtime(worklogs.get(i).getUpdatedtime());
            jiraWorklogDO.setStartedtime(worklogs.get(i).getStartedtime());
            jiraWorklogDO.setTimespnet(worklogs.get(i).getTimespnet());
            JiraWorklogDO jiraWorklogDO1 = jiraWorklogDao.get(worklogs.get(i).getJiraWorklogKey());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String selectTime = DateUtil.date2String(new Date(), "yyyy-MM-dd");
            String week = DateUtil.testDate(selectTime);
            //如果是星期一则可以重新登记星期五的数据
            if(week.equals("星期一")){
               int betweenDate = 0;
                try {
                    Date d1 = sdf.parse(StringUtils.substring(jiraWorklogDO.getCreatedtime().trim(), 0, 10));
                    //登记工时开始时间
                    Date d2 = sdf.parse(StringUtils.substring(jiraWorklogDO.getStartedtime().trim(), 0, 10));
                    betweenDate = (int) (d1.getTime() - d2.getTime()) / (3*60 * 60 * 24 * 1000);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //周一时创建时间和工作登记开始时间超过四天则不计算
                if (betweenDate > 4) {
                    continue;
                }
            }else{
                int betweenDate = 0;
                try {
                    Date d1 = sdf.parse(StringUtils.substring(jiraWorklogDO.getCreatedtime().trim(), 0, 10));
                    Date d2 = sdf.parse(StringUtils.substring(jiraWorklogDO.getStartedtime().trim(), 0, 10));
                    betweenDate = (int) (d1.getTime() - d2.getTime()) / (60 * 60 * 24 * 1000);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //非周一  创建时间和工作登记开始时间超过1天则不计算
                if (betweenDate > 1) {
                    continue;
                }
            }
            //如果工时信息未被登记 则新登记，若已登记则需要判断这条信息创建时间和 工作开始时间的差时是否大于2天
            if (JudgeUtils.isNotNull(jiraWorklogDO1)) {
                int betweenDate=0;
                try {
                    Date d1 = sdf.parse(StringUtils.substring(jiraWorklogDO.getCreatedtime().trim(), 0, 10));
                    Date d2 = sdf.parse(StringUtils.substring(jiraWorklogDO.getStartedtime().trim(), 0, 10));
                    betweenDate = (int) (d1.getTime() - d2.getTime()) / (60 * 60 * 24 * 1000);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (betweenDate<2) {
                    jiraWorklogDao.update(jiraWorklogDO);
                }
            } else {
                jiraWorklogDao.insert(jiraWorklogDO);
            }
            String date1 = StringUtils.substring(LocalDateTime.now().toString().trim(), 0, 10);
            String date2 = StringUtils.substring(jiraWorklogDO.getUpdatedtime().trim(), 0, 10);
            int betweenDate=0;
            try {
                Date d1 = sdf.parse(StringUtils.substring(jiraWorklogDO.getCreatedtime().trim(), 0, 10));
                Date d2 = sdf.parse(StringUtils.substring(jiraWorklogDO.getStartedtime().trim(), 0, 10));
                betweenDate = (int) (d1.getTime() - d2.getTime()) / (60 * 60 * 24 * 1000);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (betweenDate<2) {
                WorkingHoursDO workingHoursDO = new WorkingHoursDO();
                workingHoursDO.setJiraworklogkey(jiraWorklogDO.getJiraworklogkey());
                //需求名
                workingHoursDO.setSubtaskname(jiraTaskBodyBO.getIssueName());
                workingHoursDO.setIssuekey(jiraWorklogDO.getIssuekey());
                workingHoursDO.setName(jiraWorklogDO.getName());

                workingHoursDO.setDisplayname(jiraWorklogDO.getDisplayname());
                workingHoursDO.setTimespnet(jiraWorklogDO.getTimespnet());
                if (StringUtils.isNotBlank(jiraTaskBodyBO.getDepartment())) {
                    workingHoursDO.setDevpLeadDept(jiraTaskBodyBO.getDepartment());
                } else {
                    UserDO userDO = userExtDao.getUserByUserFullName(jiraWorklogDO.getDisplayname());
                    workingHoursDO.setDevpLeadDept(userDO.getDepartment());
                }
                workingHoursDO.setComment(jiraWorklogDO.getComment());
                //工作备注不为空   且是在测试子任务下
                if(JudgeUtils.isNotBlank(jiraWorklogDO.getComment())){
                    if(jiraTaskBodyBO.getJiraType().equals("测试子任务")){
                        //判断需求名称是否带有 【测试案例编写】 如果有 则查看是否有编写了测试案例编写数
                        if(jiraTaskBodyBO.getIssueName().indexOf("【测试案例编写】")!=-1){
                            String[] split = jiraWorklogDO.getComment().split("#");
                            Pattern pattern = compile("^[-\\+]?[\\d]*$");
                            for(int j=0;j<split.length-1;j++) {
                                if (split[j].equals("测试案例编写数") && pattern.matcher(split[j+1]).matches()) {
                                    workingHoursDO.setCaseWritingNumber(Integer.parseInt(split[2]));
                                }
                            }
                        }
                        //判断需求名称是否带有 【测试案例执行】 如果有 则查看是否有编写了测试案例执行数和测试案例完成数
                        if(jiraTaskBodyBO.getIssueName().indexOf("【测试案例执行】")!=-1){
                            String[] split = jiraWorklogDO.getComment().split("#");
                            Pattern pattern = compile("^[-\\+]?[\\d]*$");
                            for(int j=0;j<split.length-1;j++){
                                if(split[j].equals("测试案例执行数")&&pattern.matcher(split[j+1]).matches()){
                                    workingHoursDO.setCaseExecutionNumber(Integer.parseInt(split[j+1]));
                                }
                                if(split[j].equals("测试案例完成数")&&pattern.matcher(split[j+1]).matches()){
                                    workingHoursDO.setCaseCompletedNumber(Integer.parseInt(split[j+1]));
                                }
                            }
                        }
                    }

                }

                workingHoursDO.setCreatedtime(jiraWorklogDO.getCreatedtime());
                workingHoursDO.setStartedtime(jiraWorklogDO.getStartedtime());
                workingHoursDO.setUpdatedtime(jiraWorklogDO.getUpdatedtime());
                workingHoursDO.setEpickey(jiraTaskBodyBO.getEpicKey());
                workingHoursDO.setEpiccreator(jiraTaskBodyBO.getEpicCreator());
                workingHoursDO.setRegisterflag("N");
                UserInfoBO userbyLoginName = systemUserService.getUserbyLoginName(jiraWorklogDO.getName());
                workingHoursDO.setAssignmentDepartment(userbyLoginName.getDepartment());
                WorkingHoursDO workingHoursDO1 = iWorkingHoursDao.get(workingHoursDO.getJiraworklogkey());
                UserRoleDO userRoleDO = new UserRoleDO();
                userRoleDO.setUserNo(userbyLoginName.getUserNo());
                //测试
                List<UserRoleDO> userRoleDOS = new LinkedList<>();

                if (workingHoursDO.getDevpLeadDept().equals("产品测试团队")) {
                    workingHoursDO.setRoletype("测试人员");
                } else {
                    userRoleDO.setRoleId((long) 5002);
                    userRoleDOS = userRoleExtDao.find(userRoleDO);

                    if (JudgeUtils.isNotEmpty(userRoleDOS)) {
                        workingHoursDO.setRoletype("产品经理");
                    } else {
                        workingHoursDO.setRoletype("开发人员");
                    }
                }
                if (JudgeUtils.isNotNull(workingHoursDO1)) {
                    if (JudgeUtils.isNotBlank(workingHoursDO1.getRegisterflag()) && workingHoursDO1.getRegisterflag().equals("Y")) {
                        //  todo 如果已经登记则需要修改差值
                    }
                    iWorkingHoursDao.update(workingHoursDO);
                } else {
                    iWorkingHoursDao.insert(workingHoursDO);
                }
            }
        }

    }

}
