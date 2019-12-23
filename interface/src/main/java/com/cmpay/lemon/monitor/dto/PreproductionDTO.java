package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.request.GenericDTO;

import java.sql.Date;

/**
 * @author: zhou_xiong
 */
public class PreproductionDTO extends GenericDTO {
    //@Excel(name = "投产编号")
    private String preNumber;
    //@Excel(name = "需求名称及内容简述")
    private String preNeed;
    //@Excel(name = "投产类型")
    private String preType;
    private java.sql.Date preDateStart;
    private java.sql.Date preDateEnd;
    // @Excel(name = "计划投产日期")
    private java.sql.Date preDate;
    //@Excel(name = "申请部门")
    private String applicationDept;
    //@Excel(name = "预投产申请人")
    private String preApplicant;
    //@Excel(name = "申请人联系方式")
    private String applicantTel;
    //@Excel(name = "产品经理")
    private String preManager;
    //@Excel(name = "开发负责人")
    private String developmentLeader;
    //@Excel(name = "开发负责人邮箱")
    private String mailLeader;
    //@Excel(name = "验证人")
    private String identifier;
    //@Excel(name = "验证人联系方式")
    private String identifierTel;
    //@Excel(name = "验证复核人")
    private String prChecker;
    //@Excel(name = "验证复核人联系方式")
    private String checkerTel;
    //@Excel(name = "预投产需求状态")
    private String preStatus;
    //@Excel(name = "预投产验证结果")
    private String preAdvanceResult;
    //@Excel(name = "备注")
    private String remark;
    //@Excel(name = "预投产包上传时间")
    private Date proPkgTime;
    //@Excel(name = "预投产包名")
    private String proPkgName;

    /**
     * 页数
     */
    private int pageNum;
    /**
     * 页面大小
     */
    private int pageSize;

    public PreproductionDTO() {
    }

    public PreproductionDTO(String preNumber, String preNeed, String preType, Date preDateStart, Date preDateEnd, Date preDate, String applicationDept, String preApplicant, String applicantTel, String preManager, String developmentLeader, String mailLeader, String identifier, String identifierTel, String prChecker, String checkerTel, String preStatus, String preAdvanceResult, String remark, Date proPkgTime, String proPkgName, int pageNum, int pageSize) {
        this.preNumber = preNumber;
        this.preNeed = preNeed;
        this.preType = preType;
        this.preDateStart = preDateStart;
        this.preDateEnd = preDateEnd;
        this.preDate = preDate;
        this.applicationDept = applicationDept;
        this.preApplicant = preApplicant;
        this.applicantTel = applicantTel;
        this.preManager = preManager;
        this.developmentLeader = developmentLeader;
        this.mailLeader = mailLeader;
        this.identifier = identifier;
        this.identifierTel = identifierTel;
        this.prChecker = prChecker;
        this.checkerTel = checkerTel;
        this.preStatus = preStatus;
        this.preAdvanceResult = preAdvanceResult;
        this.remark = remark;
        this.proPkgTime = proPkgTime;
        this.proPkgName = proPkgName;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "PreproductionDTO{" +
                "preNumber='" + preNumber + '\'' +
                ", preNeed='" + preNeed + '\'' +
                ", preType='" + preType + '\'' +
                ", preDateStart=" + preDateStart +
                ", preDateEnd=" + preDateEnd +
                ", preDate=" + preDate +
                ", applicationDept='" + applicationDept + '\'' +
                ", preApplicant='" + preApplicant + '\'' +
                ", applicantTel='" + applicantTel + '\'' +
                ", preManager='" + preManager + '\'' +
                ", developmentLeader='" + developmentLeader + '\'' +
                ", mailLeader='" + mailLeader + '\'' +
                ", identifier='" + identifier + '\'' +
                ", identifierTel='" + identifierTel + '\'' +
                ", prChecker='" + prChecker + '\'' +
                ", checkerTel='" + checkerTel + '\'' +
                ", preStatus='" + preStatus + '\'' +
                ", preAdvanceResult='" + preAdvanceResult + '\'' +
                ", remark='" + remark + '\'' +
                ", proPkgTime=" + proPkgTime +
                ", proPkgName='" + proPkgName + '\'' +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }

    public String getPreNumber() {
        return preNumber;
    }

    public void setPreNumber(String preNumber) {
        this.preNumber = preNumber;
    }

    public String getPreNeed() {
        return preNeed;
    }

    public void setPreNeed(String preNeed) {
        this.preNeed = preNeed;
    }

    public String getPreType() {
        return preType;
    }

    public void setPreType(String preType) {
        this.preType = preType;
    }

    public Date getPreDateStart() {
        return preDateStart;
    }

    public void setPreDateStart(Date preDateStart) {
        this.preDateStart = preDateStart;
    }

    public Date getPreDateEnd() {
        return preDateEnd;
    }

    public void setPreDateEnd(Date preDateEnd) {
        this.preDateEnd = preDateEnd;
    }

    public Date getPreDate() {
        return preDate;
    }

    public void setPreDate(Date preDate) {
        this.preDate = preDate;
    }

    public String getApplicationDept() {
        return applicationDept;
    }

    public void setApplicationDept(String applicationDept) {
        this.applicationDept = applicationDept;
    }

    public String getPreApplicant() {
        return preApplicant;
    }

    public void setPreApplicant(String preApplicant) {
        this.preApplicant = preApplicant;
    }

    public String getApplicantTel() {
        return applicantTel;
    }

    public void setApplicantTel(String applicantTel) {
        this.applicantTel = applicantTel;
    }

    public String getPreManager() {
        return preManager;
    }

    public void setPreManager(String preManager) {
        this.preManager = preManager;
    }

    public String getDevelopmentLeader() {
        return developmentLeader;
    }

    public void setDevelopmentLeader(String developmentLeader) {
        this.developmentLeader = developmentLeader;
    }

    public String getMailLeader() {
        return mailLeader;
    }

    public void setMailLeader(String mailLeader) {
        this.mailLeader = mailLeader;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifierTel() {
        return identifierTel;
    }

    public void setIdentifierTel(String identifierTel) {
        this.identifierTel = identifierTel;
    }

    public String getPrChecker() {
        return prChecker;
    }

    public void setPrChecker(String prChecker) {
        this.prChecker = prChecker;
    }

    public String getCheckerTel() {
        return checkerTel;
    }

    public void setCheckerTel(String checkerTel) {
        this.checkerTel = checkerTel;
    }

    public String getPreStatus() {
        return preStatus;
    }

    public void setPreStatus(String preStatus) {
        this.preStatus = preStatus;
    }

    public String getPreAdvanceResult() {
        return preAdvanceResult;
    }

    public void setPreAdvanceResult(String preAdvanceResult) {
        this.preAdvanceResult = preAdvanceResult;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getProPkgTime() {
        return proPkgTime;
    }

    public void setProPkgTime(Date proPkgTime) {
        this.proPkgTime = proPkgTime;
    }

    public String getProPkgName() {
        return proPkgName;
    }

    public void setProPkgName(String proPkgName) {
        this.proPkgName = proPkgName;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
