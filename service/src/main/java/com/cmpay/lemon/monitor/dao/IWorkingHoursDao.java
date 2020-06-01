/*
 * @ClassName IWorkingHoursDao
 * @Description
 * @version 1.0
 * @Date 2020-04-30 15:59:10
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.DepartmentWorkDO;
import com.cmpay.lemon.monitor.entity.WorkingHoursDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IWorkingHoursDao extends BaseDao<WorkingHoursDO, String> {
    // 日总和
    List<WorkingHoursDO> findSum(WorkingHoursDO workingHoursDO);
    List<WorkingHoursDO> findWeekSum(WorkingHoursDO workingHoursDO);
    List<WorkingHoursDO> findMonthSum(WorkingHoursDO workingHoursDO);
    List<WorkingHoursDO> findSumB(WorkingHoursDO workingHoursDO);
    List<WorkingHoursDO> findWeekSumB(WorkingHoursDO workingHoursDO);
    List<WorkingHoursDO> findMonthSumB(WorkingHoursDO workingHoursDO);
    // 日期
    List<WorkingHoursDO> findList(WorkingHoursDO workingHoursDO);
    // 周
    List<WorkingHoursDO> findListWeek(WorkingHoursDO workingHoursDO);
    // 月
    List<WorkingHoursDO> findListMonth(WorkingHoursDO workingHoursDO);
    List<DepartmentWorkDO> findDeptHours(String selectTime);
}
