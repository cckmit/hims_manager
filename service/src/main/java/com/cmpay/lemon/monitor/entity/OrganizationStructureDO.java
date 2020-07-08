/*
 * @ClassName OrganizationStructureDO
 * @Description
 * @version 1.0
 * @Date 2020-07-08 19:17:54
 */
package com.cmpay.lemon.monitor.entity;

import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;

@DataObject
public class OrganizationStructureDO extends BaseDO {
    /**
     * @Fields organizationid 用户ID
     */
    private Integer organizationid;
    /**
     * @Fields firstlevelorganization 一级组织
     */
    private String firstlevelorganization;
    /**
     * @Fields secondlevelorganization 二级组织
     */
    private String secondlevelorganization;

    public Integer getOrganizationid() {
        return organizationid;
    }

    public void setOrganizationid(Integer organizationid) {
        this.organizationid = organizationid;
    }

    public String getFirstlevelorganization() {
        return firstlevelorganization;
    }

    public void setFirstlevelorganization(String firstlevelorganization) {
        this.firstlevelorganization = firstlevelorganization;
    }

    public String getSecondlevelorganization() {
        return secondlevelorganization;
    }

    public void setSecondlevelorganization(String secondlevelorganization) {
        this.secondlevelorganization = secondlevelorganization;
    }

    @Override
    public String toString() {
        return "OrganizationStructureDO{" +
                "organizationid=" + organizationid +
                ", firstlevelorganization='" + firstlevelorganization + '\'' +
                ", secondlevelorganization='" + secondlevelorganization + '\'' +
                '}';
    }
}
