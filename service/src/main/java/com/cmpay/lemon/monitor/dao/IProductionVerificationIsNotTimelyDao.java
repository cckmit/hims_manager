/*
 * @ClassName IProductionVerificationIsNotTimelyDao
 * @Description 
 * @version 1.0
 * @Date 2020-09-15 17:49:48
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.ProductionVerificationIsNotTimelyDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IProductionVerificationIsNotTimelyDao extends BaseDao<ProductionVerificationIsNotTimelyDO, String> {
}