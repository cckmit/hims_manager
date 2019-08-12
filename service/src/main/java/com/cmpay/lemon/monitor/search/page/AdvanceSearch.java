package com.cmpay.lemon.monitor.search.page;

import com.cmpay.lemon.common.utils.DateTimeUtils;
import com.cmpay.lemon.common.utils.JudgeUtils;
import com.cmpay.lemon.monitor.bo.AdvanceSearchBO;
import com.cmpay.lemon.monitor.query.SearchResult;
import com.cmpay.lemon.monitor.search.AbstractEsSearcher;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
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
public class AdvanceSearch extends AbstractEsSearcher {
    public AdvanceSearch(RestHighLevelClient client) {
        super(client);
    }

    @Override
    public <T> SearchRequest buildRequest(T t) {
        AdvanceSearchBO advanceSearchBO = Optional.ofNullable(t).filter(a -> a instanceof AdvanceSearchBO)
                .map(b -> (AdvanceSearchBO) b).orElse(new AdvanceSearchBO());
        return getSearchRequest(advanceSearchBO);
    }

    private SearchRequest getSearchRequest(AdvanceSearchBO advanceSearchBO) {
        String start = advanceSearchBO.getLogPeriods()[0];
        String end = advanceSearchBO.getLogPeriods()[1];
        String logType = advanceSearchBO.getLogType().trim();
        String[] indices = getIndicesByLogTypeAndDataStr(start, end, logType);
        LocalDateTime startTime = DateTimeUtils.parseLocalDateTime(start).minusHours(8);
        LocalDateTime endTime = DateTimeUtils.parseLocalDateTime(end).minusHours(8);
        String keywords = advanceSearchBO.getKeywords();
        // 构造搜索条件
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        List<QueryBuilder> filters = addTimeAndKeywordCriteria(startTime, endTime, keywords, builder);
        addOtherCriteria(advanceSearchBO, filters);
        SearchSourceBuilder searchSourceBuilder = buildSearchSource(advanceSearchBO, builder);
        return new SearchRequest().source(searchSourceBuilder).indices(indices).types(TYPE);
    }

    private void addOtherCriteria(AdvanceSearchBO advanceSearchBO, List<QueryBuilder> filters) {
        String logApp = advanceSearchBO.getLogApp().trim();
        String logRequestId = advanceSearchBO.getLogRequestId().trim();
        String from = advanceSearchBO.getFrom().trim();
        String to = advanceSearchBO.getTo().trim();
        String logTxCode = advanceSearchBO.getLogTxCode().trim();
        String logTxReturn = advanceSearchBO.getLogTxReturn().trim();
        String source = advanceSearchBO.getSource().trim();
        if (JudgeUtils.isNotBlank(logApp)) {
            filters.add(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("fields.log_app", logApp)).minimumShouldMatch(1));
        }
        if (JudgeUtils.isNotBlank(logRequestId)) {
            filters.add(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("log_requestId", logRequestId)).minimumShouldMatch(1));
        }
        if (JudgeUtils.isNotBlank(logTxCode)) {
            filters.add(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("log_txCode", logTxCode)).minimumShouldMatch(1));
        }
        if (JudgeUtils.isNotBlank(logTxReturn)) {
            filters.add(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("log_txReturn", logTxReturn)).minimumShouldMatch(1));
        }
        if (JudgeUtils.isNotBlank(source)) {
            filters.add(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("source", source)).minimumShouldMatch(1));
        }
        if (JudgeUtils.isNotBlank(from)) {
            filters.add(QueryBuilders.boolQuery().should(QueryBuilders.rangeQuery("log_txDuration").gte(from)).minimumShouldMatch(1));
        }
        if (JudgeUtils.isNotBlank(to)) {
            filters.add(QueryBuilders.boolQuery().should(QueryBuilders.rangeQuery("log_txDuration").lte(to)).minimumShouldMatch(1));
        }
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
}
