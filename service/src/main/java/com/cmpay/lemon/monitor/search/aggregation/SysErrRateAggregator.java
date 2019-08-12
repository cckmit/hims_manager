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
public class SysErrRateAggregator extends AbstractEsSearcher implements Aggregator {
    private static final String AGG_NAME = "sysErrRate";

    @Autowired
    private CenterService centerService;

    public SysErrRateAggregator(RestHighLevelClient client) {
        super(client);
    }

    @Override
    public <T> SearchRequest buildRequest(T t) {
        LocalDateTime[] localDateTimes = getUTC(((GolbalStatBO) t).getLogPeriods());
        BoolQueryBuilder builder = QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery(TIME_FILED).gte(localDateTimes[0]).lte(localDateTimes[1]))
                .filter(QueryBuilders.boolQuery().filter(QueryBuilders.boolQuery().mustNot(QueryBuilders.boolQuery().should(QueryBuilders.matchPhraseQuery(TX_RETRUN, ICS_SYSERROR_BUTNOT))).minimumShouldMatch(1))).
                        filter(QueryBuilders.boolQuery().should(QueryBuilders.boolQuery().should(QueryBuilders.queryStringQuery(ICS_SYSERROR).field(TX_RETRUN))).minimumShouldMatch(1)
                                .should(QueryBuilders.boolQuery().should(QueryBuilders.queryStringQuery(LEMON_SYSERROR).field(TX_RETRUN)).minimumShouldMatch(1)));
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(builder)
                .aggregation(AggregationBuilders.terms(AGG_NAME).field(APP_FIELD).size(200));
        return new SearchRequest().source(searchSourceBuilder).indices(TXNPLTE_INDICE).types(TYPE);
    }

    @Override
    public Map doStat(SearchResponse response) {
        Map<String, Long> sysErrCountMap = new LinkedHashMap<>(16);
        sysErrCountMap.put(ALL, null);
        List<? extends Terms.Bucket> buckets = getBuckets(response);
        List<CenterBO> centerBOS = centerService.findAll();
        countByApp(sysErrCountMap, buckets, centerBOS);
        Map<String, Long> txCountMap = STATDATA_HOLDER.get();
        Map<String, BigDecimal> resultMap = new LinkedHashMap<>(16);
        txCountMap.forEach((k, v) -> {
            if (v != 0) {
                resultMap.put(k, AmountUtil.div(new BigDecimal(String.valueOf(sysErrCountMap.get(k)*100)), new BigDecimal(v.toString())));
            } else {
                resultMap.put(k, BigDecimal.ZERO);
            }
        });
        return resultMap;
    }

    private List<? extends Terms.Bucket> getBuckets(SearchResponse response) {
        Terms aggregation = response.getAggregations().get(AGG_NAME);
        return aggregation.getBuckets();
    }

}
