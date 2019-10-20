/*
 * @ClassName IproductionPicDao
 * @Description 
 * @version 1.0
 * @Date 2019-10-20 10:04:37
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.ProductionPicDO;
import com.cmpay.lemon.monitor.entity.ProductionPicDOKey;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IProductionPicDao extends BaseDao<ProductionPicDO, ProductionPicDOKey> {
}