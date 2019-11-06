package com.cmpay.lemon.monitor.controller.systemOperation;


import com.cmpay.framework.data.response.GenericRspDTO;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.monitor.bo.OperationApplicationBO;
import com.cmpay.lemon.monitor.bo.OperationApplicationRspBO;
import com.cmpay.lemon.monitor.bo.ProductionBO;
import com.cmpay.lemon.monitor.bo.ProductionRspBO;
import com.cmpay.lemon.monitor.constant.MonitorConstants;
import com.cmpay.lemon.monitor.dto.*;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.production.OperationProductionService;
import com.cmpay.lemon.monitor.service.systemOperation.OperationApplicationService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = MonitorConstants.SYSTEMOPERATION_PATH)
public class systemOperationController {
    @Autowired
    private OperationApplicationService operationApplicationService;

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

}
