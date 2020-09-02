package com.cmpay.lemon.monitor.controller.demand;

import com.cmpay.framework.data.request.GenericDTO;
import com.cmpay.framework.data.response.GenericRspDTO;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.common.utils.StringUtils;
import com.cmpay.lemon.framework.data.NoBody;
import com.cmpay.lemon.monitor.bo.*;
import com.cmpay.lemon.monitor.constant.MonitorConstants;
import com.cmpay.lemon.monitor.dto.*;
import com.cmpay.lemon.monitor.entity.DemandDO;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.demand.ReqTaskService;
import com.cmpay.lemon.monitor.service.jira.JiraOperationService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import com.cmpay.lemon.monitor.utils.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.List;

import static com.cmpay.lemon.monitor.constant.MonitorConstants.FILE;
import static com.cmpay.lemon.monitor.utils.FileUtils.doWrite;

@RestController
@RequestMapping(value = MonitorConstants.EASE_PATH)
public class DemandEaseDevelopmentController {
    private static Logger logger = LoggerFactory.getLogger(DemandEaseDevelopmentController.class);

    @Autowired
    private ReqTaskService reqTaskService;
    @Autowired
    private JiraOperationService jiraOperationService;
    @Autowired
    private HttpServletResponse httpServletResponse;
    /**
     * 分页需求列表
     *
     * @param reqDTO
     * @return
     */
    @RequestMapping("/easeDevelopmentfindList")
    public GenericRspDTO<DemandEaseDevelopmentRspDTO> easeDevelopmentfindList(@RequestBody DemandEaseDevelopmentReqDTO reqDTO) {
        if((reqDTO.getStartTime() != null && !reqDTO.getStartTime().equals(""))&&(reqDTO.getEndTime()==null || reqDTO.getEndTime().equals(""))){
            reqDTO.setAcceptancedate(reqDTO.getStartTime());
        }
        if((reqDTO.getStartTime() == null|| reqDTO.getStartTime().equals(""))&&(reqDTO.getEndTime()!=null && !reqDTO.getEndTime().equals(""))){
            reqDTO.setAcceptancedate(reqDTO.getEndTime());
        }
        DemandEaseDevelopmentBO easeDevelopmentBO = BeanUtils.copyPropertiesReturnDest(new DemandEaseDevelopmentBO(), reqDTO);
        DemandEaseDevelopmentRspBO easeDevelopmentRspBO = reqTaskService.easeDevelopmentfindList(easeDevelopmentBO);
        DemandEaseDevelopmentRspDTO rspDTO = new DemandEaseDevelopmentRspDTO();
        rspDTO.setDemandEaseDevelopmentDTOList(BeanConvertUtils.convertList(easeDevelopmentRspBO.getDemandEaseDevelopmentBOList(), DemandEaseDevelopmentDTO.class));
        rspDTO.setPageNum(easeDevelopmentRspBO.getPageInfo().getPageNum());
        rspDTO.setPages(easeDevelopmentRspBO.getPageInfo().getPages());
        rspDTO.setTotal(easeDevelopmentRspBO.getPageInfo().getTotal());
        rspDTO.setPageSize(easeDevelopmentRspBO.getPageInfo().getPageSize());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, rspDTO);
    }

    /**
     * 支撑工作量导入
     *
     * @return
     */
    @PostMapping("/easeDevelopmentExcel")
    public GenericRspDTO<NoBody> easeDevelopmentDown(HttpServletRequest request, GenericDTO<NoBody> req) {
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile(FILE);
        reqTaskService.easeDevelopmentDown(file);
        return GenericRspDTO.newSuccessInstance();
    }

    /**
     * 支撑工作量导出
     *
     * @return
     * @throws IOException
     */
    @RequestMapping("/download")
    public GenericRspDTO<NoBody> download(@RequestBody DemandEaseDevelopmentReqDTO reqDTO, HttpServletResponse response) {
        if((reqDTO.getStartTime() != null && !reqDTO.getStartTime().equals(""))&&(reqDTO.getEndTime()==null || reqDTO.getEndTime().equals(""))){
            reqDTO.setAcceptancedate(reqDTO.getStartTime());
        }
        if((reqDTO.getStartTime() == null|| reqDTO.getStartTime().equals(""))&&(reqDTO.getEndTime()!=null && !reqDTO.getEndTime().equals(""))){
            reqDTO.setAcceptancedate(reqDTO.getEndTime());
        }
        DemandEaseDevelopmentBO easeDevelopmentBO = BeanUtils.copyPropertiesReturnDest(new DemandEaseDevelopmentBO(), reqDTO);
        reqTaskService.getDownload(response, easeDevelopmentBO);
        return GenericRspDTO.newSuccessInstance();
    }

    // 各一级部门支撑工作量月统计明细报表导出
    @RequestMapping("/easeDevelopmentWorkloadCountForDevp")
    public GenericRspDTO<NoBody> easeDevelopmentWorkloadCountForDevp(@RequestBody SupportWorkloadReqDTO reqDTO,HttpServletRequest request,HttpServletResponse response) {
        DemandEaseDevelopmentBO easeDevelopmentBO = BeanUtils.copyPropertiesReturnDest(new DemandEaseDevelopmentBO(), reqDTO);
        if (StringUtils.isBlank(easeDevelopmentBO.getReqImplMon())) {
            easeDevelopmentBO.setReqImplMon(DateUtil.date2String(new Date(), "yyyy-MM"));
        }
        reqTaskService.easeDevelopmentWorkloadCountForDevp(request,response, easeDevelopmentBO);
        return GenericRspDTO.newSuccessInstance();
    }
    // 各二级部门支撑工作量月统计明细报表导出
    @RequestMapping("/easeDevelopmentWorkloadCountForDevp2")
    public GenericRspDTO<NoBody> easeDevelopmentWorkloadCountForDevp2(@RequestBody SupportWorkloadReqDTO reqDTO,HttpServletRequest request,HttpServletResponse response) {
        DemandEaseDevelopmentBO easeDevelopmentBO = BeanUtils.copyPropertiesReturnDest(new DemandEaseDevelopmentBO(), reqDTO);
        if (StringUtils.isBlank(easeDevelopmentBO.getReqImplMon())) {
            easeDevelopmentBO.setReqImplMon(DateUtil.date2String(new Date(), "yyyy-MM"));
        }
        reqTaskService.easeDevelopmentWorkloadCountForDevp2(request,response, easeDevelopmentBO);
        return GenericRspDTO.newSuccessInstance();
    }
}
