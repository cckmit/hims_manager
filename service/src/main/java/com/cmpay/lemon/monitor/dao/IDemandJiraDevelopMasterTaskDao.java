/*
 * @ClassName IDemandJiraDevelopMasterTaskDao
 * @Description 
 * @version 1.0
 * @Date 2019-10-12 10:40:56
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.DemandJiraDevelopMasterTaskDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IDemandJiraDevelopMasterTaskDao extends BaseDao<DemandJiraDevelopMasterTaskDO, String> {
}