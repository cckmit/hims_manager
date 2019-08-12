package com.cmpay.lemon.monitor.search.aggregation;

import com.cmpay.lemon.monitor.bo.CenterBO;
import com.cmpay.lemon.monitor.bo.GolbalStatBO;
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
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.cmpay.lemon.monitor.constant.MonitorConstants.*;
import static com.cmpay.lemon.monitor.utils.SearchUtils.countByApp;
import static com.cmpay.lemon.monitor.utils.SearchUtils.getUTC;

/**
 * @author: zhou_xiong
 */
@Component
public class TxCountAggregator extends AbstractEsSearcher implements Aggregator {
    private static final String AGG_NAME = "txCount";
    public static final ThreadLocal<Map> STATDATA_HOLDER = new ThreadLocal();

    @Autowired
    private CenterService centerService;

    public TxCountAggregator(RestHighLevelClient client) {
        super(client);
    }

    @Override
    public <T> SearchRequest buildRequest(T t) {
        LocalDateTime[] localDateTimes = getUTC(((GolbalStatBO) t).getLogPeriods());
        // 构造搜索条件
        BoolQueryBuilder builder = QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery(TIME_FILED).gte(localDateTimes[0]).lte(localDateTimes[1]))
                .should(QueryBuilders.existsQuery(APP_FIELD)).minimumShouldMatch(1);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(builder)
                .aggregation(AggregationBuilders.terms(AGG_NAME).field(APP_FIELD).size(200));
        return new SearchRequest().source(searchSourceBuilder).indices(TXNPLTE_INDICE).types(TYPE);
    }

    @Override
    public Map doStat(SearchResponse response) {
        Map<String, Long> resultMap = new LinkedHashMap<>(16);
        resultMap.put(ALL, 0L);
        Terms aggregation = response.getAggregations().get(AGG_NAME);
        List<? extends Terms.Bucket> buckets = aggregation.getBuckets();
        List<CenterBO> centerBOS = centerService.findAll();
        countByApp(resultMap, buckets, centerBOS);
        STATDATA_HOLDER.set(resultMap);
        return resultMap;
    }

}
