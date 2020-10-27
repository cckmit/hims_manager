/*
 * @ClassName IOnlineDefectDao
 * @Description 
 * @version 1.0
 * @Date 2020-10-21 17:33:48
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.OnlineDefectDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IOnlineDefectDao extends BaseDao<OnlineDefectDO, Long> {
}