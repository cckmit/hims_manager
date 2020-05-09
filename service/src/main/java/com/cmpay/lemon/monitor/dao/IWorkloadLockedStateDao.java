/*
 * @ClassName IWorkloadLockedStateDao
 * @Description 
 * @version 1.0
 * @Date 2020-01-08 11:11:43
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.WorkloadLockedStateDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IWorkloadLockedStateDao extends BaseDao<WorkloadLockedStateDO, String> {
    WorkloadLockedStateDO getFeedback(String entrymonth) ;
    void  insertFeedback(WorkloadLockedStateDO workloadLockedStateDO);
    void updateFeedback(WorkloadLockedStateDO workloadLockedStateDO);
}