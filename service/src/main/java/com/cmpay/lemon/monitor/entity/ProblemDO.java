/*
 * @ClassName productionPicDO
 * @Description 
 * @version 1.0
 * @Date 2019-10-20 10:04:37
 */
package com.cmpay.lemon.monitor.entity;

import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;

import java.sql.Date;

@DataObject
public class ProblemDO extends BaseDO {
    private int problemSerialNumber;
    private String proNumber;
    private String problemDetail;
    private Date problemTime;

    public int getProblemSerialNumber() {
        return problemSerialNumber;
    }

    public void setProblemSerialNumber(int problemSerialNumber) {
        this.problemSerialNumber = problemSerialNumber;
    }

    public String getProNumber() {
        return proNumber;
    }

    public void setProNumber(String proNumber) {
        this.proNumber = proNumber;
    }

    public String getProblemDetail() {
        return problemDetail;
    }

    public void setProblemDetail(String problemDetail) {
        this.problemDetail = problemDetail;
    }

    public Date getProblemTime() {
        return problemTime;
    }

    public void setProblemTime(Date problemTime) {
        this.problemTime = problemTime;
    }

    public ProblemDO() {
    }

    @Override
    public String toString() {
        return "ProblemDO{" +
                "problemSerialNumber=" + problemSerialNumber +
                ", proNumber='" + proNumber + '\'' +
                ", problemDetail='" + problemDetail + '\'' +
                ", problemTime=" + problemTime +
                '}';
    }
}