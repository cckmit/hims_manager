/*
 * @ClassName IDefectDetailsDao
 * @Description
 * @version 1.0
 * @Date 2020-07-02 10:43:29
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.monitor.entity.DefectDetailsDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IDefectDetailsExtDao extends IDefectDetailsDao {

   List<DefectDetailsDO> findNotCompleted(DefectDetailsDO defectDetailsDO);
    List<DefectDetailsDO> findList(DefectDetailsDO entity);
    // 有效的plog数
    List<DefectDetailsDO> findValidList(DefectDetailsDO entity);
    List<DefectDetailsDO> findWeekList(DefectDetailsDO entity);
    List<DefectDetailsDO> findByTime(DefectDetailsDO entity);
    List<DefectDetailsDO> findUnfinishedDefects(DefectDetailsDO entity);
}
