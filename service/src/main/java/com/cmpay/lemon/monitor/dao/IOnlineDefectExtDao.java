/*
 * @ClassName IOnlineDefectDao
 * @Description
 * @version 1.0
 * @Date 2020-10-19 14:11:34
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.OnlineDefectDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IOnlineDefectExtDao extends IOnlineDefectDao {
    List<OnlineDefectDO> findList(OnlineDefectDO onlineDefectDO);
}
