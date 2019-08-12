package com.cmpay.lemon.monitor.controller;

import com.cmpay.framework.data.response.GenericRspDTO;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.monitor.bo.OrdinarySearchBO;
import com.cmpay.lemon.monitor.constant.MonitorConstants;
import com.cmpay.lemon.monitor.dto.OrdinarySearchReqDTO;
import com.cmpay.lemon.monitor.dto.PageQueryResultDTO;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.query.SearchResult;
import com.cmpay.lemon.monitor.service.OrdinarySearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhou_xiong
 */
@RestController
@RequestMapping(value = MonitorConstants.ORDINARY_PATH)
public class OrdinarySearchController {
    @Autowired
    private OrdinarySearchService ordinarySearchService;

    /**
     * 普通分页检索
     */
    @PostMapping("/list")
    public GenericRspDTO<PageQueryResultDTO> ordinarySearch(@Validated @RequestBody OrdinarySearchReqDTO ordinarySearchReqDTO) {
        OrdinarySearchBO ordinarySearchBO = BeanUtils.copyPropertiesReturnDest(new OrdinarySearchBO(), ordinarySearchReqDTO);
        SearchResult result = ordinarySearchService.search(ordinarySearchBO);
        PageQueryResultDTO pageQueryResultDTO = BeanUtils.copyPropertiesReturnDest(new PageQueryResultDTO(), result);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, pageQueryResultDTO);
    }
}
