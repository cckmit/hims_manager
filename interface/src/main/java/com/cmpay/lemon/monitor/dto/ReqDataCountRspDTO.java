package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.request.GenericDTO;


import java.util.List;

/**
 * @author: wuliangtui
 * @Description:  需求报表返回DTO
 */
public class ReqDataCountRspDTO extends GenericDTO {
    private List<ReqDataCountDTO> reportLista;
    private List<ReqDataCountDTO> reportListb;

    public List<ReqDataCountDTO> getReportLista() {
        return reportLista;
    }

    public void setReportLista(List<ReqDataCountDTO> reportLista) {
        this.reportLista = reportLista;
    }

    public List<ReqDataCountDTO> getReportListb() {
        return reportListb;
    }

    public void setReportListb(List<ReqDataCountDTO> reportListb) {
        this.reportListb = reportListb;
    }
}
