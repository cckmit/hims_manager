package com.cmpay.lemon.monitor.bo;


import java.util.Date;



public class ProblemBO {

    private int problemSerialNumber;
    private String proNumber;
    private String problemDetail;
    private Date problemTime;




    public ProblemBO() {

    }

    public ProblemBO(String proNumber,
                       String problemDetail) {
        this.proNumber = proNumber;
        this.problemDetail = problemDetail;
    }

    public ProblemBO(int problemSerialNumber, String proNumber,
                       String problemDetail) {
        super();
        this.problemSerialNumber = problemSerialNumber;
        this.proNumber = proNumber;
        this.problemDetail = problemDetail;
    }

    public ProblemBO(int problemSerialNumber, String proNumber,
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

    @Override
    public String toString() {
        return "ProblemBO{" +
                "problemSerialNumber=" + problemSerialNumber +
                ", proNumber='" + proNumber + '\'' +
                ", problemDetail='" + problemDetail + '\'' +
                ", problemTime=" + problemTime +
                '}';
    }
}
