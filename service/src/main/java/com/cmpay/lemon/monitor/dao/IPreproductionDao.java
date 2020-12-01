/*
 * @ClassName IPreproductionDao
 * @Description 
 * @version 1.0
 * @Date 2020-11-30 15:34:39
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.PreproductionDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IPreproductionDao extends BaseDao<PreproductionDO, String> {
}