package com.cmpay.lemon.monitor.bo;


import com.cmpay.lemon.monitor.query.SearchResult;

import java.util.Arrays;

/**
 * @author zhou_xiong
 */
public class OrdinarySearchBO extends PageQueryBO {
    /**
     * 时间段
     */
    private String[] logPeriods;
    /**
     * 关键字
     */
    private String keyword;
    /**
     * 搜索结果
     */
    private SearchResult searchResult;

    public String[] getLogPeriods() {
        return logPeriods;
    }

    public void setLogPeriods(String[] logPeriods) {
        this.logPeriods = logPeriods;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public SearchResult getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(SearchResult searchResult) {
        this.searchResult = searchResult;
    }

    @Override
    public String toString() {
        return "OrdinarySearchBO{" +
                "logPeriods=" + Arrays.toString(logPeriods) +
                ", keyword='" + keyword + '\'' +
                ", searchResult=" + searchResult +
                '}';
    }
}
