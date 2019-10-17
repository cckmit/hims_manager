/*
 * @ClassName ICenterDao
 * @Description 
 * @version 1.0
 * @Date 2019-07-25 11:01:18
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.ProductionDO;
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
}