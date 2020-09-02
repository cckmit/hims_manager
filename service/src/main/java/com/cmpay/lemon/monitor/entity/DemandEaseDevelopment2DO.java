/*
 * @ClassName SupportWorkloadDO
 * @Description
 * @version 1.0
 * @Date 2020-08-27 15:02:17
 */
package com.cmpay.lemon.monitor.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;

/**
 * @author ty
 */
@DataObject
public class DemandEaseDevelopment2DO extends BaseDO {

    /**
     * @Fields secondlevelorganization 二级主导团队
     */
    @Excel(name = "主导团队")
    private String secondlevelorganization;
    @Excel(name = "开发工作量汇总")
    private String developmentworkload;

    public String getSecondlevelorganization() {
        return secondlevelorganization;
    }

    public void setSecondlevelorganization(String secondlevelorganization) {
        this.secondlevelorganization = secondlevelorganization;
    }

    public String getDevelopmentworkload() {
        return developmentworkload;
    }

    public void setDevelopmentworkload(String developmentworkload) {
        this.developmentworkload = developmentworkload;
    }

    @Override
    public String toString() {
        return "DemandEaseDevelopment2DO{" +
                "secondlevelorganization='" + secondlevelorganization + '\'' +
                ", developmentworkload='" + developmentworkload + '\'' +
                '}';
    }
}
