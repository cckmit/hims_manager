/*
 * @ClassName IDemandChangeDetailsDao
 * @Description 
 * @version 1.0
 * @Date 2019-11-13 10:55:00
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.DemandChangeDetailsDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IDemandChangeDetailsDao extends BaseDao<DemandChangeDetailsDO, Integer> {
}