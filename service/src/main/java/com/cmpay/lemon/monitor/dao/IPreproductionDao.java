/*
 * @ClassName ICenterDao
 * @Description 
 * @version 1.0
 * @Date 2019-07-25 11:01:18
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.PreproductionDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 预投产mapper
 */
@Mapper
public interface IPreproductionDao extends BaseDao<PreproductionDO, String> {

}