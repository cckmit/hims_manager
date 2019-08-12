package com.cmpay.lemon.monitor.controller;

import com.cmpay.framework.data.request.GenericDTO;
import com.cmpay.framework.data.response.GenericRspDTO;
import com.cmpay.lemon.framework.data.NoBody;
import com.cmpay.lemon.monitor.bo.LogTypeBO;
import com.cmpay.lemon.monitor.constant.MonitorConstants;
import com.cmpay.lemon.monitor.dto.LogTypeDTO;
import com.cmpay.lemon.monitor.dto.LogTypeRspDTO;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.LogTypeService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhou_xiong
 */
@RestController
@RequestMapping(value = MonitorConstants.LOG_TYPE)
public class LogTypeController {
    @Autowired
    private LogTypeService logTypeService;
    /**
     * 查询日志类型列表
     */
    @GetMapping("/list")
    public GenericRspDTO<LogTypeRspDTO> getLogTypeList(GenericDTO<NoBody> req) {
        LogTypeBO logTypeBO = logTypeService.findAll();
        List<LogTypeBO> logTypeBOList = logTypeBO.getLogTypeBOList();
        LogTypeRspDTO logTypeRspDTO = new LogTypeRspDTO();
        logTypeRspDTO.setLogTypeDTOList(BeanConvertUtils.convertList(logTypeBOList, LogTypeDTO.class));
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, logTypeRspDTO);
    }
}
