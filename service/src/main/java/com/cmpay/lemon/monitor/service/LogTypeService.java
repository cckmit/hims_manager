package com.cmpay.lemon.monitor.service;


import com.cmpay.lemon.monitor.bo.LogTypeBO;

/**
 * 日志类型 服务接口
 * Created on 2019/1/16
 *
 * @author: zhou_xiong
 */

public interface LogTypeService {
    /**
     * 查询日志类型列表
     */
    LogTypeBO findAll();
}
