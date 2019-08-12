/*
 * @ClassName UserRoleDO
 * @Description
 * @version 1.0
 * @Date 2018-11-05 12:16:41
 */
package com.cmpay.lemon.monitor.entity;

import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;

@DataObject
public class UserRoleDO extends BaseDO {
    /**
     * @Fields id
     */
    private Long id;
    /**
     * @Fields userNo 用户ID
     */
    private Long userNo;
    /**
     * @Fields roleId 角色ID
     */
    private Long roleId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserNo() {
        return userNo;
    }

    public void setUserNo(Long userNo) {
        this.userNo = userNo;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
