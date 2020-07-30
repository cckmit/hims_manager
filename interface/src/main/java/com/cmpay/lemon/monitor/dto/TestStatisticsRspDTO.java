package com.cmpay.lemon.monitor.dto;

import java.util.List;

public class TestStatisticsRspDTO {
    private List<TestStatisticsDTO> testStatisticsDTOList;

    public List<TestStatisticsDTO> getTestStatisticsDTOList() {
        return testStatisticsDTOList;
    }

    public void setTestStatisticsDTOList(List<TestStatisticsDTO> testStatisticsDTOList) {
        this.testStatisticsDTOList = testStatisticsDTOList;
    }
}
