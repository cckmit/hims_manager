package com.cmpay.lemon.monitor.bo;

import com.cmpay.lemon.framework.page.PageInfo;

import java.util.List;

/**
 * @author TY
 */
public class IssueDetailsRspBO {
    List<IssueDetailsBO> issueDetailsBOList;
    PageInfo<IssueDetailsBO> pageInfo;

    public List<IssueDetailsBO> getIssueDetailsBOList() {
        return issueDetailsBOList;
    }

    public void setIssueDetailsBOList(List<IssueDetailsBO> issueDetailsBOList) {
        this.issueDetailsBOList = issueDetailsBOList;
    }

    public PageInfo<IssueDetailsBO> getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo<IssueDetailsBO> pageInfo) {
        this.pageInfo = pageInfo;
    }

    @Override
    public String toString() {
        return "IssueDetailsRspBO{" +
                "issueDetailsBOList=" + issueDetailsBOList +
                ", pageInfo=" + pageInfo +
                '}';
    }
}
