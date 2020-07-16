package com.cmpay.lemon.monitor.service.impl.jira;

import com.cmpay.lemon.common.Env;
import com.cmpay.lemon.common.exception.BusinessException;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.common.utils.JudgeUtils;
import com.cmpay.lemon.framework.utils.LemonUtils;
import com.cmpay.lemon.monitor.bo.DemandBO;
import com.cmpay.lemon.monitor.bo.jira.*;
import com.cmpay.lemon.monitor.dao.*;
import com.cmpay.lemon.monitor.entity.*;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.jira.JiraOperationService;
import com.cmpay.lemon.monitor.utils.DateUtil;
import com.cmpay.lemon.monitor.utils.ReadExcelUtils;
import com.cmpay.lemon.monitor.utils.jira.JiraUtil;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class JiraOperationServiceImpl implements JiraOperationService {
    @Autowired
    IJiraDepartmentDao jiraDepartmentDao;
    @Autowired
    IDemandJiraDao demandJiraDao;
    @Autowired
    private IUserExtDao iUserDao;
    @Autowired
    IDemandJiraDevelopMasterTaskDao demandJiraDevelopMasterTaskDao;
    @Autowired
    IDemandJiraSubtaskDao demandJiraSubtaskDao;
    //jira项目类型 和包项目 jira编号10106(测试环境)
    final static Integer PROJECTTYPE_CMPAY_dev = 10106;
    //jira项目类型 和包项目 jira编号10100
    final static Integer PROJECTTYPE_CMPAY = 10100;
    //jira项目类型 资金归集项目 jira编号
    final static Integer PROJECTTYPE_FCPT = 10104;
    //jira项目类型 团体缴费项目 jira编号
    final static Integer PROJECTTYPE_GPPT = 10102;
    //jira项目类型 团体缴费项目 jira编号
    final static Integer PROJECTTYPE_CSPT = 10103;
    //EPIC任务 jira编号
    final static Integer ISSUETYPE_EPIC = 10000;
    //开发主任务 jira编号
    final static Integer ISSUETYPE_DEVELOPMAINTASK = 10100;
    //测试主任务 jira编号
    final static Integer ISSUETYPE_TESTMAINTASK = 10102;
    //测试子任务 jira编号
    final static Integer ISSUETYPE_TESTSUBTASK = 10103;


    //Epic任务
    final static String EPIC = "Epic";
    //开发主任务
    final static String DEVELOPMAINTASK = "开发主任务";
    //测试主任务
    final static String TESTMAINTASK = "测试主任务";
    //业务失败标识
    final static String FAIL = "fail";
    //业务成功标识
    final static String SUCCESS = "success";
    //测试部门
    final static String TESTINGDIVISION = "产品测试团队";
    //部门参数未匹配
    final static String DEPARTMENTDIDNOTMATCH = "主导部门错误(部门参数未匹配)";

    //jira返回业务成功时状态码 201
    final static int JIRA_SUCCESS = 201;

    @Async
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createEpic(DemandBO demandBO) {
        DateFormat format1 = new SimpleDateFormat("yyyy-MM");
        Date date1 = null;
        Date date2 = null;
        String str = "2020-05";
        // String转Date
        String reqImplMon = demandBO.getReqImplMon();
        try {
            date1 = format1.parse(reqImplMon);
            date2 = format1.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(date1.getTime()<=date2.getTime()){
            return;
        }

        //jira关联表中已有，并且状态为成功,则不再生成jira任务
        DemandJiraDO demandJiraDO1 = demandJiraDao.get(demandBO.getReqInnerSeq());
        if (JudgeUtils.isNotNull(demandJiraDO1) && demandJiraDO1.getCreateState().equals(SUCCESS)) {
            this.createMasterTask(demandBO, demandJiraDO1);
            return;
        }


        CreateIssueEpicRequestBO createIssueEpicRequestBO = new CreateIssueEpicRequestBO();
        createIssueEpicRequestBO.setSummary(demandBO.getReqNm());
        createIssueEpicRequestBO.setDescription(demandBO.getReqDesc());
        //设置项目类型，不为以下三种则为和包项目。
        if ("资金归集项目组".equals(demandBO.getDevpLeadDept())) {
            createIssueEpicRequestBO.setProject(PROJECTTYPE_FCPT);
        } else if ("团体组织交费项目组".equals(demandBO.getDevpLeadDept())) {
            createIssueEpicRequestBO.setProject(PROJECTTYPE_GPPT);
        } else if ("客服中间层项目组".equals(demandBO.getDevpLeadDept())) {
            createIssueEpicRequestBO.setProject(PROJECTTYPE_CSPT);
        } else {
            if (LemonUtils.getEnv().equals(Env.SIT)) {
                createIssueEpicRequestBO.setProject(PROJECTTYPE_CMPAY);
            } else {
                createIssueEpicRequestBO.setProject(PROJECTTYPE_CMPAY_dev);
            }
        }

        createIssueEpicRequestBO.setIssueType(ISSUETYPE_EPIC);
        createIssueEpicRequestBO.setDevpLeadDept(demandBO.getDevpLeadDept());
        createIssueEpicRequestBO.setDescription(demandBO.getReqDesc());
        createIssueEpicRequestBO.setReqInnerSeq(demandBO.getReqInnerSeq());
        //获取部门管理人员
        JiraDepartmentDO jiraDepartmentDO = jiraDepartmentDao.get(demandBO.getDevpLeadDept());
        //未获得部门管理人员则为配置jira对应数据错误
        if (JudgeUtils.isNull(jiraDepartmentDO)) {
            DemandJiraDO demandJiraDO = new DemandJiraDO();
            demandJiraDO.setCreatTime(LocalDateTime.now());
            demandJiraDO.setReqInnerSeq(demandBO.getReqInnerSeq());
            demandJiraDO.setReqNm(demandBO.getReqNm());
            demandJiraDO.setCreateState(FAIL);
            demandJiraDO.setRemarks(DEPARTMENTDIDNOTMATCH);
            demandJiraDao.insert(demandJiraDO);
            return;
        }
        createIssueEpicRequestBO.setManager(jiraDepartmentDO.getManagerjiranm());
        Response response = JiraUtil.CreateIssue(createIssueEpicRequestBO);
        //如果返回状态码是成功则登记jira关联表
        if (response.getStatusCode() == JIRA_SUCCESS) {
            CreateIssueResponseBO createIssueResponseBO = response.getBody().as(CreateIssueResponseBO.class);
            DemandJiraDO demandJiraDO = new DemandJiraDO();
            demandJiraDO.setCreatTime(LocalDateTime.now());
            demandJiraDO.setJiraId(createIssueResponseBO.getId());
            demandJiraDO.setJiraKey(createIssueResponseBO.getKey());
            demandJiraDO.setReqInnerSeq(demandBO.getReqInnerSeq());
            demandJiraDO.setReqNm(demandBO.getReqNm());
            demandJiraDO.setAssignmentDepartment(demandBO.getDevpLeadDept());
            demandJiraDO.setIssueType(EPIC);
            demandJiraDO.setCreateState(SUCCESS);
            demandJiraDO.setRemarks("");
            //若是jira关联表已存在该项目，则更新
            DemandJiraDO demandJiraDO2 = demandJiraDao.get(demandJiraDO.getReqInnerSeq());
            if (JudgeUtils.isNull(demandJiraDO2)) {
                demandJiraDao.insert(demandJiraDO);
            } else {
                demandJiraDao.update(demandJiraDO);
            }
            //创建主任务
            this.createMasterTask(demandBO, demandJiraDO);

        } else {
            DemandJiraDO demandJiraDO = new DemandJiraDO();
            demandJiraDO.setCreatTime(LocalDateTime.now());
            demandJiraDO.setReqInnerSeq(demandBO.getReqInnerSeq());
            demandJiraDO.setReqNm(demandBO.getReqNm());
            demandJiraDO.setCreateState(FAIL);
            demandJiraDO.setRemarks(response.getBody().print());
            DemandJiraDO demandJiraDO2 = demandJiraDao.get(demandJiraDO.getReqInnerSeq());
            if (JudgeUtils.isNull(demandJiraDO2)) {
                demandJiraDao.insert(demandJiraDO);
            } else {
                demandJiraDao.update(demandJiraDO);
            }
        }
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void CreateJiraMasterTask(String devpCoorDept, DemandBO demandBO, DemandJiraDO epicDemandJiraDO, String taskType) {
        DemandJiraDevelopMasterTaskDO demandJiraDevelopMasterTaskDO1 = demandJiraDevelopMasterTaskDao.get(epicDemandJiraDO.getJiraKey() + "_" + devpCoorDept + "_" + taskType);
        if (JudgeUtils.isNotNull(demandJiraDevelopMasterTaskDO1) && demandJiraDevelopMasterTaskDO1.getCreateState().equals(SUCCESS)) {
            return;
        }
        //获取部门管理人员
        JiraDepartmentDO jiraDepartmentDO = jiraDepartmentDao.get(devpCoorDept);
        //未获得部门管理人员则为配置jira对应数据错误
        if (JudgeUtils.isNull(jiraDepartmentDO)) {
            DemandJiraDevelopMasterTaskDO demandJiraDevelopMasterTaskDO = new DemandJiraDevelopMasterTaskDO();
            demandJiraDevelopMasterTaskDO.setCreatTime(LocalDateTime.now());
            //拼凑jira主任务名
            demandJiraDevelopMasterTaskDO.setMasterTaskKey(epicDemandJiraDO.getJiraKey() + "_" + devpCoorDept + "_" + taskType);
            demandJiraDevelopMasterTaskDO.setReqNm(demandBO.getReqNm());
            demandJiraDevelopMasterTaskDO.setCreateState(FAIL);
            demandJiraDevelopMasterTaskDO.setRemarks(DEPARTMENTDIDNOTMATCH);
            demandJiraDevelopMasterTaskDao.insert(demandJiraDevelopMasterTaskDO);
            return;
        }
        CreateIssueMainTaskRequestBO createMainTaskRequestBO = new CreateIssueMainTaskRequestBO();
        createMainTaskRequestBO.setEpicKey(epicDemandJiraDO.getJiraKey());
        //拼凑jira主任务名称  部门+任务名
        createMainTaskRequestBO.setSummary("【" + devpCoorDept + "】" + demandBO.getReqNm());
        createMainTaskRequestBO.setDescription(demandBO.getReqDesc());
        //设置项目为和包项目，问题类型主任务
        if (taskType.equals(DEVELOPMAINTASK)) {
            createMainTaskRequestBO.setIssueType(ISSUETYPE_DEVELOPMAINTASK);
        } else {
            createMainTaskRequestBO.setIssueType(ISSUETYPE_TESTMAINTASK);
        }

        if ("资金归集项目组".equals(demandBO.getDevpLeadDept())) {
            createMainTaskRequestBO.setProject(PROJECTTYPE_FCPT);
        } else if ("团体组织交费项目组".equals(demandBO.getDevpLeadDept())) {
            createMainTaskRequestBO.setProject(PROJECTTYPE_GPPT);
        } else if ("客服中间层项目组".equals(demandBO.getDevpLeadDept())) {
            createMainTaskRequestBO.setProject(PROJECTTYPE_CSPT);
        } else {
            if (LemonUtils.getEnv().equals(Env.SIT)) {
                createMainTaskRequestBO.setProject(PROJECTTYPE_CMPAY);
            } else {
                createMainTaskRequestBO.setProject(PROJECTTYPE_CMPAY_dev);
            }
        }

        createMainTaskRequestBO.setDevpLeadDept(devpCoorDept);
        createMainTaskRequestBO.setDescription(demandBO.getReqDesc());
        createMainTaskRequestBO.setReqInnerSeq(demandBO.getReqInnerSeq());
        createMainTaskRequestBO.setManager(jiraDepartmentDO.getManagerjiranm());
        //创建子任务
        Response response = JiraUtil.CreateIssue(createMainTaskRequestBO);
        if (response.getStatusCode() == JIRA_SUCCESS)  {
            CreateIssueResponseBO createIssueResponseBO = response.getBody().as(CreateIssueResponseBO.class);
            DemandJiraDevelopMasterTaskDO demandJiraDevelopMasterTaskDO = new DemandJiraDevelopMasterTaskDO();
            demandJiraDevelopMasterTaskDO.setCreatTime(LocalDateTime.now());
            demandJiraDevelopMasterTaskDO.setJiraId(createIssueResponseBO.getId());
            demandJiraDevelopMasterTaskDO.setJiraKey(createIssueResponseBO.getKey());
            demandJiraDevelopMasterTaskDO.setMasterTaskKey(epicDemandJiraDO.getJiraKey() + "_" + devpCoorDept + "_" + taskType);
            demandJiraDevelopMasterTaskDO.setReqNm(demandBO.getReqNm());
            demandJiraDevelopMasterTaskDO.setAssignmentDepartment(devpCoorDept);
            demandJiraDevelopMasterTaskDO.setIssueType(taskType);
            demandJiraDevelopMasterTaskDO.setCreateState(SUCCESS);
            //业务成功，置空备注
            demandJiraDevelopMasterTaskDO.setRemarks("");
            //设置关联Epic
            demandJiraDevelopMasterTaskDO.setRelevanceEpic(epicDemandJiraDO.getJiraKey());
            //若是jira关联表已存在该项目，则更新
            DemandJiraDevelopMasterTaskDO demandJiraDevelopMasterTaskDO2 = demandJiraDevelopMasterTaskDao.get(demandJiraDevelopMasterTaskDO.getMasterTaskKey());
            if (JudgeUtils.isNull(demandJiraDevelopMasterTaskDO2)) {
                demandJiraDevelopMasterTaskDao.insert(demandJiraDevelopMasterTaskDO);
            } else {
                demandJiraDevelopMasterTaskDao.update(demandJiraDevelopMasterTaskDO);
            }

            if(taskType.equals(TESTMAINTASK)){
                CreateIssueTestSubtaskRequestBO createIssueTestSubtaskRequestBO = new CreateIssueTestSubtaskRequestBO();
                createIssueTestSubtaskRequestBO.setIssueType(ISSUETYPE_TESTSUBTASK);
                if (LemonUtils.getEnv().equals(Env.SIT)) {
                    createIssueTestSubtaskRequestBO.setProject(PROJECTTYPE_CMPAY);
                } else {
                    createIssueTestSubtaskRequestBO.setProject(PROJECTTYPE_CMPAY_dev);
                }
                createIssueTestSubtaskRequestBO.setParentKey(createIssueResponseBO.getKey());
                createIssueTestSubtaskRequestBO.setAssignee(jiraDepartmentDO.getManagerjiranm());
                String selectTime = DateUtil.date2String(new Date(), "yyyy-MM-dd");
                createIssueTestSubtaskRequestBO.setStartTime(selectTime);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DATE, + 7);
                Date time = c.getTime();
                String preDay = sdf.format(time);
                createIssueTestSubtaskRequestBO.setEndTime(preDay);

                createIssueTestSubtaskRequestBO.setSummary("【测试案例编写】"+demandBO.getReqNm());
                createIssueTestSubtaskRequestBO.setDescription("测试案例编写");
                Response response1 = JiraUtil.CreateIssue(createIssueTestSubtaskRequestBO);
                if (response1.getStatusCode() == JIRA_SUCCESS) {
                    CreateIssueResponseBO as = response1.getBody().as(CreateIssueResponseBO.class);
                    DemandJiraSubtaskDO demandJiraSubtaskDO = new DemandJiraSubtaskDO();
                    demandJiraSubtaskDO.setSubtaskkey(as.getKey());
                    demandJiraSubtaskDO.setParenttaskkey(createIssueTestSubtaskRequestBO.getParentKey());
                    demandJiraSubtaskDO.setSubtaskname(createIssueTestSubtaskRequestBO.getSummary());
                    demandJiraSubtaskDao.insert(demandJiraSubtaskDO);
                }


                createIssueTestSubtaskRequestBO.setSummary("【测试案例评审】"+demandBO.getReqNm());
                createIssueTestSubtaskRequestBO.setDescription("测试案例评审");
                response1 = JiraUtil.CreateIssue(createIssueTestSubtaskRequestBO);
                if (response1.getStatusCode() == JIRA_SUCCESS) {
                    CreateIssueResponseBO as = response1.getBody().as(CreateIssueResponseBO.class);
                    DemandJiraSubtaskDO demandJiraSubtaskDO = new DemandJiraSubtaskDO();
                    demandJiraSubtaskDO.setSubtaskkey(as.getKey());
                    demandJiraSubtaskDO.setParenttaskkey(createIssueTestSubtaskRequestBO.getParentKey());
                    demandJiraSubtaskDO.setSubtaskname(createIssueTestSubtaskRequestBO.getSummary());
                    demandJiraSubtaskDao.insert(demandJiraSubtaskDO);
                }

                createIssueTestSubtaskRequestBO.setSummary("【测试案例执行】"+demandBO.getReqNm());
                createIssueTestSubtaskRequestBO.setDescription("测试用例执行");
                response1 = JiraUtil.CreateIssue(createIssueTestSubtaskRequestBO);
                if (response1.getStatusCode() == JIRA_SUCCESS) {
                    CreateIssueResponseBO as = response1.getBody().as(CreateIssueResponseBO.class);
                    DemandJiraSubtaskDO demandJiraSubtaskDO = new DemandJiraSubtaskDO();
                    demandJiraSubtaskDO.setSubtaskkey(as.getKey());
                    demandJiraSubtaskDO.setParenttaskkey(createIssueTestSubtaskRequestBO.getParentKey());
                    demandJiraSubtaskDO.setSubtaskname(createIssueTestSubtaskRequestBO.getSummary());
                    demandJiraSubtaskDao.insert(demandJiraSubtaskDO);
                }
            }

        } else {
            DemandJiraDevelopMasterTaskDO demandJiraDevelopMasterTaskDO = new DemandJiraDevelopMasterTaskDO();
            demandJiraDevelopMasterTaskDO.setCreatTime(LocalDateTime.now());
            demandJiraDevelopMasterTaskDO.setAssignmentDepartment(devpCoorDept);
            demandJiraDevelopMasterTaskDO.setMasterTaskKey(epicDemandJiraDO.getJiraKey() + "_" + devpCoorDept + "_" + taskType);
            demandJiraDevelopMasterTaskDO.setReqNm(demandBO.getReqNm());
            demandJiraDevelopMasterTaskDO.setCreateState(FAIL);
            demandJiraDevelopMasterTaskDO.setRemarks(response.getBody().print());
            //设置关联Epic
            demandJiraDevelopMasterTaskDO.setRelevanceEpic(epicDemandJiraDO.getJiraKey());
            DemandJiraDevelopMasterTaskDO demandJiraDevelopMasterTaskDO2 = demandJiraDevelopMasterTaskDao.get(demandJiraDevelopMasterTaskDO.getMasterTaskKey());
            if (JudgeUtils.isNull(demandJiraDevelopMasterTaskDO2)) {
                demandJiraDevelopMasterTaskDao.insert(demandJiraDevelopMasterTaskDO);
            } else {
                demandJiraDevelopMasterTaskDao.update(demandJiraDevelopMasterTaskDO);
            }
        }
    }


    @Async
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void batchCreateEpic(List<DemandDO> demandDOList) {
        demandDOList.forEach(m -> {
            DemandBO demandBO = BeanUtils.copyPropertiesReturnDest(new DemandBO(), m);
            this.createEpic(demandBO);
        });
    }

    @Override
    public void createMasterTask(DemandBO demandBO, DemandJiraDO demandJiraDO) {
        //创建开发部门链表
        List<String> developmentDepartmenList = new ArrayList<>();
        //若有开发配合部门
        if (demandBO.getDevpCoorDept() != null && demandBO.getDevpCoorDept() != "") {
            String[] split = demandBO.getDevpCoorDept().split(",");
            Arrays.stream(split).forEach(arr -> developmentDepartmenList.add(arr));
        }
        //添加开发主导部门
        developmentDepartmenList.add(demandBO.getDevpLeadDept());
        developmentDepartmenList.forEach(m -> {
            if (m.isEmpty()) {
                return;
            }
            //该部门不需要创建
            if (m.equals("行业拓展事业部")) {
                return;
            }
            //创建开发子任务
            this.CreateJiraMasterTask(m, demandBO, demandJiraDO, DEVELOPMAINTASK);
        });
        //添加测试主任务，这两个部门不需要测试部测试，不需要测试主任务
        if (!demandBO.getDevpLeadDept().equals("团体组织交费项目组") && !demandBO.getDevpLeadDept().equals("资金归集项目组")&& !demandBO.getDevpLeadDept().equals("客服中间层项目组")) {
            this.CreateJiraMasterTask(TESTINGDIVISION, demandBO, demandJiraDO, TESTMAINTASK);
        }
    }


    @Override
    public void getJiraIssue(List<DemandDO> demandDOList) {
        demandDOList.forEach(demandDO -> {
            this.jiraEpicKey(demandDO);
        });
    }

    @Async
    @Override
    public void jiraEpicKey(DemandDO demandDO) {
        //获取jira epic key
        DemandJiraDO demandJiraDO = demandJiraDao.get(demandDO.getReqInnerSeq());
        if (demandJiraDO == null) {
            return;
        }
        String epicKey = demandJiraDO.getJiraKey();

        //获取jira epic key获取测试主任务
        DemandJiraDevelopMasterTaskDO demandJiraDevelopMasterTaskDO = demandJiraDevelopMasterTaskDao.get(epicKey + "_产品测试团队_测试主任务");
        if (demandJiraDevelopMasterTaskDO == null) {
            return;
        }
        String jiraKey = demandJiraDevelopMasterTaskDO.getJiraKey();
        JiraTaskBodyBO jiraTaskBodyBO = JiraUtil.GetIssue(jiraKey);
        demandDO.setAssignee(jiraTaskBodyBO.getAssignee());
        demandDO.setPlanStartTime(jiraTaskBodyBO.getPlanStartTime());
        demandDO.setPlanEndTime(jiraTaskBodyBO.getPlanEndTime());
    }

    /*
     *开发主任务批量修改
     */
    @Async
    @Override
    public void jiraTestMainTaskBatchEdit(MultipartFile file) {
        List<JiraTaskBodyBO> jiraTaskBodyBOS = new ArrayList<>();

           File f = null;

        try {
            //MultipartFile转file
            String originalFilename = file.getOriginalFilename();
            //获取后缀名
            String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            if (suffix.equals("xls")) {
                suffix = ".xls";
            } else if (suffix.equals("xlsm") || suffix.equals("xlsx")) {
                suffix = ".xlsx";
            } else {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("文件类型错误!");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            f = File.createTempFile("tmp", suffix);
            file.transferTo(f);
            String filepath = f.getPath();
            //excel转java类
            ReadExcelUtils excelReader = new ReadExcelUtils(filepath);
            Map<Integer, Map<Integer, Object>> map = excelReader.readExcelContent();
            for (int i = 1; i <= map.size(); i++) {
                JiraTaskBodyBO jiraTaskBodyBO = new JiraTaskBodyBO();
                jiraTaskBodyBO.setReqInnerSeq(map.get(i).get(1).toString());
                if (!JudgeUtils.isBlank(map.get(i).get(19).toString().trim())) {
                    jiraTaskBodyBO.setAssignee(map.get(i).get(19).toString().trim());
                }
                if (!JudgeUtils.isBlank(map.get(i).get(20).toString())) {
                    jiraTaskBodyBO.setPlanStartTime(map.get(i).get(20).toString().trim());
                } else {
                    jiraTaskBodyBO.setPlanStartTime("");
                }
                if (!JudgeUtils.isBlank(map.get(i).get(21).toString())) {
                    jiraTaskBodyBO.setPlanEndTime(map.get(i).get(21).toString().trim());
                } else {
                    jiraTaskBodyBO.setPlanEndTime("");
                }
                jiraTaskBodyBOS.add(jiraTaskBodyBO);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
        } catch (Exception e) {
            e.printStackTrace();
            BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
        } finally {
            f.delete();
        }
        //讲所有需要修改的任务添加到这个list
        List<JiraTaskBodyBO> jiraTastBodyAllTestTesk = new ArrayList<>();
        //获取导入文件的需求的jirekey
        jiraTaskBodyBOS.forEach(m -> {
            DemandJiraDO demandJiraDO;
            try {
                demandJiraDO = demandJiraDao.get(m.getReqInnerSeq());
            } catch (Exception e) {
                return;
            }
            String epicKey = "";
            try {
                epicKey = demandJiraDO.getJiraKey();
            } catch (Exception e) {
                return;
            }
            //获取jira epic key获取测试主任务
            DemandJiraDevelopMasterTaskDO demandJiraDevelopMasterTaskDO = demandJiraDevelopMasterTaskDao.get(epicKey + "_产品测试团队_测试主任务");
            if (demandJiraDevelopMasterTaskDO == null) {
                return;
            }
            String jiraKey = demandJiraDevelopMasterTaskDO.getJiraKey();
            m.setJiraKey(jiraKey);
            if (m.getAssignee() != null) {
                UserDO userDO = new UserDO();
                userDO.setFullname(m.getAssignee());
                List<UserDO> userDOS = iUserDao.find(userDO);
                if (userDOS.isEmpty()) {
                    return;
                }
                m.setAssignee(userDOS.get(0).getUsername());
                userDO.setFullname(m.getAssignee());
            }
            jiraTastBodyAllTestTesk.add(m);
            DemandJiraSubtaskDO demandJiraSubtaskDO = new DemandJiraSubtaskDO();
            demandJiraSubtaskDO.setParenttaskkey(m.getJiraKey());


            List<DemandJiraSubtaskDO> demandJiraSubtaskDOS = demandJiraSubtaskDao.find(demandJiraSubtaskDO);

            //查询对应的三个测试子任务也同步修改
            if(JudgeUtils.isNotEmpty(demandJiraSubtaskDOS)){
                for(int i=0 ;i<demandJiraSubtaskDOS.size();i++){
                    JiraTaskBodyBO jiraTaskBodyBO = new JiraTaskBodyBO();
                    jiraTaskBodyBO.setJiraKey(demandJiraSubtaskDOS.get(i).getSubtaskkey());
                    jiraTaskBodyBO.setAssignee(m.getAssignee());
                    jiraTaskBodyBO.setPlanStartTime(m.getPlanStartTime());
                    jiraTaskBodyBO.setPlanEndTime(m.getPlanEndTime());
                    jiraTastBodyAllTestTesk.add(jiraTaskBodyBO);
                }
            }
        });
            //修改jira任务
        jiraTastBodyAllTestTesk.forEach(m->{
             JiraUtil.EditTheTestMainTask(m);
        });



    }


}
