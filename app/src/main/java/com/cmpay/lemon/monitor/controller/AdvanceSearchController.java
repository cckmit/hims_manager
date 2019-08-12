package com.cmpay.lemon.monitor.controller;

import com.cmpay.framework.data.response.GenericRspDTO;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.monitor.bo.AdvanceSearchBO;
import com.cmpay.lemon.monitor.constant.MonitorConstants;
import com.cmpay.lemon.monitor.dto.AdvanceSearchReqDTO;
import com.cmpay.lemon.monitor.dto.PageQueryResultDTO;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.query.SearchResult;
import com.cmpay.lemon.monitor.service.AdvanceSearchService;
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
@RequestMapping(value = MonitorConstants.ADVANCE_PATH)
public class AdvanceSearchController {
    @Autowired
    private AdvanceSearchService advanceSearchService;

    /**
     * 高级检索
     */
    @PostMapping("/list")
    public GenericRspDTO<PageQueryResultDTO> advanceSearch(@Validated @RequestBody AdvanceSearchReqDTO advanceSearchReqDTO) {
        AdvanceSearchBO advanceSearchBO = BeanUtils.copyPropertiesReturnDest(new AdvanceSearchBO(), advanceSearchReqDTO);
        SearchResult searchResult = advanceSearchService.advanceSearch(advanceSearchBO);
        PageQueryResultDTO pageQueryResultDTO = BeanUtils.copyPropertiesReturnDest(new PageQueryResultDTO(), searchResult);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, pageQueryResultDTO);
    }
}
