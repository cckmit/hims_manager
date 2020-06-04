/*
 * @ClassName IDemandNameChangeDao
 * @Description
 * @version 1.0
 * @Date 2020-06-03 10:21:06
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.DemandNameChangeDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IDemandNameChangeExtDao extends IDemandNameChangeDao {

    List<DemandNameChangeDO> findList(DemandNameChangeDO entity);

    /** 查询单笔记录
     * @param entity
     * @return
     */
    DemandNameChangeDO findOne(DemandNameChangeDO entity);

}
