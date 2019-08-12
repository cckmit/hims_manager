/*
 * @ClassName IMenuDao
 * @Description 
 * @version 1.0
 * @Date 2019-07-01 11:37:43
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.MenuDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IMenuDao extends BaseDao<MenuDO, Long> {
}