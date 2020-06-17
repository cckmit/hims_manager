/*
 * @ClassName ISmokeTestRegistrationDao
 * @Description 
 * @version 1.0
 * @Date 2020-06-17 14:10:37
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.SmokeTestRegistrationDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ISmokeTestRegistrationDao extends BaseDao<SmokeTestRegistrationDO, Integer> {
}