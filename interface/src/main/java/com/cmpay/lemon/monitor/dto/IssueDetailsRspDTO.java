/*
 * @ClassName IssueDetailsDO
 * @Description
 * @version 1.0
 * @Date 2020-06-30 16:04:29
 */
package com.cmpay.lemon.monitor.dto;

import java.util.List;

/**
 * @author wlr
 */
public class IssueDetailsRspDTO {
    private List<IssueDetailsDTO> issueDetailsDTOList ;

    public List<IssueDetailsDTO> getIssueDetailsDTOList() {
        return issueDetailsDTOList;
    }

    public void setIssueDetailsDTOList(List<IssueDetailsDTO> issueDetailsDTOList) {
        this.issueDetailsDTOList = issueDetailsDTOList;
    }

    @Override
    public String toString() {
        return "IssueDetailsRspDTO{" +
                "issueDetailsDTOList=" + issueDetailsDTOList +
                '}';
    }
}
