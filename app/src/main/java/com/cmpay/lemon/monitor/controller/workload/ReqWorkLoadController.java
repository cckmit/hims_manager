package com.cmpay.lemon.monitor.controller.workload;

import com.cmpay.framework.data.request.GenericDTO;
import com.cmpay.framework.data.response.GenericRspDTO;
import com.cmpay.lemon.common.exception.BusinessException;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.common.utils.StringUtils;
import com.cmpay.lemon.framework.data.NoBody;
import com.cmpay.lemon.monitor.bo.DemandBO;
import com.cmpay.lemon.monitor.bo.DemandRspBO;
import com.cmpay.lemon.monitor.bo.DictionaryBO;
import com.cmpay.lemon.monitor.constant.MonitorConstants;
import com.cmpay.lemon.monitor.dto.*;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    private ReqPlanService reqPlanService;
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
    public GenericRspDTO<NoBody> goExportCountForDevp(@RequestBody DemandReqDTO reqDTO,HttpServletRequest request,HttpServletResponse response) {
        DemandBO demandBO = new DemandBO();
        BeanConvertUtils.convert(demandBO, reqDTO);
        if (StringUtils.isBlank(demandBO.getReqImplMon())) {
            demandBO.setReqImplMon(DateUtil.date2String(new Date(), "yyyy-MM"));
        }
        reqWorkLoadService.exportExcel(request,response, demandBO, "3", "基地工作量");
        return GenericRspDTO.newSuccessInstance();
    }

    /**
     * 查询主导部门工作量占比
     *
     */
    @GetMapping("/getPerDeptWorkLoad1")
    public GenericRspDTO<DictionaryRspDTO> getPerDeptWorkLoad1(@RequestParam("reqInnerSeq") String reqInnerSeq, GenericDTO<NoBody> req) {
        DemandBO bean = reqPlanService.findById(reqInnerSeq);
        DictionaryBO dictionaryBO = new DictionaryBO();
        String leadDeptPro = bean.getLeadDeptPro();
        List<DictionaryBO> leadDeptWorkLoads = new ArrayList<>();
        if (StringUtils.isNotEmpty(leadDeptPro)) {
            String leadDepts[] = leadDeptPro.split(";");
            for (int i = 0; i < leadDepts.length; i++) {
                if (StringUtils.isNotEmpty(leadDepts[i])) {
                    String perLeadDept[] = leadDepts[i].split(":");
                    if (perLeadDept.length == 2) {
                        DictionaryBO leadDeptWorkLoad = new DictionaryBO();
                        leadDeptWorkLoad.setDeptName(perLeadDept[0]);
                        leadDeptWorkLoad.setDeptRate(perLeadDept[1].replaceAll("%", ""));
                        leadDeptWorkLoads.add(leadDeptWorkLoad);
                    }
                }
            }
        } else {
            //判断配合部门是否为空
            String leadPdet = bean.getDevpLeadDept();
            if (StringUtils.isNotEmpty(leadPdet)) {
                if (StringUtils.isEmpty(bean.getDevpCoorDept())){
                    DictionaryBO coDeptWorkLoad = new DictionaryBO();
                    coDeptWorkLoad.setDeptName(leadPdet);
                    coDeptWorkLoad.setDeptRate("100");
                    leadDeptWorkLoads.add(coDeptWorkLoad);
                    if (bean.getTotalWorkload() == 0 || "".equals(bean.getTotalWorkload())){
                        bean.setLeadDeptWorkload(leadPdet+":0.00;");
                    }else {
                        bean.setLeadDeptWorkload(leadPdet+":"+String.format("%.2f",Double.valueOf(bean.getTotalWorkload()))+";");
                    }
                }else{
                    DictionaryBO coDeptWorkLoad = new DictionaryBO();
                    coDeptWorkLoad.setDeptName(leadPdet);
                    leadDeptWorkLoads.add(coDeptWorkLoad);
                    //model.addAttribute("leadDeptWorkLoads", leadDeptWorkLoads);
                }
            }
        }
        List<DictionaryBO> dictionaryBOList = leadDeptWorkLoads;
        DictionaryRspDTO dictionaryRspDTO = new DictionaryRspDTO();
        dictionaryRspDTO.setDictionaryDTOList(BeanConvertUtils.convertList(dictionaryBOList, DictionaryDTO.class));
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, dictionaryRspDTO);
    }
    /**
     * 查询配合部门工作量占比
     *
     */
    @GetMapping("/getPerDeptWorkLoad2")
    public GenericRspDTO<DictionaryRspDTO> getPerDeptWorkLoad2(@RequestParam("reqInnerSeq") String reqInnerSeq, GenericDTO<NoBody> req) {
        DemandBO bean = reqPlanService.findById(reqInnerSeq);
        DictionaryBO dictionaryBO = new DictionaryBO();
        String leadDeptPro = bean.getLeadDeptPro();
        List<DictionaryBO> leadDeptWorkLoads = new ArrayList<>();
        String coorDeptPro = bean.getCoorDeptPro();
        List<DictionaryBO> coorDeptWorkLoads = new ArrayList<>();
        if (StringUtils.isNotEmpty(coorDeptPro)) {
            String coorDepts[] = coorDeptPro.split(";");
            for (int i = 0; i < coorDepts.length; i++) {
                if (StringUtils.isNotEmpty(coorDepts[i])) {
                    String perCoorDept[] = coorDepts[i].split(":");
                    if (perCoorDept.length == 2) {
                        DictionaryBO coDeptWorkLoad = new DictionaryBO();
                        coDeptWorkLoad.setDeptName(perCoorDept[0]);
                        coDeptWorkLoad.setDeptRate(perCoorDept[1].replaceAll("%", ""));
                        coorDeptWorkLoads.add(coDeptWorkLoad);
                    }
                }
            }
        } else {
            String coorDept = bean.getDevpCoorDept();
            if (StringUtils.isNotEmpty(coorDept)) {
                String coorDepts[] = coorDept.split(",");
                for (int i = 0; i < coorDepts.length; i++) {
                    DictionaryBO coDeptWorkLoad = new DictionaryBO();
                    coDeptWorkLoad.setDeptName(coorDepts[i]);
                    coorDeptWorkLoads.add(coDeptWorkLoad);
                }
            }
        }

        List<DictionaryBO> dictionaryBOList = coorDeptWorkLoads;
        DictionaryRspDTO dictionaryRspDTO = new DictionaryRspDTO();
        dictionaryRspDTO.setDictionaryDTOList(BeanConvertUtils.convertList(dictionaryBOList, DictionaryDTO.class));
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, dictionaryRspDTO);
    }
    @RequestMapping("/checkDeptRate")
    public GenericRspDTO<WorkLoadRspDTO> checkDeptRate(@RequestBody DemandReqDTO reqDTO) {
        DemandBO demandBO = BeanUtils.copyPropertiesReturnDest(new DemandBO(), reqDTO);
        Map<String, String> map =reqWorkLoadService.checkDeptRate1(demandBO);
        if(StringUtils.isNotEmpty(map.get("message"))){
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo(MsgEnum.ERROR_WORK_IMPORT.getMsgInfo() + map.get("message"));
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        WorkLoadRspDTO rspDTO = new WorkLoadRspDTO();
        rspDTO.setRemainWorkload(Integer.parseInt(map.get("remainWordkLoad")));
        rspDTO.setLeadDeptWorkload(map.get("leadDpetWorkLoad"));
        rspDTO.setCoorDeptWorkload(map.get("coorDpetWorkLoad"));
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, rspDTO);
    }
    /**
     * 更新需求信息
     *
     * @param
     * @return
     */
    @RequestMapping("/update")
    public GenericRspDTO updateReqWorkLoad(@RequestBody DemandDTO demandDTO) {
        DemandBO demandBO = BeanUtils.copyPropertiesReturnDest(new DemandBO(), demandDTO);
        reqWorkLoadService.update(demandBO);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }
}
