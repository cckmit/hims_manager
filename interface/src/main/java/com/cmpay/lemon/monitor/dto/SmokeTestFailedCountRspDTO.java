/*
 * @ClassName SmokeTestFailedCountDO
 * @Description
 * @version 1.0
 * @Date 2020-09-16 16:08:58
 */
package com.cmpay.lemon.monitor.dto;


import java.util.List;

public class SmokeTestFailedCountRspDTO {
    List<SmokeTestFailedCountDTO> smokeTestFailedCountDTOList;

    public List<SmokeTestFailedCountDTO> getSmokeTestFailedCountDTOList() {
        return smokeTestFailedCountDTOList;
    }

    public void setSmokeTestFailedCountDTOList(List<SmokeTestFailedCountDTO> smokeTestFailedCountDTOList) {
        this.smokeTestFailedCountDTOList = smokeTestFailedCountDTOList;
    }

    @Override
    public String toString() {
        return "SmokeTestFailedCountRspDTO{" +
                "smokeTestFailedCountDTOList=" + smokeTestFailedCountDTOList +
                '}';
    }
}
