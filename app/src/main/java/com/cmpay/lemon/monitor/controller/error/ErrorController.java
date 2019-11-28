package com.cmpay.lemon.monitor.controller.error;

import com.cmpay.framework.data.request.GenericDTO;
import com.cmpay.framework.data.response.GenericRspDTO;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.framework.data.NoBody;
import com.cmpay.lemon.monitor.bo.ErcdmgErrorComditionRspBO;
import com.cmpay.lemon.monitor.bo.ErcdmgErrorComditionBO;
import com.cmpay.lemon.monitor.constant.MonitorConstants;
import com.cmpay.lemon.monitor.dto.*;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.demand.ReqPlanService;
import com.cmpay.lemon.monitor.service.errror.ErrorService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.cmpay.lemon.monitor.constant.MonitorConstants.FILE;

/**
 * @author: zhou_xiong
 * 需求月反馈
 */
@RestController
@RequestMapping(value = MonitorConstants.ERROR_PATH)
public class ErrorController {

    @Autowired
    private ErrorService errorService;
    @Autowired
    private ReqPlanService reqPlanService;

    /**
     * 分页需求列表
     *
     * @param reqDTO
     * @return
     */
    @RequestMapping("/list")
    public GenericRspDTO<ErcdmgErrorComditionRspDTO> getUserInfoPage(@RequestBody ErcdmgErrorComditionReqDTO reqDTO) {
        System.err.println(reqDTO);
        ErcdmgErrorComditionBO demandBO = BeanUtils.copyPropertiesReturnDest(new ErcdmgErrorComditionBO(), reqDTO);
        ErcdmgErrorComditionRspBO demandRspBO = errorService.searchErroeList(demandBO);
        System.err.println(demandRspBO);
        ErcdmgErrorComditionRspDTO rspDTO = new ErcdmgErrorComditionRspDTO();
        rspDTO.setErrorComditionDTOS(BeanConvertUtils.convertList(demandRspBO.getErcdmgErrorComditionBOList(), ErcdmgErrorComditionDTO.class));
        rspDTO.setPageNum(demandRspBO.getPageInfo().getPageNum());
        rspDTO.setPages(demandRspBO.getPageInfo().getPages());
        rspDTO.setTotal(demandRspBO.getPageInfo().getTotal());
        rspDTO.setPageSize(demandRspBO.getPageInfo().getPageSize());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, rspDTO);
    }
    /**
     * 新增
     * @param
     * @return
     */
    @RequestMapping("/add")
    public GenericRspDTO addError(@RequestBody ErcdmgErrorComditionDTO reqDTO) {
        System.err.println(reqDTO);
        ErcdmgErrorComditionBO errorComditionBO = new ErcdmgErrorComditionBO();
        BeanConvertUtils.convert(errorComditionBO, reqDTO);
        errorService.addError(errorComditionBO);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }
    /**
     * 修改
     * @param
     * @return
     */
    @RequestMapping("/update")
    public GenericRspDTO updateError(@RequestBody ErcdmgErrorComditionDTO reqDTO) {
        System.err.println(reqDTO);
        ErcdmgErrorComditionBO errorComditionBO = new ErcdmgErrorComditionBO();
        BeanConvertUtils.convert(errorComditionBO, reqDTO);
        errorService.updateError(errorComditionBO);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }

    /**
     * 删除
     * @param id 错误码id
     * @return
     */
    @RequestMapping("/delete")
    public GenericRspDTO deleteError(@RequestParam("ids") String id){
        System.err.println(id);
        errorService.deleteError(id);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }
    /**
     * 错误码导入
     *
     * @return
     */
    @PostMapping("/batch/import")
    public GenericRspDTO<NoBody> batchImport(HttpServletRequest request, GenericDTO<NoBody> req) {
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile(FILE);
        errorService.doBatchImport(file);
        return GenericRspDTO.newSuccessInstance();
    }
    @RequestMapping("/goback")
    public GenericRspDTO gobackError(@RequestBody ErcdmgErrorComditionDTO reqDTO) {
        System.err.println(reqDTO.getTaskIdStr()+"==="+reqDTO.getStatus());
        errorService.goback(reqDTO.getTaskIdStr(),reqDTO.getStatus());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }

}
