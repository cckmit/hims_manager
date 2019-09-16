package com.cmpay.lemon.monitor.controller.reportForm;


import com.cmpay.framework.data.request.GenericDTO;
import com.cmpay.framework.data.response.GenericRspDTO;
import com.cmpay.lemon.framework.data.NoBody;
import com.cmpay.lemon.monitor.bo.ReqDataCountBO;
import com.cmpay.lemon.monitor.constant.MonitorConstants;
import com.cmpay.lemon.monitor.dto.DemandReqDTO;
import com.cmpay.lemon.monitor.dto.ReqDataCountReqDTO;
import com.cmpay.lemon.monitor.dto.ReqDataCountRspDTO;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.reportForm.ReqDataCountService;
import com.cmpay.lemon.monitor.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = MonitorConstants.REPORTFORM_PATH)
    public class reportFormController {

    @Autowired
    private ReqDataCountService reqDataCountService;

    @RequestMapping("/reportform1")
    public GenericRspDTO<ReqDataCountRspDTO> getReportForm1(@RequestBody ReqDataCountReqDTO reqDataCountReqDTO) {
        reqDataCountReqDTO.setReqReportNm("1");
        String month = DateUtil.date2String(new Date(), "yyyy-MM");
        System.err.println(month);
       System.err.println(1);
        System.err.println(reqDataCountReqDTO.getReqImplMon());
        System.err.println(2);
        if(reqDataCountReqDTO.getReqImplMon()==null||reqDataCountReqDTO.getReqImplMon().equals("")){
            reqDataCountReqDTO.setReqImplMon(month);
        }
        System.err.println(reqDataCountReqDTO.getReqImplMon());
        List<ReqDataCountBO> reportLista = new ArrayList<>();
        List<ReqDataCountBO> reportListb = new ArrayList<>();
        if ("1".equals(reqDataCountReqDTO.getReqReportNm())) {
            reportLista = reqDataCountService.getImpl(reqDataCountReqDTO.getReqImplMon());
            reportListb = reqDataCountService.getImplByDept(reqDataCountReqDTO.getReqImplMon());
        }else if ("2".equals(reqDataCountReqDTO.getReqReportNm())) {
            reportLista = reqDataCountService.getComp(reqDataCountReqDTO.getReqImplMon());
            reportListb = reqDataCountService.getCompByDept(reqDataCountReqDTO.getReqImplMon());
        }else if ("3".equals(reqDataCountReqDTO.getReqReportNm())) {
            reportLista = reqDataCountService.getReqSts(reqDataCountReqDTO.getReqImplMon());
        }else if ("4".equals(reqDataCountReqDTO.getReqReportNm())) {
            reportLista = reqDataCountService.getStageByJd(reqDataCountReqDTO.getReqImplMon());
        }
        System.err.println(reportLista);
        System.err.println(reportListb);
        ReqDataCountRspDTO reqDataCountRspDTO = new ReqDataCountRspDTO();
        reqDataCountRspDTO.setReportLista(reportLista);
        reqDataCountRspDTO.setReportListb(reportListb);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, reqDataCountRspDTO);
    }

    @RequestMapping("/reportform2")
    public GenericRspDTO<ReqDataCountRspDTO> getReportForm2(@RequestBody ReqDataCountReqDTO reqDataCountReqDTO) {
        reqDataCountReqDTO.setReqReportNm("2");
        String month = DateUtil.date2String(new Date(), "yyyy-MM");
        System.err.println(month);
        System.err.println(1);
        System.err.println(reqDataCountReqDTO.getReqImplMon());
        System.err.println(2);
        if(reqDataCountReqDTO.getReqImplMon()==null||reqDataCountReqDTO.getReqImplMon().equals("")){
            reqDataCountReqDTO.setReqImplMon(month);
        }
        System.err.println(reqDataCountReqDTO.getReqImplMon());
        List<ReqDataCountBO> reportLista = new ArrayList<>();
        List<ReqDataCountBO> reportListb = new ArrayList<>();
        if ("1".equals(reqDataCountReqDTO.getReqReportNm())) {
            reportLista = reqDataCountService.getImpl(reqDataCountReqDTO.getReqImplMon());
            reportListb = reqDataCountService.getImplByDept(reqDataCountReqDTO.getReqImplMon());
        }else if ("2".equals(reqDataCountReqDTO.getReqReportNm())) {
            reportLista = reqDataCountService.getComp(reqDataCountReqDTO.getReqImplMon());
            reportListb = reqDataCountService.getCompByDept(reqDataCountReqDTO.getReqImplMon());
        }else if ("3".equals(reqDataCountReqDTO.getReqReportNm())) {
            reportLista = reqDataCountService.getReqSts(reqDataCountReqDTO.getReqImplMon());
        }else if ("4".equals(reqDataCountReqDTO.getReqReportNm())) {
            reportLista = reqDataCountService.getStageByJd(reqDataCountReqDTO.getReqImplMon());
        }
        System.err.println(reportLista);
        System.err.println(reportListb);
        ReqDataCountRspDTO reqDataCountRspDTO = new ReqDataCountRspDTO();
        reqDataCountRspDTO.setReportLista(reportLista);
        reqDataCountRspDTO.setReportListb(reportListb);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, reqDataCountRspDTO);
    }

    @RequestMapping("/reportform3")
    public GenericRspDTO<ReqDataCountRspDTO> getReportForm3(@RequestBody ReqDataCountReqDTO reqDataCountReqDTO) {
        reqDataCountReqDTO.setReqReportNm("3");
        String month = DateUtil.date2String(new Date(), "yyyy-MM");
        System.err.println(month);
        System.err.println(1);
        System.err.println(reqDataCountReqDTO.getReqImplMon());
        System.err.println(2);
        if(reqDataCountReqDTO.getReqImplMon()==null||reqDataCountReqDTO.getReqImplMon().equals("")){
            reqDataCountReqDTO.setReqImplMon(month);
        }
        System.err.println(reqDataCountReqDTO.getReqImplMon());
        List<ReqDataCountBO> reportLista = new ArrayList<>();
        List<ReqDataCountBO> reportListb = new ArrayList<>();
        if ("1".equals(reqDataCountReqDTO.getReqReportNm())) {
            reportLista = reqDataCountService.getImpl(reqDataCountReqDTO.getReqImplMon());
            reportListb = reqDataCountService.getImplByDept(reqDataCountReqDTO.getReqImplMon());
        }else if ("2".equals(reqDataCountReqDTO.getReqReportNm())) {
            reportLista = reqDataCountService.getComp(reqDataCountReqDTO.getReqImplMon());
            reportListb = reqDataCountService.getCompByDept(reqDataCountReqDTO.getReqImplMon());
        }else if ("3".equals(reqDataCountReqDTO.getReqReportNm())) {
            reportLista = reqDataCountService.getReqSts(reqDataCountReqDTO.getReqImplMon());
        }else if ("4".equals(reqDataCountReqDTO.getReqReportNm())) {
            reportLista = reqDataCountService.getStageByJd(reqDataCountReqDTO.getReqImplMon());
        }
        System.err.println(reportLista);
        System.err.println(reportListb);
        ReqDataCountRspDTO reqDataCountRspDTO = new ReqDataCountRspDTO();
        reqDataCountRspDTO.setReportLista(reportLista);
        reqDataCountRspDTO.setReportListb(reportListb);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, reqDataCountRspDTO);
    }

    @RequestMapping("/reportform4")
    public GenericRspDTO<ReqDataCountRspDTO> getReportForm4(@RequestBody ReqDataCountReqDTO reqDataCountReqDTO) {
        reqDataCountReqDTO.setReqReportNm("4");
        String month = DateUtil.date2String(new Date(), "yyyy-MM");
        System.err.println(month);
        System.err.println(1);
        System.err.println(reqDataCountReqDTO.getReqImplMon());
        System.err.println(2);
        if(reqDataCountReqDTO.getReqImplMon()==null||reqDataCountReqDTO.getReqImplMon().equals("")){
            reqDataCountReqDTO.setReqImplMon(month);
        }
        System.err.println(reqDataCountReqDTO.getReqImplMon());
        List<ReqDataCountBO> reportLista = new ArrayList<>();
        List<ReqDataCountBO> reportListb = new ArrayList<>();
        if ("1".equals(reqDataCountReqDTO.getReqReportNm())) {
            reportLista = reqDataCountService.getImpl(reqDataCountReqDTO.getReqImplMon());
            reportListb = reqDataCountService.getImplByDept(reqDataCountReqDTO.getReqImplMon());
        }else if ("2".equals(reqDataCountReqDTO.getReqReportNm())) {
            reportLista = reqDataCountService.getComp(reqDataCountReqDTO.getReqImplMon());
            reportListb = reqDataCountService.getCompByDept(reqDataCountReqDTO.getReqImplMon());
        }else if ("3".equals(reqDataCountReqDTO.getReqReportNm())) {
            reportLista = reqDataCountService.getReqSts(reqDataCountReqDTO.getReqImplMon());
        }else if ("4".equals(reqDataCountReqDTO.getReqReportNm())) {
            reportLista = reqDataCountService.getStageByJd(reqDataCountReqDTO.getReqImplMon());
        }
        System.err.println(reportLista);
        System.err.println(reportListb);
        ReqDataCountRspDTO reqDataCountRspDTO = new ReqDataCountRspDTO();
        reqDataCountRspDTO.setReportLista(reportLista);
        reqDataCountRspDTO.setReportListb(reportListb);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, reqDataCountRspDTO);
    }

}
