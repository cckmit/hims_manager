/*
 * @ClassName IDemandQualityDao
 * @Description 
 * @version 1.0
 * @Date 2020-06-29 15:44:03
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.DemandQualityDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IDemandQualityDao extends BaseDao<DemandQualityDO, String> {
}