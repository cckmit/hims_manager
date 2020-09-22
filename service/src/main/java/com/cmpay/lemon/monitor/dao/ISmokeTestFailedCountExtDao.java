/*
 * @ClassName ISmokeTestFailedCountDao
 * @Description
 * @version 1.0
 * @Date 2020-09-16 16:08:58
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.SmokeTestFailedCountDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ISmokeTestFailedCountExtDao extends ISmokeTestFailedCountDao {

    List<SmokeTestFailedCountDO> findMonth(SmokeTestFailedCountDO entity);
    List<SmokeTestFailedCountDO> findWeek(SmokeTestFailedCountDO entity);
}
