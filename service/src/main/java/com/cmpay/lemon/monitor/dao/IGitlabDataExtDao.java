/*
 * @ClassName IGitlabDataDao
 * @Description
 * @version 1.0
 * @Date 2020-11-19 11:53:55
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.GitlabDataDO;
import com.cmpay.lemon.monitor.entity.WorkingHoursDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IGitlabDataExtDao extends IGitlabDataDao {
    /**
     * 周部门员工详情需求工时
     * @param workingHoursDO
     * @return
     */
    List<GitlabDataDO> findListWeekView(GitlabDataDO workingHoursDO);
    /**
     * 月部门员工详情需求工时
     * @param workingHoursDO
     * @return
     */
    List<GitlabDataDO> findListMonthView(GitlabDataDO workingHoursDO);
    List<GitlabDataDO> findWeekGit(GitlabDataDO workingHoursDO);
    List<GitlabDataDO> findMonthGit(GitlabDataDO workingHoursDO);
    List<GitlabDataDO> findWeekGitLab(GitlabDataDO workingHoursDO);
    List<GitlabDataDO> findMonthGitLab(GitlabDataDO workingHoursDO);

    List<GitlabDataDO> findWeekGitLabSum(GitlabDataDO workingHoursDO);
    List<GitlabDataDO> findMonthGitLabSum(GitlabDataDO workingHoursDO);
}
