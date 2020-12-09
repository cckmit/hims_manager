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

import java.util.List;

@Mapper
public interface IPreproductionExtDao extends IPreproductionDao {
    List<PreproductionDO> findList(PreproductionDO preproductionDO);
    //上传投产包，更新包名，上传时间
    void updatePropkg(PreproductionDO preproductionDO);
    //上传投产包，更新包名，上传时间
    void updateDbapkg(PreproductionDO preproductionDO);
    void updatePreSts(PreproductionDO preproductionDO);
    void updateAgain(PreproductionDO preproductionDO);
    void updatePreDBA(PreproductionDO preproductionDO);
}
