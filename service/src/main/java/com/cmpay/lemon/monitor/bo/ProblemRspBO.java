/*
 * @ClassName ProblemDO
 * @Description
 * @version 1.0
 * @Date 2020-09-25 15:17:26
 */
package com.cmpay.lemon.monitor.bo;



import com.cmpay.lemon.framework.page.PageInfo;

import java.time.LocalDateTime;
import java.util.List;


public class ProblemRspBO {
    List<ProblemBO> problemBOList;
    PageInfo<ProblemBO> pageInfo;

    public List<ProblemBO> getProblemBOList() {
        return problemBOList;
    }

    public void setProblemBOList(List<ProblemBO> problemBOList) {
        this.problemBOList = problemBOList;
    }

    public PageInfo<ProblemBO> getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo<ProblemBO> pageInfo) {
        this.pageInfo = pageInfo;
    }

    @Override
    public String toString() {
        return "ProblemRspBO{" +
                "problemBOList=" + problemBOList +
                ", pageInfo=" + pageInfo +
                '}';
    }
}
