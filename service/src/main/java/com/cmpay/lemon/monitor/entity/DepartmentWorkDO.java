package com.cmpay.lemon.monitor.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.cmpay.lemon.framework.annotation.DataObject;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 投产状态变更记录
 */
@DataObject
public class DepartmentWorkDO extends AbstractDO {
    // 部门
    private String department;
    //应填报工时人数
    private String yingHours;
    //大于八小时人数
    private String daHours;
    //小于八小时人数
    private String xiaoHours;
    //未填报工时人数
    private String weiHours;

    @Override
    public Serializable getId() {
        return null;
    }

    @Override
    public String toString() {
        return "DepartmentWorkDO{" +
                "department=" + department +
                ", yingHours='" + yingHours + '\'' +
                ", daHours='" + daHours + '\'' +
                ", xiaoHours='" + xiaoHours + '\'' +
                ", weiHours='" + weiHours + '\'' +
                '}';
    }

    public String getWorkHoursToString() {
        return "{'product': '"+department+"', '应填报工时人数': "+yingHours+", '达到或超过8小时人数': "+daHours+", '工时不足8小时人数': "+xiaoHours+", '未填报工时人数': "+weiHours+"}";
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getYingHours() {
        return yingHours;
    }

    public void setYingHours(String yingHours) {
        this.yingHours = yingHours;
    }

    public String getDaHours() {
        return daHours;
    }

    public void setDaHours(String daHours) {
        this.daHours = daHours;
    }

    public String getXiaoHours() {
        return xiaoHours;
    }

    public void setXiaoHours(String xiaoHours) {
        this.xiaoHours = xiaoHours;
    }

    public String getWeiHours() {
        return weiHours;
    }

    public void setWeiHours(String weiHours) {
        this.weiHours = weiHours;
    }
}