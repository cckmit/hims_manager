/*
 * @ClassName IUserDao
 * @Description 
 * @version 1.0
 * @Date 2019-11-07 09:40:56
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.UserDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IUserDao extends BaseDao<UserDO, Long> {

}