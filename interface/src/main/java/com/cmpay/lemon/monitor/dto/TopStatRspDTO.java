package com.cmpay.lemon.monitor.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: zhou_xiong
 */
public class TopStatRspDTO {

    private List<TopStat> topStats;

    public List<TopStat> getTopStats() {
        return topStats;
    }

    public void setTopStats(List<TopStat> topStats) {
        this.topStats = topStats;
    }

    @Override
    public String toString() {
        return "TopStatRspDTO{" +
                "topStats=" + topStats +
                '}';
    }

    public static class TopStat {
        /**
         * 交易码
         */
        private String txCode;
        /**
         * 交易量
         */
        private Long txCount;
        /**
         * 平均时长
         */
        private Long avgDuration;
        /**
         * 成功率
         */
        private BigDecimal sucRate;
        /**
         * 系统错误率
         */
        private BigDecimal sysErrRate;

        public String getTxCode() {
            return txCode;
        }

        public void setTxCode(String txCode) {
            this.txCode = txCode;
        }

        public Long getTxCount() {
            return txCount;
        }

        public void setTxCount(Long txCount) {
            this.txCount = txCount;
        }

        public Long getAvgDuration() {
            return avgDuration;
        }

        public void setAvgDuration(Long avgDuration) {
            this.avgDuration = avgDuration;
        }

        public BigDecimal getSucRate() {
            return sucRate;
        }

        public void setSucRate(BigDecimal sucRate) {
            this.sucRate = sucRate;
        }

        public BigDecimal getSysErrRate() {
            return sysErrRate;
        }

        public void setSysErrRate(BigDecimal sysErrRate) {
            this.sysErrRate = sysErrRate;
        }

        @Override
        public String toString() {
            return "TopStat{" +
                    "txCode='" + txCode + '\'' +
                    ", txCount='" + txCount + '\'' +
                    ", avgDuration=" + avgDuration +
                    ", sucRate=" + sucRate +
                    ", sysErrRate=" + sysErrRate +
                    '}';
        }
    }
}
