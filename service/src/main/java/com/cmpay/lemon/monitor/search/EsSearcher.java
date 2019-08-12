package com.cmpay.lemon.monitor.search;

import com.cmpay.lemon.monitor.query.SearchResult;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;

import java.io.IOException;

/**
 * @author: zhou_xiong
 */
public interface EsSearcher {
    /**
     * 构造搜索请求
     *
     * @param t
     * @param <T>
     * @return
     */
    <T> SearchRequest buildRequest(T t);

    /**
     * 发送搜索请求
     *
     * @param request
     * @return
     * @throws IOException
     */
    SearchResponse query(SearchRequest request) throws IOException;

    /**
     * 搜索请求失败的处理
     *
     * @param e
     */
    void exception(Exception e);

    /**
     * 搜索返回结果的处理
     *
     * @param response
     * @return
     */
    SearchResult responseProcess(SearchResponse response);
}
