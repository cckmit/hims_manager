package com.cmpay.lemon.monitor.bo;

import cn.afterturn.easypoi.excel.annotation.Excel;
/**
 * @author: TY
 */
public class DemandImplementationReportBO {
    @Excel(name = "一级主导团队")
    private String firstLevelOrganization;
    @Excel(name = "二级主导团队")
    private String devpLeadDept;
    @Excel(name = "需求阶段")
    private String reqPrd;
    @Excel(name = "开发阶段")
    private String reqDevp;
    @Excel(name = "测试阶段")
    private String reqTest;
    @Excel(name = "预投产阶段")
    private String reqPre;
    @Excel(name = "完成发布")
    private String reqOper;
    @Excel(name = "合计")
    private String total;

    public String getDevpLeadDept() {
        return devpLeadDept;
    }

    public void setDevpLeadDept(String devpLeadDept) {
        this.devpLeadDept = devpLeadDept;
    }

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

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getFirstLevelOrganization() {
        return firstLevelOrganization;
    }

    public void setFirstLevelOrganization(String firstLevelOrganization) {
        this.firstLevelOrganization = firstLevelOrganization;
    }

    @Override
    public String toString() {
        return "DemandImplementationReportBO{" +
                "firstLevelOrganization='" + firstLevelOrganization + '\'' +
                ", devpLeadDept='" + devpLeadDept + '\'' +
                ", reqPrd='" + reqPrd + '\'' +
                ", reqDevp='" + reqDevp + '\'' +
                ", reqTest='" + reqTest + '\'' +
                ", reqPre='" + reqPre + '\'' +
                ", reqOper='" + reqOper + '\'' +
                ", total='" + total + '\'' +
                '}';
    }
}
