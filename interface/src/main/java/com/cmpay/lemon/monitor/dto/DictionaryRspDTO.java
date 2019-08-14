package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.response.GenericRspDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhou_xiong
 */
public class DictionaryRspDTO extends GenericRspDTO {
    List<DictionaryDTO> dictionaryDTOList = new ArrayList<>();

    public List<DictionaryDTO> getDictionaryDTOList() {
        return dictionaryDTOList;
    }

    public void setDictionaryDTOList(List<DictionaryDTO> dictionaryDTOList) {
        this.dictionaryDTOList = dictionaryDTOList;
    }

    @Override
    public String toString() {
        return "DictionaryRspDTO{" +
                "dictionaryDTOList=" + dictionaryDTOList +
                '}';
    }
}
