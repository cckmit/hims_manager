package com.cmpay.lemon.monitor.dto;


import com.cmpay.framework.data.response.GenericRspDTO;

import java.util.List;
import java.util.Map;

/**
 * @author zhou_xiong
 */
public class PageQueryResultDTO extends GenericRspDTO {
    /**
     * 记录行数
     */
    private long count;
    /**
     * 结果集
     */
    private List<Map<String, Object>> resultSet;

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<Map<String, Object>> getResultSet() {
        return resultSet;
    }

    public void setResultSet(List<Map<String, Object>> resultSet) {
        this.resultSet = resultSet;
    }

    @Override
    public String toString() {
        return "PageQueryResultDTO{" +
                "count=" + count +
                ", resultSet=" + resultSet +
                '}';
    }
}
