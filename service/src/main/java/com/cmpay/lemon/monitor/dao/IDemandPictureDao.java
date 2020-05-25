/*
 * @ClassName IDemandPictureDao
 * @Description 
 * @version 1.0
 * @Date 2020-05-25 17:03:08
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.DemandPictureDO;
import com.cmpay.lemon.monitor.entity.DemandPictureDOKey;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IDemandPictureDao extends BaseDao<DemandPictureDO, DemandPictureDOKey> {
}