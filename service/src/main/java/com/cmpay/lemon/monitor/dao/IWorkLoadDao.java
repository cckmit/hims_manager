package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.monitor.bo.DemandBO;
import com.cmpay.lemon.monitor.entity.DemandDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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

    /**
     * 工作量导入数据变更
     * @param bean
     */
    void updateReqWorkLoadImport(DemandDO bean);

    /**
     * 查询工作量
     * @param bean
     * @return
     */
    List<DemandDO> findList(DemandDO bean);

    /**
     * 工作量修改
     * @param bean
     */
    void updateReqWorkLoad(DemandDO bean);
    List<DemandDO> goExportCountForDevp(DemandDO bean);
    DemandDO getReqWorkLoad(String req_inner_seq);
}
