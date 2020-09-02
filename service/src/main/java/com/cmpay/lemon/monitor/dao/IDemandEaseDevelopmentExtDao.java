/*
 * @ClassName IDemandEaseDevelopmentDao
 * @Description
 * @version 1.0
 * @Date 2020-08-31 14:33:51
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.DemandEaseDevelopmentDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IDemandEaseDevelopmentExtDao extends IDemandEaseDevelopmentDao {
    List<DemandEaseDevelopmentDO>findList(DemandEaseDevelopmentDO demandEaseDevelopmentDO);

    /**
     * 获取一级部门支撑工作量汇总
     * @param supportWorkloadDO
     * @return
     */
    List<DemandEaseDevelopmentDO> easeDevelopmentWorkloadCountForDevp(DemandEaseDevelopmentDO supportWorkloadDO);
    /**
     * 获取二级部门支撑工作量汇总
     * @param supportWorkloadDO
     * @return
     */
    List<DemandEaseDevelopmentDO> easeDevelopmentWorkloadCountForDevp2(DemandEaseDevelopmentDO supportWorkloadDO);
}
