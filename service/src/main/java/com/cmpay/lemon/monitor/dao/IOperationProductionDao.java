package com.cmpay.lemon.monitor.dao;

        import com.cmpay.lemon.framework.dao.BaseDao;
        import com.cmpay.lemon.monitor.entity.ProductionDO;
        import com.cmpay.lemon.monitor.entity.sendemail.MailFlowDO;
        import com.cmpay.lemon.monitor.entity.sendemail.MailGroupDO;
        import org.apache.ibatis.annotations.Mapper;

/**
 * @author: ty
 */
@Mapper
public interface IOperationProductionDao extends BaseDao<ProductionDO, String>{

    // 查询一条邮箱组的详情
    MailGroupDO findMailGroupBeanDetail(String mail_group_id);
    //
    /**
     * 记录邮箱收发信息
     */
    void addMailFlow(MailFlowDO bean);
}
