package com.cmpay.lemon.monitor.dto;


import java.util.Date;


public class ProblemDTO {

    private int problemSerialNumber;
    private String proNumber;
    private String problemDetail;
    private Date problemTime;




    public ProblemDTO() {

    }

    public ProblemDTO(String proNumber,
                      String problemDetail) {
        this.proNumber = proNumber;
        this.problemDetail = problemDetail;
    }

    public ProblemDTO(int problemSerialNumber, String proNumber,
                      String problemDetail) {
        super();
        this.problemSerialNumber = problemSerialNumber;
        this.proNumber = proNumber;
        this.problemDetail = problemDetail;
    }

    public ProblemDTO(int problemSerialNumber, String proNumber,
                      String problemDetail, Date problemTime) {
        super();
        this.problemSerialNumber = problemSerialNumber;
        this.proNumber = proNumber;
        this.problemDetail = problemDetail;
        this.problemTime = problemTime;
    }


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
}
