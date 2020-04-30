/*
 * @ClassName IDemandJiraSubtaskDao
 * @Description 
 * @version 1.0
 * @Date 2020-04-29 18:00:18
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.DemandJiraSubtaskDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IDemandJiraSubtaskDao extends BaseDao<DemandJiraSubtaskDO, String> {
}