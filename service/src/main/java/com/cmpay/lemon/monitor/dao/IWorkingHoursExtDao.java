/*
 * @ClassName IWorkingHoursDao
 * @Description
 * @version 1.0
 * @Date 2020-06-24 16:35:45
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.monitor.entity.DepartmentWorkDO;
import com.cmpay.lemon.monitor.entity.WorkingHoursDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IWorkingHoursExtDao extends IWorkingHoursDao{
    /**
     * 日部门员工工时
     * @param workingHoursDO
     * @return
     */
    List<WorkingHoursDO> findSum(WorkingHoursDO workingHoursDO);

    /**
     * 周部门员工工时
     * @param workingHoursDO
     * @return
     */
    List<WorkingHoursDO> findWeekSum(WorkingHoursDO workingHoursDO);
    /**
     * 月部门员工工时
     * @param workingHoursDO
     * @return
     */
    List<WorkingHoursDO> findMonthSum(WorkingHoursDO workingHoursDO);
    /**
     * 日部门汇总工时
     * @param workingHoursDO
     * @return
     */
    List<WorkingHoursDO> findSumB(WorkingHoursDO workingHoursDO);
    /**
     * 周部门汇总工时
     * @param workingHoursDO
     * @return
     */
    List<WorkingHoursDO> findWeekSumB(WorkingHoursDO workingHoursDO);
    /**
     * 月部门汇总工时
     * @param workingHoursDO
     * @return
     */
    List<WorkingHoursDO> findMonthSumB(WorkingHoursDO workingHoursDO);
    /**
     * 日部门员工详情工时
     * @param workingHoursDO
     * @return
     */
    List<WorkingHoursDO> findList(WorkingHoursDO workingHoursDO);
    /**
     * 周部门员工详情工时
     * @param workingHoursDO
     * @return
     */
    List<WorkingHoursDO> findListWeek(WorkingHoursDO workingHoursDO);
    /**
     * 月部门员工详情工时
     * @param workingHoursDO
     * @return
     */
    List<WorkingHoursDO> findListMonth(WorkingHoursDO workingHoursDO);

    /**
     * 查询各部门工时
     * @param selectTime
     * @return
     */
    List<DepartmentWorkDO> findDeptHours(String selectTime);

    /**
     * 根据epicKey获取需求参与人员工时
     * @param epicKey
     * @return
     */
    List<WorkingHoursDO> findEpicKeyHours(String epicKey);

    List<WorkingHoursDO> findWeekView(WorkingHoursDO workingHoursDO);
    List<WorkingHoursDO> findMonthView(WorkingHoursDO workingHoursDO);
    /**
     * 周部门员工详情需求工时
     * @param workingHoursDO
     * @return
     */
    List<WorkingHoursDO> findListWeekView(WorkingHoursDO workingHoursDO);
    /**
     * 月部门员工详情需求工时
     * @param workingHoursDO
     * @return
     */
    List<WorkingHoursDO> findListMonthView(WorkingHoursDO workingHoursDO);
}
