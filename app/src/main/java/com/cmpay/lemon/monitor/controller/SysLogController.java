package com.cmpay.lemon.monitor.controller;

import com.cmpay.framework.data.response.GenericRspDTO;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.framework.annotation.QueryBody;
import com.cmpay.lemon.framework.page.PageInfo;
import com.cmpay.lemon.monitor.annotation.SysLog;
import com.cmpay.lemon.monitor.bo.SysLogBO;
import com.cmpay.lemon.monitor.bo.SysLogPageBO;
import com.cmpay.lemon.monitor.constant.MonitorConstants;
import com.cmpay.lemon.monitor.dto.SysLogDTO;
import com.cmpay.lemon.monitor.dto.SysLogPageReqDTO;
import com.cmpay.lemon.monitor.dto.SysLogPageRspDTO;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.SysLogService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 系统日志表 controller 控制器
 * Created on 2018/12/24
 *
 * @author zhou_xiong
 */
@RestController
@RequestMapping(value = MonitorConstants.SYSTEM_LOG_PATH)
@SysLog("系统管理-系统日志")
public class SysLogController {
    @Autowired
    private SysLogService sysLogService;

    /**
     * 多条件分页查询
     *
     * @return
     */
    @GetMapping("/list")
    @SysLog("信息列表")
    public GenericRspDTO<SysLogPageRspDTO> pageQuery(@QueryBody SysLogPageReqDTO sysLogPageReqDTO) {
        SysLogPageRspDTO sysLogPageRspDTO = new SysLogPageRspDTO();
        SysLogPageBO sysLogPageBO = BeanUtils.copyPropertiesReturnDest(new SysLogPageBO(), sysLogPageReqDTO);
        PageInfo<SysLogBO> page = sysLogService.pageQuery(sysLogPageBO);
        sysLogPageRspDTO.setSysLogDTOList( BeanConvertUtils.convertList(page.getList(),SysLogDTO.class));
        sysLogPageRspDTO.setPageNum(page.getPageNum());
        sysLogPageRspDTO.setPageSize(page.getPageSize());
        sysLogPageRspDTO.setPages(page.getPages());
        sysLogPageRspDTO.setTotal(page.getTotal());
        return GenericRspDTO.newInstance( MsgEnum.SUCCESS, sysLogPageRspDTO);
    }
}
