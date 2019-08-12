/*
 * @ClassName IRoleMenuDao
 * @Description
 * @version 1.0
 * @Date 2018-11-05 12:18:45
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.RoleMenuDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IRoleMenuDao extends BaseDao<RoleMenuDO, Long> {

}
