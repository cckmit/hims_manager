/*
 * @ClassName ITPermiDeptDao
 * @Description 
 * @version 1.0
 * @Date 2019-11-01 16:28:33
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.TPermiDeptDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ITPermiDeptDao extends BaseDao<TPermiDeptDO, Long> {
}