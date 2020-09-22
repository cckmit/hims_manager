/*
 * @ClassName IMonthWorkdayDao
 * @Description
 * @version 1.0
 * @Date 2020-09-15 11:39:14
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.MonthWorkdayDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IMonthWorkdayDao extends BaseDao<MonthWorkdayDO, Long> {

    MonthWorkdayDO getMonth(String month);
}
