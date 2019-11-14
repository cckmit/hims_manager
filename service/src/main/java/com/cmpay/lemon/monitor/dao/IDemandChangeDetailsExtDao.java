/*
 * @ClassName IDemandChangeDetailsDao
 * @Description 
 * @version 1.0
 * @Date 2019-11-13 10:55:00
 */
package com.cmpay.lemon.monitor.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IDemandChangeDetailsExtDao extends IDemandChangeDetailsDao {

    String getIdentificationByReqInnerSeq(String reqInnerSeq);

}