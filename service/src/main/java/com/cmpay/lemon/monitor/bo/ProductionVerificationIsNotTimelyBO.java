/*
 * @ClassName CenterDO
 * @Description
 * @version 1.0
 * @Date 2019-07-25 11:01:18
 */
package com.cmpay.lemon.monitor.bo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;

import java.sql.Date;
import java.sql.Timestamp;

public class ProductionVerificationIsNotTimelyBO {
    private String proNumber;
    private String proNeed;
    private String proType;
    private String validation;
    private String proDate;
    private String identifier;
    private String sumDay;
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

    @Override
    public String toString() {
        return "ProductionVerificationIsNotTimelyBO{" +
                "proNumber='" + proNumber + '\'' +
                ", proNeed='" + proNeed + '\'' +
                ", proType='" + proType + '\'' +
                ", validation='" + validation + '\'' +
                ", proDate='" + proDate + '\'' +
                ", identifier='" + identifier + '\'' +
                ", sumDay='" + sumDay + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}
