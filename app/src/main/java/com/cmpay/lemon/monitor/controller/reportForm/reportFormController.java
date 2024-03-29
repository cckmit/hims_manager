package com.cmpay.lemon.monitor.controller.reportForm;


import com.cmpay.framework.data.request.GenericDTO;
import com.cmpay.framework.data.response.GenericRspDTO;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.framework.data.NoBody;
import com.cmpay.lemon.monitor.bo.*;
import com.cmpay.lemon.monitor.constant.MonitorConstants;
import com.cmpay.lemon.monitor.dto.*;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.demand.ReqPlanService;
import com.cmpay.lemon.monitor.service.demand.ReqTaskService;
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
	private ReqTaskService reqTaskService;
	@Autowired
	private ReqDataCountService reqDataCountService;
    @Autowired
    private ReqPlanService reqPlanService;

	@RequestMapping("/reportform1")
	public GenericRspDTO<ReqDataCountRspDTO> getReportForm1(@RequestBody ReqDataCountReqDTO reqDataCountReqDTO) {
		String month = DateUtil.date2String(new Date(), "yyyy-MM");
		if (reqDataCountReqDTO.getReqImplMon() == null || reqDataCountReqDTO.getReqImplMon().equals("")) {
			reqDataCountReqDTO.setReqImplMon(month);
		}
		List<ReqDataCountBO> reportLista = new ArrayList<>();
		List<ReqDataCountBO> reportListb = new ArrayList<>();
		reportLista = reqDataCountService.getImpl(reqDataCountReqDTO.getReqImplMon());
		reportListb = reqDataCountService.getImplByDept(reqDataCountReqDTO.getReqImplMon());
		ReqDataCountRspDTO reqDataCountRspDTO = new ReqDataCountRspDTO();

		List<ReqDataCountDTO> reqDataCountDTOListA = new LinkedList<>();
		reportLista.forEach(m ->
				reqDataCountDTOListA.add(BeanUtils.copyPropertiesReturnDest(new ReqDataCountDTO(), m))
		);
		List<ReqDataCountDTO> reqDataCountDTOListB = new LinkedList<>();
		reportListb.forEach(m ->
				reqDataCountDTOListB.add(BeanUtils.copyPropertiesReturnDest(new ReqDataCountDTO(), m))
		);
		reqDataCountRspDTO.setReportLista(reqDataCountDTOListA);
		reqDataCountRspDTO.setReportListb(reqDataCountDTOListB);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, reqDataCountRspDTO);
	}

	@RequestMapping("/reportform2")
	public GenericRspDTO<ReqDataCountRspDTO> getReportForm2(@RequestBody ReqDataCountReqDTO reqDataCountReqDTO) {
		String month = DateUtil.date2String(new Date(), "yyyy-MM");
		if (reqDataCountReqDTO.getReqImplMon() == null || reqDataCountReqDTO.getReqImplMon().equals("")) {
			reqDataCountReqDTO.setReqImplMon(month);
		}
		List<ReqDataCountBO> reportLista = new ArrayList<>();
		List<ReqDataCountBO> reportListb = new ArrayList<>();
		reportLista = reqDataCountService.getComp(reqDataCountReqDTO.getReqImplMon());
		reportListb = reqDataCountService.getCompByDept(reqDataCountReqDTO.getReqImplMon());
		ReqDataCountRspDTO reqDataCountRspDTO = new ReqDataCountRspDTO();
		List<ReqDataCountDTO> reqDataCountDTOListA = new LinkedList<>();
		reportLista.forEach(m -> {
					reqDataCountDTOListA.add(BeanUtils.copyPropertiesReturnDest(new ReqDataCountDTO(), m));
				}
		);
		List<ReqDataCountDTO> reqDataCountDTOListB = new LinkedList<>();
		reportListb.forEach(m ->
				reqDataCountDTOListB.add(BeanUtils.copyPropertiesReturnDest(new ReqDataCountDTO(), m))
		);
		reqDataCountRspDTO.setReportLista(reqDataCountDTOListA);
		reqDataCountRspDTO.setReportListb(reqDataCountDTOListB);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, reqDataCountRspDTO);
	}

	@RequestMapping("/reportform3")
	public GenericRspDTO<ReqDataCountRspDTO> getReportForm3(@RequestBody ReqDataCountReqDTO reqDataCountReqDTO) {
		String month = DateUtil.date2String(new Date(), "yyyy-MM");
		if (reqDataCountReqDTO.getReqImplMon() == null || reqDataCountReqDTO.getReqImplMon().equals("")) {
			reqDataCountReqDTO.setReqImplMon(month);
		}
		List<ReqDataCountBO> reportLista = new ArrayList<>();
		reportLista = reqDataCountService.getReqSts(reqDataCountReqDTO.getReqImplMon());
		ReqDataCountRspDTO reqDataCountRspDTO = new ReqDataCountRspDTO();
		List<ReqDataCountDTO> reqDataCountDTOListA = new LinkedList<>();
		reportLista.forEach(m ->
				reqDataCountDTOListA.add(BeanUtils.copyPropertiesReturnDest(new ReqDataCountDTO(), m))
		);
		reqDataCountRspDTO.setReportLista(reqDataCountDTOListA);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, reqDataCountRspDTO);
	}


	@RequestMapping("/reportform4")
	public GenericRspDTO<ReqDataCountRspDTO> getReportForm4(@RequestBody ReqDataCountReqDTO reqDataCountReqDTO) {
		String month = DateUtil.date2String(new Date(), "yyyy-MM");
		if (reqDataCountReqDTO.getReqImplMon() == null || reqDataCountReqDTO.getReqImplMon().equals("")) {
			reqDataCountReqDTO.setReqImplMon(month);
		}
		List<ReqDataCountBO> reportLista = new ArrayList<>();
		reportLista = reqDataCountService.getStageByJd(reqDataCountReqDTO.getReqImplMon());
		ReqDataCountRspDTO reqDataCountRspDTO = new ReqDataCountRspDTO();
		List<ReqDataCountDTO> reqDataCountDTOListA = new LinkedList<>();
		reportLista.forEach(m ->
				reqDataCountDTOListA.add(BeanUtils.copyPropertiesReturnDest(new ReqDataCountDTO(), m))
		);
		reqDataCountRspDTO.setReportLista(reqDataCountDTOListA);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, reqDataCountRspDTO);
	}

	// 投产录入不及时报表
	@RequestMapping("/reportform5")
	public GenericRspDTO<ScheduleRspDTO> getReportForm5(@RequestBody ReqDataCountReqDTO reqDataCountReqDTO) {
		String month = DateUtil.date2String(new Date(), "yyyy-MM");
		if (reqDataCountReqDTO.getReqImplMon() == null || reqDataCountReqDTO.getReqImplMon().equals("")) {
			reqDataCountReqDTO.setReqImplMon(month);
		}
		List<ScheduleBO> reportLista = new ArrayList<>();
		reportLista = reqDataCountService.getProduction(reqDataCountReqDTO.getReqImplMon());
		ScheduleRspDTO reqDataCountRspDTO = new ScheduleRspDTO();
		List<ScheduleDTO> reqDataCountDTOListA = new LinkedList<>();
		reportLista.forEach(m ->
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
		if (reqDataCountReqDTO.getReqImplMon() == null || reqDataCountReqDTO.getReqImplMon().equals("")) {
			reqDataCountReqDTO.setReqImplMon(month);
		}
		List<DemandBO> reportLista = new ArrayList<>();
		reportLista = reqDataCountService.getReportForm6(reqDataCountReqDTO.getReqImplMon(), reqDataCountReqDTO.getDevpLeadDept(), reqDataCountReqDTO.getProductMng(), reqDataCountReqDTO.getFirstLevelOrganization());
		DemandRspDTO reqDataCountRspDTO = new DemandRspDTO();
		List<DemandDTO> reqDataCountDTOListA = new LinkedList<>();
		reportLista.forEach(m ->
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
		reportLista = reqDataCountService.getReportForm7(workingHoursDTO.getDevpLeadDept(), workingHoursDTO.getSelectTime(), workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		WorkingHoursRspDTO reqDataCountRspDTO = new WorkingHoursRspDTO();
		List<WorkingHoursDTO> reqDataCountDTOListA = new LinkedList<>();

		reportLista.forEach(m ->
				reqDataCountDTOListA.add(BeanUtils.copyPropertiesReturnDest(new WorkingHoursDTO(), m))
		);
		reqDataCountRspDTO.setVpnInfoDTOS(reqDataCountDTOListA);
		List<WorkingHoursBO> reportListb = new ArrayList<>();
		reportListb = reqDataCountService.getReportForm7B(workingHoursDTO.getDevpLeadDept(), workingHoursDTO.getSelectTime(), workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		List<WorkingHoursDTO> reqDataCountDTOListB = new LinkedList<>();
		reportListb.forEach(m ->
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
		if (workingHoursDTO.getSelectTime() == null || workingHoursDTO.getSelectTime().equals("")) {
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
	 *
	 * @param workingHoursDTO
	 * @return
	 */
	@RequestMapping("/findEpicKeyHours")
	public GenericRspDTO<WorkingHoursRspDTO> findEpicKeyHours(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		List<WorkingHoursBO> reportLista = new ArrayList<>();
		reportLista = reqDataCountService.findEpicKeyHours(workingHoursDTO.getEpickey());
		WorkingHoursRspDTO reqDataCountRspDTO = new WorkingHoursRspDTO();
		List<WorkingHoursDTO> reqDataCountDTOListA = new LinkedList<>();

		reportLista.forEach(m ->
				reqDataCountDTOListA.add(BeanUtils.copyPropertiesReturnDest(new WorkingHoursDTO(), m))
		);
		System.err.println(reqDataCountDTOListA);
		reqDataCountRspDTO.setVpnInfoDTOS(reqDataCountDTOListA);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, reqDataCountRspDTO);
	}

	/**
	 * 根据epic获取需求质量情况
	 *
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
	 *
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

	@RequestMapping("/getDemandHoursRole")
	public GenericRspDTO<DemandHoursRspDTO> getDemandHoursRole(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		DemandHoursRspBO reportLista = new DemandHoursRspBO();
		System.err.println(workingHoursDTO.getEpickey());
		reportLista = reqDataCountService.getDemandHoursRole(workingHoursDTO.getEpickey());
		DemandHoursRspDTO reqDataCountRspDTO = new DemandHoursRspDTO();
		BeanUtils.copyPropertiesReturnDest(reqDataCountRspDTO, reportLista);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, reqDataCountRspDTO);
	}

	/**
	 * 查询需求功能点
	 *
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
	 *
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
	 *
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
		reportLista = reqDataCountService.findList(workingHoursDTO.getDisplayname(), workingHoursDTO.getSelectTime(), workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		WorkingHoursRspDTO reqDataCountRspDTO = new WorkingHoursRspDTO();
		List<WorkingHoursDTO> reqDataCountDTOListA = new LinkedList<>();
		reportLista.forEach(m ->
				reqDataCountDTOListA.add(BeanUtils.copyPropertiesReturnDest(new WorkingHoursDTO(), m))
		);
		reqDataCountRspDTO.setVpnInfoDTOS(reqDataCountDTOListA);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, reqDataCountRspDTO);
	}

	/**
	 * 查询个人体检视图
	 *
	 * @param workingHoursDTO
	 * @return
	 */
	@RequestMapping("/reportform11")
	public GenericRspDTO<WorkingHoursDTO> getReportForm11(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		WorkingHoursBO reportLista = new WorkingHoursBO();
		System.err.println(workingHoursDTO.getDisplayname());
		reportLista = reqDataCountService.getReportForm11(workingHoursDTO.getDisplayname(), workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		WorkingHoursDTO reqDataCountRspDTO = new WorkingHoursDTO();
		BeanUtils.copyPropertiesReturnDest(reqDataCountRspDTO, reportLista);
		System.err.println(reqDataCountRspDTO);
		// 统计部门
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, reqDataCountRspDTO);
	}

	/**
	 * 查询个人体检视图
	 *
	 * @param workingHoursDTO
	 * @return
	 */
	@RequestMapping("/getDemandStaffView")
	public GenericRspDTO<WorkingHoursRspDTO> getDemandStaffView(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		List<WorkingHoursBO> reportLista = new ArrayList<>();
		reportLista = reqDataCountService.getDemandStaffView(workingHoursDTO.getDisplayname(), workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		WorkingHoursRspDTO reqDataCountRspDTO = new WorkingHoursRspDTO();
		List<WorkingHoursDTO> reqDataCountDTOListA = new LinkedList<>();
		reportLista.forEach(m ->
				reqDataCountDTOListA.add(BeanUtils.copyPropertiesReturnDest(new WorkingHoursDTO(), m))
		);
		reqDataCountRspDTO.setVpnInfoDTOS(reqDataCountDTOListA);
		// 统计部门
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, reqDataCountRspDTO);
	}

	/**
	 * 个人视图，需求任务与其它任务工时饼图
	 *
	 * @param workingHoursDTO
	 * @return
	 */
	@RequestMapping("/getDemandStaffTask")
	public GenericRspDTO<DemandHoursRspDTO> getDemandStaffTask(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		DemandHoursRspDTO demandHoursRspDTO = new DemandHoursRspDTO();
		DemandHoursRspBO reportLista = new DemandHoursRspBO();
		reportLista = reqDataCountService.getDemandStaffTask(workingHoursDTO.getDisplayname(), workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		BeanUtils.copyPropertiesReturnDest(demandHoursRspDTO, reportLista);
		System.err.println("demandHoursRspDTO==" + demandHoursRspDTO);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
	}

	/**
	 * 获取个人体检视图的缺陷情况
	 *
	 * @param workingHoursDTO
	 * @return
	 */
	@RequestMapping("/getDemandDefectDetails")
	public GenericRspDTO<DefectDetailsRspDTO> getDemandDefectDetails(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		DefectDetailsRspDTO demandHoursRspDTO = new DefectDetailsRspDTO();
		DefectDetailsRspBO reportLista = new DefectDetailsRspBO();
		reportLista = reqDataCountService.getDemandDefectDetails(workingHoursDTO.getDisplayname(), workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		demandHoursRspDTO.setDefectDetailsDTOArrayList(BeanConvertUtils.convertList(reportLista.getDefectDetailsBos(), DefectDetailsDTO.class));
		System.err.println("demandHoursRspDTO==" + demandHoursRspDTO);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
	}

	/**
	 * 个人体检视图评审问题表
	 *
	 * @param workingHoursDTO
	 * @return
	 */
	@RequestMapping("/getDemandIssueDetails")
	public GenericRspDTO<IssueDetailsRspDTO> getDemandIssueDetails(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		IssueDetailsRspDTO issueDetailsRspDTO = new IssueDetailsRspDTO();
		IssueDetailsRspBO reportLista = new IssueDetailsRspBO();
		reportLista = reqDataCountService.getDemandIssueDetails(workingHoursDTO.getDisplayname(), workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		issueDetailsRspDTO.setIssueDetailsDTOList(BeanConvertUtils.convertList(reportLista.getIssueDetailsBOList(), IssueDetailsDTO.class));
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, issueDetailsRspDTO);
	}

	/**
	 * 个人体检视图生产问题
	 *
	 * @param workingHoursDTO
	 * @return
	 */
	@RequestMapping("/getDemandProductionDetails")
	public GenericRspDTO<ProductionDefectsRspDTO> getDemandProductionDetails(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		ProductionDefectsRspDTO productionDefectsRspDTO = new ProductionDefectsRspDTO();
		ProductionDefectsRspBO reportLista = new ProductionDefectsRspBO();
		reportLista = reqDataCountService.getDemandProductionDetails(workingHoursDTO.getDisplayname(), workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		productionDefectsRspDTO.setProductionDefectsDTOList(BeanConvertUtils.convertList(reportLista.getProductionDefectsBOList(), ProductionDefectsDTO.class));
		System.err.println("demandHoursRspDTO==" + productionDefectsRspDTO);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, productionDefectsRspDTO);
	}

	/**
	 * 团队体检视图，获取基本信息
	 *
	 * @param workingHoursDTO
	 * @return
	 */
	@RequestMapping("/reportform10")
	public GenericRspDTO<WorkingHoursDTO> reportform10(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		WorkingHoursBO workingHoursBO = new WorkingHoursBO();
		workingHoursBO = reqDataCountService.getReportForm10(workingHoursDTO.getDevpLeadDept(), workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		WorkingHoursDTO workingHoursDTO1 = new WorkingHoursDTO();
		BeanUtils.copyPropertiesReturnDest(workingHoursDTO1, workingHoursBO);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, workingHoursDTO1);
	}

	/**
	 * 团队视图获取各个员工的工时
	 *
	 * @param workingHoursDTO
	 * @return
	 */
	@RequestMapping("/getDeptWorkHours")
	public GenericRspDTO<DemandHoursRspDTO> getDeptWorkHours(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
		demandHoursRspBO = reqDataCountService.getDeptWorkHours(workingHoursDTO.getDevpLeadDept(), workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		DemandHoursRspDTO demandHoursRspDTO = new DemandHoursRspDTO();
		BeanUtils.copyPropertiesReturnDest(demandHoursRspDTO, demandHoursRspBO);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
	}

	/**
	 * 团队需求开发情况
	 *
	 * @param workingHoursDTO
	 * @return
	 */
	@RequestMapping("/getDeptStaffView")
	public GenericRspDTO<WorkingHoursRspDTO> getDeptStaffView(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		List<WorkingHoursBO> reportLista = new ArrayList<>();
		reportLista = reqDataCountService.getDeptStaffView(workingHoursDTO.getDevpLeadDept(), workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		WorkingHoursRspDTO reqDataCountRspDTO = new WorkingHoursRspDTO();
		List<WorkingHoursDTO> reqDataCountDTOListA = new LinkedList<>();
		reportLista.forEach(m ->
				reqDataCountDTOListA.add(BeanUtils.copyPropertiesReturnDest(new WorkingHoursDTO(), m))
		);
		reqDataCountRspDTO.setVpnInfoDTOS(reqDataCountDTOListA);
		// 统计部门
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, reqDataCountRspDTO);
	}

	/**
	 * 团队视图的工时和功能点柱状图
	 *
	 * @param workingHoursDTO
	 * @return
	 */
	@RequestMapping("/getDeptWorkHoursAndPoint")
	public GenericRspDTO<DemandHoursRspDTO> getDeptWorkHoursAndPoint(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
		demandHoursRspBO = reqDataCountService.getDeptWorkHoursAndPoint(workingHoursDTO.getDevpLeadDept(), workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		DemandHoursRspDTO demandHoursRspDTO = new DemandHoursRspDTO();
		BeanUtils.copyPropertiesReturnDest(demandHoursRspDTO, demandHoursRspBO);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
	}

	@RequestMapping("/getDeptProduction")
	public GenericRspDTO<DemandHoursRspDTO> getDeptProduction(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
		demandHoursRspBO = reqDataCountService.getDeptProduction(workingHoursDTO.getDevpLeadDept(), workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		DemandHoursRspDTO demandHoursRspDTO = new DemandHoursRspDTO();
		BeanUtils.copyPropertiesReturnDest(demandHoursRspDTO, demandHoursRspBO);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
	}

	/**
	 * 团队获取需求异常状态
	 *
	 * @param workingHoursDTO
	 * @return
	 */
	@RequestMapping("/getDemandDispose")
	public GenericRspDTO<DemandHoursRspDTO> getDemandDispose(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
		demandHoursRspBO = reqDataCountService.getDemandDispose(workingHoursDTO.getDevpLeadDept(), workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		DemandHoursRspDTO demandHoursRspDTO = new DemandHoursRspDTO();
		BeanUtils.copyPropertiesReturnDest(demandHoursRspDTO, demandHoursRspBO);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
	}

	/**
	 * 团队获取需求异常状态
	 *
	 * @param workingHoursDTO
	 * @return
	 */
	@RequestMapping("/getDemandCoordinate")
	public GenericRspDTO<DemandHoursRspDTO> getDemandCoordinate(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
		demandHoursRspBO = reqDataCountService.getDemandCoordinate(workingHoursDTO.getDevpLeadDept(), workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		DemandHoursRspDTO demandHoursRspDTO = new DemandHoursRspDTO();
		BeanUtils.copyPropertiesReturnDest(demandHoursRspDTO, demandHoursRspBO);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
	}

	/**
	 * 团队缺陷，按月查询
	 *
	 * @param workingHoursDTO
	 * @return
	 */
	@RequestMapping("/getDeptFlawNumber")
	public GenericRspDTO<DemandHoursRspDTO> getDeptFlawNumber(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
		demandHoursRspBO = reqDataCountService.getDeptFlawNumber(workingHoursDTO.getDevpLeadDept(), workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		DemandHoursRspDTO demandHoursRspDTO = new DemandHoursRspDTO();
		BeanUtils.copyPropertiesReturnDest(demandHoursRspDTO, demandHoursRspBO);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
	}

	@RequestMapping("/getDeptIssueNumber")
	public GenericRspDTO<DemandHoursRspDTO> getDeptIssueNumber(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
		demandHoursRspBO = reqDataCountService.getDeptIssueNumber(workingHoursDTO.getDevpLeadDept(), workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		DemandHoursRspDTO demandHoursRspDTO = new DemandHoursRspDTO();
		BeanUtils.copyPropertiesReturnDest(demandHoursRspDTO, demandHoursRspBO);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
	}

	@RequestMapping("/getDeptProductionDefects")
	public GenericRspDTO<DemandHoursRspDTO> getDeptProductionDefects(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
		demandHoursRspBO = reqDataCountService.getDeptProductionDefects(workingHoursDTO.getDevpLeadDept(), workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		DemandHoursRspDTO demandHoursRspDTO = new DemandHoursRspDTO();
		BeanUtils.copyPropertiesReturnDest(demandHoursRspDTO, demandHoursRspBO);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
	}

	@RequestMapping("/getDeptProCheckTimeOutStatistics")
	public GenericRspDTO<DemandHoursRspDTO> getDeptProCheckTimeOutStatistics(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
		demandHoursRspBO = reqDataCountService.getDeptProCheckTimeOutStatistics(workingHoursDTO.getDevpLeadDept(), workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		DemandHoursRspDTO demandHoursRspDTO = new DemandHoursRspDTO();
		BeanUtils.copyPropertiesReturnDest(demandHoursRspDTO, demandHoursRspBO);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
	}

	/**
	 * 团队缺陷未解决
	 *
	 * @param workingHoursDTO
	 * @return
	 */
	@RequestMapping("/getDeptProUnhandledIssues1")
	public GenericRspDTO<DemandHoursRspDTO> getDeptProUnhandledIssues1(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
		demandHoursRspBO = reqDataCountService.getDeptProUnhandledIssues1(workingHoursDTO.getDevpLeadDept(), workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		DemandHoursRspDTO demandHoursRspDTO = new DemandHoursRspDTO();
		BeanUtils.copyPropertiesReturnDest(demandHoursRspDTO, demandHoursRspBO);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
	}

	/**
	 * 团队评审问题未解决
	 *
	 * @param workingHoursDTO
	 * @return
	 */
	@RequestMapping("/getDeptProUnhandledIssues2")
	public GenericRspDTO<DemandHoursRspDTO> getDeptProUnhandledIssues2(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
		demandHoursRspBO = reqDataCountService.getDeptProUnhandledIssues2(workingHoursDTO.getDevpLeadDept(), workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		DemandHoursRspDTO demandHoursRspDTO = new DemandHoursRspDTO();
		BeanUtils.copyPropertiesReturnDest(demandHoursRspDTO, demandHoursRspBO);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
	}

	@RequestMapping("/getDeptProductionBack")
	public GenericRspDTO<DemandHoursRspDTO> getDeptProductionBack(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
		demandHoursRspBO = reqDataCountService.getDeptProductionBack(workingHoursDTO.getDevpLeadDept(), workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		DemandHoursRspDTO demandHoursRspDTO = new DemandHoursRspDTO();
		BeanUtils.copyPropertiesReturnDest(demandHoursRspDTO, demandHoursRspBO);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
	}

	@RequestMapping("/getBuildFailedCount")
	public GenericRspDTO<DemandHoursRspDTO> getBuildFailedCount(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
		demandHoursRspBO = reqDataCountService.getBuildFailedCount(workingHoursDTO.getDevpLeadDept(), workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		DemandHoursRspDTO demandHoursRspDTO = new DemandHoursRspDTO();
		BeanUtils.copyPropertiesReturnDest(demandHoursRspDTO, demandHoursRspBO);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
	}

	@RequestMapping("/getSmokeTestFailedCount")
	public GenericRspDTO<DemandHoursRspDTO> getSmokeTestFailedCount(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
		demandHoursRspBO = reqDataCountService.getSmokeTestFailedCount(workingHoursDTO.getDevpLeadDept(), workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		DemandHoursRspDTO demandHoursRspDTO = new DemandHoursRspDTO();
		BeanUtils.copyPropertiesReturnDest(demandHoursRspDTO, demandHoursRspBO);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
	}

	@RequestMapping("/getDeptSum")
	public GenericRspDTO<DemandHoursRspDTO> getDeptSum(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
		demandHoursRspBO = reqDataCountService.getDeptSum(workingHoursDTO.getDevpLeadDept(), workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		DemandHoursRspDTO demandHoursRspDTO = new DemandHoursRspDTO();
		BeanUtils.copyPropertiesReturnDest(demandHoursRspDTO, demandHoursRspBO);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
	}

	/**
	 * 查询个人体检视图
	 *
	 * @param workingHoursDTO
	 * @return
	 */
	@RequestMapping("/reportform12")
	public GenericRspDTO<DemandHoursRspDTO> getReportForm12(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		DemandHoursRspBO reportLista = new DemandHoursRspBO();
		reportLista = reqDataCountService.getReportForm12();
		DemandHoursRspDTO reqDataCountRspDTO = new DemandHoursRspDTO();
		BeanUtils.copyPropertiesReturnDest(reqDataCountRspDTO, reportLista);
		System.err.println(reqDataCountRspDTO);
		// 统计部门
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, reqDataCountRspDTO);
	}

	@RequestMapping("/getCentreWorkHours")
	public GenericRspDTO<DemandHoursRspDTO> getCentreWorkHours(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
		demandHoursRspBO = reqDataCountService.getCentreWorkHours(workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		DemandHoursRspDTO demandHoursRspDTO = new DemandHoursRspDTO();
		BeanUtils.copyPropertiesReturnDest(demandHoursRspDTO, demandHoursRspBO);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
	}
	@RequestMapping("/getCentreWorkHours2")
	public GenericRspDTO<DemandHoursRspDTO> getCentreWorkHours2(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
		demandHoursRspBO = reqDataCountService.getCentreWorkHours2(workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		DemandHoursRspDTO demandHoursRspDTO = new DemandHoursRspDTO();
		BeanUtils.copyPropertiesReturnDest(demandHoursRspDTO, demandHoursRspBO);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
	}

	@RequestMapping("/getCentreWorkHoursPoint")
	public GenericRspDTO<DemandHoursRspDTO> getCentreWorkHoursPoint(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
		demandHoursRspBO = reqDataCountService.getCentreWorkHoursPoint(workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		DemandHoursRspDTO demandHoursRspDTO = new DemandHoursRspDTO();
		BeanUtils.copyPropertiesReturnDest(demandHoursRspDTO, demandHoursRspBO);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
	}

	@RequestMapping("/getCentreProduction")
	public GenericRspDTO<DemandHoursRspDTO> getCentreProduction(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
		demandHoursRspBO = reqDataCountService.getCentreProduction(workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		DemandHoursRspDTO demandHoursRspDTO = new DemandHoursRspDTO();
		BeanUtils.copyPropertiesReturnDest(demandHoursRspDTO, demandHoursRspBO);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
	}

	@RequestMapping("/getCentreDispose")
	public GenericRspDTO<DemandHoursRspDTO> getCentreDispose(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
		demandHoursRspBO = reqDataCountService.getCentreDispose(workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		DemandHoursRspDTO demandHoursRspDTO = new DemandHoursRspDTO();
		BeanUtils.copyPropertiesReturnDest(demandHoursRspDTO, demandHoursRspBO);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
	}

	@RequestMapping("/getCentreProductionDept")
	public GenericRspDTO<DemandHoursRspDTO> getCentreProductionDept(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
		demandHoursRspBO = reqDataCountService.getCentreProductionDept(workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		DemandHoursRspDTO demandHoursRspDTO = new DemandHoursRspDTO();
		BeanUtils.copyPropertiesReturnDest(demandHoursRspDTO, demandHoursRspBO);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
	}
		//获得测试部员工工作
	@RequestMapping("/getTestStaffWork")
	public GenericRspDTO<DemandHoursRspDTO> getTestStaffWork(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
		demandHoursRspBO = reqDataCountService.getTestStaffWork(workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		DemandHoursRspDTO demandHoursRspDTO = new DemandHoursRspDTO();
		BeanUtils.copyPropertiesReturnDest(demandHoursRspDTO, demandHoursRspBO);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
	}

	@RequestMapping("/getCentreBuildFailedCount")
	public GenericRspDTO<DemandHoursRspDTO> getCentreBuildFailedCount(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
		demandHoursRspBO = reqDataCountService.getCentreBuildFailedCount(workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		DemandHoursRspDTO demandHoursRspDTO = new DemandHoursRspDTO();
		BeanUtils.copyPropertiesReturnDest(demandHoursRspDTO, demandHoursRspBO);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
	}

	@RequestMapping("/getCentreSmokeTestFailedCount")
	public GenericRspDTO<DemandHoursRspDTO> getCentreSmokeTestFailedCount(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
		demandHoursRspBO = reqDataCountService.getCentreSmokeTestFailedCount(workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		DemandHoursRspDTO demandHoursRspDTO = new DemandHoursRspDTO();
		BeanUtils.copyPropertiesReturnDest(demandHoursRspDTO, demandHoursRspBO);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
	}

	@RequestMapping("/getCentreFlawNumber")
	public GenericRspDTO<DemandHoursRspDTO> getCentreFlawNumber(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
		demandHoursRspBO = reqDataCountService.getCentreFlawNumber(workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		DemandHoursRspDTO demandHoursRspDTO = new DemandHoursRspDTO();
		BeanUtils.copyPropertiesReturnDest(demandHoursRspDTO, demandHoursRspBO);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
	}
    @RequestMapping("/getCentreFlawNumberA")
    public GenericRspDTO<DemandHoursRspDTO> getCentreFlawNumberA(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO = reqDataCountService.getCentreFlawNumberA(workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
        DemandHoursRspDTO demandHoursRspDTO = new DemandHoursRspDTO();
        BeanUtils.copyPropertiesReturnDest(demandHoursRspDTO, demandHoursRspBO);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
    }

	@RequestMapping("/getCentreIssueNumber")
	public GenericRspDTO<DemandHoursRspDTO> getCentreIssueNumber(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
		demandHoursRspBO = reqDataCountService.getCentreIssueNumber(workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		DemandHoursRspDTO demandHoursRspDTO = new DemandHoursRspDTO();
		BeanUtils.copyPropertiesReturnDest(demandHoursRspDTO, demandHoursRspBO);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
	}

	@RequestMapping("/getCentreProductionDefects")
	public GenericRspDTO<DemandHoursRspDTO> getCentreProductionDefects(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
		demandHoursRspBO = reqDataCountService.getCentreProductionDefects(workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		DemandHoursRspDTO demandHoursRspDTO = new DemandHoursRspDTO();
		BeanUtils.copyPropertiesReturnDest(demandHoursRspDTO, demandHoursRspBO);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
	}

	/**
	 * 中心投产验证不及时
	 *
	 * @param workingHoursDTO
	 * @return
	 */
	@RequestMapping("/getCentreProCheckTimeOutStatistics")
	public GenericRspDTO<DemandHoursRspDTO> getCentreProCheckTimeOutStatistics(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
		demandHoursRspBO = reqDataCountService.getCentreProCheckTimeOutStatistics(workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		DemandHoursRspDTO demandHoursRspDTO = new DemandHoursRspDTO();
		BeanUtils.copyPropertiesReturnDest(demandHoursRspDTO, demandHoursRspBO);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
	}

	/**
	 * 中心缺陷未解决
	 *
	 * @param workingHoursDTO
	 * @return
	 */
	@RequestMapping("/getCentreProUnhandledIssues1")
	public GenericRspDTO<DemandHoursRspDTO> getCentreProUnhandledIssues1(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
		demandHoursRspBO = reqDataCountService.getCentreProUnhandledIssues1(workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		DemandHoursRspDTO demandHoursRspDTO = new DemandHoursRspDTO();
		BeanUtils.copyPropertiesReturnDest(demandHoursRspDTO, demandHoursRspBO);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
	}

	/**
	 * 中心评审问题未解决
	 *
	 * @param workingHoursDTO
	 * @return
	 */
	@RequestMapping("/getCentreProUnhandledIssues2")
	public GenericRspDTO<DemandHoursRspDTO> getCentreProUnhandledIssues2(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
		demandHoursRspBO = reqDataCountService.getCentreProUnhandledIssues2(workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		DemandHoursRspDTO demandHoursRspDTO = new DemandHoursRspDTO();
		BeanUtils.copyPropertiesReturnDest(demandHoursRspDTO, demandHoursRspBO);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
	}

	@RequestMapping("/getCentreFlawNumberDept")
	public GenericRspDTO<DemandHoursRspDTO> getCentreFlawNumberDept(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
		demandHoursRspBO = reqDataCountService.getCentreFlawNumberDept(workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		DemandHoursRspDTO demandHoursRspDTO = new DemandHoursRspDTO();
		BeanUtils.copyPropertiesReturnDest(demandHoursRspDTO, demandHoursRspBO);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
	}
    @RequestMapping("/getCentreFlawNumberDeptA")
    public GenericRspDTO<DemandHoursRspDTO> getCentreFlawNumberDeptA(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO = reqDataCountService.getCentreFlawNumberDeptA(workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
        DemandHoursRspDTO demandHoursRspDTO = new DemandHoursRspDTO();
        BeanUtils.copyPropertiesReturnDest(demandHoursRspDTO, demandHoursRspBO);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
    }

	@RequestMapping("/getCentreIssueNumberDept")
	public GenericRspDTO<DemandHoursRspDTO> getCentreIssueNumberDept(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
		demandHoursRspBO = reqDataCountService.getCentreIssueNumberDept(workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		DemandHoursRspDTO demandHoursRspDTO = new DemandHoursRspDTO();
		BeanUtils.copyPropertiesReturnDest(demandHoursRspDTO, demandHoursRspBO);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
	}

	@RequestMapping("/getCentreProductionDefectsDept")
	public GenericRspDTO<DemandHoursRspDTO> getCentreProductionDefectsDept(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
		demandHoursRspBO = reqDataCountService.getCentreProductionDefectsDept(workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		DemandHoursRspDTO demandHoursRspDTO = new DemandHoursRspDTO();
		BeanUtils.copyPropertiesReturnDest(demandHoursRspDTO, demandHoursRspBO);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
	}

	/**
	 * 中心投产验证不及时
	 *
	 * @param workingHoursDTO
	 * @return
	 */
	@RequestMapping("/getCentreProCheckTimeOutStatisticsDept")
	public GenericRspDTO<DemandHoursRspDTO> getCentreProCheckTimeOutStatisticsDept(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
		demandHoursRspBO = reqDataCountService.getCentreProCheckTimeOutStatisticsDept(workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		DemandHoursRspDTO demandHoursRspDTO = new DemandHoursRspDTO();
		BeanUtils.copyPropertiesReturnDest(demandHoursRspDTO, demandHoursRspBO);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
	}

	/**
	 * 中心缺陷未解决
	 *
	 * @param workingHoursDTO
	 * @return
	 */
	@RequestMapping("/getCentreProUnhandledIssues1Dept")
	public GenericRspDTO<DemandHoursRspDTO> getCentreProUnhandledIssues1Dept(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
		demandHoursRspBO = reqDataCountService.getCentreProUnhandledIssues1Dept(workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		DemandHoursRspDTO demandHoursRspDTO = new DemandHoursRspDTO();
		BeanUtils.copyPropertiesReturnDest(demandHoursRspDTO, demandHoursRspBO);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
	}

	/**
	 * 中心评审问题未解决
	 *
	 * @param workingHoursDTO
	 * @return
	 */
	@RequestMapping("/getCentreProUnhandledIssues2Dept")
	public GenericRspDTO<DemandHoursRspDTO> getCentreProUnhandledIssues2Dept(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
		demandHoursRspBO = reqDataCountService.getCentreProUnhandledIssues2Dept(workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		DemandHoursRspDTO demandHoursRspDTO = new DemandHoursRspDTO();
		BeanUtils.copyPropertiesReturnDest(demandHoursRspDTO, demandHoursRspBO);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
	}

	/**
	 * 团队投产验证未及时
	 *
	 * @param workingHoursDTO
	 * @return
	 */
	@RequestMapping("/listOfUntimelyStatusChanges")
	public GenericRspDTO<ProductionVerificationIsNotTimelyRspDTO> listOfUntimelyStatusChanges(@RequestBody DetailsReqDTO workingHoursDTO) {
		ProductionVerificationIsNotTimelyRspDTO demandHoursRspDTO = new ProductionVerificationIsNotTimelyRspDTO();
		List<ProductionVerificationIsNotTimelyBO> listOfUntimelyStatusChangesBos = reqDataCountService.listOfUntimelyStatusChanges(workingHoursDTO.getName(),workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		demandHoursRspDTO.setProductionVerificationIsNotTimelyDTOList(BeanConvertUtils.convertList(listOfUntimelyStatusChangesBos, ProductionVerificationIsNotTimelyDTO.class));
		System.err.println(demandHoursRspDTO.getProductionVerificationIsNotTimelyDTOList());
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
	}

	@RequestMapping("/downloadreportform1")
	public GenericRspDTO<NoBody> downloadReportForm1(@RequestBody ReqDataCountReqDTO reqDataCountReqDTO, HttpServletResponse response) {
		String month = DateUtil.date2String(new Date(), "yyyy-MM");
		if (reqDataCountReqDTO.getReqImplMon() == null || reqDataCountReqDTO.getReqImplMon().equals("")) {
			reqDataCountReqDTO.setReqImplMon(month);
		}
		reqDataCountService.downloadDemandImplementationReport(reqDataCountReqDTO.getReqImplMon(), response);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS);
	}

	@RequestMapping("/downloadreportform2")
	public GenericRspDTO<NoBody> downloadReportForm2(@RequestBody ReqDataCountReqDTO reqDataCountReqDTO, HttpServletResponse response) {
		String month = DateUtil.date2String(new Date(), "yyyy-MM");
		if (reqDataCountReqDTO.getReqImplMon() == null || reqDataCountReqDTO.getReqImplMon().equals("")) {
			reqDataCountReqDTO.setReqImplMon(month);
		}
		reqDataCountService.downloadDemandCompletionReport(reqDataCountReqDTO.getReqImplMon(), response);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS);
	}

	@RequestMapping("/downloadreportform21")
	public GenericRspDTO<NoBody> downloadReportForm21(@RequestBody ReqDataCountReqDTO reqDataCountReqDTO, HttpServletResponse response) {
		String month = DateUtil.date2String(new Date(), "yyyy-MM");
		if (reqDataCountReqDTO.getReqImplMon() == null || reqDataCountReqDTO.getReqImplMon().equals("")) {
			reqDataCountReqDTO.setReqImplMon(month);
		}
		reqDataCountService.downloadDemandCompletionReport2(reqDataCountReqDTO.getReqImplMon(), response);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS);
	}

	@RequestMapping("/downloadreportform3")
	public GenericRspDTO<NoBody> downloadReportForm3(@RequestBody ReqDataCountReqDTO reqDataCountReqDTO, HttpServletResponse response) {
		String month = DateUtil.date2String(new Date(), "yyyy-MM");
		if (reqDataCountReqDTO.getReqImplMon() == null || reqDataCountReqDTO.getReqImplMon().equals("")) {
			reqDataCountReqDTO.setReqImplMon(month);
		}
		reqDataCountService.downloadDemandTypeStatistics(reqDataCountReqDTO.getReqImplMon(), response);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS);
	}

	@RequestMapping("/downloadreportform4")
	public GenericRspDTO<NoBody> downloadReportForm4(@RequestBody ReqDataCountReqDTO reqDataCountReqDTO, HttpServletResponse response) {
		String month = DateUtil.date2String(new Date(), "yyyy-MM");
		if (reqDataCountReqDTO.getReqImplMon() == null || reqDataCountReqDTO.getReqImplMon().equals("")) {
			reqDataCountReqDTO.setReqImplMon(month);
		}
		reqDataCountService.downloadBaseOwnershipDepartmentStatistics(reqDataCountReqDTO.getReqImplMon(), response);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS);
	}

	@RequestMapping("/downloadreportform5")
	public GenericRspDTO<NoBody> downloadReportForm5(@RequestBody ReqDataCountReqDTO reqDataCountReqDTO, HttpServletResponse response) {
		String month = DateUtil.date2String(new Date(), "yyyy-MM");
		if (reqDataCountReqDTO.getReqImplMon() == null || reqDataCountReqDTO.getReqImplMon().equals("")) {
			reqDataCountReqDTO.setReqImplMon(month);
		}
		reqDataCountService.downloadProductionTypeStatistics(reqDataCountReqDTO.getReqImplMon(), response);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS);
	}

	@RequestMapping("/downloadreportform6")
	public GenericRspDTO<NoBody> downloadReportForm6(@RequestBody ReqDataCountReqDTO reqDataCountReqDTO, HttpServletResponse response) {
		String month = DateUtil.date2String(new Date(), "yyyy-MM");
		if (reqDataCountReqDTO.getReqImplMon() == null || reqDataCountReqDTO.getReqImplMon().equals("")) {
			reqDataCountReqDTO.setReqImplMon(month);
		}
		reqDataCountService.downloadDemandUploadDocumentBO(reqDataCountReqDTO.getReqImplMon(), reqDataCountReqDTO.getDevpLeadDept(), reqDataCountReqDTO.getProductMng(), reqDataCountReqDTO.getFirstLevelOrganization(), response);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS);
	}

	@RequestMapping("/downloadreportform7")
	public GenericRspDTO<NoBody> downloadReportForm7(@RequestBody WorkingHoursReqDTO workingHoursDTO, HttpServletResponse response) {
		reqDataCountService.downloadReportForm7(workingHoursDTO.getDevpLeadDept(), workingHoursDTO.getSelectTime(), workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2(),response);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS);
	}

	@RequestMapping("/getDefectMonthlyReport")
	public GenericRspDTO<DefectMonthlyRspDTO> getDefectMonthlyReport(@RequestBody DemandReqDTO reqDTO) {
		List<DefectMonthlyBO> defectMonthlyReport = reqTaskService.getDefectMonthlyReport(reqDTO.getDefectStartTime(), reqDTO.getDefectEndTime());
		List<DefectMonthlyDTO> defectMonthlyList = new LinkedList<>();
		defectMonthlyReport.forEach(m ->
				defectMonthlyList.add(BeanUtils.copyPropertiesReturnDest(new DefectMonthlyDTO(), m))
		);
		DefectMonthlyRspDTO defectMonthlyRspDTO = new DefectMonthlyRspDTO();
		defectMonthlyRspDTO.setDefectMonthlyDTOList(defectMonthlyList);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, defectMonthlyRspDTO);
	}

	//测试统计汇总表
	@RequestMapping("/testStatisticsList")
	public GenericRspDTO<TestStatisticsRspDTO> testStatisticsList(@RequestBody TestStatisticsReqDTO reqDTO) {
		TestStatisticsBO testStatisticsBO = new TestStatisticsBO();
		BeanUtils.copyPropertiesReturnDest(testStatisticsBO, reqDTO);
		List<TestStatisticsRspBO> testStatisticsRspBOS = reqDataCountService.testStatisticsList(testStatisticsBO);
		List<TestStatisticsDTO> testStatisticsDTOList = new LinkedList<>();
		testStatisticsRspBOS.forEach(m ->
				testStatisticsDTOList.add(BeanUtils.copyPropertiesReturnDest(new TestStatisticsDTO(), m))
		);
		TestStatisticsRspDTO testStatisticsRspDTO = new TestStatisticsRspDTO();
		testStatisticsRspDTO.setTestStatisticsDTOList(testStatisticsDTOList);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, testStatisticsRspDTO);
	}
    //产品线缺陷率
	@RequestMapping("/productLineDefectRate")
	public GenericRspDTO<ProductLineDefectRateRspDTO> productLineDefectRate(@RequestBody ProductLineDefectRateReqDTO reqDTO) {
		List<ProductLineDefectsBO> productLineDefectsBOS = reqDataCountService.productLineDefectRate(reqDTO.getSelectTime2());

		List<ProductLineDefectsDTO> productLineDefectsList = new LinkedList<>();
		productLineDefectsBOS.forEach(m->{
			productLineDefectsList.add(BeanUtils.copyPropertiesReturnDest(new ProductLineDefectsDTO(), m));
		});
		ProductLineDefectRateRspDTO productLineDefectRateRspDTO = new ProductLineDefectRateRspDTO();
		productLineDefectRateRspDTO.setProductLineDefectsList(productLineDefectsList);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS,productLineDefectRateRspDTO);
	}

	//部门缺陷率
	@RequestMapping("/departmentalDefectRate")
	public GenericRspDTO<ProductLineDefectRateRspDTO> departmentalDefectRate(@RequestBody ProductLineDefectRateReqDTO reqDTO) {
		List<ProductLineDefectsBO> productLineDefectsBOS = reqDataCountService.departmentalDefectRate(reqDTO.getSelectTime2());

		List<ProductLineDefectsDTO> productLineDefectsList = new LinkedList<>();
		productLineDefectsBOS.forEach(m->{
			productLineDefectsList.add(BeanUtils.copyPropertiesReturnDest(new ProductLineDefectsDTO(), m));
		});
		ProductLineDefectRateRspDTO productLineDefectRateRspDTO = new ProductLineDefectRateRspDTO();
		productLineDefectRateRspDTO.setProductLineDefectsList(productLineDefectsList);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS,productLineDefectRateRspDTO);
	}
    /**
     * 中心代码团队代码缺陷率
     *
     * @param workingHoursDTO
     * @return
     */
    @RequestMapping("/departmentalDefectRate2")
    public GenericRspDTO<DemandHoursRspDTO> departmentalDefectRate2(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO = reqDataCountService.departmentalDefectRate2(workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
        DemandHoursRspDTO demandHoursRspDTO = new DemandHoursRspDTO();
        BeanUtils.copyPropertiesReturnDest(demandHoursRspDTO, demandHoursRspBO);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
    }
	//需求测试状态
	@RequestMapping("/demandTestStatus")
	public GenericRspDTO<DemandTestStatusRspDTO> demandTestStatus(GenericDTO<NoBody> req) {

		List<DemandTestStatusDTO> demandTestStatusDTOList = new LinkedList<>();
		// 获取需求测试情况
		List<DemandTestStatusBO> demandTestStatusBOList = reqDataCountService.demandTestStatusList();
		demandTestStatusBOList.forEach(m->{
			demandTestStatusDTOList.add(BeanUtils.copyPropertiesReturnDest(new DemandTestStatusDTO(), m));
		});
		DemandTestStatusRspDTO demandTestStatusRspDTOList = new DemandTestStatusRspDTO();
		demandTestStatusRspDTOList.setDemandTestStatusDTOList(demandTestStatusDTOList);

		//当前缺陷处理情况
		List<DefectProcesStatusBO> defectProcesStatusList = reqDataCountService.defectProcesStatus();
		List<DefectProcesStatusDTO> defectProcesStatusDTOList = new LinkedList<>();
		defectProcesStatusList.forEach(m->{
			defectProcesStatusDTOList.add(BeanUtils.copyPropertiesReturnDest(new DefectProcesStatusDTO(), m));
		});
		demandTestStatusRspDTOList.setDefectProcesStatusDTOList(defectProcesStatusDTOList);

		return GenericRspDTO.newInstance(MsgEnum.SUCCESS,demandTestStatusRspDTOList);
	}

    //线上漏出率
	@RequestMapping("/getOnlineLeakageRate")
	public GenericRspDTO<OnlineLeakageRateRspDTO> getOnlineLeakageRate(@RequestBody ProductLineDefectRateReqDTO reqDTO) {
		OnlineLeakageRateBO onlineLeakageRate = reqDataCountService.getOnlineLeakageRate(reqDTO.getSelectTime2());
		OnlineLeakageRateRspDTO onlineLeakageRateRspDTO = new OnlineLeakageRateRspDTO();
		onlineLeakageRateRspDTO.setMonths(onlineLeakageRate.getMonths());
		onlineLeakageRateRspDTO.setLeakageRate(onlineLeakageRate.getLeakageRate());
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS,onlineLeakageRateRspDTO);
	}
    //查询6个月内部门的缺陷率
	@RequestMapping("/getDeptDefectRate")
	public GenericRspDTO<OnlineLeakageRateRspDTO> getDeptDefectRate(@RequestBody  WorkingHoursReqDTO workingHoursDTO) {
		OnlineLeakageRateBO deptDefectRate = reqDataCountService.getDeptDefectRate(workingHoursDTO.getDevpLeadDept(), workingHoursDTO.getSelectTime2());
		OnlineLeakageRateRspDTO onlineLeakageRateRspDTO = new OnlineLeakageRateRspDTO();
		onlineLeakageRateRspDTO.setMonths(deptDefectRate.getMonths());
		onlineLeakageRateRspDTO.setLeakageRate(deptDefectRate.getLeakageRate());
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS,onlineLeakageRateRspDTO);
	}

	//查询测试部测试进度
	@RequestMapping("/testProgressDetail")
	public GenericRspDTO<TestProgressDetailRspDTO> testProgressDetail(@RequestBody  WorkingHoursReqDTO workingHoursDTO) {
        TestProgressDetailRspBO testProgressDetailRspBO = reqDataCountService.testProgressDetail(workingHoursDTO.getSelectTime1());
        TestProgressDetailRspDTO testProgressDetailRspDTO = new TestProgressDetailRspDTO();
        testProgressDetailRspDTO.setTestProgressDetailDTOList(BeanConvertUtils.convertList(testProgressDetailRspBO.getTestProgressDetailBOList(), TestProgressDetailDTO.class));
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS,testProgressDetailRspDTO);
	}
    /**
     * 中心缺陷未解决
     *
     * @param reqDTO
     * @return
     */
    @RequestMapping("/planSearch")
    public GenericRspDTO<DemandHoursRspDTO> planSearch(@RequestBody DemandReqDTO reqDTO) {
        DemandBO demandBO = BeanUtils.copyPropertiesReturnDest(new DemandBO(), reqDTO);
        DemandRspBO demandRspBO = reqPlanService.findDemand(demandBO);
        List<DemandBO> list = demandRspBO.getDemandBOList();
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO = reqDataCountService.planSearch(list);
        DemandHoursRspDTO demandHoursRspDTO = new DemandHoursRspDTO();
        BeanUtils.copyPropertiesReturnDest(demandHoursRspDTO, demandHoursRspBO);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
    }

	/**
	 * 双击柱状图缺陷数据详情
	 * @param detailsReqDTO
	 * @return
	 */
	@RequestMapping("/getDetails")
	public GenericRspDTO<DefectDetailsRspDTO> getDefectDetails(@RequestBody DetailsReqDTO detailsReqDTO) {
		DefectDetailsRspBO defectDetailsRspBO = new DefectDetailsRspBO();
		defectDetailsRspBO = reqDataCountService.getDetails(detailsReqDTO.getSelectTime1(), detailsReqDTO.getSelectTime2(),detailsReqDTO.getSeriesName(),detailsReqDTO.getName());
		DefectDetailsRspDTO defectDetailsRspDTO = new DefectDetailsRspDTO();
		defectDetailsRspDTO.setDefectDetailsDTOArrayList(BeanConvertUtils.convertList(defectDetailsRspBO.getDefectDetailsBos(), DefectDetailsDTO.class));
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, defectDetailsRspDTO);
	}

	/**
	 * 双击柱状图评审问题详情
	 * @param detailsReqDTO
	 * @return
	 */
	@RequestMapping("/getIssueDetails")
	public GenericRspDTO<IssueDetailsRspDTO> getIssueDetails(@RequestBody DetailsReqDTO detailsReqDTO) {
		IssueDetailsRspBO issueDetailsRspBO = new IssueDetailsRspBO();
		issueDetailsRspBO = reqDataCountService.getIssueDetails(detailsReqDTO.getSelectTime1(), detailsReqDTO.getSelectTime2(),detailsReqDTO.getSeriesName(),detailsReqDTO.getName());
		IssueDetailsRspDTO issueDetailsRspDTO = new IssueDetailsRspDTO();
		issueDetailsRspDTO.setIssueDetailsDTOList(BeanConvertUtils.convertList(issueDetailsRspBO.getIssueDetailsBOList(), IssueDetailsDTO.class));
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, issueDetailsRspDTO);
	}

	/**
	 * 双击查看生产缺陷详情
	 * @param detailsReqDTO
	 * @return
	 */
	@RequestMapping("/getProDefectDetails")
	public GenericRspDTO<ProductionDefectsRspDTO> getProDefectDetails(@RequestBody DetailsReqDTO detailsReqDTO) {
		ProductionDefectsRspBO productionDefectsRspBO = new ProductionDefectsRspBO();
		productionDefectsRspBO = reqDataCountService.getProDefectDetails(detailsReqDTO.getSelectTime1(), detailsReqDTO.getSelectTime2(),detailsReqDTO.getSeriesName(),detailsReqDTO.getName());
		ProductionDefectsRspDTO productionDefectsRspDTO = new ProductionDefectsRspDTO();
		productionDefectsRspDTO.setProductionDefectsDTOList(BeanConvertUtils.convertList(productionDefectsRspBO.getProductionDefectsBOList(), ProductionDefectsDTO.class));
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, productionDefectsRspDTO);
	}

	/**
	 * 投产未解决权限，评审问题
	 * @param detailsReqDTO
	 * @return
	 */
	@RequestMapping("/getProUnhandledIssuesDetails")
	public GenericRspDTO<ProUnhandledIssuesRspDTO> getProUnhandledIssuesDetails(@RequestBody DetailsReqDTO detailsReqDTO) {
		ProUnhandledIssuesRspBO proUnhandledIssuesRspBO = new ProUnhandledIssuesRspBO();
		proUnhandledIssuesRspBO = reqDataCountService.getProUnhandledIssuesDetails(detailsReqDTO.getSelectTime1(), detailsReqDTO.getSelectTime2(),detailsReqDTO.getSeriesName(),detailsReqDTO.getName());
		ProUnhandledIssuesRspDTO unhandledIssuesRspDTO = new ProUnhandledIssuesRspDTO();
		unhandledIssuesRspDTO.setProUnhandledIssuesDTOList(BeanConvertUtils.convertList(proUnhandledIssuesRspBO.getProUnhandledIssuesBOList(), ProUnhandledIssuesDTO.class));
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, unhandledIssuesRspDTO);
	}

	/**
	 * 投产未解决权限，评审问题
	 * @param detailsReqDTO
	 * @return
	 */
	@RequestMapping("/getSmokeTestFailedCount2")
	public GenericRspDTO<SmokeTestFailedCountRspDTO> getSmokeTestFailedCount2(@RequestBody SmokeTestFailedCountReqDTO detailsReqDTO) {
		SmokeTestFailedCountRspBO proUnhandledIssuesRspBO = new SmokeTestFailedCountRspBO();
		System.err.println(detailsReqDTO);
		proUnhandledIssuesRspBO = reqDataCountService.getSmokeTestFailedCount2(detailsReqDTO.getSelectTime1(), detailsReqDTO.getSelectTime2(),detailsReqDTO.getCount());
		SmokeTestFailedCountRspDTO unhandledIssuesRspDTO = new SmokeTestFailedCountRspDTO();
		unhandledIssuesRspDTO.setSmokeTestFailedCountDTOList(BeanConvertUtils.convertList(proUnhandledIssuesRspBO.getSmokeTestFailedCountBOList(), SmokeTestFailedCountDTO.class));
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, unhandledIssuesRspDTO);
	}

	/**
	 * 个人视图查询代码提交情况
	 *
	 * @param workingHoursDTO
	 * @return
	 */
	@RequestMapping("/getGetLabDateView")
	public GenericRspDTO<GitlabDataRspDTO> getGetLabDateView(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		List<GitlabDataBO> reportLista = new ArrayList<>();
		reportLista = reqDataCountService.getGetLabDateView(workingHoursDTO.getDisplayname(), workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		GitlabDataRspDTO reqDataCountRspDTO = new GitlabDataRspDTO();
		List<GitlabDataDTO> gitlabDataDTOLinkedList = new LinkedList<>();
		reportLista.forEach(m ->
				gitlabDataDTOLinkedList.add(BeanUtils.copyPropertiesReturnDest(new GitlabDataDTO(), m))
		);
		reqDataCountRspDTO.setGitlabDataRspDTOList(gitlabDataDTOLinkedList);
		// 统计部门
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, reqDataCountRspDTO);
	}
	@RequestMapping("/getCentreGitLab")
	public GenericRspDTO<DemandHoursRspDTO> getCentreGitLab(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
		demandHoursRspBO = reqDataCountService.getCentreGitLab(workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		DemandHoursRspDTO demandHoursRspDTO = new DemandHoursRspDTO();
		BeanUtils.copyPropertiesReturnDest(demandHoursRspDTO, demandHoursRspBO);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
	}
    @RequestMapping("/getCentreGitLabDept")
    public GenericRspDTO<DemandHoursRspDTO> getCentreGitLabDept(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO = reqDataCountService.getCentreGitLabDept(workingHoursDTO.getDevpLeadDept(),workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
        DemandHoursRspDTO demandHoursRspDTO = new DemandHoursRspDTO();
        BeanUtils.copyPropertiesReturnDest(demandHoursRspDTO, demandHoursRspBO);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
    }
	@RequestMapping("/getCentreGitlabMonth")
	public GenericRspDTO<DemandHoursRspDTO> getCentreGitlabMonth(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
		DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
		demandHoursRspBO = reqDataCountService.getCentreGitlabMonth(workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
		DemandHoursRspDTO demandHoursRspDTO = new DemandHoursRspDTO();
		BeanUtils.copyPropertiesReturnDest(demandHoursRspDTO, demandHoursRspBO);
		return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
	}
    @RequestMapping("/getTeamGitlabMonth")
    public GenericRspDTO<DemandHoursRspDTO> getTeamGitlabMonth(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO = reqDataCountService.getTeamGitlabMonth(workingHoursDTO.getDevpLeadDept(),workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
        DemandHoursRspDTO demandHoursRspDTO = new DemandHoursRspDTO();
        BeanUtils.copyPropertiesReturnDest(demandHoursRspDTO, demandHoursRspBO);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
    }
    @RequestMapping("/getEmployeeGitlabMonth")
    public GenericRspDTO<DemandHoursRspDTO> getEmployeeGitlabMonth(@RequestBody WorkingHoursReqDTO workingHoursDTO) {
        DemandHoursRspBO demandHoursRspBO = new DemandHoursRspBO();
        demandHoursRspBO = reqDataCountService.getEmployeeGitlabMonth(workingHoursDTO.getDisplayname(),workingHoursDTO.getSelectTime1(), workingHoursDTO.getSelectTime2());
        DemandHoursRspDTO demandHoursRspDTO = new DemandHoursRspDTO();
        BeanUtils.copyPropertiesReturnDest(demandHoursRspDTO, demandHoursRspBO);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, demandHoursRspDTO);
    }
}
