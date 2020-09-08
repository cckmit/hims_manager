/*
 * @ClassName IZenQuestiontDao
 * @Description
 * @version 1.0
 * @Date 2020-09-04 11:30:37
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.ZenQuestiontDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IZenQuestiontExtDao extends IZenQuestiontDao {
    List<ZenQuestiontDO> findList(ZenQuestiontDO zenQuestiontDO);
}
