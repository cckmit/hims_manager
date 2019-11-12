package com.cmpay.lemon.monitor.bo;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.sql.Date;

public class DemandTypeProductionBO {

    @Excel(name = "投产编号")
    private String proNumber;
    @Excel(name = "需求名称")
    private String proNeed;
    @Excel(name = "主导部门")
    private String applicationDept;
    @Excel(name = "录入人")
    private String proOperator;
    @Excel(name = "操作时间")
    private Date scheduleTime;

    public String getProNumber() {
        return proNumber;
    }

    public void setProNumber(String proNumber) {
        this.proNumber = proNumber;
    }

    public String getProNeed() {
        return proNeed;
    }

    public void setProNeed(String proNeed) {
        this.proNeed = proNeed;
    }

    public String getApplicationDept() {
        return applicationDept;
    }

    public void setApplicationDept(String applicationDept) {
        this.applicationDept = applicationDept;
    }

    public String getProOperator() {
        return proOperator;
    }

    public void setProOperator(String proOperator) {
        this.proOperator = proOperator;
    }

    public Date getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(Date scheduleTime) {
        this.scheduleTime = scheduleTime;
    }
}
