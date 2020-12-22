package com.cmpay.lemon.monitor.controller.demand;

import com.cmpay.framework.data.request.GenericDTO;
import com.cmpay.framework.data.response.GenericRspDTO;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.framework.data.NoBody;
import com.cmpay.lemon.monitor.bo.*;
import com.cmpay.lemon.monitor.constant.MonitorConstants;
import com.cmpay.lemon.monitor.dto.*;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.demand.ReqPlanService;
import com.cmpay.lemon.monitor.service.dic.DictionaryService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.cmpay.lemon.monitor.constant.MonitorConstants.FILE;
import static com.cmpay.lemon.monitor.utils.FileUtils.doWrite;

/**
 * @author: zhou_xiong
 * 需求计划
 */
@RestController
@RequestMapping(value = MonitorConstants.REQPLAN_PATH)
public class ReqPlanController {

    @Autowired
    private ReqPlanService reqPlanService;
    @Autowired
    private DictionaryService dictionaryService;


    /**
     * 分页需求列表
     *
     * @param reqDTO
     * @return
     */
    @RequestMapping("/list")
    public GenericRspDTO<DemandRspDTO> getUserInfoPage(@RequestBody DemandReqDTO reqDTO) {
        DemandBO demandBO = BeanUtils.copyPropertiesReturnDest(new DemandBO(), reqDTO);
        DemandRspBO demandRspBO = reqPlanService.findDemand(demandBO);
        DemandRspDTO rspDTO = new DemandRspDTO();
        rspDTO.setDemandDTOList(BeanConvertUtils.convertList(demandRspBO.getDemandBOList(), DemandDTO.class));
        rspDTO.setPageNum(demandRspBO.getPageInfo().getPageNum());
        rspDTO.setPages(demandRspBO.getPageInfo().getPages());
        rspDTO.setTotal(demandRspBO.getPageInfo().getTotal());
        rspDTO.setPageSize(demandRspBO.getPageInfo().getPageSize());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, rspDTO);
    }

    /**
     * 查询需求信息
     *
     * @return
     */
    @RequestMapping("/info/{id}")
    public GenericRspDTO<DemandDTO> getUserInfoById(@PathVariable("reqInnerSeq") String req_inner_seq, GenericDTO<NoBody> req) {
        DemandBO demandBO = reqPlanService.findById(req_inner_seq);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, BeanUtils.copyPropertiesReturnDest(new DemandDTO(), demandBO));
    }

    /**
     * 查询所有需求信息
     */
    @RequestMapping("/all")
    public GenericRspDTO<DemandRspDTO> findAll(GenericDTO reqDTO) {
        DemandRspDTO rspDTO = new DemandRspDTO();
        rspDTO.setDemandDTOList(BeanConvertUtils.convertList(reqPlanService.findAll(), DemandDTO.class));
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, rspDTO);
    }

    /**
     * 添加需求信息
     *
     * @param
     * @return
     */
    @RequestMapping("/save")
    public GenericRspDTO add(@RequestBody DemandDTO demandDTO) {
        DemandBO demandBO = BeanUtils.copyPropertiesReturnDest(new DemandBO(), demandDTO);
        reqPlanService.add(demandBO);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }

    /**
     * 删除需求信息
     *
     * @param
     * @return
     */
    @RequestMapping("/delete/{id}")
    public GenericRspDTO delete(@PathVariable("reqInnerSeq") String req_inner_seq, GenericDTO<NoBody> req) {
        reqPlanService.delete(req_inner_seq);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }

    @RequestMapping("/rebooting")
    public GenericRspDTO rebooting(@RequestBody DemandReqDTO reqDTO) {
        reqPlanService.rebooting(reqDTO.getIds());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }
    /**
     * 更新需求信息
     *
     * @param
     * @return
     */
    @RequestMapping("/update")
    public GenericRspDTO update(@RequestBody DemandDTO demandDTO) {

        DemandBO demandBO = BeanUtils.copyPropertiesReturnDest(new DemandBO(), demandDTO);
        reqPlanService.update(demandBO);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }
    /**
     * 项目启动，建立svn项目文档库以及下发项目启动邮件
     *
     * @return
     */
    @RequestMapping("/goProjectStart")
    public GenericRspDTO<ProjectStartRspDTO> goProjectStart(@RequestBody ProjectStartReqDTO reqDTO) {
        ProjectStartBO demandBO = reqPlanService.goProjectStart(reqDTO.getReqInnerSeq());
        ProjectStartRspDTO projectStartRspDTO = new ProjectStartRspDTO();
        projectStartRspDTO.setReqInnerSeq(demandBO.getReqInnerSeq());
        projectStartRspDTO.setReqNm(demandBO.getReqNm());
        projectStartRspDTO.setReqNo(demandBO.getReqNo());
        projectStartRspDTO.setSendTo(demandBO.getSendTo());
        projectStartRspDTO.setCopyTo(demandBO.getCopyTo());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, projectStartRspDTO);
    }
    /**
     * 查询需求计划项目启动信息
     *
     * @return
     */
    @RequestMapping("/projectStart")
    public GenericRspDTO projectStart(@RequestBody ProjectStartReqDTO reqDTO, HttpServletRequest request) {
        // 项目启动邮件
        reqPlanService.projectStart(new ProjectStartBO(reqDTO.getReqInnerSeq(),reqDTO.getReqNo(),reqDTO.getReqNm(),reqDTO.getSendTo(),reqDTO.getCopyTo()),request);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }
    /**
     * 存量变更
     *
     * @return
     */
    @RequestMapping("/changeReq")
    public GenericRspDTO changeReq(@RequestBody DemandReqDTO reqDTO) {
        reqPlanService.changeReq(reqDTO.getReqImplMon());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }

    /**
     * 批量导入模板 下载
     *
     * @return
     * @throws IOException
     */
    @PostMapping("/template/download")
    public GenericRspDTO<NoBody> downloadTmp(GenericDTO<NoBody> req, HttpServletResponse response) {
        doWrite("static/gndDownload.xlsx", response);
        return GenericRspDTO.newSuccessInstance();
    }

    /**
     * 需求文档上传页面跳转
     */
    @RequestMapping("/goUploadFile")
    public GenericRspDTO<ProjectStartRspDTO> goUploadFile(@RequestBody DemandReqDTO reqDTO) {
        ProjectStartBO projectStartBO = reqPlanService.uploadFile(reqDTO.getReqInnerSeq());
        DemandBO demandBO = reqPlanService.findById(reqDTO.getReqInnerSeq());
        ProjectStartRspDTO projectStartRspDTO = new ProjectStartRspDTO();
        projectStartRspDTO.setReqInnerSeq(projectStartBO.getReqInnerSeq());
        projectStartRspDTO.setPreCurPeriod(demandBO.getPreCurPeriod());
        projectStartRspDTO.setReqNm(projectStartBO.getReqNm());
        projectStartRspDTO.setReqNo(projectStartBO.getReqNo());
        projectStartRspDTO.setSendTo(projectStartBO.getSendTo());
        projectStartRspDTO.setCopyTo(projectStartBO.getCopyTo());
        String preCurPeriod = dictionaryService.findFieldName("REQ_PEROID",demandBO.getPreCurPeriod());
        //将需求阶段赋值成下一阶段
        String reqPeriod = reqPlanService.getReqPeriod(preCurPeriod);
        List<DictionaryBO> dictionaryBOList = dictionaryService.findUploadPeriod(reqPeriod);
        DictionaryRspDTO dictionaryRspDTO = new DictionaryRspDTO();
        dictionaryRspDTO.setDictionaryDTOList(BeanConvertUtils.convertList(dictionaryBOList, DictionaryDTO.class));
        projectStartRspDTO.setDictionaryRspDTO(dictionaryRspDTO);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, projectStartRspDTO);
    }
    /**
     * 文档上传
     *
     * @return
     */
    @PostMapping("uploadProjrctFile")
    public GenericRspDTO uploadProjrctFile(@RequestBody ProjectStartReqDTO reqDTO,HttpServletRequest request) {
//        ProjectStartBO ProjectStartBO = new ProjectStartBO();
//        BeanConvertUtils.convert(ProjectStartBO, reqDTO);
//        reqPlanService.uploadProjrctFile(ProjectStartBO,FILES,request);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }

    /**
     * 文档上传接收文档
     *
     * @return
     */
    @PostMapping("/batch/import")
    public GenericRspDTO batchImport(HttpServletRequest request, GenericDTO<NoBody> req) {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        MultipartFile[] FILES = files.toArray(new MultipartFile[files.size()]);
        ProjectStartBO ProjectStartBO = new ProjectStartBO();
        ProjectStartBO.setReqInnerSeq(request.getParameter("reqInnerSeq"));
        ProjectStartBO.setReqNm(request.getParameter("reqNm"));
        ProjectStartBO.setReqNo(request.getParameter("reqNo"));
        ProjectStartBO.setSendTo(request.getParameter("sendTo"));
        ProjectStartBO.setCopyTo(request.getParameter("copyTo"));
        ProjectStartBO.setPreCurPeriod(request.getParameter("preCurPeriod"));
        ProjectStartBO.setUploadPeriod(request.getParameter("uploadPeriod"));
        reqPlanService.uploadProjrctFile(ProjectStartBO,FILES,request);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }

    /**
     *  需求计划导出
     *
     * @return
     * @throws IOException
     */
    @RequestMapping("/download")
    public GenericRspDTO<NoBody> download(@RequestBody DemandReqDTO reqDTO, HttpServletResponse response) {
        DemandBO demandBO = BeanUtils.copyPropertiesReturnDest(new DemandBO(), reqDTO);
        reqPlanService.getReqPlan(response, demandBO);
        return GenericRspDTO.newSuccessInstance();
    }
    /**
     * 需求计划导入
     *i
     * @return
     */
    @PostMapping("/doBatchImport")
    public GenericRspDTO<NoBody> doBatchImport (HttpServletRequest request, GenericDTO<NoBody> req) {
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile(FILE);
        reqPlanService.doBatchImport(file);
        return GenericRspDTO.newSuccessInstance();
    }


    /**
     *需求计划变更明细
     */
    @PostMapping("/detailsOfDemandPlanChanges")
    public GenericRspDTO<DemandTimeFrameHistoryRspDTO> detailsOfDemandPlanChanges (@RequestBody DemandTimeFrameHistoryReqDTO req) {
        DemandTimeFrameHistoryRspDTO rspDTO = new DemandTimeFrameHistoryRspDTO();
        DemandTimeFrameHistoryBO demandTimeFrameHistoryBO = BeanUtils.copyPropertiesReturnDest(new DemandTimeFrameHistoryBO(), req);
        DemandTimeFrameHistoryRspBO demandTimeFrameHistoryRspBO = reqPlanService.findTimeNodeModificationDetails(demandTimeFrameHistoryBO);


        rspDTO.setDemandTimeFrameHistoryDTOList(BeanConvertUtils.convertList(demandTimeFrameHistoryRspBO.getDemandTimeFrameHistoryBOList(), DemandTimeFrameHistoryDTO.class));
        rspDTO.setPageNum(demandTimeFrameHistoryRspBO.getPageInfo().getPageNum());
        rspDTO.setPages(demandTimeFrameHistoryRspBO.getPageInfo().getPages());
        rspDTO.setTotal(demandTimeFrameHistoryRspBO.getPageInfo().getTotal());
        rspDTO.setPageSize(demandTimeFrameHistoryRspBO.getPageInfo().getPageSize());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, rspDTO);
    }

    @RequestMapping("/buildSvn")
    public GenericRspDTO<NoBody> buildSvn(@RequestBody DemandReqDTO reqDTO) {
        DemandBO demandBO = BeanUtils.copyPropertiesReturnDest(new DemandBO(), reqDTO);
        reqPlanService.buildSvn(demandBO);
        return GenericRspDTO.newSuccessInstance();
    }
}
