package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.response.GenericRspDTO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 描述
 *
 * @author : 曾益
 * @date : 2018/11/13
 */
public class RoleInfoRspDTO extends GenericRspDTO {
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
    /**
     * @Fields createUserId 创建者ID
     */
    private Long createUserId;
    /**
     * @Fields createTime 创建时间
     */
    private LocalDateTime createTime;

    private List<Long> menuIds;

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

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public List<Long> getMenuIds() {
        return menuIds;
    }

    public void setMenuIds(List<Long> menuIds) {
        this.menuIds = menuIds;
    }

    @Override
    public String toString() {
        return "RoleInfoRspDTO{" +
                "roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                ", remark='" + remark + '\'' +
                ", createUserId=" + createUserId +
                ", createTime=" + createTime +
                ", menuIds=" + menuIds +
                '}';
    }
}
