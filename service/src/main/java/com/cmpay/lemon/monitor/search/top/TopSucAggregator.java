package com.cmpay.lemon.monitor.search.top;

import com.cmpay.lemon.monitor.bo.TopStatBO;
import com.cmpay.lemon.monitor.search.AbstractEsSearcher;
import com.cmpay.lemon.monitor.search.Aggregator;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.cmpay.lemon.monitor.constant.MonitorConstants.TIME_FILED;
import static com.cmpay.lemon.monitor.constant.MonitorConstants.TYPE;
import static com.cmpay.lemon.monitor.utils.SearchUtils.bucketAsMap;
import static com.cmpay.lemon.monitor.utils.SearchUtils.getUTC;

/**
 * @author: zhou_xiong
 */
@Component
public class TopSucAggregator extends AbstractEsSearcher implements Aggregator {
    private static final String TOP_SUC_AGG_NAME = "topSucAgg";

    public TopSucAggregator(RestHighLevelClient client) {
        super(client);
    }

    @Override
    public <T> SearchRequest buildRequest(T t) {
        List<TopStatBO> topStatBOS = (List<TopStatBO>) t;
        LocalDateTime[] localDateTimes = getUTC(topStatBOS.get(0).getLogPeriods());
        BoolQueryBuilder builder = QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery(TIME_FILED).gte(localDateTimes[0]).lte(localDateTimes[1]))
                .filter(QueryBuilders.boolQuery().should(QueryBuilders.queryStringQuery(SUCCESS_CRITERIA).field(TX_RETRUN)).minimumShouldMatch(1));
        List<QueryBuilder> filters = builder.filter();
        BoolQueryBuilder txCodeBoolQuery = QueryBuilders.boolQuery();
        topStatBOS.forEach(b ->
            filters.add(txCodeBoolQuery.should(QueryBuilders.matchPhraseQuery(TXCODE_FIELD, b.getTxCode())).minimumShouldMatch(1))
        );
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(builder)
                .aggregation(AggregationBuilders.terms(TOP_SUC_AGG_NAME).field(TXCODE_FIELD).size(10));
        return new SearchRequest().source(searchSourceBuilder).indices(TXNPLTE_INDICE).types(TYPE);
    }


    @Override
    public Map<String, Long> doStat(SearchResponse response) {
        return bucketAsMap(response.getAggregations().get(TOP_SUC_AGG_NAME));
    }
}
