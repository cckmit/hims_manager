/*
 * @ClassName ITestProgressDetailDao
 * @Description
 * @version 1.0
 * @Date 2020-08-18 10:50:32
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.TestProgressDetailDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ITestProgressDetailExtDao extends ITestProgressDetailDao {
    List<String> findListLine(TestProgressDetailDO testProgressDetailDO);
    List<String> findListEpic(TestProgressDetailDO testProgressDetailDO);
    List<TestProgressDetailDO> findListDate(TestProgressDetailDO testProgressDetailDO);
    List<TestProgressDetailDO> findListPriorDate(TestProgressDetailDO testProgressDetailDO);
}
