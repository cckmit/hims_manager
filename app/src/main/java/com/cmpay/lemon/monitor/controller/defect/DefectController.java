package com.cmpay.lemon.monitor.controller.defect;


import com.cmpay.framework.data.response.GenericRspDTO;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.monitor.bo.ProductionDefectsBO;
import com.cmpay.lemon.monitor.bo.ProductionDefectsRspBO;
import com.cmpay.lemon.monitor.constant.MonitorConstants;
import com.cmpay.lemon.monitor.dto.ProductionDefectsDTO;
import com.cmpay.lemon.monitor.dto.ProductionDefectsReqDTO;
import com.cmpay.lemon.monitor.dto.ProductionDefectsRspDTO;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.defects.DefectsService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = MonitorConstants.DEFECT_PATH)
public class DefectController {
    @Autowired
    DefectsService defectsService;


    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public GenericRspDTO<ProductionDefectsRspDTO> findDefectAll(@RequestBody ProductionDefectsReqDTO productionDefectsReqDTO) {
        ProductionDefectsBO productionDefectsBO = BeanUtils.copyPropertiesReturnDest(new ProductionDefectsBO(), productionDefectsReqDTO);

        ProductionDefectsRspBO productionDefectsRspBO = defectsService.findDefectAll(productionDefectsBO);

        ProductionDefectsRspDTO rspDTO = new ProductionDefectsRspDTO();
        rspDTO.setProductionDefectsDTOList(BeanConvertUtils.convertList(productionDefectsRspBO.getProductionDefectsBOList(), ProductionDefectsDTO.class));
        rspDTO.setPageNum(productionDefectsRspBO.getPageInfo().getPageNum());
        rspDTO.setPages(productionDefectsRspBO.getPageInfo().getPages());
        rspDTO.setTotal(productionDefectsRspBO.getPageInfo().getTotal());
        rspDTO.setPageSize(productionDefectsRspBO.getPageInfo().getPageSize());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, rspDTO);
    }


}
