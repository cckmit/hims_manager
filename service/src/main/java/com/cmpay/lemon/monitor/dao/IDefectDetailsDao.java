/*
 * @ClassName IDefectDetailsDao
 * @Description 
 * @version 1.0
 * @Date 2020-07-28 10:01:54
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.DefectDetailsDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IDefectDetailsDao extends BaseDao<DefectDetailsDO, String> {
}