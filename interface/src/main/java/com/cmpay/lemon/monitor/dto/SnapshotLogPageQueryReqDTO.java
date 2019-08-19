package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.response.PageableRspDTO;

/**
 * Created on 2018/12/19
 *
 * @author: ou_yn
 */
public class SnapshotLogPageQueryReqDTO extends PageableRspDTO {

    private String title;
    /**
     * 开始时间.
     */
    private String startTime;
    /**
     * 结束时间.
     */
    private String endTime;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "SnapshotLogPageQueryReqDTO{" +
                "title='" + title + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
