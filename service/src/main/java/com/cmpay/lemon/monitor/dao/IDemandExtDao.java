package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.monitor.entity.DemandDO;
import com.cmpay.lemon.monitor.entity.WorkingHoursDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author: zhou_xiong
 */
@Mapper
public interface IDemandExtDao extends IDemandDao {

    public List<DemandDO> getReqTaskByUK(DemandDO demandDO);
    // 查询是否有相同编号
    public List<DemandDO> getReqTaskByNo(DemandDO demandDO);


    public List<DemandDO> getReqTaskByUKImpl(DemandDO demandDO);

    public List<DemandDO> getReqTask(DemandDO demandDO);
    public List<DemandDO> getReqPlan(DemandDO demandDO);

    /**
     * 查找最大内部用户号
     * @param
     */
    DemandDO getMaxInnerSeq();
    /**
     * 找到上月未完成的需求
     * @param month
     * @return
     */
    List<DemandDO> findUnFinishReq(String month);
    /**
     * 找到上月已完成但工作量未录入完
     * @param month
     * @return
     */
    List<DemandDO> findUnFinishReq1(String month);

    /**
     * 插入存量需求
     */
    void insertStockReq(DemandDO demand);
    //需求计划页面update
    void updateReqPlanJsp(DemandDO bean);
    /**
     * 插入存量需求
     */
    void updateStockReq(DemandDO demand);

    List<DemandDO> getExtraTm(DemandDO bean);

    void insertExtraTm(DemandDO bean);

    void updateExtraTm(DemandDO bean);

    void updatePreCurPeriod(DemandDO demand);

    void updateReqSts(DemandDO demandDO);

    void logicDelete(String reqInnerSeq);
    //获取需求进度异常需求
    List<DemandDO> getPrdFnishAbnor(String month);
    //获取测试进度异常需求
    List<DemandDO> getTestFnishAbnor(String month);
    //获取开发进度异常需求
    List<DemandDO> getUatUpdateAbnor(String month);
    //修改需求异常状态
    void updateReqAbnorType(DemandDO bean);
    List<DemandDO> getPrdFnishWarn();
    List<DemandDO> getTestFnishWarn();

    List<DemandDO> getUatUpdateWarn();

    List<DemandDO> getNormalExecutionDemand(DemandDO demandDO);

    void WeedAndMonthFeedback(DemandDO demandDO);
    void updateOperation(DemandDO demandDO);


    List<DemandDO> findListDeptDemand(WorkingHoursDO workingHoursDO);
    List<DemandDO> findListDevpCoorDeptDemand(WorkingHoursDO workingHoursDO);
}
