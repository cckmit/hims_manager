/*
 * @ClassName IDemandResourceInvestedDao
 * @Description 
 * @version 1.0
 * @Date 2020-06-29 09:58:17
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.DemandResourceInvestedDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IDemandResourceInvestedDao extends BaseDao<DemandResourceInvestedDO, Integer> {
}