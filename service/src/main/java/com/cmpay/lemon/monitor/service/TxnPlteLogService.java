package com.cmpay.lemon.monitor.service;


import com.cmpay.lemon.monitor.bo.GolbalStatBO;
import com.cmpay.lemon.monitor.bo.TopStatBO;
import com.cmpay.lemon.monitor.bo.TxnPlteLogBO;
import com.cmpay.lemon.monitor.query.SearchResult;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author: zhou_xiong
 */
public interface TxnPlteLogService {
    /**
     * 通过requestId查询txnplte日志
     *
     * @param txnPlteLogBO
     * @return
     */
    SearchResult getDatasByRequestId(TxnPlteLogBO txnPlteLogBO);

    /**
     * 获取交易量数据
     *
     * @param golbalStatBO
     * @return
     */
    Map<String, Long> getTxCountMap(GolbalStatBO golbalStatBO);

    /**
     * 获取成功率数据
     *
     * @param golbalStatBO
     * @return
     */
    Map<String, BigDecimal> getSucRateMap(GolbalStatBO golbalStatBO);

    /**
     * 获取平均时长数据
     *
     * @param golbalStatBO
     * @return
     */
    Map<String, Long> getAvgTimeMap(GolbalStatBO golbalStatBO);

    /**
     * 获取系统错误率数据
     *
     * @param golbalStatBO
     * @return
     */
    Map<String, BigDecimal> getSysErrRateMap(GolbalStatBO golbalStatBO);

    /**
     * top10交易量统计
     *
     * @param topStatBO
     * @return
     */
    List<TopStatBO> getTopStats(TopStatBO topStatBO);

    /**
     * top10耗时统计
     *
     * @param topStatBO
     * @return
     */
    List<TopStatBO> getTopDurationStats(TopStatBO topStatBO);
}
