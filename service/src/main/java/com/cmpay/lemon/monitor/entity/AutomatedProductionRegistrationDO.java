/*
 * @ClassName AutomatedProductionRegistrationDO
 * @Description 
 * @version 1.0
 * @Date 2020-04-09 15:31:21
 */
package com.cmpay.lemon.monitor.entity;

import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;

import java.time.LocalDateTime;

@DataObject
public class AutomatedProductionRegistrationDO extends BaseDO {
    /**
     * @Fields id 
     */
    private Integer id;
    /**
     * @Fields pronumber 
     */
    private String pronumber;
    /**
     * @Fields status 
     */
    private String status;
    /**
     * @Fields env 
     */
    private String env;
    /**
     * @Fields remark 
     */
    private String remark;
    /**
     * @Fields creatTime 
     */
    private LocalDateTime creatTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPronumber() {
        return pronumber;
    }

    public void setPronumber(String pronumber) {
        this.pronumber = pronumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public LocalDateTime getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(LocalDateTime creatTime) {
        this.creatTime = creatTime;
    }
}