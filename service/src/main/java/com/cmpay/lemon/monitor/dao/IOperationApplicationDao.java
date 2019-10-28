package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.OperationApplicationDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IOperationApplicationDao extends BaseDao<OperationApplicationDO, String> {
	// 操作审批基本信息查询
	OperationApplicationDO findBaseOperationalApplicationInfo(String operNumber);
}
