/*
 * @ClassName WorkloadLockedStateDO
 * @Description 
 * @version 1.0
 * @Date 2020-01-06 17:53:54
 */
package com.cmpay.lemon.monitor.bo;

import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;

@DataObject
public class WorkloadLockedStateBO extends BaseDO {
    /**
     * @Fields month 月份
     */
    private String entrymonth;
    /**
     * @Fields status 状态
     */
    private String status;

    public String getEntrymonth() {
        return entrymonth;
    }

    public void setEntrymonth(String entrymonth) {
        this.entrymonth = entrymonth;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}