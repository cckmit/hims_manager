/*
 * @ClassName DefectDetailsDO
 * @Description
 * @version 1.0
 * @Date 2020-06-30 16:04:29
 */
package com.cmpay.lemon.monitor.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wlr 缺陷问题
 */
public class DefectDetailsRspDTO {
    private List<DefectDetailsDTO> defectDetailsDTOArrayList ;

    public List<DefectDetailsDTO> getDefectDetailsDTOArrayList() {
        return defectDetailsDTOArrayList;
    }

    public void setDefectDetailsDTOArrayList(List<DefectDetailsDTO> defectDetailsDTOArrayList) {
        this.defectDetailsDTOArrayList = defectDetailsDTOArrayList;
    }

    @Override
    public String toString() {
        return "DefectDetailsRspDTO{" +
                "defectDetailsDTOArrayList=" + defectDetailsDTOArrayList +
                '}';
    }
}
