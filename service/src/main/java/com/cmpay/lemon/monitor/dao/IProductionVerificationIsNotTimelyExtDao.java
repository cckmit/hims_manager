/*
 * @ClassName IProductionVerificationIsNotTimelyDao
 * @Description
 * @version 1.0
 * @Date 2020-09-15 17:23:21
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.ProductionVerificationIsNotTimelyDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IProductionVerificationIsNotTimelyExtDao extends IProductionVerificationIsNotTimelyDao {
    List<ProductionVerificationIsNotTimelyDO>findMonth(ProductionVerificationIsNotTimelyDO productionVerificationIsNotTimelyDO);
    List<ProductionVerificationIsNotTimelyDO>findWeek(ProductionVerificationIsNotTimelyDO productionVerificationIsNotTimelyDO);
}
