package com.cmpay.lemon.monitor.bo;

import java.time.LocalDateTime;

/**
 * 描述
 *
 * @author : 曾益
 * @date : 2018/11/12
 */
public class RolePageBO {
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
        return "RolePageBO{" +
                "roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                ", remark='" + remark + '\'' +
                ", createUserId=" + createUserId +
                ", createTime=" + createTime +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }
}
