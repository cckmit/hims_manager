package com.cmpay.lemon.monitor.bo;

import com.cmpay.lemon.framework.page.PageInfo;

import java.util.List;

public class ScheduleRspBO {
    List<ScheduleBO> ScheduleList;
    PageInfo<ScheduleBO> pageInfo;

    public List<ScheduleBO> getScheduleList() {
        return ScheduleList;
    }

    public void setScheduleList(List<ScheduleBO> scheduleList) {
        ScheduleList = scheduleList;
    }

    public PageInfo<ScheduleBO> getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo<ScheduleBO> pageInfo) {
        this.pageInfo = pageInfo;
    }
}
