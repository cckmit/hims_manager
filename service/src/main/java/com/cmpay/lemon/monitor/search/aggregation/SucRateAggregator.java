package com.cmpay.lemon.monitor.search.aggregation;

import com.cmpay.lemon.monitor.bo.CenterBO;
import com.cmpay.lemon.monitor.bo.GolbalStatBO;
import com.cmpay.lemon.monitor.search.AbstractEsSearcher;
import com.cmpay.lemon.monitor.search.Aggregator;
import com.cmpay.lemon.monitor.service.CenterService;
import com.cmpay.lemon.monitor.utils.AmountUtil;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.cmpay.lemon.monitor.constant.MonitorConstants.*;
import static com.cmpay.lemon.monitor.search.aggregation.TxCountAggregator.STATDATA_HOLDER;
import static com.cmpay.lemon.monitor.utils.SearchUtils.countByApp;
import static com.cmpay.lemon.monitor.utils.SearchUtils.getUTC;

/**
 * @author: zhou_xiong
 */
@Component
public class SucRateAggregator extends AbstractEsSearcher implements Aggregator {
    private static final String AGG_NAME = "sucRate";

    @Autowired
    private CenterService centerService;

    public SucRateAggregator(RestHighLevelClient client) {
        super(client);
    }

    @Override
    public <T> SearchRequest buildRequest(T t) {
        LocalDateTime[] localDateTimes = getUTC(((GolbalStatBO) t).getLogPeriods());
        BoolQueryBuilder builder = QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery(TIME_FILED).gte(localDateTimes[0]).lte(localDateTimes[1]))
                .filter(QueryBuilders.boolQuery().should(QueryBuilders.queryStringQuery(SUCCESS_CRITERIA).field(TX_RETRUN)).minimumShouldMatch(1));
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(builder)
                .aggregation(AggregationBuilders.terms(AGG_NAME).field(APP_FIELD).size(200));
        return new SearchRequest().source(searchSourceBuilder).indices(TXNPLTE_INDICE).types(TYPE);
    }

    @Override
    public Map doStat(SearchResponse response) {
        Map<String, Long> sucCountMap = new LinkedHashMap<>(16);
        sucCountMap.put(ALL, null);
        Terms aggregation = response.getAggregations().get(AGG_NAME);
        List<? extends Terms.Bucket> buckets = aggregation.getBuckets();
        List<CenterBO> centerBOS = centerService.findAll();
        countByApp(sucCountMap, buckets, centerBOS);
        Map<String, Long> txCountMap = STATDATA_HOLDER.get();
        Map<String, BigDecimal> resultMap = new LinkedHashMap<>(16);
        txCountMap.forEach((k, v) -> {
            if (v != 0) {
                resultMap.put(k, AmountUtil.div(new BigDecimal(String.valueOf(sucCountMap.get(k) * 100)), new BigDecimal(v.toString())));
            } else {
                resultMap.put(k, BigDecimal.ZERO);
            }
        });
        return resultMap;
    }

}
