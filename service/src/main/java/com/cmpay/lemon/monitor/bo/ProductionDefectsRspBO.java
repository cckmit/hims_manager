package com.cmpay.lemon.monitor.bo;

import com.cmpay.lemon.framework.page.PageInfo;

import java.util.List;

/**
 * @author TY
 */
public class ProductionDefectsRspBO {
    List<ProductionDefectsBO> productionDefectsBOList;
    PageInfo<ProductionDefectsBO> pageInfo;

    public List<ProductionDefectsBO> getProductionDefectsBOList() {
        return productionDefectsBOList;
    }

    public void setProductionDefectsBOList(List<ProductionDefectsBO> productionDefectsBOList) {
        this.productionDefectsBOList = productionDefectsBOList;
    }

    public PageInfo<ProductionDefectsBO> getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo<ProductionDefectsBO> pageInfo) {
        this.pageInfo = pageInfo;
    }
}
