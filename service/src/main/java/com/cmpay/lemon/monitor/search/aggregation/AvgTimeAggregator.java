package com.cmpay.lemon.monitor.search.aggregation;

import com.cmpay.lemon.common.utils.StringUtils;
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
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.cmpay.lemon.monitor.constant.MonitorConstants.*;
import static com.cmpay.lemon.monitor.utils.SearchUtils.getUTC;

/**
 * @author: zhou_xiong
 */
@Component
public class AvgTimeAggregator extends AbstractEsSearcher implements Aggregator {
    private static final String TERMS_AGG_NAME = "avgTime";
    private static final String AVG_AGG_NAME = "avg";

    @Autowired
    private CenterService centerService;

    public AvgTimeAggregator(RestHighLevelClient client) {
        super(client);
    }

    @Override
    public <T> SearchRequest buildRequest(T t) {
        LocalDateTime[] localDateTimes = getUTC(((GolbalStatBO) t).getLogPeriods());
        BoolQueryBuilder builder = QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery(TIME_FILED).gte(localDateTimes[0]).lte(localDateTimes[1]));
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(builder)
                .aggregation(AggregationBuilders.terms(TERMS_AGG_NAME).field(APP_FIELD).size(200).subAggregation(AggregationBuilders.avg(AVG_AGG_NAME).field(TX_DURATION)));
        return new SearchRequest().source(searchSourceBuilder).indices(TXNPLTE_INDICE).types(TYPE);
    }

    @Override
    public Map doStat(SearchResponse response) {
        Map<String, Long> resultMap = new LinkedHashMap<>(16);
        resultMap.put(ALL, 0L);
        Terms aggregation = response.getAggregations().get(TERMS_AGG_NAME);
        List<? extends Terms.Bucket> buckets = aggregation.getBuckets();
        List<CenterBO> centerBOS = centerService.findAll();
        long all = 0;
        long centerHasCount = 0;
        for (CenterBO centerBO : centerBOS) {
            long avgTime = 0;
            if (StringUtils.equals(centerBO.getCenterName(), ALL)) {
                continue;
            }
            if (StringUtils.isBlank(centerBO.getApp())) {
                resultMap.put(centerBO.getCenterName(), 0L);
            } else {
                List<String> apps = Arrays.asList(StringUtils.split(centerBO.getApp(), ";"));
                int appHasCount = 0;
                for (Terms.Bucket bucket : buckets) {
                    if (apps.contains(bucket.getKey())) {
                        Avg avgAgg = bucket.getAggregations().get(AVG_AGG_NAME);
                        avgTime += avgAgg.getValue();
                        appHasCount++;
                    }
                }
                if (appHasCount != 0) {
                    long trueAvgTime = avgTime / appHasCount;
                    resultMap.put(centerBO.getCenterName(), trueAvgTime);
                    all += trueAvgTime;
                    centerHasCount++;
                } else {
                    resultMap.put(centerBO.getCenterName(), 0L);
                }
            }
        }
        if (centerHasCount != 0) {
            resultMap.put(ALL, all / centerHasCount);
        }
        return resultMap;
    }

}
