/*
 * @ClassName IZenQuestiontDao
 * @Description 
 * @version 1.0
 * @Date 2020-09-07 10:14:44
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.ZenQuestiontDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IZenQuestiontDao extends BaseDao<ZenQuestiontDO, String> {
}