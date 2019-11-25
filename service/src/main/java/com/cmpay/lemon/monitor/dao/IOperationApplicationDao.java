package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.OperationApplicationDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IOperationApplicationDao extends BaseDao<OperationApplicationDO, String> {
	// 操作审批基本信息查询
	OperationApplicationDO findBaseOperationalApplicationInfo(String operNumber);


	List<OperationApplicationDO> findPageBreakByCondition(OperationApplicationDO vo);

	//修改操作审批状态
	void updateOperationalApplication(OperationApplicationDO bean);

	//插入操作审批
	void insertOperationalApplication(OperationApplicationDO bean);

	//修改操作审批状态(全部)
	void updateAllOperationalApplication(OperationApplicationDO bean);

	List<OperationApplicationDO> getSystemEntryVerificationIsNotTimelyList(OperationApplicationDO operationApplicationDO);
}
