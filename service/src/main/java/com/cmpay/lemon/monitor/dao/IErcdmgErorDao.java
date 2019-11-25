package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.DemandDO;
import com.cmpay.lemon.monitor.entity.ErcdmgErrorComditionDO;
import com.cmpay.lemon.monitor.entity.ErrorRecordBeanDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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

    /**
     * 查询sit测试数据
     * @param errorCd
     * @return
     */
    String selectPubtmsg(String errorCd);
    /**
     * 查询sit测试数据
     * @param errorCd
     * @return
     */
    public String selectPubttms(@Param("errorCd") String errorCd, @Param("buscnl")  String buscnl);

    void insertPubtmsg(ErcdmgErrorComditionDO ercdmgError);

    void insertPubttms(ErcdmgErrorComditionDO ercdmgError);

    public ErcdmgErrorComditionDO checkErrorCodeExist(@Param("errorCd") String errorCd, @Param("buscnl")  String buscnl);


    /**
     * 插入存量需求
     */
    void insertErrorRecordBean(ErrorRecordBeanDO errorRecordBeanDO);
}
