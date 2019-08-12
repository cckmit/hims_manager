package com.cmpay.lemon.monitor.controller;

import com.cmpay.framework.data.request.GenericDTO;
import com.cmpay.framework.data.response.GenericRspDTO;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.framework.annotation.QueryBody;
import com.cmpay.lemon.framework.data.NoBody;
import com.cmpay.lemon.framework.page.PageInfo;
import com.cmpay.lemon.monitor.bo.CenterBO;
import com.cmpay.lemon.monitor.constant.MonitorConstants;
import com.cmpay.lemon.monitor.dto.CenterDTO;
import com.cmpay.lemon.monitor.dto.CenterInfoPageQueryReqDTO;
import com.cmpay.lemon.monitor.dto.CenterInfoPageQueryRspDTO;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.CenterService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: zhou_xiong
 */
@RestController
@RequestMapping(value = MonitorConstants.CENTER_PATH)
public class CenterController {

    @Autowired
    private CenterService centerService;

    /**
     * 查询中台信息
     *
     * @return
     */
    @GetMapping("/info/{id}")
    public GenericRspDTO<CenterDTO> getUserInfoById(@PathVariable("id") String id, GenericDTO<NoBody> req) {
        Long centerId = Long.valueOf(id);
        CenterBO centerBO = centerService.findById(centerId);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, BeanUtils.copyPropertiesReturnDest(new CenterDTO(), centerBO));
    }

    /**
     * 分页中台列表
     *
     * @param reqDTO
     * @return
     */
    @GetMapping("/list")
    public GenericRspDTO<CenterInfoPageQueryRspDTO> getUserInfoPage(@QueryBody CenterInfoPageQueryReqDTO reqDTO) {
        CenterBO centerBO = BeanUtils.copyPropertiesReturnDest(new CenterBO(), reqDTO);
        PageInfo<CenterBO> pageInfo = centerService.findCenters(centerBO);
        List<CenterBO> centerBOList = BeanConvertUtils.convertList(pageInfo.getList(), CenterBO.class);
        CenterInfoPageQueryRspDTO rspDTO = new CenterInfoPageQueryRspDTO();
        rspDTO.setCenterDTOList(BeanConvertUtils.convertList(centerBOList, CenterDTO.class));
        rspDTO.setPageNum(pageInfo.getPageNum());
        rspDTO.setPageSize(pageInfo.getPageSize());
        rspDTO.setPages(pageInfo.getPages());
        rspDTO.setTotal(pageInfo.getTotal());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, rspDTO);
    }

    /**
     * 查询所有中台信息
     */
    @GetMapping("/all")
    public GenericRspDTO<CenterInfoPageQueryRspDTO> findAll(GenericDTO reqDTO) {
        CenterInfoPageQueryRspDTO rspDTO = new CenterInfoPageQueryRspDTO();
        rspDTO.setCenterDTOList(BeanConvertUtils.convertList(centerService.findAll(), CenterDTO.class));
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, rspDTO);
    }

    /**
     * 添加中台信息
     *
     * @param
     * @return
     */
    @PostMapping("/save")
    public GenericRspDTO add(@RequestBody CenterDTO centerDTO) {
        CenterBO centerBO = BeanUtils.copyPropertiesReturnDest(new CenterBO(), centerDTO);
        centerService.add(centerBO);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }

    /**
     * 删除中台信息
     *
     * @param
     * @return
     */
    @GetMapping("/delete/{id}")
    public GenericRspDTO delete(@PathVariable("id") String id, GenericDTO<NoBody> req) {
        Long centerId = Long.valueOf(id);
        centerService.delete(centerId);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }

    /**
     * 更新中台信息
     *
     * @param
     * @return
     */
    @PostMapping("/update")
    public GenericRspDTO update(@RequestBody CenterDTO centerDTO) {
        CenterBO centerBO = BeanUtils.copyPropertiesReturnDest(new CenterBO(), centerDTO);
        centerService.update(centerBO);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }

}
