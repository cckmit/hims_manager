package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.request.GenericDTO;

import javax.validation.constraints.NotNull;
import java.util.Arrays;

/**
 * @author: zhou_xiong
 */
public class TopStatReqDTO extends GenericDTO {
    /**
     * 时间段
     */
    @NotNull(message = "时间段不能为空")
    private String[] logPeriods;
    /**
     * 中台名称
     */
    @NotNull(message = "中台名称不能为空")
    private String centerName;

    public String[] getLogPeriods() {
        return logPeriods;
    }

    public void setLogPeriods(String[] logPeriods) {
        this.logPeriods = logPeriods;
    }

    public String getCenterName() {
        return centerName;
    }

    public void setCenterName(String centerName) {
        this.centerName = centerName;
    }

    @Override
    public String toString() {
        return "TopStatReqDTO{" +
                "logPeriods=" + Arrays.toString(logPeriods) +
                ", centerName='" + centerName + '\'' +
                '}';
    }
}
