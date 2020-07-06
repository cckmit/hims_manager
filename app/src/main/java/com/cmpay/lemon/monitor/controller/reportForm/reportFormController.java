package com.cmpay.lemon.monitor.controller.reportForm;


import com.cmpay.framework.data.response.GenericRspDTO;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.common.utils.JudgeUtils;
import com.cmpay.lemon.common.utils.StringUtils;
import com.cmpay.lemon.framework.data.NoBody;
import com.cmpay.lemon.monitor.bo.*;
import com.cmpay.lemon.monitor.constant.MonitorConstants;
import com.cmpay.lemon.monitor.dto.*;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.reportForm.ReqDataCountService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
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
    // 投产录入不及时报表
    @RequestMapping("/reportform5")
    public GenericRspDTO<ScheduleRspDTO> getReportForm5(@RequestBody ReqDataCountReqDTO reqDataCountReqDTO) {
        String month = DateUtil.date2String(new Date(), "yyyy-MM");
        if(reqDataCountReqDTO.getReqImplMon()==null||reqDataCountReqDTO.getReqImplMon().equals("")){
            reqDataCountReqDTO.setReqImplMon(month);
        }
        List<ScheduleBO> reportLista = new ArrayList<>();
        reportLista = reqDataCountService.getProduction(reqDataCountReqDTO.getReqImplMon());
        ScheduleRspDTO reqDataCountRspDTO = new ScheduleRspDTO();
        List<ScheduleDTO> reqDataCountDTOListA = new LinkedList<>();
        reportLista.forEach(m->
                reqDataCountDTOListA.add(BeanUtils.copyPropertiesReturnDest(new ScheduleDTO(), m))
        );
        reqDataCountRspDTO.setScheduleDTOList(reqDataCountDTOListA);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, reqDataCountRspDTO);
    }
    // 需求文档上传情况报表
    @RequestMapping("/reportform6")
    public GenericRspDTO<DemandRspDTO> getReportForm6(@RequestBody ReqDataCountReqDTO reqDataCountReqDTO) {
        System.err.println(reqDataCountReqDTO);
        String month = DateUtil.date2String(new Date(), "yyyy-MM");
        if(reqDataCountReqDTO.getReqImplMon()==null||reqDataCountReqDTO.getReqImplMon().equals("")){
            reqDataCountReqDTO.setReqImplMon(month);
        }
        List<DemandBO> reportLista = new ArrayList<>();
        reportLista = reqDataCountService.getReportForm6(reqDataCountReqDTO.getReqImplMon(),reqDataCountReqDTO.getDevpLeadDept(),reqDataCountReqDTO.getProductMng(),reqDataCountReqDTO.getFirstLevelOrganization());
        DemandRspDTO reqDataCountRspDTO = new DemandRspDTO();
        List<DemandDTO> reqDataCountDTOListA = new LinkedList<>();
        reportLista.forEach(m->
                reqDataCountDTOListA.add(BeanUtils.copyPropertiesReturnDest(new DemandDTO(), m))
        );
        reqDataCountRspDTO.setDemandDTOList(reqDataCountDTOListA);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, reqDataCountRspDTO);
    }

    //部门员工工时
    @RequestMapping("/reportform7")
    public GenericRspDTO<WorkingHoursRspDTO> getReportForm7(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
        /*String selectTime = DateUtil.date2String(new Date(), "yyyy-MM-dd");
        if(workingHoursDTO.getSelectTime()==null||workingHoursDTO.getSelectTime().equals("")){
            workingHoursDTO.setSelectTime(selectTime);
        }*/
        List<WorkingHoursBO> reportLista = new ArrayList<>();
        reportLista = reqDataCountService.getReportForm7(workingHoursDTO.getDevpLeadDept(),workingHoursDTO.getSelectTime(),workingHoursDTO.getSelectTime1(),workingHoursDTO.getSelectTime2());
        WorkingHoursRspDTO reqDataCountRspDTO = new WorkingHoursRspDTO();
        List<WorkingHoursDTO> reqDataCountDTOListA = new LinkedList<>();

        reportLista.forEach(m->
                reqDataCountDTOListA.add(BeanUtils.copyPropertiesReturnDest(new WorkingHoursDTO(), m))
        );
        reqDataCountRspDTO.setVpnInfoDTOS(reqDataCountDTOListA);
        List<WorkingHoursBO> reportListb = new ArrayList<>();
        reportListb = reqDataCountService.getReportForm7B(workingHoursDTO.getDevpLeadDept(),workingHoursDTO.getSelectTime(),workingHoursDTO.getSelectTime1(),workingHoursDTO.getSelectTime2());
        List<WorkingHoursDTO> reqDataCountDTOListB = new LinkedList<>();
        reportListb.forEach(m->
                reqDataCountDTOListB.add(BeanUtils.copyPropertiesReturnDest(new WorkingHoursDTO(), m))
        );
        reqDataCountRspDTO.setVpnInfoBDTOS(reqDataCountDTOListB);
        // 统计部门
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, reqDataCountRspDTO);
    }
    //部门员工工时
    @RequestMapping("/reportform8")
    public GenericRspDTO<WorkingHoursRspDTO> getReportForm8(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
        String selectTime = DateUtil.date2String(new Date(), "yyyy-MM-dd");
        if(workingHoursDTO.getSelectTime()==null||workingHoursDTO.getSelectTime().equals("")){
            workingHoursDTO.setSelectTime(selectTime);
        }
        List<String> reportLista = new ArrayList<>();
        reportLista = reqDataCountService.getReportForm8(workingHoursDTO.getSelectTime());
        WorkingHoursRspDTO reqDataCountRspDTO = new WorkingHoursRspDTO();
        reqDataCountRspDTO.setStringList(reportLista);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, reqDataCountRspDTO);
    }

    /**
     * 根据epic获取需求员工的工时
     * @param workingHoursDTO
     * @return
     */
    @RequestMapping("/findEpicKeyHours")
    public GenericRspDTO<WorkingHoursRspDTO> findEpicKeyHours(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
        List<WorkingHoursBO> reportLista = new ArrayList<>();
        reportLista = reqDataCountService.findEpicKeyHours(workingHoursDTO.getEpickey());
        WorkingHoursRspDTO reqDataCountRspDTO = new WorkingHoursRspDTO();
        List<WorkingHoursDTO> reqDataCountDTOListA = new LinkedList<>();

        reportLista.forEach(m->
                reqDataCountDTOListA.add(BeanUtils.copyPropertiesReturnDest(new WorkingHoursDTO(), m))
        );
        System.err.println(reqDataCountDTOListA);
        reqDataCountRspDTO.setVpnInfoDTOS(reqDataCountDTOListA);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, reqDataCountRspDTO);
    }

    /**
     * 根据epic获取需求质量情况
     * @param workingHoursDTO
     * @return
     */
    @RequestMapping("/findDemandQuality")
    public GenericRspDTO<DemandQualityDTO> findDemandQuality(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
        String epic = workingHoursDTO.getEpickey();
        DemandQualityBO demandQualityBO = new DemandQualityBO();
        demandQualityBO = reqDataCountService.findDemandQuality(epic);
        DemandQualityDTO demandQualityDTO = new DemandQualityDTO();
        BeanUtils.copyPropertiesReturnDest(demandQualityDTO, demandQualityBO);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandQualityDTO);
    }

    /**
     * 查询需求所消耗工时
     * @param workingHoursDTO
     * @return
     */
    @RequestMapping("/getDemandHours")
    public GenericRspDTO<DemandHoursRspDTO> getDemandHours(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
        DemandHoursRspBO reportLista = new DemandHoursRspBO();
        reportLista = reqDataCountService.getDemandHours(workingHoursDTO.getEpickey());
        DemandHoursRspDTO reqDataCountRspDTO = new DemandHoursRspDTO();
        BeanUtils.copyPropertiesReturnDest(reqDataCountRspDTO, reportLista);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, reqDataCountRspDTO);
    }

    /**
     * 查询需求功能点
     * @param workingHoursDTO
     * @return
     */
    @RequestMapping("/getWorkLoad")
    public GenericRspDTO<DemandHoursRspDTO> getWorkLoad(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
        DemandHoursRspBO reportLista = new DemandHoursRspBO();
        reportLista = reqDataCountService.getWorkLoad(workingHoursDTO.getEpickey());
        DemandHoursRspDTO reqDataCountRspDTO = new DemandHoursRspDTO();
        BeanUtils.copyPropertiesReturnDest(reqDataCountRspDTO, reportLista);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, reqDataCountRspDTO);
    }

    /**
     * 根据epic获取缺陷类型数
     * @param workingHoursDTO
     * @return
     */
    @RequestMapping("/getFlawNumber")
    public GenericRspDTO<DemandHoursRspDTO> getFlawNumber(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
        DemandHoursRspBO reportLista = new DemandHoursRspBO();
        reportLista = reqDataCountService.getFlawNumber(workingHoursDTO.getEpickey());
        DemandHoursRspDTO reqDataCountRspDTO = new DemandHoursRspDTO();
        BeanUtils.copyPropertiesReturnDest(reqDataCountRspDTO, reportLista);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, reqDataCountRspDTO);
    }
    /**
     * 根据epic获取评审问题类型数
     * @param workingHoursDTO
     * @return
     */
    @RequestMapping("/getReviewNumber")
    public GenericRspDTO<DemandHoursRspDTO> getReviewNumber(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
        DemandHoursRspBO reportLista = new DemandHoursRspBO();
        reportLista = reqDataCountService.getReviewNumber(workingHoursDTO.getEpickey());
        DemandHoursRspDTO reqDataCountRspDTO = new DemandHoursRspDTO();
        BeanUtils.copyPropertiesReturnDest(reqDataCountRspDTO, reportLista);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, reqDataCountRspDTO);
    }


    // 查询某人某天的具体工时
    @RequestMapping("/findList")
    public GenericRspDTO<WorkingHoursRspDTO> findList(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
        List<WorkingHoursBO> reportLista = new ArrayList<>();
        reportLista = reqDataCountService.findList(workingHoursDTO.getDisplayname(),workingHoursDTO.getSelectTime(),workingHoursDTO.getSelectTime1(),workingHoursDTO.getSelectTime2());
        WorkingHoursRspDTO reqDataCountRspDTO = new WorkingHoursRspDTO();
        List<WorkingHoursDTO> reqDataCountDTOListA = new LinkedList<>();
        reportLista.forEach(m->
                reqDataCountDTOListA.add(BeanUtils.copyPropertiesReturnDest(new WorkingHoursDTO(), m))
        );
        reqDataCountRspDTO.setVpnInfoDTOS(reqDataCountDTOListA);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, reqDataCountRspDTO);
    }

    /**
     * 查询个人体检视图
     * @param workingHoursDTO
     * @return
     */
    @RequestMapping("/reportform11")
    public GenericRspDTO<WorkingHoursDTO> getReportForm11(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
        WorkingHoursBO reportLista = new WorkingHoursBO();
        System.err.println(workingHoursDTO.getDisplayname());
        reportLista = reqDataCountService.getReportForm11(workingHoursDTO.getDisplayname(),workingHoursDTO.getSelectTime1(),workingHoursDTO.getSelectTime2());
        WorkingHoursDTO reqDataCountRspDTO = new WorkingHoursDTO();
        BeanUtils.copyPropertiesReturnDest(reqDataCountRspDTO, reportLista);
        System.err.println(reqDataCountRspDTO);
        // 统计部门
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, reqDataCountRspDTO);
    }

    /**
     * 查询个人体检视图
     * @param workingHoursDTO
     * @return
     */
    @RequestMapping("/getDemandStaffView")
    public GenericRspDTO<WorkingHoursRspDTO> getDemandStaffView(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
        List<WorkingHoursBO> reportLista = new ArrayList<>();
        reportLista = reqDataCountService.getDemandStaffView(workingHoursDTO.getDisplayname(),workingHoursDTO.getSelectTime1(),workingHoursDTO.getSelectTime2());
        WorkingHoursRspDTO reqDataCountRspDTO = new WorkingHoursRspDTO();
        List<WorkingHoursDTO> reqDataCountDTOListA = new LinkedList<>();
        reportLista.forEach(m->
                reqDataCountDTOListA.add(BeanUtils.copyPropertiesReturnDest(new WorkingHoursDTO(), m))
        );
        reqDataCountRspDTO.setVpnInfoDTOS(reqDataCountDTOListA);
        // 统计部门
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, reqDataCountRspDTO);
    }

    /**
     * 个人视图，需求任务与其它任务工时饼图
     * @param workingHoursDTO
     * @return
     */
    @RequestMapping("/getDemandStaffTask")
    public GenericRspDTO<DemandHoursRspDTO> getDemandStaffTask(@RequestBody WorkingHoursReqDTO workingHoursDTO)  {
        DemandHoursRspDTO demandHoursRspDTO = new DemandHoursRspDTO();
        DemandHoursRspBO reportLista = new DemandHoursRspBO();
        reportLista = reqDataCountService.getDemandStaffTask(workingHoursDTO.getDisplayname(),workingHoursDTO.getSelectTime1(),workingHoursDTO.getSelectTime2());
        BeanUtils.copyPropertiesReturnDest(demandHoursRspDTO, reportLista);
        System.err.println("demandHoursRspDTO=="+demandHoursRspDTO);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
    }

    /**
     * 获取个人体检视图的缺陷情况
     * @param workingHoursDTO
     * @return
     */
    @RequestMapping("/getDemandDefectDetails")
    public GenericRspDTO<DefectDetailsRspDTO> getDemandDefectDetails(@RequestBody WorkingHoursReqDTO workingHoursDTO)  {
        DefectDetailsRspDTO demandHoursRspDTO = new DefectDetailsRspDTO();
        DefectDetailsRspBO reportLista = new DefectDetailsRspBO();
        reportLista = reqDataCountService.getDemandDefectDetails(workingHoursDTO.getDisplayname(),workingHoursDTO.getSelectTime1(),workingHoursDTO.getSelectTime2());
        demandHoursRspDTO.setDefectDetailsDTOArrayList(BeanConvertUtils.convertList(reportLista.getDefectDetailsBos(), DefectDetailsDTO.class));
        System.err.println("demandHoursRspDTO=="+demandHoursRspDTO);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
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
    @RequestMapping("/downloadreportform21")
    public  GenericRspDTO<NoBody> downloadReportForm21(@RequestBody ReqDataCountReqDTO reqDataCountReqDTO ,HttpServletResponse response) {
        String month = DateUtil.date2String(new Date(), "yyyy-MM");
        if(reqDataCountReqDTO.getReqImplMon()==null||reqDataCountReqDTO.getReqImplMon().equals("")){
            reqDataCountReqDTO.setReqImplMon(month);
        }
        reqDataCountService.downloadDemandCompletionReport2(reqDataCountReqDTO.getReqImplMon(),response);
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
    @RequestMapping("/downloadreportform5")
    public  GenericRspDTO<NoBody> downloadReportForm5(@RequestBody ReqDataCountReqDTO reqDataCountReqDTO ,HttpServletResponse response) {
        String month = DateUtil.date2String(new Date(), "yyyy-MM");
        if(reqDataCountReqDTO.getReqImplMon()==null||reqDataCountReqDTO.getReqImplMon().equals("")){
            reqDataCountReqDTO.setReqImplMon(month);
        }
        reqDataCountService.downloadProductionTypeStatistics(reqDataCountReqDTO.getReqImplMon(),response);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS);
    }
    @RequestMapping("/downloadreportform6")
    public  GenericRspDTO<NoBody> downloadReportForm6(@RequestBody ReqDataCountReqDTO reqDataCountReqDTO ,HttpServletResponse response) {
        String month = DateUtil.date2String(new Date(), "yyyy-MM");
        if(reqDataCountReqDTO.getReqImplMon()==null||reqDataCountReqDTO.getReqImplMon().equals("")){
            reqDataCountReqDTO.setReqImplMon(month);
        }
        reqDataCountService.downloadDemandUploadDocumentBO(reqDataCountReqDTO.getReqImplMon(),reqDataCountReqDTO.getDevpLeadDept(),reqDataCountReqDTO.getProductMng(),reqDataCountReqDTO.getFirstLevelOrganization(),response);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS);
    }


}
