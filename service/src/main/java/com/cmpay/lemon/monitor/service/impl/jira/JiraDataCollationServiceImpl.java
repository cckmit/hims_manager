package com.cmpay.lemon.monitor.service.impl.jira;

import com.cmpay.lemon.common.utils.JudgeUtils;
import com.cmpay.lemon.common.utils.StringUtils;
import com.cmpay.lemon.monitor.bo.DemandBO;
import com.cmpay.lemon.monitor.bo.jira.JiraSubtasksBO;
import com.cmpay.lemon.monitor.bo.jira.JiraTaskBodyBO;
import com.cmpay.lemon.monitor.bo.jira.JiraWorklogBO;
import com.cmpay.lemon.monitor.dao.*;
import com.cmpay.lemon.monitor.entity.*;
import com.cmpay.lemon.monitor.service.jira.JiraDataCollationService;
import com.cmpay.lemon.monitor.service.jira.JiraOperationService;
import com.cmpay.lemon.monitor.utils.jira.JiraUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    public void getEpicRelatedTasks(DemandBO demandBO) {
        demandBO.setReqInnerSeq("XQ00003707");
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
        demandJiraDevelopMasterTaskDOS.forEach(m-> JIRAKeys.add(m.getJiraKey()));
        //根据key查找对应数据
        JIRAKeys.forEach(m->{
            JiraTaskBodyBO jiraTaskBodyBO = registerJiraBasicInfo(m,null);
            List<JiraSubtasksBO> subtasks = JiraUtil.getSubtasks(jiraTaskBodyBO);
            if(JudgeUtils.isNotEmpty(subtasks)){
                for(int i=0;i<subtasks.size();i++){
                    JiraTaskBodyBO jiraTaskBodyBO1 = this.registerJiraBasicInfo(subtasks.get(i).getSubtaskkey(), jiraTaskBodyBO.getDepartment());
                    DemandJiraSubtaskDO demandJiraSubtaskDO = new DemandJiraSubtaskDO();
                    demandJiraSubtaskDO.setSubtaskkey(jiraTaskBodyBO1.getJiraKey());
                    demandJiraSubtaskDO.setAssignee(jiraTaskBodyBO1.getAssignee());
                    demandJiraSubtaskDO.setSubtaskname(jiraTaskBodyBO1.getIssueName());
                    demandJiraSubtaskDO.setParenttaskkey(jiraTaskBodyBO.getJiraKey());
                    DemandJiraSubtaskDO demandJiraSubtaskDO1 = demandJiraSubtaskDao.get(jiraTaskBodyBO1.getJiraKey());
                    if(JudgeUtils.isNotNull(demandJiraSubtaskDO1)){
                        demandJiraSubtaskDao.update(demandJiraSubtaskDO);
                    }else{
                        demandJiraSubtaskDao.insert(demandJiraSubtaskDO);
                    }
                    this.registerWorklogs(jiraTaskBodyBO1);
                }
            }
            this.registerWorklogs(jiraTaskBodyBO);
        });
    }

    private JiraTaskBodyBO registerJiraBasicInfo(String m,String department) {
        JiraTaskBodyBO jiraTaskBodyBO = JiraUtil.GetIssue(m);
        JiraBasicInfoDO jiraBasicInfoDO = new JiraBasicInfoDO();
        jiraBasicInfoDO.setJirakey(jiraTaskBodyBO.getJiraKey());
        jiraBasicInfoDO.setAggregatetimespent(jiraTaskBodyBO.getAggregatetimespent());
        jiraBasicInfoDO.setTimespent(jiraTaskBodyBO.getTimespent());
        jiraBasicInfoDO.setAssignee(jiraTaskBodyBO.getAssignee());
        jiraBasicInfoDO.setJiratype(jiraTaskBodyBO.getJiraType());
        jiraBasicInfoDO.setDescription(jiraTaskBodyBO.getIssueName());
        if(jiraTaskBodyBO.getJiraType().equals("测试主任务")){
            jiraBasicInfoDO.setDepartment("产品测试部");
            jiraBasicInfoDO.setDescription("问题描述");
        }else if(jiraTaskBodyBO.getJiraType().equals("测试子任务")){

        }else if(jiraTaskBodyBO.getJiraType().equals("开发子任务")){
            jiraBasicInfoDO.setDepartment(department);
        }else {
            jiraBasicInfoDO.setDepartment(jiraTaskBodyBO.getDepartment());
        }
        jiraBasicInfoDO.setPlanstarttime(jiraTaskBodyBO.getPlanStartTime());
        jiraBasicInfoDO.setPlanendtime(jiraTaskBodyBO.getPlanEndTime());
        JiraBasicInfoDO jiraBasicInfoDO1 = jiraBasicInfoDao.get(jiraTaskBodyBO.getJiraKey());
        if(JudgeUtils.isNotNull(jiraBasicInfoDO1)){
            jiraBasicInfoDao.update(jiraBasicInfoDO);
        }else{
            jiraBasicInfoDao.insert(jiraBasicInfoDO);
        }


        return jiraTaskBodyBO;
    }
    private void registerWorklogs(JiraTaskBodyBO jiraTaskBodyBO) {
        List<JiraWorklogBO> worklogs = JiraUtil.getWorklogs(jiraTaskBodyBO);
        System.err.println(jiraTaskBodyBO.getWorklogs());

        for(int i=0 ;i<worklogs.size();i++){
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
            System.out.println(worklogs.get(i).getJiraWorklogKey());
            if(JudgeUtils.isNotNull(jiraWorklogDO1)){
                String date1 = StringUtils.substring(jiraWorklogDO.getStartedtime().trim(), 0,10);
                String date2 = StringUtils.substring(jiraWorklogDO.getUpdatedtime().trim(), 0,10);
                if(date1.equals(date2)){
                    jiraWorklogDao.update(jiraWorklogDO);
                }
            }else{
                jiraWorklogDao.insert(jiraWorklogDO);
            }


        }

    }

}
