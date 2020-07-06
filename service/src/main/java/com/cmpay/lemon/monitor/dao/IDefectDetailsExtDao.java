/*
 * @ClassName IDefectDetailsDao
 * @Description
 * @version 1.0
 * @Date 2020-06-30 16:04:29
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.DefectDetailsDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IDefectDetailsExtDao extends IDefectDetailsDao {

    List<DefectDetailsDO> findList(DefectDetailsDO entity);
    List<DefectDetailsDO> findWeekList(DefectDetailsDO entity);
}
