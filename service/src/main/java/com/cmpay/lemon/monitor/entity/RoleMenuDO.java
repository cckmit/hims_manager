/*
 * @ClassName RoleMenuDO
 * @Description
 * @version 1.0
 * @Date 2018-11-05 12:18:45
 */
package com.cmpay.lemon.monitor.entity;

import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;

@DataObject
public class RoleMenuDO extends BaseDO {
    /**
     * @Fields id
     */
    private Long id;
    /**
     * @Fields roleId 角色ID
     */
    private Long roleId;
    /**
     * @Fields menuId 菜单ID
     */
    private Long menuId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }
}
