package com.cmpay.lemon.monitor.search;

import org.elasticsearch.action.search.SearchResponse;

/**
 * @author: zhou_xiong
 */
public interface Aggregator extends EsSearcher {
    /**
     * 聚合统计
     *
     * @param response
     * @return
     */
    <T> T doStat(SearchResponse response);
}
