/*
 * @ClassName IDemandJiraDao
 * @Description 
 * @version 1.0
 * @Date 2019-09-29 10:20:16
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.DemandJiraDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IDemandJiraDao extends BaseDao<DemandJiraDO, String> {
}