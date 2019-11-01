package com.cmpay.lemon.monitor.controller.production;


import com.cmpay.framework.data.request.GenericDTO;
import com.cmpay.framework.data.response.GenericRspDTO;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.framework.data.NoBody;
import com.cmpay.lemon.monitor.bo.*;
import com.cmpay.lemon.monitor.constant.MonitorConstants;
import com.cmpay.lemon.monitor.dto.*;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.production.OperationProductionService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;

import static com.cmpay.lemon.monitor.constant.MonitorConstants.FILE;


@RestController
@RequestMapping(value = MonitorConstants.Production_PATH)
public class OperationProductionController {
    @Autowired
    private OperationProductionService operationProductionService;

    /**
     * 分页需求列表
     *
     * @param reqDTO
     * @return
     */
    @RequestMapping("/list")
    public GenericRspDTO<ProductionConditionRspDTO> getUserInfoPage(@RequestBody ProductionConditionReqDTO reqDTO) {
        if(reqDTO.getProDateStart()!=null && !reqDTO.getProDateStart().equals("") && (reqDTO.getProDateEnd()==null || reqDTO.getProDateEnd().equals(""))){
            reqDTO.setProDate(reqDTO.getProDateStart());
        }
        if(reqDTO.getProDateEnd()!=null && !reqDTO.getProDateEnd().equals("") && (reqDTO.getProDateStart()==null || reqDTO.getProDateStart().equals(""))){
            reqDTO.setProDate(reqDTO.getProDateEnd());
        }
        ProductionBO productionBO = BeanUtils.copyPropertiesReturnDest(new ProductionBO(), reqDTO);
        ProductionRspBO productionRspBO = operationProductionService.find(productionBO);
        ProductionConditionRspDTO rspDTO = new ProductionConditionRspDTO();
        rspDTO.setProductionList(BeanConvertUtils.convertList(productionRspBO.getProductionList(), ProductionDTO.class));
        rspDTO.setPageNum(productionRspBO.getPageInfo().getPageNum());
        rspDTO.setPages(productionRspBO.getPageInfo().getPages());
        rspDTO.setTotal(productionRspBO.getPageInfo().getTotal());
        rspDTO.setPageSize(productionRspBO.getPageInfo().getPageSize());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, rspDTO);
    }

    @RequestMapping("/audit")
    public GenericRspDTO<ScheduleRspDTO> audit(@RequestBody ScheduleReqDTO reqDTO) {
        ScheduleBO scheduleBO = BeanUtils.copyPropertiesReturnDest(new ScheduleBO(), reqDTO);
        ScheduleRspBO productionRspBO = operationProductionService.find1(scheduleBO);
        ScheduleRspDTO rspDTO = new ScheduleRspDTO();
        rspDTO.setDemandDTOList(BeanConvertUtils.convertList(productionRspBO.getScheduleList(), ScheduleDTO.class));
        rspDTO.setPageNum(productionRspBO.getPageInfo().getPageNum());
        rspDTO.setPages(productionRspBO.getPageInfo().getPages());
        rspDTO.setTotal(productionRspBO.getPageInfo().getTotal());
        rspDTO.setPageSize(productionRspBO.getPageInfo().getPageSize());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, rspDTO);
    }
    @RequestMapping("/doProductionDetailDownload")
    public void doProductionDetailDownload(@RequestParam("taskIdStr") String taskIdStr, HttpServletRequest request, HttpServletResponse response)throws Exception{
        operationProductionService.doProductionDetailDownload(request,response,taskIdStr);
    }
    // 基地工作量导出
    @RequestMapping("/productionOut")
    public void exportExcel(@RequestBody ProductionConditionReqDTO reqDTO, HttpServletRequest request, HttpServletResponse response) {
        ProductionBO productionBO = BeanUtils.copyPropertiesReturnDest(new ProductionBO(), reqDTO);
        System.err.println(productionBO);
        operationProductionService.exportExcel(request,response,productionBO);
    }
    @RequestMapping("/updateAllProduction")
    public GenericRspDTO<NoBody> updateAllProduction(@RequestParam("taskIdStr") String taskIdStr, HttpServletRequest request, HttpServletResponse response){
        System.err.println(taskIdStr);
        operationProductionService.updateAllProduction(request,response,taskIdStr);
        return GenericRspDTO.newSuccessInstance();
    }
    // 投产清单通报
    @RequestMapping("/sendGoExport")
    public GenericRspDTO<NoBody> sendGoExport(@RequestParam("taskIdStr") String taskIdStr, HttpServletRequest request, HttpServletResponse response){
        operationProductionService.sendGoExport(request,response,taskIdStr);
        return GenericRspDTO.newSuccessInstance();
    }
    // 投产结果通报
    @RequestMapping("/sendGoExportResult")
    public GenericRspDTO<NoBody> sendGoExportResult(@RequestParam("taskIdStr") String taskIdStr, HttpServletRequest request, HttpServletResponse response){
        operationProductionService.sendGoExportResult(request,response,taskIdStr);
        return GenericRspDTO.newSuccessInstance();
    }

    // 投产包检查
    @RequestMapping("/proPkgCheck")
    public GenericRspDTO proPkgCheck(@RequestParam("taskIdStr") String taskIdStr, HttpServletRequest request, HttpServletResponse response){
        String result = operationProductionService.proPkgCheck(request,response,taskIdStr);
        MsgEnum.SUCCESS.setMsgInfo("");
        MsgEnum.SUCCESS.setMsgInfo(result);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }

    //获取邮件组
    @RequestMapping("/mailGroupSearch")
    public GenericRspDTO<MailGroupSearchRspDTO> productionInput( @RequestBody MailGroupSearchReqDTO req , HttpServletRequest request, HttpServletResponse response){
        MailGroupBO mailGroupBO = BeanUtils.copyPropertiesReturnDest(new MailGroupBO(), req);
        MailGroupSearchRspDTO mailGroupSearchRspDTO = new MailGroupSearchRspDTO();
        List<MailGroupBO> lst =operationProductionService.searchMailGroupList(mailGroupBO);
        LinkedList<MailGroupDTO> mailGroupDTOList = new LinkedList<>();
        lst.forEach(m->{
            mailGroupDTOList.add(BeanUtils.copyPropertiesReturnDest(new MailGroupDTO(),m));
        });
        mailGroupSearchRspDTO.setMailGroupBOList(mailGroupDTOList);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS,mailGroupSearchRspDTO);
    }


    //保存全部投产记录
    @RequestMapping(value = "/productionInput", method = RequestMethod.POST)
    public GenericRspDTO<NoBody>  productionInput( ProductionInputReqDTO reqDTO,HttpServletRequest request) {
        MultipartFile file = null;
        String isApproveProduct1 = request.getParameter("isApproveProduct");
        Boolean isApproveProduct = false;
        if (isApproveProduct1 != null && isApproveProduct1.equals("true")) {
            isApproveProduct = true;
        }
        if(reqDTO.getAttachment()) {
            List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
            file = files.get(0);
        }
        ProductionBO bean = new ProductionBO();
        BeanUtils.copyPropertiesReturnDest(bean,reqDTO);
        MsgEnum msgEnum = operationProductionService.productionInput(file, isApproveProduct, bean);

        return GenericRspDTO.newInstance(msgEnum);
    }

    /**
     * 导入
     *
     * @return
     */
    @PostMapping("/batch/import")
    public GenericRspDTO<NoBody> batchImport(HttpServletRequest request, GenericDTO<NoBody> req) {
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile(FILE);
        String reqNumber = request.getParameter("proNumber");
        operationProductionService.doBatchImport(file,reqNumber);
        return GenericRspDTO.newSuccessInstance();
    }

    /**
     * 问题录入前查询
     */
    @PostMapping("/getQuestion")
    public GenericRspDTO<GetQuestionRsqDTO> getQuestion(@RequestBody GetQuestionReqDTO getQuestionReqDTO) {
        String proNumber = getQuestionReqDTO.getProNumber();
        List<ProblemBO> problemBOS = operationProductionService.findProblemInfo(proNumber);
        List<ProblemDTO> problemDTOS = new LinkedList<>();
        problemBOS.forEach(m ->
                {
                    problemDTOS.add(BeanUtils.copyPropertiesReturnDest(new ProblemDTO(), m));
                }
        );
        GetQuestionRsqDTO getQuestionRsqDTO = new GetQuestionRsqDTO();
        getQuestionRsqDTO.setProblemDTO(problemDTOS);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, getQuestionRsqDTO);
    }

    /**
     * 问题录入
     */
    @PostMapping("questionInput")
    public GenericRspDTO<NoBody> questionInput(@RequestBody QuestionInputReqDTO questionInputReqDTO) {

        QuestionInputReqBO questionInputReqBO = BeanUtils.copyPropertiesReturnDest(new QuestionInputReqBO(), questionInputReqDTO);
        operationProductionService.questionInput(questionInputReqBO);

        return GenericRspDTO.newSuccessInstance();
    }
}
