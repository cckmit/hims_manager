package com.cmpay.lemon.monitor.bo;

import com.cmpay.lemon.framework.page.PageInfo;
import java.util.List;

public class OperationApplicationRspBO {
    List<OperationApplicationBO> operationApplicationList;
    PageInfo<OperationApplicationBO> pageInfo;

    public List<OperationApplicationBO> getProductionList() {
        return operationApplicationList;
    }

    public void setProductionList(List<OperationApplicationBO> productionList) {
        operationApplicationList = productionList;
    }

    public PageInfo<OperationApplicationBO> getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo<OperationApplicationBO> pageInfo) {
        this.pageInfo = pageInfo;
    }
}
