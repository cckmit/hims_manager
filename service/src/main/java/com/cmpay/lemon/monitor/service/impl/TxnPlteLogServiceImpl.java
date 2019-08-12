package com.cmpay.lemon.monitor.service.impl;

import com.cmpay.lemon.common.utils.JudgeUtils;
import com.cmpay.lemon.common.utils.StringUtils;
import com.cmpay.lemon.monitor.bo.GolbalStatBO;
import com.cmpay.lemon.monitor.bo.TopStatBO;
import com.cmpay.lemon.monitor.bo.TxnPlteLogBO;
import com.cmpay.lemon.monitor.query.SearchResult;
import com.cmpay.lemon.monitor.search.Aggregator;
import com.cmpay.lemon.monitor.search.EsSearcher;
import com.cmpay.lemon.monitor.search.aggregation.AvgTimeAggregator;
import com.cmpay.lemon.monitor.search.aggregation.SucRateAggregator;
import com.cmpay.lemon.monitor.search.aggregation.SysErrRateAggregator;
import com.cmpay.lemon.monitor.search.aggregation.TxCountAggregator;
import com.cmpay.lemon.monitor.search.top.TopSucAggregator;
import com.cmpay.lemon.monitor.search.top.TopSysErrAggregator;
import com.cmpay.lemon.monitor.search.top.TopTxAndAvgTimeAggregator;
import com.cmpay.lemon.monitor.search.top.duration.TopDurationCountAggregator;
import com.cmpay.lemon.monitor.service.TxnPlteLogService;
import com.cmpay.lemon.monitor.utils.AmountUtil;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author zhou_xiong
 */
@Service
public class TxnPlteLogServiceImpl implements TxnPlteLogService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TxnPlteLogServiceImpl.class);
    @Autowired
    @Qualifier("txnPlteLogSearch")
    private EsSearcher esSearcher;
    @Autowired
    private List<Aggregator> aggregators;

    @Override
    public SearchResult getDatasByRequestId(TxnPlteLogBO txnPlteLogBO) {
        SearchResponse searchResponse = null;
        SearchRequest searchRequest = esSearcher.buildRequest(txnPlteLogBO);
        long start = System.currentTimeMillis();
        try {
            searchResponse = esSearcher.query(searchRequest);
            LOGGER.debug("txnplte search elapsed: {}ms", System.currentTimeMillis() - start);
        } catch (Exception e) {
            esSearcher.exception(e);
        }
        return esSearcher.responseProcess(searchResponse);
    }

    @Override
    public Map<String, Long> getTxCountMap(GolbalStatBO golbalStatBO) {
        return searchAndStat(getByBeanClass(TxCountAggregator.class), golbalStatBO);
    }

    @Override
    public Map<String, BigDecimal> getSucRateMap(GolbalStatBO golbalStatBO) {
        return searchAndStat(getByBeanClass(SucRateAggregator.class), golbalStatBO);
    }

    @Override
    public Map<String, Long> getAvgTimeMap(GolbalStatBO golbalStatBO) {
        return searchAndStat(getByBeanClass(AvgTimeAggregator.class), golbalStatBO);
    }

    @Override
    public Map<String, BigDecimal> getSysErrRateMap(GolbalStatBO golbalStatBO) {
        return searchAndStat(getByBeanClass(SysErrRateAggregator.class), golbalStatBO);
    }

    @Override
    public List<TopStatBO> getTopStats(TopStatBO topStatBO) {
        // 交易码,交易量,平均时长查询
        List<TopStatBO> topStatBOS = searchAndStat(getByBeanClass(TopTxAndAvgTimeAggregator.class), topStatBO);
        if (JudgeUtils.isEmpty(topStatBOS)) {
            return topStatBOS;
        }
        topStatBOS.get(0).setLogPeriods(topStatBO.getLogPeriods());
        rateCalculate(topStatBOS);
        return topStatBOS;
    }

    @Override
    public List<TopStatBO> getTopDurationStats(TopStatBO topStatBO) {
        List<TopStatBO> topStatBOS = searchAndStat(getByBeanClass(TopDurationCountAggregator.class), topStatBO);
        if (JudgeUtils.isEmpty(topStatBOS)) {
            return topStatBOS;
        }
        topStatBOS.get(0).setLogPeriods(topStatBO.getLogPeriods());
        rateCalculate(topStatBOS);
        return topStatBOS;
    }

    private void rateCalculate(List<TopStatBO> topStatBOS) {
        // 成功率统计
        Map<String, Long> sucCount = searchAndStat(getByBeanClass(TopSucAggregator.class), topStatBOS);
        // 系统错误率统计
        Map<String, Long> sysErrCount = searchAndStat(getByBeanClass(TopSysErrAggregator.class), topStatBOS);
        topStatBOS.forEach(t -> {
            BigDecimal txCount = new BigDecimal(String.valueOf(t.getTxCount().toString()));
            Long suc = JudgeUtils.isNull(sucCount.get(t.getTxCode())) ? 0L : sucCount.get(t.getTxCode());
            Long sysErr = JudgeUtils.isNull(sysErrCount.get(t.getTxCode())) ? 0L : sysErrCount.get(t.getTxCode());
            BigDecimal sucRate = AmountUtil.div(new BigDecimal(String.valueOf(suc * 100)), txCount);
            BigDecimal sysErrRate = AmountUtil.div(new BigDecimal(String.valueOf(sysErr * 100)), txCount);
            t.setSucRate(sucRate);
            t.setSysErrRate(sysErrRate);
        });
    }

    private <T, E> E searchAndStat(Aggregator aggregator, T t) {
        SearchResponse searchResponse = null;
        SearchRequest searchRequest = aggregator.buildRequest(t);
        long start = System.currentTimeMillis();
        try {
            searchResponse = aggregator.query(searchRequest);
            LOGGER.debug("{} elapsed: {}ms", aggregator.getClass().getSimpleName(), System.currentTimeMillis() - start);
        } catch (Exception e) {
            aggregator.exception(e);
        }
        return aggregator.doStat(searchResponse);
    }

    private Aggregator getByBeanClass(Class<? extends Aggregator> clazz) {
        return aggregators.stream().filter(aggregator -> StringUtils.equals(clazz.getSimpleName(), aggregator.getClass().getSimpleName())).findFirst().get();
    }
}
