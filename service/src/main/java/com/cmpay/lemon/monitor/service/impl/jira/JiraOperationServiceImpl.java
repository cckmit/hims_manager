package com.cmpay.lemon.monitor.service.impl.jira;

import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.common.utils.JudgeUtils;
import com.cmpay.lemon.monitor.bo.DemandBO;
import com.cmpay.lemon.monitor.bo.jira.CreateIssueRequestBO;
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
import java.util.List;

@Service
public class JiraOperationServiceImpl implements JiraOperationService {
    @Autowired
    IJiraDepartmentDao jiraDepartmentDao;
    @Autowired
    IDemandJiraDao demandJiraDao;

    @Override
    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public void createEpic(DemandBO demandBO)   {
        CreateIssueRequestBO createIssueRequestBO = new CreateIssueRequestBO();
        createIssueRequestBO.setSummary(demandBO.getReqNm());
        createIssueRequestBO.setDescription(demandBO.getReqDesc());
        createIssueRequestBO.setIssueType(10110);
        createIssueRequestBO.setProject(11221);
        createIssueRequestBO.setDevpLeadDept(demandBO.getDevpLeadDept());
        createIssueRequestBO.setDescription(demandBO.getReqDesc());
        createIssueRequestBO.setReqInnerSeq(demandBO.getReqInnerSeq());
        //获取部门管理
        JiraDepartmentDO jiraDepartmentDO = jiraDepartmentDao.get(demandBO.getDevpLeadDept());
        if(JudgeUtils.isNull(jiraDepartmentDO)){
            DemandJiraDO demandJiraDO = new DemandJiraDO();
            demandJiraDO.setCreatTime(LocalDateTime.now());
            demandJiraDO.setReqInnerSeq(demandBO.getReqInnerSeq());
            demandJiraDO.setReqNm(demandBO.getReqNm());
            demandJiraDO.setCreateState("fail");
            demandJiraDO.setFailCause("主导部门错误");
            demandJiraDao.insert(demandJiraDO);
            return;
        }
        createIssueRequestBO.setManager(jiraDepartmentDO.getManagerjiranm());
        JiraUtil jiraUtil = new JiraUtil();
        Response response = jiraUtil.CreateIssue(createIssueRequestBO);
        if(response.getStatusCode()==201) {
            CreateIssueResponseBO createIssueResponseBO = response.getBody().as(CreateIssueResponseBO.class);
            DemandJiraDO demandJiraDO = new DemandJiraDO();
            demandJiraDO.setCreatTime(LocalDateTime.now());
            demandJiraDO.setJiraId(createIssueResponseBO.getId());
            demandJiraDO.setJiraKey(createIssueResponseBO.getKey());
            demandJiraDO.setReqInnerSeq(demandBO.getReqInnerSeq());
            demandJiraDO.setReqNm(demandBO.getReqNm());
            demandJiraDO.setCreateState("success");
            demandJiraDao.insert(demandJiraDO);
        }else{
            DemandJiraDO demandJiraDO = new DemandJiraDO();
            demandJiraDO.setCreatTime(LocalDateTime.now());
            demandJiraDO.setReqInnerSeq(demandBO.getReqInnerSeq());
            demandJiraDO.setReqNm(demandBO.getReqNm());
            demandJiraDO.setCreateState("fail");
            demandJiraDO.setFailCause(response.getBody().toString());
            demandJiraDao.insert(demandJiraDO);
        }
    }

    @Async
    @Override
    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public void batchCreateEpic(List<DemandDO> demandDOList) {
        demandDOList.forEach(m->{
            DemandBO demandBO = BeanUtils.copyPropertiesReturnDest(new DemandBO(), m);
            createEpic(demandBO);
        });
    }
}
