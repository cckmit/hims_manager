package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.monitor.entity.DemandDO;
import com.cmpay.lemon.monitor.entity.MenuDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author: zhou_xiong
 */
@Mapper
public interface IDemandExtDao extends IDemandDao{

    //根据字典id查询字段值
    public List<DemandDO> getReqTaskByUK(DemandDO demandDO);

    /**
     * 查找最大内部用户号
     * @param
     */
    DemandDO getMaxInnerSeq();
}
