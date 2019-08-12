/*
 * @ClassName IRoleDao
 * @Description
 * @version 1.0
 * @Date 2018-11-05 12:17:52
 */
package com.cmpay.lemon.monitor.dao;


import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.RoleDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IRoleDao extends BaseDao<RoleDO, Long> {

}
