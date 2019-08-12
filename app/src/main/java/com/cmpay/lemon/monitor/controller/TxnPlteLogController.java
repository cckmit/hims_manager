package com.cmpay.lemon.monitor.controller;

import com.cmpay.framework.data.request.GenericDTO;
import com.cmpay.framework.data.response.GenericRspDTO;
import com.cmpay.lemon.common.exception.BusinessException;
import com.cmpay.lemon.common.utils.JudgeUtils;
import com.cmpay.lemon.framework.data.NoBody;
import com.cmpay.lemon.monitor.bo.GolbalStatBO;
import com.cmpay.lemon.monitor.bo.TopStatBO;
import com.cmpay.lemon.monitor.bo.TxnPlteLogBO;
import com.cmpay.lemon.monitor.constant.MonitorConstants;
import com.cmpay.lemon.monitor.dto.GolbalStatReqDTO;
import com.cmpay.lemon.monitor.dto.GolbalStatRspDTO;
import com.cmpay.lemon.monitor.dto.TopStatReqDTO;
import com.cmpay.lemon.monitor.dto.TopStatRspDTO;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.query.SearchResult;
import com.cmpay.lemon.monitor.service.TxnPlteLogService;
import com.cmpay.lemon.monitor.translog.TransLog;
import com.cmpay.lemon.monitor.translog.TransLogSort;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.cmpay.lemon.monitor.search.aggregation.TxCountAggregator.STATDATA_HOLDER;

/**
 * @author zhou_xiong
 */
@RestController
@RequestMapping(value = MonitorConstants.TXNPLTELOG_PATH)
public class TxnPlteLogController {
    @Autowired
    private TxnPlteLogService txnPlteLogService;

    /**
     * 调用链
     *
     * @param logRequestId
     * @param req
     * @return
     */
    @GetMapping("/getDatasByRequestId")
    public GenericRspDTO<TransLog> getDatasByRequestId(@RequestParam("id") String logRequestId, GenericDTO<NoBody> req) {
        if (JudgeUtils.isBlank(logRequestId)) {
            BusinessException.throwBusinessException(MsgEnum.REQUESTID_IS_BLANK);
        }
        TxnPlteLogBO txnPlteLogBO = new TxnPlteLogBO(logRequestId);
        SearchResult searchResult = txnPlteLogService.getDatasByRequestId(txnPlteLogBO);
        TransLog transLog = TransLogSort.buildTree(searchResult.getResultSet());
        if (JudgeUtils.isNotNull(transLog)) {
            transLog.setSearchResult(searchResult);
        }
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, transLog);
    }

    /**
     * 全局统计
     *
     * @param golbalStatReqDTO
     * @return
     */
    @PostMapping("/golbal/statistics")
    public GenericDTO<GolbalStatRspDTO> golbalStat(@Validated @RequestBody GolbalStatReqDTO golbalStatReqDTO) {
        GolbalStatBO golbalStatBO = new GolbalStatBO(golbalStatReqDTO.getLogPeriods());
        Map<String, Long> txCountMap = txnPlteLogService.getTxCountMap(golbalStatBO);
        Map<String, BigDecimal> sucRateMap = txnPlteLogService.getSucRateMap(golbalStatBO);
        Map<String, Long> avgTimeMap = txnPlteLogService.getAvgTimeMap(golbalStatBO);
        Map<String, BigDecimal> sysErrRateMap = txnPlteLogService.getSysErrRateMap(golbalStatBO);
        STATDATA_HOLDER.remove();
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, new GolbalStatRspDTO(txCountMap, sucRateMap, avgTimeMap, sysErrRateMap));
    }

    /**
     * top10交易量统计
     *
     * @param topStatReqDTO
     * @return
     */
    @PostMapping("/top10/statistics")
    public GenericRspDTO<TopStatRspDTO> topStat(@Validated @RequestBody TopStatReqDTO topStatReqDTO) {
        List<TopStatBO> topStatBOS = txnPlteLogService.getTopStats(new TopStatBO(topStatReqDTO.getLogPeriods(), topStatReqDTO.getCenterName()));
        TopStatRspDTO topStatRspDTO = new TopStatRspDTO();
        topStatRspDTO.setTopStats(BeanConvertUtils.convertList(topStatBOS, TopStatRspDTO.TopStat.class));
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, topStatRspDTO);
    }

    /**
     * top10耗时统计
     *
     * @param topStatReqDTO
     * @return
     */
    @PostMapping("/top10/duration/statistics")
    public GenericRspDTO<TopStatRspDTO> topDurationStat(@Validated @RequestBody TopStatReqDTO topStatReqDTO) {
        List<TopStatBO> topStatBOS = txnPlteLogService.getTopDurationStats(new TopStatBO(topStatReqDTO.getLogPeriods(), topStatReqDTO.getCenterName()));
        TopStatRspDTO topStatRspDTO = new TopStatRspDTO();
        topStatRspDTO.setTopStats(BeanConvertUtils.convertList(topStatBOS, TopStatRspDTO.TopStat.class));
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, topStatRspDTO);
    }
}
