/*
 * @ClassName DemandChangeDetailsDO
 * @Description
 * @version 1.0
 * @Date 2019-11-13 10:55:00
 */
package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.response.PageableRspDTO;

import java.util.List;


public class DefectMonthlyRspDTO extends PageableRspDTO {
    private List<DefectMonthlyDTO> defectMonthlyDTOList ;

    public List<DefectMonthlyDTO> getDefectMonthlyDTOList() {
        return defectMonthlyDTOList;
    }

    public void setDefectMonthlyDTOList(List<DefectMonthlyDTO> defectMonthlyDTOList) {
        this.defectMonthlyDTOList = defectMonthlyDTOList;
    }
}