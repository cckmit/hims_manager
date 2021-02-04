package com.cmpay.lemon.monitor.service.jira;

import com.cmpay.lemon.monitor.bo.DemandBO;
import com.cmpay.lemon.monitor.bo.ProblemBO;
import com.cmpay.lemon.monitor.entity.DemandDO;
import com.cmpay.lemon.monitor.entity.DemandJiraDO;
import com.cmpay.lemon.monitor.entity.ProblemDO;
import com.cmpay.lemon.monitor.entity.ProductionFollowDO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface JiraOperationService {

    void createEpic(DemandBO demandBO);

    void createProduction(ProblemBO problemBO);
    void createProduction2(ProductionFollowDO productionFollowDO);

    void batchCreateEpic(List<DemandDO> demandDOList);

    void createMasterTask(DemandBO demandBO, DemandJiraDO demandJiraDO1);

    void getJiraIssue(List<DemandDO> demandDOList);

    void jiraEpicKey(DemandDO demandDO);

    void jiraTestMainTaskBatchEdit(MultipartFile file);

}
