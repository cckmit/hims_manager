package com.cmpay.lemon.monitor.service.impl.jira;

import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.common.utils.JudgeUtils;
import com.cmpay.lemon.monitor.bo.DemandBO;
import com.cmpay.lemon.monitor.bo.jira.CreateIssueEpicRequestBO;
import com.cmpay.lemon.monitor.bo.jira.CreateIssueMainTaskRequestBO;
import com.cmpay.lemon.monitor.bo.jira.CreateIssueResponseBO;
import com.cmpay.lemon.monitor.dao.IDemandJiraDao;
import com.cmpay.lemon.monitor.dao.IDemandJiraDevelopMasterTaskDao;
import com.cmpay.lemon.monitor.dao.IJiraDepartmentDao;
import com.cmpay.lemon.monitor.entity.DemandDO;
import com.cmpay.lemon.monitor.entity.DemandJiraDO;
import com.cmpay.lemon.monitor.entity.DemandJiraDevelopMasterTaskDO;
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
    @Autowired
    IDemandJiraDevelopMasterTaskDao demandJiraDevelopMasterTaskDao;
    //jira项目类型 和包项目 jira编号
    final static  Integer PROJECTTYPE_CMPAY=10009;
    //EPIC任务 jira编号
    final static  Integer ISSUETYPE_EPIC=10000;
    //开发主任务 jira编号
    final static  Integer ISSUETYPE_DEVELOPMAINTASK=10005;
    //测试主任务 jira编号
    final static  Integer ISSUETYPE_TESTMAINTASK=10006;
    //Epic任务
    final static  String EPIC="Epic";
    //开发主任务
    final static  String DEVELOPMAINTASK="开发主任务";
    //开发主任务
    final static  String TESTMAINTASK="测试主任务";
    //业务失败标识
    final static  String FAIL="fail";
    //业务成功标识
    final static  String SUCCESS="success";
    //测试部门
    final static  String TESTINGDIVISION="产品测试部";
    //部门参数未匹配
    final static  String DEPARTMENTDIDNOTMATCH="主导部门错误(部门参数未匹配)";

    //jira返回业务成功时状态码 201
    final static  int JIRA_SUCCESS=201;

    @Async
    @Override
    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public void createEpic(DemandBO demandBO)   {
        //jira关联表中已有，并且状态为成功,则不再生成jira任务
        DemandJiraDO demandJiraDO1 = demandJiraDao.get(demandBO.getReqInnerSeq());
        if(JudgeUtils.isNotNull(demandJiraDO1)&&demandJiraDO1.getCreateState().equals(SUCCESS)){
            this.createMasterTask(demandBO,demandJiraDO1);
            return;
        }
        CreateIssueEpicRequestBO createIssueEpicRequestBO = new CreateIssueEpicRequestBO();
        createIssueEpicRequestBO.setSummary(demandBO.getReqNm());
        createIssueEpicRequestBO.setDescription(demandBO.getReqDesc());
        //设置项目为和包项目，问题类型开发主任务
        createIssueEpicRequestBO.setIssueType(ISSUETYPE_EPIC);
        createIssueEpicRequestBO.setProject(PROJECTTYPE_CMPAY);
        createIssueEpicRequestBO.setDevpLeadDept(demandBO.getDevpLeadDept());
        createIssueEpicRequestBO.setDescription(demandBO.getReqDesc());
        createIssueEpicRequestBO.setReqInnerSeq(demandBO.getReqInnerSeq());
        //获取部门管理人员
        System.err.println(demandBO.getDevpLeadDept());
        JiraDepartmentDO jiraDepartmentDO = jiraDepartmentDao.get(demandBO.getDevpLeadDept());
        //未获得部门管理人员则为配置jira对应数据错误
        if(JudgeUtils.isNull(jiraDepartmentDO)){
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
        if(response.getStatusCode()==JIRA_SUCCESS) {
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
            if(JudgeUtils.isNull(demandJiraDO2)){
                demandJiraDao.insert(demandJiraDO);
            }else{
                demandJiraDao.update(demandJiraDO);
            }
            this.createMasterTask(demandBO, demandJiraDO);

        }else{
            DemandJiraDO demandJiraDO = new DemandJiraDO();
            demandJiraDO.setCreatTime(LocalDateTime.now());
            demandJiraDO.setReqInnerSeq(demandBO.getReqInnerSeq());
            demandJiraDO.setReqNm(demandBO.getReqNm());
            demandJiraDO.setCreateState(FAIL);
            demandJiraDO.setRemarks(response.getBody().print());
            DemandJiraDO demandJiraDO2 = demandJiraDao.get(demandJiraDO.getReqInnerSeq());
            if(JudgeUtils.isNull(demandJiraDO2)){
                demandJiraDao.insert(demandJiraDO);
            }else{
                demandJiraDao.update(demandJiraDO);
            }
        }
    }

    @Async
    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public void CreateJiraMasterTask(String devpCoorDept, DemandBO demandBO, DemandJiraDO epicDemandJiraDO, String taskType) {
        DemandJiraDevelopMasterTaskDO demandJiraDevelopMasterTaskDO1 = demandJiraDevelopMasterTaskDao.get(epicDemandJiraDO.getJiraKey()+"_"+devpCoorDept+"_"+taskType);
        if(JudgeUtils.isNotNull(demandJiraDevelopMasterTaskDO1)&&demandJiraDevelopMasterTaskDO1.getCreateState().equals(SUCCESS)){
            return;
        }
        //获取部门管理人员
        JiraDepartmentDO jiraDepartmentDO = jiraDepartmentDao.get(devpCoorDept);
        //未获得部门管理人员则为配置jira对应数据错误
        if(JudgeUtils.isNull(jiraDepartmentDO)){
            DemandJiraDevelopMasterTaskDO demandJiraDevelopMasterTaskDO = new DemandJiraDevelopMasterTaskDO();
            demandJiraDevelopMasterTaskDO.setCreatTime(LocalDateTime.now());
            //拼凑jira主任务名
            demandJiraDevelopMasterTaskDO.setMasterTaskKey(epicDemandJiraDO.getJiraKey()+"_"+devpCoorDept+"_"+taskType);
            demandJiraDevelopMasterTaskDO.setReqNm(demandBO.getReqNm());
            demandJiraDevelopMasterTaskDO.setCreateState(FAIL);
            demandJiraDevelopMasterTaskDO.setRemarks(DEPARTMENTDIDNOTMATCH);
            demandJiraDevelopMasterTaskDao.insert(demandJiraDevelopMasterTaskDO);
            return;
        }
        CreateIssueMainTaskRequestBO createMainTaskRequestBO = new CreateIssueMainTaskRequestBO();
        createMainTaskRequestBO.setEpicKey(epicDemandJiraDO.getJiraKey());
        //拼凑jira主任务名称  部门+任务名
        createMainTaskRequestBO.setSummary("【"+devpCoorDept+"】"+demandBO.getReqNm());
        createMainTaskRequestBO.setDescription(demandBO.getReqDesc());
        //设置项目为和包项目，问题类型主任务
        if(taskType.equals(DEVELOPMAINTASK)){
            createMainTaskRequestBO.setIssueType(ISSUETYPE_DEVELOPMAINTASK);
        }else{
            createMainTaskRequestBO.setIssueType(ISSUETYPE_TESTMAINTASK);
        }

        createMainTaskRequestBO.setProject(PROJECTTYPE_CMPAY);
        createMainTaskRequestBO.setDevpLeadDept(devpCoorDept);
        createMainTaskRequestBO.setDescription(demandBO.getReqDesc());
        createMainTaskRequestBO.setReqInnerSeq(demandBO.getReqInnerSeq());
        createMainTaskRequestBO.setManager(jiraDepartmentDO.getManagerjiranm());
        Response response = JiraUtil.CreateIssue(createMainTaskRequestBO);
        if(response.getStatusCode()==JIRA_SUCCESS) {
            CreateIssueResponseBO createIssueResponseBO = response.getBody().as(CreateIssueResponseBO.class);
            DemandJiraDevelopMasterTaskDO demandJiraDevelopMasterTaskDO = new DemandJiraDevelopMasterTaskDO();
            demandJiraDevelopMasterTaskDO.setCreatTime(LocalDateTime.now());
            demandJiraDevelopMasterTaskDO.setJiraId(createIssueResponseBO.getId());
            demandJiraDevelopMasterTaskDO.setJiraKey(createIssueResponseBO.getKey());
            demandJiraDevelopMasterTaskDO.setMasterTaskKey(epicDemandJiraDO.getJiraKey()+"_"+devpCoorDept+"_"+taskType);
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
        }else{
            DemandJiraDevelopMasterTaskDO demandJiraDevelopMasterTaskDO = new DemandJiraDevelopMasterTaskDO();
            demandJiraDevelopMasterTaskDO.setCreatTime(LocalDateTime.now());
            demandJiraDevelopMasterTaskDO.setAssignmentDepartment(devpCoorDept);
            demandJiraDevelopMasterTaskDO.setMasterTaskKey(epicDemandJiraDO.getJiraKey()+"_"+devpCoorDept+"_"+taskType);
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
    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public void batchCreateEpic(List<DemandDO> demandDOList) {
        demandDOList.forEach(m->{
            DemandBO demandBO = BeanUtils.copyPropertiesReturnDest(new DemandBO(), m);
            this.createEpic(demandBO);
        });
    }

    @Override
    public void createMasterTask(DemandBO demandBO, DemandJiraDO demandJiraDO) {
        //创建开发部门链表
        List<String> developmentDepartmenList = new ArrayList<>();
        //若有开发配合部门
        if(demandBO.getDevpCoorDept()!=null&&demandBO.getDevpCoorDept()!="") {
            String[] split = demandBO.getDevpCoorDept().split(",");
            Arrays.stream(split).forEach(arr -> developmentDepartmenList.add(arr));
        }
        //添加开发主导部门
        developmentDepartmenList.add(demandBO.getDevpLeadDept());
        developmentDepartmenList.forEach(m->{
            if(m.isEmpty()){
                return;
            }
            this.CreateJiraMasterTask(m,demandBO,demandJiraDO,DEVELOPMAINTASK);
        });
        //添加测试主任务
        this.CreateJiraMasterTask(TESTINGDIVISION,demandBO,demandJiraDO,TESTMAINTASK);
    }


}
