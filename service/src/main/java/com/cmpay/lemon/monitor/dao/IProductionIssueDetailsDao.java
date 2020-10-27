/*
 * @ClassName IProductionIssueDetailsDao
 * @Description 
 * @version 1.0
 * @Date 2020-10-27 11:32:57
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.ProductionIssueDetailsDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IProductionIssueDetailsDao extends BaseDao<ProductionIssueDetailsDO, String> {
}