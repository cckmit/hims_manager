package com.cmpay.lemon.monitor.controller;

/**
 * Created on 2018/12/17
 *
 * @author: ou_yn
 */

import com.cmpay.framework.data.response.GenericRspDTO;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.common.utils.JudgeUtils;
import com.cmpay.lemon.framework.annotation.QueryBody;
import com.cmpay.lemon.framework.data.NoBody;
import com.cmpay.lemon.framework.security.SecurityUtils;
import com.cmpay.lemon.framework.utils.LemonUtils;
import com.cmpay.lemon.monitor.annotation.SysLog;
import com.cmpay.lemon.monitor.bo.SnapshotLogBO;
import com.cmpay.lemon.monitor.bo.SnapshotLogQueryBO;
import com.cmpay.lemon.monitor.dto.SnapshotLogDTO;
import com.cmpay.lemon.monitor.dto.SnapshotLogPageQueryReqDTO;
import com.cmpay.lemon.monitor.dto.SnapshotLogReqDTO;
import com.cmpay.lemon.monitor.dto.SnapshotLogRspDTO;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.SnapshotLogService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sys/snapshotlog")
@SysLog("系统管理-日志快照")
public class SnapshotLogController {
    @Autowired
    private SnapshotLogService snapshotLogService;

    @PostMapping("/html")
    public GenericRspDTO saveHtml(@RequestBody SnapshotLogReqDTO snapshotLogReqDTO) {
        if (snapshotLogReqDTO != null) {
            SnapshotLogBO snapshotLog = new SnapshotLogBO();
            snapshotLogReqDTO.setCreateBy( SecurityUtils.getLoginName());
            snapshotLogReqDTO.setRemoteAddr( LemonUtils.getClientIp());
            BeanUtils.copyProperties(snapshotLog, snapshotLogReqDTO);
            snapshotLogService.insert(snapshotLog);
        }

        return GenericRspDTO.newInstance( MsgEnum.SUCCESS, NoBody.class);
    }

    @GetMapping("/html/list")
    @SysLog("信息列表")
    public GenericRspDTO<SnapshotLogRspDTO> getHtml(@QueryBody SnapshotLogPageQueryReqDTO pageQueryReqDTO) {

        SnapshotLogQueryBO snapshotLogQueryBO = new SnapshotLogQueryBO();
        BeanUtils.copyProperties(snapshotLogQueryBO, pageQueryReqDTO);

        PageInfo<SnapshotLogBO> snapshotLogPage = snapshotLogService.getSnapshotHtmls(snapshotLogQueryBO);
        List<SnapshotLogDTO> snapshotLogDTOs;
        SnapshotLogRspDTO snapshotLogRspDTO = new SnapshotLogRspDTO();
        if (JudgeUtils.isNotEmpty(snapshotLogPage.getList())) {

            snapshotLogDTOs = BeanConvertUtils.convertList(snapshotLogPage.getList(), SnapshotLogDTO.class);

            snapshotLogRspDTO.setSnapshotLogDTOs(snapshotLogDTOs);
            snapshotLogRspDTO.setPageNum(snapshotLogPage.getPageNum());
            snapshotLogRspDTO.setPageSize(snapshotLogPage.getPageSize());
            snapshotLogRspDTO.setPages(snapshotLogPage.getPages());
            snapshotLogRspDTO.setTotal(snapshotLogPage.getTotal());
        }


        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, snapshotLogRspDTO);

    }

}
