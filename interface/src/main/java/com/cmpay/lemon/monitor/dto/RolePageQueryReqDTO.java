package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.request.GenericDTO;

/**
 * 描述
 *
 * @author : 曾益
 * @date : 2018/11/12
 */
public class RolePageQueryReqDTO extends GenericDTO {
    private Long roleId;
    private String roleName;
    private Integer pageNum;
    private Integer pageSize;


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

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "RolePageQueryReqDTO{" +
                "roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }
}
