package com.cmpay.lemon.monitor.bo;
/**
 * @author ty
 */
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
                "\"proPkgName\":"+"\""+this.getProPkgName()+"\""+",\n" +
                "\"proNumber\":"+"\""+this.getProNumber()+"\""+",\n" +
                "\"env\": "+"\""+this.getEnv()+"\""+"\n" +
                "}\n";
    }

    public String getTest(){
        return "{\n" +
                "\t\"proNumber\":\"autotouchantest_yutouchan\",\n" +
                "\t\"status\":\"1\",\n" +
                "\t\"env\":\"0\",\n" +
                "\t\"remark\":\"其他\"\n" +
                "}";
    }

    @Override
    public String toString() {
        return "AutomatedProductionBO{" +
                "env='" + env + '\'' +
                ", proNumber='" + proNumber + '\'' +
                ", proPkgName='" + proPkgName + '\'' +
                '}';
    }
}
