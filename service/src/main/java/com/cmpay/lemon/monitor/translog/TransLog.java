package com.cmpay.lemon.monitor.translog;


import com.cmpay.lemon.monitor.query.SearchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TransLog {

    private String sn;
    private String csn;
    private String psn;
    private String reqId;
    private String tc;
    private String mc;
    private String reg;
    private String nod;
    private String svr;
    private long dur;
    private String st;
    private String et;
    private String ip;
    private String jrn;
    private String sysCnl;
    private String appData;
    private int isLost = 1;
    //root节点用
    private int lostCnt = 0;
    private SearchResult searchResult;

    public int getIsLost() {
        return isLost;
    }

    public void setIsLost(int isLost) {
        this.isLost = isLost;
    }

    public int getLostCnt() {
        return lostCnt;
    }

    public String getCsn() {
        return csn;
    }

    public void setCsn(String csn) {
        this.csn = csn;
    }


    public void setLostCnt(int lostCnt) {
        this.lostCnt = lostCnt;
    }

    private List<TransLog> children = new ArrayList<>();

    public TransLog() {
    }

    public TransLog(String sn, String psn, int isLost) {
        this.sn = sn;
        this.psn = psn;
        this.isLost = isLost;
        this.reqId = "";
        this.reg = "";
        this.nod = "";
        this.tc = "";
        this.mc = "";
    }

    public TransLog(String sn, String psn, String csn, int isLost) {
        this.sn = sn;
        this.psn = psn;
        this.isLost = isLost;
        this.csn = csn;
        this.reqId = "";
        this.reg = "";
        this.nod = "";
        this.tc = "";
        this.mc = "";
    }

    public TransLog(String sn, String psn, String reqId, String reg, String nod, String tc, String mc) {
        this.sn = sn;
        this.psn = psn;
        this.reqId = reqId;
        this.reg = reg;
        this.nod = nod;
        this.tc = tc;
        this.mc = mc;
    }

    public TransLog(Map<String, Object> data) {

        this.sn = (String) data.getOrDefault("sn", "-1");
        this.psn = (String) data.getOrDefault("psn", "0");
        this.csn = (String) data.getOrDefault("csn", "0");
        this.reqId = (String) data.getOrDefault("lsn", "");
        this.reg = (String) data.getOrDefault("reg", "");
        this.nod = (String) data.getOrDefault("nod", "");
        this.tc = (String) data.getOrDefault("tc", "");
        this.mc = (String) data.getOrDefault("mc", "");
        this.svr = (String) data.getOrDefault("svr", "");
        this.dur = Long.parseLong(data.getOrDefault("dur", "-1").toString());
        this.ip = (String) data.getOrDefault("ip", "");
        this.jrn = (String) data.getOrDefault("jrn", "");
        this.sysCnl = (String) data.getOrDefault("sysCnl", "");
        this.appData = (String) data.getOrDefault("appData", "");
        this.st = (String) data.getOrDefault("st", "");
        this.et = (String) data.getOrDefault("et", "");
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getPsn() {
        return psn;
    }

    public void setPsn(String psn) {
        this.psn = psn;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public String getTc() {
        return tc;
    }

    public void setTc(String tc) {
        this.tc = tc;
    }

    public String getMc() {
        return mc;
    }

    public void setMc(String mc) {
        this.mc = mc;
    }

    public String getReg() {
        return reg;
    }

    public void setReg(String reg) {
        this.reg = reg;
    }

    public String getNod() {
        return nod;
    }

    public void setNod(String nod) {
        this.nod = nod;
    }

    public String getSvr() {
        return svr;
    }

    public void setSvr(String svr) {
        this.svr = svr;
    }

    public long getDur() {
        return dur;
    }

    public void setDur(long dur) {
        this.dur = dur;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public String getEt() {
        return et;
    }

    public void setEt(String et) {
        this.et = et;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getJrn() {
        return jrn;
    }

    public void setJrn(String jrn) {
        this.jrn = jrn;
    }

    public String getSysCnl() {
        return sysCnl;
    }

    public void setSysCnl(String sysCnl) {
        this.sysCnl = sysCnl;
    }

    public List<TransLog> getChildren() {
        return children;
    }

    public String getAppData() {
        return appData;
    }

    public void setAppData(String appData) {
        this.appData = appData;
    }

    public void setChildren(List<TransLog> children) {
        this.children = children;
    }

    public SearchResult getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(SearchResult searchResult) {
        this.searchResult = searchResult;
    }

    @Override
    public String toString() {
        return "reqId:" + reqId + ",psn:" + psn + ",sn:" + sn + ",csn:" + csn + ",reg:" + reg + ",nod:"
                + nod + ",tc:" + tc + ",mc:" + mc + ",isLost:" + isLost + ",lostCnt:" + lostCnt + ",children:" + children;
    }

}
