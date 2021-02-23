/*
 * @ClassName ProblemDO
 * @Description
 * @version 1.0
 * @Date 2020-09-25 15:17:26
 */
package com.cmpay.lemon.monitor.bo;



import com.cmpay.lemon.framework.page.PageInfo;

import java.util.List;


public class ProductionFollowRspBO {
    List<ProductionFollowBO> productionFollowBOList;
    PageInfo<ProductionFollowBO> pageInfo;

    public List<ProductionFollowBO> getProductionFollowBOList() {
        return productionFollowBOList;
    }

    public void setProductionFollowBOList(List<ProductionFollowBO> productionFollowBOList) {
        this.productionFollowBOList = productionFollowBOList;
    }

    public PageInfo<ProductionFollowBO> getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo<ProductionFollowBO> pageInfo) {
        this.pageInfo = pageInfo;
    }

    @Override
    public String toString() {
        return "ProdutionFollowRspBO{" +
                "productionFollowBOList=" + productionFollowBOList +
                ", pageInfo=" + pageInfo +
                '}';
    }
}
