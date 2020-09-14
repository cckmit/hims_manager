/*
 * @ClassName ProUnhandledIssuesDO
 * @Description
 * @version 1.0
 * @Date 2020-07-03 17:03:49
 */
package com.cmpay.lemon.monitor.bo;

import com.cmpay.lemon.framework.page.PageInfo;

import java.util.List;

/**
 * @author ty
 */
public class ProUnhandledIssuesRspBO {
    List<ProUnhandledIssuesBO> proUnhandledIssuesBOList;
    PageInfo<ProUnhandledIssuesBO> pageInfo;

    public List<ProUnhandledIssuesBO> getProUnhandledIssuesBOList() {
        return proUnhandledIssuesBOList;
    }

    public void setProUnhandledIssuesBOList(List<ProUnhandledIssuesBO> proUnhandledIssuesBOList) {
        this.proUnhandledIssuesBOList = proUnhandledIssuesBOList;
    }

    public PageInfo<ProUnhandledIssuesBO> getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo<ProUnhandledIssuesBO> pageInfo) {
        this.pageInfo = pageInfo;
    }

    @Override
    public String toString() {
        return "ProUnhandledIssuesRspBO{" +
                "proUnhandledIssuesBOList=" + proUnhandledIssuesBOList +
                ", pageInfo=" + pageInfo +
                '}';
    }
}
