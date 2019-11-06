package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.request.GenericDTO;
import com.cmpay.framework.data.response.PageableRspDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: wuliangtui
 * @Description:  首页返回DTO
 */
public class ReqIndexCountRspDTO extends PageableRspDTO {
    private Map reMap;
    private List<String> li;
    private List<ReqDataCountDTO> listDetl;
    private String totle;
    private List<String> list;
    private List<DemandDTO> demandDTOList = new ArrayList<>();

    public Map getReMap() {
        return reMap;
    }

    public void setReMap(Map reMap) {
        this.reMap = reMap;
    }

    public List<String> getLi() {
        return li;
    }

    public void setLi(List<String> li) {
        this.li = li;
    }

    public List<ReqDataCountDTO> getListDetl() {
        return listDetl;
    }

    public void setListDetl(List<ReqDataCountDTO> listDetl) {
        this.listDetl = listDetl;
    }

    public String getTotle() {
        return totle;
    }

    public void setTotle(String totle) {
        this.totle = totle;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public List<DemandDTO> getDemandDTOList() {
        return demandDTOList;
    }

    public void setDemandDTOList(List<DemandDTO> demandDTOList) {
        this.demandDTOList = demandDTOList;
    }

    @Override
    public String toString() {
        return "ReqIndexCountRspDTO{" +
                "reMap=" + reMap +
                ", li=" + li +
                ", listDetl=" + listDetl +
                ", totle='" + totle + '\'' +
                ", list=" + list +
                ", demandDTOList=" + demandDTOList +
                '}';
    }
}
