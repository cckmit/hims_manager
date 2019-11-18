package com.cmpay.lemon.monitor.controller.error;

import com.cmpay.framework.data.response.GenericRspDTO;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.monitor.bo.ErcdmgErrorComditionRspBO;
import com.cmpay.lemon.monitor.bo.ErcdmgErrorComditionBO;
import com.cmpay.lemon.monitor.constant.MonitorConstants;
import com.cmpay.lemon.monitor.dto.*;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.demand.ReqPlanService;
import com.cmpay.lemon.monitor.service.errror.ErrorService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
