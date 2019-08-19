package com.cmpay.lemon.monitor.bo;

/**
 * Created on 2018/12/26
 *
 * @author: ou_yn
 */
public class SnapshotLogQueryBO {

    private String title;
    /**
     * 开始时间.
     */
    private Long startTime;
    /**
     * 结束时间.
     */
    private Long endTime;
    /**
     * 页码
     */
    private Integer pageNum;
    /**
     * 每页记录数
     */
    private Integer pageSize;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

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

    @Override
    public String toString() {
        return "SnapshotLogQueryBO{" +
                "title='" + title + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }
}
