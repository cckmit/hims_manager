/*
 * @ClassName JiraDepartmentDO
 * @Description 
 * @version 1.0
 * @Date 2019-09-26 17:08:37
 */
package com.cmpay.lemon.monitor.entity;

import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;

@DataObject
public class JiraDepartmentDO extends BaseDO {
    /**
     * @Fields departmentnm 部门名称
     */
    private String departmentnm;
    /**
     * @Fields departmentid 部门编号(jira)
     */
    private String departmentid;
    /**
     * @Fields managernm 管理人姓名
     */
    private String managernm;
    /**
     * @Fields managerjiranm 管理人jira账号
     */
    private String managerjiranm;

    public String getDepartmentnm() {
        return departmentnm;
    }

    public void setDepartmentnm(String departmentnm) {
        this.departmentnm = departmentnm;
    }

    public String getDepartmentid() {
        return departmentid;
    }

    public void setDepartmentid(String departmentid) {
        this.departmentid = departmentid;
    }

    public String getManagernm() {
        return managernm;
    }

    public void setManagernm(String managernm) {
        this.managernm = managernm;
    }

    public String getManagerjiranm() {
        return managerjiranm;
    }

    public void setManagerjiranm(String managerjiranm) {
        this.managerjiranm = managerjiranm;
    }
}