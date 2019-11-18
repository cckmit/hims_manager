package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.DemandDO;
import com.cmpay.lemon.monitor.entity.ErcdmgErrorComditionDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * @author: ty
 */
@Mapper
public interface IErcdmgErorDao extends BaseDao<ErcdmgErrorComditionDO, String> {
    /**
     * 查询分页列表
     */
    List<ErcdmgErrorComditionDO> findErcdmgErrorList(ErcdmgErrorComditionDO vo);
}
