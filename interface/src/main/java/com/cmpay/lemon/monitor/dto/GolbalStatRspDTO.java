package com.cmpay.lemon.monitor.dto;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author: zhou_xiong
 */
public class GolbalStatRspDTO {
    /**
     * 交易量
     */
    private Map<String, Long> txCountMap ;
    /**
     * 成功率
     */
    private Map<String, BigDecimal> sucRateMap ;
    /**
     * 平均时长
     */
    private Map<String, Long> avgTimeMap ;
    /**
     * 系统错误
     */
    private Map<String, BigDecimal> sysErrRateMap ;

    public GolbalStatRspDTO(Map<String, Long> txCountMap, Map<String, BigDecimal> sucRateMap, Map<String, Long> avgTimeMap, Map<String, BigDecimal> sysErrRateMap) {
        this.txCountMap = txCountMap;
        this.sucRateMap = sucRateMap;
        this.avgTimeMap = avgTimeMap;
        this.sysErrRateMap = sysErrRateMap;
    }

    public Map<String, Long> getTxCountMap() {
        return txCountMap;
    }

    public void setTxCountMap(Map<String, Long> txCountMap) {
        this.txCountMap = txCountMap;
    }

    public Map<String, BigDecimal> getSucRateMap() {
        return sucRateMap;
    }

    public void setSucRateMap(Map<String, BigDecimal> sucRateMap) {
        this.sucRateMap = sucRateMap;
    }

    public Map<String, Long> getAvgTimeMap() {
        return avgTimeMap;
    }

    public void setAvgTimeMap(Map<String, Long> avgTimeMap) {
        this.avgTimeMap = avgTimeMap;
    }

    public Map<String, BigDecimal> getSysErrRateMap() {
        return sysErrRateMap;
    }

    public void setSysErrRateMap(Map<String, BigDecimal> sysErrRateMap) {
        this.sysErrRateMap = sysErrRateMap;
    }

    @Override
    public String toString() {
        return "GolbalStatRspDTO{" +
                "txCountMap=" + txCountMap +
                ", sucRateMap=" + sucRateMap +
                ", avgTimeMap=" + avgTimeMap +
                ", sysErrRateMap=" + sysErrRateMap +
                '}';
    }
}
