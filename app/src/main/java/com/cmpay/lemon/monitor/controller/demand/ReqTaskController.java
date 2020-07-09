package com.cmpay.lemon.monitor.controller.demand;

import com.cmpay.framework.data.request.GenericDTO;
import com.cmpay.framework.data.response.GenericRspDTO;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.framework.data.NoBody;
import com.cmpay.lemon.monitor.bo.*;
import com.cmpay.lemon.monitor.constant.MonitorConstants;
import com.cmpay.lemon.monitor.dto.*;
import com.cmpay.lemon.monitor.entity.DemandDO;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.demand.ReqTaskService;
import com.cmpay.lemon.monitor.service.jira.JiraOperationService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

@Api(tags = "需求任务")
@RestController
@RequestMapping(value = MonitorConstants.REQTASK_PATH)
public class ReqTaskController {

    @Autowired
    private ReqTaskService reqTaskService;
    @Autowired
    private JiraOperationService jiraOperationService;
    /**
     * 分页需求列表
     *
     * @param reqDTO
     * @return
     */
    @ApiOperation(value = "分页需求列表" )
    @RequestMapping(value = "/list" ,method = RequestMethod.POST)
    public GenericRspDTO<DemandRspDTO> findAll(@RequestBody DemandReqDTO reqDTO) {
        DemandBO demandBO = BeanUtils.copyPropertiesReturnDest(new DemandBO(), reqDTO);
        DemandRspBO demandRspBO = reqTaskService.find(demandBO);
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
    @RequestMapping("/info")
    public GenericRspDTO<DemandDTO> getInfoById(@RequestBody DemandReqDTO reqDTO) {
        DemandBO demandBO = reqTaskService.findById(reqDTO.getReqInnerSeq());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, BeanUtils.copyPropertiesReturnDest(new DemandDTO(), demandBO));
    }

    /**
     * 查询需求信息
     *
     * @return
     */
    @RequestMapping("/getTimeAxisData")
    public GenericRspDTO<TimeAxisDataDTO> getTimeAxisData(@RequestBody DemandReqDTO reqDTO) {
        TimeAxisDataBO timeAxisData = reqTaskService.getTimeAxisData(reqDTO.getReqInnerSeq());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, BeanUtils.copyPropertiesReturnDest(new TimeAxisDataDTO(), timeAxisData));
    }

    /**
     * 添加需求信息
     *
     * @param
     * @return
     */
    @RequestMapping("/add")
    public GenericRspDTO add(@RequestBody DemandDTO demandDTO) {
        DemandBO demandBO = BeanUtils.copyPropertiesReturnDest(new DemandBO(), demandDTO);
        reqTaskService.add(demandBO);
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
        reqTaskService.update(demandBO);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }

    /**
     * 更新需求信息--需求状态
     *
     * @param
     * @return
     */
    @RequestMapping("/updateReqSts")
    public GenericRspDTO updateReqSts(@RequestBody UpdateReqStsDTO updateReqStsDTO) {
        String reqInnerSeq = updateReqStsDTO.getReqInnerSeq();
        String reqSts = updateReqStsDTO.getReqSts();
        String reqStsRemarks = updateReqStsDTO.getReqStsRemarks();
        String reqNm = updateReqStsDTO.getReqNm();
        String reqNo = updateReqStsDTO.getReqNo();
        reqTaskService.updateReqSts(reqInnerSeq,reqNo,reqSts,reqStsRemarks,reqNm);

        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }

    /**
     * 删除/批量删除需求信息
     *
     * @param reqDTO
     * @return
     */
    @DeleteMapping("/delete")
    public GenericRspDTO delete(@RequestBody DemandReqDTO reqDTO) {
        reqTaskService.deleteBatch(reqDTO.getIds());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }

    @PostMapping("/createJiraEpic")
    public GenericRspDTO createJiraEpic(@RequestBody DemandReqDTO reqDTO) {
        List<String> ids = reqDTO.getIds();
        List<DemandDO> demandDOList = reqTaskService.findById(ids);
        jiraOperationService.batchCreateEpic(demandDOList);
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
        doWrite("static/reqTask.xlsm", response);
        return GenericRspDTO.newSuccessInstance();
    }

    /**
     * 导出
     *
     * @return
     * @throws IOException
     */
    @RequestMapping("/download")
    public GenericRspDTO<NoBody> download(@RequestBody DemandReqDTO reqDTO, HttpServletResponse response) {
        DemandBO demandBO = BeanUtils.copyPropertiesReturnDest(new DemandBO(), reqDTO);
        reqTaskService.getReqTask(response, demandBO);
        return GenericRspDTO.newSuccessInstance();
    }

    /**
     * 项目文档下载
     *
     * @return
     * @throws IOException
     */
    @RequestMapping("/downloadProFile")
    public GenericRspDTO<NoBody> downloadProFile(GenericDTO<NoBody> req, HttpServletRequest request, HttpServletResponse response) {
        reqTaskService.doBatchDown((MultipartHttpServletRequest) request, response);
        return GenericRspDTO.newSuccessInstance();
    }


    /**
     * 导入
     *
     * @return
     */
    @PostMapping("/batch/import")
    public GenericRspDTO<NoBody> batchImport(HttpServletRequest request, GenericDTO<NoBody> req) {
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile(FILE);
        reqTaskService.doBatchImport(file);
        return GenericRspDTO.newSuccessInstance();
    }
    /**
     *获取上传文档列表
     */
    @RequestMapping("/lists")
    public GenericRspDTO<ReqIndexCountRspDTO> lists(@RequestBody DemandReqDTO reqDTO) {
        DemandBO demandBO = BeanUtils.copyPropertiesReturnDest(new DemandBO(), reqDTO);
        List<String> list  = reqTaskService.lists( demandBO);
        ReqIndexCountRspDTO rspDTO = new ReqIndexCountRspDTO();
        rspDTO.setLi(list);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, rspDTO);
    }

    /**
     *需求状态修改明细
     */
    @PostMapping("/demandTaskStatusBreakdown")
    public GenericRspDTO<DemandStateHistoryRspDTO> demandTaskStatusBreakdown (@RequestBody DemandChangeDetailsReqDTO req) {
        DemandStateHistoryRspDTO rspDTO = new DemandStateHistoryRspDTO();
        DemandChangeDetailsBO demandChangeDetailsBO = BeanUtils.copyPropertiesReturnDest(new DemandChangeDetailsBO(), req);
        DemandStateHistoryRspBO demandStateHistoryRspBO = reqTaskService.findDemandChangeDetails(demandChangeDetailsBO);
        rspDTO.setDemandStateHistoryDTOList(BeanConvertUtils.convertList(demandStateHistoryRspBO.getDemandStateHistoryBOList(), DemandStateHistoryDTO.class));
        rspDTO.setPageNum(demandStateHistoryRspBO.getPageInfo().getPageNum());
        rspDTO.setPages(demandStateHistoryRspBO.getPageInfo().getPages());
        rspDTO.setTotal(demandStateHistoryRspBO.getPageInfo().getTotal());
        rspDTO.setPageSize(demandStateHistoryRspBO.getPageInfo().getPageSize());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, rspDTO);
    }

    /**
     *需求阶段变更明细
     */
    @PostMapping("/detailsOfChangesInTheDemandPhase")
    public GenericRspDTO<DemandCurperiodHistoryRspDTO> detailsOfChangesInTheDemandPhase (@RequestBody DemandChangeDetailsReqDTO req) {
        DemandCurperiodHistoryRspDTO rspDTO = new DemandCurperiodHistoryRspDTO();
        DemandChangeDetailsBO demandChangeDetailsBO = BeanUtils.copyPropertiesReturnDest(new DemandChangeDetailsBO(), req);
        DemandCurperiodHistoryRspBO demandCurperiodHistoryRspBO = reqTaskService.findDemandCurperiodDetails(demandChangeDetailsBO);
        rspDTO.setDemandCurperiodHistoryDTOList(BeanConvertUtils.convertList(demandCurperiodHistoryRspBO.getDemandCurperiodHistoryBOList(), DemandCurperiodHistoryDTO.class));
        rspDTO.setPageNum(demandCurperiodHistoryRspBO.getPageInfo().getPageNum());
        rspDTO.setPages(demandCurperiodHistoryRspBO.getPageInfo().getPages());
        rspDTO.setTotal(demandCurperiodHistoryRspBO.getPageInfo().getTotal());
        rspDTO.setPageSize(demandCurperiodHistoryRspBO.getPageInfo().getPageSize());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, rspDTO);
    }
    /**
     *测试主任务批量修改
     */
    @PostMapping("/jiraTestMainTaskUpload")
    public GenericRspDTO<NoBody> jiraTestMainTaskUpload(HttpServletRequest request, GenericDTO<NoBody> req) {
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile(FILE);
        jiraOperationService.jiraTestMainTaskBatchEdit(file);
        return GenericRspDTO.newSuccessInstance();
    }
    /**
     * 文档上传接收文档
     *
     * @return
     */
    @PostMapping("/approval/process")
    public GenericRspDTO approvalProcess(HttpServletRequest request, GenericDTO<NoBody> req) {
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile(FILE);
        String ids = request.getParameter("ids");
        System.err.println(ids + "==="+file.getOriginalFilename());
        reqTaskService.approvalProcess(file,ids);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }

    /**
     * 查看金科领导审批照片
     *
     * @return
     */
    @PostMapping("/approval/findOne")
    public GenericRspDTO approvalFindOne(@RequestBody DemandReqDTO reqDTO) {
        String reqInnerSeq = reqDTO.getReqInnerSeq();
        String month = reqDTO.getReqImplMon();
        System.err.println(reqInnerSeq);
        System.err.println(month);
        DemandBO demandBO = reqTaskService.approvalFindOne(reqInnerSeq,month);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, BeanUtils.copyPropertiesReturnDest(new DemandDTO(), demandBO));
    }
    /**
     *需求名称编号变更明细
     */
    @PostMapping("/numberNameChangeDetail")
    public GenericRspDTO<DemandNameChangeRspDTO> numberNameChangeDetail (@RequestBody DemandNameChangeDTO req) {
        DemandNameChangeBO demandNameChangeBO = BeanUtils.copyPropertiesReturnDest(new DemandNameChangeBO(), req);
        DemandnNameChangeRspBO demandnNameChangeRspBO = reqTaskService.numberNameChangeDetail(demandNameChangeBO);
        DemandNameChangeRspDTO rspDTO = new DemandNameChangeRspDTO();
        rspDTO.setDemandDTOList(BeanConvertUtils.convertList(demandnNameChangeRspBO.getDemandNameChangeBOS(), DemandNameChangeDTO.class));
        rspDTO.setPageNum(demandnNameChangeRspBO.getPageInfo().getPageNum());
        rspDTO.setPages(demandnNameChangeRspBO.getPageInfo().getPages());
        rspDTO.setTotal(demandnNameChangeRspBO.getPageInfo().getTotal());
        rspDTO.setPageSize(demandnNameChangeRspBO.getPageInfo().getPageSize());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, rspDTO);
    }
    /**
     * 生产缺陷导入
     */
    @PostMapping("/productionDefectIntroduction")
    public GenericRspDTO<NoBody> productionDefectIntroduction(HttpServletRequest request, GenericDTO<NoBody> req) {
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile(FILE);
        reqTaskService.productionDefectIntroduction(file);
        return GenericRspDTO.newSuccessInstance();
    }

    /**
     * 冒烟测试不通过登登记
     */
    @PostMapping("/smokeTestRegistration")
    public GenericRspDTO<NoBody> smokeTestRegistration(@RequestBody SmokeTestRegistrationDTO req) {
        SmokeTestRegistrationBO smokeTestRegistrationBO = BeanUtils.copyPropertiesReturnDest(new SmokeTestRegistrationBO(), req);
        reqTaskService.smokeTestRegistration(smokeTestRegistrationBO);
        return GenericRspDTO.newSuccessInstance();
    }
}
