/*
 * @ClassName ISupportWorkloadDao
 * @Description
 * @version 1.0
 * @Date 2020-08-27 15:02:17
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.SupportWorkloadDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ISupportWorkloadExtDao extends ISupportWorkloadDao {
    List<SupportWorkloadDO> findList(SupportWorkloadDO supportWorkloadDO);
}
