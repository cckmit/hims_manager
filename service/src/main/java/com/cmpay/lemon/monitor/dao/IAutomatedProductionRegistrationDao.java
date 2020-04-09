/*
 * @ClassName IAutomatedProductionRegistrationDao
 * @Description 
 * @version 1.0
 * @Date 2020-04-09 15:31:21
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.AutomatedProductionRegistrationDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IAutomatedProductionRegistrationDao extends BaseDao<AutomatedProductionRegistrationDO, Integer> {
}