package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.ProductionTimeD0;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;


/**
 * Created by zouxin on 2018/8/15.
 */
@Mapper
public interface IProductionTimeDao extends BaseDao<ProductionTimeD0, String> {

    /** 查询所有投产时间设置详细信息*/
    List<ProductionTimeD0> findProductTimeList();

    /** 通过标示查询投产时间信息*/
    String findProductTimeByID(Integer id);

    /** 通过标示查询投产时间详细信息*/
    ProductionTimeD0 findProductTimeBeanByID(Integer id);

}
