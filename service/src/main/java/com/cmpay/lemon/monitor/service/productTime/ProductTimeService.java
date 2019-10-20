package com.cmpay.lemon.monitor.service.productTime;

import com.cmpay.lemon.monitor.bo.ProductionTimeBO;

import java.util.List;

public interface ProductTimeService {
    /** 查询所有投产时间设置的详细信息*/
    List<ProductionTimeBO> productTimeList();

    /** 通过标示查询投产时间信息*/
    String findProductTimeByID(Integer id);

    /** 通过标示查询投产时间详细信息*/
    ProductionTimeBO findProductTimeBeanByID(Integer id);

    /** 更新投产时间信息*/
    void updateProductTime (ProductionTimeBO productTimeBean);
}
