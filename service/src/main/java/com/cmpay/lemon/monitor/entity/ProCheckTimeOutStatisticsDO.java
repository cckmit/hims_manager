/*
 * @ClassName ProCheckTimeOutStatisticsDO
 * @Description 
 * @version 1.0
 * @Date 2020-06-15 09:33:23
 */
package com.cmpay.lemon.monitor.entity;

import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;

@DataObject
public class ProCheckTimeOutStatisticsDO extends BaseDO {
    /**
     * @Fields id 主键
     */
    private Integer id;
    /**
     * @Fields department 部门
     */
    private String department;
    /**
     * @Fields registrationdate 统计日期
     */
    private String registrationdate;
    /**
     * @Fields count 统计次数
     */
    private Integer count;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getRegistrationdate() {
        return registrationdate;
    }

    public void setRegistrationdate(String registrationdate) {
        this.registrationdate = registrationdate;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}