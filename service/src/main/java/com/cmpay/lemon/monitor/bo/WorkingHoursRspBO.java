package com.cmpay.lemon.monitor.bo;

import com.cmpay.lemon.framework.page.PageInfo;

import java.util.List;

public class WorkingHoursRspBO {
    List<WorkingHoursBO> workingHoursBOS;
    PageInfo<WorkingHoursBO> pageInfo;

    public List<WorkingHoursBO> getWorkingHoursBOS() {
        return workingHoursBOS;
    }

    public void setWorkingHoursBOS(List<WorkingHoursBO> workingHoursBOS) {
        this.workingHoursBOS = workingHoursBOS;
    }

    public PageInfo<WorkingHoursBO> getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo<WorkingHoursBO> pageInfo) {
        this.pageInfo = pageInfo;
    }
}
