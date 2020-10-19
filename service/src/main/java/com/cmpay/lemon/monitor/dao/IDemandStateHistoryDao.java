/*
 * @ClassName IDemandStateHistoryDao
 * @Description 
 * @version 1.0
 * @Date 2020-10-15 16:54:07
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.DemandStateHistoryDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IDemandStateHistoryDao extends BaseDao<DemandStateHistoryDO, Integer> {
}