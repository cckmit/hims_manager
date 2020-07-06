/*
 * @ClassName IProUnhandledIssuesDao
 * @Description 
 * @version 1.0
 * @Date 2020-07-03 17:03:49
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.ProUnhandledIssuesDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IProUnhandledIssuesDao extends BaseDao<ProUnhandledIssuesDO, String> {
}