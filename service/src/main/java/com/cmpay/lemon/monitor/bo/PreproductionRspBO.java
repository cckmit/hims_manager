package com.cmpay.lemon.monitor.bo;

import com.cmpay.lemon.framework.page.PageInfo;

import java.util.List;

public class PreproductionRspBO {
    List<PreproductionBO> preproductionBOList;
    PageInfo<PreproductionBO> pageInfo;

    public List<PreproductionBO> getPreproductionBOList() {
        return preproductionBOList;
    }

    public void setPreproductionBOList(List<PreproductionBO> preproductionBOList) {
        this.preproductionBOList = preproductionBOList;
    }

    public PageInfo<PreproductionBO> getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo<PreproductionBO> pageInfo) {
        this.pageInfo = pageInfo;
    }
}
