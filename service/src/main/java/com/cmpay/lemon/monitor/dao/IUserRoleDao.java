/*
 * @ClassName IUserRoleDao
 * @Description
 * @version 1.0
 * @Date 2018-11-05 12:16:41
 */
package com.cmpay.lemon.monitor.dao;


import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.UserRoleDO;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface IUserRoleDao extends BaseDao<UserRoleDO, Long> {
}
