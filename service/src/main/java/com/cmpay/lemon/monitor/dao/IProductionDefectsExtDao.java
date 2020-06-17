/*
 * @ClassName IProductionDefectsDao
 * @Description 
 * @version 1.0
 * @Date 2020-06-16 15:15:52
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.monitor.entity.ProductionDefectsDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IProductionDefectsExtDao extends IProductionDefectsDao {

    List<ProductionDefectsDO> findAll(ProductionDefectsDO productionDefectsDO);
}