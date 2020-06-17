/*
 * @ClassName ISmokeTestFailedCountDao
 * @Description 
 * @version 1.0
 * @Date 2020-06-17 14:29:04
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.SmokeTestFailedCountDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ISmokeTestFailedCountDao extends BaseDao<SmokeTestFailedCountDO, Integer> {
}