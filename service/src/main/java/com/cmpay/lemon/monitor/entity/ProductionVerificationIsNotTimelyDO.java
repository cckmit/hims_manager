/*
 * @ClassName ProductionVerificationIsNotTimelyDO
 * @Description 
 * @version 1.0
 * @Date 2020-09-15 17:49:48
 */
package com.cmpay.lemon.monitor.entity;

import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;

@DataObject
public class ProductionVerificationIsNotTimelyDO extends BaseDO {
    /**
     * @Fields proNumber 
     */
    private String proNumber;
    /**
     * @Fields proNeed 
     */
    private String proNeed;
    /**
     * @Fields proType 
     */
    private String proType;
    /**
     * @Fields validation 
     */
    private String validation;
    /**
     * @Fields proDate 
     */
    private String proDate;
    /**
     * @Fields identifier 
     */
    private String identifier;
    /**
     * @Fields sumDay 
     */
    private String sumDay;
    /**
     * @Fields department 
     */
    private String department;

    public String getProNumber() {
        return proNumber;
    }

    public void setProNumber(String proNumber) {
        this.proNumber = proNumber;
    }

    public String getProNeed() {
        return proNeed;
    }

    public void setProNeed(String proNeed) {
        this.proNeed = proNeed;
    }

    public String getProType() {
        return proType;
    }

    public void setProType(String proType) {
        this.proType = proType;
    }

    public String getValidation() {
        return validation;
    }

    public void setValidation(String validation) {
        this.validation = validation;
    }

    public String getProDate() {
        return proDate;
    }

    public void setProDate(String proDate) {
        this.proDate = proDate;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getSumDay() {
        return sumDay;
    }

    public void setSumDay(String sumDay) {
        this.sumDay = sumDay;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}