/*
 * @ClassName IProductionProblemJiraDao
 * @Description 
 * @version 1.0
 * @Date 2020-10-26 16:31:27
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.ProductionProblemJiraDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IProductionProblemJiraDao extends BaseDao<ProductionProblemJiraDO, String> {
}