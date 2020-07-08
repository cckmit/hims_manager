/*
 * @ClassName IIssueDetailsDao
 * @Description
 * @version 1.0
 * @Date 2020-06-30 16:04:29
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.monitor.entity.IssueDetailsDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IIssueDetailsExtDao extends IIssueDetailsDao {

    List<IssueDetailsDO> findNotCompleted(IssueDetailsDO issueDetailsDO);
    List<IssueDetailsDO> findList(IssueDetailsDO entity);
    List<IssueDetailsDO> findWeekList(IssueDetailsDO entity);
}
