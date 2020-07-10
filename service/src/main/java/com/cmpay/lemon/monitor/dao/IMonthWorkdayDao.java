/*
 * @ClassName IMonthWorkdayDao
 * @Description 
 * @version 1.0
 * @Date 2020-07-10 16:02:26
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.MonthWorkdayDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IMonthWorkdayDao extends BaseDao<MonthWorkdayDO, String> {
}