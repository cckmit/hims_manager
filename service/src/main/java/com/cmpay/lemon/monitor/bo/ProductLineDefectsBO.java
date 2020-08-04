package com.cmpay.lemon.monitor.bo;


/*
 *产品线或部门缺陷率
 */
public class ProductLineDefectsBO {

    //产品线/部门
    private String productLine;
    //缺陷数
    private String defectsNumber;
    //工作量
    private String workload;
    //缺陷率
    private String defectRate;

    public String getProductLine() {
        return productLine;
    }

    public void setProductLine(String productLine) {
        this.productLine = productLine;
    }

    public String getDefectsNumber() {
        return defectsNumber;
    }

    public void setDefectsNumber(String defectsNumber) {
        this.defectsNumber = defectsNumber;
    }

    public String getWorkload() {
        return workload;
    }

    public void setWorkload(String workload) {
        this.workload = workload;
    }

    public String getDefectRate() {
        return defectRate;
    }

    public void setDefectRate(String defectRate) {
        this.defectRate = defectRate;
    }
}
