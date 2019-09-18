package com.cmpay.lemon.monitor.bo;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class DemandTypeStatisticsBO {

    @Excel(name = "本月总需求")
    private String reqTotal;
    @Excel(name = "增量需求")
    private String reqIncre;
    @Excel(name = "存量需求")
    private String reqStock;
    @Excel(name = "置换需求")
    private String reqReplace;
    @Excel(name = "插队需求")
    private String reqJump;

    public String getReqTotal() {
        return reqTotal;
    }

    public void setReqTotal(String reqTotal) {
        this.reqTotal = reqTotal;
    }

    public String getReqIncre() {
        return reqIncre;
    }

    public void setReqIncre(String reqIncre) {
        this.reqIncre = reqIncre;
    }

    public String getReqStock() {
        return reqStock;
    }

    public void setReqStock(String reqStock) {
        this.reqStock = reqStock;
    }

    public String getReqReplace() {
        return reqReplace;
    }

    public void setReqReplace(String reqReplace) {
        this.reqReplace = reqReplace;
    }

    public String getReqJump() {
        return reqJump;
    }

    public void setReqJump(String reqJump) {
        this.reqJump = reqJump;
    }
}
