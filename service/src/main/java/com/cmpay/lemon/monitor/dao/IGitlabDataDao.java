/*
 * @ClassName IGitlabDataDao
 * @Description 
 * @version 1.0
 * @Date 2020-11-20 15:21:41
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.GitlabDataDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IGitlabDataDao extends BaseDao<GitlabDataDO, String> {
}