/*
 * @ClassName ISysSnapshotLogDao
 * @Description 
 * @version 1.0
 * @Date 2019-08-16 09:04:42
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.SnapshotLogDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface ISnapshotLogDao extends BaseDao<SnapshotLogDO, String> {
    List<SnapshotLogDO> querySnapshotList(@Param("title") String title, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

}