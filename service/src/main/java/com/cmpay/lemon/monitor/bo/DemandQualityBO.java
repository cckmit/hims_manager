/*
 * @ClassName demandQualityDO
 * @Description
 * @version 1.0
 * @Date 2020-06-29 14:54:22
 */
package com.cmpay.lemon.monitor.bo;

import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;

public class DemandQualityBO {
    /**
     * @Fields epickey epic编号
     */
    private String epickey;
    /**
     * @Fields smokePassNumber 冒烟不通过次数
     */
    private Integer smokePassNumber;
    /**
     * @Fields buildFailuresNumber 构建失败次数
     */
    private Integer buildFailuresNumber;
    /**
     * @Fields productionNumber 投产次数
     */
    private Integer productionNumber;
    /**
     * @Fields productionProblemsNumber 投产问题数
     */
    private Integer productionProblemsNumber;
    /**
     * @Fields defectsNumber 总缺陷数
     */
    private Integer defectsNumber;
    /**
     * @Fields reviewProblemNumber 总评审问题
     */
    private Integer reviewProblemNumber;
    /**
     * @Fields productionNoDefectsNumber 投产时未解决缺陷数
     */
    private Integer productionNoDefectsNumber;
    /**
     * @Fields productionNoReviewNumber 投产时未解决评审问题
     */
    private Integer productionNoReviewNumber;

    public String getEpickey() {
        return epickey;
    }

    public void setEpickey(String epickey) {
        this.epickey = epickey;
    }

    public Integer getSmokePassNumber() {
        return smokePassNumber;
    }

    public void setSmokePassNumber(Integer smokePassNumber) {
        this.smokePassNumber = smokePassNumber;
    }

    public Integer getBuildFailuresNumber() {
        return buildFailuresNumber;
    }

    public void setBuildFailuresNumber(Integer buildFailuresNumber) {
        this.buildFailuresNumber = buildFailuresNumber;
    }

    public Integer getProductionNumber() {
        return productionNumber;
    }

    public void setProductionNumber(Integer productionNumber) {
        this.productionNumber = productionNumber;
    }

    public Integer getProductionProblemsNumber() {
        return productionProblemsNumber;
    }

    public void setProductionProblemsNumber(Integer productionProblemsNumber) {
        this.productionProblemsNumber = productionProblemsNumber;
    }

    public Integer getDefectsNumber() {
        return defectsNumber;
    }

    public void setDefectsNumber(Integer defectsNumber) {
        this.defectsNumber = defectsNumber;
    }

    public Integer getReviewProblemNumber() {
        return reviewProblemNumber;
    }

    public void setReviewProblemNumber(Integer reviewProblemNumber) {
        this.reviewProblemNumber = reviewProblemNumber;
    }

    public Integer getProductionNoDefectsNumber() {
        return productionNoDefectsNumber;
    }

    public void setProductionNoDefectsNumber(Integer productionNoDefectsNumber) {
        this.productionNoDefectsNumber = productionNoDefectsNumber;
    }

    public Integer getProductionNoReviewNumber() {
        return productionNoReviewNumber;
    }

    public void setProductionNoReviewNumber(Integer productionNoReviewNumber) {
        this.productionNoReviewNumber = productionNoReviewNumber;
    }

    @Override
    public String toString() {
        return "demandQualityDO{" +
                "epickey='" + epickey + '\'' +
                ", smokePassNumber=" + smokePassNumber +
                ", buildFailuresNumber=" + buildFailuresNumber +
                ", productionNumber=" + productionNumber +
                ", productionProblemsNumber=" + productionProblemsNumber +
                ", defectsNumber=" + defectsNumber +
                ", reviewProblemNumber=" + reviewProblemNumber +
                ", productionNoDefectsNumber=" + productionNoDefectsNumber +
                ", productionNoReviewNumber=" + productionNoReviewNumber +
                '}';
    }
}
