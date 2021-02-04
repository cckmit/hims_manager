/*
 * @ClassName IProductionFollowDao
 * @Description 
 * @version 1.0
 * @Date 2021-02-02 16:03:37
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.ProductionFollowDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IProductionFollowDao extends BaseDao<ProductionFollowDO, Long> {
}