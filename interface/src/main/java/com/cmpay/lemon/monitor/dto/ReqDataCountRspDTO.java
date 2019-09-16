package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.request.GenericDTO;
import com.cmpay.lemon.monitor.bo.ReqDataCountBO;

import java.util.List;

/**
 * @author: wuliangtui
 * @Description:  需求报表返回DTO
 */
public class ReqDataCountRspDTO extends GenericDTO {
    private List<ReqDataCountBO> reportLista;
    private List<ReqDataCountBO> reportListb;

    public List<ReqDataCountBO> getReportLista() {
        return reportLista;
    }

    public void setReportLista(List<ReqDataCountBO> reportLista) {
        this.reportLista = reportLista;
    }

    public List<ReqDataCountBO> getReportListb() {
        return reportListb;
    }

    public void setReportListb(List<ReqDataCountBO> reportListb) {
        this.reportListb = reportListb;
    }
}
