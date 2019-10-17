package com.cmpay.lemon.monitor.bo;

import com.cmpay.lemon.framework.page.PageInfo;

import java.util.List;

public class ProductionRspBO {
    List<ProductionBO> ProductionList;
    PageInfo<ProductionBO> pageInfo;

    public List<ProductionBO> getProductionList() {
        return ProductionList;
    }

    public void setProductionList(List<ProductionBO> productionList) {
        ProductionList = productionList;
    }

    public PageInfo<ProductionBO> getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo<ProductionBO> pageInfo) {
        this.pageInfo = pageInfo;
    }
}
