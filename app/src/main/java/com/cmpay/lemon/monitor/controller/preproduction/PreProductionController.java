package com.cmpay.lemon.monitor.controller.preproduction;


import com.cmpay.framework.data.request.GenericDTO;
import com.cmpay.framework.data.response.GenericRspDTO;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.framework.data.NoBody;
import com.cmpay.lemon.monitor.bo.*;
import com.cmpay.lemon.monitor.constant.MonitorConstants;
import com.cmpay.lemon.monitor.dto.*;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.preproduction.PreProductionService;
import com.cmpay.lemon.monitor.service.production.OperationProductionService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import com.cmpay.lemon.monitor.utils.wechatUtil.schedule.BoardcastScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;

import static com.cmpay.lemon.monitor.constant.MonitorConstants.FILE;

/**
 * 预投产过程管理
 */
@RestController
@RequestMapping(value = MonitorConstants.PREPRODUCTION_PATH)
public class PreProductionController {
    @Autowired
    private PreProductionService preProductionService;
    /**
     * 分页需求列表
     *
     * @param reqDTO
     * @return
     */
    @RequestMapping("/list")
    public GenericRspDTO<PreproductionRspDTO> getUserInfoPage(@RequestBody PreproductionReqDTO reqDTO) {
        if(reqDTO.getPreDateStart()!=null && !reqDTO.getPreDateStart().equals("") && (reqDTO.getPreDateEnd()==null || reqDTO.getPreDateEnd().equals(""))){
            reqDTO.setPreDate(reqDTO.getPreDateStart());
        }
        if(reqDTO.getPreDateEnd()!=null && !reqDTO.getPreDateEnd().equals("") && (reqDTO.getPreDateStart()==null || reqDTO.getPreDateStart().equals(""))){
            reqDTO.setPreDate(reqDTO.getPreDateEnd());
        }

        PreproductionBO productionBO = BeanUtils.copyPropertiesReturnDest(new PreproductionBO(), reqDTO);
        PreproductionRspBO productionRspBO = preProductionService.find(productionBO);
        PreproductionRspDTO rspDTO = new PreproductionRspDTO();
        rspDTO.setPreproductionDTOList(BeanConvertUtils.convertList(productionRspBO.getPreproductionBOList(), PreproductionDTO.class));
        rspDTO.setPageNum(productionRspBO.getPageInfo().getPageNum());
        rspDTO.setPages(productionRspBO.getPageInfo().getPages());
        rspDTO.setTotal(productionRspBO.getPageInfo().getTotal());
        rspDTO.setPageSize(productionRspBO.getPageInfo().getPageSize());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, rspDTO);
    }

    @RequestMapping("/update")
    public GenericRspDTO<NoBody> PreProductionUpdate( @RequestBody PreproductionReqDTO reqDTO){
        PreproductionBO productionBO = BeanUtils.copyPropertiesReturnDest(new PreproductionBO(), reqDTO);
        preProductionService.update(productionBO);
        return GenericRspDTO.newSuccessInstance();
    }

    @RequestMapping("/add")
    public GenericRspDTO<NoBody> PreProductionAdd( @RequestBody PreproductionReqDTO reqDTO){
        PreproductionBO productionBO = BeanUtils.copyPropertiesReturnDest(new PreproductionBO(), reqDTO);
        preProductionService.add(productionBO);
        return GenericRspDTO.newSuccessInstance();
    }

    @RequestMapping("/updateAllProduction")
    public GenericRspDTO<NoBody> updateAllProduction(@RequestParam("taskIdStr") String taskIdStr, HttpServletRequest request, HttpServletResponse response){
        return GenericRspDTO.newSuccessInstance();
    }

    /**
     * 投产包
     *
     * @return
     */
    @PostMapping("/batch/import")
    public GenericRspDTO<NoBody> batchImport(HttpServletRequest request, GenericDTO<NoBody> req) {
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile(FILE);
        String reqNumber = request.getParameter("proNumber");
        preProductionService.doBatchImport(file,reqNumber);
        return GenericRspDTO.newSuccessInstance();
    }

    // 下载投产包
    @RequestMapping("/pkgDownload")
    public GenericRspDTO<NoBody> pkgDownload(@RequestParam("proNumber") String proNumber, HttpServletRequest request, HttpServletResponse response){
        preProductionService.pkgDownload(request,response,proNumber);
        return GenericRspDTO.newSuccessInstance();
    }
}
