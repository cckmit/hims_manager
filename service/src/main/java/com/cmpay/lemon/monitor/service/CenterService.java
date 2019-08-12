package com.cmpay.lemon.monitor.service;

import com.cmpay.lemon.framework.page.PageInfo;
import com.cmpay.lemon.monitor.bo.CenterBO;

import java.util.List;

/**
 * @author: zhou_xiong
 */
public interface CenterService {
    /**
     * 通过id查找记录
     *
     * @param centerId
     * @return
     */
    CenterBO findById(Long centerId);

    /**
     * 分页查询
     *
     * @param centerBO
     * @return
     */
    PageInfo<CenterBO> findCenters(CenterBO centerBO);

    /**
     * 新增
     *
     * @param centerBO
     */
    void add(CenterBO centerBO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);

    /**
     * 修改
     *
     * @param centerBO
     */
    void update(CenterBO centerBO);

    /**
     * 查找所有
     *
     * @return
     */
    List<CenterBO> findAll();

    /**
     * 通过中台名称查询中台下的应用
     *
     * @param centerName
     * @return
     */
    List<String> getAppsByName(String centerName);
}
