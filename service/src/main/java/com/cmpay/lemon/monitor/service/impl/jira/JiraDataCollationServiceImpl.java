package com.cmpay.lemon.monitor.service.impl.jira;

import com.cmpay.lemon.common.utils.JudgeUtils;
import com.cmpay.lemon.monitor.bo.DemandBO;
import com.cmpay.lemon.monitor.bo.jira.JiraSubtasksBO;
import com.cmpay.lemon.monitor.bo.jira.JiraTaskBodyBO;
import com.cmpay.lemon.monitor.bo.jira.JiraWorklogBO;
import com.cmpay.lemon.monitor.dao.*;
import com.cmpay.lemon.monitor.entity.*;
import com.cmpay.lemon.monitor.service.SystemUserService;
import com.cmpay.lemon.monitor.service.jira.JiraDataCollationService;
import com.cmpay.lemon.monitor.service.jira.JiraOperationService;
import com.cmpay.lemon.monitor.utils.jira.JiraUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
    @Async
    @Override
    public void getIssueModifiedWithinOneDay() {
        List<JiraTaskBodyBO> jiraTaskBodyBOList = new LinkedList<>();
        int i=0;
        while (true){
            List<JiraTaskBodyBO> jiraTaskBodyBOS = JiraUtil.batchQueryIssuesModifiedWithinOneDay(i);
            if(JudgeUtils.isEmpty(jiraTaskBodyBOS)){
                break;
            }
            jiraTaskBodyBOList.addAll(jiraTaskBodyBOS);
            i=i+50;
        }
        if(JudgeUtils.isNotEmpty(jiraTaskBodyBOList)){
            jiraTaskBodyBOList.forEach(m->{
                    try {
                        JiraTaskBodyBO jiraTaskBodyBO = JiraUtil.GetIssue(m.getJiraKey());
                        this.registerJiraBasicInfo(jiraTaskBodyBO);
                        this.registerWorklogs(jiraTaskBodyBO);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            });
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
        jiraBasicInfoDO.setJiratype(jiraTaskBodyBO.getJiraType());
        jiraBasicInfoDO.setDescription(jiraTaskBodyBO.getIssueName());
        jiraBasicInfoDO.setEpickey(jiraTaskBodyBO.getEpicKey());
        jiraBasicInfoDO.setParenttaskkey(jiraTaskBodyBO.getParentTaskKey());
        if(JudgeUtils.isBlank(jiraTaskBodyBO.getEpicKey())){
            JiraBasicInfoDO jiraBasicInfoDO1 = new JiraBasicInfoDO();
            jiraBasicInfoDO1.setJirakey(jiraTaskBodyBO.getParentTaskKey());
            List<JiraBasicInfoDO> jiraBasicInfoDOS = jiraBasicInfoDao.find(jiraBasicInfoDO1);
            jiraBasicInfoDO.setEpickey(jiraBasicInfoDOS.get(0).getEpickey());
            jiraTaskBodyBO.setEpicKey(jiraBasicInfoDOS.get(0).getEpickey());
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
            JiraWorklogDO jiraWorklogDO = new JiraWorklogDO();
            jiraWorklogDO.setJiraworklogkey(worklogs.get(i).getJiraWorklogKey());
            jiraWorklogDO.setIssuekey(jiraTaskBodyBO.getJiraKey());
            jiraWorklogDO.setName(worklogs.get(i).getName());
            jiraWorklogDO.setDisplayname(worklogs.get(i).getDisplayname());
           // jiraWorklogDO.setComment(worklogs.get(i).getComment());
            jiraWorklogDO.setCreatedtime(worklogs.get(i).getCreatedtime());
            jiraWorklogDO.setUpdatedtime(worklogs.get(i).getUpdatedtime());
            jiraWorklogDO.setStartedtime(worklogs.get(i).getStartedtime());
            jiraWorklogDO.setTimespnet(worklogs.get(i).getTimespnet());
            JiraWorklogDO jiraWorklogDO1 = jiraWorklogDao.get(worklogs.get(i).getJiraWorklogKey());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            int betweenDate=0;
            try {
                Date d1 = sdf.parse(StringUtils.substring(jiraWorklogDO.getCreatedtime().trim(), 0, 10));
                Date d2 = sdf.parse(StringUtils.substring(jiraWorklogDO.getStartedtime().trim(), 0, 10));
                 betweenDate = (int) (d1.getTime() - d2.getTime()) / (60 * 60 * 24 * 1000);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(betweenDate>1){
                continue;
            }

            if (JudgeUtils.isNotNull(jiraWorklogDO1)) {
                String date1 = StringUtils.substring(jiraWorklogDO.getStartedtime().trim(), 0, 10);
                String date2 = StringUtils.substring(jiraWorklogDO.getUpdatedtime().trim(), 0, 10);
                if (date1.equals(date2)) {
                    jiraWorklogDao.update(jiraWorklogDO);
                }
            } else {
                jiraWorklogDao.insert(jiraWorklogDO);
            }
            String date1 = StringUtils.substring(LocalDateTime.now().toString().trim(), 0, 10);
            String date2 = StringUtils.substring(jiraWorklogDO.getUpdatedtime().trim(), 0, 10);
            if (date1.equals(date2)) {
                WorkingHoursDO workingHoursDO = new WorkingHoursDO();
                workingHoursDO.setJiraworklogkey(jiraWorklogDO.getJiraworklogkey());
                //需求名
                workingHoursDO.setSubtaskname(jiraTaskBodyBO.getIssueName());
                workingHoursDO.setIssuekey(jiraWorklogDO.getIssuekey());
                workingHoursDO.setName(jiraWorklogDO.getName());

                workingHoursDO.setDisplayname(jiraWorklogDO.getDisplayname());
                workingHoursDO.setTimespnet(jiraWorklogDO.getTimespnet());
                //workingHoursDO.setAssignmentDepartment();
                if (StringUtils.isNotBlank(jiraTaskBodyBO.getDepartment())) {
                    workingHoursDO.setDevpLeadDept(jiraTaskBodyBO.getDepartment());
                } else {
                    UserDO userDO = userExtDao.getUserByUserFullName(jiraWorklogDO.getDisplayname());
                    workingHoursDO.setDevpLeadDept(userDO.getDepartment());
                }
                workingHoursDO.setComment(jiraWorklogDO.getComment());
                workingHoursDO.setCreatedtime(jiraWorklogDO.getCreatedtime());
                workingHoursDO.setStartedtime(jiraWorklogDO.getStartedtime());
                workingHoursDO.setUpdatedtime(jiraWorklogDO.getUpdatedtime());
                workingHoursDO.setEpickey(jiraTaskBodyBO.getEpicKey());
                workingHoursDO.setRegisterflag("N");
                workingHoursDO.setAssignmentDepartment(systemUserService.getDepartmentByUser(jiraWorklogDO.getName()));
                WorkingHoursDO workingHoursDO1 = iWorkingHoursDao.get(workingHoursDO.getJiraworklogkey());
                if (JudgeUtils.isNotNull(workingHoursDO1)) {
                    if(JudgeUtils.isNotBlank(workingHoursDO1.getRegisterflag())&&workingHoursDO1.getRegisterflag().equals("Y")){
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
