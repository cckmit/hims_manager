package com.cmpay.lemon.monitor.controller.vpn;


import com.cmpay.framework.data.response.GenericRspDTO;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.framework.data.NoBody;
import com.cmpay.lemon.monitor.bo.*;
import com.cmpay.lemon.monitor.constant.MonitorConstants;
import com.cmpay.lemon.monitor.dto.*;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.systemOperation.OperationApplicationService;
import com.cmpay.lemon.monitor.service.vpn.VpnInfoService;
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
@RequestMapping(value = MonitorConstants.SYSTEMOPERATION_VPN)
public class VpnInfoController {
    @Autowired
    private VpnInfoService vpnInfoService;
    /**
     * 分页需求列表
     *
     * @param reqDTO
     * @return
     */
    @RequestMapping("/list")
    public GenericRspDTO<VpnInfoRspDTO> getUserInfoPage(@RequestBody VpnInfoReqDTO reqDTO) {

        VpnInfoBO vpnInfoBO = BeanUtils.copyPropertiesReturnDest(new VpnInfoBO(), reqDTO);
        VpnInfoRspBO vpnInfoRspBO = vpnInfoService.find(vpnInfoBO);
        System.err.println(vpnInfoRspBO.getVpnInfoBOList());
        VpnInfoRspDTO rspDTO = new VpnInfoRspDTO();
        rspDTO.setVpnInfoDTOS(BeanConvertUtils.convertList(vpnInfoRspBO.getVpnInfoBOList(), VpnInfoDTO.class));
        rspDTO.setPageNum(vpnInfoRspBO.getPageInfo().getPageNum());
        rspDTO.setPages(vpnInfoRspBO.getPageInfo().getPages());
        rspDTO.setTotal(vpnInfoRspBO.getPageInfo().getTotal());
        rspDTO.setPageSize(vpnInfoRspBO.getPageInfo().getPageSize());
         return GenericRspDTO.newInstance(MsgEnum.SUCCESS, rspDTO);
    }

    @RequestMapping("/update")
    public GenericRspDTO<NoBody> PreProductionUpdate( @RequestBody PreproductionReqDTO reqDTO){
        PreproductionBO productionBO = BeanUtils.copyPropertiesReturnDest(new PreproductionBO(), reqDTO);
        //preProductionService.update(productionBO);
        return GenericRspDTO.newSuccessInstance();
    }

    @RequestMapping("/add")
    public GenericRspDTO<NoBody> VpnInfoAdd( @RequestBody VpnInfoReqDTO reqDTO){
        System.err.println(reqDTO);
        VpnInfoBO vpnInfoBO = BeanUtils.copyPropertiesReturnDest(new VpnInfoBO(), reqDTO);
        vpnInfoService.add(vpnInfoBO);
        return GenericRspDTO.newSuccessInstance();
    }

    @RequestMapping("/updateAllProduction")
    public GenericRspDTO<NoBody> updateAllProduction(@RequestParam("taskIdStr") String taskIdStr, HttpServletRequest request, HttpServletResponse response){
        vpnInfoService.updateAllProduction(request,response,taskIdStr);
        return GenericRspDTO.newSuccessInstance();
    }
    @RequestMapping("/access")
    public GenericRspDTO<ErcdmgPordUserDTO> access(){
        ErcdmgPordUserBO ercdmgPordUserBO = vpnInfoService.access();
        ErcdmgPordUserDTO rspDTO =  new ErcdmgPordUserDTO();
        BeanConvertUtils.convert(rspDTO, ercdmgPordUserBO);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, rspDTO);
    }

}
