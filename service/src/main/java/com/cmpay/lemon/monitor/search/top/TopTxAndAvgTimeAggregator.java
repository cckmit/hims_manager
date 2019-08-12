package com.cmpay.lemon.monitor.search.top;

import com.cmpay.lemon.monitor.bo.TopStatBO;
import com.cmpay.lemon.monitor.search.AbstractEsSearcher;
import com.cmpay.lemon.monitor.search.Aggregator;
import com.cmpay.lemon.monitor.service.CenterService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.cmpay.lemon.monitor.constant.MonitorConstants.TIME_FILED;
import static com.cmpay.lemon.monitor.constant.MonitorConstants.TYPE;
import static com.cmpay.lemon.monitor.utils.SearchUtils.getUTC;

/**
 * @author: zhou_xiong
 */
@Component
public class TopTxAndAvgTimeAggregator extends AbstractEsSearcher implements Aggregator {
    private static final String TOP_AGG_NAME = "topAgg";
    private static final String TOP_AVG_AGG_NAME = "topAvgAgg";
    @Autowired
    private CenterService centerService;

    public TopTxAndAvgTimeAggregator(RestHighLevelClient client) {
        super(client);
    }

    @Override
    public <T> SearchRequest buildRequest(T t) {
        TopStatBO topStatBO = (TopStatBO) t;
        LocalDateTime[] localDateTimes = getUTC(topStatBO.getLogPeriods());
        List<String> appsOfCenter = centerService.getAppsByName(topStatBO.getCenterName());
        BoolQueryBuilder builder = QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery(TIME_FILED).gte(localDateTimes[0]).lte(localDateTimes[1]))
                .filter(QueryBuilders.boolQuery().should(QueryBuilders.termsQuery(APP_FIELD, appsOfCenter.toArray(new String[0])).boost(10)));
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(builder)
                .aggregation(AggregationBuilders.terms(TOP_AGG_NAME).field(TXCODE_FIELD).size(10).subAggregation(AggregationBuilders.avg(TOP_AVG_AGG_NAME).field(TX_DURATION)));
        return new SearchRequest().source(searchSourceBuilder).indices(TXNPLTE_INDICE).types(TYPE);
    }

    @Override
    public List<TopStatBO> doStat(SearchResponse response) {
        List<TopStatBO> result = new ArrayList<>();
        Terms aggregation = response.getAggregations().get(TOP_AGG_NAME);
        List<? extends Terms.Bucket> buckets = aggregation.getBuckets();
        buckets.forEach(b -> {
            TopStatBO topStatBO = new TopStatBO();
            topStatBO.setTxCode((String) b.getKey());
            topStatBO.setTxCount(b.getDocCount());
            Avg avg = b.getAggregations().get(TOP_AVG_AGG_NAME);
            topStatBO.setAvgDuration((long) avg.getValue());
            result.add(topStatBO);
        });
        return result;
    }
}
