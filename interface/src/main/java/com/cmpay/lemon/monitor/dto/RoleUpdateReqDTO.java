package com.cmpay.lemon.monitor.dto;

import java.util.List;

/**
 * 描述
 *
 * @author : 曾益
 * @date : 2018/11/12
 */
public class RoleUpdateReqDTO {
    /**
     * @Fields roleId
     */
    private Long roleId;
    /**
     * @Fields roleName 角色名称
     */
    private String roleName;

    /**
     * @Fields remark 备注
     */
    private String remark;

    private List<Long> menuIdList;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<Long> getMenuIdList() {
        return menuIdList;
    }

    public void setMenuIdList(List<Long> menuIdList) {
        this.menuIdList = menuIdList;
    }

    @Override
    public String toString() {
        return "RoleUpdateReqDTO{" +
                "roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                ", remark='" + remark + '\'' +
                ", menuIdList=" + menuIdList +
                '}';
    }
}
