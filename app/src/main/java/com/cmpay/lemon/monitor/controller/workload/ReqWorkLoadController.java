package com.cmpay.lemon.monitor.controller.workload;

import com.cmpay.framework.data.request.GenericDTO;
import com.cmpay.framework.data.response.GenericRspDTO;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.common.utils.StringUtils;
import com.cmpay.lemon.framework.data.NoBody;
import com.cmpay.lemon.monitor.bo.DemandBO;
import com.cmpay.lemon.monitor.bo.DemandRspBO;
import com.cmpay.lemon.monitor.constant.MonitorConstants;
import com.cmpay.lemon.monitor.dto.DemandDTO;
import com.cmpay.lemon.monitor.dto.DemandReqDTO;
import com.cmpay.lemon.monitor.dto.DemandRspDTO;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.demand.ReqPlanService;
import com.cmpay.lemon.monitor.service.workload.ReqWorkLoadService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import com.cmpay.lemon.monitor.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

import static com.cmpay.lemon.monitor.constant.MonitorConstants.FILE;
import static com.cmpay.lemon.monitor.utils.FileUtils.doWrite;

/**
 * @author: zhou_xiong
 * 需求周反馈
 */
@RestController
@RequestMapping(value = MonitorConstants.REQWORK_PATH)
public class ReqWorkLoadController {

    @Autowired
    private ReqWorkLoadService reqWorkLoadService;

    /**
     * 分页需求列表
     *
     * @param reqDTO
     * @return
     */
    @RequestMapping("/list")
    public GenericRspDTO<DemandRspDTO> getUserInfoPage(@RequestBody DemandReqDTO reqDTO) {
        System.out.println("工作量查询");
        DemandBO demandBO = BeanUtils.copyPropertiesReturnDest(new DemandBO(), reqDTO);
        DemandRspBO demandRspBO = reqWorkLoadService.findDemand(demandBO);
        DemandRspDTO rspDTO = new DemandRspDTO();
        rspDTO.setDemandDTOList(BeanConvertUtils.convertList(demandRspBO.getDemandBOList(), DemandDTO.class));
        rspDTO.setPageNum(demandRspBO.getPageInfo().getPageNum());
        rspDTO.setPages(demandRspBO.getPageInfo().getPages());
        rspDTO.setTotal(demandRspBO.getPageInfo().getTotal());
        rspDTO.setPageSize(demandRspBO.getPageInfo().getPageSize());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, rspDTO);
    }

    /**
     * 存量变更
     *
     * @return
     */
    @RequestMapping("/changeReq")
    public GenericRspDTO changeReq(@RequestBody DemandReqDTO reqDTO) {
        System.out.println(reqDTO.getReqImplMon());
        reqWorkLoadService.changeReq(reqDTO.getReqImplMon());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }
    /**
     * 模板下载
     *
     * @return
     * @throws IOException
     */
    @PostMapping("/template/download")
    public GenericRspDTO<NoBody> downloadTmp(GenericDTO<NoBody> req, HttpServletResponse response) {
        doWrite("static/workLoad.xlsx", response);
        return GenericRspDTO.newSuccessInstance();
    }
    /**
     * 工作量导入
     *
     * @return
     */
    @PostMapping("/batch/import")
    public GenericRspDTO<NoBody> batchImport(HttpServletRequest request, GenericDTO<NoBody> req) {
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile(FILE);
        reqWorkLoadService.doBatchImport(file);
        return GenericRspDTO.newSuccessInstance();
    }
    // 基地工作量导出
    @RequestMapping("/goExportForJd")
    public void goExportForJd(@RequestBody DemandReqDTO reqDTO,HttpServletRequest request,HttpServletResponse response) {
        DemandBO demandBO = new DemandBO();
        BeanConvertUtils.convert(demandBO, reqDTO);
        if (StringUtils.isBlank(demandBO.getReqImplMon())) {
            demandBO.setReqImplMon(DateUtil.date2String(new Date(), "yyyy-MM"));
        }
        reqWorkLoadService.exportExcel(request,response, demandBO, "1", "基地工作量");
    }
    // 各部门工作量月统计明细报表导出
    @RequestMapping("/goExportDetlForDevp")
    public void goExportDetlForDevp(@RequestBody DemandReqDTO reqDTO,HttpServletRequest request,HttpServletResponse response) {
        DemandBO demandBO = new DemandBO();
        BeanConvertUtils.convert(demandBO, reqDTO);
        if (StringUtils.isBlank(demandBO.getReqImplMon())) {
            demandBO.setReqImplMon(DateUtil.date2String(new Date(), "yyyy-MM"));
        }
        reqWorkLoadService.exportExcel(request,response, demandBO, "2", "基地工作量");
    }
    // 各部门工作量月统计明细报表导出
    @RequestMapping("/goExportCountForDevp")
    public void goExportCountForDevp(@RequestBody DemandReqDTO reqDTO,HttpServletRequest request,HttpServletResponse response) {
        DemandBO demandBO = new DemandBO();
        BeanConvertUtils.convert(demandBO, reqDTO);
        if (StringUtils.isBlank(demandBO.getReqImplMon())) {
            demandBO.setReqImplMon(DateUtil.date2String(new Date(), "yyyy-MM"));
        }
        reqWorkLoadService.exportExcel(request,response, demandBO, "3", "基地工作量");
    }
}
