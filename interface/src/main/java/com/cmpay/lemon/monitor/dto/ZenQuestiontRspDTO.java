/*
 * @ClassName ZenQuestiontDO
 * @Description
 * @version 1.0
 * @Date 2020-09-04 11:30:37
 */
package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.response.PageableRspDTO;

import java.util.List;

public class ZenQuestiontRspDTO extends PageableRspDTO {
    private List<ZenQuestiontDTO> zenQuestiontDTOList;

    public List<ZenQuestiontDTO> getZenQuestiontDTOList() {
        return zenQuestiontDTOList;
    }

    public void setZenQuestiontDTOList(List<ZenQuestiontDTO> zenQuestiontDTOList) {
        this.zenQuestiontDTOList = zenQuestiontDTOList;
    }

    @Override
    public String toString() {
        return "ZenQuestiontRspDTO{" +
                "zenQuestiontDTOList=" + zenQuestiontDTOList +
                '}';
    }
}
