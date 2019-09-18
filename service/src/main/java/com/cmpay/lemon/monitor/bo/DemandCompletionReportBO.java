package com.cmpay.lemon.monitor.bo;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class DemandCompletionReportBO {

    @Excel(name = "开发主导部门")
    private String devpLeadDept;
    @Excel(name = "本月总需求")
    private String reqTotal;
    @Excel(name = "本月需求受理总数")
    private String reqAcceptance;
    @Excel(name = "已投产需求")
    private String reqOper;
    @Excel(name = "已完成目标需求")
    private String reqFinish;
    @Excel(name = "暂停需求")
    private String reqSuspend;
    @Excel(name = "取消需求")
    private String reqCancel;
    @Excel(name = "进度异常")
    private String reqAbnormal;
    @Excel(name = "进度异常率")
    private String reqAbnormalRate;
    @Excel(name = "投产率")
    private String reqFinishRate;
     @Excel(name = "完成率")
    private String total;
    @Excel(name = "需求不准确率")
    private String reqInaccuracyRate;


    public String getDevpLeadDept() {
        return devpLeadDept;
    }

    public void setDevpLeadDept(String devpLeadDept) {
        this.devpLeadDept = devpLeadDept;
    }

    public String getReqTotal() {
        return reqTotal;
    }

    public void setReqTotal(String reqTotal) {
        this.reqTotal = reqTotal;
    }

    public String getReqAcceptance() {
        return reqAcceptance;
    }

    public void setReqAcceptance(String reqAcceptance) {
        this.reqAcceptance = reqAcceptance;
    }

    public String getReqOper() {
        return reqOper;
    }

    public void setReqOper(String reqOper) {
        this.reqOper = reqOper;
    }

    public String getReqFinish() {
        return reqFinish;
    }

    public void setReqFinish(String reqFinish) {
        this.reqFinish = reqFinish;
    }

    public String getReqSuspend() {
        return reqSuspend;
    }

    public void setReqSuspend(String reqSuspend) {
        this.reqSuspend = reqSuspend;
    }

    public String getReqCancel() {
        return reqCancel;
    }

    public void setReqCancel(String reqCancel) {
        this.reqCancel = reqCancel;
    }

    public String getReqAbnormal() {
        return reqAbnormal;
    }

    public void setReqAbnormal(String reqAbnormal) {
        this.reqAbnormal = reqAbnormal;
    }

    public String getReqAbnormalRate() {
        return reqAbnormalRate;
    }

    public void setReqAbnormalRate(String reqAbnormalRate) {
        this.reqAbnormalRate = reqAbnormalRate;
    }

    public String getReqFinishRate() {
        return reqFinishRate;
    }

    public void setReqFinishRate(String reqFinishRate) {
        this.reqFinishRate = reqFinishRate;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getReqInaccuracyRate() {
        return reqInaccuracyRate;
    }

    public void setReqInaccuracyRate(String reqInaccuracyRate) {
        this.reqInaccuracyRate = reqInaccuracyRate;
    }
}
