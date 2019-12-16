/*
 * @ClassName IPermiUserDao
 * @Description 
 * @version 1.0
 * @Date 2019-11-01 17:34:10
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.PermiUserDO;
import com.cmpay.lemon.monitor.entity.UserRoleDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IPermiUserDao extends BaseDao<PermiUserDO, Long> {
    void insertUserRole(@Param("userId")String  userId,@Param("roleId") String roleId);
    void deleteUserRole(@Param("userId")String  userId);
}