/*
 * @ClassName IDemandStateHistoryDao
 * @Description 
 * @version 1.0
 * @Date 2019-11-28 14:23:21
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.monitor.entity.DemandStateHistoryDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IDemandStateHistoryExtDao extends IDemandStateHistoryDao{
    public List<DemandStateHistoryDO> getLastRecordByReqInnerSeq(String reqInnerSeq);
}