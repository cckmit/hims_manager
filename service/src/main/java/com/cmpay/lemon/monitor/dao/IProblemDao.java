/*
 * @ClassName IProblemDao
 * @Description 
 * @version 1.0
 * @Date 2020-09-25 15:17:26
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.ProblemDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IProblemDao extends BaseDao<ProblemDO, Long> {
}