/*
 * @ClassName IVerificationResultsFeedbackDao
 * @Description 
 * @version 1.0
 * @Date 2021-02-02 16:57:23
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.VerificationResultsFeedbackDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IVerificationResultsFeedbackDao extends BaseDao<VerificationResultsFeedbackDO, Long> {
}