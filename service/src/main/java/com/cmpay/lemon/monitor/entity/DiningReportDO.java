/*
 * @ClassName DiningReportDO
 * @Description 
 * @version 1.0
 * @Date 2020-08-25 16:31:56
 */
package com.cmpay.lemon.monitor.entity;

import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;

@DataObject
public class DiningReportDO extends BaseDO {
    /**
     * @Fields reportId 
     */
    private String reportId;
    /**
     * @Fields reportName 
     */
    private String reportName;
    /**
     * @Fields reportSrc 
     */
    private String reportSrc;
    /**
     * @Fields reportType 
     */
    private String reportType;
    /**
     * @Fields reportStyle 
     */
    private String reportStyle;
    /**
     * @Fields reportPeriod 
     */
    private String reportPeriod;
    /**
     * @Fields reportDay 
     */
    private String reportDay;
    /**
     * @Fields reportSts 
     */
    private String reportSts;
    /**
     * @Fields reportGroup 
     */
    private String reportGroup;
    /**
     * @Fields reportLink 
     */
    private String reportLink;
    /**
     * @Fields raqPath 
     */
    private String raqPath;
    /**
     * @Fields reportFile 
     */
    private String reportFile;
    /**
     * @Fields raqArgs 
     */
    private String raqArgs;
    /**
     * @Fields startTm 
     */
    private String startTm;
    /**
     * @Fields endTm 
     */
    private String endTm;
    /**
     * @Fields runType 
     */
    private String runType;
    /**
     * @Fields runDt 
     */
    private String runDt;
    /**
     * @Fields upTm 
     */
    private String upTm;
    /**
     * @Fields upUser 
     */
    private String upUser;

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportSrc() {
        return reportSrc;
    }

    public void setReportSrc(String reportSrc) {
        this.reportSrc = reportSrc;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getReportStyle() {
        return reportStyle;
    }

    public void setReportStyle(String reportStyle) {
        this.reportStyle = reportStyle;
    }

    public String getReportPeriod() {
        return reportPeriod;
    }

    public void setReportPeriod(String reportPeriod) {
        this.reportPeriod = reportPeriod;
    }

    public String getReportDay() {
        return reportDay;
    }

    public void setReportDay(String reportDay) {
        this.reportDay = reportDay;
    }

    public String getReportSts() {
        return reportSts;
    }

    public void setReportSts(String reportSts) {
        this.reportSts = reportSts;
    }

    public String getReportGroup() {
        return reportGroup;
    }

    public void setReportGroup(String reportGroup) {
        this.reportGroup = reportGroup;
    }

    public String getReportLink() {
        return reportLink;
    }

    public void setReportLink(String reportLink) {
        this.reportLink = reportLink;
    }

    public String getRaqPath() {
        return raqPath;
    }

    public void setRaqPath(String raqPath) {
        this.raqPath = raqPath;
    }

    public String getReportFile() {
        return reportFile;
    }

    public void setReportFile(String reportFile) {
        this.reportFile = reportFile;
    }

    public String getRaqArgs() {
        return raqArgs;
    }

    public void setRaqArgs(String raqArgs) {
        this.raqArgs = raqArgs;
    }

    public String getStartTm() {
        return startTm;
    }

    public void setStartTm(String startTm) {
        this.startTm = startTm;
    }

    public String getEndTm() {
        return endTm;
    }

    public void setEndTm(String endTm) {
        this.endTm = endTm;
    }

    public String getRunType() {
        return runType;
    }

    public void setRunType(String runType) {
        this.runType = runType;
    }

    public String getRunDt() {
        return runDt;
    }

    public void setRunDt(String runDt) {
        this.runDt = runDt;
    }

    public String getUpTm() {
        return upTm;
    }

    public void setUpTm(String upTm) {
        this.upTm = upTm;
    }

    public String getUpUser() {
        return upUser;
    }

    public void setUpUser(String upUser) {
        this.upUser = upUser;
    }
}