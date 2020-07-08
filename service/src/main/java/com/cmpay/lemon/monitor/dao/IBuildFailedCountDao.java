/*
 * @ClassName IBuildFailedCountDao
 * @Description 
 * @version 1.0
 * @Date 2020-07-08 11:33:25
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.BuildFailedCountDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IBuildFailedCountDao extends BaseDao<BuildFailedCountDO, Integer> {
}