package com.cmpay.lemon.monitor.dto;

import java.util.List;

public class DemandTestStatusRspDTO {

    private List<DemandTestStatusDTO> demandTestStatusDTOList;
    private List<DefectProcesStatusDTO>  defectProcesStatusDTOList;

    public List<DemandTestStatusDTO> getDemandTestStatusDTOList() {
        return demandTestStatusDTOList;
    }

    public void setDemandTestStatusDTOList(List<DemandTestStatusDTO> demandTestStatusDTOList) {
        this.demandTestStatusDTOList = demandTestStatusDTOList;
    }

    public List<DefectProcesStatusDTO> getDefectProcesStatusDTOList() {
        return defectProcesStatusDTOList;
    }

    public void setDefectProcesStatusDTOList(List<DefectProcesStatusDTO> defectProcesStatusDTOList) {
        this.defectProcesStatusDTOList = defectProcesStatusDTOList;
    }
}
