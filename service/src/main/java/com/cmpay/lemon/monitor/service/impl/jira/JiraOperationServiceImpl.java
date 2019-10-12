package com.cmpay.lemon.monitor.service.impl.jira;

import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.common.utils.JudgeUtils;
import com.cmpay.lemon.monitor.bo.DemandBO;
import com.cmpay.lemon.monitor.bo.jira.CreateIssueEpicRequestBO;
import com.cmpay.lemon.monitor.bo.jira.CreateIssueMainTaskRequestBO;
import com.cmpay.lemon.monitor.bo.jira.CreateIssueResponseBO;
import com.cmpay.lemon.monitor.dao.IDemandJiraDao;
import com.cmpay.lemon.monitor.dao.IJiraDepartmentDao;
import com.cmpay.lemon.monitor.entity.DemandDO;
import com.cmpay.lemon.monitor.entity.DemandJiraDO;
import com.cmpay.lemon.monitor.entity.JiraDepartmentDO;
import com.cmpay.lemon.monitor.service.jira.JiraOperationService;
import com.cmpay.lemon.monitor.utils.jira.JiraUtil;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class JiraOperationServiceImpl implements JiraOperationService {
    @Autowired
    IJiraDepartmentDao jiraDepartmentDao;
    @Autowired
    IDemandJiraDao demandJiraDao;
    //jira项目类型 和包项目
    final static  Integer PROJECTTYPE_CMPAY=10009;

    final static  Integer ISSUETYPE_EPIC=10000;
    //开发主任务
    final static  Integer ISSUETYPE_DEVELOPMAINTASK=10005;
    //测试主任务
    final static  Integer ISSUETYPE_TESTMAINTASK=10006;
    @Override
    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public void createEpic(DemandBO demandBO)   {
        //jira关联表中已有，并且状态为成功
        DemandJiraDO demandJiraDO1 = demandJiraDao.get(demandBO.getReqInnerSeq());
        if(JudgeUtils.isNotNull(demandJiraDO1)&&demandJiraDO1.getCreateState().equals("success")){
            //todo 创建开发主任务
            this.createMasterTask(demandBO);
            //todo 创建测试主任务
            return;
        }
        CreateIssueEpicRequestBO createIssueEpicRequestBO = new CreateIssueEpicRequestBO();
        createIssueEpicRequestBO.setSummary(demandBO.getReqNm());
        createIssueEpicRequestBO.setDescription(demandBO.getReqDesc());
        //设置项目为和包项目，问题类型EPIC
        createIssueEpicRequestBO.setIssueType(ISSUETYPE_EPIC);
        createIssueEpicRequestBO.setProject(PROJECTTYPE_CMPAY);
        createIssueEpicRequestBO.setDevpLeadDept(demandBO.getDevpLeadDept());
        createIssueEpicRequestBO.setDescription(demandBO.getReqDesc());
        createIssueEpicRequestBO.setReqInnerSeq(demandBO.getReqInnerSeq());
        //获取部门管理人员
        JiraDepartmentDO jiraDepartmentDO = jiraDepartmentDao.get(demandBO.getDevpLeadDept());
        //未获得部门管理人员则为配置jira对应数据错误
        if(JudgeUtils.isNull(jiraDepartmentDO)){
            DemandJiraDO demandJiraDO = new DemandJiraDO();
            demandJiraDO.setCreatTime(LocalDateTime.now());
            demandJiraDO.setReqInnerSeq(demandBO.getReqInnerSeq());
            demandJiraDO.setReqNm(demandBO.getReqNm());
            demandJiraDO.setCreateState("fail");
            demandJiraDO.setRemarks("主导部门错误");
            demandJiraDao.insert(demandJiraDO);
            return;
        }
        createIssueEpicRequestBO.setManager(jiraDepartmentDO.getManagerjiranm());
        JiraUtil jiraUtil = new JiraUtil();
        Response response = jiraUtil.CreateIssue(createIssueEpicRequestBO);
        if(response.getStatusCode()==201) {
            CreateIssueResponseBO createIssueResponseBO = response.getBody().as(CreateIssueResponseBO.class);
            DemandJiraDO demandJiraDO = new DemandJiraDO();
            demandJiraDO.setCreatTime(LocalDateTime.now());
            demandJiraDO.setJiraId(createIssueResponseBO.getId());
            demandJiraDO.setJiraKey(createIssueResponseBO.getKey());
            demandJiraDO.setReqInnerSeq(demandBO.getReqInnerSeq());
            demandJiraDO.setReqNm(demandBO.getReqNm());
            demandJiraDO.setAssignmentDepartment(demandBO.getDevpLeadDept());
            demandJiraDO.setIssueType("Epic");
            demandJiraDO.setCreateState("success");
            //若是jira关联表已存在该项目，则更新
            DemandJiraDO demandJiraDO2 = demandJiraDao.get(demandJiraDO.getReqInnerSeq());
            if(JudgeUtils.isNull(demandJiraDO2)){
                demandJiraDao.insert(demandJiraDO);
            }else{
                demandJiraDao.update(demandJiraDO);
            }
            //todo 创建开发主任务
            this.createMasterTask(demandBO);
            //todo 创建测试主任务

        }else{
            DemandJiraDO demandJiraDO = new DemandJiraDO();
            demandJiraDO.setCreatTime(LocalDateTime.now());
            demandJiraDO.setReqInnerSeq(demandBO.getReqInnerSeq());
            demandJiraDO.setReqNm(demandBO.getReqNm());
            demandJiraDO.setCreateState("fail");
            demandJiraDO.setRemarks(response.getBody().print());
            DemandJiraDO demandJiraDO2 = demandJiraDao.get(demandJiraDO.getReqInnerSeq());
            if(JudgeUtils.isNull(demandJiraDO2)){
                demandJiraDao.insert(demandJiraDO);
            }else{
                demandJiraDao.update(demandJiraDO);
            }
        }
    }

    private void CreateMainTask(String devpCoorDept, DemandBO demandBO, DemandJiraDO epicDemandJiraDO) {

        CreateIssueMainTaskRequestBO createMainTaskRequestBO = new CreateIssueMainTaskRequestBO();
        createMainTaskRequestBO.setSummary(demandBO.getReqNm());
        createMainTaskRequestBO.setDescription(demandBO.getReqDesc());
        //设置项目为和包项目，问题类型EPIC
        createMainTaskRequestBO.setIssueType(ISSUETYPE_DEVELOPMAINTASK);
        createMainTaskRequestBO.setProject(PROJECTTYPE_CMPAY);
        createMainTaskRequestBO.setDevpLeadDept(devpCoorDept);
        createMainTaskRequestBO.setDescription(demandBO.getReqDesc());
        createMainTaskRequestBO.setReqInnerSeq(demandBO.getReqInnerSeq());
        JiraUtil jiraUtil = new JiraUtil();
        Response response = jiraUtil.CreateIssue(createMainTaskRequestBO);
        if(response.getStatusCode()==201) {
            CreateIssueResponseBO createIssueResponseBO = response.getBody().as(CreateIssueResponseBO.class);
            DemandJiraDO demandJiraDO = new DemandJiraDO();
            demandJiraDO.setCreatTime(LocalDateTime.now());
            demandJiraDO.setJiraId(createIssueResponseBO.getId());
            demandJiraDO.setJiraKey(createIssueResponseBO.getKey());
            demandJiraDO.setReqInnerSeq(epicDemandJiraDO.getJiraKey()+"_"+devpCoorDept);
            demandJiraDO.setReqNm(demandBO.getReqNm());
            demandJiraDO.setAssignmentDepartment(demandBO.getDevpLeadDept());
            demandJiraDO.setIssueType("开发主任务");
            demandJiraDO.setCreateState("success");
            //设置关联Epic
            demandJiraDO.setRelevanceEpic(epicDemandJiraDO.getJiraKey());
            //若是jira关联表已存在该项目，则更新
            DemandJiraDO demandJiraDO2 = demandJiraDao.get(demandJiraDO.getReqInnerSeq());
            if (JudgeUtils.isNull(demandJiraDO2)) {
                demandJiraDao.insert(demandJiraDO);
            } else {
                demandJiraDao.update(demandJiraDO);
            }
        }else{
            DemandJiraDO demandJiraDO = new DemandJiraDO();
            demandJiraDO.setCreatTime(LocalDateTime.now());
            demandJiraDO.setReqInnerSeq(epicDemandJiraDO.getJiraKey()+"_"+devpCoorDept);
            demandJiraDO.setReqNm(demandBO.getReqNm());
            demandJiraDO.setCreateState("fail");
            demandJiraDO.setRemarks(response.getBody().print());
            //设置关联Epic
            demandJiraDO.setRelevanceEpic(epicDemandJiraDO.getJiraKey());
            DemandJiraDO demandJiraDO2 = demandJiraDao.get(demandJiraDO.getReqInnerSeq());
            if(JudgeUtils.isNull(demandJiraDO2)){
                demandJiraDao.insert(demandJiraDO);
            }else{
                demandJiraDao.update(demandJiraDO);
            }
        }
    }

    @Async
    @Override
    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public void batchCreateEpic(List<DemandDO> demandDOList) {
        demandDOList.forEach(m->{
            DemandBO demandBO = BeanUtils.copyPropertiesReturnDest(new DemandBO(), m);
            this.createEpic(demandBO);
        });
    }

    @Override
    public void createMasterTask(DemandBO demandBO) {
        //创建开发部门链表
        List<String> developmentDepartmenList = new ArrayList<>();
        developmentDepartmenList.add(demandBO.getDevpLeadDept());
        String[] split = demandBO.getDevpCoorDept().split(",");
        //若有开发配合部门
        if(JudgeUtils.isNotEmpty(split)) {
            Arrays.stream(split).forEach(arr -> developmentDepartmenList.add(arr));
        }
        System.err.println(developmentDepartmenList);

    }
}
