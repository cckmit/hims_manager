package com.cmpay.lemon.monitor.service;

import com.cmpay.lemon.framework.page.PageInfo;
import com.cmpay.lemon.monitor.bo.SysLogBO;
import com.cmpay.lemon.monitor.bo.SysLogPageBO;

/**
 * 系统日志表 服务接口
 * Created on 2018/12/24
 *
 * @author: zhou_xiong
 */

public interface SysLogService {
    /**
     * 多条件分页查询
     * @param sysLogPageBO
     * @return
     */
    PageInfo<SysLogBO> pageQuery(SysLogPageBO sysLogPageBO);

    /**
     * 新增
     * @param sysLogBO
     */
    void insert(SysLogBO sysLogBO);
}
