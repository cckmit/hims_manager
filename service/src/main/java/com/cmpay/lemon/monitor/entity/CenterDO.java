/*
 * @ClassName CenterDO
 * @Description 
 * @version 1.0
 * @Date 2019-07-25 11:01:18
 */
package com.cmpay.lemon.monitor.entity;

import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;

@DataObject
public class CenterDO extends BaseDO {
    /**
     * @Fields id 主键
     */
    private Long id;
    /**
     * @Fields centerName 中台名称
     */
    private String centerName;
    /**
     * @Fields app 应用列表
     */
    private String app;
    /**
     * @Fields manager 负责人
     */
    private String manager;
    /**
     * @Fields mobile 联系电话
     */
    private String mobile;
    /**
     * @Fields reportNum 播报号
     */
    private String reportNum;
    /**
     * @Fields enable 是否播报 0:否 1:是
     */
    private Byte enable;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCenterName() {
        return centerName;
    }

    public void setCenterName(String centerName) {
        this.centerName = centerName;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getReportNum() {
        return reportNum;
    }

    public void setReportNum(String reportNum) {
        this.reportNum = reportNum;
    }

    public Byte getEnable() {
        return enable;
    }

    public void setEnable(Byte enable) {
        this.enable = enable;
    }
}