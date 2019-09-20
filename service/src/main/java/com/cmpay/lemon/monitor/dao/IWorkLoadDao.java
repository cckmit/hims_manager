package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.monitor.entity.DemandDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: ty
 */
@Mapper
public interface IWorkLoadDao extends IDemandDao {
    /**
     * 工作量存量变更
     * @param bean Demand对象
     */
    void updateRwlByImpl(DemandDO bean);
}
