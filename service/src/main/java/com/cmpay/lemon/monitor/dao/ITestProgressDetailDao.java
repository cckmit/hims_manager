/*
 * @ClassName ITestProgressDetailDao
 * @Description 
 * @version 1.0
 * @Date 2020-08-18 10:50:32
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.TestProgressDetailDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ITestProgressDetailDao extends BaseDao<TestProgressDetailDO, Long> {
}