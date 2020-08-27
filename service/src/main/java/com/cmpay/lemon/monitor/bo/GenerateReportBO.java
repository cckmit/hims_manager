package com.cmpay.lemon.monitor.bo;


import java.util.Arrays;

/**
 * Created on 17:17 2019/6/12
 *
 * @author hu_hx
 * <p>
 * description
 */
public class GenerateReportBO {
    /**
     * 报表IP
     */
    private String reportId;
    /**
     * 报表参数
     */
    private String[] raqArgs;
    /**
     * 报表模板文件路径
     */
    private String raqPath;
    /**
     * 生成文件存放路线
     */
    private String fileSavePath;
    /**
     * 报表格式xlsx ,pdf
     */
    private String reportStyle;
    /**
     * 报表参数
     */
    private String [] paramArray;
    /**
     * 授权文件路径
     */
    private String licUrl;
    /**
     * 配置数据源
     */
    private String reportLink;
    /**
     * 数据源名称
     */
    private String dataSourceName;
    /**
     * 数据驱动
     */
    private String dataBaseDriver;
    /**
     * 数据库url
     */
    private String dataBaseUrl;
    /**
     * 数据库用户名
     */
    private String dataBaseUserName;
    /**
     * 数据库密码
     */
    private String userPassword;

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String[] getRaqArgs() {
        return raqArgs;
    }

    public void setRaqArgs(String[] raqArgs) {
        this.raqArgs = raqArgs;
    }

    public String getRaqPath() {
        return raqPath;
    }

    public void setRaqPath(String raqPath) {
        this.raqPath = raqPath;
    }

    public String getFileSavePath() {
        return fileSavePath;
    }

    public void setFileSavePath(String fileSavePath) {
        this.fileSavePath = fileSavePath;
    }

    public String getReportStyle() {
        return reportStyle;
    }

    public void setReportStyle(String reportStyle) {
        this.reportStyle = reportStyle;
    }

    public String[] getParamArray() {
        return paramArray;
    }

    public void setParamArray(String[] paramArray) {
        this.paramArray = paramArray;
    }

    public String getLicUrl() {
        return licUrl;
    }

    public void setLicUrl(String licUrl) {
        this.licUrl = licUrl;
    }

    public String getReportLink() {
        return reportLink;
    }

    public void setReportLink(String reportLink) {
        this.reportLink = reportLink;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public String getDataBaseDriver() {
        return dataBaseDriver;
    }

    public void setDataBaseDriver(String dataBaseDriver) {
        this.dataBaseDriver = dataBaseDriver;
    }

    public String getDataBaseUrl() {
        return dataBaseUrl;
    }

    public void setDataBaseUrl(String dataBaseUrl) {
        this.dataBaseUrl = dataBaseUrl;
    }

    public String getDataBaseUserName() {
        return dataBaseUserName;
    }

    public void setDataBaseUserName(String dataBaseUserName) {
        this.dataBaseUserName = dataBaseUserName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Override
    public String toString() {
        return "GenerateReportBO{" +
                "reportId='" + reportId + '\'' +
                ", raqArgs=" + Arrays.toString(raqArgs) +
                ", raqPath='" + raqPath + '\'' +
                ", fileSavePath='" + fileSavePath + '\'' +
                ", reportStyle='" + reportStyle + '\'' +
                ", paramArray=" + Arrays.toString(paramArray) +
                ", licUrl='" + licUrl + '\'' +
                ", reportLink='" + reportLink + '\'' +
                ", dataSourceName='" + dataSourceName + '\'' +
                ", dataBaseDriver='" + dataBaseDriver + '\'' +
                ", dataBaseUrl='" + dataBaseUrl + '\'' +
                ", dataBaseUserName='" + dataBaseUserName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                '}';
    }
}
