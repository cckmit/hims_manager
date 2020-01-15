package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.response.PageableRspDTO;

import java.sql.Date;

/**
 * @author: zhou_xiong
 */
public class ITProductionReqDTO extends PageableRspDTO {
    // 更新开始时间
    private String startTime;
    // // 更新结束时间
    private String endTime;
    // 投产主要支持人员
    private String supportStaff;
    // Weblogic重启情况
    private String restartTheSituation;
    // 服务台拨测人员
    private String serviceDeskTester;
    // 拨测结果
    private String testResult;
    // 告警监控人员
    private String alarmMonitor;
    // 告警监控情况
    private String alarmMonitoring;
    // 本次投产总结
    private String productionOfTheSummary;
    // 选择的投产编号
    private String taskIdStr;
    /**
     * 页数
     */
    private int pageNum;
    /**
     * 页面大小
     */
    private int pageSize;

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

    public String getSupportStaff() {
        return supportStaff;
    }

    public void setSupportStaff(String supportStaff) {
        this.supportStaff = supportStaff;
    }

    public String getRestartTheSituation() {
        return restartTheSituation;
    }

    public void setRestartTheSituation(String restartTheSituation) {
        this.restartTheSituation = restartTheSituation;
    }

    public String getServiceDeskTester() {
        return serviceDeskTester;
    }

    public void setServiceDeskTester(String serviceDeskTester) {
        this.serviceDeskTester = serviceDeskTester;
    }

    public String getTestResult() {
        return testResult;
    }

    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }

    public String getAlarmMonitor() {
        return alarmMonitor;
    }

    public void setAlarmMonitor(String alarmMonitor) {
        this.alarmMonitor = alarmMonitor;
    }

    public String getAlarmMonitoring() {
        return alarmMonitoring;
    }

    public void setAlarmMonitoring(String alarmMonitoring) {
        this.alarmMonitoring = alarmMonitoring;
    }

    public String getProductionOfTheSummary() {
        return productionOfTheSummary;
    }

    public void setProductionOfTheSummary(String productionOfTheSummary) {
        this.productionOfTheSummary = productionOfTheSummary;
    }

    public String getTaskIdStr() {
        return taskIdStr;
    }

    public void setTaskIdStr(String taskIdStr) {
        this.taskIdStr = taskIdStr;
    }

    @Override
    public int getPageNum() {
        return pageNum;
    }

    @Override
    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "ITProductionReqDTO{" +
                "startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", supportStaff='" + supportStaff + '\'' +
                ", restartTheSituation='" + restartTheSituation + '\'' +
                ", serviceDeskTester='" + serviceDeskTester + '\'' +
                ", testResult='" + testResult + '\'' +
                ", alarmMonitor='" + alarmMonitor + '\'' +
                ", alarmMonitoring='" + alarmMonitoring + '\'' +
                ", productionOfTheSummary='" + productionOfTheSummary + '\'' +
                ", taskIdStr='" + taskIdStr + '\'' +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }
}
