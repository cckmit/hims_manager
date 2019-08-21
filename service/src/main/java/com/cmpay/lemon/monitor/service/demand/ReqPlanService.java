package com.cmpay.lemon.monitor.service.demand;

import com.cmpay.lemon.framework.page.PageInfo;
import com.cmpay.lemon.monitor.bo.DemandBO;
import com.cmpay.lemon.monitor.bo.ProjectStartBO;

import java.util.List;
import java.util.Map;

/**
 * @author: tu_yi
 */
public interface ReqPlanService {
    /**
     * 通过id查找记录
     *
     * @param req_inner_seq
     * @return
     */
    DemandBO findById(String req_inner_seq);

    /**
     * 分页查询
     *
     * @param demandBO
     * @return
     */
    PageInfo<DemandBO> findDemand(DemandBO demandBO);

    /**
     * 新增
     *
     * @param demandBO
     */
    void add(DemandBO demandBO);

    /**
     * 删除
     *
     * @param req_inner_seq
     */
    void delete(String req_inner_seq);

    /**
     * 修改
     *
     * @param demandBO
     */
    void update(DemandBO demandBO);

    /**
     * 查找所有
     *
     * @return
     */
    List<DemandBO> findAll();

    /**
     * 根据内部编号查询项目启动信息
     * @param req_inner_seq
     * @return
     */
    ProjectStartBO goProjectStart(String req_inner_seq);

    /**
     * 根据编号获取邮箱
     */
    Map<String,String> getMailbox(String req_inner_seq);
}
