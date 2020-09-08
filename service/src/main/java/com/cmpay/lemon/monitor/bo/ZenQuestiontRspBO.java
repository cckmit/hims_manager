/*
 * @ClassName ZenQuestiontDO
 * @Description
 * @version 1.0
 * @Date 2020-09-04 11:30:37
 */
package com.cmpay.lemon.monitor.bo;

import com.cmpay.lemon.framework.page.PageInfo;

import java.util.List;

public class ZenQuestiontRspBO {
   private List<ZenQuestiontBO> zenQuestiontBOList;
   private PageInfo<ZenQuestiontBO> pageInfo;

    public List<ZenQuestiontBO> getZenQuestiontBOList() {
        return zenQuestiontBOList;
    }

    public void setZenQuestiontBOList(List<ZenQuestiontBO> zenQuestiontBOList) {
        this.zenQuestiontBOList = zenQuestiontBOList;
    }

    public PageInfo<ZenQuestiontBO> getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo<ZenQuestiontBO> pageInfo) {
        this.pageInfo = pageInfo;
    }

    @Override
    public String toString() {
        return "ZenQuestiontRspBO{" +
                "zenQuestiontBOList=" + zenQuestiontBOList +
                ", pageInfo=" + pageInfo +
                '}';
    }
}
