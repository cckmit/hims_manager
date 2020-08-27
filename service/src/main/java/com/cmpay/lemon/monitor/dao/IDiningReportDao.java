/*
 * @ClassName IDiningReportDao
 * @Description 
 * @version 1.0
 * @Date 2020-08-25 16:31:56
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.DiningReportDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IDiningReportDao extends BaseDao<DiningReportDO, String> {
}