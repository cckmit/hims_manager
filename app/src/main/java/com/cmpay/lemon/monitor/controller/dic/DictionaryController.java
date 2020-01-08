package com.cmpay.lemon.monitor.controller.dic;

import com.cmpay.framework.data.request.GenericDTO;
import com.cmpay.framework.data.response.GenericRspDTO;
import com.cmpay.lemon.framework.data.NoBody;
import com.cmpay.lemon.monitor.bo.DictionaryBO;
import com.cmpay.lemon.monitor.constant.MonitorConstants;
import com.cmpay.lemon.monitor.dto.*;
import com.cmpay.lemon.monitor.entity.DictionaryDO;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.dic.DictionaryService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: zhou_xiong
 */
@RestController
@RequestMapping(value = MonitorConstants.DICTIONARY_PATH)
public class DictionaryController {

    @Autowired
    private DictionaryService dictionaryService;

    /**
     * 根据dicId查询字典项
     *
     */
    @GetMapping("/getDicByDicId")
    public GenericRspDTO<DictionaryRspDTO> getDicByDicId(@RequestParam("dicId") String dicId, GenericDTO<NoBody> req) {
        DictionaryDO dictionaryDO = new DictionaryDO();
        dictionaryDO.setDicId(dicId);
        DictionaryBO dictionaryBO = dictionaryService.getDicByDicId(dictionaryDO);
        List<DictionaryBO> dictionaryBOList = dictionaryBO.getDictionaryBOList();
        DictionaryRspDTO dictionaryRspDTO = new DictionaryRspDTO();
        dictionaryRspDTO.setDictionaryDTOList(BeanConvertUtils.convertList(dictionaryBOList, DictionaryDTO.class));
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, dictionaryRspDTO);
    }

    /**
     * 查询基地人员信息
     *
     */
    @GetMapping("/getJdInfo")
    public GenericRspDTO<DictionaryRspDTO> getJdInfo(GenericDTO<NoBody> req) {
        DictionaryBO dictionaryBO = dictionaryService.getJdInfo(new DictionaryDO());
        List<DictionaryBO> dictionaryBOList = dictionaryBO.getDictionaryBOList();
        DictionaryRspDTO dictionaryRspDTO = new DictionaryRspDTO();
        dictionaryRspDTO.setDictionaryDTOList(BeanConvertUtils.convertList(dictionaryBOList, DictionaryDTO.class));
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, dictionaryRspDTO);
    }
    /**
     * 错误码产品模块
     *
     */
    @GetMapping("/findPordmod")
    public GenericRspDTO<DictionaryRspDTO> findPordmod(GenericDTO<NoBody> req) {
        DictionaryBO dictionaryBO = dictionaryService.findPordmod();
        List<DictionaryBO> dictionaryBOList = dictionaryBO.getDictionaryBOList();
        DictionaryRspDTO dictionaryRspDTO = new DictionaryRspDTO();
        dictionaryRspDTO.setDictionaryDTOList(BeanConvertUtils.convertList(dictionaryBOList, DictionaryDTO.class));
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, dictionaryRspDTO);
    }
    /**
     * 错误码渠道
     *
     */
    @GetMapping("/findDictionary")
    public GenericRspDTO<DictionaryRspDTO> findDictionary(GenericDTO<NoBody> req) {
        DictionaryBO dictionaryBO = dictionaryService.findDictionary();
        List<DictionaryBO> dictionaryBOList = dictionaryBO.getDictionaryBOList();
        DictionaryRspDTO dictionaryRspDTO = new DictionaryRspDTO();
        dictionaryRspDTO.setDictionaryDTOList(BeanConvertUtils.convertList(dictionaryBOList, DictionaryDTO.class));
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, dictionaryRspDTO);
    }

    /**
     * 查询产品经理
     *
     */
    @GetMapping("/getcpInfo")
    public GenericRspDTO<DictionaryRspDTO> getcpInfo(GenericDTO<NoBody> req) {
        List<DictionaryBO> dictionaryBOS = dictionaryService.getcpInfo(new DictionaryDO());
        DictionaryRspDTO dictionaryRspDTO = new DictionaryRspDTO();
        dictionaryRspDTO.setDictionaryDTOList(BeanConvertUtils.convertList(dictionaryBOS, DictionaryDTO.class));
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, dictionaryRspDTO);
    }
    @RequestMapping("/workloadLockStatus")
    public GenericRspDTO updateReqWorkLoad(GenericDTO<NoBody> req) {
        dictionaryService.workloadLockStatus();
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }

}
