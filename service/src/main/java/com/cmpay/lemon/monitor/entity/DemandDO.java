/*
 * @ClassName CenterDO
 * @Description 
 * @version 1.0
 * @Date 2019-07-25 11:01:18
 */
package com.cmpay.lemon.monitor.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;

import java.util.Date;

@DataObject
public class DemandDO extends BaseDO {
    @Excel(name = "归属部门")
    private String reqProDept;
    @Excel(name = "需求提出人")
    private String reqProposer;
    @Excel(name = "需求负责人")
    private String reqMnger;
    @Excel(name = "产品线")
    private String reqPrdLine;
    @Excel(name = "需求名称")
    private String reqNm;
    @Excel(name = "需求描述")
    private String reqDesc;
    @Excel(name = "当月预计投入（人月）")
    private double expInput;
    @Excel(name = "是否核减")
    private String isCut;
    @Excel(name = "月初备注")
    private String monRemark;
    @Excel(name = "预计投产时间")
    private String expPrdReleaseTm;
    @Excel(name = "月初需求阶段")
    private String preMonPeriod;
    @Excel(name = "本月预计完成阶段")
    private String curMonTarget;
    @Excel(name = "内部编号")
    private String reqInnerSeq;
    @Excel(name = "需求编号")
    private String reqNo;
    @Excel(name = "投入资源")
    private int inputRes;
    @Excel(name = "开发周期")
    private int devCycle;
    @Excel(name = "反馈时间")
    private String riskFeedbackTm;
    @Excel(name = "最新进展")
    private String preCurPeriod;
    @Excel(name = "风险点、问题及解决方案")
    private String riskSolution;
    @Excel(name = "PRD定稿时间")
    private String prdFinshTm;
    @Excel(name = "UAT更新时间")
    private String uatUpdateTm;
    @Excel(name = "高阳开发负责部门")
    private String devpLeadDept;
    @Excel(name = "高阳开发配合部门")
    private String devpCoorDept;
    @Excel(name = "高阳产品经理")
    private String productMng;
    @Excel(name = "需求启动月份")
    private String reqStartMon;
    @Excel(name = "需求实施月份")
    private String reqImplMon;
    @Excel(name = "需求类型")
    private String reqType;
    @Excel(name = "需求状态")
    private String reqSts;
    private String actPrdFinshTm;
    private String devpResMng;
    private String projectMng;
    private String qaMng;
    private String configMng;
    private String devpEng;
    private String frontEng;
    private String testEng;
    private String actUatUpdateTm;
    private String preTm;
    private String testFinshTm;
    private String actTestFinshTm;
    @Excel(name = "月底反馈")
    private String endMonRemark;
    private String endFeedbackTm;
    private int totalWorkload;
    //已录入总工作量
    private int inputWorkload;
    //上月录入
    private int lastInputWorkload;
    //剩余录入工作量
    private int remainWorkload;
    //本月录入工作量
    private int monInputWorkload;
    //	主导部门工作量占比
    private String leadDeptPro;
    //	配合部门工作量占比
    private String coorDeptPro;
    //	主导部门工作量
    private String leadDeptWorkload;
    //	配合部门工作量
    private String coorDeptWorkload;
    //是否建立svn目录
    private String isSvnBuild;

    private String proId;
    private String projectStartTm;
    private String actPrdUploadTm;
    private String actWorkloadUploadTm;
    private String actSitUploadTm;
    private String actTestCasesUploadTm;
    private String actUatUploadTm;
    private String actPreUploadTm;
    private String actProductionUploadTm;

    private Date creatTime;
    private Date updateTime;
    private String creatUser;
    private String updateUser;
    private String reqAbnorType;
    public DemandDO() {
    }

    public DemandDO(String reqInnerSeq, String reqPrdLine, String reqType, String reqSts, String reqNo, String reqNm, String reqDesc, int inputRes, int devCycle, double expInput, String prdFinshTm, String actPrdFinshTm, String preCurPeriod, String curMonTarget, String expPrdReleaseTm, String devpLeadDept, String devpCoorDept, String reqProDept, String reqProposer, String reqMnger, String devpResMng, String reqStartMon, String reqImplMon, String projectMng, String productMng, String isCut, String preMonPeriod, String qaMng, String configMng, String monRemark, String devpEng, String frontEng, String testEng, String uatUpdateTm, String actUatUpdateTm, String preTm, String testFinshTm, String actTestFinshTm, String riskSolution, String riskFeedbackTm, String endMonRemark, String endFeedbackTm, int totalWorkload, int inputWorkload, int lastInputWorkload, int remainWorkload, int monInputWorkload, String leadDeptPro, String coorDeptPro, String leadDeptWorkload, String coorDeptWorkload, String isSvnBuild, Date creatTime, Date updateTime, String creatUser, String updateUser, String reqAbnorType) {
        this.reqInnerSeq = reqInnerSeq;
        this.reqPrdLine = reqPrdLine;
        this.reqType = reqType;
        this.reqSts = reqSts;
        this.reqNo = reqNo;
        this.reqNm = reqNm;
        this.reqDesc = reqDesc;
        this.inputRes = inputRes;
        this.devCycle = devCycle;
        this.expInput = expInput;
        this.prdFinshTm = prdFinshTm;
        this.actPrdFinshTm = actPrdFinshTm;
        this.preCurPeriod = preCurPeriod;
        this.curMonTarget = curMonTarget;
        this.expPrdReleaseTm = expPrdReleaseTm;
        this.devpLeadDept = devpLeadDept;
        this.devpCoorDept = devpCoorDept;
        this.reqProDept = reqProDept;
        this.reqProposer = reqProposer;
        this.reqMnger = reqMnger;
        this.devpResMng = devpResMng;
        this.reqStartMon = reqStartMon;
        this.reqImplMon = reqImplMon;
        this.projectMng = projectMng;
        this.productMng = productMng;
        this.isCut = isCut;
        this.preMonPeriod = preMonPeriod;
        this.qaMng = qaMng;
        this.configMng = configMng;
        this.monRemark = monRemark;
        this.devpEng = devpEng;
        this.frontEng = frontEng;
        this.testEng = testEng;
        this.uatUpdateTm = uatUpdateTm;
        this.actUatUpdateTm = actUatUpdateTm;
        this.preTm = preTm;
        this.testFinshTm = testFinshTm;
        this.actTestFinshTm = actTestFinshTm;
        this.riskSolution = riskSolution;
        this.riskFeedbackTm = riskFeedbackTm;
        this.endMonRemark = endMonRemark;
        this.endFeedbackTm = endFeedbackTm;
        this.totalWorkload = totalWorkload;
        this.inputWorkload = inputWorkload;
        this.lastInputWorkload = lastInputWorkload;
        this.remainWorkload = remainWorkload;
        this.monInputWorkload = monInputWorkload;
        this.leadDeptPro = leadDeptPro;
        this.coorDeptPro = coorDeptPro;
        this.leadDeptWorkload = leadDeptWorkload;
        this.coorDeptWorkload = coorDeptWorkload;
        this.isSvnBuild = isSvnBuild;
        this.creatTime = creatTime;
        this.updateTime = updateTime;
        this.creatUser = creatUser;
        this.updateUser = updateUser;
        this.reqAbnorType = reqAbnorType;
    }

    public DemandDO(String reqInnerSeq, String reqNm, String reqNo, String devpLeadDept, String devpCoorDept,
                        String projectMng, String devpEng, String frontEng, String productMng, String testEng,
                        String prdFinshTm, String uatUpdateTm, String testFinshTm, String expPrdReleaseTm, String reqPrdLine,
                        String preCurPeriod, String reqSts, String curMonTarget) {
        super();
        this.reqInnerSeq = reqInnerSeq;
        this.reqNm = reqNm;
        this.reqNo = reqNo;
        this.devpLeadDept = devpLeadDept;
        this.devpCoorDept = devpCoorDept;
        this.projectMng = projectMng;
        this.devpEng = devpEng;
        this.frontEng = frontEng;
        this.productMng = productMng;
        this.testEng = testEng;
        this.prdFinshTm = prdFinshTm;
        this.uatUpdateTm = uatUpdateTm;
        this.testFinshTm = testFinshTm;
        this.reqPrdLine = reqPrdLine;
        this.preCurPeriod = preCurPeriod;
        this.reqSts = reqSts;
        this.curMonTarget = curMonTarget;
        this.expPrdReleaseTm=expPrdReleaseTm;
    }

    public String getReqProDept() {
        return reqProDept;
    }

    public void setReqProDept(String reqProDept) {
        this.reqProDept = reqProDept;
    }

    public String getReqProposer() {
        return reqProposer;
    }

    public void setReqProposer(String reqProposer) {
        this.reqProposer = reqProposer;
    }

    public String getReqMnger() {
        return reqMnger;
    }

    public void setReqMnger(String reqMnger) {
        this.reqMnger = reqMnger;
    }

    public String getReqPrdLine() {
        return reqPrdLine;
    }

    public void setReqPrdLine(String reqPrdLine) {
        this.reqPrdLine = reqPrdLine;
    }

    public String getReqNm() {
        return reqNm;
    }

    public void setReqNm(String reqNm) {
        this.reqNm = reqNm;
    }

    public String getReqDesc() {
        return reqDesc;
    }

    public void setReqDesc(String reqDesc) {
        this.reqDesc = reqDesc;
    }

    public double getExpInput() {
        return expInput;
    }

    public void setExpInput(double expInput) {
        this.expInput = expInput;
    }

    public String getIsCut() {
        return isCut;
    }

    public void setIsCut(String isCut) {
        this.isCut = isCut;
    }

    public String getMonRemark() {
        return monRemark;
    }

    public void setMonRemark(String monRemark) {
        this.monRemark = monRemark;
    }

    public String getExpPrdReleaseTm() {
        return expPrdReleaseTm;
    }

    public void setExpPrdReleaseTm(String expPrdReleaseTm) {
        this.expPrdReleaseTm = expPrdReleaseTm;
    }

    public String getPreMonPeriod() {
        return preMonPeriod;
    }

    public void setPreMonPeriod(String preMonPeriod) {
        this.preMonPeriod = preMonPeriod;
    }

    public String getCurMonTarget() {
        return curMonTarget;
    }

    public void setCurMonTarget(String curMonTarget) {
        this.curMonTarget = curMonTarget;
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

    public int getInputRes() {
        return inputRes;
    }

    public void setInputRes(int inputRes) {
        this.inputRes = inputRes;
    }

    public int getDevCycle() {
        return devCycle;
    }

    public void setDevCycle(int devCycle) {
        this.devCycle = devCycle;
    }

    public String getRiskFeedbackTm() {
        return riskFeedbackTm;
    }

    public void setRiskFeedbackTm(String riskFeedbackTm) {
        this.riskFeedbackTm = riskFeedbackTm;
    }

    public String getPreCurPeriod() {
        return preCurPeriod;
    }

    public void setPreCurPeriod(String preCurPeriod) {
        this.preCurPeriod = preCurPeriod;
    }

    public String getRiskSolution() {
        return riskSolution;
    }

    public void setRiskSolution(String riskSolution) {
        this.riskSolution = riskSolution;
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

    public String getDevpLeadDept() {
        return devpLeadDept;
    }

    public void setDevpLeadDept(String devpLeadDept) {
        this.devpLeadDept = devpLeadDept;
    }

    public String getDevpCoorDept() {
        return devpCoorDept;
    }

    public void setDevpCoorDept(String devpCoorDept) {
        this.devpCoorDept = devpCoorDept;
    }

    public String getProductMng() {
        return productMng;
    }

    public void setProductMng(String productMng) {
        this.productMng = productMng;
    }

    public String getReqStartMon() {
        return reqStartMon;
    }

    public void setReqStartMon(String reqStartMon) {
        this.reqStartMon = reqStartMon;
    }

    public String getReqImplMon() {
        return reqImplMon;
    }

    public void setReqImplMon(String reqImplMon) {
        this.reqImplMon = reqImplMon;
    }

    public String getReqType() {
        return reqType;
    }

    public void setReqType(String reqType) {
        this.reqType = reqType;
    }

    public String getReqSts() {
        return reqSts;
    }

    public void setReqSts(String reqSts) {
        this.reqSts = reqSts;
    }

    public String getActPrdFinshTm() {
        return actPrdFinshTm;
    }

    public void setActPrdFinshTm(String actPrdFinshTm) {
        this.actPrdFinshTm = actPrdFinshTm;
    }

    public String getDevpResMng() {
        return devpResMng;
    }

    public void setDevpResMng(String devpResMng) {
        this.devpResMng = devpResMng;
    }

    public String getProjectMng() {
        return projectMng;
    }

    public void setProjectMng(String projectMng) {
        this.projectMng = projectMng;
    }

    public String getQaMng() {
        return qaMng;
    }

    public void setQaMng(String qaMng) {
        this.qaMng = qaMng;
    }

    public String getConfigMng() {
        return configMng;
    }

    public void setConfigMng(String configMng) {
        this.configMng = configMng;
    }

    public String getDevpEng() {
        return devpEng;
    }

    public void setDevpEng(String devpEng) {
        this.devpEng = devpEng;
    }

    public String getFrontEng() {
        return frontEng;
    }

    public void setFrontEng(String frontEng) {
        this.frontEng = frontEng;
    }

    public String getTestEng() {
        return testEng;
    }

    public void setTestEng(String testEng) {
        this.testEng = testEng;
    }

    public String getActUatUpdateTm() {
        return actUatUpdateTm;
    }

    public void setActUatUpdateTm(String actUatUpdateTm) {
        this.actUatUpdateTm = actUatUpdateTm;
    }

    public String getPreTm() {
        return preTm;
    }

    public void setPreTm(String preTm) {
        this.preTm = preTm;
    }

    public String getTestFinshTm() {
        return testFinshTm;
    }

    public void setTestFinshTm(String testFinshTm) {
        this.testFinshTm = testFinshTm;
    }

    public String getActTestFinshTm() {
        return actTestFinshTm;
    }

    public void setActTestFinshTm(String actTestFinshTm) {
        this.actTestFinshTm = actTestFinshTm;
    }

    public String getEndMonRemark() {
        return endMonRemark;
    }

    public void setEndMonRemark(String endMonRemark) {
        this.endMonRemark = endMonRemark;
    }

    public String getEndFeedbackTm() {
        return endFeedbackTm;
    }

    public void setEndFeedbackTm(String endFeedbackTm) {
        this.endFeedbackTm = endFeedbackTm;
    }

    public int getTotalWorkload() {
        return totalWorkload;
    }

    public void setTotalWorkload(int totalWorkload) {
        this.totalWorkload = totalWorkload;
    }

    public int getInputWorkload() {
        return inputWorkload;
    }

    public void setInputWorkload(int inputWorkload) {
        this.inputWorkload = inputWorkload;
    }

    public int getLastInputWorkload() {
        return lastInputWorkload;
    }

    public void setLastInputWorkload(int lastInputWorkload) {
        this.lastInputWorkload = lastInputWorkload;
    }

    public int getRemainWorkload() {
        return remainWorkload;
    }

    public void setRemainWorkload(int remainWorkload) {
        this.remainWorkload = remainWorkload;
    }

    public int getMonInputWorkload() {
        return monInputWorkload;
    }

    public void setMonInputWorkload(int monInputWorkload) {
        this.monInputWorkload = monInputWorkload;
    }

    public String getLeadDeptPro() {
        return leadDeptPro;
    }

    public void setLeadDeptPro(String leadDeptPro) {
        this.leadDeptPro = leadDeptPro;
    }

    public String getCoorDeptPro() {
        return coorDeptPro;
    }

    public void setCoorDeptPro(String coorDeptPro) {
        this.coorDeptPro = coorDeptPro;
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

    public String getIsSvnBuild() {
        return isSvnBuild;
    }

    public void setIsSvnBuild(String isSvnBuild) {
        this.isSvnBuild = isSvnBuild;
    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public String getProjectStartTm() {
        return projectStartTm;
    }

    public void setProjectStartTm(String projectStartTm) {
        this.projectStartTm = projectStartTm;
    }

    public String getActPrdUploadTm() {
        return actPrdUploadTm;
    }

    public void setActPrdUploadTm(String actPrdUploadTm) {
        this.actPrdUploadTm = actPrdUploadTm;
    }

    public String getActWorkloadUploadTm() {
        return actWorkloadUploadTm;
    }

    public void setActWorkloadUploadTm(String actWorkloadUploadTm) {
        this.actWorkloadUploadTm = actWorkloadUploadTm;
    }

    public String getActSitUploadTm() {
        return actSitUploadTm;
    }

    public void setActSitUploadTm(String actSitUploadTm) {
        this.actSitUploadTm = actSitUploadTm;
    }

    public String getActTestCasesUploadTm() {
        return actTestCasesUploadTm;
    }

    public void setActTestCasesUploadTm(String actTestCasesUploadTm) {
        this.actTestCasesUploadTm = actTestCasesUploadTm;
    }

    public String getActUatUploadTm() {
        return actUatUploadTm;
    }

    public void setActUatUploadTm(String actUatUploadTm) {
        this.actUatUploadTm = actUatUploadTm;
    }

    public String getActPreUploadTm() {
        return actPreUploadTm;
    }

    public void setActPreUploadTm(String actPreUploadTm) {
        this.actPreUploadTm = actPreUploadTm;
    }

    public String getActProductionUploadTm() {
        return actProductionUploadTm;
    }

    public void setActProductionUploadTm(String actProductionUploadTm) {
        this.actProductionUploadTm = actProductionUploadTm;
    }

    public Date getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreatUser() {
        return creatUser;
    }

    public void setCreatUser(String creatUser) {
        this.creatUser = creatUser;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getReqAbnorType() {
        return reqAbnorType;
    }

    public void setReqAbnorType(String reqAbnorType) {
        this.reqAbnorType = reqAbnorType;
    }

    @Override
    public String toString() {
        return "DemandDO{" +
                "reqProDept='" + reqProDept + '\'' +
                ", reqProposer='" + reqProposer + '\'' +
                ", reqMnger='" + reqMnger + '\'' +
                ", reqPrdLine='" + reqPrdLine + '\'' +
                ", reqNm='" + reqNm + '\'' +
                ", reqDesc='" + reqDesc + '\'' +
                ", expInput=" + expInput +
                ", isCut='" + isCut + '\'' +
                ", monRemark='" + monRemark + '\'' +
                ", expPrdReleaseTm='" + expPrdReleaseTm + '\'' +
                ", preMonPeriod='" + preMonPeriod + '\'' +
                ", curMonTarget='" + curMonTarget + '\'' +
                ", reqInnerSeq='" + reqInnerSeq + '\'' +
                ", reqNo='" + reqNo + '\'' +
                ", inputRes=" + inputRes +
                ", devCycle=" + devCycle +
                ", riskFeedbackTm='" + riskFeedbackTm + '\'' +
                ", preCurPeriod='" + preCurPeriod + '\'' +
                ", riskSolution='" + riskSolution + '\'' +
                ", prdFinshTm='" + prdFinshTm + '\'' +
                ", uatUpdateTm='" + uatUpdateTm + '\'' +
                ", devpLeadDept='" + devpLeadDept + '\'' +
                ", devpCoorDept='" + devpCoorDept + '\'' +
                ", productMng='" + productMng + '\'' +
                ", reqStartMon='" + reqStartMon + '\'' +
                ", reqImplMon='" + reqImplMon + '\'' +
                ", reqType='" + reqType + '\'' +
                ", reqSts='" + reqSts + '\'' +
                ", actPrdFinshTm='" + actPrdFinshTm + '\'' +
                ", devpResMng='" + devpResMng + '\'' +
                ", projectMng='" + projectMng + '\'' +
                ", qaMng='" + qaMng + '\'' +
                ", configMng='" + configMng + '\'' +
                ", devpEng='" + devpEng + '\'' +
                ", frontEng='" + frontEng + '\'' +
                ", testEng='" + testEng + '\'' +
                ", actUatUpdateTm='" + actUatUpdateTm + '\'' +
                ", preTm='" + preTm + '\'' +
                ", testFinshTm='" + testFinshTm + '\'' +
                ", actTestFinshTm='" + actTestFinshTm + '\'' +
                ", endMonRemark='" + endMonRemark + '\'' +
                ", endFeedbackTm='" + endFeedbackTm + '\'' +
                ", totalWorkload=" + totalWorkload +
                ", inputWorkload=" + inputWorkload +
                ", lastInputWorkload=" + lastInputWorkload +
                ", remainWorkload=" + remainWorkload +
                ", monInputWorkload=" + monInputWorkload +
                ", leadDeptPro='" + leadDeptPro + '\'' +
                ", coorDeptPro='" + coorDeptPro + '\'' +
                ", leadDeptWorkload='" + leadDeptWorkload + '\'' +
                ", coorDeptWorkload='" + coorDeptWorkload + '\'' +
                ", isSvnBuild='" + isSvnBuild + '\'' +
                ", proId='" + proId + '\'' +
                ", projectStartTm='" + projectStartTm + '\'' +
                ", actPrdUploadTm='" + actPrdUploadTm + '\'' +
                ", actWorkloadUploadTm='" + actWorkloadUploadTm + '\'' +
                ", actSitUploadTm='" + actSitUploadTm + '\'' +
                ", actTestCasesUploadTm='" + actTestCasesUploadTm + '\'' +
                ", actUatUploadTm='" + actUatUploadTm + '\'' +
                ", actPreUploadTm='" + actPreUploadTm + '\'' +
                ", actProductionUploadTm='" + actProductionUploadTm + '\'' +
                ", creatTime=" + creatTime +
                ", updateTime=" + updateTime +
                ", creatUser='" + creatUser + '\'' +
                ", updateUser='" + updateUser + '\'' +
                ", reqAbnorType='" + reqAbnorType + '\'' +
                '}';
    }
}