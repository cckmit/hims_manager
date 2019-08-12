package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.request.GenericDTO;

import javax.validation.constraints.NotNull;
import java.util.Arrays;


/**
 * @author zhou_xiong
 */
public class OrdinarySearchReqDTO extends GenericDTO {
    /**
     * 时间段
     */
    @NotNull
    private String[] logPeriods;
    /**
     * 关键字
     */
    private String keyword;
    /**
     * 页码
     */
    private Integer pageNum;
    /**
     * 每页记录数
     */
    private Integer pageSize;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

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

    @Override
    public String toString() {
        return "OrdinarySearchReqDTO{" +
                "logPeriods=" + Arrays.toString(logPeriods) +
                ", keyword='" + keyword + '\'' +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }
}
