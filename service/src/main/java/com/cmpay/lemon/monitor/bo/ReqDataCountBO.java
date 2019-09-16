package com.cmpay.lemon.monitor.bo;


/**
 * @author: wuliangtui
 * @Description:  需求报表bo
 */
public class ReqDataCountBO  {
    private String reqPrd;
    // 开发阶段：技术方案撰写、技术方案定稿、开发编码、完成开发编码、启动SIT测试、完成SIT测试
    private String reqDevp;
    // 测试阶段：UAT版本更新、UAT测试、完成UAT测试
    private String reqTest;
    // 预投产阶段：预投产测试、完成预投产、待投产
    private String reqPre;
    // 投产上线阶段：已投产
    private String reqOper;
    // 进行中
    private String reqIng;
    // 需求状态：正常
    private String reqUsual;
    // 需求状态：异常
    private String reqUnusual;
    // 产品线
    private String reqPrdLine;
    // 开发主导部门
    private String devpLeadDept;
    //开始月份、结束月份
    private String reportStartMon;
    private String reportEndMon;
    // 合计
    private String total;
    //本月总需求数
    private String reqTotal;
    //已完成需求数：统计需求阶段为“完成产品发布”以及需求当前阶段与本月期望目标一致或超出本月期望目标的数据
    private String reqFinish;
    //暂停需求：统计需求状态为“暂停”
    private String reqSuspend;
    //取消需求：统计需求状态为“取消”
    private String reqCancel;
    //进度异常：统计需求为进度异常
    private String reqAbnormal;
    //完成率：已完成需求数+暂停+取消/需求总数
    private String reqFinishRate;
    //增量需求
    private String reqIncre;
    //存量需求
    private String reqStock;
    //置换需求
    private String reqReplace;
    //插队需求
    private String reqJump;
    //财务部
    private String financeDevp;
    //产品品质部
    private String qualityDevp;
    //创新支付事业部
    private String innoDevp;
    //电商事业部
    private String elecDevp;
    //风险合规部
    private String riskDevp;
    //金融支付事业部
    private String financialDevp;
    //通信支付事业部
    private String commDevp;
    //信息技术部
    private String infoDevp;
    //业务运营部
    private String busiDevp;
    //政企支付事业部
    private String goveDevp;

    private String reqInnerSeq;
    private String reqNo;
    private String reqNm;
    private String preCurPeriod;
    private String expPrdReleaseTm;

    //本月受理需求总数：本月需求总数-暂停-取消
    private String reqAcceptance;
    //需求不准确率：暂停+取消的需求个数/本月需求总数
    private String reqInaccuracyRate;
    //进度异常率：进度异常的需求/本月受理需求总数
    private String reqAbnormalRate;

    public String getReqPrd() {
        return reqPrd;
    }

    public void setReqPrd(String reqPrd) {
        this.reqPrd = reqPrd;
    }

    public String getReqDevp() {
        return reqDevp;
    }

    public void setReqDevp(String reqDevp) {
        this.reqDevp = reqDevp;
    }

    public String getReqTest() {
        return reqTest;
    }

    public void setReqTest(String reqTest) {
        this.reqTest = reqTest;
    }

    public String getReqPre() {
        return reqPre;
    }

    public void setReqPre(String reqPre) {
        this.reqPre = reqPre;
    }

    public String getReqOper() {
        return reqOper;
    }

    public void setReqOper(String reqOper) {
        this.reqOper = reqOper;
    }

    public String getReqIng() {
        return reqIng;
    }

    public void setReqIng(String reqIng) {
        this.reqIng = reqIng;
    }

    public String getReqUsual() {
        return reqUsual;
    }

    public void setReqUsual(String reqUsual) {
        this.reqUsual = reqUsual;
    }

    public String getReqUnusual() {
        return reqUnusual;
    }

    public void setReqUnusual(String reqUnusual) {
        this.reqUnusual = reqUnusual;
    }

    public String getReqPrdLine() {
        return reqPrdLine;
    }

    public void setReqPrdLine(String reqPrdLine) {
        this.reqPrdLine = reqPrdLine;
    }

    public String getDevpLeadDept() {
        return devpLeadDept;
    }

    public void setDevpLeadDept(String devpLeadDept) {
        this.devpLeadDept = devpLeadDept;
    }

    public String getReportStartMon() {
        return reportStartMon;
    }

    public void setReportStartMon(String reportStartMon) {
        this.reportStartMon = reportStartMon;
    }

    public String getReportEndMon() {
        return reportEndMon;
    }

    public void setReportEndMon(String reportEndMon) {
        this.reportEndMon = reportEndMon;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getReqTotal() {
        return reqTotal;
    }

    public void setReqTotal(String reqTotal) {
        this.reqTotal = reqTotal;
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

    public String getReqFinishRate() {
        return reqFinishRate;
    }

    public void setReqFinishRate(String reqFinishRate) {
        this.reqFinishRate = reqFinishRate;
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

    public String getReqInnerSeq() {
        return reqInnerSeq;
    }

    public void setReqInnerSeq(String reqInnerSeq) {
        this.reqInnerSeq = reqInnerSeq;
    }

    public String getReqNo() {
        return reqNo;
    }

    public void setReqNo(String reqNo) {
        this.reqNo = reqNo;
    }

    public String getReqNm() {
        return reqNm;
    }

    public void setReqNm(String reqNm) {
        this.reqNm = reqNm;
    }

    public String getPreCurPeriod() {
        return preCurPeriod;
    }

    public void setPreCurPeriod(String preCurPeriod) {
        this.preCurPeriod = preCurPeriod;
    }

    public String getExpPrdReleaseTm() {
        return expPrdReleaseTm;
    }

    public void setExpPrdReleaseTm(String expPrdReleaseTm) {
        this.expPrdReleaseTm = expPrdReleaseTm;
    }

    public String getReqAcceptance() {
        return reqAcceptance;
    }

    public void setReqAcceptance(String reqAcceptance) {
        this.reqAcceptance = reqAcceptance;
    }

    public String getReqInaccuracyRate() {
        return reqInaccuracyRate;
    }

    public void setReqInaccuracyRate(String reqInaccuracyRate) {
        this.reqInaccuracyRate = reqInaccuracyRate;
    }

    public String getReqAbnormalRate() {
        return reqAbnormalRate;
    }

    public void setReqAbnormalRate(String reqAbnormalRate) {
        this.reqAbnormalRate = reqAbnormalRate;
    }

    @Override
    public String toString() {
        return "ReqDataCountBO{" +
                "reqPrd='" + reqPrd + '\'' +
                ", reqDevp='" + reqDevp + '\'' +
                ", reqTest='" + reqTest + '\'' +
                ", reqPre='" + reqPre + '\'' +
                ", reqOper='" + reqOper + '\'' +
                ", reqIng='" + reqIng + '\'' +
                ", reqUsual='" + reqUsual + '\'' +
                ", reqUnusual='" + reqUnusual + '\'' +
                ", reqPrdLine='" + reqPrdLine + '\'' +
                ", devpLeadDept='" + devpLeadDept + '\'' +
                ", reportStartMon='" + reportStartMon + '\'' +
                ", reportEndMon='" + reportEndMon + '\'' +
                ", total='" + total + '\'' +
                ", reqTotal='" + reqTotal + '\'' +
                ", reqFinish='" + reqFinish + '\'' +
                ", ReqSuspend='" + reqSuspend + '\'' +
                ", reqCancel='" + reqCancel + '\'' +
                ", reqAbnormal='" + reqAbnormal + '\'' +
                ", reqFinishRate='" + reqFinishRate + '\'' +
                ", reqIncre='" + reqIncre + '\'' +
                ", reqStock='" + reqStock + '\'' +
                ", reqReplace='" + reqReplace + '\'' +
                ", reqJump='" + reqJump + '\'' +
                ", financeDevp='" + financeDevp + '\'' +
                ", qualityDevp='" + qualityDevp + '\'' +
                ", innoDevp='" + innoDevp + '\'' +
                ", elecDevp='" + elecDevp + '\'' +
                ", riskDevp='" + riskDevp + '\'' +
                ", financialDevp='" + financialDevp + '\'' +
                ", commDevp='" + commDevp + '\'' +
                ", infoDevp='" + infoDevp + '\'' +
                ", busiDevp='" + busiDevp + '\'' +
                ", goveDevp='" + goveDevp + '\'' +
                ", reqInnerSeq='" + reqInnerSeq + '\'' +
                ", reqNo='" + reqNo + '\'' +
                ", reqNm='" + reqNm + '\'' +
                ", preCurPeriod='" + preCurPeriod + '\'' +
                ", expPrdReleaseTm='" + expPrdReleaseTm + '\'' +
                ", reqAcceptance='" + reqAcceptance + '\'' +
                ", reqInaccuracyRate='" + reqInaccuracyRate + '\'' +
                ", reqAbnormalRate='" + reqAbnormalRate + '\'' +
                '}';
    }
}
