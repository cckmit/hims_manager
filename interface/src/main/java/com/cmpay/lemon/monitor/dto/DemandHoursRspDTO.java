package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.response.PageableRspDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhou_xiong
 */
public class DemandHoursRspDTO extends PageableRspDTO {
    private String sum ;
    private List<String> listSum ;
    private List<String> stringList = new ArrayList<>();
    private List<String> listRate ;

    public List<String> getStringList() {
        return stringList;
    }

    public void setStringList(List<String> stringList) {
        this.stringList = stringList;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public List<String> getListSum() {
        return listSum;
    }

    public void setListSum(List<String> listSum) {
        this.listSum = listSum;
    }

    public List<String> getListRate() {
        return listRate;
    }

    public void setListRate(List<String> listRate) {
        this.listRate = listRate;
    }

    @Override
    public String toString() {
        return "DemandHoursRspDTO{" +
                "sum='" + sum + '\'' +
                ", listSum=" + listSum +
                ", stringList=" + stringList +
                ", listRate=" + listRate +
                '}';
    }
}
