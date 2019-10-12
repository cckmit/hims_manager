package com.cmpay.lemon.monitor.controller.demand;

import com.cmpay.framework.data.request.GenericDTO;
import com.cmpay.framework.data.response.GenericRspDTO;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.framework.data.NoBody;
import com.cmpay.lemon.monitor.bo.DemandBO;
import com.cmpay.lemon.monitor.bo.DemandRspBO;
import com.cmpay.lemon.monitor.constant.MonitorConstants;
import com.cmpay.lemon.monitor.dto.DemandDTO;
import com.cmpay.lemon.monitor.dto.DemandReqDTO;
import com.cmpay.lemon.monitor.dto.DemandRspDTO;
import com.cmpay.lemon.monitor.dto.UpdateReqStsDTO;
import com.cmpay.lemon.monitor.entity.DemandDO;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.demand.ReqTaskService;
import com.cmpay.lemon.monitor.service.jira.JiraOperationService;
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
    @RequestMapping("/list")
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
    @RequestMapping("/info/{id}")
    public GenericRspDTO<DemandDTO> getInfoById(@PathVariable("reqInnerSeq") String req_inner_seq, GenericDTO<NoBody> req) {
        DemandBO demandBO = reqTaskService.findById(req_inner_seq);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, BeanUtils.copyPropertiesReturnDest(new DemandDTO(), demandBO));
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
}
