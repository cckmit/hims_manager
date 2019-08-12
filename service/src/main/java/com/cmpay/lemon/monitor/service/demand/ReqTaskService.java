package com.cmpay.lemon.monitor.service.demand;

import com.cmpay.lemon.framework.page.PageInfo;
import com.cmpay.lemon.monitor.bo.DemandBO;

import java.util.List;

/**
 * @author: zhou_xiong
 */
public interface ReqTaskService {
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

}
