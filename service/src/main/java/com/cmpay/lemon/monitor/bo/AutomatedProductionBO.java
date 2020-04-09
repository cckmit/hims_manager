package com.cmpay.lemon.monitor.bo;

public class AutomatedProductionBO {
    String env;
    String proNumber;
    String proPkgName;

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getProNumber() {
        return proNumber;
    }

    public void setProNumber(String proNumber) {
        this.proNumber = proNumber;
    }

    public String getProPkgName() {
        return proPkgName;
    }

    public void setProPkgName(String proPkgName) {
        this.proPkgName = proPkgName;
    }

    public String getJson(){
        return "{\n" +
                "name:"+this.getProPkgName()+",\n" +
                "number:"+this.getProNumber()+",\n" +
                "env: "+this.getEnv()+"\n" +
                "}\n";
    }
}
