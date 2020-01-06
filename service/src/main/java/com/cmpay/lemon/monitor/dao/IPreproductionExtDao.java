/*
 * @ClassName IPreproductionDao
 * @Description 
 * @version 1.0
 * @Date 2019-12-25 11:24:02
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.PreproductionDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IPreproductionExtDao extends IPreproductionDao {
    //上传投产包，更新包名，上传时间
    void updatePropkg(PreproductionDO preproductionDO);

    void updatePreSts(PreproductionDO preproductionDO);
}