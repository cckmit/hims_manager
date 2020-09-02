/*
 * @ClassName IDemandEaseDevelopmentDao
 * @Description 
 * @version 1.0
 * @Date 2020-08-31 14:54:50
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.DemandEaseDevelopmentDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IDemandEaseDevelopmentDao extends BaseDao<DemandEaseDevelopmentDO, String> {
}