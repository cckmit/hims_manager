package com.cmpay.lemon.monitor.controller.reportForm;


import com.cmpay.framework.data.response.GenericRspDTO;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.framework.data.NoBody;
import com.cmpay.lemon.monitor.bo.ReqDataCountBO;
import com.cmpay.lemon.monitor.constant.MonitorConstants;
import com.cmpay.lemon.monitor.dto.ReqDataCountDTO;
import com.cmpay.lemon.monitor.dto.ReqDataCountReqDTO;
import com.cmpay.lemon.monitor.dto.ReqDataCountRspDTO;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.reportForm.ReqDataCountService;
import com.cmpay.lemon.monitor.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping(value = MonitorConstants.REPORTFORM_PATH)
    public class reportFormController {

    @Autowired
    private ReqDataCountService reqDataCountService;

    @RequestMapping("/reportform1")
    public GenericRspDTO<ReqDataCountRspDTO> getReportForm1(@RequestBody ReqDataCountReqDTO reqDataCountReqDTO) {
        String month = DateUtil.date2String(new Date(), "yyyy-MM");
        if(reqDataCountReqDTO.getReqImplMon()==null||reqDataCountReqDTO.getReqImplMon().equals("")){
            reqDataCountReqDTO.setReqImplMon(month);
        }
        List<ReqDataCountBO> reportLista = new ArrayList<>();
        List<ReqDataCountBO> reportListb = new ArrayList<>();
            reportLista = reqDataCountService.getImpl(reqDataCountReqDTO.getReqImplMon());
            reportListb = reqDataCountService.getImplByDept(reqDataCountReqDTO.getReqImplMon());
        ReqDataCountRspDTO reqDataCountRspDTO = new ReqDataCountRspDTO();

        List<ReqDataCountDTO> reqDataCountDTOListA = new LinkedList<>();
        reportLista.forEach(m->
                reqDataCountDTOListA.add(BeanUtils.copyPropertiesReturnDest(new ReqDataCountDTO(), m))
        );
        List<ReqDataCountDTO> reqDataCountDTOListB = new LinkedList<>();
        reportListb.forEach(m->
                reqDataCountDTOListB.add(BeanUtils.copyPropertiesReturnDest(new ReqDataCountDTO(), m))
        );
        reqDataCountRspDTO.setReportLista(reqDataCountDTOListA);
        reqDataCountRspDTO.setReportListb(reqDataCountDTOListB);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, reqDataCountRspDTO);
    }

    @RequestMapping("/reportform2")
    public GenericRspDTO<ReqDataCountRspDTO> getReportForm2(@RequestBody ReqDataCountReqDTO reqDataCountReqDTO) {
        String month = DateUtil.date2String(new Date(), "yyyy-MM");
        if(reqDataCountReqDTO.getReqImplMon()==null||reqDataCountReqDTO.getReqImplMon().equals("")){
            reqDataCountReqDTO.setReqImplMon(month);
        }
        List<ReqDataCountBO> reportLista = new ArrayList<>();
        List<ReqDataCountBO> reportListb = new ArrayList<>();
            reportLista = reqDataCountService.getComp(reqDataCountReqDTO.getReqImplMon());
            reportListb = reqDataCountService.getCompByDept(reqDataCountReqDTO.getReqImplMon());
        ReqDataCountRspDTO reqDataCountRspDTO = new ReqDataCountRspDTO();
        List<ReqDataCountDTO> reqDataCountDTOListA = new LinkedList<>();
        reportLista.forEach(m->{
                reqDataCountDTOListA.add(BeanUtils.copyPropertiesReturnDest(new ReqDataCountDTO(), m));
        }
        );
        List<ReqDataCountDTO> reqDataCountDTOListB = new LinkedList<>();
        reportListb.forEach(m->
                reqDataCountDTOListB.add(BeanUtils.copyPropertiesReturnDest(new ReqDataCountDTO(), m))
        );
        reqDataCountRspDTO.setReportLista(reqDataCountDTOListA);
        reqDataCountRspDTO.setReportListb(reqDataCountDTOListB);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, reqDataCountRspDTO);
    }

    @RequestMapping("/reportform3")
    public GenericRspDTO<ReqDataCountRspDTO> getReportForm3(@RequestBody ReqDataCountReqDTO reqDataCountReqDTO) {
        String month = DateUtil.date2String(new Date(), "yyyy-MM");
        if(reqDataCountReqDTO.getReqImplMon()==null||reqDataCountReqDTO.getReqImplMon().equals("")){
            reqDataCountReqDTO.setReqImplMon(month);
        }
        List<ReqDataCountBO> reportLista = new ArrayList<>();
        reportLista = reqDataCountService.getReqSts(reqDataCountReqDTO.getReqImplMon());
        ReqDataCountRspDTO reqDataCountRspDTO = new ReqDataCountRspDTO();
        List<ReqDataCountDTO> reqDataCountDTOListA = new LinkedList<>();
        reportLista.forEach(m->
                reqDataCountDTOListA.add(BeanUtils.copyPropertiesReturnDest(new ReqDataCountDTO(), m))
        );
        reqDataCountRspDTO.setReportLista(reqDataCountDTOListA);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, reqDataCountRspDTO);
    }


    @RequestMapping("/reportform4")
    public GenericRspDTO<ReqDataCountRspDTO> getReportForm4(@RequestBody ReqDataCountReqDTO reqDataCountReqDTO) {
        String month = DateUtil.date2String(new Date(), "yyyy-MM");
        if(reqDataCountReqDTO.getReqImplMon()==null||reqDataCountReqDTO.getReqImplMon().equals("")){
            reqDataCountReqDTO.setReqImplMon(month);
        }
        List<ReqDataCountBO> reportLista = new ArrayList<>();
        reportLista = reqDataCountService.getStageByJd(reqDataCountReqDTO.getReqImplMon());
        ReqDataCountRspDTO reqDataCountRspDTO = new ReqDataCountRspDTO();
        List<ReqDataCountDTO> reqDataCountDTOListA = new LinkedList<>();
        reportLista.forEach(m->
                reqDataCountDTOListA.add(BeanUtils.copyPropertiesReturnDest(new ReqDataCountDTO(), m))
        );
        reqDataCountRspDTO.setReportLista(reqDataCountDTOListA);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, reqDataCountRspDTO);
    }


    @RequestMapping("/downloadreportform1")
    public  GenericRspDTO<NoBody> downloadReportForm1(@RequestBody ReqDataCountReqDTO reqDataCountReqDTO ,HttpServletResponse response) {
        String month = DateUtil.date2String(new Date(), "yyyy-MM");
        if(reqDataCountReqDTO.getReqImplMon()==null||reqDataCountReqDTO.getReqImplMon().equals("")){
           reqDataCountReqDTO.setReqImplMon(month);
        }
        reqDataCountService.downloadDemandImplementationReport(reqDataCountReqDTO.getReqImplMon(),response);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS);
    }
    @RequestMapping("/downloadreportform2")
    public  GenericRspDTO<NoBody> downloadReportForm2(@RequestBody ReqDataCountReqDTO reqDataCountReqDTO ,HttpServletResponse response) {
        String month = DateUtil.date2String(new Date(), "yyyy-MM");
        if(reqDataCountReqDTO.getReqImplMon()==null||reqDataCountReqDTO.getReqImplMon().equals("")){
            reqDataCountReqDTO.setReqImplMon(month);
        }
        reqDataCountService.downloadDemandCompletionReport(reqDataCountReqDTO.getReqImplMon(),response);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS);
    }

    @RequestMapping("/downloadreportform3")
    public  GenericRspDTO<NoBody> downloadReportForm3(@RequestBody ReqDataCountReqDTO reqDataCountReqDTO ,HttpServletResponse response) {
        String month = DateUtil.date2String(new Date(), "yyyy-MM");
        if(reqDataCountReqDTO.getReqImplMon()==null||reqDataCountReqDTO.getReqImplMon().equals("")){
            reqDataCountReqDTO.setReqImplMon(month);
        }
        reqDataCountService.downloadDemandTypeStatistics(reqDataCountReqDTO.getReqImplMon(),response);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS);
    }

    @RequestMapping("/downloadreportform4")
    public  GenericRspDTO<NoBody> downloadReportForm4(@RequestBody ReqDataCountReqDTO reqDataCountReqDTO ,HttpServletResponse response) {
        String month = DateUtil.date2String(new Date(), "yyyy-MM");
        if(reqDataCountReqDTO.getReqImplMon()==null||reqDataCountReqDTO.getReqImplMon().equals("")){
            reqDataCountReqDTO.setReqImplMon(month);
        }
        reqDataCountService.downloadBaseOwnershipDepartmentStatistics(reqDataCountReqDTO.getReqImplMon(),response);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS);
    }

}