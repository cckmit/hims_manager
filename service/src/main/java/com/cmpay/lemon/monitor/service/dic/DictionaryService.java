package com.cmpay.lemon.monitor.service.dic;


import com.cmpay.lemon.monitor.bo.DictionaryBO;
import com.cmpay.lemon.monitor.entity.DictionaryDO;

import java.util.List;

/**
 * 日志类型 服务接口
 * Created on 2019/1/16
 *
 * @author: zhou_xiong
 */

public interface DictionaryService {
    /**
     * 查询日志类型列表
     */
    DictionaryBO getDicByDicId(DictionaryDO dictionaryDO);

    /**
     * 查询基地人员信息
     */
    DictionaryBO getJdInfo(DictionaryDO dictionaryDO);
    String findFieldName(String fieldId, String fieldValue);
    public List<DictionaryBO> findUploadPeriod(String reqPeriod);
}
