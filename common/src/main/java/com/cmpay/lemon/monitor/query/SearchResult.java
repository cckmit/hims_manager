package com.cmpay.lemon.monitor.query;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 2019/2/15
 *
 * @author: zhou_xiong
 */
public class SearchResult {
    /**
     * 记录行数
     */
    private long count;
    /**
     * 结果集
     */
    private List<Map<String, Object>> resultSet;

    /**
     * 构造方法
     *
     * @param count
     * @param resultSet
     */
    public SearchResult(long count, List<Map<String, Object>> resultSet) {
        this.count = count;
        this.resultSet = resultSet;
    }

    public SearchResult() {
    }

    /**
     * 对象转 Map
     *
     * @return { records: 查询结果集, count: countSQL 执行结果}
     */
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<String, Object>(16);
        map.put("count", count);
        map.put("records", resultSet);
        return map;
    }

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

    /**
     * 对象转字符串
     *
     * @return 转换后的字符串
     */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
