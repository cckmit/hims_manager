/*
 * @ClassName GitlabDataDO
 * @Description
 * @version 1.0
 * @Date 2020-11-19 11:53:55
 */
package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.response.PageableRspDTO;

import java.util.List;

public class GitlabDataRspDTO extends PageableRspDTO {
   List<GitlabDataDTO> gitlabDataRspDTOList;

    public List<GitlabDataDTO> getGitlabDataRspDTOList() {
        return gitlabDataRspDTOList;
    }

    public void setGitlabDataRspDTOList(List<GitlabDataDTO> gitlabDataRspDTOList) {
        this.gitlabDataRspDTOList = gitlabDataRspDTOList;
    }

}
