package com.cmpay.lemon.monitor.bo;

import java.util.List;

public class TimeAxisDataBO {
    String[] xAxisDate;
    List<String> data;
    int position;
    //定稿时间
    String prdFinshTm;
    //uat更新时间
    String uatUpdateTm;
    //测试完成时间时间
    String testFinshTm;
    //预投产时间
    String preTm;
    //投产时间
    String expPrdReleaseTm;
    //当前状态
    String preCurPeriod;
    //查询时间
    String selectTime;

    public String getSelectTime() {
        return selectTime;
    }

    public void setSelectTime(String selectTime) {
        this.selectTime = selectTime;
    }

    public String getPrdFinshTm() {
        return prdFinshTm;
    }

    public String getPreCurPeriod() {
        return preCurPeriod;
    }

    public void setPreCurPeriod(String preCurPeriod) {
        this.preCurPeriod = preCurPeriod;
    }

    public void setPrdFinshTm(String prdFinshTm) {
        this.prdFinshTm = prdFinshTm;
    }

    public String getUatUpdateTm() {
        return uatUpdateTm;
    }

    public void setUatUpdateTm(String uatUpdateTm) {
        this.uatUpdateTm = uatUpdateTm;
    }

    public String getTestFinshTm() {
        return testFinshTm;
    }

    public void setTestFinshTm(String testFinshTm) {
        this.testFinshTm = testFinshTm;
    }

    public String getPreTm() {
        return preTm;
    }

    public void setPreTm(String preTm) {
        this.preTm = preTm;
    }

    public String getExpPrdReleaseTm() {
        return expPrdReleaseTm;
    }

    public void setExpPrdReleaseTm(String expPrdReleaseTm) {
        this.expPrdReleaseTm = expPrdReleaseTm;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public String[] getxAxisDate() {
        return xAxisDate;
    }

    public void setxAxisDate(String[] xAxisDate) {
        this.xAxisDate = xAxisDate;
    }
}
