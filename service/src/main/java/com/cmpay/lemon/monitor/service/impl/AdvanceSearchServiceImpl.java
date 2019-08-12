package com.cmpay.lemon.monitor.service.impl;

import com.cmpay.lemon.monitor.bo.AdvanceSearchBO;
import com.cmpay.lemon.monitor.query.SearchResult;
import com.cmpay.lemon.monitor.search.EsSearcher;
import com.cmpay.lemon.monitor.service.AdvanceSearchService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author zhou_xiong
 */
@Service
public class AdvanceSearchServiceImpl implements AdvanceSearchService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdvanceSearchServiceImpl.class);
    @Autowired
    @Qualifier("advanceSearch")
    private EsSearcher esSearcher;

    @Override
    public SearchResult advanceSearch(AdvanceSearchBO advanceSearchBO) {
        SearchResponse searchResponse = null;
        SearchRequest searchRequest = esSearcher.buildRequest(advanceSearchBO);
        long start = System.currentTimeMillis();
        try {
            searchResponse = esSearcher.query(searchRequest);
            LOGGER.debug("advance search elapsed: {}ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            esSearcher.exception(e);
        }
        return esSearcher.responseProcess(searchResponse);
    }
}

