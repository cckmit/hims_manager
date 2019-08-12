/*
 * @ClassName ICenterDao
 * @Description 
 * @version 1.0
 * @Date 2019-07-25 11:01:18
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.DemandDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IDemandDao extends BaseDao<DemandDO, String> {

}