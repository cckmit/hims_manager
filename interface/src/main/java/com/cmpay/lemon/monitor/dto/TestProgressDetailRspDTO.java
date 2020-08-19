package com.cmpay.lemon.monitor.dto;

import java.util.List;
/**
 * @author  ty
 */
public class TestProgressDetailRspDTO {
    private List<TestProgressDetailDTO> testProgressDetailDTOList ;

    public List<TestProgressDetailDTO> getTestProgressDetailDTOList() {
        return testProgressDetailDTOList;
    }

    public void setTestProgressDetailDTOList(List<TestProgressDetailDTO> testProgressDetailDTOList) {
        this.testProgressDetailDTOList = testProgressDetailDTOList;
    }

    @Override
    public String toString() {
        return "TestProgressDetailRspDTO{" +
                "testProgressDetailDTOList=" + testProgressDetailDTOList +
                '}';
    }
}
