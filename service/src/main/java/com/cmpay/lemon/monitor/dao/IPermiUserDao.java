/*
 * @ClassName IPermiUserDao
 * @Description 
 * @version 1.0
 * @Date 2019-11-01 17:34:10
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.PermiUserDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IPermiUserDao extends BaseDao<PermiUserDO, Long> {
}