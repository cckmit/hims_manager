package com.cmpay.lemon.monitor.service.production;


import com.cmpay.lemon.monitor.bo.ProductionBO;
import com.cmpay.lemon.monitor.bo.ProductionRspBO;
import com.cmpay.lemon.monitor.entity.ProductionPicDO;
import com.cmpay.lemon.monitor.entity.ScheduleDO;
import com.cmpay.lemon.monitor.entity.sendemail.MailFlowBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

/**
 * @author: zhou_xiong
 */
public interface OperationProductionService {

    /**
     * 分页查询
     *
     * @param productionBO
     * @return
     */
    ProductionRspBO find(ProductionBO productionBO);
    void exportExcel(HttpServletRequest request, HttpServletResponse response, ProductionBO productionBO);

    void updateAllProduction(HttpServletRequest request, HttpServletResponse response, String str);


    /** 根据人员姓名查询各部门经理邮箱*/
    String findManagerMailByUserName(List<String> userNames);

    void addMailFlow(MailFlowBean bnb);

    // 导出非投产日正常投产申请表EXCEL
    File exportUnusualExcel(HttpServletResponse response,List<ProductionBO> list);

    // 导出紧急更新申请表EXCEL
    File exportUrgentExcel(HttpServletResponse response,List<ProductionBO> list);
    // 查询一条投产记录的详情
    ProductionBO searchProdutionDetail(String proNumber);


    void updateAllProduction(ProductionBO bean);

    void addScheduleBean(ScheduleDO scheduleBean);

    void addProduction(ProductionBO bean);
    /** 保存图片基本信息*/
    void addProductionPicBean(ProductionPicDO productionPicDO);
}
