package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.request.GenericDTO;

import javax.validation.constraints.NotNull;
import java.util.Arrays;

/**
 * @author: zhou_xiong
 */
public class GolbalStatReqDTO extends GenericDTO {
    /**
     * 时间段
     */
    @NotNull(message = "时间段不能为空")
    private String[] logPeriods;

    public String[] getLogPeriods() {
        return logPeriods;
    }

    public void setLogPeriods(String[] logPeriods) {
        this.logPeriods = logPeriods;
    }

    @Override
    public String toString() {
        return "GolbalStatReqDTO{" +
                "logPeriods=" + Arrays.toString(logPeriods) +
                '}';
    }
}
