package com.cmpay.lemon.monitor.service;


import com.cmpay.lemon.monitor.bo.OrdinarySearchBO;
import com.cmpay.lemon.monitor.query.SearchResult;

/**
 * @author: zhou_xiong
 */
public interface OrdinarySearchService {
    /**
     * 普通分页检索
     *
     * @param ordinarySearchBO
     * @return
     */
    SearchResult search(OrdinarySearchBO ordinarySearchBO);
}
