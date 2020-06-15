/*
 * @ClassName IProCheckTimeOutStatisticsDao
 * @Description 
 * @version 1.0
 * @Date 2020-06-15 09:33:23
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.ProCheckTimeOutStatisticsDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IProCheckTimeOutStatisticsDao extends BaseDao<ProCheckTimeOutStatisticsDO, Integer> {
}