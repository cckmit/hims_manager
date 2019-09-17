package com.cmpay.lemon.monitor.controller.workload;

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
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.demand.ReqPlanService;
import com.cmpay.lemon.monitor.service.workload.ReqWorkLoadService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: zhou_xiong
 * 需求周反馈
 */
@RestController
@RequestMapping(value = MonitorConstants.REQWORK_PATH)
public class ReqWorkLoadController {

    @Autowired
    private ReqWorkLoadService reqWorkLoadService;

    /**
     * 分页需求列表
     *
     * @param reqDTO
     * @return
     */
    @RequestMapping("/list")
    public GenericRspDTO<DemandRspDTO> getUserInfoPage(@RequestBody DemandReqDTO reqDTO) {
        System.out.println("工作量查询");
        DemandBO demandBO = BeanUtils.copyPropertiesReturnDest(new DemandBO(), reqDTO);
        DemandRspBO demandRspBO = reqWorkLoadService.findDemand(demandBO);
        DemandRspDTO rspDTO = new DemandRspDTO();
        rspDTO.setDemandDTOList(BeanConvertUtils.convertList(demandRspBO.getDemandBOList(), DemandDTO.class));
        rspDTO.setPageNum(demandRspBO.getPageInfo().getPageNum());
        rspDTO.setPages(demandRspBO.getPageInfo().getPages());
        rspDTO.setTotal(demandRspBO.getPageInfo().getTotal());
        rspDTO.setPageSize(demandRspBO.getPageInfo().getPageSize());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, rspDTO);
    }

    /**
     * 存量变更
     *
     * @return
     */
    @RequestMapping("/changeReq")
    public GenericRspDTO changeReq(@RequestBody DemandReqDTO reqDTO) {
        System.out.println(reqDTO.getReqImplMon());
        //reqPlanService.changeReq(reqDTO.getReqImplMon());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }
}
