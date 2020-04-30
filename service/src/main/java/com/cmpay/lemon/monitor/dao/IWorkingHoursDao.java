/*
 * @ClassName IWorkingHoursDao
 * @Description 
 * @version 1.0
 * @Date 2020-04-29 16:02:39
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.WorkingHoursDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IWorkingHoursDao extends BaseDao<WorkingHoursDO, Long> {
    List<WorkingHoursDO> findSum(WorkingHoursDO workingHoursDO);
    List<WorkingHoursDO> findList(WorkingHoursDO workingHoursDO);
}