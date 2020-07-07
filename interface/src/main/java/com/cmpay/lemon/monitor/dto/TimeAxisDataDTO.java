package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.request.GenericDTO;

import java.util.List;


public class TimeAxisDataDTO extends GenericDTO {

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

    public String[] getxAxisDate() {
        return xAxisDate;
    }

    public void setxAxisDate(String[] xAxisDate) {
        this.xAxisDate = xAxisDate;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getPrdFinshTm() {
        return prdFinshTm;
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

    public String getPreCurPeriod() {
        return preCurPeriod;
    }

    public void setPreCurPeriod(String preCurPeriod) {
        this.preCurPeriod = preCurPeriod;
    }
}
