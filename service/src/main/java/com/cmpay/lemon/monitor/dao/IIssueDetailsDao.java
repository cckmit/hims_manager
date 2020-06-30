/*
 * @ClassName IIssueDetailsDao
 * @Description 
 * @version 1.0
 * @Date 2020-06-30 16:04:29
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.IssueDetailsDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IIssueDetailsDao extends BaseDao<IssueDetailsDO, String> {
}