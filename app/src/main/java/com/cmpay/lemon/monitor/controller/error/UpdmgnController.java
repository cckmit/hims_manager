package com.cmpay.lemon.monitor.controller.error;

import com.cmpay.framework.data.request.GenericDTO;
import com.cmpay.framework.data.response.GenericRspDTO;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.framework.data.NoBody;
import com.cmpay.lemon.monitor.bo.ErcdmgErrorComditionBO;
import com.cmpay.lemon.monitor.bo.ErcdmgErrorComditionRspBO;
import com.cmpay.lemon.monitor.constant.MonitorConstants;
import com.cmpay.lemon.monitor.dto.ErcdmgErrorComditionDTO;
import com.cmpay.lemon.monitor.dto.ErcdmgErrorComditionReqDTO;
import com.cmpay.lemon.monitor.dto.ErcdmgErrorComditionRspDTO;
import com.cmpay.lemon.monitor.dto.ErcdmgUpdmgnDTO;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.SystemUserService;
import com.cmpay.lemon.monitor.service.demand.ReqPlanService;
import com.cmpay.lemon.monitor.service.errror.ErrorService;
import com.cmpay.lemon.monitor.service.errror.UpdmgnService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: zhou_xiong
 * 需求月反馈
 */
@RestController
@RequestMapping(value = MonitorConstants.UPDMGN_PATH)
public class UpdmgnController {

    @Autowired
    private ErrorService errorService;
    @Autowired
    private ReqPlanService reqPlanService;
    @Autowired
    SystemUserService userService;
    @Autowired
    private UpdmgnService updmgnService;
    /**
     * 分页需求列表
     *
     * @param reqDTO
     * @return
     */
    @RequestMapping("/list")
    public GenericRspDTO<ErcdmgErrorComditionRspDTO> getUserInfoPage(@RequestBody ErcdmgErrorComditionReqDTO reqDTO) {
        ErcdmgErrorComditionBO demandBO = BeanUtils.copyPropertiesReturnDest(new ErcdmgErrorComditionBO(), reqDTO);
        ErcdmgErrorComditionRspBO demandRspBO = updmgnService.searchErroeList(demandBO);
        ErcdmgErrorComditionRspDTO rspDTO = new ErcdmgErrorComditionRspDTO();
        rspDTO.setErcdmgUpdmgnDTOS(BeanConvertUtils.convertList(demandRspBO.getErcdmgUpdmgnBOList(), ErcdmgUpdmgnDTO.class));
        rspDTO.setPageNum(demandRspBO.getUpdmgnBOPageInfo().getPageNum());
        rspDTO.setPages(demandRspBO.getUpdmgnBOPageInfo().getPages());
        rspDTO.setTotal(demandRspBO.getUpdmgnBOPageInfo().getTotal());
        rspDTO.setPageSize(demandRspBO.getUpdmgnBOPageInfo().getPageSize());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, rspDTO);
    }

    @RequestMapping("/details")
    public GenericRspDTO<ErcdmgErrorComditionRspDTO>  details(@RequestParam("ids") String ids){
        ErcdmgErrorComditionRspBO demandRspBO = updmgnService.details(ids);
        ErcdmgErrorComditionRspDTO rspDTO = new ErcdmgErrorComditionRspDTO();
        rspDTO.setErrorComditionDTOS(BeanConvertUtils.convertList(demandRspBO.getErcdmgErrorComditionBOList(), ErcdmgErrorComditionDTO.class));
        rspDTO.setErcdmgUpdmgnDTOS(BeanConvertUtils.convertList(demandRspBO.getErcdmgUpdmgnBOList(), ErcdmgUpdmgnDTO.class));
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, rspDTO);
    }

    /**
     * 提交生产更新
     * @param req
     * @return
     */
    @RequestMapping("/updatePro")
    public GenericRspDTO updatePro(GenericDTO<NoBody> req){
        updmgnService.updatePro();
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }


    @RequestMapping("/updmgnProduction")
    public GenericRspDTO<ErcdmgErrorComditionRspDTO>  updmgnProduction(@RequestParam("ids") String ids){
        ErcdmgErrorComditionRspBO demandRspBO = updmgnService.updmgnProduction(ids);
        ErcdmgErrorComditionRspDTO rspDTO = new ErcdmgErrorComditionRspDTO();
        rspDTO.setErcdmgUpdmgnDTOS(BeanConvertUtils.convertList(demandRspBO.getErcdmgUpdmgnBOList(), ErcdmgUpdmgnDTO.class));
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, rspDTO);
    }


    @RequestMapping("/submitPorduct")
    public GenericRspDTO submitPorduct(@RequestBody ErcdmgErrorComditionDTO reqDTO) {
        System.err.println(reqDTO.getTaskIdStr()+"=="+reqDTO.getEmailContent());
        updmgnService.submitPorduct(reqDTO.getTaskIdStr(),reqDTO.getEmailContent());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }
}
