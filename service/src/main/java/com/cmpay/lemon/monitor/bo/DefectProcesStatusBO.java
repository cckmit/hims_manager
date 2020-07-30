package com.cmpay.lemon.monitor.bo;

public class DefectProcesStatusBO {

    //部门
    private String department;
    //处理中数量
    private int process;
    //待处理
    private int pending;
    //待更新
    private int pendingUpgrade;
    //待重测
    private int withRetest;
    //问题冻结
    private int problemFreeze;

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getProcess() {
        return process;
    }

    public void setProcess(int process) {
        this.process = process;
    }

    public int getPending() {
        return pending;
    }

    public void setPending(int pending) {
        this.pending = pending;
    }

    public int getPendingUpgrade() {
        return pendingUpgrade;
    }

    public void setPendingUpgrade(int pendingUpgrade) {
        this.pendingUpgrade = pendingUpgrade;
    }

    public int getWithRetest() {
        return withRetest;
    }

    public void setWithRetest(int withRetest) {
        this.withRetest = withRetest;
    }

    public int getProblemFreeze() {
        return problemFreeze;
    }

    public void setProblemFreeze(int problemFreeze) {
        this.problemFreeze = problemFreeze;
    }
}
