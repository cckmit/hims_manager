/*
 * @ClassName IQuantitativeDataDao
 * @Description
 * @version 1.0
 * @Date 2020-11-02 16:52:35
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.QuantitativeDataDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IQuantitativeDataExtDao extends IQuantitativeDataDao {

    List<QuantitativeDataDO> findOne(QuantitativeDataDO entity);

}
