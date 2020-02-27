/*
 * @ClassName IDemandCurperiodHistoryDao
 * @Description 
 * @version 1.0
 * @Date 2020-02-24 16:35:12
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.DemandCurperiodHistoryDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IDemandCurperiodHistoryDao extends BaseDao<DemandCurperiodHistoryDO, Integer> {
}