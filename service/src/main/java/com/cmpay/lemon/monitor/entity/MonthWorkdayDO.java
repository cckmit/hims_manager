/*
 * @ClassName MonthWorkdayDO
 * @Description 
 * @version 1.0
 * @Date 2020-09-15 11:39:14
 */
package com.cmpay.lemon.monitor.entity;

import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;

@DataObject
public class MonthWorkdayDO extends BaseDO {
    /**
     * @Fields workId 
     */
    private Long workId;
    /**
     * @Fields workMonth 
     */
    private String workMonth;
    /**
     * @Fields workSumDay 
     */
    private Integer workSumDay;
    /**
     * @Fields workPastDay 
     */
    private Integer workPastDay;

    public Long getWorkId() {
        return workId;
    }

    public void setWorkId(Long workId) {
        this.workId = workId;
    }

    public String getWorkMonth() {
        return workMonth;
    }

    public void setWorkMonth(String workMonth) {
        this.workMonth = workMonth;
    }

    public Integer getWorkSumDay() {
        return workSumDay;
    }

    public void setWorkSumDay(Integer workSumDay) {
        this.workSumDay = workSumDay;
    }

    public Integer getWorkPastDay() {
        return workPastDay;
    }

    public void setWorkPastDay(Integer workPastDay) {
        this.workPastDay = workPastDay;
    }
}