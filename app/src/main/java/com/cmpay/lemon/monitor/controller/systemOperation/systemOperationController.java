package com.cmpay.lemon.monitor.controller.systemOperation;


import com.cmpay.framework.data.response.GenericRspDTO;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.framework.data.NoBody;
import com.cmpay.lemon.monitor.bo.OperationApplicationBO;
import com.cmpay.lemon.monitor.bo.OperationApplicationRspBO;
import com.cmpay.lemon.monitor.constant.MonitorConstants;
import com.cmpay.lemon.monitor.dto.OperationApplicationDTO;
import com.cmpay.lemon.monitor.dto.OperationApplicationReqDTO;
import com.cmpay.lemon.monitor.dto.OperationApplicationRspDTO;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.systemOperation.OperationApplicationService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping(value = MonitorConstants.SYSTEMOPERATION_PATH)
public class systemOperationController {
    @Autowired
    private OperationApplicationService operationApplicationService;

    @RequestMapping("/entry")
    public GenericRspDTO<NoBody> systemOperationEntry(OperationApplicationDTO operationApplicationDTO, HttpServletRequest request) {
        List<MultipartFile> files=null;
        //判断是否带附件
        if(operationApplicationDTO.getAttachment()!=null&&operationApplicationDTO.getAttachment().equals("true")) {
            files = ((MultipartHttpServletRequest) request).getFiles("file");
        }
        OperationApplicationBO operationApplicationBO = BeanUtils.copyPropertiesReturnDest(new OperationApplicationBO(), operationApplicationDTO);

        operationApplicationService.systemOperationEntry(files,operationApplicationBO,request);

        return GenericRspDTO.newInstance(MsgEnum.SUCCESS);
    }
    /**
     * 分页需求列表
     *
     * @param reqDTO
     * @return
     */
    @RequestMapping("/list")
    public GenericRspDTO<OperationApplicationRspDTO> getUserInfoPage(@RequestBody OperationApplicationReqDTO reqDTO) {
        if(reqDTO.getPoDateStart()!=null && !reqDTO.getPoDateStart().equals("") && (reqDTO.getPoDateEnd()==null || reqDTO.getPoDateEnd().equals(""))){
            reqDTO.setProposeDate(reqDTO.getPoDateStart());
        }
        if(reqDTO.getPoDateEnd()!=null && !reqDTO.getPoDateEnd().equals("") && (reqDTO.getPoDateStart()==null || reqDTO.getPoDateStart().equals(""))){
            reqDTO.setProposeDate(reqDTO.getPoDateEnd());
        }
        OperationApplicationBO operationApplicationBO = BeanUtils.copyPropertiesReturnDest(new OperationApplicationBO(), reqDTO);
        OperationApplicationRspBO productionRspBO = operationApplicationService.find(operationApplicationBO);
        OperationApplicationRspDTO rspDTO = new OperationApplicationRspDTO();
        rspDTO.setOperationApplicationList(BeanConvertUtils.convertList(productionRspBO.getProductionList(), OperationApplicationDTO.class));
        rspDTO.setPageNum(productionRspBO.getPageInfo().getPageNum());
        rspDTO.setPages(productionRspBO.getPageInfo().getPages());
        rspDTO.setTotal(productionRspBO.getPageInfo().getTotal());
        rspDTO.setPageSize(productionRspBO.getPageInfo().getPageSize());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, rspDTO);
    }

    @RequestMapping("/doOperationDownload")
    public void doOperationDownload(@RequestParam("taskIdStr") String taskIdStr, HttpServletRequest request, HttpServletResponse response)throws Exception{
        operationApplicationService.doOperationDownload(request,response,taskIdStr);
    }

    @RequestMapping("/doAllOperationDownload")
    public void doAllOperationDownload(@RequestBody OperationApplicationReqDTO reqDTO, HttpServletRequest request, HttpServletResponse response)throws Exception{
        if(reqDTO.getPoDateStart()!=null && !reqDTO.getPoDateStart().equals("") && (reqDTO.getPoDateEnd()==null || reqDTO.getPoDateEnd().equals(""))){
            reqDTO.setProposeDate(reqDTO.getPoDateStart());
        }
        if(reqDTO.getPoDateEnd()!=null && !reqDTO.getPoDateEnd().equals("") && (reqDTO.getPoDateStart()==null || reqDTO.getPoDateStart().equals(""))){
            reqDTO.setProposeDate(reqDTO.getPoDateEnd());
        }
        OperationApplicationBO operationApplicationBO = BeanUtils.copyPropertiesReturnDest(new OperationApplicationBO(), reqDTO);
        operationApplicationService.doAllOperationDownload(request,response,operationApplicationBO);
    }

    @RequestMapping("/updateAllOperationApplication")
    public GenericRspDTO<NoBody> updateAllOperationApplication(@RequestParam("taskIdStr") String taskIdStr, HttpServletRequest request, HttpServletResponse response) {
        System.err.println(taskIdStr);
        operationApplicationService.updateAllOperationApplication(request,response,taskIdStr);
        return GenericRspDTO.newSuccessInstance();
    }

    @RequestMapping("/update")
    public GenericRspDTO<NoBody> systemOperationUpdate(OperationApplicationDTO operationApplicationDTO, HttpServletRequest request) {
        List<MultipartFile> files=null;
        //判断是否带附件
        if(operationApplicationDTO.getAttachment()!=null&&operationApplicationDTO.getAttachment().equals("true")) {
            files = ((MultipartHttpServletRequest) request).getFiles("file");
        }
        OperationApplicationBO operationApplicationBO = BeanUtils.copyPropertiesReturnDest(new OperationApplicationBO(), operationApplicationDTO);
        operationApplicationService.systemOperationUpdate(files,operationApplicationBO,request);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS);
    }
    // 下载投产包
    @RequestMapping("/pkgDownload")
    public GenericRspDTO<NoBody> pkgDownload(@RequestParam("proNumber") String proNumber, HttpServletRequest request, HttpServletResponse response){
        operationApplicationService.pkgDownload(request,response,proNumber);
        return GenericRspDTO.newSuccessInstance();
    }
}
