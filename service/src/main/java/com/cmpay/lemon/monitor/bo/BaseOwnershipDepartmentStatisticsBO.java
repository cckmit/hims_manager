package com.cmpay.lemon.monitor.bo;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class BaseOwnershipDepartmentStatisticsBO {

    @Excel(name = "需求阶段")
    private String reqPrd;
    @Excel(name = "财务部")
    private String financeDevp;
    @Excel(name = "产品质量部")
    private String qualityDevp;
    @Excel(name = "创新支付事业部")
    private String innoDevp;
    @Excel(name = "电商事业部")
    private String elecDevp;
    @Excel(name = "风险合规部")
    private String riskDevp;
    @Excel(name = "金融支付事业部")
    private String financialDevp;
    @Excel(name = "通信支付事业部")
    private String commDevp;
    @Excel(name = "信息技术部")
    private String infoDevp;
    @Excel(name = "业务运营部")
    private String busiDevp;
    @Excel(name = "政企支付事业部")
    private String goveDevp;
    @Excel(name = "总计")
    private String total;

    public String getReqPrd() {
        return reqPrd;
    }

    public void setReqPrd(String reqPrd) {
        this.reqPrd = reqPrd;
    }

    public String getFinanceDevp() {
        return financeDevp;
    }

    public void setFinanceDevp(String financeDevp) {
        this.financeDevp = financeDevp;
    }

    public String getQualityDevp() {
        return qualityDevp;
    }

    public void setQualityDevp(String qualityDevp) {
        this.qualityDevp = qualityDevp;
    }

    public String getInnoDevp() {
        return innoDevp;
    }

    public void setInnoDevp(String innoDevp) {
        this.innoDevp = innoDevp;
    }

    public String getElecDevp() {
        return elecDevp;
    }

    public void setElecDevp(String elecDevp) {
        this.elecDevp = elecDevp;
    }

    public String getRiskDevp() {
        return riskDevp;
    }

    public void setRiskDevp(String riskDevp) {
        this.riskDevp = riskDevp;
    }

    public String getFinancialDevp() {
        return financialDevp;
    }

    public void setFinancialDevp(String financialDevp) {
        this.financialDevp = financialDevp;
    }

    public String getCommDevp() {
        return commDevp;
    }

    public void setCommDevp(String commDevp) {
        this.commDevp = commDevp;
    }

    public String getInfoDevp() {
        return infoDevp;
    }

    public void setInfoDevp(String infoDevp) {
        this.infoDevp = infoDevp;
    }

    public String getBusiDevp() {
        return busiDevp;
    }

    public void setBusiDevp(String busiDevp) {
        this.busiDevp = busiDevp;
    }

    public String getGoveDevp() {
        return goveDevp;
    }

    public void setGoveDevp(String goveDevp) {
        this.goveDevp = goveDevp;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
