/*
 * @ClassName IDemandStateHistoryDao
 * @Description 
 * @version 1.0
 * @Date 2019-10-10 10:16:09
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.DemandStateHistoryDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IDemandStateHistoryDao extends BaseDao<DemandStateHistoryDO, Integer> {
}