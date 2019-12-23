package com.cmpay.lemon.monitor.service.jira;

import com.cmpay.lemon.monitor.bo.DemandBO;
import com.cmpay.lemon.monitor.entity.DemandDO;
import com.cmpay.lemon.monitor.entity.DemandJiraDO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface JiraOperationService {

    void createEpic(DemandBO demandBO);

    void batchCreateEpic(List<DemandDO> demandDOList);

    void createMasterTask(DemandBO demandBO, DemandJiraDO demandJiraDO1);

    void getJiraIssue(List<DemandDO> demandDOList);

    void jiraTestMainTaskBatchEdit(MultipartFile file);
}
