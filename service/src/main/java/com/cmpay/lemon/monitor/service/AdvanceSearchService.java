package com.cmpay.lemon.monitor.service;

import com.cmpay.lemon.monitor.bo.AdvanceSearchBO;
import com.cmpay.lemon.monitor.query.SearchResult;

/**
 * 高级检索 服务接口
 * Created on 2019/1/25
 *
 * @author: zhou_xiong
 */

public interface AdvanceSearchService {
    /**
     * 多条件分页查询应用日志
     */
    SearchResult advanceSearch(AdvanceSearchBO advanceSearchBO);
}
