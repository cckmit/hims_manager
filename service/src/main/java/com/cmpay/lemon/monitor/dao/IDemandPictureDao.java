/*
 * @ClassName IDemandPictureDao
 * @Description
 * @version 1.0
 * @Date 2020-05-27 15:28:00
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.DemandPictureDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IDemandPictureDao extends BaseDao<DemandPictureDO, Integer> {
    DemandPictureDO findOne(String picReqinnerseq);
    DemandPictureDO showOne(DemandPictureDO demandPictureDO);

}
