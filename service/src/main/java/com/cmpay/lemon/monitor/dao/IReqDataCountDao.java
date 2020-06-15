
package com.cmpay.lemon.monitor.dao;


import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface IReqDataCountDao extends BaseDao<DemandDO,String> {
	ReqDataCountDO getProg(String reportStartMon, String reportEndMon);

	ReqDataCountDO getProgDetl(String reportStartMon, String reportEndMon);

	List<ReqDataCountDO> getAbnoByDept(String reportStartMon, String reportEndMon);

	List<ReqDataCountDO> getAbnoByLine(String reportStartMon, String reportEndMon);

	List<ReqDataCountDO> getProgByDept(String reportStartMon, String reportEndMon);

	List<ReqDataCountDO> getProgDetlByDept(String reportStartMon, String reportEndMon);

	//需求实施总体统计
	List<ReqDataCountDO> getImpl(String reqImplMon);

	//各部门需求实施情况
	List<ReqDataCountDO> getImplByDept(String reqImplMon);

	//需求完成总体统计
	List<ReqDataCountDO> getComp(String reqImplMon);

	//需求完成情况统计
	List<ReqDataCountDO> getCompByDept(String reqImplMon);
	//需求完成情况统计
	List<ReqDataCountDO> getCompByDept2(String reqImplMon);

	//需求类型统计
	List<ReqDataCountDO> getReqSts(String reqImplMon);

	//投产录入不及时
	List<ScheduleDO> getProduction(String reqImplMon);

	//需求文档上传情况
	List<DemandDO> getReportForm6(ReqDateCountDO reqDateCountDO);

	//按基地归属部门统计报表
	List<ReqDataCountDO> getStageByJd(String reqImplMon);

	//查询本月需求详情
	List<ReqDataCountDO> selectDetl(ReqMngDO vo);
	Integer findNumberByCondition(ReqMngDO vo);

	//按中心经理查询
    ReqDataCountDO selectByCenter(ReqMngDO vo);

	//按产品查询
    ReqDataCountDO selectByProduct(ReqMngDO vo);

	//按测试查询
    ReqDataCountDO selectByTest(ReqMngDO vo);

	//按开发查询
    ReqDataCountDO selectByEng(ReqMngDO vo);
    @Override
	List<DemandDO> find(DemandDO bean);

}

