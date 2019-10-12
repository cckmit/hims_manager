/*
 * @ClassName IDemandJiraDao
 * @Description 
 * @version 1.0
 * @Date 2019-10-11 17:11:20
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.DemandJiraDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IDemandJiraDao extends BaseDao<DemandJiraDO, String> {
}