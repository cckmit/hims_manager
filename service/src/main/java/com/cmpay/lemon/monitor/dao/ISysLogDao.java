/*
 * @ClassName ISysLogDao
 * @Description 
 * @version 1.0
 * @Date 2019-08-16 09:41:53
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.SysLogDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface ISysLogDao extends BaseDao<SysLogDO, Long> {
    /**
     * 多条件分页查询
     *
     * @param userNo
     * @param operation
     * @param requestUri
     * @param startTime
     * @param endTime
     * @return
     */
    List<SysLogDO> pageQuery(@Param("userNo") String userNo,
                             @Param("operation") String operation,
                             @Param("requestUri") String requestUri,
                             @Param("startTime") Date startTime,
                             @Param("endTime") Date endTime);
}