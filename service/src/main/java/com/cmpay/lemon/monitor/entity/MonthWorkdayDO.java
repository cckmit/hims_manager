/*
 * @ClassName MonthWorkdayDO
 * @Description 
 * @version 1.0
 * @Date 2020-07-10 16:02:26
 */
package com.cmpay.lemon.monitor.entity;

import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;

@DataObject
public class MonthWorkdayDO extends BaseDO {
    /**
     * @Fields month 月份
     */
    private String month;
    /**
     * @Fields workday 工作日
     */
    private String workday;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getWorkday() {
        return workday;
    }

    public void setWorkday(String workday) {
        this.workday = workday;
    }
}