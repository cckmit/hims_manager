/*
 * @ClassName IWorkingHoursDao
 * @Description 
 * @version 1.0
 * @Date 2020-07-03 15:25:50
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.WorkingHoursDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IWorkingHoursDao extends BaseDao<WorkingHoursDO, String> {
}