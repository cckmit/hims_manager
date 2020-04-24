/*
 * @ClassName IVpnInfoDao
 * @Description 
 * @version 1.0
 * @Date 2020-04-22 09:53:42
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.VpnInfoDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IVpnInfoDao extends BaseDao<VpnInfoDO, String> {
    /**
     * 查找最大内部用户号
     * @param
     */
    VpnInfoDO getMaxInnerSeq();
}