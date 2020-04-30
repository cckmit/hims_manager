/*
 * @ClassName IJiraWorklogDao
 * @Description 
 * @version 1.0
 * @Date 2020-04-30 15:52:09
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.JiraWorklogDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IJiraWorklogDao extends BaseDao<JiraWorklogDO, String> {
}