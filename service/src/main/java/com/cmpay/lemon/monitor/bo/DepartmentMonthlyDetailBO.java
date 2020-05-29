package com.cmpay.lemon.monitor.bo;

import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * 各部门工作量月统计明细报表BO
 */
public class DepartmentMonthlyDetailBO {

    @Excel(name = "牵头部门")
    private String devpLeadDept;
    @Excel(name = "需求编号")
    private String reqNo;
    @Excel(name = "需求名称")
    private String reqNm;
    @Excel(name = "总工作量")
    private int totalWorkload;
    @Excel(name = "二级主导团队完成情况")
    private String leadDeptWorkload;
    @Excel(name = "二级配合团队完成情况")
    private String coorDeptWorkload;
    @Excel(name = "本月录入工作量")
    private int monInputWorkload;
    @Excel(name = "二级主导团队本月工作量")
    private String leadDeptWorkload1;
    @Excel(name = "二级配合团队本月工作量")
    private String coorDeptWorkload1;

    public String getDevpLeadDept() {
        return devpLeadDept;
    }

    public void setDevpLeadDept(String devpLeadDept) {
        this.devpLeadDept = devpLeadDept;
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

    public int getTotalWorkload() {
        return totalWorkload;
    }

    public void setTotalWorkload(int totalWorkload) {
        this.totalWorkload = totalWorkload;
    }

    public String getLeadDeptWorkload() {
        return leadDeptWorkload;
    }

    public void setLeadDeptWorkload(String leadDeptWorkload) {
        this.leadDeptWorkload = leadDeptWorkload;
    }

    public String getCoorDeptWorkload() {
        return coorDeptWorkload;
    }

    public void setCoorDeptWorkload(String coorDeptWorkload) {
        this.coorDeptWorkload = coorDeptWorkload;
    }

    public int getMonInputWorkload() {
        return monInputWorkload;
    }

    public void setMonInputWorkload(int monInputWorkload) {
        this.monInputWorkload = monInputWorkload;
    }

    public String getLeadDeptWorkload1() {
        return leadDeptWorkload1;
    }

    public void setLeadDeptWorkload1(String leadDeptWorkload1) {
        this.leadDeptWorkload1 = leadDeptWorkload1;
    }

    public String getCoorDeptWorkload1() {
        return coorDeptWorkload1;
    }

    public void setCoorDeptWorkload1(String coorDeptWorkload1) {
        this.coorDeptWorkload1 = coorDeptWorkload1;
    }
}
