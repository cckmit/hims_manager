/*
 * @ClassName IDemandTimeFrameHistoryDao
 * @Description 
 * @version 1.0
 * @Date 2019-12-02 17:57:24
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.DemandTimeFrameHistoryDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IDemandTimeFrameHistoryDao extends BaseDao<DemandTimeFrameHistoryDO, Integer> {
}