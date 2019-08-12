package com.cmpay.lemon.monitor.search.page;

import com.cmpay.lemon.common.utils.DateTimeUtils;
import com.cmpay.lemon.monitor.bo.OrdinarySearchBO;
import com.cmpay.lemon.monitor.query.SearchResult;
import com.cmpay.lemon.monitor.search.AbstractEsSearcher;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.cmpay.lemon.monitor.constant.MonitorConstants.TYPE;
import static com.cmpay.lemon.monitor.utils.SearchUtils.*;

/**
 * @author: zhou_xiong
 */
@Component
public class OrdinarySearch extends AbstractEsSearcher {

    public OrdinarySearch(RestHighLevelClient client) {
        super(client);
    }

    @Override
    public <T> SearchRequest buildRequest(T t) {
        OrdinarySearchBO ordinarySearchBO = Optional.ofNullable(t).filter(a -> a instanceof OrdinarySearchBO)
                .map(b -> (OrdinarySearchBO) b).orElse(new OrdinarySearchBO());
        return getSearchRequest(ordinarySearchBO);
    }

    @Override
    public SearchResult responseProcess(SearchResponse response) {
        List<Map<String, Object>> list = new ArrayList<>();
        SearchHit[] hits = response.getHits().getHits();
        for (SearchHit hit : hits) {
            list.add(resultReplaceHighlightField(hit));
        }
        return new SearchResult(response.getHits().totalHits, list);
    }

    private SearchRequest getSearchRequest(OrdinarySearchBO ordinarySearchBO) {
        String start = ordinarySearchBO.getLogPeriods()[0];
        String end = ordinarySearchBO.getLogPeriods()[1];
        String[] indices = getIndicesByDateStr(start, end);
        LocalDateTime startTime = DateTimeUtils.parseLocalDateTime(start).minusHours(8);
        LocalDateTime endTime = DateTimeUtils.parseLocalDateTime(end).minusHours(8);
        String keywords = ordinarySearchBO.getKeyword();
        // 构造搜索条件
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        addTimeAndKeywordCriteria(startTime, endTime, keywords, builder);
        SearchSourceBuilder searchSourceBuilder = buildSearchSource(ordinarySearchBO, builder);
        return new SearchRequest().source(searchSourceBuilder).indices(indices).types(TYPE);
    }

}
