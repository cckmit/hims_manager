/*
 * @ClassName WorkingHoursDO
 * @Description
 * @version 1.0
 * @Date 2020-04-29 16:02:39
 */
package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.request.GenericDTO;

public class DetailsReqDTO extends GenericDTO {
    private String seriesName;
    /**
     * @Fields name 操作人name
     */
    private String name;
    private String selectTime1;
    private String selectTime2;
    /**
     * 页数
     */
    private int pageNum;
    /**
     * 页面大小
     */
    private int pageSize;

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSelectTime1() {
        return selectTime1;
    }

    public void setSelectTime1(String selectTime1) {
        this.selectTime1 = selectTime1;
    }

    public String getSelectTime2() {
        return selectTime2;
    }

    public void setSelectTime2(String selectTime2) {
        this.selectTime2 = selectTime2;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "DetailsReqDTO{" +
                "seriesName='" + seriesName + '\'' +
                ", name='" + name + '\'' +
                ", selectTime1='" + selectTime1 + '\'' +
                ", selectTime2='" + selectTime2 + '\'' +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }
}
