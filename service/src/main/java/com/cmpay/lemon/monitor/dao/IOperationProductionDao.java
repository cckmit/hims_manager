/*
 * @ClassName ICenterDao
 * @Description 
 * @version 1.0
 * @Date 2019-07-25 11:01:18
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.bo.ProblemBO;
import com.cmpay.lemon.monitor.entity.ProblemDO;
import com.cmpay.lemon.monitor.entity.ProductionDO;
import com.cmpay.lemon.monitor.entity.ScheduleDO;
import com.cmpay.lemon.monitor.entity.sendemail.MailFlowBean;
import com.cmpay.lemon.monitor.entity.sendemail.MailFlowConditionDO;
import com.cmpay.lemon.monitor.entity.sendemail.MailFlowDO;
import com.cmpay.lemon.monitor.entity.sendemail.MailGroupDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IOperationProductionDao extends BaseDao<ProductionDO, String> {
    /** 分页详情查询
     * @param vo 投产对象
     * @return  投产信息的list集合
     */
    List<ProductionDO> findPageBreakByCondition(ProductionDO vo);

    // 查询一条邮箱组的详情
    MailGroupDO findMailGroupBeanDetail(String mail_group_id);
    //
    /**
     * 记录邮箱收发信息
     */
    void addMailFlow(MailFlowDO bean);

    List<ProductionDO> findExportExcelListByDate(ProductionDO productionBean);
    // 查询一条投产记录的详情
    ProductionDO findProductionBean(String pro_number);
    //修改投产状态
    void updateProduction(ProductionDO bean);

    //插入投产流水记录
    void insertSchedule(ScheduleDO bean);

    //查询邮箱
    MailFlowDO searchUserEmail(MailFlowConditionDO vo);

    // 投产纪录导出列表查询
    ProductionDO findExportExcelList(String pro_number);

    /** 根据人员姓名查询各部门经理邮箱 */
    String findManagerMailByUserName(List<String> userNames);

    //记录邮箱收发信息
    void insertMailFlow(MailFlowBean bean);
    //修改产品信息
    void updateAllProduction(ProductionDO bean);

    void insertProduction(ProductionDO bean);
    //查询投产问题
    List<ProblemDO> findProblemInfo(String pro_number);

    List<MailGroupDO> findMailGroup(MailGroupDO mailGroupDO);

    List<ScheduleDO> findPageBreakBySchedule(ScheduleDO vo);
    // 投产审计导出列表查询
    ScheduleDO findOperationExcelList(int seq_id);

    void updateProblem(ProblemDO problemDO);

    void deleteProblemInfo(String proNumber1);

    void insertProblemInfo(ProblemBO problemBO);

    ProductionDO findDeptManager(String deptName);
}