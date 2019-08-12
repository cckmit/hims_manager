package com.cmpay.lemon.monitor.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 描述
 *
 * @author : 曾益
 * @date : 2018/11/12
 */
public class RoleAddReqDTO {
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
    /**
     * 菜单ID
     */
    private List<Long> menuIdList;

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

    public List<Long> getMenuIdList() {
        return menuIdList;
    }

    public void setMenuIdList(List<Long> menuIdList) {
        this.menuIdList = menuIdList;
    }
}
